package org.jzkangta.tlspc.system.web.exception;

/**
 * @author tangjun
 */
public class AppBaseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3728403084326926290L;

	/** */
	public AppBaseException() {
		super();
	}

	/**
	 * 
	 * @param message
	 */
	public AppBaseException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
    public AppBaseException(String message, Throwable cause) {
        super(message, cause);
    }

}