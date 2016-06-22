package com.havens.siamese.service.desk;

import com.havens.siamese.entity.helper.DeskHelper;
import com.havens.siamese.service.UserService;
import org.json.JSONObject;

/**
 * Created by havens on 16-6-14.
 */
public class DeskInfoService extends UserService {
    @Override
    public void filter(JSONObject jObject) throws Exception {
        write(DeskHelper.desk_info("desk_info",worldManager.getDesk(user.deskId)));
    }
}
