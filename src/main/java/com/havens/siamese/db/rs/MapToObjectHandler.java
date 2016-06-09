package com.havens.siamese.db.rs;

/**
 * Created by havens on 15-8-12.
 */
public interface MapToObjectHandler<T> {

    boolean handler(T o);
}
