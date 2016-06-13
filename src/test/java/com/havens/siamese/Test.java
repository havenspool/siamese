package com.havens.siamese;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by havens on 16-5-23.
 */
public class Test {
    public static String jsonTest() throws JSONException {
        JSONObject json=new JSONObject();
        JSONArray jsonMembers = new JSONArray();
        JSONObject member1 = new JSONObject();
        member1.put("loginname", "zhangfan");
        member1.put("password", "userpass");
        member1.put("email","10371443@qq.com");
        member1.put("sign_date", "2007-06-12");
        jsonMembers.put(member1);

        JSONObject member2 = new JSONObject();
        member2.put("loginname", "zf");
        member2.put("password", "userpass");
        member2.put("email","8223939@qq.com");
        member2.put("sign_date", "2008-07-16");
        jsonMembers.put(member2);
        json.put("users", jsonMembers);
        System.out.println(jsonMembers.toString());
        return json.toString();
    }

    public static void testArray() throws JSONException {
        int[] arr=new int[3];
        arr[0]=1;
        arr[1]=2;
        arr[2]=3;
        System.out.println(Arrays.toString(arr));
    }

    public static void main(String[] args) throws Exception {
//        JSONArray roleList = new JSONArray();
//        roleList.put("1");
//        roleList.put("2");
//        System.out.println(jsonTest());

        testArray();
    }
}
