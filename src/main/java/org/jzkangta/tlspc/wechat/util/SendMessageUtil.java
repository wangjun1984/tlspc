package org.jzkangta.tlspc.wechat.util;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jzkangta.tlspc.wechat.entity.Articles;
import org.jzkangta.tlspc.wechat.entity.Department;
import org.jzkangta.tlspc.wechat.entity.Member;
import org.jzkangta.tlspc.wechat.entity.Menu;
import org.jzkangta.tlspc.wechat.entity.MenuConditional;
import org.jzkangta.tlspc.wechat.entity.MpArticles;
import org.jzkangta.tlspc.wechat.entity.OauthAccessToken;
import org.jzkangta.tlspc.framework.util.StringUtil;
import org.jzkangta.tlspc.framework.util.UrlUtil;
import org.jzkangta.tlspc.framework.util.file.FileUtil;
import org.jzkangta.tlspc.framework.util.httpclient.ContentTypeEnum;
import org.jzkangta.tlspc.framework.util.httpclient.HttpClientUtil;
import org.jzkangta.tlspc.framework.util.httpclient.PostObject;
import org.jzkangta.tlspc.system.web.exception.RequestIllegalException;

/**
 * 注：类名需要修改
 * @author wushubin
 *
 */
public class SendMessageUtil extends BaseWechatUtil{
	private final static Logger log = LoggerFactory.getLogger(SendMessageUtil.class);	
	
	public final static String ACCESS_TOKEN_CACHE_KEY = "ACCESS_TOKEN" + "_" + APPID;
	
	public final static String JS_API_TICKET_CACHE_KEY = "JS_API_TICKET" + "_" + APPID;
	
	public final static String CARD_API_TICKET_CACHE_KEY = "CARD_API_TICKET" + "_" + APPID;
	
	// 通过appid和secret获取access_token的接口地址（GET） 限200（次/天）
	private final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
	private final static String ACCESS_TOKEN_URL_PARAMS = "grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	private final static String GET_WEIXIN_IP = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN";

	private final static String GET_API_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
	private final static String GET_JS_API_TICKET_URL_PARAMS = "access_token=ACCESS_TOKEN&type=jsapi";
	private final static String GET_CARD_API_TICKET_URL_PARAMS = "access_token=ACCESS_TOKEN&type=wx_card";
	private final static String MASS_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";
	
	// 通过页面授权code 获取access_token
	private final static String OAUTH_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	private final static String OAUTH_ACCESS_TOKEN_URL_PARAMS = "appid=APPID&secret=APPSECRET&code=CODE&grant_type=authorization_code";
	
	//创建用户分组
	public final static String CREATE_GROUPS_URL  = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=ACCESS_TOKEN";
	
	//查询所有分组
	public final static String GET_GROUPS_URL  = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=ACCESS_TOKEN";
	
	//查询用户所在分组
	public final static String GET_GROUP_BY_OPENID_URL  = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=ACCESS_TOKEN";
	
	//修改分组
	public final static String UPDATE_GROUP_URL  = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=ACCESS_TOKEN";
	
	//批量移动用户
	public final static String GROUP_MEMBERS_BATCH_UPDATE_URL  = "https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?access_token=ACCESS_TOKEN";
	
	//移动用户分组
	public final static String GROUP_MEMBERS_UPDATE_URL  = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";
	
	//批量移动用户
	public final static String DELETE_USER_GROUP  = "https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=ACCESS_TOKEN";
	
	
	
	// 刷新页面授权后的access_token
	private final static String OAUTH_ACCESS_TOKEN_REFRESH_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	private final static String OAUTH_ACCESS_TOKEN_REFRESH_URL_PARAMS = "appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";

	// 页面oauth验证跳转，不能私有化，其他地方需要使用
	public final static String OAUTH_AUTHORIZE_REDIRECT_URL  = "https://open.weixin.qq.com/connect/oauth2/authorize?redirect_uri=REDIRECT_URI&appid=APPID&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	
	
	public enum OauthAuthorizeRedirectUrlParams{
		/**
		 * 不弹出授权页面，直接跳转，只能获取用户openid
		 */
		SCOPE_BASE("snsapi_base"),
		/**
		 * 弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息
		 */ 
		SCOPE_USERINFO("snsapi_userinfo"),
		
		/**
		 * 重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值
		 */ 
		STATE(StringUtil.getRandomString(5));
		
		String value;
		OauthAuthorizeRedirectUrlParams(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return this.value;
		}
	}
	
	// 企业微信token
	private final static String QY_ACCESS_TOKEN_URL =  "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
	private final static String QY_ACCESS_TOKEN_URL_PARAMS =  "corpid=APPID&corpsecret=APPSECRET";
	
	//获取企业媒体文件
	private final static String QY_MEDIA_GET_URL = "https://qyapi.weixin.qq.com/cgi-bin/media/get";
	private final static String QY_MEDIA_GET_URL_PARAMS = "access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	// 删除企业菜单
	private static String QY_MENU_DELETE_URL = "https://qyapi.weixin.qq.com/cgi-bin/menu/delete";
	private static String QY_MENU_DELETE_URL_PARAMS = "access_token=ACCESS_TOKEN&agentId=AGENT_ID";
	
	// 企业菜单创建（POST） 限100（次/天）
	private final static String QY_MENU_CREATE_URL = "https://qyapi.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN&agentid=AGENT_ID";
	
	// 获取菜单列表
	private final static String QY_MENU_LIST_URL = "https://qyapi.weixin.qq.com/cgi-bin/menu/get";
	private final static String QY_MENU_LIST_URL_PARAMS = "access_token=ACCESS_TOKEN&agentid=AGENT_ID";
	
	//添加部门
	private final static String QY_DEPARTMENT_ADD_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=ACCESS_TOKEN"; 
	//更新部门
	private final static String QY_DEPARTMENT_UPDATE_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token=ACCESS_TOKEN"; 
	
	//获取企业部门列表
	private final static String QY_DEPARTMENT_LIST_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/list"; 
	private final static String QY_DEPARTMENT_LIST_URL_PARAMS = "access_token=ACCESS_TOKEN&agentId=AGENT_ID"; 
	
	//删除部门
	private final static String QY_DEPARTMENT_DELETE_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/delete"; 
	private final static String QY_DEPARTMENT_DELETE_URL_PARAMS = "access_token=ACCESS_TOKEN&id=ID"; 
	
	//添加部门成员
	private final static String QY_MEMBER_CREATE_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=ACCESS_TOKEN"; 
	//更新成员
	private final static String QY_MEMBER_UPDATE_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=ACCESS_TOKEN"; 
	
	//删除成员
	private final static String QY_MEMBER_DELETE_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/delete"; 
	private final static String QY_MEMBER_DELETE_URL_PARAMS = "access_token=ACCESS_TOKEN&userid=USER_ID"; 
	
	//获取成员
	private final static String QY_MEMBER_GET_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/get"; 
	private final static String QY_MEMBER_GET_URL_PARAMS = "access_token=ACCESS_TOKEN&userid=USER_ID"; 
	
	//获取成员列表
	private final static String QY_MEMBER_LIST_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/list"; 
	private final static String QY_MEMBER_LIST_URL_PARAMS = "access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD"; 
	
	//获取应用代理
	private final static String QY_AGENT_GET_URL = "https://qyapi.weixin.qq.com/cgi-bin/agent/get"; 
	private final static String QY_AGENT_GET_URL_PARAMS = "access_token=ACCESS_TOKEN&agentid=AGENT_ID"; 
	
	//发送企业消息
	private final static String QY_MESSAGE_SEND_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";
	
	//根据微信服务器回调的code获取用户信息，code只能用一次
	private final static String QY_MESSAGE_USERINFO_GET_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo";
	private final static String QY_MESSAGE_USERINFO_GET_URL_PARAMS = "access_token=ACCESS_TOKEN&code=CODE&agentid=AGENT_ID";
	
	//上传多媒体
	private final static String QY_MEDIA_UPLOAD_URL = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	
	//下载多媒体
	private final static String MEDIA_DOWNLOAD_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get";
	private final static String MEDIA_DOWNLOAD_URL_PARAMS = "access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	
	
	// 通过页面授权 获取用户信息
	private final static String OAUTH_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";
	private final static String OAUTH_USER_INFO_URL_PARAMS = "access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	// 获取用户信息
	private final static String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info";
	private final static String USER_INFO_URL_PARAMS = "access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	
	// 菜单创建（POST） 限100（次/天）
	private final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	// 菜单删除
	private final static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete";
	private final static String MENU_DELETE_URL_PARAMS = "access_token=ACCESS_TOKEN";
	
	// 创建个性化菜单
	private final static String MENU_ADDCONDITIONAL_URL = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=ACCESS_TOKEN";
	// 个性菜单删除
	private final static String MENU_CONDITIONAL_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delconditional?access_token=ACCESS_TOKEN";
	// 测试个性化菜单匹配结果
	private final static String MENU_TRY_MATCH = "https://api.weixin.qq.com/cgi-bin/menu/trymatch?access_token=ACCESS_TOKEN";
	
	// 客服给用户发消息（POST） 48小时不限次数
	private final static String POST_CUSTOM_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
	
	// 获取模板ID
	private final static String TEMPLATE_ID_GET_URL = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";
	
	// 发送模板消息(POST)
	private final static String POST_TEMPLATE_MESSAGE_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	
	// 生成二维码ticket （公众号，可带推广链接）
	private final static String QRCODE_CREATE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
	//此接口获取的是一个图片
	private final static String SHOW_QRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode";
	private final static String SHOW_QRCODE_PARAMS = "ticket=TICKET";

	// 上传图片
	private final static String UPLOAD_IMG_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";
	
	// 群发
	private final static String MSG_SEND_ALL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";
	
	private static Map<String ,String> headMap= new HashMap<String,String>();
	
	static {
		headMap.put("User-Agent","Mozilla/4.0 (compatible; MSIE 7.0;)" );
		headMap.put("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint, */*");
		headMap.put("Bizmp-Version", "1.0");
	}	
	
	/**
	 * 获取用户信息
	 * 
	 * @param openId
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static JSONObject getUserInfo(String accessToken,String openId) {
		PostObject po = new PostObject();
		po.setContent(USER_INFO_URL_PARAMS.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(USER_INFO_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		log.info("func[getUserInfo] 获取微信用户的信息:" + jsonObject);
		return jsonObject;
	}
	
	/**
	 * 客服给用户发消息
	 * 
	 * @param 
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static JSONObject sendCustomMessage(String accessToken,String openId,String messageStr) {
		String paramStr = "{\"touser\":\""+openId+"\",\"msgtype\":\"text\",\"text\":{\"content\":\""+messageStr+"\"}}";
		PostObject po = new PostObject();
		po.setContent(paramStr);
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(POST_CUSTOM_MESSAGE.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		log.info("发送客服信息结果【{}】,paramStr【{}】:",new Object[]{JSONObject.toJSONString(jsonObject), paramStr});
		return jsonObject;
	}
	
	/**
	 * 发送客服接口-图文
	 * @param toUser
	 * @param toParty
	 * @param articles
	 * @param accessToken
	 * @return
	 */
	public static JSONObject sendNewsMessage(String toUser, String toParty, List<Articles> articles, String accessToken) {
		String paramStr = "{\"touser\":\""+toUser+"\",\"msgtype\":\"news\",\"news\":{\"articles\":[ARTICLES_JSON]}}";
		String articlesJson = "";
		for(Articles art : articles) {
			articlesJson += ",{\"title\":\"" + art.getTitle() + "\",\"description\":\"" + art.getDescription() + "\","
					+ "\"url\":\"" + art.getUrl() + "\",\"picurl\":\"" + art.getPicUrl() + "\""
					+ "}";
		}
		articlesJson = articlesJson.replaceFirst(",", "");
		PostObject po = new PostObject();
		po.setContent(paramStr.replace("ARTICLES_JSON", articlesJson));
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(POST_CUSTOM_MESSAGE.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 通过页面授权 获取用户信息
	 * @param accessToken
	 * @param openId
	 * @return
	 */
	public static JSONObject getOauthUserInfo(String accessToken,String openId) {
		PostObject po = new PostObject();
		po.setContent(OAUTH_USER_INFO_URL_PARAMS.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(OAUTH_USER_INFO_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 创建菜单
	 * 文档说明网址：http://mp.weixin.qq.com/wiki/index.php?title=%E8%87%AA%E5%AE%9A%E4%B9%89%E8%8F%9C%E5%8D%95%E5%88%9B%E5%BB%BA%E6%8E%A5%E5%8F%A3
	 * @param menu 菜单实例
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static JSONObject createMenu(Menu menu, String accessToken) {
		return createCommonMenu(menu, MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken));
	}
	
	/**
	 * 创建个性化菜单
	 * @param menuConditional
	 * @param accessToken
	 * @return
	 */
	public static JSONObject createMenuConditional(MenuConditional menuConditional, String accessToken) {
		return createCommonMenu(menuConditional, MENU_ADDCONDITIONAL_URL.replace("ACCESS_TOKEN", accessToken));
	}
	
	/**
	 * 创建企业微信菜单
	 * @param menu
	 * @param accessToken
	 * @return
	 */
	public static JSONObject createQyMenu(Menu menu, String accessToken) {
		return createCommonMenu(menu, QY_MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken).replace("AGENT_ID", AGENTID));
	}
	
	/**
	 * 通用创建菜单
	 */
	private static JSONObject createCommonMenu(Menu menu, String url) {
		PostObject po = new PostObject();
		po.setContent(JSON.toJSON(menu).toString());
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(url);
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 删除菜单
	 * 文档说明网址：http://mp.weixin.qq.com/wiki/index.php?title=%E8%87%AA%E5%AE%9A%E4%B9%89%E8%8F%9C%E5%8D%95%E5%88%9B%E5%BB%BA%E6%8E%A5%E5%8F%A3
	 * @param menu
	 * @param accessToken
	 * @return
	 */
	public static JSONObject deleteMenu(Menu menu, String accessToken) {
		PostObject po = new PostObject();
		po.setContent(MENU_DELETE_URL_PARAMS.replace("ACCESS_TOKEN", accessToken));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(MENU_DELETE_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 删除个性化菜单
	 * @param menuId
	 * @param accessToken
	 * @return
	 */
	public static JSONObject deleteConditionalMenu(String menuId, String accessToken) {
		PostObject po = new PostObject();
		JSONObject params = new JSONObject();
		params.put("menuid", menuId);
		po.setContent(params);
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(MENU_CONDITIONAL_DELETE_URL.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 删除企业菜单列表
	 * 文档说明网址：http://mp.weixin.qq.com/wiki/index.php?title=%E8%87%AA%E5%AE%9A%E4%B9%89%E8%8F%9C%E5%8D%95%E5%88%9B%E5%BB%BA%E6%8E%A5%E5%8F%A3
	 * @param menu
	 * @param accessToken
	 * @return
	 */
	public static JSONObject deleteQyMenu(Menu menu, String accessToken) {
		PostObject po = new PostObject();
		po.setContent(QY_MENU_DELETE_URL_PARAMS.replace("ACCESS_TOKEN", accessToken).replace("AGENT_ID", AGENTID));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_MENU_DELETE_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 获取企业菜单列表
	 * 文档说明网址：http://mp.weixin.qq.com/wiki/index.php?title=%E8%87%AA%E5%AE%9A%E4%B9%89%E8%8F%9C%E5%8D%95%E5%88%9B%E5%BB%BA%E6%8E%A5%E5%8F%A3
	 * @param menu
	 * @param accessToken
	 * @return
	 */
	public static JSONObject listQyMenu(String accessToken) {
		PostObject po = new PostObject();
		po.setContent(QY_MENU_LIST_URL_PARAMS.replace("ACCESS_TOKEN", accessToken).replace("AGENT_ID", AGENTID));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_MENU_LIST_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 获取access_token
	 * 文档说明网址：http://mp.weixin.qq.com/wiki/index.php?title=%E8%8E%B7%E5%8F%96access_token
	 * @param appid 凭证
	 * @param appsecret 密钥
	 * @return
	 */
	public static String getAccessToken() {
		if(StringUtil.isEmpty(APPID) || StringUtil.isEmpty(APPSECRET))
			throw new RequestIllegalException("APPID或APPSECRET为空");
		return getAccessToken(APPID,APPSECRET);
	}
	
	public static String getAccessToken(String appId, String appSecret) {
		PostObject po = new PostObject();
		String params = ACCESS_TOKEN_URL_PARAMS.replace("APPID", appId).replace("APPSECRET", appSecret);
		po.setContent(params);
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(ACCESS_TOKEN_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		log.info("获取新的access_token:" + jsonObject.getString("access_token")+" ACCESS_TOKEN_URL:"+ACCESS_TOKEN_URL+" params:"+params+" jsonObject:"+jsonObject);
		return jsonObject.getString("access_token");
	}
	
	/**
	 * 获取 js-api_ticket
	 * @param accessToken 
	 * @return
	 */
	public static String getJsApiTicket(String accessToken) {
		PostObject po = new PostObject();
		po.setContent(GET_JS_API_TICKET_URL_PARAMS.replace("ACCESS_TOKEN", accessToken));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(GET_API_TICKET_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		log.info("获取新的js-api_ticket:" + jsonObject);
		return jsonObject.getString("ticket");
	}
	
	/**
	 * 获取 wx-card-api_ticket
	 * @param accessToken 
	 * @return
	 */
	public static String getWxCardApiTicket(String accessToken) {
		PostObject po = new PostObject();
		po.setContent(GET_CARD_API_TICKET_URL_PARAMS.replace("ACCESS_TOKEN", accessToken));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(GET_API_TICKET_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		log.info("获取新的wx-card-api_ticket:" + jsonObject);
		return jsonObject.getString("ticket");
	}
	
	
	
	
	/**
	 * 获取企业微信的accesstoken<br/>
	 * 有效期7200秒，如果有消息交互，则自动续期
	 * @return
	 */
	public static String getQyAccessToken() {
		if(StringUtil.isEmpty(APPID) || StringUtil.isEmpty(APPSECRET))
			throw new RequestIllegalException("APPID或APPSECRET为空");
		return getQyAccessToken(APPID,APPSECRET);
	}
	
	public static String getQyAccessToken(String appId, String appSecret) {
		PostObject po = new PostObject();
		po.setContent(QY_ACCESS_TOKEN_URL_PARAMS.replace("APPID", appId).replace("APPSECRET", appSecret));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_ACCESS_TOKEN_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		log.info("获取新的access_token:" + jsonObject.getString("access_token"));
		return jsonObject.getString("access_token");
	}
	
	/**
	 * 通过oauth认证，获取access_token、openid
	 * 文档说明网址：http://mp.weixin.qq.com/wiki/index.php?title=%E7%BD%91%E9%A1%B5%E6%8E%88%E6%9D%83%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E5%9F%BA%E6%9C%AC%E4%BF%A1%E6%81%AF
	 * @param appid 凭证
	 * @param appsecret 密钥
	 * @return
	 */
	public static OauthAccessToken getOauthAccessToken(String code) {
		if(StringUtil.isEmpty(APPID) || StringUtil.isEmpty(APPSECRET))
			throw new RequestIllegalException("APPID或APPSECRET为空");
		return getOauthAccessToken(code, APPID, APPSECRET);
	}
	public static OauthAccessToken getOauthAccessToken(String code, String appId, String appSecret) {
		PostObject po = new PostObject();
		po.setContent(OAUTH_ACCESS_TOKEN_URL_PARAMS.replace("APPID", appId).replace("APPSECRET", appSecret).replace("CODE", code));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(OAUTH_ACCESS_TOKEN_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		log.info("func[getOauthAccessToken] 获取授权的access_token:" + jsonObject);
		OauthAccessToken accessToken = new OauthAccessToken();
		accessToken.setToken(jsonObject.getString("access_token"));
		accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
		accessToken.setOpenId(jsonObject.getString("openid"));
		accessToken.setRefreshToken(jsonObject.getString("refresh_token"));
		accessToken.setScope(jsonObject.getString("scope"));
		return accessToken;
	}
	
	/**
	 * 刷新通过oauth认证，获取的access_token
	 * 文档说明网址：http://mp.weixin.qq.com/wiki/index.php?title=%E7%BD%91%E9%A1%B5%E6%8E%88%E6%9D%83%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E5%9F%BA%E6%9C%AC%E4%BF%A1%E6%81%AF
	 * @param refershToken
	 * @return
	 */
	public static OauthAccessToken refreshOauthAccessToken(String refershToken) {
		if(StringUtil.isEmpty(APPID) || StringUtil.isEmpty(APPSECRET))
			throw new RequestIllegalException("APPID或APPSECRET为空");
		return refreshOauthAccessToken(refershToken, APPID);
	}
	
	public static OauthAccessToken refreshOauthAccessToken(String refershToken, String appId) {
		PostObject po = new PostObject();
		po.setContent(OAUTH_ACCESS_TOKEN_REFRESH_URL_PARAMS.replace("APPID", appId).replace("REFRESH_TOKEN", refershToken));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(OAUTH_ACCESS_TOKEN_REFRESH_URL);
		
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		OauthAccessToken accessToken = new OauthAccessToken();
		accessToken.setToken(jsonObject.getString("access_token"));
		accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
		accessToken.setOpenId(jsonObject.getString("openid"));
		accessToken.setRefreshToken(jsonObject.getString("refresh_token"));
		accessToken.setScope(jsonObject.getString("scope"));
		return accessToken;
	}
	
	/**
     * 通过code获取openid
     * @param code
     * @return {"access_token":"123","expires_in":"456","refresh_token":"789","openid":"12345"}
     */
    public static JSONObject getOpenid(String code) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	OauthAccessToken json = getOauthAccessToken(code);
    	map.put("access_token", json.getToken());
    	map.put("expires_in", json.getExpiresIn());
    	map.put("refresh_token", json.getRefreshToken());
    	map.put("openid", json.getOpenId());
    	JSONObject jsonObject = new JSONObject(map);
        return jsonObject;
    }
    
    /**
     * 获取媒体,暂未成功,调用接口返回流
     * @param mediaId
     * @return
     */
    public static Object getMedia(String accessToken, String mediaId) {
    	PostObject po = new PostObject();
		po.setContent(QY_MEDIA_GET_URL_PARAMS.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_MEDIA_GET_URL);
		return HttpClientUtil.get(po);
    }
    
    /**
     * 生成带参数关注二维码
     * @param 
     * @return
     */
    public static Object createQrcode(String accessToken, String sceneId, String sceneCode) {
    	PostObject po = new PostObject();
    	String paramsStr = "";
    	if(!StringUtil.isEmpty(sceneCode)) {
    		paramsStr = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+sceneCode+"\"}}}";
    	} else if(!StringUtil.isEmpty(sceneId)) {
    		paramsStr = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\":"+sceneId + "}}}";
    	}
    	
    	if(StringUtil.isEmpty(paramsStr)) return "";
    	
    	po.setContent(paramsStr);
    	po.setContentType(ContentTypeEnum.WWW_FORM.toString());
    	po.setHeaders(headMap);
    	po.setHttps(true);
    	po.setUrl(QRCODE_CREATE.replace("ACCESS_TOKEN", accessToken));
    	return HttpClientUtil.post(po);
    }
    
    /**
     * 获取带参数关注二维码
     * @param 
     * @return
     */
    public static Object showQrcode(String ticket) {
    	PostObject po = new PostObject();
    	po.setContent(SHOW_QRCODE_PARAMS.replace("TICKET", UrlUtil.encode(ticket)));
    	po.setContentType(ContentTypeEnum.IMAGE.toString());
    	po.setHeaders(headMap);
    	po.setHttps(true);
    	po.setUrl(SHOW_QRCODE);
    	return HttpClientUtil.get(po);
    }
    
    /**
	 * 添加企业部门
	 * @param accessToken
	 * @param department
	 * @return
	 */
	public static JSONObject createQyDepartment(String accessToken, Department department) {
		PostObject po = new PostObject();
		po.setContent(JSONObject.toJSONString(department));
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_DEPARTMENT_ADD_URL.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 更新企业部门
	 * @param accessToken
	 * @param department
	 * @return
	 */
	public static JSONObject updateQyDepartment(String accessToken, Department department) {
		PostObject po = new PostObject();
		po.setContent(JSONObject.toJSONString(department));
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_DEPARTMENT_UPDATE_URL.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 更新企业部门,需要管理员权限
	 * @param accessToken
	 * @param department
	 * @return
	 */
	public static JSONObject listQyDepartment(String accessToken) {
		PostObject po = new PostObject();
		po.setContent(QY_DEPARTMENT_LIST_URL_PARAMS.replace("ACCESS_TOKEN", accessToken));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_DEPARTMENT_LIST_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 删除企业部门,需要对应部门权限
	 * @param accessToken
	 * @param department
	 * @return
	 */
	public static JSONObject deleteQyDepartment(String accessToken, int id) {
		PostObject po = new PostObject();
		po.setContent(QY_DEPARTMENT_DELETE_URL_PARAMS.replace("ACCESS_TOKEN", accessToken).replace("ID", "" + id));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_DEPARTMENT_DELETE_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 添加成员
	 * @param member
	 * @param accessToken
	 * @return
	 */
	public static JSONObject createQyMember(Member member, String accessToken) {
		PostObject po = new PostObject();
		po.setContent(JSONObject.toJSONString(member));
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_MEMBER_CREATE_URL.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 修改成员
	 * @param member
	 * @param accessToken
	 * @return
	 */
	public static JSONObject updateQyMember(Member member, String accessToken) {
		PostObject po = new PostObject();
		po.setContent(JSONObject.toJSONString(member));
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_MEMBER_UPDATE_URL.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 删除成员
	 * @param member
	 * @param accessToken
	 * @return
	 */
	public static JSONObject deleteQyMember(String userId, String accessToken) {
		PostObject po = new PostObject();
		po.setContent(QY_MEMBER_DELETE_URL_PARAMS.replace("ACCESS_TOKEN", accessToken).replace("USER_ID", userId));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_MEMBER_DELETE_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 获取成员
	 * @param member
	 * @param accessToken
	 * @return
	 */
	public static JSONObject getQyMember(String userId, String accessToken) {
		PostObject po = new PostObject();
		po.setContent(QY_MEMBER_GET_URL_PARAMS.replace("ACCESS_TOKEN", accessToken).replace("USER_ID", userId));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_MEMBER_GET_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 获取成员列表
	 * @param accessToken
	 * @return
	 */
	public static JSONObject listQyMember(int departmentId,int fetchChild, String accessToken) {
		PostObject po = new PostObject();
		po.setContent(QY_MEMBER_LIST_URL_PARAMS.replace("ACCESS_TOKEN", accessToken).replace("DEPARTMENT_ID", "" + departmentId).replace("FETCH_CHILD", "" + fetchChild));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_MEMBER_LIST_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 获取应用代理
	 * @param accessToken
	 * @return
	 */
	public static JSONObject getAgent(String accessToken) {
		PostObject po = new PostObject();
		po.setContent(QY_AGENT_GET_URL_PARAMS.replace("ACCESS_TOKEN", accessToken).replace("AGENT_ID",  AGENTID));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_AGENT_GET_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 发送企业消息
	 * 
	 * @param 
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static JSONObject sendQyMessage(String accessToken, String toUser, String toParty, String messageStr) {
		String paramStr = "{\"touser\":\""+toUser+"\",\"toparty\":\""+toParty+"\",\"msgtype\":\"text\",\"text\":{\"content\":\""+messageStr+"\"},\"agentid\":\"" + AGENTID + "\"}";
		PostObject po = new PostObject();
		po.setContent(paramStr);
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_MESSAGE_SEND_URL.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 发送带文件信息
	 * 
	 * @param toUser
	 * @param toParty
	 * @param mediaId
	 * @param fileType -image,voice,video,file
	 * @param accessToken
	 * @return
	 */
	public static JSONObject sendQyFileMessage(String toUser, String toParty, String mediaId, String fileType, String accessToken) {
		String paramStr = "{\"touser\":\""+toUser+"\",\"toparty\":\""+toParty+"\",\"msgtype\":\""+fileType+"\",\""+fileType+"\":{\"media_id\":\""+mediaId+"\"},\"agentid\":\"" + AGENTID + "\"}";
		PostObject po = new PostObject();
		po.setContent(paramStr);
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_MESSAGE_SEND_URL.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	public static JSONObject sendQyNewsMessage(String toUser, String toParty, List<Articles> articles, String accessToken) {
		String paramStr = "{\"touser\":\""+toUser+"\",\"toparty\":\""+toParty+"\",\"msgtype\":\"news\",\"news\":{\"articles\":[ARTICLES_JSON]},\"agentid\":\"" + AGENTID + "\"}";
		String articlesJson = "";
		for(Articles art : articles) {
			articlesJson += ",{\"title\":\"" + art.getTitle() + "\",\"description\":\"" + art.getDescription() + "\","
					+ "\"url\":\"" + art.getUrl() + "\",\"picurl\":\"" + art.getPicUrl() + "\""
					+ "}";
		}
		articlesJson = articlesJson.replaceFirst(",", "");
		PostObject po = new PostObject();
		po.setContent(paramStr.replace("ARTICLES_JSON", articlesJson));
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_MESSAGE_SEND_URL.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 发送多图文消息，thumb_media_id为封面图，且必须是平台内图片
	 * @param toUser
	 * @param articles
	 * @param accessToken
	 * @return
	 */
	public static JSONObject sendMpNewsMessage(String toUser, String toParty, List<MpArticles> articles, String accessToken) {
		String paramStr = "{\"touser\":\""+toUser+"\",\"toparty\":\""+toParty+"\",\"msgtype\":\"mpnews\",\"mpnews\":{\"articles\":[ARTICLES_JSON]},\"agentid\":\"" + AGENTID + "\"}";
		String articlesJson = "";
		for(MpArticles art : articles) {
			articlesJson += ",{\"thumb_media_id\":\"" + art.getThumbMediaId() + "\",\"author\":\"" + art.getAuthor() + "\","
					+ "\"content_source_url\":\"" + art.getContentSourceUrl() + "\",\"content\":\"" + art.getContent() + "\","
					+ "\"digest\":\"" + art.getDigest() + "\",\"show_cover_pic\":\"" + art.getShowCoverPic() + "\","
					+ "\"title\":\"" + art.getTitle() + "\""
					+ "}";
		}
		articlesJson = articlesJson.replaceFirst(",", "");
		PostObject po = new PostObject();
		po.setContent(paramStr.replace("ARTICLES_JSON", articlesJson));
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_MESSAGE_SEND_URL.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 根据链接返回的code获取企业用户信息
	 * @param accessToken
	 * @param code
	 * @return
	 */
	public static JSONObject getQyUserInfo(String accessToken, String code) {
		PostObject po = new PostObject();
		po.setContent(QY_MESSAGE_USERINFO_GET_URL_PARAMS.replace("ACCESS_TOKEN", accessToken).replace("AGENT_ID",  AGENTID).replace("CODE", code));
		po.setContentType(ContentTypeEnum.WWW_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_MESSAGE_USERINFO_GET_URL);
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 上传多媒体文件
	 * @param accessToken
	 * @param file
	 * @return
	 */
	public static JSONObject uploadMedia(String accessToken, File file) {
		PostObject po = new PostObject();
		po.setContent(file);
		po.setContentType(ContentTypeEnum.MULTIPART_FORM.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QY_MEDIA_UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", FileUtil.sortFileType(file.getName())));
		Object o = HttpClientUtil.get(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	 /**
     * 下载多媒体
     * @param accessToken
     * @param mediaId
     * @return
     */
    public static Object downloadMedia(String accessToken, String mediaId) {
    	PostObject po = new PostObject();
		po.setContent(MEDIA_DOWNLOAD_URL_PARAMS.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId));
		po.setContentType(ContentTypeEnum.IMAGE.toString());
		po.setHeaders(headMap);
		po.setHttps(false);
		po.setUrl(MEDIA_DOWNLOAD_URL);
		return HttpClientUtil.get(po);
    }
	
	/**
	 * 获取模板ID
	 * 
	 * @param accessToken
	 * @param templateIdShort
	 * @return
	 */
	public static JSONObject getTemplatId(String accessToken, String templateIdShort) {
		String paramStr = "{\"template_id_short\":\""+templateIdShort + "\"}";
		PostObject po = new PostObject();
		po.setContent(paramStr);
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(TEMPLATE_ID_GET_URL.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 发送模板消息
	 * 
	 * @param accessToken
	 * @param data(模板可变参数填充)
	 * @return
	 */
	public static JSONObject sendTemplatMessage(String accessToken, String data) {
		PostObject po = new PostObject();
		po.setContent(data);
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(POST_TEMPLATE_MESSAGE_SEND_URL.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}

	/**
	 * 生成带参数的二维码
	 * @param qrcode
	 * @param accessToken
	 * @return
	 */
	public static JSONObject createQrcode(String json, String accessToken) {
		PostObject po = new PostObject();
		po.setContent(json);
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(QRCODE_CREATE.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 群发
	 * @param accessToken
	 * @return
	 */
	public static JSONObject msgSendAll(String json, String accessToken) {
		PostObject po = new PostObject();
		po.setContent(json);
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(MSG_SEND_ALL.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 测试个性化菜单匹配结果
	 * @param userId 可以是粉丝的OpenID，也可以是粉丝的微信号
	 * @param accessToken
	 * @return
	 */
	public static JSONObject menuTryMatch(String userId, String accessToken) {
		JSONObject jObj = new JSONObject();
		jObj.put("user_id", userId);
		PostObject po = new PostObject();
		po.setContent(jObj.toJSONString());
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl(MENU_TRY_MATCH.replace("ACCESS_TOKEN", accessToken));
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	
	/**
	 * 创建用户分组
	 * @param accessToken
	 * @return
	 */
	public static JSONObject createUserGroup(String groupName, String accessToken) {
		String groupJson = "{\"group\":{\"name\":\""+groupName+"\"}}}";
		PostObject po = new PostObject();
		po.setContent(groupJson);
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl( CREATE_GROUPS_URL.replace("ACCESS_TOKEN", accessToken) );
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 获取所有用户分组
	 * @param accessToken
	 * @return
	 */
	public static JSONObject getUserGroups(String accessToken) {
		PostObject po = new PostObject();
		po.setContent("");
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl( GET_GROUPS_URL.replace("ACCESS_TOKEN", accessToken) );
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 查询用户所在分组
	 * @param openId
	 * @param accessToken
	 * @return
	 */
	public static JSONObject getGroupByUserId(String openId, String accessToken) {
		String userJson = "{\"openid\":\"" + openId + "\"}";
		PostObject po = new PostObject();
		po.setContent(userJson);
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl( GET_GROUP_BY_OPENID_URL.replace("ACCESS_TOKEN", accessToken) );
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 修改分组
	 * @param openId
	 * @param accessToken
	 * @return
	 */
	public static JSONObject updateGroup(int groupId, String groupName, String accessToken) {
		String userGroupJson = "{\"group\":{\"id\":"+groupId+",\"name\":\""+ groupName +"\"}}";
		PostObject po = new PostObject();
		po.setContent(userGroupJson);
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl( UPDATE_GROUP_URL.replace("ACCESS_TOKEN", accessToken) );
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 批量移动用户分组
	 * size不能超过50
	 * @param openId
	 * @param accessToken
	 * @return
	 */
	public static JSONObject batchUpdateGroupMember(String[] openIdList, int groupId, String accessToken) {
		String userGroupJson = "{\"openid_list\":"+JSONArray.toJSONString(openIdList)+",\"to_groupid\":"+groupId+"}";
		PostObject po = new PostObject();
		po.setContent(userGroupJson);
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl( GROUP_MEMBERS_BATCH_UPDATE_URL.replace("ACCESS_TOKEN", accessToken) );
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 将用户移动到指定分组
	 * @param openId
	 * @param accessToken
	 * @return
	 */
	public static JSONObject updateGroupMember(String openId, int groupId, String accessToken) {
		String userGroupJson = "{\"openid\":\""+openId+"\",\"to_groupid\":"+groupId+"}";
		PostObject po = new PostObject();
		po.setContent(userGroupJson);
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl( GROUP_MEMBERS_UPDATE_URL.replace("ACCESS_TOKEN", accessToken) );
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 删除分组
	 * size不能超过50
	 * @param openId
	 * @param accessToken
	 * @return
	 */
	public static JSONObject deleteUserGroup(int groupId, String accessToken) {
		String userGroupJson = "{\"group\":{\"id\":"+groupId+"}}";
		PostObject po = new PostObject();
		po.setContent(userGroupJson);
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl( DELETE_USER_GROUP.replace("ACCESS_TOKEN", accessToken) );
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	/**
	 * 获取微信IP
	 * size不能超过50
	 * @param openId
	 * @param accessToken
	 * @return
	 */
	public static JSONObject getWeixinIp(String accessToken) {
		PostObject po = new PostObject();
		po.setContent("");
		po.setContentType(ContentTypeEnum.JSON.toString());
		po.setHeaders(headMap);
		po.setHttps(true);
		po.setUrl( GET_WEIXIN_IP.replace("ACCESS_TOKEN", accessToken) );
		Object o = HttpClientUtil.post(po);
		JSONObject jsonObject = JSON.parseObject(o.toString());
		return jsonObject;
	}
	
	
}
