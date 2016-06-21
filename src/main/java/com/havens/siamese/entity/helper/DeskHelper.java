package com.havens.siamese.entity.helper;

import com.havens.siamese.Constants;
import com.havens.siamese.entity.Desk;
import com.havens.siamese.entity.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by havens on 16-6-14.
 */
public class DeskHelper {

    public static int getBetCoin(int type){
        switch(type){
            case 1:return Constants.BET_TIMES;
            case 2:return Constants.BET_COINS;
            default:return Constants.BET_TIMES;
        }
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
}
