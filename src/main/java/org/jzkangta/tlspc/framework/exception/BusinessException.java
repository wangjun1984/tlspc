package org.jzkangta.tlspc.framework.exception;



/**
 * 表示业务逻辑的checked异常类
 *
 */
public class BusinessException extends BaseAppException {

	private static final long serialVersionUID = -2403009184390388310L;

	public BusinessException() {
		super();
		// TODO 自动生成构造函数存根
	}

	public BusinessException(String errorCode, Object[] args) {
		super(errorCode, args);
		// TODO 自动生成构造函数存根
	}

	public BusinessException(String errorCode, String message, Object[] args) {
		super(errorCode, message, args);
		// TODO 自动生成构造函数存根
	}

	public BusinessException(String errorCode, String message, Throwable cause, Object[] args) {
		super(errorCode, message, cause, args);
		// TODO 自动生成构造函数存根
	}

	public BusinessException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
		// TODO 自动生成构造函数存根
	}

	public BusinessException(String errorCode, String message) {
		super(errorCode, message);
		// TODO 自动生成构造函数存根
	}

	public BusinessException(String errorCode, Throwable cause, Object[] args) {
		super(errorCode, cause, args);
		// TODO 自动生成构造函数存根
	}

	public BusinessException(String errorCode, Throwable cause) {
		super(errorCode, cause);
		// TODO 自动生成构造函数存根
	}

	public BusinessException(String errorCode) {
		super(errorCode);
		// TODO 自动生成构造函数存根
	}

	public BusinessException(Throwable cause) {
		super(cause);
		// TODO 自动生成构造函数存根
	}
	



}
