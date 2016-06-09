package com.havens.siamese.server;

import com.havens.siamese.codec.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;


import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by havens on 15-8-7.
 */
public class ServerChannelHandler extends ChannelInitializer<SocketChannel> {
    Server server;
    public ServerChannelHandler(Server server){
        this.server=server;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //LengthFieldBasedFrameDecoder和LengthFieldPrepender就是设定协议头长度的，我这里设定协议头的长度为4个字节。
//        ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//        ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
//        ch.pipeline().addLast("decoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
//        ch.pipeline().addLast("encoder", new LengthFieldPrepender(4, false));
//        ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
//        ch.pipeline().addLast("decoder", new MessageDecoder());
//        ch.pipeline().addLast("encoder", new MessageEncoder());

          ch.pipeline().addLast("decoder", new ByteArrayDecoder());
          ch.pipeline().addLast("encoder", new ByteArrayEncoder());
//        ch.pipeline().addLast("decoder", new StringDecoder());//字符串长度不能超过1024
//        ch.pipeline().addLast("encoder", new StringEncoder());//字符串长度不能超过1024
//        ch.pipeline().addLast("decoder", MarshallingCodeCFactory.buildMarshallingDecoder());
//        ch.pipeline().addLast("encoder", MarshallingCodeCFactory.buildMarshallingEncoder());
//        //解码用
//        ch.pipeline().addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
//        //构造函数传递要解码成的类型
//        ch.pipeline().addLast("protobufDecoder", new ProtobufDecoder(LocalTimeProtocol.LocalTimes.getDefaultInstance()));
//        //编码用
//        ch.pipeline().addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
//        ch.pipeline().addLast("protobufEncoder", new ProtobufEncoder());
        ch.pipeline().addLast(new ServerHandler(server));
        //System.out.println("Client:" + ch.remoteAddress() + "连接上");
    }
}
