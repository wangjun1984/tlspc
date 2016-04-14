<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8" %>
<%@ include file="/inc/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7"/>
    <title>首页</title>

    <link href="/js/dwz-ria-1.5.0/themes/default/style.css" rel="stylesheet" type="text/css" media="screen"/>
    <link href="/js/dwz-ria-1.5.0/themes/css/core.css" rel="stylesheet" type="text/css" media="screen"/>
    <link href="/js/dwz-ria-1.5.0/themes/css/print.css" rel="stylesheet" type="text/css" media="print"/>
    <link href="/js/dwz-ria-1.5.0/uploadify/css/uploadify.css" rel="stylesheet" type="text/css" media="screen"/>
    <!--[if IE]>
    <link href="/js/dwz-ria-1.5.0/themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
    <![endif]-->
    <!-- <link href="/js/select2/select2.css" rel="stylesheet"/> -->

    <script src="/js/dwz-ria-1.5.0/js/speedup.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/jquery-1.7.2.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/jquery.cookie.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/jquery.validate.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/jquery.bgiframe.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/xheditor/xheditor-1.2.1.min.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/uploadify/scripts/jquery.uploadify.js" type="text/javascript"></script>

    <script src="/js/dwz-ria-1.5.0/js/dwz.core.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.util.date.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.validate.method.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.regional.zh.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.barDrag.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.drag.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.tree.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.accordion.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.ui.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.theme.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.switchEnv.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.alertMsg.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.contextmenu.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.navTab.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.tab.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.resize.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.dialog.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.dialogDrag.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.sortDrag.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.cssTable.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.stable.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.taskBar.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.ajax.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.pagination.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.database.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.datepicker.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.effects.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.panel.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.checkbox.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.history.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.combox.js" type="text/javascript"></script>
    <script src="/js/dwz-ria-1.5.0/js/dwz.print.js" type="text/javascript"></script>
    
<!--     <script src="/js/dwz-ria-1.5.0/bin/dwz.min.js" type="text/javascript"></script>
 -->    
    <script src="/js/dwz-ria-1.5.0/js/dwz.regional.zh.js" type="text/javascript"></script>
    <script type="text/javascript" src="/js/commonArea.js"></script>

	<script src="/js/highcharts/highcharts.js"></script>    
    <script src="/js/highcharts/exporting.js"></script>
    <!-- <script src="/js/select2/select2.js" type="text/javascript"></script> -->
    

    <script type="text/javascript">
        $(function () {
            DWZ.init("/js/dwz-ria-1.5.0/dwz.frag.xml", {
                loginUrl:"login_dialog.html", loginTitle:"登录", // 弹出登录对话框
//		loginUrl:"login.html",	// 跳到登录页面
                statusCode:{ok:200, error:300, timeout:301}, //【可选】
                pageInfo:{pageNum:"pageNum", numPerPage:"numPerPage", orderField:"orderField", orderDirection:"orderDirection"}, //【可选】
                debug:false, // 调试模式 【true|false】
                callback:function () {
                    initEnv();
                    $("#themeList").theme({themeBase:"/js/dwz-ria-1.5.0/themes"}); // themeBase 相对于index页面的主题base路径
                }
            });
        });
     </script>
     
</head>

<body scroll="no">
<div id="layout">
<div id="header">
    <div class="headerNav">
        <ul class="nav">
        	
        </ul>
    </div>

    <!-- navMenu -->

</div>

<div id="leftside">
    <div id="sidebar_s">
        <div class="collapse">
            <div class="toggleCollapse">
                <div></div>
            </div>
        </div>
    </div>
    <div id="sidebar">
        <div class="toggleCollapse"><h2>主菜单</h2>

            <div>收缩</div>
        </div>

        <div class="accordion" fillSpace="sidebar">
            <div class="accordionHeader">
                <h2>功能菜单</h2>
            </div>
            <div class="accordionContent">
                <ul class="tree treeFolder collapse">
                	<li>
                		<a>蛙鸣硬件测试管理</a>
                		<ul>
                			<li>
                				<a href="/admin/manufacturerInfo/?rel=manufacturerInfoHome" 
                					target="navTab" rel="manufacturerInfo" 
                					title="设备管理">厂家管理</a>
                			</li>
                			
                		</ul>
                        	
                    </li>
                
                </ul>
            </div>
        </div>
    </div>
</div>
<div id="container">
    <div id="navTab" class="tabsPage">
        <div class="tabsPageHeader">
            <div class="tabsPageHeaderContent"><!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
                <ul class="navTab-tab">
                    <li tabid="main" class="main"><a href="javascript:;"><span><span
                            class="home_icon">首页</span></span></a></li>
                </ul>
            </div>
            <div class="tabsLeft">left</div>
            <!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
            <div class="tabsRight">right</div>
            <!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
            <div class="tabsMore">more</div>
        </div>
        <!--<ul class="tabsMoreList">
            <li><a href="javascript:;">我的主页</a></li>
        </ul>
        -->
        <div class="navTab-panel tabsPageContent layoutBox">
            <div class="page unitBox">
                <div class="pageFormContent" layoutH="80" style="margin-right:230px">
                </div>
            </div>

        </div>
    </div>
</div>

</div>

<div id="footer">Copyright &copy; 2010 <a href="demo_page2.html" target="dialog">JAVA项目组</a></div>

<script type="text/javascript">
<!--
$(function() {
	//alert('当前操作的是【${currentHost }】！' );
});
//-->
</script>
</body>
</html>