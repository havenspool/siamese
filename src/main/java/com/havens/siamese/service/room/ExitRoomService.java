package com.havens.siamese.service.room;

import com.havens.siamese.ErrorCode;
import com.havens.siamese.message.MessageHelper;
import com.havens.siamese.server.WorldManager;
import com.havens.siamese.service.UserService;
import com.havens.siamese.utils.StringHelper;
import org.json.JSONObject;

/**
 * Created by havens on 16-6-11.
 */
public class ExitRoomService extends UserService {
    @Override
    public void filter(JSONObject jObject) throws Exception {
        int roomId= StringHelper.getInt(jObject,"roomId");

        if(roomId!= WorldManager.SERVER_ID){
            write(MessageHelper.cmd_error("exit_room", false, ErrorCode.ROOM_NOT_EXISTS));
            return;
        }

        worldManager.outDesk(user);
        write(MessageHelper.cmd_error("exit_room", true, 0));
    }
}
