package com.havens.siamese;

import com.havens.siamese.server.Server;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.io.IOException;

import org.json.JSONObject;

/**
 * Created by havens on 15-8-11.
 */
public abstract class Service {
    protected String cmd;

    protected Channel channel;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    public Channel getChannel() {
        return this.channel;
    }

    protected void write(String json) throws IOException {
        // 在当前场景下，发送的数据必须转换成ByteBuf数组
//        ByteBuf encoded = channel.alloc().buffer(4 * json.length());
//        encoded.writeBytes(json.getBytes());
//        System.out.println("channel isWritable:"+channel.isWritable());
        channel.writeAndFlush(json.getBytes("UTF-8"));
        System.out.println("write:"+json);
    }
    public final void service(JSONObject jObject) throws Exception {
        if (beforeFilter(jObject)) {
            filter(jObject);
            afterFilter();
        }
    }

    public void create(Server server) throws Exception {
    }

    public boolean beforeFilter(final JSONObject jObject) throws Exception {
        return true;
    }

    public void afterFilter() throws Exception {
    }

    public abstract void filter(final JSONObject jObject) throws Exception;
}
