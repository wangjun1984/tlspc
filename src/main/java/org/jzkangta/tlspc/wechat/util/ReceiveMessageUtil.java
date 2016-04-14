package org.jzkangta.tlspc.wechat.util;

import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import org.jzkangta.tlspc.wechat.entity.Articles;
import org.jzkangta.tlspc.wechat.entity.ArticlesMessage;
import org.jzkangta.tlspc.wechat.entity.BaseMessage;
import org.jzkangta.tlspc.wechat.entity.Image;
import org.jzkangta.tlspc.wechat.entity.ImageMessage;
import org.jzkangta.tlspc.wechat.entity.Music;
import org.jzkangta.tlspc.wechat.entity.MusicMessage;
import org.jzkangta.tlspc.wechat.entity.TextMessage;
import org.jzkangta.tlspc.wechat.entity.Video;
import org.jzkangta.tlspc.wechat.entity.VideoMessage;
import org.jzkangta.tlspc.wechat.entity.Voice;
import org.jzkangta.tlspc.wechat.entity.VoiceMessage;
import org.jzkangta.tlspc.framework.util.StringUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class ReceiveMessageUtil extends BaseWechatUtil{
	//回复的信息类型
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";
	public static final String RESP_MESSAGE_TYPE_IMAGE = "image";
	public static final String RESP_MESSAGE_TYPE_VOICE = "voice";
	public static final String RESP_MESSAGE_TYPE_VIDEO = "video";
	public static final String RESP_MESSAGE_TYPE_DEVICE_TEXT = "device_text";
	public static final String RESP_MESSAGE_TYPE_DEVICE_EVENT = "device_event";
	public static final String RESP_MESSAGE_TYPE_DEVICE_STATUS = "device_status";
	public static final String RESP_MESSAGE_TYPE_CUSTOMER = "transfer_customer_service";
	
	//请求的信息类型
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";
	public static final String REQ_MESSAGE_TYPE_VIDEO = "video";
	//public static final String REQ_MESSAGE_TYPE_LINK = "link";
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";
	public static final String REQ_MESSAGE_TYPE_DEVICE_TEXT = "device_text";
	public static final String REQ_MESSAGE_TYPE_DEVICE_EVENT = "device_event";
	
	
	
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";//关注事件
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";//取消关注事件
	public static final String EVENT_TYPE_QY_VERIFY = "verify";//企业微信用户二次身份验证
	public static final String EVENT_TYPE_CLICK = "CLICK";//菜单点击事件
	public static final String EVENT_TYPE_LOCATION = "LOCATION";//上报地理位置事件
	public static final String EVENT_TYPE_SCAN = "SCAN";//（已关注）用户扫描二维码事件
	public static final String EVENT_TYPE_DEVICE_BIND = "BIND";//蓝牙设备绑定
	public static final String EVENT_TYPE_DEVICE_UNBIND = "UNBIND";//蓝牙设备解绑
	public static final String EVENT_TYPE_DEVICE_SUBSCRIBE = "subscribe_status";//无线wifi设备订阅
	public static final String EVENT_TYPE_DEVICE_UNSUBSCRIBE = "unsubscribe_status";//无线wifi设备退订
	
	public static final String CARD_PASS_CHECK = "card_pass_check";//卡券通过审核
	public static final String CARD_NOT_PASS_CHECK = "card_not_pass_check";//卡券未通过审核
	public static final String USER_GET_CARD = "user_get_card";//用户领取卡券
	public static final String USER_DEL_CARD = "user_del_card";//用户删除卡券
	
	private static final SortedMap<String, String> messageTypeMap = new TreeMap<String, String>();

	public static String getRequestMessageTypeMap(String msgType) {
		messageTypeMap.put(REQ_MESSAGE_TYPE_TEXT, "您发送的是文本消息!");
		messageTypeMap.put(REQ_MESSAGE_TYPE_IMAGE, "您发送的是图片消息!");
		//messageTypeMap.put(REQ_MESSAGE_TYPE_LINK, "您发送的是链接消息!");
		messageTypeMap.put(REQ_MESSAGE_TYPE_VOICE, "您发送的是地理位置消息!");	
		messageTypeMap.put(REQ_MESSAGE_TYPE_LOCATION, "您发送的是地理位置消息!");	
		messageTypeMap.put(REQ_MESSAGE_TYPE_VIDEO, "您发送的是地理位置消息!");	
		return messageTypeMap.get(msgType);
	}

	/**
	 * 微信调用我们服务端的参数认证
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean hashEquals(String signature, String timestamp,
			String nonce) {		
		return signature.equals(hash(timestamp, nonce));
	}
	
	public static String hash(String timestamp, String nonce) {		
		return StringUtil.doHash(sorts(timestamp, nonce), "","SHA1");
	}

	public static String sorts(String timestamp, String nonce) {
		StringBuilder builder = new StringBuilder();
		ArrayList<String> params = new ArrayList<String>();
		params.add(TOKEN);
		params.add(timestamp);
		params.add(nonce);
		Collections.sort(params);
		for (String param : params) {
			builder.append(param);
		}
		return builder.toString();
	}

	public static String parameterMapToString(HttpServletRequest request) {
		StringBuilder builder = new StringBuilder();
		@SuppressWarnings("unchecked")
		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			String value = request.getParameter(key);
			builder.append(key).append("=").append(value).append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
	
	/**
	 * request解析为xml对象
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		
		InputStream inputStream = request.getInputStream();
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		Element root = document.getRootElement();
		List<Element> elementList = root.elements();
		
		for (Element e : elementList)
			map.put(e.getName(), e.getText());
				
		inputStream.close();
		inputStream = null;
		
		return map;		
	}
	

	
	public static String textMessageToXml(TextMessage textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}
	
	public static String musicMessageToXml(MusicMessage musicMessage) {
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}
	
	public static String videoMessageToXml(VideoMessage videoMessage) {
		xstream.alias("xml", videoMessage.getClass());
		return xstream.toXML(videoMessage);
	}
	
	public static String imageMessageToXml(ImageMessage imageMessage) {
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}
	
	public static String voiceMessageToXml(VoiceMessage voiceMessage) {
		xstream.alias("xml", voiceMessage.getClass());
		return xstream.toXML(voiceMessage);
	}
	
	public static String newsMessageToXml(ArticlesMessage newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Articles().getClass());
		return xstream.toXML(newsMessage);
	}
	
	public static String customerMessageToXml(BaseMessage baseMessage) {
		xstream.alias("xml", baseMessage.getClass());
		return xstream.toXML(baseMessage);
	}
	
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				boolean cdata = true;
				
				@SuppressWarnings("unchecked")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}
				
				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
	
	
	
	/**
	 * 回复视频信息
	 * @param fromUserName
	 * @param toUserName
	 * @param content
	 * @return
	 */
	public static String getVideoMessage(String fromUserName,
			String toUserName, Video content) {
		VideoMessage message = new VideoMessage();
		message.setToUserName(fromUserName);
		message.setFromUserName(toUserName);
		message.setCreateTime(new Date().getTime());
		message.setMsgType(RESP_MESSAGE_TYPE_IMAGE);
		message.setFuncFlag(0);
		message.setVideo(content);
		return videoMessageToXml(message);
	}
	
	/**
	 * 回复音乐信息
	 * @param fromUserName
	 * @param toUserName
	 * @param content
	 * @return
	 */
	public static String getMuiceMessage(String fromUserName,
			String toUserName, Music content) {
		MusicMessage message = new MusicMessage();
		message.setToUserName(fromUserName);
		message.setFromUserName(toUserName);
		message.setCreateTime(new Date().getTime());
		message.setMsgType(RESP_MESSAGE_TYPE_IMAGE);
		message.setFuncFlag(0);
		message.setMusic(content);
		return musicMessageToXml(message);
	}
	
	/**
	 * 回复语音信息
	 * @param fromUserName
	 * @param toUserName
	 * @param content
	 * @return
	 */
	public static String getVoiceMessage(String fromUserName,
			String toUserName, String content) {
		VoiceMessage message = new VoiceMessage();
		message.setToUserName(fromUserName);
		message.setFromUserName(toUserName);
		message.setCreateTime(new Date().getTime());
		message.setMsgType(RESP_MESSAGE_TYPE_IMAGE);
		message.setFuncFlag(0);
		Voice voice = new Voice();
		voice.setMediaId(content);
		message.setVoice(voice);
		return voiceMessageToXml(message);
	}
	
	/**
	 * 回复图片信息
	 * @param fromUserName
	 * @param toUserName
	 * @param content
	 * @return
	 */
	public static String getImageMessage(String fromUserName,
			String toUserName, String content) {
		ImageMessage message = new ImageMessage();
		message.setToUserName(fromUserName);
		message.setFromUserName(toUserName);
		message.setCreateTime(new Date().getTime());
		message.setMsgType(RESP_MESSAGE_TYPE_IMAGE);
		message.setFuncFlag(0);
		Image image = new Image();
		image.setMediaId(content);
		message.setImage(image);
		return imageMessageToXml(message);
	}
	
	
	/**
	 * 回复文本消息
	 * @param fromUserName
	 * @param toUserName
	 * @param pro
	 * @return
	 */
	public static String getTextMessage(String fromUserName,
			String toUserName, String content) {
		TextMessage message = new TextMessage();
		message.setToUserName(fromUserName);
		message.setFromUserName(toUserName);
		message.setCreateTime(new Date().getTime());
		message.setMsgType(RESP_MESSAGE_TYPE_TEXT);
		message.setFuncFlag(0);
		if(StringUtil.isEmpty(content))
			message.setContent(DEFAULT_MESSAGE);
		else
			message.setContent(content);
		return textMessageToXml(message);
	}
	
	/**
	 * 回复图文消息
	 * @param fromUserName
	 * @param toUserName
	 * @param pro
	 * @return
	 */
	public static String getNewsMessage(String fromUserName, String toUserName,List<Articles> articleList) {
		ArticlesMessage message =new ArticlesMessage();
		message.setToUserName(fromUserName);
		message.setFromUserName(toUserName);
		message.setCreateTime(new Date().getTime());
		message.setMsgType(RESP_MESSAGE_TYPE_NEWS);
		message.setFuncFlag(0);
		message.setArticleCount(articleList.size());
		message.setArticles(articleList);
		return newsMessageToXml(message);
	}
	
	/**
	 * 转发多客服系统
	 * @param fromUserName
	 * @param toUserName
	 * @return
	 */
	public static String getCustomerMessage(String fromUserName, String toUserName) {
		BaseMessage message = new BaseMessage();
		message.setToUserName(fromUserName);
		message.setFromUserName(toUserName);
		message.setCreateTime(new Date().getTime());
		message.setMsgType(RESP_MESSAGE_TYPE_CUSTOMER);
		message.setFuncFlag(0);
		return customerMessageToXml(message);
	}
	
	/**
	 * 
	 * 判断是否是白名单中的人员
	 * @param pro
	 * @return 
	 */
	public static boolean isWhiteList(String fromUserName) {
		List<String> white_list = new ArrayList<String>();
		String[] whitelists = WHITE_USER_LIST.split(",");
		for (int i = 0; i < whitelists.length; i ++) {
			white_list.add(whitelists[i]);
		}
		if (white_list.contains(fromUserName) || white_list.contains("开放")) {
			return true;
		}
		return false;
	}
	
}
