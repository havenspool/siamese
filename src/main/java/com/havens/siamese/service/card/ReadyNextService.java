package com.havens.siamese.service.card;

import com.havens.siamese.message.MessageHelper;
import com.havens.siamese.service.UserService;
import org.json.JSONObject;

/**
 * Created by havens on 16-6-14.
 */
public class ReadyNextService extends UserService {
    @Override
    public void filter(JSONObject jObject) throws Exception {
        worldManager.readyNext(user.deskId);
        write(MessageHelper.cmd_error("ready_next", true, 0));
    }
}