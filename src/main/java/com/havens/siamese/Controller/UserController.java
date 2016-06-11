package com.havens.siamese.Controller;

import com.havens.siamese.entity.User;
import com.havens.siamese.entity.dao.DBFactory;
import com.havens.siamese.entity.dao.UserDao;

/**
 * Created by havens on 15-8-13.
 */
public class UserController {
    public User user;

    public UserController(User user) {
        this.user = user;
    }

    private DBFactory dbFactory;
    private UserDao userDao = null;

    public void initDAO(DBFactory dbFactory) {
        this.dbFactory = dbFactory;
    }

    public UserDao userDao() {
        if (userDao == null) {
            userDao = dbFactory.userDao();
        }
        return userDao;
    }

}
