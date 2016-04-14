/**
 * 
 */
package org.jzkangta.tlspc.system.web.exception;

/**
 * 无法找到业务逻辑类异常
 *
 * @author lijian
 * @version 2014年3月15日 - 下午1:26:51
 */
public class ServiceOperationException extends AppBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4618415364025173907L;

	/** */
	public ServiceOperationException() {
		super();
	}

	/**
	 * 
	 * @param message
	 */
	public ServiceOperationException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
    public ServiceOperationException(String message, Throwable cause) {
        super(message, cause);
    }

}