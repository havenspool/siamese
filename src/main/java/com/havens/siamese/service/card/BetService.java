package com.havens.siamese.service.card;

import com.havens.siamese.ErrorCode;
import com.havens.siamese.message.MessageHelper;
import com.havens.siamese.service.UserService;
import com.havens.siamese.utils.StringHelper;
import org.json.JSONObject;

/**
 * Created by havens on 16-6-14.
 */
public class BetService extends UserService {
    @Override
    public void filter(JSONObject jObject) throws Exception {
        int betCoin= StringHelper.getInt(jObject,"betCoin");
        if(betCoin<=0){
            write(MessageHelper.cmd_error("bet", false, ErrorCode.BET_COIN_ERROR));
            return;
        }

        if(!worldManager.bet(user,betCoin)){
            write(MessageHelper.cmd_error("bet", false, ErrorCode.BET_COIN_CANNOT));
            return;
        }
        write(MessageHelper.cmd_error("bet", true, 0));
    }
}
