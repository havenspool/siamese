package com.havens.siamese.utils;

import org.json.JSONObject;

/**
 * Created by havens on 16-6-2.
 */
public class StringHelper {

    public static String getString(JSONObject jObject,String str){
        if(jObject.opt(str)!=null)
            return (String) jObject.get(str);

        return "";
    }

    public static long getLong(JSONObject jObject,String str){
        if(jObject.opt(str)!=null)
            return (Long) jObject.get(str);
        return 0L;
    }

    public static int getInt(JSONObject jObject,String str){
        if(jObject.opt(str)!=null)
            return (Integer) jObject.get(str);
        return 0;
    }

    public static boolean getBoolean(JSONObject jObject,String str){
        if(jObject.opt(str)!=null)
            return (Boolean) jObject.get(str);
        return false;
    }
}
