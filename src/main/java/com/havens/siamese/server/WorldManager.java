package com.havens.siamese.server;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.havens.siamese.Constants;
import com.havens.siamese.Controller.UserController;
import com.havens.siamese.entity.Desk;
import com.havens.siamese.entity.User;
import com.havens.siamese.entity.dao.DBFactory;
import com.havens.siamese.entity.dao.cache.DBFactoryCache;
import com.havens.siamese.entity.helper.CardHelper;
import com.havens.siamese.entity.helper.DeskHelper;
import com.havens.siamese.job.DeskJob;
import com.havens.siamese.utils.HttpRequestHelper;

import java.util.concurrent.*;

/**
 * Created by havens on 15-8-13.
 */
public class WorldManager {
    public static String User_DB;  //UserDB
    public static int SERVER_ID;  //serverId
    public static String SERVER_NAME;  //serverName
    public static int MINCOST;//房间最低消费
    public static int CONDITION;//房间条件
    public static int ROOMMAXNUM;//房间最大人数
    public static String HOST;
    public static int PORT;
    public static String APP_HOME;

    private DBFactory dbFactory;
    private static WorldManager god;
    Server server;
    private ConcurrentHashMap<Integer, Desk> allDesks; //牌桌

    // thread pool
    private ThreadPoolExecutor dataJobMgr;
    // thread pool
//    private ThreadPoolExecutor udpJobMgr;
    private ScheduledExecutorService scheduledJobMgr;

    private DeskJob deskJob;

    private WorldManager(Server server){
        this.server=server;
        dbFactory = new DBFactoryCache();
        dbFactory.init();

        allDesks = new ConcurrentHashMap<Integer, Desk>();

        //dataJobMgr = (ThreadPoolExecutor) Executors.newFixedThreadPool(loneWolf.poolThreads);
        dataJobMgr = new ThreadPoolExecutor(0, 1000,10L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
        // Set a default reject handler
        dataJobMgr.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        scheduledJobMgr = Executors.newScheduledThreadPool(8);

        deskJob=new DeskJob();

        scheduledJobMgr.scheduleAtFixedRate(deskJob, 60, 5, TimeUnit.SECONDS);
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
        UserController userCtrl = new UserController();
        userCtrl.initDAO(dbFactory);
        User user= userCtrl.userDao().getUser(userId);
        if(user==null){
            user=new User();
        }
        user.coin=10000;
        userCtrl.setUser(user);
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

    public void broadcast(int userId,String json){
        UserController userCtrl=onlineUser().getUnchecked(userId);
        if(userCtrl!=null&&userCtrl.user.id!=0){
            userCtrl.channel.write(json);
        }
    }

    public final Object DESK_LOCK = new Object();

    public Desk getDesk(int deskId) {
        return allDesks.get(deskId);
    }

    public void joinDesk(User user,int deskId) {
        boolean isJoin=false;
        synchronized (DESK_LOCK){
            for (Desk desk:allDesks.values()){
                if(!desk.isFull()&&desk.state!=Constants.DESK_BET&&desk.deskId!=deskId){
                    user.position=desk.users.size();
                    user.deskId=desk.deskId;
                    if(desk.users==null) desk.users=new ConcurrentHashMap<Integer, User>();
                    desk.users.put(user.id,user);

                    if(desk.users.size()==2){
                        //进入选装
                        desk.state=Constants.DESK_GETBANKER;
                        for(User tmp:desk.users.values()){
                            broadcast(tmp.id, DeskHelper.desk_info("desk_info",desk));
                        }
                        desk.time=System.currentTimeMillis()/1000;
                        deskJob.addBanker(deskId);
                    }
                    isJoin=true;
                    break;
                }
            }
            if(!isJoin){
                Desk desk=new Desk();
                desk.deskId=allDesks.size()+1;
                desk.state=Constants.DESK_READY;
                desk.users=new ConcurrentHashMap<Integer, User>();
                desk.cards=new ConcurrentHashMap<Integer, int[]>();
                user.deskId=desk.deskId;
                user.position=desk.users.size();
                desk.users.put(user.id,user);
                allDesks.put(desk.deskId,desk);
            }
        }
    }

    public void deskBanker(int deskId) {
        synchronized (DESK_LOCK) {
            Desk desk = allDesks.get(deskId);
            if (desk != null&&desk.state == Constants.DESK_GETBANKER) {
                //选庄
                long curTime=System.currentTimeMillis()/1000;
                if(curTime-desk.time<5){
                    deskJob.addBet(deskId);
                    return;
                }

                desk.bankerUserId=DeskHelper.doBanker(desk.users);
                for(User tmp:desk.users.values()){
                    tmp.banker=0;
                    broadcast(tmp.id, DeskHelper.get_banker("get_banker",desk.bankerUserId));
                }
                desk.state = Constants.DESK_BET;
                desk.time=System.currentTimeMillis()/1000;
                deskJob.addBet(deskId);
            }
        }
    }


    public void outDesk(User user) {
        synchronized (DESK_LOCK) {
            Desk desk = allDesks.get(user.deskId);
            if (desk != null) {
                if(desk.state != Constants.DESK_READY){
                    //扣除银币
                    minusCoin(user,Constants.MINUS_COINS_TYPE_EXITROOM,Constants.MINUS_COINS_AMOUNT_EXITROOM);
                }
                desk.users.remove(user.id);
            }
        }
    }

    public void minusCoin(User user,int type,int coin){
        user.coin-=coin;
    }


    public void outDesk(int userId) {
        synchronized (DESK_LOCK) {
            for (Desk desk:allDesks.values()){
                if (desk != null&&desk.users!=null&&desk.users.size()>0) {
                    User user=desk.users.get(userId);
                    if(user!=null){
                        if(desk.state != Constants.DESK_READY){
                            //扣除银币
                            minusCoin(user,Constants.MINUS_COINS_TYPE_EXITROOM,Constants.MINUS_COINS_AMOUNT_EXITROOM);
                        }
                        desk.users.remove(userId);

                        if(desk.users.size()==0) {
                            allDesks.remove(desk.deskId);
                        }
                        break;
                    }
                }
            }
        }
    }

    public void newJoinDesk(User user) {
        joinDesk(user,0);
    }

    public void changeDesk(User user) {
        int deskId=user.deskId;
        outDesk(user);
        joinDesk(user,deskId);
    }

    public void readyNext(int deskId) {
        synchronized (DESK_LOCK) {
            Desk desk = allDesks.get(deskId);
            if (desk != null&&desk.state==Constants.DESK_BET) {
                desk.state = Constants.DESK_READY;
            }
        }
    }

    public void doBanker(User user) {
        synchronized (DESK_LOCK) {
            Desk desk = allDesks.get(user.deskId);
            if (desk != null && desk.state == Constants.DESK_READY) {
                User tmp=desk.users.get(user.id);
                if(tmp!=null){
                    tmp.banker=1;
                }
            }
        }
    }

    public boolean bet(User user,int betCoin) {
        boolean isBet=false;
        synchronized (DESK_LOCK) {
            Desk desk = allDesks.get(user.deskId);
            if (desk != null) {
                if(desk.state == Constants.DESK_BET&&desk.users!=null&&desk.users.size()>0){
                    User tmp=desk.users.get(user.id);
                    if(tmp!=null){
                        tmp.betCoin=betCoin;
                        isBet=true;
                    }
                }
            }
        }
        return isBet;
    }

    public void openCard(int deskId) {
        synchronized (DESK_LOCK) {
            Desk desk = allDesks.get(deskId);
            if (desk != null) {
                if(desk.state == Constants.DESK_BET&&desk.users!=null&&desk.users.size()>0){
                    //产生牌点数
                    int[] cards=CardHelper.genCard(desk.users.size());
                    int index=0;
                    for(User tmp:desk.users.values()){
                        if(tmp!=null){
                            tmp.cards=new int[3];
                            tmp.cards[0]=cards[index++];
                            tmp.cards[1]=cards[index++];
                            tmp.cards[2]=cards[index++];
                            if(desk.cards==null) desk.cards=new ConcurrentHashMap<Integer, int[]>();
                            desk.cards.put(tmp.id,tmp.cards);
                        }
                    }
                    //判断输赢
                    int winUserId=0;
                    int[] cards1=null;
                    for(int userId:desk.cards.keySet()){
                        int[] cards2=desk.cards.get(userId);
                        if(cards1==null){
                            cards1=cards2;
                            winUserId=userId;
                            continue;
                        }
                        //cards1>cards2 true
                        if(CardHelper.equalCards(cards1,cards2)) winUserId=userId;

                    }
                    desk.winUserId=winUserId;
                    desk.state = Constants.DESK_READY;
                    for(User tmp:desk.users.values()){
                        if(tmp.id!=winUserId) {
                            //扣除银币
                            minusCoin(tmp, Constants.MINUS_COINS_TYPE_BET, Constants.BET_COINS);
                        }

                        broadcast(tmp.id, DeskHelper.open_card("open_card",desk));
                    }
                }
            }
        }
    }

}
