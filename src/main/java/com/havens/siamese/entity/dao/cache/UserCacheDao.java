package com.havens.siamese.entity.dao.cache;

import com.havens.siamese.db.DBException;
import com.havens.siamese.db.DataSourceManager;
import com.havens.siamese.db.KeyWords;
import com.havens.siamese.db.cache.DBObjectCacheDAO;
import com.havens.siamese.db.rs.MapToDBObjectHandler;
import com.havens.siamese.entity.User;
import com.havens.siamese.entity.dao.UserDao;
import com.havens.siamese.entity.idGenerator.UserIdGenerator;
import com.havens.siamese.server.WorldManager;

import java.sql.SQLException;

/**
 * Created by havens on 15-8-13.
 */
public class UserCacheDao extends DBObjectCacheDAO implements UserDao{

    private static final UserIdGenerator idGenerator = new UserIdGenerator(WorldManager.SERVER_ID);

    public UserCacheDao() {
        super(DataSourceManager.getQueryRunner(WorldManager.User_DB, KeyWords.MASTER),DataSourceManager.getQueryRunner(WorldManager.User_DB,KeyWords.SLAVE));
        this.idGen = idGenerator;
    }

    public User getUser(int id){
        User user=null;
        try {
            user =slaveRunner.query("select * from users where id = ? ",new MapToDBObjectHandler<User>(User.class), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public User getUser(String name){
        User user=null;
        try {
            user = slaveRunner.query("select * from users where name = ? ",new MapToDBObjectHandler<User>(User.class), name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void insertUser(User user) throws DBException {
        insert(user);
    }

    public void updateUser(User user) throws DBException{
        update(user);
    }

    public void deleteUser(User user) throws DBException{
        delete(user);
    }

    public void deleteUserById(int userId) throws DBException{
        delete(getUser(userId));
    }

}
