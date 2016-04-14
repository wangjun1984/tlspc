package org.jzkangta.tlspc.framework.web.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;

import org.jzkangta.tlspc.framework.util.StringUtil;

/**
 * 对http回答信息进行编码转换或压缩
 *
 */
public class ResponseEncodingFilter implements Filter {

	private Logger log = Logger.getLogger( getClass() );

    /** 
     * 最终输出的编码格式
     */
    protected String encoding = "GBK";

    /** 
     * jsp, java bean的默认编码格式
     * 
     */
    protected String defaultEncoding = "GBK";
    
    /**
     * 排除不需要过滤的url
     */
    private String excludeUrl = "";
    private String[] excludeUrlArray = null;
    
    /**
     * 是否压缩内容
     */
    protected boolean gzip = false;

	public ResponseEncodingFilter() {
		super();
	}


	public void destroy() {
	}
	
    /**
     * 初始化。从web.xml的配置中装载配置信息：defaultEncoding，encoding，gzip
     * 
     * @param filterConfig
     * @throws ServletException
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        // 从配置信息中取encoding和ignore参数
        String value = filterConfig.getInitParameter("defaultEncoding");
        if (value != null)
            this.defaultEncoding = value;
        value = filterConfig.getInitParameter("encoding");
        if (value != null)
            this.encoding = value;
        value = filterConfig.getInitParameter("gzip");
        if (value != null && (value.equalsIgnoreCase("true")|| value.equalsIgnoreCase("yes") ) )
            this.gzip = true;

        excludeUrl = filterConfig.getInitParameter("excludeUrl");
		if( !StringUtil.isEmpty( excludeUrl ) ){
			excludeUrlArray = excludeUrl.split(",");
		}
        
        log.debug("responseEncoding filter inited");
    }

    /**
     * 过滤处理。
     * 
     * @param req   Servlet请求
     * @param res  Servlet响应
     * @param chain  过滤器链
     * @throws IOException IO异常
     * @throws ServletException  Servlet异常
     */
    
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest) req;
    	HttpServletResponse response = (HttpServletResponse)res;
    	
		// 获取请求的URL
		StringBuffer requestUrl = request.getRequestURL();
		String queryString = request.getQueryString();

		if (queryString != null && queryString.length() > 0) {
			requestUrl.append('?');
			requestUrl.append(queryString);
		}
		
		String currentRequestUrl = requestUrl.toString();
    	if( excludeUrlArray != null ){
			for (int i = 0; i < excludeUrlArray.length; i++) {
				if (currentRequestUrl.indexOf(excludeUrlArray[i]) >= 0) {
					chain.doFilter(req, res);
					return;
				}
			}
		}
    	
    	EncodingResponseWrapper responseWrapper = new EncodingResponseWrapper(response);
    	chain.doFilter( request, responseWrapper );
    	if( (!responseWrapper.needConvert) && !gzip )
    		return;
		String content = responseWrapper.writer.toString();
    	if( responseWrapper.needConvert ){
	        log.debug("encoding converted ." );
    		content = StringUtil.iso2gbk( content );
    	}
    	if( gzip ){
    		String requestAcceptEncoding  = request.getHeader( "Accept-Encoding");
    		if( requestAcceptEncoding!=null && (requestAcceptEncoding.indexOf("gzip")>-1 || requestAcceptEncoding.indexOf("deflate") > -1 ) ) {
    			byte[] data = zip( content );
    	        log.debug("gzipped,ContentLength:" + content.length() + " to " + data.length );
        		response.setContentLength( data.length );
        		response.setHeader( "Content-Encoding", "gzip" );
        		response.getOutputStream().write( data );
        		response.getOutputStream().flush();
    			return;
    		}
    	}
    	if(!StringUtil.isEmpty(content)){
    		response.getWriter().println( content );
    		response.getWriter().flush();
    	}
    }
    
    private byte[] zip( String src ) throws IOException{
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	GZIPOutputStream zipOut = new  GZIPOutputStream( out );
    	zipOut.write( src.getBytes (encoding) );
    	zipOut.finish();
    	return out.toByteArray();
    }
    
    private class EncodingResponseWrapper extends HttpServletResponseWrapper { 

    	private String internalEncoding = null;
    	private boolean needConvert = false;
    	private StringWriter writer = new StringWriter();

    	
    	public EncodingResponseWrapper(HttpServletResponse response) { 
	    	super(response); 
    	} 

        private boolean isIso88591( String enc ){
        	String e = null;
        	if ( enc !=null  ) 
        		e = enc.toLowerCase();
        	else
        		e = System.getProperty( "file.encoding" ).toLowerCase();
        	return e.indexOf("iso")>-1 && e.indexOf("8859")>-1 && e.endsWith("1") ;
        }

    	public void setContentType(String value) {
    		needConvert = false;
    		if( value.toLowerCase().indexOf("text/") <= -1 && value.toLowerCase().indexOf("javascript") <= -1){
    			super.setContentType( value );
    			return;
    		}
    		int index = value.toLowerCase().indexOf("charset="); 
			String contentType = null;
    		if( index > -1 ){
    			internalEncoding = value.substring( index + "charset=".length() );
				contentType = value.substring(0,index);
    		}
    		else{
    			internalEncoding = super.getCharacterEncoding();
    			contentType = value+";";
    			if( internalEncoding == null )
    				internalEncoding = defaultEncoding;
    		}
			if( !encoding.equalsIgnoreCase( internalEncoding ) &&  isIso88591(internalEncoding) ){
	    		needConvert = true;
			}
			contentType = contentType + "charset="+encoding;
    		log.debug("contentType:"+ contentType +", convert "+ internalEncoding + " to "+ encoding );
    		super.setContentType( contentType );
    	} 
    	
    	public void setHeader( String name, String value){
    		if( !name.equalsIgnoreCase( "Content-Type") )
    			super.setHeader( name, value);
    		else{
    			setContentType( value );
    		}
    	}
    	
    	
    	public void addHeader( String name, String value){
    		if( !name.equalsIgnoreCase( "Content-Type") )
    			super.addHeader( name, value);
    		else{
    			setContentType( value );
    		}
    	}

    	public PrintWriter getWriter() throws IOException{
    		if( (!needConvert) && !gzip )
    			return super.getWriter();
    		return new PrintWriter( writer, true );
    	}
    	
    	public ServletOutputStream getOutputStream() throws IOException{
    		if( !needConvert )
    			return super.getOutputStream();
			return new EncodingServletOutputStream( super.getOutputStream() );
    	}

    	private class EncodingServletOutputStream extends ServletOutputStream{
    		private ServletOutputStream out;
    		public EncodingServletOutputStream( ServletOutputStream out ){
    			this.out = out;
    		}
			public void print(String arg0) throws IOException {
				out.print(StringUtil.iso2gbk( arg0 ));
			}
			public void println(String arg0) throws IOException {
				out.println(StringUtil.iso2gbk( arg0 ));
			}
			public void write(int arg0) throws IOException {
				out.write( arg0 );
			}
			public void print(char arg0) throws IOException {
				out.print( arg0 );
			}
			public void println(char arg0) throws IOException {
				out.println(arg0);
			}
			public void print(boolean arg0) throws IOException {
				out.print(arg0);
			}
			public void print(double arg0) throws IOException {
				out.print(arg0);
			}
			public void print(float arg0) throws IOException {
				out.print(arg0);
			}
			public void print(int arg0) throws IOException {
				out.print(arg0);
			}
			public void print(long arg0) throws IOException {
				out.print(arg0);
			}
			public void println(boolean arg0) throws IOException {
				out.println(arg0);
			}
			public void println(double arg0) throws IOException {
				out.println(arg0);
			}
			public void println(float arg0) throws IOException {
				out.println(arg0);
			}
			public void println(int arg0) throws IOException {
				out.println(arg0);
			}
			public void println(long arg0) throws IOException {
				out.println(arg0);
			}
			public void close() throws IOException {
				out.close();
			}
			public void flush() throws IOException {
				out.flush();
			}
			public void write(byte[] arg0, int arg1, int arg2) throws IOException {
				out.write(arg0, arg1, arg2);
			}
			public void write(byte[] arg0) throws IOException {
				out.write(arg0);
			}
			public void println() throws IOException {
				out.println();
			}
    		
    		
    	}

   	} 


}
