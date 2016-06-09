package com.havens.siamese.service;

import com.havens.siamese.Service;
import com.havens.siamese.message.MessageHelper;
import org.json.JSONObject;

/**
 * Created by havens on 15-8-11.
 */
public class TimeCheckService extends Service {

    @Override
    public void filter(JSONObject jObject) throws Exception {
        write(MessageHelper.time_check());
    }
}
