package com.llb.test;

import com.llb.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
* 创建时间：2020年12月2日
* 多线程控制类
* @author LLB
*/
@Slf4j
public class RequestThread implements Runnable{

	private String requestUrl;
	private CountDownLatch countDownLatch;

	public RequestThread() {}

	public RequestThread(String requestUrl, CountDownLatch countDownLatch) {
		super();
		this.requestUrl = requestUrl;
		this.countDownLatch = countDownLatch;
	}

	/**
	 * 控制每一个线程下的执行流程
	 */
	@Override
	public void run() {

		try {
			countDownLatch.countDown();
			countDownLatch.await();
			boolean requestSuccess = HttpClientUtil.get(requestUrl, null);
			if (!requestSuccess) {
				log.error("response error.....threadName:" + Thread.currentThread().getName());
			} else {
				log.info("response success.....threadName:" + Thread.currentThread().getName());
			}
//			countDownLatch.countDown();

//			int i = 1;
//
//			i++;
//			System.out.println(Thread.currentThread().getName() + "====>" + i);

		} catch (Exception e) {
			log.error("requset error.....threadName:" + Thread.currentThread().getName());
		}

	}

}
