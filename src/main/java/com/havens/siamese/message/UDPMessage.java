package com.havens.siamese.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.HashMap;

/**
 * Created by havens on 15-8-17.
 */
public class UDPMessage {

    public static DatagramPacket send(Message msg){
        ByteBuf buf= Unpooled.copiedBuffer(Message.toByteArray(msg));
        DatagramPacket packet=new DatagramPacket(buf,new InetSocketAddress("127.0.0.1",9000));
        return packet;
    }

    public static DatagramPacket send(Message msg,InetSocketAddress addr){
        ByteBuf buf= Unpooled.copiedBuffer(Message.toByteArray(msg));
        DatagramPacket packet=new DatagramPacket(buf,addr);
        return packet;
    }

    public static Message receive(DatagramPacket packet){
        ByteBuf buf = packet.copy().content();
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        return (Message)Message.toObject(req);
    }
    public static void main(String[] args){
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
