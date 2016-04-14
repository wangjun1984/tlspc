package org.jzkangta.tlspc.framework.base.dao.mybatis;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import org.jzkangta.tlspc.framework.util.SimpleThreadPool;
import org.jzkangta.tlspc.framework.util.StringUtil;


/**
 * CountCacheManager cache的管理容器。当查询参数（Map）中包括了countCacheSeconds参数，系统自动对相应的sqlId和参数Params的count结果启动cache策略。
 * cache的有效期由参数countCacheSeconds来指定。cache包括内存cache和数据表cache，如果没有创建SYS_COUNT_CACHE数据表, 就只有内存cache，如果创建了该
 * 数据表，那么内存cache和数据表cache同时起效，并且内存cache优先。
 */
public class CountCacheManager implements Runnable{
	
	private static final CountCacheManager instance = new CountCacheManager();
	
	private Logger log = Logger.getLogger( getClass() );
	
	private final Map<String, CountCacheItem> countCacheMap = Collections.synchronizedMap(new HashMap<String, CountCacheItem>()); 
	
	private volatile boolean existCountCacheTable = false;
	
	private SimpleThreadPool threadPool = new SimpleThreadPool(2,2,10);
	
	private volatile boolean keepAliveRun = true;
	
	private long keepAliveInterval = 1000*60;	// 1 minutes
	
	private CountCacheManager(){
		threadPool.execute( this );
	}
	
	public static CountCacheManager getInstance(){
		return instance;
	}
	
	/* 
	 * 自动cache失效处理，自动刷新cache的处理的线程操作
	 */
	public void run() {
		while( keepAliveRun ) {
			try {
				Thread.sleep( keepAliveInterval );
			} catch (InterruptedException e) {
				if( !keepAliveRun )		break;
			}
			synchronized(countCacheMap){
				for( Iterator<CountCacheItem> iter = countCacheMap.values().iterator(); iter.hasNext();){
					CountCacheItem item = iter.next();
					if( item.isExpire() ){
						iter.remove();
						if( log.isDebugEnabled() )
							log.debug( "expired:" + item);
						continue;
					}
					if( item.isNeedRefresh() ){
						if( log.isDebugEnabled() )
							log.debug( "refresh:" + item);
						threadPool.execute( new CountTask(item) );
					}
				}
			}
		}
		
	}		
	
	/**
	 * 构造cache项，并加入cache容器中，同时尝试cache到数据库的SYS_COUNT_CACHE数据表中
	 * @param sqlId
	 * @param param
	 * @param count
	 * @param dao
	 */
	@SuppressWarnings("unchecked")
	void addCache( String sqlmapid, Map<String,Object> param, int count, BaseDaoMybatis<?> dao ){
		String key = getKey( sqlmapid, param );
		CountCacheItem item = countCacheMap.get( key );
		if( item == null  ){
			item = new CountCacheItem( sqlmapid, param, key, count, dao );
			countCacheMap.put( key, item );
		}
		save2Db(key, item);
	}

	private void save2Db(String key, CountCacheItem item) {
		if( log.isDebugEnabled() )
			log.debug("synctoDB:" + JSONObject.toJSONString(item));
		try {
			if( existCountCacheTable ) {
				CountCacheItem dbItem = item.dao.getCountFromCacheTable( key );
				if( dbItem != null )
					item.dao.updateCountCache2Table( item );
				else
					item.dao.insertCountCache2Table( item );
			}
		} catch (Exception e) {
			setExistCountCacheTable( false );
			e.printStackTrace();
		}
	}
	
	/**
	 * 从内存cache中查询，如果没有，会从数据库的SYS_COUNT_CACHE数据表去查询，如果仍然没有，返回-1
	 * @param sqlId count查询语句
	 * @param param 查询参数
	 * @return 查询结果，如果内存cache和数据表cache中都没有，返回-1
	 */
	@SuppressWarnings("unchecked")
	int getCount( String sqlId, Map<String,Object> param, BaseDaoMybatis<?> dao ){
		String key = getKey( sqlId, param );
		CountCacheItem item = countCacheMap.get( key );
		if( item != null) {
			item.useStamp = System.currentTimeMillis();
			return item.countValue;
		}
		
		try {
			if( existCountCacheTable ) {
				item = dao.getCountFromCacheTable( key );
				if( item != null && !item.isExpire() ) {
					item.param = new HashMap<String,Object>(param.size());
					item.param.putAll(param);
					item.sqlId = sqlId;
					item.countKey = key;
					item.dao = dao;
					countCacheMap.put( key, item );
					return item.countValue;
				}
			}
		} catch( Exception e) {
			setExistCountCacheTable( false );
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * 计算用于cacheManager的key值
	 * @param sqlId sqlmap中的sqlid
	 * @param param 查询参数
	 * @return cache的key值
	 */
	static String getKey(String sqlId, Map<String,Object> param){
		return StringUtil.md5(sqlId+":"+param);
	}
	
	/**
	 * 返回cacheManager当前cache的数量
	 * @return 当前cache的数量
	 */
	public int getCacheSize(){
		return countCacheMap.size();
	}
	
	/** 
	 * 销毁对象，停止内部线程
	 */
	public void destroy()  {
		if( threadPool == null )
			return;
		keepAliveRun = false;
		threadPool.waitCompleted();
		threadPool.shutdown();
		countCacheMap.clear();
		threadPool = null;
	}
	
	synchronized void setExistCountCacheTable(boolean value) {
		existCountCacheTable = value;
	}
	
	/**
	 * CountTask 执行统计任务，如果已创建SYS_COUNT_CACHE数据表，同时更新到数据中
	 */
	private class CountTask implements Runnable {
		CountCacheItem item = null;
		
		CountTask(CountCacheItem item){
			this.item = item;
		}
		
		public void run(){
			item.updateCount();
			if( existCountCacheTable ) {
				item.dao.updateCountCache2Table( item );
			}
		}
	}
	
}
