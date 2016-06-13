package com.havens.siamese.service.card;

import com.havens.siamese.message.Message;
import com.havens.siamese.service.UserService;
import org.json.JSONObject;

/**
 * Created by havens on 16-6-14.
 */
public class GetBankerService  extends UserService {
    @Override
    public void filter(JSONObject jObject) throws Exception {
        write(get_banker("get_banker",worldManager.getDesk(user.deskId).bankerUserId));
    }

    public static String get_banker(String cmd, long bankerUserId) {
        JSONObject jObject=new JSONObject();
        jObject.put("cmd", cmd);
        jObject.put("isSuccess", true);
        jObject.put("erroeCode", 0);
        jObject.put("bankerUserId", bankerUserId);
        return  Message.newInstance(cmd,jObject);
    }
}
