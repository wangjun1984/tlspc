package org.jzkangta.tlspc.framework.exception;


/**
 * 业务逻辑中的运行时异常类
 *
 */
public class BusinessRuntimeException extends BaseAppRuntimeException {

	private static final long serialVersionUID = 1325601692472497460L;

	public BusinessRuntimeException(Exception e) {
		super(e);
	}

	public BusinessRuntimeException() {
		super();
		// TODO 自动生成构造函数存根
	}

	public BusinessRuntimeException(String errorCode, Object[] args) {
		super(errorCode, args);
		// TODO 自动生成构造函数存根
	}

	public BusinessRuntimeException(String errorCode, String message, Object[] args) {
		super(errorCode, message, args);
		// TODO 自动生成构造函数存根
	}

	public BusinessRuntimeException(String errorCode, String message, Throwable cause, Object[] args) {
		super(errorCode, message, cause, args);
		// TODO 自动生成构造函数存根
	}

	public BusinessRuntimeException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
		// TODO 自动生成构造函数存根
	}

	public BusinessRuntimeException(String errorCode, String message) {
		super(errorCode, message);
		// TODO 自动生成构造函数存根
	}

	public BusinessRuntimeException(String errorCode, Throwable cause, Object[] args) {
		super(errorCode, cause, args);
		// TODO 自动生成构造函数存根
	}

	public BusinessRuntimeException(String errorCode, Throwable cause) {
		super(errorCode, cause);
		// TODO 自动生成构造函数存根
	}

	public BusinessRuntimeException(String errorCode) {
		super(errorCode);
		// TODO 自动生成构造函数存根
	}

	public BusinessRuntimeException(Throwable cause) {
		super(cause);
		// TODO 自动生成构造函数存根
	}


}
