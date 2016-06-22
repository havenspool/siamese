package com.havens.siamese.entity.helper;

import com.havens.siamese.Constants;
import com.havens.siamese.entity.Desk;
import com.havens.siamese.entity.User;
import com.havens.siamese.message.Message;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by havens on 16-6-14.
 */
public class DeskHelper {


    public static int doBanker(ConcurrentHashMap<Integer, User> users){
        boolean no=true;
        int[] bankers=new int[users.size()];
        int i=0;
        for(User user:users.values()){
            if(user.banker==1){
                bankers[i++]=user.id;
                no=false;
            }
        }

        if(no){
            for(User user:users.values()){
                bankers[i++]=user.id;
            }
        }

        int num = (int) (Math.random() * i);
        return bankers[num];
    }


    public static int getBetCoin(int type){
        switch(type){
            case 1:return Constants.BET_TIMES;
            case 2:return Constants.BET_COINS;
            default:return Constants.BET_TIMES;
        }
    }

    public static String desk_info(String cmd, Desk desk) {
        JSONObject jObject=new JSONObject();
        jObject.put("cmd", cmd);
        jObject.put("isSuccess", true);
        jObject.put("erroeCode", 0);
        jObject.put("playerInfoList", playerInfoList(desk));
        return  Message.newInstance(cmd,jObject);
    }

    public static JSONArray playerInfoList(Desk desk) {
        JSONArray playerInfoList = new JSONArray();
        if(desk!=null&&desk.users!=null&&desk.users.size()>0){
            for(User user:desk.users.values()){
                JSONObject map=new JSONObject();
                if(user!=null){
                    map.put("userId",user.id);
                    map.put("userName",user.name);
                    map.put("coin",user.coin);
                    map.put("position",user.position);
                    map.put("banker",user.banker);
                    map.put("betCoin",user.betCoin);
                    map.put("bets",user.bets);
                    if(user.cards!=null&&user.cards.length>0){
                        map.put("cards",Arrays.toString(user.cards));
                    }
                    playerInfoList.put(map);
                }
            }
        }
        return  playerInfoList;
    }

    public static String get_banker(String cmd, int bankerUserId) {
        JSONObject jObject=new JSONObject();
        jObject.put("cmd", cmd);
        jObject.put("isSuccess", true);
        jObject.put("erroeCode", 0);
        jObject.put("bankerUserId", bankerUserId);
        return  Message.newInstance(cmd,jObject);
    }

    public static String open_card(String cmd, Desk desk) {
        JSONObject jObject=new JSONObject();
        jObject.put("cmd", cmd);
        jObject.put("isSuccess", true);
        jObject.put("erroeCode", 0);
        if(desk!=null)
            jObject.put("curTime", desk.time);
        else
            jObject.put("curTime", 0);
        jObject.put("playerInfoList", DeskHelper.playerInfoList(desk));
        return  Message.newInstance(cmd,jObject);
    }
}
