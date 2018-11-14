package com.fahai.cc.service.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;


/**
 * http请求工具类
 */
public class HttpClientUtil {
	
	
	private static HttpPost buildHttpPost(String url){
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Cache-Control", "max-age=0");
		httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpPost.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//		httpPost.setHeader("Accept-Language", "en-US,en;q=0.8");
		httpPost.setHeader("Accept-Charset", "UTF-8,utf-8;q=0.7,*;q=0.3");
		httpPost.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		return httpPost;
	}

	public static String query(String url, int timeout) throws ClientProtocolException, IOException {
		
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = buildHttpPost(url);
		//设置请求超时时间
		RequestConfig requestConfig = RequestConfig.custom()
		        .setConnectTimeout(2*60*1000).setConnectionRequestTimeout(timeout)
		        .setSocketTimeout(2*60*1000).build();
		httpPost.setConfig(requestConfig);
		StringBuilder sb = new StringBuilder();
		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		int statusCode = response.getStatusLine().getStatusCode();
		if(200 != statusCode){
			return null;
		}
		if (entity != null) {
			BufferedReader bf = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
			// 文件处理方式
			String line;
			while ((line = bf.readLine()) != null) {
				sb.append(line + "\n");
			}
			httpPost.abort();
		}
		httpclient.getConnectionManager().shutdown();
		return sb.toString();
	}

	/**
	 * Url请求
	 * @param url	Url
	 * @param json	参数
	 * @return	返回数据
	 * @throws Exception	抛出的异常
	 */
	public static String load(String url,String json) throws Exception{
		URL restURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type","application/json;");
		PrintWriter out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));
		out.write(json);
		out.close();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
		String line;
		StringBuffer stringBuffer = new StringBuffer();
		while(null != (line=bReader.readLine())){
			stringBuffer.append(line);
		}
		bReader.close();
		return stringBuffer.toString();
	}
	
	/**
	 * 	获取HTTP请求状态码
	 */
	public static int getRequestSatusCode(String url, int timeout) throws ClientProtocolException, IOException {
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = buildHttpPost(url);
		//设置请求超时时间
		RequestConfig requestConfig = RequestConfig.custom()
		        .setConnectTimeout(5000).setConnectionRequestTimeout(timeout)  
		        .setSocketTimeout(5000).build();  
		httpPost.setConfig(requestConfig);
		
		HttpResponse response = httpclient.execute(httpPost);
		int statusCode = response.getStatusLine().getStatusCode();
		httpPost.abort();
		return statusCode;
	}
	
	//获取ip地址
	public static String getIpAddress(HttpServletRequest request) {  
		String ip = request.getHeader("x-forwarded-for");
		//log.debug("request.getHeader(\"x-forwarded-for\")=" + ip);

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		ip = request.getHeader("X-Forwarded-For");
		//log.debug("request.getHeader(\"X-Forwarded-For\")=" + ip);
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		ip = request.getHeader("Proxy-Client-IP");
		//log.debug("request.getHeader(\"Proxy-Client-IP\")=" + ip);
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		ip = request.getHeader("WL-Proxy-Client-IP");
		//log.debug("request.getHeader(\"WL-Proxy-Client-IP\")=" + ip);
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		ip = request.getHeader("HTTP_CLIENT_IP");
		//log.debug("request.getHeader(\"HTTP_CLIENT_IP\")=" + ip);
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		//log.debug("request.getHeader(\"HTTP_X_FORWARDED_FOR\")=" + ip);
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		ip = request.getRemoteAddr();
		//log.debug("request.getRemoteAddr()=" + ip);
		}

		if(null != ip && ip.indexOf(',') != -1){
		//如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串 IP 值
		//取X-Forwarded-For中第一个非unknown的有效IP字符串
		//如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100
		//用户真实IP为： 192.168.1.110 
		//注意:当访问地址为 localhost 时 地址格式为 0:0:0:0:0:0:1
		//log.debug("ip=" + ip);
		String[] ips = ip.split(",");
		for (int i = 0; i < ips.length; i++) {
		if(null != ips[i] && !"unknown".equalsIgnoreCase(ips[i])){
		ip = ips[i];
		break;
		}
		}
		if("0:0:0:0:0:0:1".equals(ip)){
		//log.warn("由于客户端访问地址使用 localhost，获取客户端真实IP地址错误，请使用IP方式访问");
		}
		}

		if("unknown".equalsIgnoreCase(ip)){
		//log.warn("由于客户端通过Squid反向代理软件访问，获取客户端真实IP地址错误，请更改squid.conf配置文件forwarded_for项默认是为on解决");
		}
		//log.debug("====================================================");
		return ip; 
    }
}
