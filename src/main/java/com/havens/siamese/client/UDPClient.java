package com.havens.siamese.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.IOException;


/**
 * Created by havens on 15-8-17.
 */
public class UDPClient {

    public void init(String host,int port){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap  = new Bootstrap()
                    .group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new UDPClientHandler());
            Channel channel = bootstrap.connect(host, port).sync().channel();

            while (true){
                Thread.sleep(500);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws IOException {
        new UDPClient().init("127.0.0.1",9000);
    }
}
