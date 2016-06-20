package com.havens.siamese.server;

import com.havens.siamese.ErrorCode;
import com.havens.siamese.Service;
import com.havens.siamese.entity.Desk;
import com.havens.siamese.message.Message;
import com.havens.siamese.message.MessageHelper;
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
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by havens on 15-8-7.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    Server server;
    protected Map<String, Service> myservices = new HashMap<String, Service>();
    public ServerHandler(Server server){
        this.server=server;
        WorldManager.getInstance(server);
    }

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static ConcurrentHashMap<Channel, Integer> channelsMap = new ConcurrentHashMap<Channel, Integer>();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client:"+ctx.channel().remoteAddress()+" handlerAdded");
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        int userId=channelsMap.get(ctx.channel());
        System.out.println("Client:"+ctx.channel().remoteAddress()+":handlerRemoved:"+ userId);
        channelsMap.remove(ctx.channel());
        channels.remove(ctx.channel());
        WorldManager.getInstance().outDesk(userId);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        String resultStr = new String((byte[])obj);
        Message msg=new Message(resultStr);
        if (msg.jObject==null) {
            ctx.channel().writeAndFlush(MessageHelper.cmd_error(msg.cmd, false, ErrorCode.PACKET_ERROR).getBytes("UTF-8"));
            return;
        }
        System.out.println("Client from:" +resultStr);
        msg.channel=ctx.channel();
        Service service = myservices.get(msg.cmd);
        if (service == null) {
            service = server.service(msg.cmd);
            if (service == null) {
                msg.channel.writeAndFlush(MessageHelper.cmd_error(msg.cmd, false, ErrorCode.PACKET_ERROR).getBytes("UTF-8"));
                return;
            }
            service.setChannel(msg.channel);
            myservices.put(msg.cmd, service);
            channelsMap.put(msg.channel,msg.userId);
        }
        service(service, msg.jObject);
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
        int userId=channelsMap.get(ctx.channel());
        System.out.println("Client:"+ctx.channel().remoteAddress()+":exceptionCaught:"+ userId);
        channelsMap.remove(ctx.channel());
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
