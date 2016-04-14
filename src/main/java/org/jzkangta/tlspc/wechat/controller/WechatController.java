/**
 * 
 */
package org.jzkangta.tlspc.wechat.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.jzkangta.tlspc.wechat.service.WechatService;
import org.jzkangta.tlspc.wechat.util.ReceiveMessageUtil;
import org.jzkangta.tlspc.framework.util.StringUtil;

@Controller
@RequestMapping("/wechat")
public class WechatController {

	private static Logger log = LoggerFactory.getLogger(WechatController.class);	

	@Resource
	private WechatService wechatService;
	
	/**
	 * 用于校验请求是否来源于微信
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public void verify(String signature, String timestamp, String nonce, String echostr, HttpServletResponse response) {
		log.info("func[verify] signature[{}] timestamp[{}] nonce[{}] echostr[{}] desc[receive request from weixin]",
				new Object[] { signature, timestamp, nonce, echostr });
		
		String result = "error";
		if (ReceiveMessageUtil.hashEquals(signature, timestamp, nonce)) {
			result = echostr;
		}
		PrintWriter printWriter = null;
		try {
			printWriter = response.getWriter();
			printWriter.write(result);
			printWriter.flush();
		} catch (IOException e) {
			log.error("func[verify] signature[{}] timestamp[{}] nonce[{}] echostr[{}] exception[{} - {}] desc[fail]",
					new Object[] { signature, timestamp, nonce, echostr, e.getMessage(), Arrays.deepToString(e.getStackTrace()) });
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
	}

	/**
	 * 接收微信服务器的信息并返回处理结果
	 * 特别说明：下发的信息列表使用的是NewsService接口中的getArticlesList方法（其中参数content为微信用户提交上来的指令信息），
	 * 			也就是说具体的实现类自己去实现列表的查询
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		Map<String , String> headerMap = new HashMap<String , String>();
		Enumeration<String> headerNames = request.getHeaderNames();
	    while(headerNames.hasMoreElements()) {
	    	String headerName = (String)headerNames.nextElement();
	    	headerMap.put(headerName ,request.getHeader(headerName));
	    }
		
		
		log.info("func[doPost] parameterMap[{}] desc[receive request from weixin] header [{}]", ReceiveMessageUtil.parameterMapToString(request),headerMap);
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		PrintWriter printWriter = null;
		
		String signature = request.getParameter("signature");//公众平台用到
		
		String msgSignature = request.getParameter("msg_signature");//企业微信用到
		if(StringUtil.isEmpty(signature))
			signature = msgSignature;//为了让代码通用，统一使用signature变量

        String timestamp = request.getParameter("timestamp");     
        String nonce = request.getParameter("nonce");  
		try {
			if (ReceiveMessageUtil.hashEquals(signature, timestamp, nonce)) {
				String respMessage = wechatService.push(request);
				printWriter = response.getWriter();
				printWriter.print(respMessage);
			}			
		} catch (Exception e) {
			log.error("func[doPost] msg[{}] desc[receive msg from weixin] error[{}]", ReceiveMessageUtil.sorts(timestamp, nonce),Arrays.deepToString(e.getStackTrace()));
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
	}
}
