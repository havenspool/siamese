package com.havens.siamese.client;

import com.havens.siamese.message.Message;
import com.havens.siamese.message.UDPMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

/**
 * Created by havens on 15-8-17.
 */
public class UDPClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        Message req = new Message();
        req.cmd = "time_check";
        incoming.writeAndFlush(UDPMessage.send(req));
        System.out.println("UDPClientHandler channelActive:" + req);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        Message msg= UDPMessage.receive(packet);
        if (msg == null) {
            return;
        }
        System.out.println("UDPClientHandler channelRead0:" + msg);
    }
}