package org.jzkangta.tlspc.framework.util.httpclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jzkangta.tlspc.framework.util.file.FileUtil;

/**
 * httpclient工具类
 * @author hongyuhao
 *
 */
public class HttpClientUtil {
	
	private final static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);	
	
	private static Map<String ,String> headMap= new HashMap<String,String>();
	
	static {
		headMap.put("User-Agent","Mozilla/4.0 (compatible; MSIE 7.0;)" );
		headMap.put("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint, */*");
		headMap.put("Bizmp-Version", "1.0");
	}
	
//	protected static String propertiesFileName = "wechat";
	private static final String downloadPath = "/download/"; 
	protected static Integer TIME_OUT ;
	
	static{
		TIME_OUT = 7000;
		//PropertyUtil.getIntProperty(propertiesFileName, "timeout" , 20000);
	}
	/**
	 * 说明：
	 * 提交json：ContentType为{@link ContentTypeEnum} JSON， Content为json格式的字符串
	 * 提交xml：ContentType为{@link ContentTypeEnum} XML， Content为xml格式的字符串
	 * 提交form：ContentType为{@link ContentTypeEnum} WWW_FORM， Content为链接参数格式的字符串
	 * 提交文件：ContentType为{@link ContentTypeEnum} MULTIPART_FORM， Content为上传的文件
	 * 
	 * 返回值根据不同返回值而定，如果是json则返回jsonObject，普通文件则先保存在本地再返回文件路径，文本返回文本内容
	 * 
	 * 此方法暂时不支持MULTIPART_FORM 以后再实现,（已支持文件上传）
	 * @param postObject
	 * @return
	 * @throws Exception 
	 */
	public static Object post(PostObject postObject ){
		JSONObject returnJson = null;
		HttpClient client = null;
		try {
			client = getHttpClientInstance(postObject);
			
			HttpPost post = getHttpPostInstance(postObject);
			
			//开始请求
			HttpResponse res = client.execute(post);
			if(res.containsHeader("content-type")) {
				String head = res.getHeaders("content-type")[0].getValue();
				String contentType = head.split(";")[0];
				
				if(contentType.equalsIgnoreCase(ResponseContentTypeEnum.CONTENT_TYPE_JSON.getName())) {
					String strResult = EntityUtils.toString(res.getEntity(), postObject.getCharset());// 取得返回的字符串
					return (JSONObject) JSON.parse(strResult);
				} else if("text".equals(ResponseContentTypeEnum.getByName(contentType).getType())){
					return EntityUtils.toString(res.getEntity(), postObject.getCharset());// 取得返回的字符串
				} else {
					return "return nothing";
				}
			} else {
				return EntityUtils.toString(res.getEntity(), postObject.getCharset());
			}
			
		}catch(UnsupportedEncodingException uee){
			returnJson = (JSONObject) JSON.parse("{tip:\"内容编码失败\"}");
			log.error("func[post] postObject[{}] exception[{} - {}] desc[fail]",//
					new Object[] {JSON.toJSONString(postObject) , uee.getMessage(), Arrays.deepToString(uee.getStackTrace()) });
		}
		catch (Exception e) {
			returnJson = (JSONObject) JSON.parse("{tip:\"连接获取数据失败\"}");
			log.error("func[post] postObject[{}] exception[{} - {}] desc[fail]",//
					new Object[] {JSON.toJSONString(postObject) , e.getMessage(), Arrays.deepToString(e.getStackTrace()) });
		} finally {
			if(client != null)
				client.getConnectionManager().shutdown();// 关闭连接 ,释放资源
		}
		return returnJson;
	}
	
	/**
	 * 
	 * @param postObject
	 * @return
	 */
	public static Object put(PostObject postObject){
		JSONObject returnJson = null;
		HttpClient client = null;
		try {
			client = getHttpClientInstance(postObject);
			HttpPut request = getHttpPutInstance(postObject);
			HttpResponse res = client.execute(request);
			//System.out.println(JSON.toJSONString(res));
			if(res.containsHeader("content-type")) {
				String head = res.getHeaders("content-type")[0].getValue();
				String contentType = head.split(";")[0];
				
				if(contentType.equalsIgnoreCase(ResponseContentTypeEnum.CONTENT_TYPE_JSON.getName())) {
					String strResult = EntityUtils.toString(res.getEntity(), postObject.getCharset());// 取得返回的字符串
					return (JSONObject) JSON.parse(strResult);
				} else if("text".equals(ResponseContentTypeEnum.getByName(contentType).getType())){
					return EntityUtils.toString(res.getEntity(), postObject.getCharset());// 取得返回的字符串
				} else {
					return "return nothing";
				}
			} else {
				return EntityUtils.toString(res.getEntity(), postObject.getCharset());
			}
		}catch(UnsupportedEncodingException uee){
			returnJson = (JSONObject) JSON.parse("{tip:\"内容编码失败\"}");
			log.error("func[put] postObject[{}] exception[{} - {}] desc[fail]",//
					new Object[] {JSON.toJSONString(postObject) , uee.getMessage(), Arrays.deepToString(uee.getStackTrace()) });
		}
		catch (Exception e) {
			returnJson = (JSONObject) JSON.parse("{tip:\"连接获取数据失败\"}");
			log.error("func[put] postObject[{}] exception[{} - {}] desc[fail]",//
					new Object[] {JSON.toJSONString(postObject) , e.getMessage(), Arrays.deepToString(e.getStackTrace()) });
		} finally {
			if(client != null)
				client.getConnectionManager().shutdown();// 关闭连接 ,释放资源
		}
		return returnJson;
	}
	
	/**
	 * postObject name :new String[]{"file","file"},  
       postObject fileBody : new FileBody[]{  
							                new FileBody(new File("G:/workspace/multiuploadClient/src/multi.cer")),  
							                new FileBody(new File("G:/workspace/multiuploadClient/src/client.keystore"))  
							                }  
	 * @param postObject
	 */
	public static Object multipartPost(MultipartPostObject postObject){
		JSONObject returnJson = null;
		HttpClient client = null;
		try {
			client = getHttpClientInstance(postObject);
			
			HttpPost post = getHttpPostInstance(postObject);
			
			HttpResponse res = client.execute(post);
			
			String head = res.getHeaders("content-type")[0].getValue();
	
			String strResult = EntityUtils.toString(res.getEntity(), postObject.getCharset());// 取得返回的字符串
			
			log.info("func[multipartPost] response[{}] ",strResult);
			
			return (JSONObject) JSON.parse(strResult);
			
		}catch(UnsupportedEncodingException uee){
			returnJson = (JSONObject) JSON.parse("{tip:\"内容编码失败\"}");
			log.error("func[multipartPost] postObject[{}] exception[{} - {}] desc[fail]",//
					new Object[] {JSON.toJSONString(postObject) , uee.getMessage(), Arrays.deepToString(uee.getStackTrace()) });
		}
		catch (Exception e) {
			returnJson = (JSONObject) JSON.parse("{tip:\"连接获取数据失败\"}");
			log.error("func[multipartPost] postObject[{}] exception[{} - {}] desc[fail]",//
					new Object[] {JSON.toJSONString(postObject) , e.getMessage(), Arrays.deepToString(e.getStackTrace()) });
		} finally {
			if(client != null)
				client.getConnectionManager().shutdown();// 关闭连接 ,释放资源
		}
		return returnJson;
	}
	
	
	/**
	 * 
	 * @param postObject
	 * @return
	 */
	public static Object get(PostObject postObject){
		JSONObject returnJson = null;
		HttpClient client = null;
		try {
			client = getHttpClientInstance(postObject);
			HttpGet httpGet = getHttpGetInstance(postObject);
			//开始请求
			HttpResponse res = client.execute(httpGet);
			if(res.containsHeader("content-type")) {
				String head = res.getHeaders("content-type")[0].getValue();
				String contentType = head.split(";")[0];
				
				if(contentType.equalsIgnoreCase(ResponseContentTypeEnum.CONTENT_TYPE_JSON.getName())) {
					String strResult = EntityUtils.toString(res.getEntity(), postObject.getCharset());// 取得返回的字符串
					return (JSONObject) JSON.parse(strResult);
				}else {
					if(res.containsHeader("Content-disposition")) {
						String disposition = res.getHeaders("Content-disposition")[0].getValue();
						String fileName = disposition.substring(disposition.indexOf("=")+2, disposition.lastIndexOf("\""));
						File file = new File(FileUtil.getSaveFilePath(System.getProperty("user.dir"), downloadPath, fileName));
						FileOutputStream fos = new FileOutputStream(file);
						FileUtil.pipeTransform(res.getEntity().getContent(), fos);
						return file.getAbsolutePath();
					}
					else{
						return EntityUtils.toString(res.getEntity(), postObject.getCharset());// 取得返回的字符串
					}
				}
			} else {
				return EntityUtils.toString(res.getEntity(), postObject.getCharset());
			}
			
		} catch(URISyntaxException uee){
			returnJson = (JSONObject) JSON.parse("{tip:\"URI格式有误\"}");
			log.error("func[get] postObject[{}] exception[{} - {}] desc[fail]",//
					new Object[] {JSON.toJSONString(postObject) , uee.getMessage(), Arrays.deepToString(uee.getStackTrace()) });
		} catch (Exception e) {
			returnJson = (JSONObject) JSON.parse("{tip:\"连接获取数据失败\"}");
			log.error("func[get] postObject[{}] exception[{} - {}] desc[fail]",//
					new Object[] {JSON.toJSONString(postObject) , e.getMessage(), Arrays.deepToString(e.getStackTrace()) });
		} finally {
			if(client != null)
				client.getConnectionManager().shutdown();// 关闭连接 ,释放资源
		}
		return returnJson;
	}
	
	/**
	 * 初始化HttpClient，设置代理，超时时间，是否使用TSL协议
	 * @param postObject
	 * @return
	 * @throws Exception
	 */
	public static HttpClient getHttpClientInstance (PostObject postObject) throws Exception{
		HttpClient client;
		
		if(postObject.isHttps())
			client = new DefaultHttpClient(getThreadSafeClientConnManager());
		else
			client = new DefaultHttpClient();
		//设置代理
		String proxyArray[] = postObject.getProxyIpAndPort();
		if(null != proxyArray && proxyArray.length > 1){
			HttpHost proxy = new HttpHost(proxyArray[0], Integer.parseInt(proxyArray[1]));
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		if (postObject.getTimeout() > 0) {
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,postObject.getTimeout()); 
		} else {
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,TIME_OUT); 
		}
		return client;
	}
	
	/**
	 * 初始化HttpPut，设置heads，参数
	 * @param postObject
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static HttpPut getHttpPutInstance (PostObject postObject) throws UnsupportedEncodingException{
		HttpPut request = new HttpPut(postObject.getUrl());
		
		if (postObject.getHeaders() != null) {
			for (String header : postObject.getHeaders().keySet()) {
				request.setHeader(header, postObject.getHeaders().get(header));
			}
		} else{
			for(String header : headMap.keySet()){
				request.setHeader(header, headMap.get(header));
			}
		}
		
		StringEntity entity = new StringEntity((String) postObject.getContent(), postObject.getCharset());
		entity.setContentEncoding(postObject.getCharset());
		entity.setContentType(postObject.getContentType());
		request.setEntity(entity);

		return request;
	}
	
	/**
	 * 初始化HttpPost，设置heads，参数
	 * @param postObject
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static HttpPost getHttpPostInstance (PostObject postObject) throws UnsupportedEncodingException{
		//设置头部信息
		HttpPost post = new HttpPost(postObject.getUrl());
		if (postObject.getHeaders() != null) {
			for (String header : postObject.getHeaders().keySet()) {
				post.addHeader(header, postObject.getHeaders().get(header));
			}
		} else{
			for(String header : headMap.keySet()){
				post.addHeader(header, headMap.get(header));
			}
		}
		
		//设置提交的内容
		if(postObject.getContentType().equalsIgnoreCase(ContentTypeEnum.MULTIPART_FORM.getType())) {
			MultipartPostObject multipartPostObject = (MultipartPostObject)postObject;
			MultipartEntity reqEntity = new MultipartEntity();
			for (int i = 0; i < multipartPostObject.getFileBodyName().length; i++) {  
	            reqEntity.addPart(multipartPostObject.getFileBodyName()[i], multipartPostObject.getFileBody()[i]);  
	        } 
			for (int i = 0; i < multipartPostObject.getFieldName().length; i++) {  
	            reqEntity.addPart(multipartPostObject.getFieldName()[i], multipartPostObject.getFieldValue()[i]);  
	        }
			post.setEntity(reqEntity);
			
		} else {
			StringEntity entity = new StringEntity((String) postObject.getContent(), postObject.getCharset());
			entity.setContentEncoding(postObject.getCharset());
			entity.setContentType(postObject.getContentType());
			post.setEntity(entity);
		}
		
		return post;
	}
	
	/**
	 * 初始化HttpGet，设置heads，参数
	 * @param postObject
	 * @return
	 * @throws URISyntaxException
	 */
	public static HttpGet getHttpGetInstance (PostObject postObject) throws URISyntaxException{
		HttpGet get = new HttpGet(postObject.getUrl()); 
		//设置头部信息
		if (postObject.getHeaders() != null) {
			for (String header : postObject.getHeaders().keySet()) {
				get.addHeader(header, postObject.getHeaders().get(header));
			}
		} else{
			for(String header : headMap.keySet()){
				get.addHeader(header, headMap.get(header));
			}
		}
		
		
		//设置提交的内容
		get.setURI(new URI(get.getURI().toString() + "?" + postObject.getContent())); 
		return get;
	}

	/**
	 * 获取ssl连接
	 * @return
	 * @throws Exception
	 */
	private static ThreadSafeClientConnManager getThreadSafeClientConnManager() throws Exception{
		//SSLContext sslContext = SSLContext.getInstance("SSL");
		
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
        };
        ctx.init(null, new TrustManager[] { tm }, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("https", 443, ssf));
        ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);
        //ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);
        
        return mgr;
        
    }
}
