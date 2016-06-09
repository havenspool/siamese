package com.havens.siamese.server;

import com.havens.siamese.Service;
import com.havens.siamese.message.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by havens on 15-8-7.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    Server server;
    protected Map<String, Service> myservices = new HashMap<String, Service>();
    public ServerHandler(Server server){
        this.server=server;
    }

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client:"+ctx.channel().remoteAddress()+" handlerAdded");
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client:"+ctx.channel().remoteAddress()+" handlerRemoved");
        channels.remove(ctx.channel());
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object s) throws Exception {
//        System.out.println("Client from" +s);
//        ByteBuf result = (ByteBuf) s;
//        byte[] result1 = new byte[result.readableBytes()];
//        // msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中
//        result.readBytes(result1);
//        String resultStr = new String(result1);
//        // 释放资源，这行很关键
//        result.release();
//        System.out.println("Client from" +s);
        byte[] result1 = (byte[])s;
        String resultStr = new String(result1);
        Message msg=new Message(resultStr);
        if (msg.jObject==null) {
            return;
        }
        msg.channel=ctx.channel();
        Service service = myservices.get(msg.cmd);
        if (service == null) {
            service = server.service(msg.cmd);
            service.setChannel(msg.channel);
            myservices.put(msg.cmd, service);
        }
        service(service, msg.jObject);

//        Service service=server.service(msg.cmd);
//        if(service!=null){
//            System.out.println("from Client:" + msg.dataJson);
//            service.setChannel(msg.channel);
//            service.filter(msg.jObject);
//        }
    }

    protected boolean service(Service service, final JSONObject jObject) throws IOException {
        if (service == null) {
            return false;
        }
        try {
            service.service(jObject);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        System.out.println("Client:"+ctx.channel().remoteAddress()+" channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        System.out.println("Client:"+ctx.channel().remoteAddress()+" channelInactive");
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Client:"+ctx.channel().remoteAddress()+" exceptionCaught");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
