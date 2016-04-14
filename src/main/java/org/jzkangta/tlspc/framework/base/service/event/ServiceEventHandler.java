package org.jzkangta.tlspc.framework.base.service.event;

import java.util.ArrayList;
import java.util.List;

import org.jzkangta.tlspc.framework.base.service.ServiceEventListener;

/**
 * ServiceEventHandler 为具体的业务处理类（<tt>BaseServiceImpl</tt>的子类）提供{@link ServiceEventListener}的容器。
 * 为业务处理类绑定0~n个<code>ServiceEventListener<code>的实现提供便利。
 */
public class ServiceEventHandler implements ServiceEventListener{

	private List<ServiceEventListener> listeners = new ArrayList<ServiceEventListener>();
    
    /**
     * 向容器中注册指定的<code>ServiceEventListener<code>监听器
     * @param listener 待注册的<code>ServiceEventListener<code>监听器
     */
    public void addListener( ServiceEventListener listener ){
    	listeners.add( listener );
    }

    /**
     * 注入<code>ServiceEventListener<code>容器
     * @param listeners 容器，方便spring配置
     */
    public void setListeners( List<ServiceEventListener> listeners ){
    	this.listeners.addAll( listeners );
    }

    /**
     * 删除容器中指定的<code>ServiceEventListener<code>监听器
     * @param listener 待删除的<code>ServiceEventListener<code>监听器
     */
    public void removeListener( ServiceEventListener listener ){
    	listeners.remove( listener );
    }

    public ServiceEventHandler() {
		super();
	}

	public void onAfterEvent( ServiceEvent event ) {
		for( ServiceEventListener listener : listeners){
			listener.onAfterEvent( event );
		}
	}
	public void onBeforeEvent(ServiceEvent event ) {
		for( ServiceEventListener listener : listeners){
			listener.onBeforeEvent( event );
		}
	}



}
