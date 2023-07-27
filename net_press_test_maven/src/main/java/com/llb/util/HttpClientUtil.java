package com.llb.util;
/**
* 创建时间：2020年12月2日
* 测试HTTP请求工具类
* @author LLB
*/

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@Slf4j
public class HttpClientUtil {

	private static CloseableHttpClient httpClient = HttpClients.createDefault();

	public void test() {

	}
	
	public static boolean get(String url, Map<String, String> param) {
		// String result = "";
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

			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);
			response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				log.info("Request is success,responseCode:" + response.getStatusLine().getStatusCode()
						+ " ,responseMsg:" + "响应状态正常");
				// result = EntityUtils.toString(response.getEntity(), "utf-8");
				return true;
			} else {
				log.error("Request is unsuccess,responseCode:" + response.getStatusLine().getStatusCode()
						+ " ,responseMsg:" + "响应状态异常");
			}
		} catch (Exception e) {
			log.error("Request error,exceptionInfo:" + ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
}
