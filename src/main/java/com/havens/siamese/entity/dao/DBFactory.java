package com.havens.siamese.entity.dao;

/**
 * Created by havens on 15-8-13.
 */
public abstract interface DBFactory {
    public abstract void init();
    public abstract UserDao userDao();
    public abstract RoleDao roleDao();
}
