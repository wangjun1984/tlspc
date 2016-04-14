package org.jzkangta.tlspc.framework.base.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jzkangta.tlspc.framework.util.PageList;

/**
 * BaseDao 最基本的数据库查询操作，可以作为业务DAO的接口的基类接口
 */
public interface BaseDao<T> {

	/**
	 * 不带分页的获取列表数据。
	 * 
	 * @param params
	 * @param doCount
	 * @return
	 */
	public PageList<T> selectByMap(Map<String, Object> params, boolean doCount);

	public PageList<T> selectByMap(Map<String, Object> params, int pageNo, int pageSize, boolean doCount);

	public PageList<T> selectByMap(Map<String, Object> params, int pageNo, int pageSize);

	public T selectById(Object id);

	public int selectCountByMap(Map<String, Object> param);

	public void insert(T object);

	public int updateByMap(Map<String, Object> param);

	public int update(T object);

	public int deleteByMap(Map<String, Object> params);

	public int deleteById(Object id);

	/**
	 * @return 返回 sqlmap的nameSpace。
	 */
	public String getNameSpace();

	/**
	 * @param nameSpace
	 *            要设置的sqlmap的nameSpace。
	 */
	public void setNameSpace(String nameSpace);

	/**
	 * 获取数据库服务器的系统时间
	 * 
	 * @return 数据库服务器的系统时间
	 */
	public abstract Date getCurrentTime();

	/**
	 * 获取数据库Sequence的nextValue
	 * 
	 * @param sequenceName
	 *            sequence名称
	 * @return nextValue 整数
	 */
	public abstract int getSequenceNextValue(String sequenceName);

	/**
	 * 获取数据库Sequence的当前值
	 * 
	 * @param sequenceName
	 *            sequence名称
	 * @return nextValue 当前值，整数
	 */
	public abstract int getSequenceCurrentValue(String sequenceName);

	/**
	 * 获取数据库中某个表的记录总数
	 * 
	 * @param tableName
	 *            表名
	 * @return 某个表的记录总数
	 */
	public abstract int getRowCountOfTable(String tableName);

	/**
	 * 销毁资源
	 */
	public abstract void destroy();

	/**
	 * 注意调用sqlmap中的.insert语句
	 * @param list
	 */
	public void batchInsert(List<T> list);
	
	/**
	 * 注意调用sqlmap中的.updateById语句
	 * @param list
	 */
	public void batchUpdateById(List<T> list);

}