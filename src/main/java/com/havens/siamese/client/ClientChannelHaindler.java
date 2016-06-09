package com.havens.siamese.client;

import com.havens.siamese.codec.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by havens on 15-8-7.
 */
public class ClientChannelHaindler extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
//        ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
//        ch.pipeline().addLast("decoder", new MessageDecoder());
//        ch.pipeline().addLast("encoder", new MessageEncoder());
        ch.pipeline().addLast("decoder", new StringDecoder());
        ch.pipeline().addLast("encoder", new StringEncoder());
//        ch.pipeline().addLast("decoder", MarshallingCodeCFactory.buildMarshallingDecoder());
//        ch.pipeline().addLast("encoder", MarshallingCodeCFactory.buildMarshallingEncoder());

        ch.pipeline().addLast("handler", new ClientHandler());
    }
}