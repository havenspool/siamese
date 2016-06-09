package com.havens.siamese.client;

import com.havens.siamese.entity.User;
import com.havens.siamese.message.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by havens on 15-8-7.
 */
public class ClientHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
        User user=new User();
        user.name="havens";
        user.passwd="123456";

        Message msg = new Message();
        msg.cmd = "login";
        JSONObject jObject=new JSONObject();
        jObject.put("cmd", "login");
        jObject.put("id",user.id);
        jObject.put("name",user.name);
        jObject.put("passwd",user.passwd);
        msg.jObject=jObject;
        msg.dataJson=jObject.toString();
        incoming.writeAndFlush(msg.dataJson);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object s) throws Exception {
        Message msg=new Message((String)s);
        if (msg.jObject==null) {
            return;
        }
        msg.channel=ctx.channel();
        System.out.println("data from server: cmd:"+msg.cmd+" json:"+msg.dataJson);
        if("login".equals(msg.cmd)||"time_check".equals(msg.cmd)){
            JSONObject jObject=new JSONObject();
            jObject.put("cmd", "login");
            jObject.put("isSuccess", true);
            jObject.put("erroeCode", 0);
            ctx.channel().writeAndFlush(Message.newInstance("login",jObject));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = ctx.channel();
        System.out.println("Client:"+incoming.remoteAddress()+"异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}