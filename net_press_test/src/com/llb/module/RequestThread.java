package com.llb.module;

import com.llb.util.GlobalVariable;
import com.llb.util.HttpClientUtil;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.concurrent.CountDownLatch;

/**
* 创建时间：2020年12月2日
* 多线程控制类
* @author LLB
*/

public class RequestThread implements Runnable{

	private String requestUrl; //需要测试的网站地址
	private CountDownLatch countDownLatch;

	public RequestThread() {}

	public RequestThread(String requestUrl, CountDownLatch countDownLatch) {
		this.requestUrl = requestUrl;
		this.countDownLatch = countDownLatch;
	}

	/**
	 * 控制每一个线程下的执行流程
	 */
	@Override
	public void run() {
		try {
			boolean requestSuccess = HttpClientUtil.get(requestUrl, null);
			System.out.println("requestSuccess = " + requestSuccess);
			if (requestSuccess) {
				//成功数量+1
				GlobalVariable.successThreadCount.getAndIncrement();
				System.out.println(Thread.currentThread().getName() + "线程已连通");
			} else {
				//失败数量+1
				GlobalVariable.failureThreadCount.getAndIncrement();
				System.out.println(Thread.currentThread().getName() + "线程未连通");
				throw new RuntimeException("有线程未连通");
			}

		} catch (Exception e) {
			//失败数量+1
			GlobalVariable.failureThreadCount.getAndIncrement();
			System.out.println("有线程出现异常");
			throw new RuntimeException("有线程出现异常");
		} finally {
			//countDownLatch阻塞数量-1，除主线程外的所有线程运行完毕后，主线程即可以继续运行
			countDownLatch.countDown();
		}
	}
}
