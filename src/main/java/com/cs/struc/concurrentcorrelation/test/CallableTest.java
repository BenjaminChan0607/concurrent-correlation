package com.cs.struc.concurrentcorrelation.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author benjaminChan
 * @date 2019/1/5 0005 下午 5:09
 *
 * Callable+Future/FutureTask却可以获取多线程运行的结果，
 * 可以在等待时间太长没获取到需要的数据的情况下取消该线程的任务
 */
public class CallableTest {
    public static void main(String[] args) {
        CallableTest callableTest = new CallableTest();
        callableTest.testCallable();
    }

    private void testCallable() {
        Callable<Integer> callable = new IntCallable();
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        try {
            new Thread(futureTask).start();
            System.out.println(futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private class IntCallable implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            int i = 0;
            for (; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + "," + i);
            }
            return i;
        }
    }
}
