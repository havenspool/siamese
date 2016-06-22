package com.havens.siamese.job;

import com.havens.siamese.server.WorldManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by havens on 16-6-21.
 */
public class DeskJob  implements Runnable{
    public static final int MAX_SEND_MESSAGE = 15;

    private Queue<Integer> bankerDesks;
    private Queue<Integer> betDesks;
    WorldManager worldManager;

    public DeskJob() {
        bankerDesks = new ConcurrentLinkedQueue<Integer>();
        betDesks = new ConcurrentLinkedQueue<Integer>();
        worldManager = WorldManager.getInstance();
    }

    public boolean addBanker(int deskId) {
        boolean sendOK = false;
        if (bankerDesks.size() <= MAX_SEND_MESSAGE) {
            bankerDesks.add(deskId);
            sendOK = true;
        }
        return sendOK;
    }

    public boolean addBet(int deskId) {
        boolean sendOK = false;
        if (betDesks.size() <= MAX_SEND_MESSAGE) {
            betDesks.add(deskId);
            sendOK = true;
        }
        return sendOK;
    }


    public void run() {
        try {
            List<Integer> bankerTasks = new ArrayList<Integer>();
            List<Integer> betTasks = new ArrayList<Integer>();

            int size = bankerDesks.size();
            int bsize = betDesks.size();

            if (size > 0) {
                if (size > 20) {
                    size = 20;
                }
                for (int i = 0; i < size; i++) {
                    bankerTasks.add(bankerDesks.poll());
                }

                for (int deskId:bankerTasks){
                    WorldManager.getInstance().deskBanker(deskId);
                }
            }

            if (bsize > 0) {
                if (size > 20) {
                    size = 20;
                }
                for (int i = 0; i < size; i++) {
                    betTasks.add(betDesks.poll());
                }
                for (int deskId:betTasks){
                    WorldManager.getInstance().openCard(deskId);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}