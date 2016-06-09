package com.havens.siamese.server;

import com.havens.siamese.message.Message;
import com.havens.siamese.message.UDPMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

/**
 * Created by havens on 15-8-17.
 */
public class UDPSeverHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        Message msg= UDPMessage.receive(packet);
        System.out.println("UDPSeverHandler channelRead0:" + msg);
        if (msg == null) {
            return;
        }

        ctx.channel().writeAndFlush(UDPMessage.send(msg,packet.sender()));
//        if(msg.data instanceof Map){
//            msg.channel=ctx.channel();
//            Service service=Server.service(msg.cmd);
//            service.setChannel(msg.channel);
//            service.filter(msg.data);
//        }
        System.out.println("UDPSeverHandler channelRead0:" + msg);
    }
}
