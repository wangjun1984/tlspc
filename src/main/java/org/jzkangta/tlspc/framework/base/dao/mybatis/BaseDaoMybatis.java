package org.jzkangta.tlspc.framework.base.dao.mybatis;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jzkangta.tlspc.framework.base.dao.BaseDao;
import org.jzkangta.tlspc.framework.util.PageHashMap;
import org.jzkangta.tlspc.framework.util.PageList;
import org.jzkangta.tlspc.framework.util.PageTreeMap;
import org.jzkangta.tlspc.framework.util.PageTurn;

/**
 * <p>
 * Description: BaseDao的Mybatis实现类，并提供分页查询的实现
 * 参考链接： http://www.cnblogs.com/binbang/p/4772650.html
 * 类中注释的方法请暂时保留，后续待补充实现
 * </p>
 * 
 * @version 1.0
 */
public abstract class BaseDaoMybatis<T> extends SqlSessionDaoSupport  implements BaseDao<T> {
	
	/**
	 * log4j日志
	 */
	protected Logger log = LoggerFactory.getLogger(getClass());
	/**
	 * 查询参数map中,存放cache有效期的参数名，有效期的单位是秒
	 */
	private String nameSpace = "";

	/*****************************************************************************************/
	/**
	 * 如果调用分页方法的pageSize等于或小于0时，系统采用默认的pageSize为1000
	 */
	public static final int DEFAULT_PAGESIZE = 1000;

	public static final String COUNT_CACHE_SECONDS = "countCacheSeconds";

	private static final CountCacheManager cacheManager = CountCacheManager.getInstance();
	
	private SqlSessionFactory sqlSessionFactory;

	public BaseDaoMybatis() {
		super();
	}
	
	/**
	 * sessionFactory提供放开
	 */
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	    this.sqlSessionFactory = sqlSessionFactory;
    }
	
	@SuppressWarnings("unchecked")
	public T selectById(Object id) {
		return (T)queryForObject(getNameSpace() + ".selectById", id);
	}

	@SuppressWarnings("unchecked")
	public PageList<T> selectByMap(Map<String, Object> params, boolean doCount) {
		return selectByMap(getNameSpace() + ".selectByMap", params, doCount);
	}

	@SuppressWarnings("unchecked")
	public PageList<T> selectByMap(String sqlmapId, Map<String, Object> params, boolean doCount) {
		return (PageList<T>) queryList(sqlmapId, params, doCount);
	}

	public PageList<T> selectByMap(Map<String, Object> params, int pageNo, int pageSize) {
		return selectByMap(getNameSpace() + ".selectByMap", params, pageNo, pageSize);
	}

	public PageList<T> selectByMap(String sqlmapId, Map<String, Object> params, int pageNo, int pageSize) {
		return (PageList<T>) queryforPageList(sqlmapId, params, pageNo, pageSize);
	}

	public PageList<T> selectByMap(Map<String, Object> params, int pageNo, int pageSize, boolean doCount) {
		return selectByMap(getNameSpace() + ".selectByMap", params, pageNo, pageSize, doCount);
	}

	public PageList<T> selectByMap(String sqlmapId, Map<String, Object> params, int pageNo, int pageSize, boolean doCount) {
		return (PageList<T>) queryforPageList(sqlmapId, params, pageNo, pageSize, doCount);
	}

	public int selectCountByMap(Map<String, Object> params) {
		return selectCountByMap(getNameSpace() + ".selectByMap_count", params);
	}

	public int selectCountByMap(String sqlmapId, Map<String, Object> params) {
		Integer count = (Integer) queryForObject(sqlmapId, params);
		return count.intValue();
	}

	public void insert(T object) {
		insert(getNameSpace() + ".insert", object);
	}
	
	public Integer insert(final String sqlmapId, final T object) {
		return getSqlSession().insert(sqlmapId, object);
	}

	public int updateByMap(Map<String, Object> param) {
		return updateByMap(getNameSpace() + ".updateByMap", param);
	}

	public int updateByMap(final String sqlmapId, final Map<String, Object> param) {
		return getSqlSession().update(sqlmapId, param);
	}

	public int update(T object) {
		return update(getNameSpace() + ".updateById", object);
	}

	public int update(final String sqlmapId,final T object) {
		return getSqlSession().update(sqlmapId, object);
	}

	public int deleteByMap(Map<String, Object> params) {
		return deleteByMap(getNameSpace() + ".deleteByMap", params);
	}
	
	public int deleteByMap(final String sqlmapId, final Map<String, Object> param) {
		return getSqlSession().delete(sqlmapId, param);
	}

	public int deleteById(Object id) {
		return deleteById(getNameSpace() + ".deleteById", id);
	}
	
	public int deleteById(final String sqlmapId, final Object id) {
		return getSqlSession().delete(sqlmapId, id);
	}

	/**
	 * @return 返回 sqlmap的nameSpace。
	 */
	public String getNameSpace() {
		return nameSpace;
	}

	/**
	 * @param nameSpace
	 *            要设置的sqlmap的nameSpace。
	 */
	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	/**
	 * 根据参数获取分页数据，每查询一次都会统计符合条件的总条数
	 * 
	 * @param sqlmapid
	 *            sqlMap文件中定义的sql语句的id
	 * @param params
	 *            传入的参数
	 * @param page
	 *            页数
	 * @param pagesize
	 *            每页的记录条数
	 * @return PageList< 实体类 >
	 */
	@SuppressWarnings("unchecked")
	protected PageList queryforPageList(String sqlmapid, Map<String, Object> params, int page, int pagesize) {
		return queryforPageList(sqlmapid, params, new PageTurn(page, pagesize), true);
	}

	/**
	 * 不带分布的获取数据。
	 * 
	 * @param sqlmapid
	 *            sqlMap文件中定义的sql语句的id
	 * @param params
	 *            传入的参数
	 * @param page
	 *            页数
	 * @param pagesize
	 *            每页的记录条数
	 * @param doCount
	 *            是否统计符合条件的总条数
	 * @return PageList< 实体类 >
	 */
	@SuppressWarnings("unchecked")
	protected PageList queryList(String sqlmapid, Map<String, Object> params, boolean doCount) {
		return queryforPageList(sqlmapid, params, new PageTurn(1, PageTurn.DEFAULT_PAGE_SIZE), doCount);
	}

	/**
	 * 根据参数获取分页数据
	 * 
	 * @param sqlmapid
	 *            sqlMap文件中定义的sql语句的id
	 * @param params
	 *            传入的参数
	 * @param page
	 *            页数
	 * @param pagesize
	 *            每页的记录条数
	 * @param doCount
	 *            是否统计符合条件的总条数
	 * @return PageList< 实体类 >
	 */
	@SuppressWarnings("unchecked")
	protected PageList queryforPageList(String sqlmapid, Map<String, Object> params, int page, int pagesize, boolean doCount) {
		return queryforPageList(sqlmapid, params, new PageTurn(page, pagesize), doCount);
	}

	/**
	 * 根据参数获取分页数据
	 * 
	 * @param sqlmapid
	 *            sqlMap文件中定义的sql语句的id
	 * @param params
	 *            传入的参数
	 * @param turn
	 *            页数及每页的记录条数，记录总条数等信息
	 * @param doCount
	 *            是否统计符合条件的总条数
	 * @return PageList<T>
	 */
	@SuppressWarnings("unchecked")
	protected PageList queryforPageList(String sqlmapid, Map<String, Object> params, PageTurn turn, boolean doCount) {
		PageTurn retTurn = initParamAndPageTurn(sqlmapid + "_count", params, turn, doCount);
		PageList pagelist = new PageList();
		pagelist.setPageTurn(retTurn);

		if (retTurn.getRowCount().intValue() > 0) {
			try {
				List list = queryForList(sqlmapid, params);
				pagelist.addAll(list);
			} catch (Exception e) {
				log.error("queryforPageList error:" + e.getMessage());
			}
		}

		return pagelist;
	}
	
	/**
	 * 根据参数获取分页数据，每查询一次都会统计符合条件的总条数
	 * 
	 * @param sqlmapid
	 *            sqlMap文件中定义的sql语句的id, 同时，sqlMap中必须还有个count的语句和他对应，如：sqlmapid
	 *            =SelectByMap，则必须还要定义个SelectByMap_count的语句
	 * @param params
	 *            传入的参数
	 * @param page
	 *            页数
	 * @param pagesize
	 *            每页的记录条数
	 * @param keyProperty
	 *            实体的属性名，返回的结果集中，每个实体类对应的该属性值必须是唯一的
	 * @return PageHashMap< keyPropertyValue, 实体类 >
	 *         ，其中keyPropertyValue为实体类对应的属性keyProperty的值
	 */
	@SuppressWarnings("unchecked")
	protected PageHashMap queryforPageMap(String sqlmapid, Map<String, Object> params, int page, int pagesize, String keyProperty) {
		return queryforPageMap(sqlmapid, params, new PageTurn(page, pagesize), true, keyProperty);
	}

	/**
	 * 根据参数获取分页数据
	 * 
	 * @param sqlmapid
	 *            sqlMap文件中定义的sql语句的id, 同时，sqlMap中必须还有个count的语句和他对应，如：sqlmapid
	 *            =SelectByMap，则必须还要定义个SelectByMap_count的语句
	 * @param params
	 *            传入的参数
	 * @param page
	 *            页数
	 * @param pagesize
	 *            每页的记录条数
	 * @param reCount
	 *            是否统计符合条件的总条数
	 * @param keyProperty
	 *            尸体的属性名，返回的结果集中，每个实体类对应的该属性值必须是唯一的
	 * @return PageHashMap< keyPropertyValue, 实体类 >
	 *         ，其中keyPropertyValue为实体类对应的属性keyProperty的值
	 */
	@SuppressWarnings("unchecked")
	protected PageHashMap queryforPageMap(String sqlmapid, Map<String, Object> params, int page, int pagesize, boolean reCount, String keyProperty) {
		return queryforPageMap(sqlmapid, params, new PageTurn(page, pagesize), reCount, keyProperty);
	}

	/**
	 * 根据参数获取分页数据
	 * 
	 * @param sqlmapid
	 *            sqlMap文件中定义的sql语句的id, 同时，sqlMap中必须还有个count的语句和他对应，如：sqlmapid
	 *            =SelectByMap，则必须还要定义个SelectByMap_count的语句
	 * @param params
	 *            传入的参数
	 * @param turn
	 *            页数及每页的记录条数，记录总条数等信息
	 * @param reCount
	 *            是否统计符合条件的总条数
	 * @param keyProperty
	 *            尸体的属性名，返回的结果集中，每个实体类对应的该属性值必须是唯一的
	 * @return PageHashMap< keyPropertyValue, 实体类 >
	 *         ，其中keyPropertyValue为实体类对应的属性keyProperty的值
	 */
	@SuppressWarnings("unchecked")
	protected PageHashMap queryforPageMap(String sqlmapid, Map<String, Object> params, PageTurn turn, boolean reCount, String keyProperty) {
		PageTurn retTurn = initParamAndPageTurn(sqlmapid + "_count", params, turn, reCount);
		PageHashMap pageMap = new PageHashMap();
		pageMap.setPageTurn(retTurn);

		if (retTurn.getRowCount().intValue() > 0) {
			try {
				Map map = getSqlSession().selectMap(sqlmapid, params, keyProperty);
				pageMap.putAll(map);
			} catch (Exception e) {
				log.error("queryforPageMap error:" + e.getMessage());
			}
		}

		return pageMap;
	}

	/**
	 * 根据参数获取分页数据，每查询一次都会统计符合条件的总条数
	 * 
	 * @param sqlmapid
	 *            sqlMap文件中定义的sql语句的id, 同时，sqlMap中必须还有个count的语句和他对应，如：sqlmapid
	 *            =SelectByMap，则必须还要定义个SelectByMap_count的语句
	 * @param params
	 *            传入的参数
	 * @param page
	 *            页数
	 * @param pagesize
	 *            每页的记录条数
	 * @param keyProperty
	 *            尸体的属性名，返回的结果集中，每个实体类对应的该属性值必须是唯一的
	 * @return PageTreeMap< keyPropertyValue, 实体类 >
	 *         ，其中keyPropertyValue为实体类对应的属性keyProperty的值,实体类需要实现
	 *         <code>java.lang.Comparable</code> 接口
	 */
	@SuppressWarnings("unchecked")
	protected PageTreeMap queryforPageTreeMap(String sqlmapid, Map<String, Object> params, int page, int pagesize, String keyProperty) {
		return queryforPageTreeMap(sqlmapid, params, new PageTurn(page, pagesize), true, keyProperty);
	}

	/**
	 * 根据参数获取分页数据
	 * 
	 * @param sqlmapid
	 *            sqlMap文件中定义的sql语句的id, 同时，sqlMap中必须还有个count的语句和他对应，如：sqlmapid
	 *            =SelectByMap，则必须还要定义个SelectByMap_count的语句
	 * @param params
	 *            传入的参数
	 * @param page
	 *            页数
	 * @param pagesize
	 *            每页的记录条数
	 * @param reCount
	 *            是否统计符合条件的总条数
	 * @param keyProperty
	 *            尸体的属性名，返回的结果集中，每个实体类对应的该属性值必须是唯一的
	 * @return PageTreeMap< keyPropertyValue, 实体类 >
	 *         ，其中keyPropertyValue为实体类对应的属性keyProperty的值,实体类需要实现
	 *         <code>java.lang.Comparable</code> 接口
	 */
	@SuppressWarnings("unchecked")
	protected PageTreeMap queryforPageTreeMap(String sqlmapid, Map<String, Object> params, int page, int pagesize, boolean reCount, String keyProperty) {
		return queryforPageTreeMap(sqlmapid, params, new PageTurn(page, pagesize), reCount, keyProperty);
	}

	/**
	 * 根据参数获取分页数据
	 * 
	 * @param sqlmapid
	 *            sqlMap文件中定义的sql语句的id, 同时，sqlMap中必须还有个count的语句和他对应，如：sqlmapid
	 *            =SelectByMap，则必须还要定义个SelectByMap_count的语句
	 * @param params
	 *            传入的参数
	 * @param turn
	 *            页数及每页的记录条数，记录总条数等信息
	 * @param doCount
	 *            是否统计符合条件的总条数
	 * @param keyProperty
	 *            实体的属性名，返回的结果集中，每个实体类对应的该属性值必须是唯一的
	 * @return PageTreeMap< keyPropertyValue, 实体类 >
	 *         ，其中keyPropertyValue为实体类对应的属性keyProperty的值,实体类需要实现
	 *         <code>java.lang.Comparable</code> 接口
	 */
	@SuppressWarnings("unchecked")
	protected PageTreeMap queryforPageTreeMap(String sqlmapid, Map<String, Object> params, PageTurn turn, boolean reCount, String keyProperty) {
		PageTurn retTurn = initParamAndPageTurn(sqlmapid + "_count", params, turn, reCount);
		PageTreeMap pageMap = new PageTreeMap();
		pageMap.setPageTurn(retTurn);

		if (retTurn.getRowCount().intValue() > 0) {
			try {
				Map map = getSqlSession().selectMap(sqlmapid, params, keyProperty);
				pageMap.putAll(map);
			} catch (Exception e) {
				log.error("queryforPageTreeMap error:" + e.getMessage());
			}
		}

		return pageMap;
	}

	private PageTurn initParamAndPageTurn(String sqlmapid, Map<String, Object> params, PageTurn turn, boolean doCount) {
		PageTurn retTurn = new PageTurn();
		int total = -1;
		if (doCount || turn.getPageSize().intValue() == 0)
			// if (doCount )
			total = queryCount(sqlmapid, params);
		if (total < 0)
			total = turn.getRowCount().intValue();

		if (total > 0) {
			int tempPagesize = turn.getPageSize().intValue();
			if (tempPagesize <= 0)
				tempPagesize = total > DEFAULT_PAGESIZE ? DEFAULT_PAGESIZE : total;
			retTurn.initWithPage(turn.getPage().intValue(), tempPagesize, total);
			params.put("pageturn", retTurn);
			params.put("pageturnStartRow", retTurn.getStart());
			params.put("pageturnEndRow", retTurn.getEnd());

			if (log.isDebugEnabled())
				log.debug("PageTurn:" + retTurn);
		}
		return retTurn;
	}

	/**
	 * 根据参数和sqlId从CountCacheManager中检索被cache的数据，如果没有或cache过期，就执行该sql来刷新cache，
	 * 并返回结果
	 * 
	 * @param sqlmapid
	 *            sql语句id
	 * @param param
	 *            查询参数
	 * @return count结果
	 */
	protected int queryCount(String sqlmapid, Map<String, Object> param) {
		int total = -1;
		
		Integer temp = (Integer) param.get(COUNT_CACHE_SECONDS);
		if (temp != null && temp.intValue() > 0) {
			total = cacheManager.getCount(sqlmapid, param, this);

			if (log.isDebugEnabled())
				log.debug("cache:" + sqlmapid + ",param:" + param + ",count:" + total);
	
			if (total < 0) {
				total = doCount(sqlmapid, param);
				cacheManager.addCache(sqlmapid, param, total, this);
			}
		} else {
			total = doCount(sqlmapid, param);
		}

		return total;
	}

	/**
	 * 根据参数和sqlId,执行该sql来并返回结果
	 * 
	 * @param sqlmapid
	 *            sql语句id
	 * @param param
	 *            查询参数
	 * @return count结果
	 */
	protected int doCount(final String sqlmapid, final Map<String, Object> param) {
		int total = -1;
		try {
			total = ((Integer) getSqlSession().selectOne(sqlmapid, param)).intValue();
			if (log.isDebugEnabled())
				log.debug("doCount return:" + total);

		} catch (Exception e) {
			log.error("doCount error:" + e.getMessage());
		}
		return total;
		
//		return getSqlSession().selectOne(sqlmapid, param);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jzkangta.tlspc.framework.base.db.BaseDao#getCurrentTime()
	 */
	public Date getCurrentTime() {
		
		return (Date) queryForObject("BASE_DAO.getCurrentTime", null);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jzkangta.tlspc.framework.base.db.BaseDao#getSequenceNextValue(java.lang.
	 * String )
	 */
	public int getSequenceNextValue(String sequenceName) {
		return ((Integer) queryForObject("BASE_DAO.getSequenceNextValue", sequenceName)).intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jzkangta.tlspc.framework.base.db.BaseDao#getSequenceCurrentValue(java.lang
	 * .String)
	 */
	public int getSequenceCurrentValue(String sequenceName) {
		return ((Integer) queryForObject("BASE_DAO.getSequenceCurrentValue", sequenceName)).intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jzkangta.tlspc.framework.base.db.BaseDao#getRowCountOfTable(java.lang.String
	 * )
	 */
	public int getRowCountOfTable(String tableName) {
		return ((Integer) queryForObject("BASE_DAO.getRowCountOfTable", tableName)).intValue();
	}

	/**
	 * 从数据表SYS_COUNT_CACHE中返回被cache的count信息
	 * 
	 * @param countKey
	 *            CountCacheItem.getCountKey()返回的字符串
	 * @return 被cache的Count实例
	 */
	public CountCacheItem getCountFromCacheTable(String countKey) {
		return (CountCacheItem) queryForObject("BASE_DAO.getCountFromCacheTable", countKey);
	}

	/**
	 * 插入count信息到countCache数据表SYS_COUNT_CACHE中
	 * 
	 * @param item
	 */
	public void insertCountCache2Table(CountCacheItem item) {
		getSqlSession().insert("BASE_DAO.insertCountCache2Table", item);
	}

	/**
	 * 
	 * 使用独立事务批量插入，注意事务控制与关闭
	 * @param list
	 */
	public void batchInsert(List<T> list) {
		
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);  
	    try {  
	        for (int i = 0; i< list.size(); i++) {
	        	T obj = list.get(i);
	        	sqlSession.insert(getNameSpace() + ".insert", obj);
	        	if(i % 100 == 0) {
	        		sqlSession.commit();
	        		sqlSession.close();
	        		
	        		sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);  
	        	}
	        }
	        sqlSession.commit();  
	  } catch (Exception e) {
            e.printStackTrace();
            logger.error("fail to execute batchInsert",e);
           throw new RuntimeException("数据库操作异常"+e.getMessage());
       } finally {  
	        sqlSession.close();  
	    }  
	}
	
	/**
	 * 注意调用sqlmap中的.updateById语句
	 * 
	 * @param list
	 */
	public void batchUpdateById(List<T> list) {
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);  
	    try {  
	        for (int i = 0; i< list.size(); i++) {
	        	T obj = list.get(i);
	        	sqlSession.insert(getNameSpace() + ".updateById", obj);
	        	if(i % 1000 == 0) {  //一千条提交一次
	        		sqlSession.commit();
	        		sqlSession.close();
	        		
	        		sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);  
	        	}
	        }
	        sqlSession.commit();  
	    } catch (Exception e) {
            e.printStackTrace();
            logger.error("fail to execute batchInsert",e);
           throw new RuntimeException("数据库操作异常"+e.getMessage());
        } finally {  
	        sqlSession.close();  
	    } 
	}
	
	/**
	 * 注意调用sqlmap中的.updateById语句
	 * 
	 * @param list
	 */
	public void batchUpdateByMap(List<Map<String, Object>> paramMapList)throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);  
	    try {  
	        for (int i = 0; i< paramMapList.size(); i++) {
	        	Map<String, Object> obj = paramMapList.get(i);
	        	sqlSession.insert(getNameSpace() + ".updateByMap", obj);
	        	if(i % 1000 == 0) {  //一千条提交一次
	        		sqlSession.commit();
	        		sqlSession.close();
	        		
	        		sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);  
	        	}
	        }
	        sqlSession.commit();  
	    } catch (Exception e) {
            e.printStackTrace();
            logger.error("fail to execute batchInsert",e);
           throw new RuntimeException("数据库操作异常"+e.getMessage());
        } finally {  
	        sqlSession.close();  
	    } 
	}

	/**
	 * 更新count信息到数据表SYS_COUNT_CACHE中
	 * 
	 * @param item
	 * @return 更新的记录条数
	 */
	public int updateCountCache2Table(CountCacheItem item) {
		return getSqlSession().update("BASE_DAO.updateCountCache2Table", item);
	}

	/**
	 * 销毁资源
	 */
	public void destroy() {
		cacheManager.destroy();
	}
	
    protected Object queryForObject(final String sqlMapId, final Object param) {
    	return getSqlSession().selectOne(sqlMapId, param);
	}
    
    protected List queryForList(final String sqlMapId, final Object param) {
		 
    	return getSqlSession().selectList(sqlMapId, param);
    }
}
