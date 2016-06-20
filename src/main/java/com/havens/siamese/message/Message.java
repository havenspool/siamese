package com.havens.siamese.message;

import com.havens.siamese.utils.StringHelper;
import io.netty.channel.Channel;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by havens on 15-8-7.
 */
public class Message implements Serializable {
    public int code;
    public String cmd;
    public Channel channel;
    public String dataJson;
    public JSONObject jObject;

    public int userId;

    public Message(){

    }

    public Message(String str){
        JSONObject json = new JSONObject(str);
        if(json.get("cmd")==null)
            return;
        this.cmd=(String)json.get("cmd");
        this.dataJson=str;
        this.jObject=json;

        int userId= StringHelper.getInt(json,"userId");
        if(userId==0) return;
        this.userId=userId;
        this.jObject=json;
    }

    /**
     * 对象转数组
     * @param obj
     * @return
     */
    public static byte[] toByteArray (Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 数组转对象
     * @param bytes
     * @return
     */
    public static Object toObject (byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
            ObjectInputStream ois = new ObjectInputStream (bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    // Json生成Map
    public Map<String, Object> jsonToMap(String jsonString) throws JSONException {
        //JSONObject必须以"{"开头
        JSONObject jsonObject = new JSONObject(jsonString);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Iterator<String> iter = jsonObject.keys();
        String key=null;
        Object value=null;
        while (iter.hasNext()) {
            key=iter.next();
            value=jsonObject.get(key);
            resultMap.put(key, value);
        }
        return resultMap;
    }

//    // Map和类生成Json
//    public String buildJson(Map map){
////        JSONObject jObject=new JSONObject();
////        jObject.put("name", "Edward");
////        jObject.put("name1", "Edward1");
////        System.out.println(jObject.toString());
//        /*
//         * 打印结果：{"name":"Edward","name1":"Edward1"}
//         */
//        //注意JSONObject和JSONArray装载数据的格式
////        JSONArray jmap = new JSONArray();
////        jmap.put(map1);
////        System.out.println("JSONArray对象数据格式：");
////        System.out.println(jmap.toString());
////        /*
////         * JSONArray对象数据格式：[{"sex":"female","name":"Alexia","age":"23"}]
////         */
//        JSONObject jall = new JSONObject();
//        jall.put("map", map1);//Map转换成Json
//        jall.put("employ",jbean );
//        return jall.toString();
//    }

    public static String newInstance(final String cmd,final JSONObject jObject) {
        Message msg = new Message();
        msg.cmd = cmd;
        msg.jObject=jObject;
        msg.dataJson=jObject.toString();
        return msg.dataJson;
    }

    public static void main(String[] args) throws Exception {
        Message msg = new Message();
        msg.cmd = "time_check";
        JSONObject jObject=new JSONObject();
        jObject.put("cmd", "time_check");
        jObject.put("ctime", System.currentTimeMillis()/1000);
        msg.jObject=jObject;
        msg.dataJson=jObject.toString();

        System.out.println(msg.dataJson);

        System.out.println(MessageHelper.time_check());
    }



}
