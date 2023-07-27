package com.llb.util;
/**
* 创建时间：2020年12月2日
* 测试HTTP请求工具类
* @author LLB
*/

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {

	private static CloseableHttpClient httpClient; //发送请求的对象
	private static RequestConfig requestConfig =
			RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build(); //设置请求时间和传输超时时间分别为5秒

	public void test() {

	}

	/*
	静态代码块用于设置默认的重试次数
	 */
	static {
		//第一个参数代表重试次数
		HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(0, false);
		httpClient = HttpClients.custom().setRetryHandler(retryHandler).build();
		//还可以使用PoolingHttpClientConnectionManager 对象来构建连接池
//		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
//		connectionManager.setMaxTotal(500);
//		connectionManager.setDefaultMaxPerRoute(500);
//		httpClient = HttpClients.custom().setRetryHandler(retryHandler).
//				setConnectionManager(connectionManager).build();
	}
	
	public static boolean get(String url, Map<String, String> param) {

		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();

			//创建http GET请求
			HttpGet httpGet = new HttpGet(uri);
			//设置请求配置
			httpGet.setConfig(requestConfig);
			response = httpClient.execute(httpGet);
			//若响应状态码为200则代表成功相应，否则失败
			if (response.getStatusLine().getStatusCode() == 200) {
				//创建字符缓冲流
				//通过OpenOption的实现类来实现追加模式
				//通过Charset类来设置字符编码集
				BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(GlobalVariable.filePath), StandardOpenOption.APPEND);
				//写入响应数据到文本中
				bufferedWriter.write(Thread.currentThread().getName() + "中的响应内容为：");
				bufferedWriter.newLine(); //在文件中新建一行
				bufferedWriter.write(EntityUtils.toString(response.getEntity() , "UTF-8"));
				bufferedWriter.newLine(); //在文件中新建一行
				//刷新缓冲流
				bufferedWriter.flush();
				//关闭缓冲流
				bufferedWriter.close();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			//向外层抛出异常
			throw new RuntimeException(e);
		} finally {
			if (response != null) {
				try {
					//关闭资源
					response.close();
				} catch (IOException e) {
					System.out.println("线程关闭异常");
					e.printStackTrace();
				}
			}
		}
	}
	
}
