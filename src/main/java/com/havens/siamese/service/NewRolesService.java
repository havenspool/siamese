package com.havens.siamese.service;

import com.havens.siamese.ErrorCode;
import com.havens.siamese.dfa.DFAOutCallBack;
import com.havens.siamese.dfa.DfaTool;
import com.havens.siamese.entity.Role;
import com.havens.siamese.entity.dao.RoleDao;
import com.havens.siamese.message.MessageHelper;
import com.havens.siamese.server.WorldManager;
import com.havens.siamese.utils.StringHelper;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by havens on 16-5-14.
 */
public class NewRolesService extends UserService{
    public static class MyDFAOutCallBack implements DFAOutCallBack {
        public Set<String> words = new HashSet<String>();
        public void CallBack(String keyword) {
            words.add(keyword);
        }
    }

    @Override
    public void filter(JSONObject jObject) throws Exception {
        int serverId= StringHelper.getInt(jObject,"serverId");
        int userId= StringHelper.getInt(jObject,"userId");

        String roleName= StringHelper.getString(jObject,"roleName");

        int headImage=(Integer) jObject.get("headImage");

        MyDFAOutCallBack myDFAOutCallBack = new MyDFAOutCallBack();
        DfaTool.removeKeyword.Search(roleName, myDFAOutCallBack);
        if (myDFAOutCallBack.words.size()>0) {
            write(MessageHelper.cmd_error("new_role", false, ErrorCode.CREATE_ROLE_NOT_PERMISSION));
            return;
        }

        WorldManager worldManager = WorldManager.getInstance();
        RoleDao roleDao = worldManager.dbFactory().roleDao();
        Role role = roleDao.createHero(userId, serverId,roleName, headImage);
        if(role==null){
            write(MessageHelper.cmd_error("new_role", false, ErrorCode.ROLENAME_EXIST));
            return;
        }

        role.userCtrl = userCtrl;
//        write(GetRolesService.get_roles("new_role",userCtrl.getRoles(userId)));
    }


    public static void main(String[] args) {
        System.out.println("load...");
        DfaTool.loadJsonData();
        String mm = "来fuck";
        MyDFAOutCallBack myDFAOutCallBack = new MyDFAOutCallBack();
        DfaTool.removeKeyword.Search(mm, myDFAOutCallBack);
        System.out.println(myDFAOutCallBack.words.size());
        for (String keyword : myDFAOutCallBack.words) {
            System.out.println(mm);
            mm = mm.replaceAll(keyword, "*");
        }
        System.out.println(mm);
    }
}
