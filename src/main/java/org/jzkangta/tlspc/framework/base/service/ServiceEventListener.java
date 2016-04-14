package org.jzkangta.tlspc.framework.base.service;

import org.jzkangta.tlspc.framework.base.service.event.ServiceEvent;

/**
 * ServiceEventListener 业务事件监听器
 */
public interface ServiceEventListener {
	
	/**
	 * 当业务事件发生后触发调用
	 * @param event 业务事件
	 */
	void onAfterEvent( ServiceEvent event );
	
	/**
	 * 当业务事件发生前触发调用
	 * @param event 业务事件
	 */
	void onBeforeEvent( ServiceEvent event );

}
