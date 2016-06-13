package com.havens.siamese.service.card;

import com.havens.siamese.ErrorCode;
import com.havens.siamese.message.MessageHelper;
import com.havens.siamese.service.UserService;
import org.json.JSONObject;

/**
 * Created by havens on 16-6-14.
 */
public class DoBankerService extends UserService {
    @Override
    public void filter(JSONObject jObject) throws Exception {
        worldManager.doBanker(user);
        if(worldManager.getDesk(user.deskId).bankerUserId!=user.id){
            write(MessageHelper.cmd_error("do_banker", false, ErrorCode.BANKER_IS_EXIST));
            return;
        }
        write(MessageHelper.cmd_error("do_banker", true, 0));
    }
}
