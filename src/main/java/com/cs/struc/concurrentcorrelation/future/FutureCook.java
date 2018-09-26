package com.cs.struc.concurrentcorrelation.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author benjaminChan
 * @date 2018/8/20 0020 下午 3:46
 */
public class FutureCook {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        Callable<CookTool> onlieShopping = new Callable<CookTool>() {
            @Override
            public CookTool call() throws Exception {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Cook tool is coming");
                return new CookTool();
            }
        };

        FutureTask<CookTool> futureTask = new FutureTask<CookTool>(onlieShopping);
        new Thread(futureTask).start();

        TimeUnit.SECONDS.sleep(1);

        if (!futureTask.isDone()) {
            System.out.println("Cook tool is not coming,please wait a moment");
        }

        CookTool cookTool = futureTask.get();
        System.out.println("food is coming");
        System.out.println("start cookie");
        System.out.println(System.currentTimeMillis() - startTime);
    }


    private static class CookTool {
    }
}

