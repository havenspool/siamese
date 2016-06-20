package com.havens.siamese.job;

/**
 * Created by havens on 16-6-21.
 */
public class DeskJob  implements Runnable{

    public DeskJob() {
    }


    public void run() {
        System.out.println("DeskJob run");
    }
}