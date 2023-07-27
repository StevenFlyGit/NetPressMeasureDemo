package com.llb.controller;

import com.llb.module.RequestThread;
import com.llb.util.GlobalVariable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * 创建时间：2020年12月1日
 * 主窗口控制器
 * @author LLB
 */
public class mainController implements Initializable{

	private Integer totalThreadCount ;
	private ExecutorService ThreadPool;

	@FXML
	private TextField requestUrl;
	@FXML
	private TextField originRequestCount;
	@FXML
	private TextField addRequestCount;

	@FXML
	private TextArea testResult;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//测试时可以进行初始化
		requestUrl.setText("http://www.baidu.com");
		originRequestCount.setText("800");

		//正式使用时输入以下字符
//		requestUrl.setText("请输入合法的Url地址");
//		originRequestCount.setText("请输入30000以内的数字");
//		addRequestCount.setText("与初始并发量相加后不能超过30000");
	}

	@FXML
	private void netTestButton(ActionEvent event) throws InterruptedException {

		//构造url正则表达式匹配的对象
		Pattern urlPattern = Pattern.compile(GlobalVariable.urlRegx);
		//判断输入的url地址是否何法
		if (urlPattern.matcher(requestUrl.getText()).matches() && !("").equals(requestUrl.getText())) {
			//构造整数正则表达式匹配的对象
			Pattern numPattern = Pattern.compile(GlobalVariable.numRegex);

			//判断数字的输入框是否输入了数字以及是否都为空字符串
			if (numPattern.matcher(originRequestCount.getText()).matches()
			   	&& numPattern.matcher(addRequestCount.getText()).matches()
				&& !(("").equals(originRequestCount.getText()) && ("").equals(addRequestCount.getText()))) {

				//如果初始并发数量为空，则设为0
				if (("").equals(originRequestCount.getText())) {
					originRequestCount.setText("0");
				}
				//如果增加并发数量为空，则设为0
				if (("").equals(addRequestCount.getText())) {
					addRequestCount.setText("0");
				}

 				//得到需要测试的总共线程数量
				totalThreadCount = Integer.valueOf(originRequestCount.getText()) + Integer.valueOf(addRequestCount.getText());
				//创建线程池对象，分配一般数量的线程
				ThreadPool = Executors.newFixedThreadPool(totalThreadCount / 2);
				//创建countDownLatch对象用于阻塞主线程的运行，只有当其他线程都运行完毕后才能运行主线程
				CountDownLatch countDownLatch = new CountDownLatch(totalThreadCount);

				//判断即将写入的文件是否存在，若不存在则创建一个
				if (!Files.exists(Paths.get(GlobalVariable.filePath))) {
					try {
						Files.createFile(Paths.get(GlobalVariable.filePath));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				//正式测试前的时间毫秒值
				Long startTime = System.currentTimeMillis();
				for (int i = 0; i < totalThreadCount; i++) {
					//创建将执行的线程对象
					RequestThread requestThread = new RequestThread(requestUrl.getText(), countDownLatch);
					//分配线程池中的线程来执行
					ThreadPool.execute(requestThread);
				}
				//阻塞主线程
				countDownLatch.await();
				//测试结束的时间毫秒值
				Long endTime = System.currentTimeMillis();

				//获取成功和失败的线程数
				int successThreadCount = GlobalVariable.successThreadCount.get();
				int failureThreadCount = GlobalVariable.failureThreadCount.get();

				//计算平均响应时间
				double averageResponseTime = (endTime - startTime) / successThreadCount;
				//计算出错率
				double wrongRate = (double) failureThreadCount / (double) totalThreadCount * 100;

				//设置返回窗口的信息
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("共有" + GlobalVariable.successThreadCount.get() + "个线程访问成功")
						.append("\n")
						.append("平均每个线程的响应时间为：" + averageResponseTime + "ms")
						.append("\n")
						.append("总响应时间为：" + (endTime - startTime) + "ms")
						.append("\n")
						.append("本次测试的出错率为：" + wrongRate + "%")
						.append("\n");
				//判断测试是否成功
				System.out.println("线程失败的数量为：" + GlobalVariable.failureThreadCount);
				if (successThreadCount == totalThreadCount) {
					stringBuilder.append("测试成功，该网站可以承受" + totalThreadCount + "个客户端同时访问");
				} else {
					stringBuilder.append("测试失败，该网站无法承受" + totalThreadCount + "个客户端同时访问");
				}

				//将返回的结果字符串设置到TextArea框中
				testResult.setText(stringBuilder.toString());
			} else {
				testResult.setText("请在并发数量栏中输入数字！");
			}
		} else {
			testResult.setText("请输入合法的url地址！");
		}

	}

}
