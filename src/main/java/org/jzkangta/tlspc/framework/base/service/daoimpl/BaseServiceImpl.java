package org.jzkangta.tlspc.framework.base.service.daoimpl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.jzkangta.tlspc.framework.base.dao.BaseDao;
import org.jzkangta.tlspc.framework.base.service.BaseService;
import org.jzkangta.tlspc.framework.base.service.ServiceEventListener;
import org.jzkangta.tlspc.framework.base.service.event.ServiceEventHandler;
import org.jzkangta.tlspc.framework.util.PageList;

/**
 * BaseServiceImpl 提供BaseService的基本实现
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {
	/**
	 * 日志
	 */
	protected Logger log = Logger.getLogger(getClass());

	/**
	 * 事件处理器
	 */
	protected ServiceEventHandler eventHandler = null;

	/**
	 * 如果需要直接使用BaseService< T
	 * >定义的业务方法，必须在子类中覆盖本方法。为了兼容使用了旧版本的应用程序，这里并没有把本方法定义为abstract。
	 * 
	 * @return 返回在子类中实际的注入的BaseDao< T >
	 * @since v1.1, 2010-08-07, jimmy zhou
	 */
	protected BaseDao<T> getBaseDao() {
		throw new UnsupportedOperationException("If need use directly BaseService's method, the sub-class must override this method.");
	}

	/**
	 * 获取业务类事件处理器
	 * 
	 * @return 返回 eventHandler。
	 */
	public ServiceEventHandler getEventHandler() {
		return eventHandler;
	}

	/**
	 * 注入业务类事件处理器
	 * 
	 * @param eventHandler
	 *            要设置的 eventHandler。
	 */
	public void setEventHandler(ServiceEventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	/**
	 * 注册指定的事件监听器到事件处理器容器中。
	 * 
	 * @param listener
	 *            待注册的事件监听器
	 */
	public synchronized void addEventListener(ServiceEventListener listener) {
		if (eventHandler == null)
			eventHandler = new ServiceEventHandler();
		eventHandler.addListener(listener);
	}

	public void insert(T object) {
		getBaseDao().insert(object);
	}

	public int update(T object) {
		return getBaseDao().update(object);
	}

	public int update(Map<String, Object> param) {
		return getBaseDao().updateByMap(param);
	}

	public int delete(Map<String, Object> param) {
		return getBaseDao().deleteByMap(param);
	}

	public int delete(Object id) {
		return getBaseDao().deleteById(id);
	}

	public T getById(Object id) {
		return getBaseDao().selectById(id);
	}

	public int getCount(Map<String, Object> param) {
		return getBaseDao().selectCountByMap(param);
	}

	public PageList<T> getList(Map<String, Object> params, int pageNo, int pageSize, boolean doCount) {
		return getBaseDao().selectByMap(params, pageNo, pageSize, doCount);
	}

	public PageList<T> getList(Map<String, Object> params, int pageNo, int pageSize) {
		return getBaseDao().selectByMap(params, pageNo, pageSize);
	}

	public PageList<T> getList(Map<String, Object> params) {
		return getBaseDao().selectByMap(params, false);
	}

	@Override
	public void batchInsert(List<T> list) {
		getBaseDao().batchInsert(list);
	}
}
