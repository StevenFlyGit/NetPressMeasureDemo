package com.llb.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
* 创建时间：2020年12月1日
* 主窗口控制器
* @author LLB
*/
public class mainController implements Initializable{


	private Integer threadCount ;
	@FXML
	private TextField requestUrl;
	@FXML
	private TextField requestCount;
	private ExecutorService ThreadPool;

	@FXML
	private TextField result;

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		threadCount = Integer.valueOf(requestCount.getText());
		ThreadPool = Executors.newFixedThreadPool(threadCount);
	}
	
	@FXML
	private void showTimeButton(ActionEvent event) {
		System.out.println("requestUrl = " + requestUrl);
		System.out.println("threadCount = " + threadCount);
		result.setText("测试成功！");
	}
	
}
