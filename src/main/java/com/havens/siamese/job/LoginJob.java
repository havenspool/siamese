package com.havens.siamese.job;
import com.havens.siamese.message.Message;
import com.havens.siamese.server.Server;

/**
 * Created by havens on 15-8-11.
 */
public class LoginJob extends BaseJob implements Runnable{
    private static LoginJob INSTANCE = new LoginJob();

    private Server server;
    private final Object lock = new Object();
    private int size=0;
    public void addLoginJob(Message msg) {
        synchronized (lock) {
            INSTANCE.put(msg);
            size++;
        }
    }
    public LoginJob() {
    }

    public static LoginJob getInstance() {
        return INSTANCE;
    }

    public void run() {
        System.out.println("LoginJob run");
        if(size>0){
            synchronized (lock) {
                Message msg= INSTANCE.take();
//                Service service= Server.service(msg.cmd);
//                System.out.println(service);
                size--;
            }
        }
        try {
            Thread.sleep(50);
        }catch (InterruptedException e){

        }
    }
}