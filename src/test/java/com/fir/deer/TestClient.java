package com.havens.siamese;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by havens on 16-5-24.
 */
public class TestClient {
    public static void main(String[] args) throws Exception {
        Socket server = new Socket();
        server.connect(new InetSocketAddress("127.0.0.1", 8260));//119.29.254.14  127.0.0.1
        server.setKeepAlive(true);
        OutputStream out = server.getOutputStream();
        InputStream in = server.getInputStream();

        ByteBuffer header = ByteBuffer.allocate(4);
        String sendMsg="{\"userName\":\"asan\",\"userPwd\":\"123456\",\"channel\":\"SYGF\",\"cmd\":\"user_login\"}";
        String msg="{\"serverId\":10001,\"userId\":1001,\"roleName\":\"哈哈\",\"cmd\":\"get_roles\"}";
        header.putInt(sendMsg.getBytes().length);
//        out.write(header.array());
//        byte[] msg=new byte[sendMsg.getBytes().length+2];
//        out.write(msg);
        out.write(msg.getBytes("UTF-8"));
        out.flush();

        byte[] buff = new byte[4096];
        int readed = in.read(buff);
        if(readed > 0){
            String str = new String(buff,4,readed);
            System.out.println("client received msg from server:"+str);
        }
        out.close();
    }
}
