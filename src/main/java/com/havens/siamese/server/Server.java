package com.havens.siamese.server;

import com.havens.siamese.Service;
import com.havens.siamese.utils.FileHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.*;

/**
 * Created by havens on 15-8-7.
 */
public class Server implements Runnable{
    public static final String SERVICES_FILE = "services.xml";
    private final HashMap<String, Class> services;

    protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors()*2; //默认
    protected static final int BIZTHREADSIZE = 4;
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);


    public Server(){
        services=new HashMap<String, Class>();
    }

    public Service service(String code) {
        synchronized (services) {
            Class clazz = services.get(code);
            if (clazz == null) {
                clazz = services.get("default");
                if (clazz == null)
                    return null;
            }
            Service service = null;
            try {
                service = (Service) clazz.newInstance();
                service.create(this);
                service.setCmd(code);
            } catch (Exception e) {
//                e.printStackTrace(out);
                e.printStackTrace();
            }
            return service;
        }
    }

    private void deployInstance() throws Exception {
        Set<Class> instances = FileHelper.getInstances();
        for (Class clazz : instances) {
            Service instance = (Service) clazz.newInstance();
            instance.create(this);
        }
    }

    private void deployServices() throws Exception {
        Map<String, Class> deployServices = FileHelper.getServices();
        for (String code : deployServices.keySet()) {
            if (code == null) {
                return;
            }
            StringTokenizer codes = new StringTokenizer(code, ":");
            while (codes.hasMoreTokens()) {
                String inside_code = codes.nextToken();
                Class clazz = deployServices.get(code);
                services.put(inside_code, clazz);
            }
        }
    }

    public void init(){

    }

    public void run() {
        try {
            deployInstance();
            deployServices();

            init();
            System.out.println("listen on port:"+WorldManager.PORT);
            bind(WorldManager.PORT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void bind(int port) throws Exception {
        // 配置服务端的NIO线程组
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)//最大客户端连接数为1024
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ServerChannelHandler(this));
            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();

            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new Server().run();
    }
}
