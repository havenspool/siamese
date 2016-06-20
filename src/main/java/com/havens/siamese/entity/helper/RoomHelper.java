package com.havens.siamese.entity.helper;

import com.havens.siamese.entity.Desk;
import com.havens.siamese.entity.User;
import com.havens.siamese.server.Server;
import com.havens.siamese.server.WorldManager;
import org.json.JSONObject;

/**
 * Created by havens on 16-6-11.
 */
public class RoomHelper {
    public static JSONObject roomInfo(Desk desk) {
        JSONObject roomInfo=new JSONObject();
        if(desk!=null){
            roomInfo.put("roomId", WorldManager.SERVER_ID);
            roomInfo.put("roomName",WorldManager.SERVER_NAME);
            roomInfo.put("host",WorldManager.HOST);
            roomInfo.put("port", WorldManager.PORT);
            roomInfo.put("deskId",desk.deskId);
            roomInfo.put("minCost",WorldManager.MINCOST);
            roomInfo.put("condition",WorldManager.CONDITION);
            roomInfo.put("onlineNum",WorldManager.getInstance().onlineUser().size());
            roomInfo.put("roomMaxNum",WorldManager.ROOMMAXNUM);
            roomInfo.put("playInfo", DeskHelper.playerInfoList(desk));
        }
        return  roomInfo;
    }
}
