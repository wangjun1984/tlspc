package org.jzkangta.tlspc.wechat.util;

import java.util.Date;

import org.jzkangta.tlspc.framework.util.DateUtil;



/**
 * 系统通知模版转换工具类
 *
 * @author zhuqiang
 * @version 2015年9月8日 - 上午9:49:37
 */
public class TemplateUtil {
	
	public static String convert(String url, String keyword, String remark){
		//模版id
		String templateId = "sIfDLZ_S1aUPYsFJZVrBirGCIMlV_DAJQfH5DcnAwSw";
		//模版内容
		String data = "{\"first\": {\"value\":\"firstData\",\"color\":\"#ff7f50\"},"
				+ "\"keyword1\": {\"value\":\"keyword1Data\",\"color\":\"#173177\"},"
				+ "\"keyword2\": {\"value\":\"keyword2Data\",\"color\":\"#173177\"},"
				+ "\"remark\": {\"value\":\"remarkData\",\"color\":\"#173177\"}}";
		//日期
		String date = DateUtil.formatDate(new Date(), "yyyy年MM月dd日 HH时mm分");
		//头部颜色
		String topcolor = "#000";
		String dataOther = data.replace("firstData", "")
				.replace("keyword1Data", keyword)
				.replace("keyword2Data", date)
				.replace("remarkData", remark);
		//模版
		String messageStr="{\"touser\":\"openId\",\"template_id\":\""+templateId+"\",\"url\":\""+url+
				"\",\"topcolor\":\""+topcolor+"\",\"data\":"+dataOther+"}";
		return messageStr;
	}
}
