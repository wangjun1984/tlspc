package org.jzkangta.tlspc.system.web.exception;

/**
 * @author tangjun
 */
public class RequestIllegalException extends AppBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 508756173313949529L;

	/** */
	public RequestIllegalException() {
		super();
	}
	
	/**
	 * 
	 * @param message
	 */
	public RequestIllegalException(String message) {
		super(message);
	}

}
