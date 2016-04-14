package org.jzkangta.tlspc.framework.base.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

/**
 * CountCacheItem cache在的CountCacheManager容器中的实体类，不同的sqlId和参数params对应的cacheKey是不同的。
 * 
 */
public class CountCacheItem {

	int countValue = 0;
	String countKey = null;
	String sqlId = null;
	Map<String,Object> param = null;

    BaseDaoMybatis<?> dao = null;
	
	volatile int expireSecond = 60*10;	// 10 minutes
	volatile long useStamp = System.currentTimeMillis();
	volatile long updateTime = System.currentTimeMillis();
	
	/**
	 * 构造器 
	 */
	public CountCacheItem() {
		super();
	}

	/**
	 * 构造器
	 * @param sqlId count查询对应的sql语句的id
	 * @param param 查询参数
	 * @param count 通过查询获得的count值，cache的目的就是要cache这个数值
	 * @param dao count查询时用到的dao实例
	 */
	CountCacheItem( String sqlId, Map<String,Object> param, String countKey, int count, BaseDaoMybatis<?> dao ){
		this.sqlId = sqlId;
		this.countKey = countKey;
		this.countValue = count;
		this.dao = dao;
		if( param != null && param.size() > 0 ){
			this.param = new HashMap<String,Object>(param.size());
			this.param.putAll(param);
			Integer temp = (Integer)param.get( BaseDaoMybatis.COUNT_CACHE_SECONDS );
			if( temp != null && temp.intValue()>0 )
				expireSecond = temp.intValue()*1000;
		}
	}
	
	/**
	 * 从数据库重新count，同事刷新更新时戳 
	 */
	synchronized void updateCount() {
		countValue = dao.doCount( sqlId, param );
		updateTime = System.currentTimeMillis();
	}
	
	public String toString(){
		return sqlId+",param:"+ param +",countValue:"+countValue;
	}
	
	/**
	 * @return 是否过期
	 */
	boolean isExpire(){
		return (System.currentTimeMillis() - useStamp) > expireSecond;
	}
	
	/**
	 * @return 是否需要刷新cache值
	 */
	boolean isNeedRefresh(){
		return (System.currentTimeMillis() - updateTime) > expireSecond;
	}
	
	/**
	 * @return 计算用于cacheManager的key
	 */
	public String getCountKey(){
		return countKey;
	}
	
	/**
	 * @return 返回 countValue。
	 */
	public Integer getCountValue() {
		return countValue;
	}
	
	/**
	 * @return 返回 expireSecond。
	 */
	public int getExpireSecond() {
		return expireSecond;
	}
	
	/**
	 * @param expireSecond 要设置的 expireSecond。
	 */
	public void setExpireSecond(int expireSecond) {
		this.expireSecond = expireSecond;
	}
	
	/**
	 * @return 返回 updateTime。
	 */
	public long getUpdateTime() {
		return updateTime;
	}
	
	/**
	 * @param updateTime 要设置的 updateTime。
	 */
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	/**
     * @param countValue the countValue to set
     */
    public void setCountValue( int countValue ) {
    	this.countValue = countValue;
    }

	/**
     * @param countKey the countKey to set
     */
    public void setCountKey( String countKey ) {
    	this.countKey = countKey;
    }

}
