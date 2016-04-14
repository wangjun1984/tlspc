<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.jzkangta.tlspc.framework.web.filter.SpringWebContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.util.*,java.io.*,java.text.*,java.net.*" %>
<%@ page import="org.jzkangta.tlspc.framework.web.*,org.jzkangta.tlspc.framework.util.* , org.jzkangta.tlspc.framework.exception.*," %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.Calendar"%>
<%@ taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="org.jzkangta.tlspc.entity.*" %>
<%@ page import="org.jzkangta.tlspc.entity._enum.*" %>
<%@ page import="org.jzkangta.tlspc.wechat.util.BaseWechatUtil"%>

<%@ page isELIgnored="false" %>
<%  
	WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
	String path = request.getContextPath();
	String serverDomain = request.getServerName();
	request.setAttribute("timeStamp",Long.toString(System.currentTimeMillis() / 1000));
	request.setAttribute("noncestr",UUID.randomUUID().toString());
	request.setAttribute("appsecret",BaseWechatUtil.APPSECRET);
	request.setAttribute("appId",BaseWechatUtil.APPID);
	request.setAttribute("time",new Date().getTime());
%>
<c:set var="path" value="<%=path %>"/>
<c:set var="CSS_PATH" value="${path }/css" scope="request" />
<c:set var="JS_PATH" value="${path }/js" scope="request" />
<c:set var="IMAGE_PATH" value="${path }/images" scope="request" />
<c:set var="CONTEXT_PATH" value="<%=request.getContextPath() %>"/>

