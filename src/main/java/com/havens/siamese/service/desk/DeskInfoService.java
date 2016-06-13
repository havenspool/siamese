package com.havens.siamese.service.desk;

import com.havens.siamese.entity.Desk;
import com.havens.siamese.entity.helper.DeskHelper;
import com.havens.siamese.message.Message;
import com.havens.siamese.service.UserService;
import org.json.JSONObject;

/**
 * Created by havens on 16-6-14.
 */
public class DeskInfoService extends UserService {
    @Override
    public void filter(JSONObject jObject) throws Exception {
        write(desk_info("desk_info",worldManager.getDesk(user.deskId)));
    }

    public static String desk_info(String cmd, Desk desk) {
        JSONObject jObject=new JSONObject();
        jObject.put("cmd", cmd);
        jObject.put("isSuccess", true);
        jObject.put("erroeCode", 0);
        jObject.put("playerInfoList", DeskHelper.playerInfoList(desk));
        return  Message.newInstance(cmd,jObject);
    }
}
