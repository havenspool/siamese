package com.havens.siamese.service;

import com.havens.siamese.entity.Role;
import com.havens.siamese.message.Message;
import com.havens.siamese.utils.StringHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by havens on 16-5-14.
 */
public class GetRolesService extends UserService{
    @Override
    public void filter(JSONObject jObject) throws Exception {
        int serverId= StringHelper.getInt(jObject,"serverId");
        int userId= StringHelper.getInt(jObject,"userId");
        String roleName= StringHelper.getString(jObject,"roleName");
        System.out.println(roleName);
        write(get_roles("get_roles",userCtrl.getRoles(userId)));
    }

    public static String get_roles(String cmd, List<Role> roles) {
        JSONObject jObject=new JSONObject();
        jObject.put("cmd", cmd);
        jObject.put("isSuccess", true);
        jObject.put("erroeCode", 0);
        JSONArray roleList = new JSONArray();
        for(Role role: roles){
            JSONObject map=new JSONObject();
            if(role!=null){
                map.put("id",role.id);
                map.put("userId",role.userId);
                map.put("roleName",role.roleName);
                map.put("level",role.level);
                map.put("exp",role.exp);
                map.put("gold",role.gold);
                map.put("createTime",role.createTime);
                map.put("loginTime",role.loginTime);
                map.put("consecutiveDays",role.consecutiveDays);
                map.put("roleState",role.roleState);
                map.put("headImage",role.headImage);
                roleList.put(map);
            }
        }
        jObject.put("roleInfo",roleList);
        return  Message.newInstance(cmd,jObject);
    }
}