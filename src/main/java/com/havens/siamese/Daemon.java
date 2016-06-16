package com.havens.siamese;

import com.havens.siamese.server.Server;
import com.havens.siamese.server.WorldManager;
import com.havens.siamese.utils.FileHelper;

import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.Properties;

/**
 * Created by havens on 15-8-7.
 */
public class Daemon{
    public static final String SERVER_PROP = "server.properties";

    public static void setUp(){
        Properties config = FileHelper.getPropertiesUTF8(SERVER_PROP);
        WorldManager.PORT = Integer.valueOf(config.getProperty("port", "9010")).intValue();
        WorldManager.APP_HOME = config.getProperty("app_home", "/home/havens/Code/siamese/");

        WorldManager.HOST = config.getProperty("host", "119.29.254.14");
        WorldManager.User_DB = config.getProperty("user_db", "miser");
        WorldManager.SERVER_ID = Integer.valueOf(config.getProperty("server_id", "10001")).intValue();
        WorldManager.SERVER_NAME = config.getProperty("server_name", "小试身手");
        WorldManager.MINCOST = Integer.valueOf(config.getProperty("mincost", "1000")).intValue();
        WorldManager.CONDITION = Integer.valueOf(config.getProperty("condition", "1")).intValue();
        WorldManager.ROOMMAXNUM = Integer.valueOf(config.getProperty("roomMaxNum", "300")).intValue();
    }

    public static void main(String[] args) throws Exception {
        setUp();

        try{
            String pid = ManagementFactory.getRuntimeMXBean().getName();
            PrintWriter out = new PrintWriter(WorldManager.APP_HOME + "pid.txt");
            out.println(pid.substring(0, pid.indexOf('@')));
            out.flush();
            out.close();
            System.out.println("pid:"+pid);

            System.out.println("SERVER_NAME:"+WorldManager.SERVER_NAME);
        }
        catch (Exception localException){
        }

        new Server().run();
//        ExecutorService exec = Executors.newCachedThreadPool();
//        exec.execute(LoginJob.getInstance());
    }
}
