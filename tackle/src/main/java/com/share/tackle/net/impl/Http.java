package com.share.tackle.net.impl;

import java.net.SocketTimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class Http {
	public static String sendPost(String url, Object xmlObj) {
		String result = "";
		HttpPost httpPost = new HttpPost(url);

		XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));

		String postDataXML = xStream.toXML(xmlObj);

		System.out.println("API，POST过去的数据是：");
		System.out.println(postDataXML);
		try {

			// 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
			StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
			httpPost.addHeader("Content-Type", "text/xml");
			httpPost.setEntity(postEntity);

			// 设置请求器的配置
			// RequestConfig requestConfig =
			// RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
			// httpPost.setConfig(requestConfig);

			System.out.println("executing request" + httpPost.getRequestLine());

			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity entity = response.getEntity();

			result = EntityUtils.toString(entity, "UTF-8");

		} catch (ConnectionPoolTimeoutException e) {
			// log.e("http get throw ConnectionPoolTimeoutException(wait time out)");

		} catch (ConnectTimeoutException e) {
			// log.e("http get throw ConnectTimeoutException");

		} catch (SocketTimeoutException e) {
			// log.e("http get throw SocketTimeoutException");

		} catch (Exception e) {
			// log.e("http get throw Exception");

		} finally {
			httpPost.abort();
		}

		return result;
	}
}
