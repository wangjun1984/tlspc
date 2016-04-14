package org.jzkangta.tlspc.framework.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.jzkangta.tlspc.framework.util.StringUtil;

/**
 * BaseAppRuntimeException 应用层的运行时异常的基类，在构造该类型的异常时，可以指定错误代码和相关参数，以便于异常处理类获取相关信息。
 */
public class BaseAppRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -6021077900819863433L;

	private String errorCode = null;
	private Object[] args = null;
	
    /**
     * @return errorCode
     */
    public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode 要设置的errorCode
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public BaseAppRuntimeException() {
		super();
	}
	
	public BaseAppRuntimeException(String errorCode) {
		super();
		setErrorCode(errorCode);
	}

	public BaseAppRuntimeException(Throwable cause) {
	    super(cause);
	}
	
	public BaseAppRuntimeException(String errorCode, Throwable cause) {
		super(cause);
		setErrorCode(errorCode);
	}

	public BaseAppRuntimeException(String errorCode, String message, Throwable cause) {
		this(message, cause);
		setErrorCode(errorCode);
	}
	
	public BaseAppRuntimeException(String errorCode, String message) {
		super(message);
		setErrorCode(errorCode);
	}

	public BaseAppRuntimeException(String errorCode, String message, Object[] args ) {
	    super(message);
		setErrorCode(errorCode);
	    setArgs( args );
	}
	public BaseAppRuntimeException(String errorCode, Object[] args ) {
	    super();
		setErrorCode(errorCode);
	    setArgs( args );
	}
	public BaseAppRuntimeException(String errorCode, Throwable cause, Object[] args) {
		super(cause);
		setErrorCode(errorCode);
	    setArgs( args );
	}

	public BaseAppRuntimeException(String errorCode, String message, Throwable cause, Object[] args) {
		super(message, cause);
		setErrorCode(errorCode);
	    setArgs( args );
	}

	/**
	 * @return 调用堆栈的详细信息
	 */
	public String getStackTraceString() {
		StringWriter sw = new StringWriter();
	    printStackTrace(new PrintWriter(sw));
	    return sw.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer( getClass().getName() );
		if( !StringUtil.isEmpty( getErrorCode() ) )
			ret.append(":").append( getErrorCode() );
		
        String message = getLocalizedMessage();
		if( !StringUtil.isEmpty( message ) )
			ret.append(",").append( message );
		
		if( args != null ) {
			ret.append(",args:");
			for( int i=0; i<args.length; i++ )
				ret.append( args[i] ).append(",");
		}
        return ret.toString();
	}

	/**
	 * @return 返回 args。
	 */
	public Object[] getArgs() {
		return args;
	}

	/**
	 * @param args 要设置的 args。
	 */
	public void setArgs(Object[] args) {
		this.args = args;
	}
	

}
