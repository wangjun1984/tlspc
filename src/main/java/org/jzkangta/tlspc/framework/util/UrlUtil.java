package org.jzkangta.tlspc.framework.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class UrlUtil {

	public static String encode(String str) {
		if (str == null || str.equals(""))
			return "";
		try {
			str = java.net.URLEncoder.encode(str, "GBK");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String decode(String str) {
		if (str == null || str.equals(""))
			return "";
		try {
			str = java.net.URLDecoder.decode(str, "GBK");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String encode(String str, String code) {
		if (str == null || str.equals(""))
			return "";
		try {
			str = java.net.URLEncoder.encode(str, code);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String decode(String str, String code) {
		if (str == null || str.equals(""))
			return "";
		try {
			str = java.net.URLDecoder.decode(str, code);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static String escape(String src) {
		if (src == null || src.equals("")){
			return "";
		}
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	public static String unescape(String src) {
		if (src == null || src.equals("")){
			return "";
		}
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src
							.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src
							.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	/**
	 * 获取域
	 */
	public static  String getDomain(String url){
		
		String content = "";
		if(!StringUtil.isEmpty(url))
		{
		
			Matcher matcher = StringUtil.getMatcherGroup(url, ".*?(\\w*?\\.(com|cn|net|org|biz|info|cc|tv))");
			while(matcher.find()){
				content = matcher.group(1);
			}
		}
		return content;
	}
	
	
	/**
	 * 获取域名
	 */
	public static  String getServerName(String url){
		//Pattern p = Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);//获取url地址中的主域名
		////如果要得到 chinajavaworld.com/entry/4545/0/正则表达式最后加上 .* 即可.
		//Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);//取完整域名.
		//return StringUtil.getMatcherGroup(url, "[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)",0);
		String content = "";
		if(!StringUtil.isEmpty(url))
		{
			//logger.info("getServerNameByPattern >>" +url );
			Matcher matcher = StringUtil.getMatcherGroup(url, "(?<=http://|https://|ftp://)([\\s\\S]*?\\.(com.cn|com|cn|net|org|biz|info|cc|tv))");
			while(matcher.find()){
				content = matcher.group();
			}
		}
		return content;
	}
	
	/**
	 * 获取目录
	 * @param url
	 * @return
	 * @version 2012-8-20 - 下午06:11:26
	 * @author wushubin
	 */
	public static  String getDirectory(String url){
		String content = "";
		if(!StringUtil.isEmpty(url))
		{
			Matcher matcher = StringUtil.getMatcherGroup(url, "(com|cn|net|org|biz|info|cc|tv)(\\/.*\\/)([^\\/]*)$");
			while(matcher.find()){
				content = matcher.group(2);
			}
		}
		return content;
	}
	
	
	/**
	 * 将url参数拆分为map
	 * @param queryString
	 * @return
	 */
	public static Map<String,String> queryStringToMap(String queryString) {
		if(null == queryString || "".equals(queryString)) {
			return null;
		}
		
		Map<String,String> m = new HashMap<String,String>();
		String[] strArray = queryString.split("&");
		for(int index = 0; index < strArray.length; index++) {
			String pair = strArray[index];
			if(null == pair || "".equals(pair)) {
				continue;
			}
			
			int indexOf = pair.indexOf("=");
			if(-1 != indexOf) {
				String k = pair.substring(0, indexOf);
				String v = pair.substring(indexOf+1, pair.length());
				if(null != k && !"".equals(k)) {
					m.put(k, v);
				}
			} else {
				m.put(pair, "");
			}
		}
		
		return m;
		
	}
	
	
}
