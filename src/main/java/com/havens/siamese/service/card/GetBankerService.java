package com.havens.siamese.service.card;

import com.havens.siamese.entity.helper.DeskHelper;
import com.havens.siamese.message.Message;
import com.havens.siamese.service.UserService;
import org.json.JSONObject;

/**
 * Created by havens on 16-6-14.
 */
public class GetBankerService  extends UserService {
    @Override
    public void filter(JSONObject jObject) throws Exception {
        write(DeskHelper.get_banker("get_banker",worldManager.getDesk(user.deskId).bankerUserId));
    }
}
