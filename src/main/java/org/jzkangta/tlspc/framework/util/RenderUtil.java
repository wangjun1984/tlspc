package org.jzkangta.tlspc.framework.util;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;


/**
 * 
 * 
 *
 */
public class RenderUtil {
	
	//-- header 常量定义 --//
	private static final String ENCODING_PREFIX = "encoding";
	private static final String NOCACHE_PREFIX = "no-cache";
	private static final String ENCODING_DEFAULT = "UTF-8";
	private static final boolean NOCACHE_DEFAULT = true;
	
	//-- content-type 常量定义 --//
	private static final String TEXT_TYPE = "text/plain";
	private static final String JSON_TYPE = "text/json";
	private static final String XML_TYPE = "text/xml";
	private static final String HTML_TYPE = "text/html";
	private static final String JS_TYPE = "text/javascript";
	
	
	

	private static Logger logger = Logger.getLogger("Struts2Util");

	

	/**
	 * 直接输出内容的简便函数.
	 * 
	 * eg. render("text/plain", "hello", "encoding:GBK"); render("text/plain",
	 * "hello", "no-cache:false"); render("text/plain", "hello", "encoding:GBK",
	 * "no-cache:false");
	 * 
	 * @param headers
	 *            可变的header数组，目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true.
	 */
	public static void render(final String contentType, final String content,HttpServletResponse response,
			final String... headers) {

		try {
			// 分析headers参数
			String encoding = ENCODING_DEFAULT;
			boolean noCache = NOCACHE_DEFAULT;
			for (String header : headers) {
				String headerName = StringUtils.substringBefore(header, ":");
				String headerValue = StringUtils.substringAfter(header, ":");

				if (StringUtils.equalsIgnoreCase(headerName, ENCODING_PREFIX)) {
					encoding = headerValue;
				} else if (StringUtils.equalsIgnoreCase(headerName,
						NOCACHE_PREFIX)) {
					noCache = Boolean.parseBoolean(headerValue);
				} else
					throw new IllegalArgumentException(headerName
							+ "不是一个合法的header类型");
			}

			
			// 设置headers参数
			String fullContentType = contentType + ";charset=" + encoding;
			response.setContentType(fullContentType);
			if (noCache) {
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
			}
			response.getWriter().write(content);
			response.getWriter().flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 直接输出文本.
	 * @see #render(String, String, String...)
	 */
	public static void renderText(final String text,HttpServletResponse response, final String... headers) {
		render(TEXT_TYPE, text, response,headers);
	}

	/**
	 * 直接输出HTML.
	 * @see #render(String, String, String...)
	 */
	public static void renderHtml(final String html, HttpServletResponse response,final String... headers) {
		render(HTML_TYPE, html, response,headers);
	}

	/**
	 * 直接输出XML.
	 * @see #render(String, String, String...)
	 */
	public static void renderXml(final String xml,  HttpServletResponse response,final String... headers) {
		render(XML_TYPE, xml, response,headers);
	}
	
	/**
	 * 直接输出JSON.
	 * 
	 * @param jsonString json字符串.
	 * @see #render(String, String, String...)
	 */
	public static void renderJson(final String jsonString,  HttpServletResponse response,final String... headers) {
		render(JSON_TYPE, jsonString, response,headers);
	}
	/**
	 * 直接输出Js.
	 * 
	 * @param jsString js字符串.
	 * @see #render(String, String, String...)
	 */
	public static void renderJs(final String jsString,  HttpServletResponse response,final String... headers) {
		render(JS_TYPE, jsString, response,headers);
	}
}
