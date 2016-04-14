
package org.jzkangta.tlspc.framework.web.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 将SpringContext和ServletContext保存到线程局部变量SpringWebContext中，方便获取。
 *
 */
public class SpringContextFilter  extends HttpServlet implements Filter {

	private static final long serialVersionUID = 7379407269970059301L;

	private static Logger log = Logger.getLogger( SpringContextFilter.class );

    /**
     * 初始化Filter。
     * 
     * @param filterConfig
     * @throws ServletException
     */
    public void init(FilterConfig filterConfig) throws ServletException {
		if( SpringWebContext.servletContext == null ) 
			SpringWebContext.servletContext = filterConfig.getServletContext();
		
		if( SpringWebContext.springContext == null ) 
			SpringWebContext.springContext = WebApplicationContextUtils.getRequiredWebApplicationContext( filterConfig.getServletContext() );

		log.info("SpringContextFilter is inited");
    }
    
	/**
	 * 初始化 servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		if( SpringWebContext.servletContext == null ) 
			SpringWebContext.servletContext = getServletContext();
		
		if( SpringWebContext.springContext == null ) 
			SpringWebContext.springContext = WebApplicationContextUtils.getRequiredWebApplicationContext( getServletContext() );

		log.info("SpringContextFilter is inited as a servlet");
	}
	
	/**
	 * The doGet method of the servlet. just a test.<br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			SpringWebContext.getSpringContext().getBean( "dataSource");
			out.print( "OK. Just a test." );
		} catch (BeansException e) {
			out.print( "OK. Just a test. but Spring context no bean: dataSource");
		} 
	}	

    /**
     * Filter过滤处理。
     * 
     * @param servletRequest   Servlet请求
     * @param servletResponse  Servlet响应
     * @param filterChain  过滤器链
     * @throws IOException IO异常
     * @throws ServletException  Servlet异常
     */
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
    	try {
    		SpringWebContext.servletRequestLocal.set( (HttpServletRequest)servletRequest );
    		SpringWebContext.servletResponseLocal.set( (HttpServletResponse)servletResponse );
    		filterChain.doFilter(servletRequest, servletResponse);
    	}
    	finally {
    		SpringWebContext.servletRequestLocal.remove();
    		SpringWebContext.servletResponseLocal.remove();
    	}
    }

    /**
     * 释放资源。
     */
    public void destroy() {
    	SpringWebContext.destroy();
    }



}
