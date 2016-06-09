package com.havens.siamese.server;

import com.havens.siamese.Constants;
import com.havens.siamese.Controller.UserController;
import com.havens.siamese.db.DBException;
import com.havens.siamese.entity.User;
import com.havens.siamese.entity.dao.DBFactory;
import com.havens.siamese.entity.dao.cache.DBFactoryCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Created by havens on 15-8-13.
 */
public class WorldManager {
    public static String User_DB;  //UserDB
    public static int SERVER_ID;  //serverId

    private DBFactory dbFactory;
    private static WorldManager god;
    Server server;

    private WorldManager(Server server){
        this.server=server;
        dbFactory = new DBFactoryCache();
        dbFactory.init();
    }

    private void buildUpTheWorld() {
        System.err.println("===== BUILD UP THE WORLD ======");
    }

    public static WorldManager getInstance(Server server) {
        if (god == null) {
            god = new WorldManager(server);
            god.buildUpTheWorld();
        }
        return god;
    }

    public static WorldManager getInstance() {
        return god;
    }

    private UserController getUserController(int userId) {
        User user = new User();
        user.id = userId;
        UserController userCtrl = new UserController(user);
        userCtrl.initDAO(dbFactory);
        try {
            userCtrl.roles(userCtrl.roleDao().getRoles(userId));
        } catch (DBException e) {
            e.printStackTrace();
        }
        return userCtrl;
    }

    public DBFactory dbFactory() {
        return this.dbFactory;
    }

    private LoadingCache<Integer, UserController> onlineUser = CacheBuilder.newBuilder()
            .maximumSize(Constants.MAX_CACHE_ONLINE_USER_SIZE)
            .build(new CacheLoader<Integer, UserController>() {
                @Override
                public UserController load(Integer userId) throws Exception {
                    return getUserController(userId);
                }
            });

    public LoadingCache<Integer, UserController> onlineUser() {
        return this.onlineUser;
    }
}
