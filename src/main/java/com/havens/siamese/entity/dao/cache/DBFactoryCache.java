package com.havens.siamese.entity.dao.cache;

import com.havens.siamese.entity.dao.DBFactory;
import com.havens.siamese.entity.dao.RoleDao;
import com.havens.siamese.entity.dao.UserDao;

/**
 * Created by havens on 15-8-13.
 */
public class DBFactoryCache implements DBFactory {
    public void init(){
//        userDao().init();
    }

    public UserDao userDao() {
        return new UserCacheDao();
    }

    public RoleDao roleDao() {
        return new RoleCacheDao();
    }
}