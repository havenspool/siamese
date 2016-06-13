package com.havens.siamese.service.card;

import com.havens.siamese.entity.Desk;
import com.havens.siamese.entity.helper.DeskHelper;
import com.havens.siamese.message.Message;
import com.havens.siamese.service.UserService;
import org.json.JSONObject;

/**
 * Created by havens on 16-6-14.
 */
public class OpenCardService extends UserService {
    @Override
    public void filter(JSONObject jObject) throws Exception {
        worldManager.openCard(user);
        write(open_card("open_card",worldManager.getDesk(user.deskId)));
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
