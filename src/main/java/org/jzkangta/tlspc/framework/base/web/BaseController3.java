package org.jzkangta.tlspc.framework.base.web;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorSupport;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import org.jzkangta.tlspc.framework.util.StringUtil;
import org.jzkangta.tlspc.framework.web.HttpRequestInfo;

/**
 * 控制器基类 简化版
 * @author wushubin 
 * @version 2012-3-1 - 下午03:40:27
 *
 */
public class BaseController3 {
	
	private static Logger log = Logger.getLogger(BaseController3.class);
	
	public static final int DEFAULT_PAGE_NUM = 1;
	public static final int DEFAULT_PAGE_SIZE = 20;
	
	public static final String PAGE_NO = "pageNum";
	public static final String PAGE_SIZE = "numPerPage";
	public static final String QUERY_PARAM = "queryParam";
	
	public static final String OPERATE_FAILED_MSG = "操作失败!";

	public static final String OPERATE_SUCCESS_MSG = "操作成功!";
	
	/**
	 * 前几列表
	 */
	protected static final String TOP_LIST = "/topList";
	
	/**
	 * 列表
	 */
	protected static final String LIST = "/list";
	
	/**
	 * 部分、某几个
	 */
	protected static final String PARTIAL_LIST = "/partialList";
	
	/**
	 * 修改
	 */
	protected static final String UPDATE = "/update";
	
	/**
	 * 删除
	 */
	protected static final String DELETE = "/delete";
	
	/**
	 * 审核（适用后台管理）
	 */
	protected static final String AUDIT = "/audit";
	
	/**
	 * 编辑（修改/新增前的数据展现）
	 */
	protected static final String EDIT = "/edit";
	
	/**
	 * 显示某条记录详情<br/>
	 * 建议使用 {@link BaseController3 #DETAIL}
	 */
	@Deprecated
	protected static final String SHOW = "/show";
	
	/**
	 * 显示某条记录详情
	 */
	protected static final String DETAIL = "/detail"; 
	
	/**
	 * 新增
	 */
	protected static final String ADD = "/add";
	
	/** 保存操作，针对未知记录是否存在的情况 */
	protected static final String MERGE = "/merge";
	
	/** 出错提示页 */
	protected static final String ERROR_VIEW = "error";
	

	protected static void setRequestModelMap(HttpServletRequest request, ModelMap model ){
    	setRequestModelMap( request, model, false, null );
    }
	
	protected static void setRequestModelMap(HttpServletRequest request, ModelMap model, boolean query ){
    	setRequestModelMap( request, model, query, null );
    }
	
    /**
     * 把request中的数据填充到ModelMap中
     * 
     * @param request
     * @param model
     * @param query
     * @param jsonEncoding
     * @version 2012-3-1 - 下午03:41:02
     * @author wushubin
     * 
     */
	@SuppressWarnings("unchecked")
    protected static void setRequestModelMap(HttpServletRequest request, ModelMap model, boolean query, String jsonEncoding ){
		Map<String, String[]> parameterMap = request.getParameterMap();
		
		StringBuilder sb = new StringBuilder();
		boolean and = false;
		String queryParam = null;
		for(Map.Entry<String, String[]> entry : parameterMap.entrySet()){
			String key = entry.getKey();
			String[] values = entry.getValue();
			log.info(key+">>"+values[0]);
			if(values != null && values.length >0 ){
				
				if(key.equals("id")) // 主键id不列入(注意如果这里一列入可能会影响ibatis的更新和插入)
					continue;
				
				if( QUERY_PARAM.equalsIgnoreCase( key ) && !StringUtil.isEmpty(values[0]) ){
					queryParam = values[0];
					continue;
				}
				
				List<String> list = new ArrayList<String>();
				for( String v : values ){
					if( StringUtil.isEmpty( v ) )
						continue;
					list.add( v );
					try {
	                    v = URLEncoder.encode( v, request.getCharacterEncoding() );
                    }
                    catch( UnsupportedEncodingException e ) {
	                    e.printStackTrace();
                    }
                    if( and )
                    	sb.append( "&" );
                    else
                    	and = true;
					sb.append( key ).append( "=" ).append( v );
					
				}
				
				if( list.size() == 1 || key.equals("id")){
					if( key.startsWith( "in" ) || key.startsWith( "notIn" ) )
						model.addAttribute( key, list );
					else{
						String v = list.get( 0 );
						if( jsonEncoding != null ){
							v = StringUtil.charsetConvert( v, request.getCharacterEncoding(), jsonEncoding );
			        		v = StringUtil.gbk2iso( v );
						}
						model.addAttribute( key, v );
					}
				}
				else if( list.size()>1 ){
					model.addAttribute( key, list );
				}
			}
		}
		if( query || queryParam == null )
			queryParam = sb.toString();
		model.addAttribute( QUERY_PARAM, queryParam );
		
		if( parameterMap.get(PAGE_NO) == null){
			model.addAttribute(PAGE_NO, DEFAULT_PAGE_NUM );
		}
		if( parameterMap.get(PAGE_SIZE) == null){
			model.addAttribute(PAGE_SIZE, DEFAULT_PAGE_SIZE );
		}
	}
	
	protected String getGetObjectIds(HttpRequestInfo reqInfo, ModelMap model) {
        boolean batchParam = false;
        StringBuilder objectIds = new StringBuilder();
        
        if( model.containsKey( "inIdList" )  ){
            objectIds.append( model.get( "inIdList" ) );
            batchParam = true;
        }
        else if( !StringUtil.isEmpty( reqInfo.getParameter( "inIds" ) )  ){
            objectIds.append( model.get( "inIds" ) );
            batchParam = true;
        }
        else if( !StringUtil.isEmpty( reqInfo.getParameter( "ids" ) )  ){
            objectIds.append( model.get( "ids" ) );
            batchParam = true;
        }
        
        if( batchParam ){
            model.remove( "eqId" );
        }
        else if( !StringUtil.isEmpty( reqInfo.getParameter( "eqId" ) )  ){
            objectIds.append( model.get( "eqId" ) );
        }
        
        return objectIds.toString();
    }

	@InitBinder  
	public void InitBinder(WebDataBinder dataBinder)  
	{  
	    dataBinder.registerCustomEditor(Date.class, new PropertyEditorSupport() {  
	        public void setAsText(String value) {  
	            try {  
	                setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value));  
	            } catch(ParseException e) {  
	            	try{
	            		setValue(new SimpleDateFormat("yyyy-MM-dd").parse(value));
	            	}
	            	catch(ParseException se){
	            		setValue(null);
	            	}
	            }  
	        }  
	  
	        public String getAsText() {  
	            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) getValue());  
	        }          
	  
	    });  
	}
	
	public String getCookieVal(HttpServletRequest request, String cookieName) {
		String cookieVal = "";
		HttpRequestInfo httpRequest = new HttpRequestInfo(request);
		Cookie cookie = httpRequest.getCookie(cookieName);
		if (cookie != null) {
			return cookie.getValue();
		}
		return cookieVal;
	}

	/**
	 * 此方法为PropertyUtilsBean类中的copyProperties方法，只是在属性赋值处进行对值的null判断
	 * 因为PropertyUtilsBean类中的copyProperties方法无法对属性为null进行过滤，
	 * 即新对象中的null值会把就对象中的值设置为null了
	 * @param dest
	 * @param orig
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public void copyProperties(Object dest, Object orig)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
		
		PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();

        if (dest == null) {
            throw new IllegalArgumentException
                    ("No destination bean specified");
        }
        if (orig == null) {
            throw new IllegalArgumentException("No origin bean specified");
        }

        if (orig instanceof DynaBean) {
            DynaProperty[] origDescriptors =
                ((DynaBean) orig).getDynaClass().getDynaProperties();
            for (int i = 0; i < origDescriptors.length; i++) {
                String name = origDescriptors[i].getName();
                if (propertyUtilsBean.isReadable(orig, name) && propertyUtilsBean.isWriteable(dest, name)) {
                    try {
                        Object value = ((DynaBean) orig).get(name);
                        if (dest instanceof DynaBean) {
                            ((DynaBean) dest).set(name, value);
                        } else {
                        	propertyUtilsBean.setSimpleProperty(dest, name, value);
                        }
                    } catch (NoSuchMethodException e) {
                        if (log.isDebugEnabled()) {
                            log.debug("Error writing to '" + name + "' on class '" + dest.getClass() + "'", e);
                        }
                    }
                }
            }
        } else if (orig instanceof Map) {
            Iterator entries = ((Map) orig).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String name = (String)entry.getKey();
                if (propertyUtilsBean.isWriteable(dest, name)) {
                    try {
                        if (dest instanceof DynaBean) {
                            ((DynaBean) dest).set(name, entry.getValue());
                        } else {
                        	propertyUtilsBean.setSimpleProperty(dest, name, entry.getValue());
                        }
                    } catch (NoSuchMethodException e) {
                        if (log.isDebugEnabled()) {
                            log.debug("Error writing to '" + name + "' on class '" + dest.getClass() + "'", e);
                        }
                    }
                }
            }
        } else /* if (orig is a standard JavaBean) */ {
            PropertyDescriptor[] origDescriptors =
            		propertyUtilsBean.getPropertyDescriptors(orig);
            for (int i = 0; i < origDescriptors.length; i++) {
                String name = origDescriptors[i].getName();
                if (propertyUtilsBean.isReadable(orig, name) && propertyUtilsBean.isWriteable(dest, name)) {
                    try {
                        Object value = propertyUtilsBean.getSimpleProperty(orig, name);
                        if(value != null){
	                        if (dest instanceof DynaBean) {
	                            ((DynaBean) dest).set(name, value);
	                        } else {
	                        	propertyUtilsBean.setSimpleProperty(dest, name, value);
	                        }
                        }
                    } catch (NoSuchMethodException e) {
                        if (log.isDebugEnabled()) {
                            log.debug("Error writing to '" + name + "' on class '" + dest.getClass() + "'", e);
                        }
                    }
                }
            }
        }

    }
}
