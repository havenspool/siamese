package com.havens.siamese.service.desk;

import com.havens.siamese.ErrorCode;
import com.havens.siamese.entity.Desk;
import com.havens.siamese.entity.helper.RoomHelper;
import com.havens.siamese.message.Message;
import com.havens.siamese.message.MessageHelper;
import com.havens.siamese.server.WorldManager;
import com.havens.siamese.service.UserService;
import com.havens.siamese.utils.StringHelper;
import org.json.JSONObject;

/**
 * Created by havens on 16-6-11.
 */
public class ChangeDeskService extends UserService {
    @Override
    public void filter(JSONObject jObject) throws Exception {
        int roomId= StringHelper.getInt(jObject,"roomId");

        if(roomId!= WorldManager.SERVER_ID){
            write(MessageHelper.cmd_error("change_desk", false, ErrorCode.ROOM_NOT_EXISTS));
            return;
        }

        worldManager.changeDesk(user);
        write(enter_room("change_desk",worldManager.getDesk(user.deskId)));
    }

    public static String enter_room(String cmd, Desk desk) {
        JSONObject jObject=new JSONObject();
        jObject.put("cmd", cmd);
        jObject.put("isSuccess", true);
        jObject.put("erroeCode", 0);
        jObject.put("roomInfo", RoomHelper.roomInfo(desk));
        return  Message.newInstance(cmd,jObject);
    }
}
