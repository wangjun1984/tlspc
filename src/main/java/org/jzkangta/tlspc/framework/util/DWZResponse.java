/**
 * 
 */
package org.jzkangta.tlspc.framework.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * DWZ框架Ajax提交返回信息
 * 
 * @author tangjun@mail.39.net
 * @version 2012-5-3 - 上午10:28:38
 * 
 */
public class DWZResponse {
	// DWZ框架返回的ajax属性
	/** 状态码 */
	private final int statusCode;
	/** 详细信息 */
	private final String message;
	/** nav tab id */
	private final String navTabId;
	/** */
	private final String rel;
	/** js回调函数 */
	private final String callbackType;
	/** 跳转 */
	private final String forwardUrl;

	// 扩展属性
	/** 个性化属性 */
	private final Map<String, Object> attributes;

	/**
	 * 
	 * @param args
	 * @version 2012-5-8 - 下午04:17:15
	 * @author tangjun@mail.39.net
	 */
	public static void main(String[] args) {
		// 成功
		DWZResponse.Builder builder = DWZResponse.getSucessBuilder("操作成功");
		builder.callbackType("callbackType").forwardUrl("forwardUrl");
		System.out.println(builder.build().toString());

		// 失败
		builder = DWZResponse.getFailBuilder("操作失败");
		builder.callbackType("callbackType").forwardUrl("forwardUrl");
		System.out.println(builder.build().toString());

		// 超时
		builder = DWZResponse.getTimeOutBuilder("操作超时");
		builder.callbackType("callbackType").forwardUrl("forwardUrl");
		System.out.println(builder.build().toString());

		// 定制
		builder = DWZResponse.getBuilder(404, "操作定制");
		builder.callbackType("callbackType").forwardUrl("forwardUrl");
		builder.addAttribute("nullAttr", null).addAttribute("blankStr", "");
		builder.addAttribute("integer1", Integer.valueOf(1));
		System.out.println(builder.build().toString());
	}

	/**
	 * 
	 * @param builder
	 */
	public DWZResponse(Builder builder) {
		this.statusCode = builder.statusCode;
		this.message = builder.message;
		this.navTabId = builder.navTabId;
		this.rel = builder.rel;
		this.callbackType = builder.callbackType;
		this.forwardUrl = builder.forwardUrl;
		this.attributes = new HashMap<String, Object>(builder.attributes);
	}

	/**
	 * 
	 * @param toAll
	 *            true 则输出所有属性<br />
	 *            false 则只输出必要的属性（例如statusCode/message）
	 * @return
	 * @version 2012-5-3 - 上午10:42:09
	 * @author tangjun@mail.39.net
	 */
	public String toString(boolean toAll) {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append("\"statusCode\":\"").append(statusCode).append("\",");
		builder.append("\"message\":\"").append(message).append("\"");
		if (toAll) {
			builder.append(",");
			builder.append("\"navTabId\":\"").append(navTabId).append("\",");
			builder.append("\"rel\":\"").append(rel).append("\",");
			builder.append("\"callbackType\":\"").append(callbackType).append("\",");
			builder.append("\"forwardUrl\":\"").append(forwardUrl).append("\"");
			if (attributes != null && !attributes.isEmpty()) {
				Set<String> keySet = attributes.keySet();
				for (String key : keySet) {
					builder.append(",");
					Object value = attributes.get(key);
					if (value == null) {
						value = "";
					}
					builder.append("\"").append(key).append("\":\"");
					builder.append(value).append("\"");
				}
			}
		}
		builder.append("}");
		return builder.toString();
	}

	/**
	 * 返回全属性的json格式字符串
	 * 
	 * @see {@link #toString(boolean)}
	 */
	@Override
	public String toString() {
		return toString(true);
	}

	/**
	 * 返回一个表示成功的Builder实例
	 * 
	 * <pre>
	 * 默认值
	 * "statusCode":"200"
	 * </pre>
	 * 
	 * @param message
	 * @return
	 * @version 2012-5-5 - 上午11:19:01
	 * @author tangjun@mail.39.net
	 */
	public static Builder getSucessBuilder(String message) {
		Builder builder = new Builder(200, message);
		//builder.callbackType = "forward";
		return builder;
	}
	
	/**
	 * 返回一个表示成功的Builder实例
	 * 
	 * <pre>
	 * 默认值
	 * "statusCode":"200"
	 * "callbackType":"forward"
	 * </pre>
	 * 
	 * @param message
	 * @return
	 * @version 2014-11-26 - 上午09:12:01
	 * @author lijian
	 */
	public static Builder getSucessBuilder(String message, String forwardUrl) {
		Builder builder = new Builder(200, message);
		builder.callbackType = "forward";
		builder.forwardUrl = forwardUrl;
		return builder;
	}

	/**
	 * 返回一个表示失败的Builder实例
	 * 
	 * <pre>
	 * "statusCode":"300"
	 * "callbackType":"closeCurrent"
	 * </pre>
	 * 
	 * @param message
	 * @return
	 * @version 2012-5-5 - 上午11:20:43
	 * @author tangjun@mail.39.net
	 */
	public static Builder getFailBuilder(String message) {
		Builder builder = new Builder(300, message);
		builder.callbackType = "closeCurrent";
		return builder;
	}

	/**
	 * 返回一个表示超时的Builder实例
	 * 
	 * <pre>
	 * "statusCode":"301"
	 * "callbackType":"closeCurrent"
	 * </pre>
	 * 
	 * @param message
	 * @return
	 * @version 2012-5-5 - 上午11:20:43
	 * @author tangjun@mail.39.net
	 */
	public static Builder getTimeOutBuilder(String message) {
		Builder builder = new Builder(301, message);
		builder.callbackType = "closeCurrent";
		return builder;
	}

	/**
	 * 返回一个指定了statusCode和message的实例
	 * 
	 * @param statusCode
	 * @param message
	 * @return
	 * @version 2012-5-5 - 上午11:28:46
	 * @author tangjun@mail.39.net
	 */
	public static Builder getBuilder(int statusCode, String message) {
		return new Builder(statusCode, message);
	}

	/**
	 * Builder类
	 * 
	 * @author tangjun@mail.39.net
	 * @version 2012-5-3 - 上午10:32:18
	 * 
	 */
	public static class Builder {
		// 必须的属性
		private int statusCode;

		private String message;

		// 可选的属性
		private String navTabId = "";

		private String rel = "";

		private String callbackType = "";

		private String forwardUrl = "";

		// 个性化的属性
		private Map<String, Object> attributes = new HashMap<String, Object>();

		/**
		 * 
		 * @param statusCode
		 * @param message
		 */
		private Builder(int statusCode, String message) {
			this.statusCode = statusCode;
			this.message = message;
		}

		/**
		 * 
		 * @param navTabId
		 * @return
		 */
		public Builder navTabId(String navTabId) {
			this.navTabId = navTabId;
			return this;
		}

		/**
		 * 
		 * @param rel
		 * @return
		 */
		public Builder rel(String rel) {
			this.rel = rel;
			return this;
		}

		/**
		 * 
		 * @param forwardUrl
		 * @return
		 */
		public Builder forwardUrl(String forwardUrl) {
			this.forwardUrl = forwardUrl;
			return this;
		}

		/**
		 * 
		 * @param callbackType
		 * @return
		 */
		public Builder callbackType(String callbackType) {
			this.callbackType = callbackType;
			return this;
		}

		/**
		 * 
		 * @param key
		 * @param value
		 * @return
		 */
		public Builder addAttribute(String key, Object value) {
			this.attributes.put(key, value);
			return this;
		}

		/**
		 * 
		 * @return
		 */
		public DWZResponse build() {
			return new DWZResponse(this);
		}

	}

}