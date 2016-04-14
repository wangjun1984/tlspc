package org.jzkangta.tlspc.wechat.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import org.jzkangta.tlspc.wechat.entity.Articles;
import org.jzkangta.tlspc.wechat.entity.Video;
import org.jzkangta.tlspc.wechat.service.WechatService;
import org.jzkangta.tlspc.wechat.util.ReceiveMessageUtil;
import org.jzkangta.tlspc.framework.util.DateUtil;
import org.jzkangta.tlspc.framework.util.PropertyUtil;
import org.jzkangta.tlspc.framework.util.StringUtil;

/**
 * 
 * @author wushubin
 *
 */
public abstract class WechatBaseServiceImpl implements WechatService{	
	private static Logger log = LoggerFactory.getLogger(WechatBaseServiceImpl.class);
	private static Logger subscribeLog = LoggerFactory.getLogger("subscribe.user");
	private static String propertiesFileName = "wechat";
	private static String DEBUG ;

	static{
		DEBUG = PropertyUtil.getProperty(propertiesFileName, "debug");
	}

	/**
	 * 接收请求后，推送信息
	 * @param request
	 * @return
	 */
	@Override
	public String push(HttpServletRequest request) {
		String respMessage = null;
		try {
			Map<String, String> requestMap = ReceiveMessageUtil.parseXml(request);
			String msgType = requestMap.get("MsgType");//消息类型
			if ("true".equals(DEBUG)) {
				log.debug("func[processRequest] requestMap[{}] ", new Object[] { JSON.toJSONString(requestMap) });
			}
			
			if (ReceiveMessageUtil.REQ_MESSAGE_TYPE_EVENT.equalsIgnoreCase(msgType)) {
				respMessage = this.getEventResult(requestMap);
			} else if (ReceiveMessageUtil.REQ_MESSAGE_TYPE_DEVICE_TEXT.equalsIgnoreCase(msgType)) {
				respMessage = this.getDeviceMessageResult(requestMap);
			} else if (ReceiveMessageUtil.REQ_MESSAGE_TYPE_DEVICE_EVENT.equalsIgnoreCase(msgType)) {
				respMessage = this.getDeviceEventResult(requestMap);	
			} else if (ReceiveMessageUtil.getRequestMessageTypeMap(msgType) != null) {
				respMessage = this.getMessageResult(requestMap);	
			} else {
				respMessage = this.getUnknowResult(requestMap);
			}
		} catch (Exception e) {
			log.error("func[processRequest] exception[{} - {}] desc[fail]",
					new Object[] { e.getMessage(), Arrays.deepToString(e.getStackTrace()) });
		}
		return respMessage;
	}
	
	/**
	 * 根据事件返回信息
	 * @param requestMap
	 * @param articleList
	 * @return
	 */
	public String getEventResult(Map<String, String> requestMap ){
		String fromUserName = requestMap.get("FromUserName");//接收方帐号（收到的OpenID）
		String toUserName = requestMap.get("ToUserName");//开发者微信号
		String eventType = requestMap.get("Event");//事件类型
		String instructions = requestMap.get("Content");//用户上行内容
		String eventKey = requestMap.get("EventKey");//企业微信用户输入的认证信息
		String cardId =  requestMap.get("CardId");//审核卡券的ID
		String friendUserName =  requestMap.get("FriendUserName");//赠送方账号（一个 OpenID），"IsGiveByFriend” 为 1 时填写该参数
		String isGiveByFriend =  requestMap.get("IsGiveByFriend");//是否为转赠， 1 代表是， 0 代表否。
		String userCardCode = requestMap.get("UserCardCode");//code 序列号。
		String outerId = requestMap.get("OuterId");//领取场景值，用于领取渠道数据统计。
		String ticket = requestMap.get("Ticket");//用于查询推广渠道id
		String logMessage = "";
		String respMessage = "";
		//事件请求
		if (eventType.equalsIgnoreCase(ReceiveMessageUtil.EVENT_TYPE_SUBSCRIBE)) {
			if(!StringUtil.isEmpty(ticket))
				subscribeLog.info("{},{},{}",new Object[]{fromUserName ,ticket , DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") });
			
			logMessage = " 关注了";
			respMessage = this.handleSubscribeEventMessage(requestMap);
			
		} else if (eventType.equalsIgnoreCase(ReceiveMessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
			logMessage = " 取消关注了";
			respMessage = this.handleUnsubscribeEventMessage(requestMap);
			
		} else if (eventType.equalsIgnoreCase(ReceiveMessageUtil.EVENT_TYPE_CLICK)) {
			logMessage = " 点击投诉建议";
			Object contentList = this.getContentText(eventKey);
			respMessage = ReceiveMessageUtil.getTextMessage(fromUserName, toUserName, (String)contentList);
			
		} else if (eventType.equalsIgnoreCase(ReceiveMessageUtil.EVENT_TYPE_QY_VERIFY)) {
			logMessage = " 企业微信用户二次身份验证";
			respMessage = verifyQywechatUser(fromUserName, eventKey);
			
		} else if (eventType.equalsIgnoreCase(ReceiveMessageUtil.EVENT_TYPE_LOCATION)) {
			logMessage = " 首次上报地理位置事件";
			Object contentList = this.getContentList(instructions);
			respMessage = ReceiveMessageUtil.getNewsMessage(fromUserName, toUserName, (List<Articles>)contentList);
		}
		
		if ("true".equalsIgnoreCase(DEBUG)) {
			log.debug(toUserName + logMessage);
		}
		
		return respMessage;
	}
	
	public abstract String handleSubscribeEventMessage(Map<String, String> requestMap);
	public abstract String handleUnsubscribeEventMessage(Map<String, String> requestMap);
	
	
	/**
	 * 根据指令返回信息
	 * @param requestMap
	 * @param articleList
	 * @return
	 */
	public String getMessageResult(Map<String, String> requestMap){
		String fromUserName = requestMap.get("FromUserName");//接收方帐号（收到的OpenID）
		String toUserName = requestMap.get("ToUserName");//开发者微信号
		String msgType = requestMap.get("MsgType");//消息类型
		String instructions = requestMap.get("Content");//用户上行内容
		//
		String logMessage = ReceiveMessageUtil.getRequestMessageTypeMap(msgType);
		
		String respMessage = "";
		if(msgType.equalsIgnoreCase(ReceiveMessageUtil.REQ_MESSAGE_TYPE_LOCATION)){
			//上行地理信息
			String latitude = requestMap.get("Latitude");//用户纬度
			String longitude = requestMap.get("Longitude");//用户经度
			String precision = requestMap.get("Precision");//用户精度
			respMessage = ReceiveMessageUtil.getNewsMessage(fromUserName, toUserName, this.getContentList(latitude, longitude));
			
		} else if(msgType.equalsIgnoreCase(ReceiveMessageUtil.REQ_MESSAGE_TYPE_TEXT)){
			//上行指令
//			respMessage = ReceiveMessageUtil.getTextMessage(fromUserName, toUserName, this.getContentText(fromUserName, instructions));
			respMessage = this.replyTextMessage(fromUserName, toUserName, instructions);
			
		} else if(msgType.equalsIgnoreCase(ReceiveMessageUtil.REQ_MESSAGE_TYPE_IMAGE)){
			//上行图片
			//respMessage = ReceiveMessageUtil.getImageMessage(fromUserName, toUserName, this.getContentText(instructions));
			respMessage = "success";
		} else if(msgType.equalsIgnoreCase(ReceiveMessageUtil.REQ_MESSAGE_TYPE_VOICE)){
			//上行语音
			//respMessage = ReceiveMessageUtil.getVoiceMessage(fromUserName, toUserName, this.getContentText(instructions));
			respMessage = "success";
		} else if(msgType.equalsIgnoreCase(ReceiveMessageUtil.REQ_MESSAGE_TYPE_VIDEO)){
			//上行视频
			//respMessage = ReceiveMessageUtil.getVideoMessage(fromUserName, toUserName, this.getContentVideo(instructions));
			respMessage = "success";
		}
		
		if ("true".equals(DEBUG)) {
			log.debug(toUserName + logMessage);
		}
		
		return respMessage;
	}
	
	/**
	 * 未知指令，下发默认信息
	 * @param requestMap
	 * @return
	 */
	public String getUnknowResult(Map<String, String> requestMap){
		String fromUserName = requestMap.get("FromUserName");//开发者微信号
		String toUserName = requestMap.get("ToUserName");//接收方帐号（收到的OpenID）
		String msgType = requestMap.get("MsgType");//消息类型
		
		String logMessage = " 发出了未知的msgType请求：" + msgType;
		String respMessage =  ReceiveMessageUtil.getTextMessage(fromUserName, toUserName,"");
		if ("true".equalsIgnoreCase(DEBUG)) {
			log.debug(toUserName + logMessage);
		}
		return respMessage;
	}
	
	/**
	 * 获取视频内容
	 * @param instructions 微信指令
	 * @return
	 */
	public abstract Video getContentVideo(String instructions);
	
	
	/**
	 * 获取内容
	 * @param instructions 微信指令
	 * @return
	 */
	public abstract String getContentText(String instructions);
	
	/**
	 * 获取内容
	 * @param instructions 微信指令
	 * @return
	 */
	public abstract String getContentText(String toUserName, String instructions);

	/**
	 * 获取列表内容
	 * @param instructions 微信指令
	 * @return
	 */
	public abstract List<Articles> getContentList(String instructions);
	
	
	/**
	 * 根据用户经纬度，下发信息
	 * @param latitude 用户纬度
	 * @param longitude 用户经度
	 * @return
	 */
	public abstract List<Articles> getContentList(String latitude,String longitude );

	/**
	 * 企业微信用户二次身份验证
	 * @param fromUserName 员工userid
	 * @param key 员工输入的认证信息
	 * @return
	 */
	public abstract String verifyQywechatUser(String fromUserName,String key);
	
	/**
	 * 通知卡券审核信息
	 * @param cardId 卡券ID
	 * @param result 审核结果 0通过 1不通过
	 * @return
	 */
	public abstract String notifyCardStatus(String cardId,int result);
	
	/**
	 * 用户领取，赠送卡券通知
	 * @param cardId
	 * @param fromUserName 领券方帐号（一个 OpenID）
	 * @param friendUserName 赠送方账号（一个 OpenID），"IsGiveByFriend” 为 1 时填写该参数。
	 * @param isGiveByFriend  是否为转赠， 1 代表是， 0 代表否
	 * @param userCardCode  code 序列号
	 * @param outerId  领取场景值，用于领取渠道数据统计。
	 * @return
	 */
	public abstract String notifyReceiveCardCode(String cardId,String fromUserName,String friendUserName,String isGiveByFriend,String userCardCode,String outerId);
	
	/**
	 * 用户删除卡券通知
	 * @param cardId
	 * @param fromUserName 发送方帐号（一个 OpenID）
	 * @param userCardCode 商户自定义 code 值。 非自定 code 推送为空串。
	 * @return
	 */
	public abstract String notifyDeleteCardCode(String cardId,String fromUserName,String userCardCode );
	
	
	/**
	 * 根据事件返回信息
	 * @param requestMap
	 * @param articleList
	 * @return
	 */
	public String getDeviceEventResult(Map<String, String> requestMap ){
		//String fromUserName = requestMap.get("FromUserName");//发送方（微信用户）的 user name
		String toUserName = requestMap.get("ToUserName");//接收方（公众号）的 user name
		//String createTime = requestMap.get("CreateTime");//消息创建时间，消息后台生成
		String eventType = requestMap.get("Event");//事件类型，取值为 bind/unbind ,bind：绑定设备 unbind：解除绑定
		//String msgType = requestMap.get("MsgType");//消息类型：device_event
		//String deviceType = requestMap.get("DeviceType");//设备类型，目前为“公众账号原始 ID”
		//String deviceID = requestMap.get("DeviceID");//设备 ID，第三方提供
		//String content = requestMap.get("Content");//当 Event 为 bind 时， Content 字段存放二维码中 第三方追加的自定义的数据（详情见 1.5 章节  获取设备二维码  或 1.11.1 章节  API：获取 deviceid 和二维码）
		//String sessionID = requestMap.get("SessionID");//微信客户端生成的 session  id，用于 request 和response 对应，因此响应中该字段第三方需要原封不变的带回
		//String openID = requestMap.get("OpenID");//微信账号的 OpenID
		//String opType = requestMap.get("OpType");//请求类型：0：退订设备状态;1：心跳；（心跳的处理方式跟订阅一样）;2：订阅设备状态;
		String respMessage = "";
		String logMessage = "";
		//事件请求
		/*if (eventType.equalsIgnoreCase(ReceiveMessageUtil.EVENT_TYPE_DEVICE_BIND)) {
			logMessage = " 绑定蓝牙设备";
			DeviceBluetoothMessage deviceMessage = this.handleBluetoothBindEventMessage(requestMap);
			respMessage = ReceiveMessageUtil.getBluetoothEventMessage(deviceMessage);
		} else if (eventType.equalsIgnoreCase(ReceiveMessageUtil.EVENT_TYPE_DEVICE_UNBIND)) {
			logMessage = " 解绑蓝牙设备";
			DeviceBluetoothMessage deviceMessage = this.handleBluetoothUnbindEventMessage(requestMap);
			respMessage = ReceiveMessageUtil.getBluetoothEventMessage(deviceMessage);
		} else if (eventType.equalsIgnoreCase(ReceiveMessageUtil.EVENT_TYPE_DEVICE_SUBSCRIBE)) {
			logMessage = " 订阅WIFI设备";
			DeviceWifiMessage deviceMessage = this.handleWifiSubscribeEventMessage(requestMap);
			respMessage = ReceiveMessageUtil.getWifiEventMessage(deviceMessage);
		} else if (eventType.equalsIgnoreCase(ReceiveMessageUtil.EVENT_TYPE_DEVICE_UNSUBSCRIBE)) {
			logMessage = " 退订WIFI设备";
			DeviceWifiMessage deviceMessage = this.handleWifiUnsubscribeEventMessage(requestMap);
			respMessage = ReceiveMessageUtil.getWifiEventMessage(deviceMessage);
		}*/
		
		if ("true".equalsIgnoreCase(DEBUG)) {
			log.debug(toUserName + logMessage);
		}
		
		
		return respMessage;
	}
	
/*	public abstract DeviceBluetoothMessage handleBluetoothUnbindEventMessage(Map<String, String> requestMap);
	
	public abstract DeviceBluetoothMessage handleBluetoothBindEventMessage(Map<String, String> requestMap);
	
	public abstract DeviceWifiMessage handleWifiSubscribeEventMessage(Map<String, String> requestMap);
	
	public abstract DeviceWifiMessage handleWifiUnsubscribeEventMessage(Map<String, String> requestMap);*/
	
	
	/**
	 * 根据指令返回信息
	 * 注意：这个方法需要到时测试，现在还不知道有什么作用
	 * @param requestMap
	 * @param articleList
	 * @return
	 */
	public String getDeviceMessageResult(Map<String, String> requestMap){
		//String fromUserName = requestMap.get("FromUserName");//发送方（微信用户）的 user name
		//String toUserName = requestMap.get("ToUserName");//接收方（公众号）的 user name
		//String createTime = requestMap.get("CreateTime");//消息创建时间，消息后台生成
		//String msgType = requestMap.get("MsgType");//消息类型：device_text
		//String deviceType = requestMap.get("DeviceType");//设备类型，目前为“公众账号原始 ID”
		//String deviceID = requestMap.get("DeviceID");//设备 ID，第三方提供
		//String content = requestMap.get("Content");//消息内容，BASE64 编码
		//String sessionID = requestMap.get("SessionID");//微信客户端生成的 sessionid，用于 request 和response 对应，因此响应中该字段第三方需要原封不变的带回
		//String msgID = requestMap.get("MsgID");//消息 id，微信后台生成
		//String openID = requestMap.get("OpenID");//微信用户账号的 OpenID
		
//		DeviceMessage deviceMessage = this.handleDeviceTextMessage(requestMap);
//		
//		String respMessage = ReceiveMessageUtil.getDeviceMessage(deviceMessage);
//		
//		if ("true".equals(DEBUG)) {
//			log.info("func[getMessageResult] respMessage[{}] ",respMessage);
//		}
//		
//		return respMessage;
		return null;
	}
	
	//public abstract DeviceMessage handleDeviceTextMessage(Map<String, String> requestMap);
	
	public abstract String replyTextMessage(String fromUserName, String toUserName, String instructions);
	
}
