package com.havens.siamese.job;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import com.havens.siamese.message.Message;

/**
 * Created by havens on 15-8-11.
 */
public class BaseJob {
    private final BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();

    public Message take() {
        return queue.poll();
    }

    public void put(Message t) {
        try {
            queue.put(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getQueueSize() {
        return queue.size();
    }
}
