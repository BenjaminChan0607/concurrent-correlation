package com.cs.struc.concurrentcorrelation.future;

import java.util.concurrent.TimeUnit;

/**
 * @author benjaminChan
 * @date 2018/8/20 0020 下午 3:11
 */
public class CommonCook {

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        OnlineShopping thread = new OnlineShopping();
        thread.start();
        thread.join();

        TimeUnit.SECONDS.sleep(1);

        System.out.println("food is coming");
        System.out.println("start cookie");
        System.out.println(System.currentTimeMillis() - startTime);
    }

    private static class OnlineShopping extends Thread {
        private CookTool cookTool;

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Cook tool is coming");
            cookTool = new CookTool();
        }
    }


    private static class CookTool {

    }
}
