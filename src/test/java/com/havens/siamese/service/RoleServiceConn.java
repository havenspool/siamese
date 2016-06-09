
package com.havens.siamese.service;

import com.havens.siamese.client.ClientChannelHaindler;
import com.havens.siamese.message.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by havens on 15-8-7.
 */
public class RoleServiceConn extends SimpleChannelInboundHandler<Object> {
    Channel channel;
    Message sendMessage;
    Message receivemessage;

    public RoleServiceConn(String host, int port,String username, String password) throws IOException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap  = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelHaindler());
            channel = bootstrap.connect(host, port).sync().channel();
            while(true){
                TimeUnit.SECONDS.sleep(5);
                login(username, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
        System.out.println("Client: username"+username+" password:"+password);
        login(username, password);
    }


    public void login(String userName, String userPwd) throws IOException {
        Message msg = new Message();
        msg.cmd = "user_login";
        Map data = new HashMap();
        data.put("userName",userName);
        data.put("userPwd",userPwd);

        channel.writeAndFlush(msg);
    }

    public void sendMessage(Message msg) throws IOException {
        channel.writeAndFlush(msg);
    }

    public void setMessage(Message msg) throws IOException {
        this.receivemessage=msg;
    }

    public Message getMessage() throws IOException {
        return receivemessage;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object s) throws Exception {
        setMessage((Message)s);
    }

    public static void main(String[] args) throws Exception {
        RoleServiceConn testCon = new RoleServiceConn("127.0.0.1", 8110, "gooooo", "123456");
    }
}
