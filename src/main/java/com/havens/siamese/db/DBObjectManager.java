/**
 * (c) 2014 Copyright
 * Created by hank on 14-6-17.
 */
package com.havens.siamese.db;

import com.havens.siamese.db.conf.DBConfig;
import com.havens.siamese.db.conf.DataSourceConf;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.dbutils.QueryRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * some db object generate information helper class
 * <p/>
 * there are some cache in the class;
 *
 * @Created by havens on 15-8-12.
 */
public final class DBObjectManager {

    private static final String TABLE_SUFFIX_ALL_NO_INCR = "_all_no_incr";
    private static final String TABLE_SUFFIX_NO_KEY = "_no_key";
    private static final String TABLE_SUFFIX_ALL = "_all";
    private static final String TABLE_SUFFIX_KEY = "_key";
    private static final String TABLE_SUFFIX_JSON_ATTR = "_json_attr";
    private static final String TABLE_SUFFIX_JSON_KEY = "_json_key";

    private static final String OBJECT_ATTR_SPLIT_KEY = ":";
    private static final String OBJECT_ATTR_EXCLUDE_SPLIT_KEY = ",";


    private static Map<String, Set<String>> ObjectColumnsCache = new ConcurrentHashMap<String, Set<String>>();

    private static Map<String, Class> ObjectTable = new ConcurrentHashMap<String, Class>();
    private static Map<Class, String> ObjectCache = new ConcurrentHashMap<Class, String>();
    private static Map<Class, Map<String, Field>> ObjectFieldCache = new ConcurrentHashMap<Class, Map<String, Field>>();

    private static Map<String, String> TableCacheKeyFields = new ConcurrentHashMap<String, String>();

    private static Map<String, String> SQLINSERTCACHE = new ConcurrentHashMap<String, String>();
    private static Map<String, String> SQLUPDATECACHE = new ConcurrentHashMap<String, String>();
    private static Map<String, String> SQLDELETECACHE = new ConcurrentHashMap<String, String>();

    private static Map<String, Field> TableInsertIncrKeyField = new ConcurrentHashMap<String, Field>();

    public static IdGenerator idGenerator;

    private static MessageDigest md;
    private final static Object MD5LOCK = new Object();

    private DBObjectManager(){}

    static {
        init(DBManager.DB_CONFIG);
    }

    /**
     * 注册数据库的一些配置属性
     */
    private static void init(DBConfig config) {
        try {
            md = MessageDigest.getInstance("MD5");
            idGenerator = new DefaultIdGenerator();
            for (String key : config.dataSources.keySet()) {
                String[] spilts=key.split(KeyWords._DB_);
                if(spilts.length>=2){
                    DataSourceConf df = config.dataSources.get(key);
                    DBObjectManager.registerTables(DataSourceManager.getQueryRunner(spilts[0],Integer.parseInt(spilts[1])), df);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void registerTables(QueryRunner queryRunner,
                                       DataSourceConf config) throws SQLException {

        // 获取数据源table相关的columns(类的属性与之相关)
        String key;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = queryRunner.getDataSource().getConnection();
            for (String table : config.tableToClass.keySet()) {
                try {
                    String incrKey = null;
                    Set<String> keys = new HashSet<String>();
                    Set<String> no_incr_key_columns = new HashSet<String>();
                    Set<String> no_key_columns = new HashSet<String>();
                    Set<String> all_columns = new HashSet<String>();

                    rs = con.getMetaData().getPrimaryKeys(null, null, table);
                    while (rs.next()) { // column name in the NO. 4
                        key = rs.getString(4);   //rs.getString("COLUMN_NAME");
                        keys.add(key);
                    }
                    rs.close();

                    rs = con.getMetaData().getColumns(null, null, table, null);
                    while (rs.next()) { // column name in the NO. 4
                        String name = rs.getString(4); //rs.getString("COLUMN_NAME");
                        all_columns.add(name);
                        if (keys.contains(name)) {
                            if (!"YES".equals(rs.getString("IS_AUTOINCREMENT"))) {
                                no_incr_key_columns.add(name);
                            } else {
                                incrKey = name;
                            }
//                    System.out.println(rs.getString("IS_AUTOINCREMENT")); "YES"
                            // nothing
                        } else {
                            no_incr_key_columns.add(name);
                            no_key_columns.add(name);
                        }
                    }

                    // 所有键值除了自增长的关键值
                    ObjectColumnsCache.put(table + TABLE_SUFFIX_ALL_NO_INCR, no_incr_key_columns);
                    // 所有键值除了关键值
                    ObjectColumnsCache.put(table + TABLE_SUFFIX_NO_KEY, no_key_columns);
                    // 所有键值
                    ObjectColumnsCache.put(table + TABLE_SUFFIX_ALL, all_columns);
                    // 所有关键值
                    ObjectColumnsCache.put(table + TABLE_SUFFIX_KEY, keys);

                    String className = config.tableToClass.get(table);

                    Class clazz = Class.forName(className);

                    // 缓存table类的属性
                    registerTableClassField(clazz);

                    Set<String> excludes = new HashSet<String>();
                    String[] sss = config.tableExcludes.get(table).split(OBJECT_ATTR_EXCLUDE_SPLIT_KEY);
                    Collections.addAll(excludes, sss);

                    no_key_columns.removeAll(excludes); // remove update attribute

                    // must register json attribute and then json register key
                    DBObjectManager.registerJSONAttr(table, all_columns.toArray(new String[all_columns.size()]));
                    DBObjectManager.registerJSONKey(table, keys.toArray(new String[keys.size()]));
                    DBObjectManager.registerTableClass(table, clazz);

                    // 缓存table自增长的关键值
                    if (incrKey != null) {
                        Field field = ObjectFieldCache.get(clazz).get(incrKey);
                        if (field != null) {
                            TableInsertIncrKeyField.put(table, field);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (rs != null)
                rs.close();
//            DbUtils.close(con);
        }

        // 缓存table相关的sql语句
        for (String table : config.tableToClass.keySet()) {
            SQLINSERTCACHE.put(table, makeInsertSQL(table));
            SQLUPDATECACHE.put(table, makeUpdateSQL(table));
            SQLDELETECACHE.put(table, makeDeleteSQL(table));
        }

        // 缓存table相关的类
        for (String className : config.classToTable.keySet()) {
            try {
                Class clazz = Class.forName(className);
                String table = config.classToTable.get(className);
                setTableNameByClass(clazz, table);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        // 缓存cache key
        // redis 缓存结构key:　$table_name_$cache_key_field_(upd|del|ins)_*
        // 默认system
        TableCacheKeyFields.putAll(config.tableToCacheKeyField);
    }

    private static void registerTableClassField(Class clazz) {
        if (ObjectFieldCache.get(clazz) == null) {
            ImmutableMap.Builder<String, Field> classMap = ImmutableMap.builder();
            Class uper = clazz;
            while (uper != null) {
                for (Field field : uper.getDeclaredFields()) {
                    if (Modifier.isFinal(field.getModifiers())
                            || Modifier.isStatic(field.getModifiers())
                            || !Modifier.isPublic(field.getModifiers())
                            || field.getType().isArray())
                        continue;
                    classMap.put(field.getName(), field);
                }
                uper = uper.getSuperclass();
            }
            ObjectFieldCache.put(clazz, classMap.build());
        }
    }

    protected static void registerTableClass(String table_name, Class clazz) {
        ObjectTable.put(table_name, clazz);
    }

    public static Class getClassByTable(String table_name) {
        return ObjectTable.get(table_name);
    }

    public static Field getInsertIncrKeyField(String table_name) {
        return TableInsertIncrKeyField.get(table_name);
    }

    /**
     * 注册 object 对应的 key 名字，生成 $table_name:$key1:$key2
     *
     * @param table_name
     * @param attrs
     */
    protected static void registerJSONKey(String table_name, String attrs) {
        registerJSONKey(table_name, attrs.split(OBJECT_ATTR_SPLIT_KEY));
    }

    /**
     * @param table_name
     * @param attrs      String...
     */
    protected static void registerJSONKey(String table_name, String... attrs) {
        Set<String> keyString = ObjectColumnsCache.get(table_name + TABLE_SUFFIX_JSON_KEY);
        if (keyString == null) {
            keyString = Collections.synchronizedSet(new HashSet<String>());
            ObjectColumnsCache.put(table_name + TABLE_SUFFIX_JSON_KEY, keyString);
        }
        Set<String> attrString = ObjectColumnsCache.get(table_name + TABLE_SUFFIX_JSON_ATTR);
        if (attrString == null) {
            attrString = Collections.synchronizedSet(new HashSet<String>());
            ObjectColumnsCache.put(table_name + TABLE_SUFFIX_JSON_ATTR, attrString);
        }
        Collections.addAll(keyString, attrs);
        Collections.addAll(attrString, attrs);
    }

    /**
     * 注册 object 对应的json 字符 生成 {$attr1:$value1,$attr2:value2.....}
     *
     * @param table_name
     * @param attrs      "dungeonId:times:reset:day:star:bide"
     */
    protected static void registerJSONAttr(String table_name, String attrs) {
        registerJSONAttr(table_name, attrs.split(OBJECT_ATTR_SPLIT_KEY));
    }

    protected static void registerJSONAttr(String table_name, String... attrs) {
        Set<String> attrString = ObjectColumnsCache.get(table_name + TABLE_SUFFIX_JSON_ATTR);
        if (attrString == null) {
            attrString = Collections.synchronizedSet(new HashSet<String>());
            ObjectColumnsCache.put(table_name + TABLE_SUFFIX_JSON_ATTR, attrString);
        }
        Collections.addAll(attrString, attrs);
//        ObjectColumnsCache.put(table_name + TABLE_SUFFIX_JSON_ATTR, Collections.synchronizedSet(attrString));
    }

    public static Set<String> getObjectJSONKeys(String table_name) {
        return ObjectColumnsCache.get(table_name + TABLE_SUFFIX_JSON_KEY);
    }

    public static Set<String> getObjectJSONAttr(String table_name) {
        return ObjectColumnsCache.get(table_name + TABLE_SUFFIX_JSON_ATTR);
    }

    public static Set<String> getTableAllColumns(String table) {
        return ObjectColumnsCache.get(table + TABLE_SUFFIX_ALL);
    }

    public static Set<String> getTableAllColumnsNoIncr(String table) {
        return ObjectColumnsCache.get(table + TABLE_SUFFIX_ALL_NO_INCR);
    }

    public static Set<String> getTableAllColumnsNoKey(String table) {
        return ObjectColumnsCache.get(table + TABLE_SUFFIX_NO_KEY);
    }

    public static Set<String> getTablePrimaryKey(String table) {
        return ObjectColumnsCache.get(table + TABLE_SUFFIX_KEY);
    }


    public static Map<String, Field> getClazzField(final Class clazz) {
        Map<String, Field> result = ObjectFieldCache.get(clazz);
        if (result == null) {
            registerTableClassField(clazz);
            result = ObjectFieldCache.get(clazz);
        }
        return result;
    }

    /**
     * generate table name by object
     *
     * @param clazz Clazz
     * @return table name
     */
    public static String getTableNameByObject(Class clazz) {
        return ObjectCache.get(clazz);
    }

    protected static void setTableNameByClass(Class clazz, String table_name) {
        ObjectCache.put(clazz, table_name);
    }


    public static String md5(String input) {
        synchronized (MD5LOCK) {
            StringBuilder sb = new StringBuilder();
            try {
                byte[] bytesOfMessage = input.getBytes("UTF-8");

                byte[] thedigest = md.digest(bytesOfMessage);

                for (byte aThedigest : thedigest) {
                    sb.append(Integer.toHexString((aThedigest & 0xFF) | 0x100).substring(1, 3));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return sb.toString();
        }
    }

    public static String getCacheKeyFieldByTable(String table_name) {
        return TableCacheKeyFields.get(table_name);
    }

    /*

    SQL CACHE BUILDER

     */

    public static String getInsertSQLByTable(final String table) {
        return SQLINSERTCACHE.get(table);
    }

    public static String getUpdateSQLByTable(final String table) {
        return SQLUPDATECACHE.get(table);
    }

    public static String getDeleteSQLByTable(final String table) {
        return SQLDELETECACHE.get(table);
    }

    private static String makeInsertSQL(String table) {
        StringBuilder s = new StringBuilder("insert into ").append(table).append("(");
        StringBuilder sv = new StringBuilder(" values(");

        Set<String> columns = DBObjectManager.getTableAllColumnsNoIncr(table); // true means if it is not auto increase then add key's column
        int size = columns.size();
        for (String column : columns) {
            if (--size == 0) {
                // last item
                s.append('`').append(column).append("`)");
                sv.append("?)");
            } else {
                s.append('`').append(column).append("`,");
                sv.append("?,");
            }
        }
        return s.append(sv).toString();
    }

    private static String makeUpdateSQL(String table) {
        Set<String> primary_keys = DBObjectManager.getTablePrimaryKey(table);
        StringBuilder s = new StringBuilder("update ").append(table).append(" set ");
        Set<String> columns = DBObjectManager.getTableAllColumnsNoKey(table);
        int size = columns.size();
        for (String column : columns) {
            if (--size == 0) {
                // last item
                s.append('`').append(column).append("`=? ");
            } else {
                s.append('`').append(column).append("`=?, ");
            }
        }
        s.append(" where ");
        int key_size = primary_keys.size();
        int kye_i = 1;
        for (String primary_key : primary_keys) {
            s.append('`').append(primary_key).append("` = ? ");
            if (kye_i != key_size)
                s.append(" AND ");
            kye_i++;
        }
        return s.toString();
    }

    private static String makeDeleteSQL(String table) {
        Set<String> primary_keys = DBObjectManager.getTablePrimaryKey(table);

        StringBuilder s = new StringBuilder("delete from ");
        s.append(table).append(" where ");

        int key_size = primary_keys.size();
        int kye_i = 1;
        for (String primary_key : primary_keys) {
            s.append('`').append(primary_key).append("` = ? ");
            if (kye_i != key_size)
                s.append(" AND ");
            kye_i++;
        }
        return s.toString();
    }
}
