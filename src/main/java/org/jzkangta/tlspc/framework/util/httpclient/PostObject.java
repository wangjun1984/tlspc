package org.jzkangta.tlspc.framework.util.httpclient;

import java.util.Map;

public class PostObject {
	/**
	 * 请求的连接
	 */
	private String url;
	
	/**
	 * 提交的参数：JSON/XML/link param
	 */
	private Object content;
	
	/**
	 * 设置提交时的头部信息：User-Agent、Accept等
	 */
	private Map<String, String> headers;
	
	/**
	 * 编码：一般为UTF8
	 */
	private String charset = "UTF8";
	
	/**
	 * {@link ContentTypeEnum}
	 */
	private String contentType ;
	
	/**
	 * 代理ip和端口
	 */
	private String[] proxyIpAndPort;
	
	/**
	 * 请求的是否为https链接
	 */
	private boolean isHttps = false;
	
	private Integer timeout = 0;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String[] getProxyIpAndPort() {
		return proxyIpAndPort;
	}
	public void setProxyIpAndPort(String[] proxyIpAndPort) {
		this.proxyIpAndPort = proxyIpAndPort;
	}
	public boolean isHttps() {
		return isHttps;
	}
	public void setHttps(boolean isHttps) {
		this.isHttps = isHttps;
	}
	public Integer getTimeout() {
		return timeout;
	}
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	
}
