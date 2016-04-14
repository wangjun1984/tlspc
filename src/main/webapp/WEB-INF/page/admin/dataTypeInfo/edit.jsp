<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<script type="text/javascript">
function processBack(response){
	alertMsg.correct(response.message);
	 $.pdialog.closeCurrent();
	 navTab.reload("${path }/admin/dataTypeInfo", "");
}
</script>
<div class="pageContent">
	<form method="post" action="${path }/admin/dataTypeInfo/${not empty model.id ? "update" :"add"}"
		 class="pageForm required-validate" onsubmit="return validateCallback(this, processBack);">
		<input name="id" type="hidden" size="75" value="${model.id }"/>
		<input type="hidden" name="rel" value="${rel}"/>
		<div class="pageFormContent" layoutH="57" align="left">
			<p class="nowrap">
            	<label>名称：</label>
				<input name="name" type="text"  value="${model.name }"  />            
			</p>
			<p class="nowrap">
            	<label>类型前缀：</label>
				<input name="prefix" type="text"  value="${model.prefix }"  />            
			</p>
			<p class="nowrap">
            	<label>逻辑类名：</label>
				<input name="className" type="text"  value="${model.className }"  />            
			</p>
		</div>
		<div class="formBar">
			<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">返回</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>