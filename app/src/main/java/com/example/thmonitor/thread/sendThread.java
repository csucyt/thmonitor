package com.example.thmonitor.thread;

/**
 * Created by Administrator on 2017/4/24.
 */

public class sendThread implements Runnable {

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
