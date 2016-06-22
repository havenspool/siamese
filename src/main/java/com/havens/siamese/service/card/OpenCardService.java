package com.havens.siamese.service.card;

import com.havens.siamese.entity.helper.DeskHelper;
import com.havens.siamese.service.UserService;
import org.json.JSONObject;

/**
 * Created by havens on 16-6-14.
 */
public class OpenCardService extends UserService {
    @Override
    public void filter(JSONObject jObject) throws Exception {
        write(DeskHelper.open_card("open_card",worldManager.getDesk(user.deskId)));
    }
}
