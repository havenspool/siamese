package com.havens.siamese.message;

import org.json.JSONObject;

/**
 * Created by havens on 15-8-11.
 */
public class MessageHelper {

    public static String cmd_error(String cmd, boolean isSuccess, int erroeCode) {
        JSONObject jObject=new JSONObject();
        jObject.put("cmd", cmd);
        jObject.put("isSuccess", isSuccess);
        jObject.put("erroeCode", erroeCode);
        return  Message.newInstance(cmd,jObject);
    }

    public static String time_check(){
        JSONObject jObject=new JSONObject();
        jObject.put("cmd", "time_check");
        jObject.put("ctime", System.currentTimeMillis()/1000);
        return  Message.newInstance("time_check",jObject);
    }
}
