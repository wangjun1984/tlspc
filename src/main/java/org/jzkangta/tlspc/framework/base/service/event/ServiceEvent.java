package org.jzkangta.tlspc.framework.base.service.event;

import java.util.EventObject;

/**
 * ServiceEvent 定义业务事件，主要记录了事件对应的Service对象和具体的参数，具体的事件类型分为<code>InsertEvent</code>,
 * <code>UpdateEvent</code>, <code>DeleteEvent</code>这3种。
 */
public class ServiceEvent extends EventObject {

	private static final long serialVersionUID = 1832282528630934999L;

	private String methodName ;
	private Object param ;

	private Object returnValue = null;

	/**
	 * 事件构造器
	 * @param serivce 事件对应的Service对象
	 * @param param 事件对应的参数
	 */
	protected ServiceEvent(Object service, Object param ) {
		this(service, null, param, null);
	}
	
	protected ServiceEvent(Object service, String methodName, Object param ) {
		this(service, methodName, param, null);
	}
	
	protected ServiceEvent(Object service, String methodName, Object param, Object returnValue) {
		super(service);
		this.methodName = methodName;
		this.param = param;
		this.returnValue = returnValue;
	}
	
	/**
	 * @return 事件对应的Service对象,等同于getSource方法
	 */
	public Object getService(){
		return getSource();
	}

	/**
	 * @return 事件对应的参数
	 */
	public Object getParam(){
		return param;
	}

	/**
     * @param returnValue the returnValue to set
     */
    public void setReturnValue( Object returnValue ) {
    	this.returnValue = returnValue;
    }

	/**
     * @return the returnValue
     */
    public Object getReturnValue() {
    	return returnValue;
    }

	/**
     * @return the methodName
     */
    public String getMethodName() {
    	return methodName;
    }


}
