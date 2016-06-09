package com.havens.siamese.db;

import com.havens.siamese.db.conf.CacheConfig;
import com.havens.siamese.db.conf.DBConfig;
import com.havens.siamese.db.conf.DataSourceConf;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by havens on 15-8-12.
 */
public class DBManager {
    private static final String DB_CONF_FILE = "rabbit.xml";
    private static final String DBOBJECT_CONF_FILE = "DBObject.xml";

    public static final DBConfig DB_CONFIG;

    static {
        DB_CONFIG = readConfig();
    }

    // init manager
    // datasource manger
    // DBObject manager
    // cache manager
    private DBManager() {
    }



    /**
     * read rabbit.xml  数据库连接配置文件和缓存配置文件
     * <p/>
     * read DBObject.xml  数据库表与对象相关连的配置文件
     *
     * @return DBConfig
     */
    private static DBConfig readConfig(){
        DBConfig dbConfig = new DBConfig();
        Map dataSources = new HashMap();
        try {
            Document dd = parseXML(DB_CONF_FILE, false);

            NodeList list = dd.getElementsByTagName("datasource");
            String defaultName = null;
            for (int i = 0; i < list.getLength(); i++) {
                Element tE = (Element)list.item(i);
                String name = tE.getAttribute("name");
                String role = tE.getAttribute("role");
                String def = tE.getAttribute("default");
                DataSourceConf df = new DataSourceConf();
                df.name = name;
                df.role = Integer.parseInt(role);
                if ((def != null) && ("true".equals(def))) {
                    df._default = true;
                    defaultName = name;
                }
                NodeList subList = tE.getElementsByTagName("url");
                if (subList.getLength() > 0) {
                    df.url = subList.item(0).getTextContent();
                }

                subList = tE.getElementsByTagName("user");
                if (subList.getLength() > 0) {
                    df.user = subList.item(0).getTextContent();
                }

                subList = tE.getElementsByTagName("password");
                if (subList.getLength() > 0) {
                    df.password = subList.item(0).getTextContent();
                }

                subList = tE.getElementsByTagName("driver");
                if (subList.getLength() > 0) {
                    df.driver = subList.item(0).getTextContent();
                }

                df.tableToClass = new HashMap();
                df.tableExcludes = new HashMap();
                df.classToTable = new HashMap();
                df.tableToCacheKeyField = new HashMap();
                dataSources.put(name + "_db_" + role, df);
            }

            dbConfig.afterCacheClass = null;
            list = dd.getElementsByTagName("afterCacheClass");
            if (list.getLength() > 0) {
                String afterClass = list.item(0).getTextContent();
                if (afterClass.length() > 0) {
                    try {
                        dbConfig.afterCacheClass = Class.forName(afterClass);
                    }
                    catch (Exception localException1)
                    {
                    }
                }
            }

            list = dd.getElementsByTagName("redis");

            if (list.getLength() > 0) {
                Element tE = (Element)list.item(0);
                CacheConfig cacheConfig = new CacheConfig();
                cacheConfig.cache = "redis";
                NodeList subList = dd.getElementsByTagName("key_prefix");
                if (subList.getLength() > 0) {
                    cacheConfig.cachePrefix = subList.item(0).getTextContent();
                }
                subList = tE.getElementsByTagName("sync_time");
                if (subList.getLength() > 0)
                    cacheConfig.syncTime = Long.valueOf(subList.item(0).getTextContent()).longValue();
                else {
                    cacheConfig.syncTime = CacheConfig.DEFAULT.syncTime;
                }

                subList = tE.getElementsByTagName("cluster");
                cacheConfig.cluster = ((subList.getLength() > 0) && ("true".equals(subList.item(0).getTextContent())));

                subList = tE.getElementsByTagName("password");
                if (subList.getLength() > 0)
                    cacheConfig.password = subList.item(0).getTextContent();
                else {
                    cacheConfig.password = null;
                }

                cacheConfig.hosts = new ArrayList();
                subList = tE.getElementsByTagName("host");
                for (int i = 0; i < subList.getLength(); i++) {
                    CacheConfig.Host host = new CacheConfig.Host();
                    Element hostE = (Element)subList.item(i);
                    host.port = Integer.valueOf(hostE.getAttribute("port")).intValue();
                    host.host = hostE.getTextContent();
                    cacheConfig.hosts.add(host);
                }
                dbConfig.cacheConfig = cacheConfig;
            }
            else {
                dbConfig.cacheConfig = CacheConfig.DEFAULT;
            }

            Document objectXML = parseXML(DBOBJECT_CONF_FILE, false);

            list = objectXML.getElementsByTagName("dbobject");

            for (int i = 0; i < list.getLength(); i++) {
                Element tE = (Element)list.item(i);
                String tDatasource = tE.getAttribute("datasource");
                if ((tDatasource == null) || (tDatasource.length() < 1)) {
                    tDatasource = defaultName;
                }
                dataSourceInit((DataSourceConf)dataSources.get(tDatasource + "_db_" + 1), tE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        dbConfig.dataSources = dataSources;
        return dbConfig;
    }

    private static void dataSourceInit(DataSourceConf df, Element tE) throws Exception {
        NodeList subList = tE.getElementsByTagName("class_name");
        if (subList.getLength() > 0) {
            String class_name = subList.item(0).getTextContent();
            NodeList subList2 = tE.getElementsByTagName("table_name");
            Element tableE = (Element)subList2.item(0);
            String tMark = tableE.getAttribute("mark");
            String table_name = tableE.getTextContent();
            df.tableToClass.put(table_name, class_name);
            if ("true".equals(tMark)) {
                df.classToTable.put(class_name, table_name);
            }
            NodeList subList3 = tE.getElementsByTagName("exclude_field");
            if (subList3.getLength() > 0) {
                String exclude_field = subList3.item(0).getTextContent();
                df.tableExcludes.put(table_name, exclude_field);
            } else {
                df.tableExcludes.put(table_name, "");
            }
            NodeList subList4 = tE.getElementsByTagName("cache_key");
            if (subList4.getLength() > 0) {
                String key_field = subList4.item(0).getTextContent();
                df.tableToCacheKeyField.put(table_name, key_field);
            } else {
                df.tableToCacheKeyField.put(table_name, "system");
            }
        }
    }

    private static Document parseXML(String filename,
                                     boolean validating) throws Exception {        // Create a builder factory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(validating);
        // Prevent expansion of entity references
        factory.setExpandEntityReferences(false);
        // Create the builder and parse the file
        InputStream in = getInputStream(filename);
        return factory.newDocumentBuilder().parse(in);
    }

    private static InputStream getInputStream(String filename) {
        ClassLoader cL = Thread.currentThread().getContextClassLoader();
        if (cL == null) {
            cL = DBManager.class.getClassLoader();
        }
        return cL.getResourceAsStream(filename);
    }

    public static void main(String[] args){
        readConfig();
        System.out.println(readConfig());
    }

}
