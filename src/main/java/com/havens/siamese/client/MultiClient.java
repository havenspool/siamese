package com.havens.siamese.client;

/**
 * Created by havens on 15-8-7.
 */
public class MultiClient {
    public static void main(String[] args) throws Exception {
        for(int i=0;i<10;i++){
            new Client().connect(8090, "127.0.0.1");
        }

    }
}
