package org.jzkangta.tlspc.wechat.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import org.jzkangta.tlspc.framework.util.StringUtil;

/**
 * 生成微信JS-SDK调用的签名
 * @author Administrator
 *
 */
public class WXJSSDKSign {
	
	private static Logger log = LoggerFactory.getLogger(WXJSSDKSign.class);
	
	
	/**
	 * 卡券签名生成算法
	 * @param HashMap<String,String> params 请求参数集，所有参数必须已转换为字符串类型
	 * @return 签名
	 * @throws IOException
	 */
	public static String getCardSignature(HashMap<String,String> params) throws IOException{
	    // 先将参数以其参数名的字典序升序进行排序
		StringBuilder basestring = sortValue(params);
	    String str = basestring.toString();
        return StringUtil.doHash(str, "", "SHA1");
	}
	
	/**
	 * 签名生成算法
	 * @param HashMap<String,String> params 请求参数集，所有参数必须已转换为字符串类型
	 * @return 签名
	 * @throws IOException
	 */
	public static String getSignature(HashMap<String,String> params) throws IOException{
	    // 先将参数以其参数名的字典序升序进行排序
		StringBuilder basestring = sort(params);
	    String str = basestring.toString();
	    str = str.substring(0, str.length()-1);
        return StringUtil.doHash(str, "", "SHA1");
	}
	
	/**
	 * 签名生成算法
	 * @param HashMap<String,String> params 请求参数集，所有参数必须已转换为字符串类型
	 * @param String secret 签名密钥
	 * @return 签名
	 * @throws IOException
	 */
	public static String getSignature(HashMap<String,String> params, String secret) throws IOException
	{
	    // 先将参数以其参数名的字典序升序进行排序
		StringBuilder basestring = sort(params);
	    if(!StringUtil.isEmpty(secret)){
	    	basestring.append(secret);
	    }
	    String str = basestring.toString();
	    str = str.substring(0, str.length()-1);
        return StringUtil.doHash(str, "", "SHA1");
	    
	}
	
	/**
	 * 字典排序（ASCII排序）
	 * @param params
	 * @return
	 */
	private static StringBuilder sort(HashMap<String,String> params){
		Map<String, String> sortedParams = new TreeMap<String, String>(params);
	    Set<Entry<String, String>> entrys = sortedParams.entrySet();
	 
	    // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
	    StringBuilder basestring = new StringBuilder();
	    for (Entry<String, String> param : entrys) {
	        basestring.append(param.getKey()).append("=").append(param.getValue()).append("&");
	    }
	    return basestring;
	}
	
	/**
	 * map中的value根据字典排序
	 * @param params
	 * @return
	 */
	private static StringBuilder sortValue(HashMap<String,String> params){
		
//		Map<String, String> sortedParams = new TreeMap<String, String>(params);
	    Set<Entry<String, String>> entrys = params.entrySet();
	    List<String> vallist = new ArrayList<String>();
	    // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
	    StringBuilder basestring = new StringBuilder();
	    for (Entry<String, String> param : entrys) {
//	        basestring.append(param.getValue());
	    	vallist.add(param.getValue());
	    }
	    System.out.println("111" + JSONObject.toJSONString(vallist));
	    Collections.sort(vallist);
	    System.out.println("222" + JSONObject.toJSONString(vallist));
	    for(String str : vallist) {
	    	basestring.append(str);
	    }
	    System.out.println(basestring);
	    return basestring;
	}
	
	
	
	public static Map<String, String> sign(String jsapi_ticket, String url,String nonce_str,String timestamp) {
        Map<String, String> ret = new HashMap<String, String>();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        log.info("func[sign] basestring="+string1+" desc[start]");

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
	
   
}
