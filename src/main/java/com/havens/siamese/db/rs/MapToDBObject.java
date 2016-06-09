
package com.havens.siamese.db.rs;

import com.havens.siamese.db.DBObject;

import java.util.Map;

/**
 * Created by havens on 15-8-12.
 */
public class MapToDBObject<T extends DBObject> implements MapToObject<T> {

    private Class<T> clazz;
    private MapToObjectHandler<T> handler;

    public MapToDBObject(Class<T> clazz) {
        this.clazz = clazz;
        this.handler = new BasicHandler<T>();
    }

    public MapToDBObject(Class<T> clazz, MapToObjectHandler<T> handler) {
        this.clazz = clazz;
        this.handler = handler;
    }

    public T toObject(Map<String, Object> map) {
        T obj = null;
        try {
            obj = clazz.newInstance();
            obj.MapToObj(map);
            if (!this.handler.handler(obj)) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static <T extends DBObject> MapToObject<T> newHandler(final Class<T> clazz) {
        return new MapToDBObject<T>(clazz);
    }

    public static <T extends DBObject> MapToDBObjectHandler<T> newRsHandler(final Class<T> clazz) {
        return new MapToDBObjectHandler<T>(clazz);
    }

    public static <T extends DBObject> MapToDBObjectListHandler<T> newListHandler(final Class<T> clazz) {
        return new MapToDBObjectListHandler<T>(clazz);
    }

    public static <T extends DBObject> MapToDBObjectListHandler<T> newListHandler(final Class<T> clazz
            , MapToObjectHandler<T> handler) {
        return new MapToDBObjectListHandler<T>(clazz, handler);
    }

    class BasicHandler<E> implements MapToObjectHandler<E> {
        public boolean handler(E o) {
            return true;
        }
    }
}
