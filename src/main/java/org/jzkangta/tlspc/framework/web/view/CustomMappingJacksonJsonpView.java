package org.jzkangta.tlspc.framework.web.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import org.jzkangta.tlspc.framework.util.StringUtil;

/**
 * 重写MappingJacksonJsonView方法.
 * MappingJacksonJsonView会返回 {model类名:{内容}}
 * 重写后返回{内容} 
 * @author Administrator
 *
 *配置：
 *     	<property name="mediaTypes">
 *			<map>
 *				<entry key="jsonp" value="application/javascript"/>
 *			</map>
 *		</property>
 *		<property name="defaultViews">
 *			<list>
 *				<bean class="org.jzkangta.tlspc.framework.web.view.CustomMappingJacksonJsonpView" ></bean>
 *			</list>
 *		</property>		
 *访问例子：
 *http://xxxx.39.net/yy.jsonp?callback=aaa
 */
public class CustomMappingJacksonJsonpView extends MappingJackson2JsonView {
	private Logger log = Logger.getLogger( getClass() );
	public static final String DEFAULT_CONTENT_TYPE = "application/javascript";

	private String charset = "UTF-8";
	
	@Override
	public String getContentType() {
		return DEFAULT_CONTENT_TYPE;
	}

	protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
		log.debug("prepareResponse>>"+this.getCharset());
		response.setContentType(getContentType());
		response.setCharacterEncoding(this.getCharset());
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
		response.addDateHeader("Expires", 1L);
	}
	
	@Override
	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if ("GET".equals(request.getMethod().toUpperCase())) {
			String callback = request.getParameter("callback");
			if (!StringUtil.isEmpty(callback)) {
				log.debug("callback>>"+callback);
				
				response.getOutputStream().write(new String(callback + "(").getBytes());
				super.render(model, request, response);
				response.getOutputStream().write(new String(");").getBytes());
				response.setContentType(DEFAULT_CONTENT_TYPE);
			}
			else {
				super.render(model, request, response);
			}
		}
		else {
			super.render(model, request, response);
		}
	}
	
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
}
