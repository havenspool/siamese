package com.havens.siamese.server;

import com.havens.siamese.Constants;
import com.havens.siamese.Controller.UserController;
import com.havens.siamese.db.DBException;
import com.havens.siamese.entity.Desk;
import com.havens.siamese.entity.User;
import com.havens.siamese.entity.dao.DBFactory;
import com.havens.siamese.entity.dao.cache.DBFactoryCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by havens on 15-8-13.
 */
public class WorldManager {
    public static String User_DB;  //UserDB
    public static int SERVER_ID;  //serverId
    public static int SERVER_NAME;  //serverName
    public static int MINCOST;//房间最低消费
    public static int CONDITION;//房间条件
    public static int ROOMNUM;//房间最大人数

    private DBFactory dbFactory;
    private static WorldManager god;
    Server server;
    private ConcurrentHashMap<Integer, Desk> allDesks; //牌桌

    private WorldManager(Server server){
        this.server=server;
        dbFactory = new DBFactoryCache();
        dbFactory.init();

        allDesks = new ConcurrentHashMap<Integer, Desk>();
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

    public final Object DESK_LOCK = new Object();

    public Desk getDesk(int deskId) {
        return allDesks.get(deskId);
    }

    public void joinDesk(User user,int deskId) {
        boolean isJoin=false;
        synchronized (DESK_LOCK){
            for (Desk desk:allDesks.values()){
                if(!desk.isFuul&&desk.state==Constants.DESK_READY&&desk.deskId!=deskId){
                    user.position=desk.users.size();
                    desk.users.put(user.id,user);
                    isJoin=true;
                }
            }
            if(!isJoin){
                Desk desk=new Desk();
                desk.deskId=allDesks.size()+1;
                desk.state=Constants.DESK_READY;
                desk.users=new ConcurrentHashMap<Long, User>();
                user.position=desk.users.size();
                desk.users.put(user.id,user);
                allDesks.put(desk.deskId,desk);
            }
        }
    }

    public void outDesk(User user) {
        synchronized (DESK_LOCK) {
            Desk desk = allDesks.get(user.deskId);
            if (desk != null && desk.state == Constants.DESK_READY) {
                desk.users.remove(user.id);
            }
        }
    }

    public void newJoinDesk(User user) {
        joinDesk(user,0);
    }

    public void changeDesk(User user) {
        int deskId=user.deskId;
        outDesk(user);
        joinDesk(user,user.deskId);
    }

}
