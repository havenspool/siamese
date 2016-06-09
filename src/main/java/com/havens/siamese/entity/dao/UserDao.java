package com.havens.siamese.entity.dao;

import com.havens.siamese.db.DBException;
import com.havens.siamese.entity.User;

/**
 * Created by havens on 15-8-13.
 */
public abstract interface UserDao {
    User getUser(int id);
    User getUser(String name);
    void insertUser(User user) throws DBException;
    void updateUser(User user) throws DBException;
    void deleteUser(User user) throws DBException;
    void deleteUserById(int id) throws DBException;
}
