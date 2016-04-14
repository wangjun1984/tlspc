package org.jzkangta.tlspc.framework.web.filter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import org.jzkangta.tlspc.framework.util.StringUtil;
/**
 * 保存Spring WebApplicationContext和ServletContext环境的引用，并将HttpServletRequest，HttpServletResponse也放入线程的局部
 * 变量中，方便程序访问。
 *
 */
public class SpringWebContext {

	private static Logger log = Logger.getLogger( SpringWebContext.class );

	static WebApplicationContext springContext = null;
    static ServletContext servletContext = null;
    static ThreadLocal<HttpServletRequest> servletRequestLocal = new ThreadLocal<HttpServletRequest>();
    static ThreadLocal<HttpServletResponse> servletResponseLocal = new ThreadLocal<HttpServletResponse>();

	/**
	 * @return 返回 springContext。
	 */
	public static WebApplicationContext getSpringContext() {
		if ( springContext == null )
			springContext = ContextLoader.getCurrentWebApplicationContext();
   		return springContext;
	}
	
	/**
	 * 对于在Controller的上下文中可以引用该方法，可以获得Servlet，Controller等bean的引用
	 * @param request
	 * @return 返回 springContext
	 */
	public static WebApplicationContext getControllerSpringContext( HttpServletRequest request ) {
		WebApplicationContext wac = (WebApplicationContext) request.getAttribute( DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE );
		if ( springContext == null )
			springContext = wac;
		return wac;
	}
	
	/**
	 * 从springContext获取指定的bean
	 * @param beanName 待检索的bean Id
	 * @return 指定的bean的引用
	 */
	public static Object getSpringBean( String beanName ) {
   		try {
			return getSpringContext().getBean(beanName);
		} catch (Exception e) {
			log.error("not defined:" +  beanName + "," + e);
			return null;
		}
	}

	/**
	 * 从springContext获取指定的bean
	 * @param beanName 待检索的bean Id标识串
	 * @param clazz 待检索的bean的类型
	 * @return 指定的bean的引用
	 */
	@SuppressWarnings("unchecked")
    public static Object getSpringBean( String beanName, Class clazz ) {
		try {
			return getSpringContext().getBean(beanName, clazz);
		} catch (BeansException e) {
			log.error("not defined:" +  beanName + "," + e);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 返回urlPath对应的真实物理路径
	 * @param urlPath url
	 * @return urlPath对应的真实物理路径
	 */
	public static String getRealPath( String filePath ) {
		if( StringUtil.isEmpty( filePath ) ){
			filePath = "/";
		}
		if( filePath.startsWith( "classpath:" ) ){
			filePath = filePath.substring( "classpath:".length() );
			String path = getClassesPath();
			StringBuilder sb = new StringBuilder( path );
			if( StringUtil.isEmpty( filePath ) )
				return sb.append( "/" ).toString();
			if( filePath.startsWith( "/" ) )
				return sb.append( filePath ).toString();
			return sb.append( "/" ).append( filePath ).toString();
		}
		if( servletContext != null ){
			return servletContext.getRealPath( filePath );
		}
		if( filePath.startsWith( "/WEB-INF/" )){
			String path = getClassesPath();
			StringBuilder sb = new StringBuilder( path );
			int index = path.indexOf("/WEB-INF/classes");
			if( index > -1 )
				sb.setLength( index );
			if( StringUtil.isEmpty( filePath ) )
				return sb.append( "/" ).toString();
			if( filePath.startsWith( "/" ) )
				return sb.append( filePath ).toString();
			return sb.append( "/" ).append( filePath ).toString();
		}
		return filePath;
	}
	
	/**
	 * 返回web容器对应的classpath真实物理路径
	 * @return
	 */
	public static String getClassesPath(){
		//System.out.println( SpringWebContext.class.getCanonicalName() );
		String path = SpringWebContext.class.getResource( "/"+SpringWebContext.class.getCanonicalName().replace( '.', '/' )+".class" ).getPath();
		if( path == null){
			path = Thread.currentThread().getContextClassLoader().getResource( "." ).getPath();
			log.debug( path );
		}
		else{
			path = path.substring( 0, path.indexOf( SpringWebContext.class.getCanonicalName().replace( '.', '/' )+".class") );
		}
		if( path.endsWith( "/classes/") )
			return path;
		if( path.endsWith( ".jar!/") && path.startsWith( "file:" ) ){
			path = path.substring( 5 );
			path = path.substring( 0, path.lastIndexOf( ".jar!/" ) );
			path = path.substring( 0, path.lastIndexOf( "/" )+1 );
			return path.replace( "/lib/", "/classes/");
		}
		return path;
	}
	
	public static void main(String[] args ) throws Exception{
		System.out.println( getClassesPath() );
	}
	
	/**
	 * @return servlet上下文
	 */
	public static ServletContext getServletContext() {
		if ( servletContext == null )
			log.error("SpringContextFilter not inited");
   		return servletContext;
	}

	/**
	 * @return 当前线程的HttpServletRequest对象
	 */
	public static HttpServletRequest getServletRequest() {
   		return servletRequestLocal.get();
	}
	
	/**
	 * @return 当前线程的HttpServletResponse对象
	 */
	public static HttpServletResponse getServletResponse() {
   		return servletResponseLocal.get();
	}

	/**
	 * 释放上下文的引用
	 */
	static void destroy() {
    	springContext = null;
    	servletContext = null;
    }	
}
