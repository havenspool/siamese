package com.havens.siamese.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by havens on 15-8-7.
 */
public class Client {
    public void connect(int port, String host) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap  = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelHaindler());
            Channel channel = bootstrap.connect(host, port).sync().channel();
           // BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while(true){
//              channel.writeAndFlush((Object)(in.readLine() +"\r\n"));
                channel.writeAndFlush("{\"userName\":\"asan\",\"userPwd\":\"123456\",\"cmd\":\"user_login\"}");
//                channel.writeAndFlush(msg2);
//                channel.writeAndFlush(msg);
                //channel.writeAndFlush(msg2);
                Thread.sleep(15000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //max 2500
        for(int i=0;i<1;i++){
            new Thread(new Runnable() {
                public void run() {
                    try {
                        new Client().connect(8090, "119.29.254.14");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
