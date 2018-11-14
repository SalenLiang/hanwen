package com.fahai.cc.service.util;import java.io.BufferedReader;import java.io.DataInputStream;import java.io.DataOutputStream;import java.io.File;import java.io.FileInputStream;import java.io.FileReader;import java.io.IOException;import java.io.InputStreamReader;import java.io.OutputStream;import java.net.HttpURLConnection;import java.net.URL;import java.util.ArrayList;import java.util.HashMap;import java.util.HashSet;import java.util.Iterator;import java.util.List;import java.util.Map;import java.util.Set;import javax.activation.MimetypesFileTypeMap;import org.apache.commons.lang.StringUtils;import org.apache.http.HttpEntity;import org.apache.http.NameValuePair;import org.apache.http.client.ClientProtocolException;import org.apache.http.client.config.RequestConfig;import org.apache.http.client.entity.UrlEncodedFormEntity;import org.apache.http.client.methods.CloseableHttpResponse;import org.apache.http.client.methods.HttpGet;import org.apache.http.client.methods.HttpPost;import org.apache.http.entity.StringEntity;import org.apache.http.impl.client.CloseableHttpClient;import org.apache.http.impl.client.HttpClientBuilder;import org.apache.http.message.BasicNameValuePair;import org.apache.http.util.EntityUtils;/** * <b>function:</b> HTTPUtil常用工具类 包含post 和get 方法 */public class HttpUtil {	private static final CloseableHttpClient httpClient;	public static final String CHARSET = "UTF-8";	static {		RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(10000).build();		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();	}	public static String doGet(String url, Map<String, String> params) {		return doGet(url, params, CHARSET);	}	public static String doPost(String url, Map<String, String> params) {		return doPost(url, params, CHARSET);	}	public static String doPost(String url, String params) {		return doPost(url, params, CHARSET);	}	/**	 * HTTP Get 获取内容	 * 	 * @param url	 *            请求的url地址 ?之前的地址	 * @param params	 *            请求的参数	 * @param charset	 *            编码格式	 * @return 页面内容	 */	public static String doGet(String url, Map<String, String> params, String charset) {		if (StringUtils.isBlank(url)) {			return null;		}		try {			if (params != null && !params.isEmpty()) {				List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());				for (Map.Entry<String, String> entry : params.entrySet()) {					String value = entry.getValue();					if (value != null) {						pairs.add(new BasicNameValuePair(entry.getKey(), value));					}				}				url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));			}			HttpGet httpGet = new HttpGet(url);			CloseableHttpResponse response = httpClient.execute(httpGet);			int statusCode = response.getStatusLine().getStatusCode();			if (statusCode != 200) {				httpGet.abort();				throw new RuntimeException("HttpClient,error status code :" + statusCode);			}			HttpEntity entity = response.getEntity();			String result = null;			if (entity != null) {				result = EntityUtils.toString(entity, "utf-8");			}			EntityUtils.consume(entity);			response.close();			return result;		} catch (Exception e) {					}		return null;	}	/**	 * HTTP Post 获取内容	 * 	 * @param url	 *            请求的url地址 ?之前的地址	 * @param params	 *            请求的参数	 * @param charset	 *            编码格式	 * @return 页面内容	 */	public static String doPost(String url, Map<String, String> params, String charset) {		if (StringUtils.isBlank(url)) {			return null;		}		try {			List<NameValuePair> pairs = null;			if (params != null && !params.isEmpty()) {				pairs = new ArrayList<NameValuePair>(params.size());				for (Map.Entry<String, String> entry : params.entrySet()) {					String value = entry.getValue();					if (value != null) {						pairs.add(new BasicNameValuePair(entry.getKey(), value));					}				}			}			HttpPost httpPost = new HttpPost(url);			if (pairs != null && pairs.size() > 0) {				httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));			}			CloseableHttpResponse response = httpClient.execute(httpPost);			int statusCode = response.getStatusLine().getStatusCode();			if (statusCode != 200) {				httpPost.abort();				throw new RuntimeException("HttpClient,error status code :" + statusCode);			}			HttpEntity entity = response.getEntity();			String result = null;			if (entity != null) {				result = EntityUtils.toString(entity, "utf-8");			}			EntityUtils.consume(entity);			response.close();			return result;		} catch (Exception e) {			e.printStackTrace();		}		return null;	}	/**	 * HTTP Post 获取内容 传入String类型的POSt方法	 * 	 * @param url	 *            请求的url地址 ?之前的地址	 * @param params	 *            请求的参数	 * @param charset	 *            编码格式	 * @return 页面内容	 */	public static String doPost(String url, String params, String charset) {		if (StringUtils.isBlank(url)) {			return null;		}		try {			HttpPost httpPost = new HttpPost(url);			if (params != null && params.length() > 0) {				httpPost.setEntity(new StringEntity(params, "UTF-8"));			}			CloseableHttpResponse response = httpClient.execute(httpPost);			int statusCode = response.getStatusLine().getStatusCode();			if (statusCode != 200) {				httpPost.abort();				throw new RuntimeException("HttpClient,error status code :" + statusCode);			}			HttpEntity entity = response.getEntity();			String result = null;			if (entity != null) {				result = EntityUtils.toString(entity, "utf-8");			}			EntityUtils.consume(entity);			response.close();			return result;		} catch (Exception e) {			e.printStackTrace();		}		return null;	}	/**	 * 上传图片	 * 	 * @param urlStr	 * @param textMap	 * @param fileMap	 * @return	 */	public static String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) {		String res = "";		HttpURLConnection conn = null;		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符		try {			URL url = new URL(urlStr);			conn = (HttpURLConnection) url.openConnection();			conn.setConnectTimeout(5000);			conn.setReadTimeout(30000);			conn.setDoOutput(true);			conn.setDoInput(true);			conn.setUseCaches(false);			conn.setRequestMethod("POST");			conn.setRequestProperty("Connection", "Keep-Alive");			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);			OutputStream out = new DataOutputStream(conn.getOutputStream());			// text			if (textMap != null) {				StringBuffer strBuf = new StringBuffer();				Iterator iter = textMap.entrySet().iterator();				while (iter.hasNext()) {					Map.Entry entry = (Map.Entry) iter.next();					String inputName = (String) entry.getKey();					String inputValue = (String) entry.getValue();					if (inputValue == null) {						continue;					}					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");					strBuf.append(inputValue);				}				out.write(strBuf.toString().getBytes());			}			// file			if (fileMap != null) {				Iterator iter = fileMap.entrySet().iterator();				while (iter.hasNext()) {					Map.Entry entry = (Map.Entry) iter.next();					String inputName = (String) entry.getKey();					String inputValue = (String) entry.getValue();					if (inputValue == null) {						continue;					}					File file = new File(inputValue);					String filename = file.getName();					String contentType = new MimetypesFileTypeMap().getContentType(file);					if (filename.endsWith(".png")) {						contentType = "image/png";					}					if (contentType == null || contentType.equals("")) {						contentType = "application/octet-stream";					}					StringBuffer strBuf = new StringBuffer();					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename							+ "\"\r\n");					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");					out.write(strBuf.toString().getBytes());					DataInputStream in = new DataInputStream(new FileInputStream(file));					int bytes = 0;					byte[] bufferOut = new byte[1024];					while ((bytes = in.read(bufferOut)) != -1) {						out.write(bufferOut, 0, bytes);					}					in.close();				}			}			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();			out.write(endData);			out.flush();			out.close();			// 读取返回数据			StringBuffer strBuf = new StringBuffer();			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));			String line = null;			while ((line = reader.readLine()) != null) {				strBuf.append(line).append("\n");			}			res = strBuf.toString();			reader.close();			reader = null;		} catch (Exception e) {			System.out.println("发送POST请求出错。" + urlStr);			e.printStackTrace();		} finally {			if (conn != null) {				conn.disconnect();				conn = null;			}		}		return res;	}		public static void main(String[] args) {		String doGet = HttpUtil.doGet("http://pingtai.fahaicc.com:81/fhfk/entry?authCode=", null);		System.out.println(doGet);	}}