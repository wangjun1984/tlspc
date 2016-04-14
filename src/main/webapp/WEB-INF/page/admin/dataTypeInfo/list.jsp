<%@ page contentType="text/html;charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<form id="pagerForm" method="post" action="/admin/dataTypeInfo?isLookup=1">
	<input type="hidden" id="pageNum" name="pageNum" value="${null == pageTurn.page?1:pageTurn.page }" />
	<input type="hidden" name="numPerPage" value="${numPerPage }" />
	<input type="hidden" value="${eqId}" name="eqId" />
	<input type="hidden" value="${likeName}" name="likeName" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return dwzSearch(this, 'dialog');" action="/admin/dataTypeInfo?isLookup=1" method="post">
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<td>
					ID: <input type="text" value="${eqId}" name="eqId"></input>
				</td>
				<td>
					名称: <input type="text" value="${likeName}" name="likeName" ></input>
				</td>
				<td>
					<div class="buttonActive"><div class="buttonContent"><button type="submit">查询</button></div></div>
				</td>
			</tr>
		</table>
	</div>
	</form>

</div>
<div class="pageContent">
    <div layoutH="60">
		<table class="list" width="100%"  targetType="dialog">
			<thead>
				<tr>
					<th>名称</th>
					<th>创建时间</th>
					<th>类型前缀</th>
					<th>逻辑类名</th>
					<th width="10">查找带回</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${null==dataTypeInfoList||dataTypeInfoList.isEmpty()}">
						<tr class="tatr2">
							<td colspan="11" align="center">暂无数据</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="o" items="${dataTypeInfoList}">
							<tr target="id" rel="${o.id}">
								<td>${o.name}</td>
								<td><fmt:formatDate value="${o.createTime }" pattern="yyyy-MM-dd HH:mm" /></td>
								<td>${o.prefix}</td>	
								<td>${o.className}</td>
								<td><a class="btnSelect" href="javascript:$.bringBack({dataTypeId:'${o.id}',dataTypeName:'${o.name}'})" title="查找带回">选择</a></td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
	<div style="clear:both"></div>
    <div class="panelBar">
        <%@ include file="/inc/admin_pagination.jsp" %>
    </div>
</div>