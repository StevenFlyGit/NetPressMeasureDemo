package com.llb.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建时间：2020/12/2
 * 网络压力测试运行类
 * @author wpf
 */

public class NetTest {

    private static Integer threadCount = 50;
    private static String requestUrl = "http://localhost:8080/";
    private static Integer requestCount = 50;
    private static ExecutorService ThreadPool = Executors.newFixedThreadPool(threadCount);

    private static void exec() throws InterruptedException {

        CountDownLatch requestCountDown = new CountDownLatch(requestCount);
        for (int i = 0; i < requestCount; i++) {
            RequestThread requestThread = new RequestThread(requestUrl, requestCountDown);
            ThreadPool.execute(requestThread);
            System.out.println(i);
        }
        requestCountDown.await();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("start execute.....");
        long startTime = System.currentTimeMillis();
        exec();
        long endTime = System.currentTimeMillis();
        System.out.println("execute end ....spendTime:" + (endTime - startTime));

        System.out.println("方法运行结束");
    }


}
