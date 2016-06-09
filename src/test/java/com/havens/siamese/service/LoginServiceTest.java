
package com.havens.siamese.service;

import com.havens.siamese.message.Message;
import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 *Created by havens on 15-8-7.
 */
public class LoginServiceTest extends RoleServiceTest {

    @Test
    public void get_roles() throws Exception {
        Message msg = new Message();
        msg.cmd = "get_roles";
        Map data = new HashMap();
        data.put("serverID",10001);
        data.put("userID",1234567);

        testCon.sendMessage(msg);
        Message myMap =testCon.getMessage();
        Assert.assertNotNull(myMap);
        System.out.println(myMap);
    }

    @Test
    public void new_role() throws Exception {
        Message msg = new Message();
        msg.cmd = "new_role";
        Map data = new HashMap();
        data.put("serverID",10001);
        data.put("userID",1234567);
        data.put("roleName","hi");
        data.put("headImage",1);

        testCon.sendMessage(msg);

        Message myMap =testCon.getMessage();
        Assert.assertNotNull(myMap);
        System.out.println(myMap);
    }

    @Test
    public void user_login() throws Exception {
        Message msg = new Message();
        msg.cmd = "user_login";
        Map data = new HashMap();
        data.put("userName","asan");
        data.put("userPwd","123456");

        testCon.sendMessage(msg);
        Thread.sleep(5000);
        Message myMap =testCon.getMessage();
        System.out.println(myMap);
//        Assert.assertNotNull(myMap);
    }

    public static void main(String[] args) throws Exception {
        setUpBeforeClass();
    }
}
