package org.jzkangta.tlspc.wechat.util;

import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jzkangta.tlspc.framework.util.PropertyUtil;

public class BaseWechatUtil {
	
	private static Logger log = LoggerFactory.getLogger(BaseWechatUtil.class);
	protected static String TOKEN ;
	protected static String WHITE_USER_LIST;
	protected static String DEFAULT_MESSAGE ;
	public static String APPID ;
	public static String APPSECRET ;
	protected static String AGENTID ; 
	protected static Integer TIME_OUT ;
	protected static String propertiesFileName = "wechat";
	public static String MERCHANT_NUM ;
	public static String PAYKEY ;
	public static String CERTPATH ;
	public static String BILL_TYPE;
	
	static{
		try{
			TOKEN = PropertyUtil.getProperty(propertiesFileName, "token", "");
			WHITE_USER_LIST = PropertyUtil.getProperty(propertiesFileName, "whitelist", "");
			DEFAULT_MESSAGE = PropertyUtil.getProperty(propertiesFileName, "respMessage", "");
			BILL_TYPE = PropertyUtil.getProperty(propertiesFileName, "billType", "MCHT");//用于支付系统订单查询
			
			MERCHANT_NUM = PropertyUtil.getProperty(propertiesFileName, "merchantNum", "");
			PAYKEY = PropertyUtil.getProperty(propertiesFileName, "payKey", "");
			if (PropertyUtil.containsKey(propertiesFileName, "certPath")) {
				CERTPATH = PropertyUtil.getProperty(propertiesFileName, "certPath", "");
			}
			
			APPID = PropertyUtil.getProperty(propertiesFileName, "appId", "");
			APPSECRET = PropertyUtil.getProperty(propertiesFileName, "appSecret", "");
			if (PropertyUtil.containsKey(propertiesFileName, "agentId")) {
				AGENTID = PropertyUtil.getProperty(propertiesFileName, "agentId", "");
			}
			
			TIME_OUT = PropertyUtil.getIntProperty(propertiesFileName, "timeout" , 8000);
		}catch(Exception e){
			log.error("func[BaseWechatUtil] exception[{} - {}] desc[fail]",//
					new Object[] { e.getMessage(), Arrays.deepToString(e.getStackTrace()) });
		}
	}
	
	protected static String getRequestParams(HttpServletRequest request){
		StringBuilder sb = new StringBuilder();
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
		    String key = e.nextElement();
		    String val = request.getParameter(key);
		    sb.append(key).append("=").append(val).append("&");
		}
		return sb.toString();
	}
}
