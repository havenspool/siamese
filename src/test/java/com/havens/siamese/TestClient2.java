package com.havens.siamese;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by havens on 16-6-14.
 */
public class TestClient2 {

    public static String enter_room(){
        String msg="{\"roomId\":10001,\"userId\":10002,\"cmd\":\"enter_room\"}";
        return msg;
    }

    public static String exit_room(){
        String msg="{\"roomId\":10001,\"userId\":10002,\"cmd\":\"exit_room\"}";
        return msg;
    }

    public static String change_desk(){
        String msg="{\"roomId\":10001,\"userId\":10002,\"cmd\":\"change_desk\"}";
        return msg;
    }

    public static String desk_info(){
        String msg="{\"roomId\":10001,\"userId\":10002,\"cmd\":\"desk_info\"}";
        return msg;
    }

    public static String do_banker(){
        String msg="{\"roomId\":10001,\"userId\":10002,\"cmd\":\"do_banker\"}";
        return msg;
    }

    public static String get_banker(){
        String msg="{\"roomId\":10001,\"userId\":10002,\"cmd\":\"get_banker\"}";
        return msg;
    }

    public static String bet(){
        String msg="{\"roomId\":10001,\"userId\":10002,\"betCoinType\":1,\"cmd\":\"bet\"}";
        return msg;
    }

    public static String open_card(){
        String msg="{\"roomId\":10001,\"userId\":10002,\"cmd\":\"open_card\"}";
        return msg;
    }

    public static String ready_next(){
        String msg="{\"roomId\":10001,\"userId\":10002,\"cmd\":\"ready_next\"}";
        return msg;
    }


    public static void sendAndReceive(OutputStream out,InputStream in,String msg) throws Exception{
        out.write(msg.getBytes("UTF-8"));
        out.flush();
        byte[] buff = new byte[4096];
        int readed = in.read(buff);
        if(readed > 0){
            String str = new String(buff,0,readed);
            System.out.println("client received msg from server:"+str);
        }
    }

    public static void main(String[] args) throws Exception {
        Socket server = new Socket();
        server.connect(new InetSocketAddress("127.0.0.1", 9010));//119.29.254.14  127.0.0.1
        server.setKeepAlive(true);
        OutputStream out = server.getOutputStream();
        InputStream in = server.getInputStream();

        //room
        sendAndReceive(out,in,enter_room());
        sendAndReceive(out,in,change_desk());
        sendAndReceive(out,in,exit_room());
        sendAndReceive(out,in,enter_room());
        sendAndReceive(out,in,desk_info());
        sendAndReceive(out,in,do_banker());
        sendAndReceive(out,in,get_banker());
        sendAndReceive(out,in,bet());
        sendAndReceive(out,in,open_card());
        sendAndReceive(out,in,ready_next());
        out.close();
    }
}
