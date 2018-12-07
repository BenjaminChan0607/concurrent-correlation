package com.cs.struc.concurrentcorrelation.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author benjaminChan
 * @date 2018/12/7 0007 下午 5:32
 * 目前的解决方案：把这个大List进行拆分，然后多线程并发操作，以提升性能，降低远程调用的耗时。
 */
public class TestFutureTask {

    //准备要执行的数据，数据整备完毕
    private static ExecutorService es = Executors.newFixedThreadPool(100);
    private static int size = 6;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<UserInfo> userInfoList = new ArrayList<UserInfo>();
        for (int i = 0; i < 1000; i++) {
            UserInfo uc = new UserInfo();
            uc.setMoneyA((int) (Math.random() * 100));
            uc.setMoneyB((int) (Math.random() * 100));
            userInfoList.add(uc);
        }
        serialCalc(userInfoList);
        concurrentCalc(userInfoList);
    }

    private static void concurrentCalc(List<UserInfo> userInfoList) throws ExecutionException, InterruptedException {
        //存储多线程计算花费的时间
        List<UserInfo> retInfoList2 = new ArrayList<UserInfo>();
        List<List<UserInfo>> subTaskList = new ArrayList<List<UserInfo>>();
        //切分任务
        for (int i = 0; i <= userInfoList.size(); i = i + size) {
            subTaskList.add(userInfoList.subList(i, Math.min(i + size, userInfoList.size())));
        }
        Long aDateLong2 = System.currentTimeMillis();
        //这个list是专门用来保存所有的future的，先把所有任务全部扔出去执行，这时候不管结果
        List<FutureTask<List<UserInfo>>> tasklist = new ArrayList<FutureTask<List<UserInfo>>>();
        for (List<UserInfo> alist : subTaskList) {
            FutureTask<List<UserInfo>> task = new FutureTask<List<UserInfo>>(new SumJob(alist));
            es.execute(task);
            tasklist.add(task);
        }
        //拿到所有的结果
        for (FutureTask<List<UserInfo>> futureTask : tasklist) {
            retInfoList2.addAll(futureTask.get());
        }
        Long bDateLong2 = System.currentTimeMillis();
        System.out.println("多线程拆分后耗时:" + (bDateLong2 - aDateLong2));

        es.shutdown();
    }

    private static void serialCalc(List<UserInfo> userInfoList) throws InterruptedException {
        //存储本地串行计算花费的时间
        List<UserInfo> retInfoList1 = new ArrayList<UserInfo>();

        Long aDateLong = System.currentTimeMillis();
        for (int i = 0; i < userInfoList.size(); i++) {
            int sum = userInfoList.get(i).getMoneyA() + userInfoList.get(i).getMoneyB();
            userInfoList.get(i).setSumNum(sum);
            retInfoList1.add(userInfoList.get(i));
            //线程睡眠以模仿计算消耗的时间
            Thread.sleep(1);
        }
        Long bDateLong = System.currentTimeMillis();
        System.out.println("串行进行的耗时:" + (bDateLong - aDateLong));
    }
}

//求和的线程任务
class SumJob implements Callable<List<UserInfo>> {
    private List<UserInfo> sumList = null;

    public SumJob(List<UserInfo> sumList) {
        super();
        this.sumList = sumList;
    }

    public List<UserInfo> call() throws Exception {
        for (int i = 0; i < sumList.size(); i++) {
            int sum = sumList.get(i).getMoneyA() + sumList.get(i).getMoneyB();
            sumList.get(i).setSumNum(sum);
            //线程睡眠以模仿计算消耗的时间
            Thread.sleep(1);
        }
        return sumList;
    }
}