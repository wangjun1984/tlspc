<%@ page contentType="text/html;charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<form id="pagerForm" method="post" action="/admin/dataTypeInfo">
	<input type="hidden" id="pageNum" name="pageNum" value="${null == pageTurn.page?1:pageTurn.page }" />
	<input type="hidden" name="numPerPage" value="${numPerPage }" />
	<input type="hidden" value="${eqId}" name="eqId" />
	<input type="hidden" value="${likeName}" name="likeName" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/admin/dataTypeInfo" method="post">
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
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="/admin/dataTypeInfo/edit" target="dialog" rel="editTag" mask="true" title="新增" width="620" height="500"><span>新增</span></a></li>
			<!-- <li><a class="delete" href="/admin/dataTypeInfo/delete" target="selectedTodo" rel="inIdList" title="确定删除?"><span>删除</span></a></li> -->
		</ul>
	</div>
	<table class="list" width="100%" layoutH="100">
		<thead>
			<tr>
				<th>ID<label style="float:left"><input type="checkbox" class="checkboxCtrl" group="inIdList" /></label></th>
				<th>名称</th>
				<th>创建时间</th>
				<th>类型前缀</th>
				<th>逻辑类名</th>
				<th>操作</th>
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
								<td>
									<input type="checkbox" name="inIdList" id="inIdList" value="${o.id}"/>${o.id}
								</td>
								<td>${o.name}</td>
								<td><fmt:formatDate value="${o.createTime }" pattern="yyyy-MM-dd HH:mm" /></td>
								<td>${o.prefix}</td>	
								<td>${o.className}</td>
								<td><a title="修改" target="dialog" href="${path}/admin/dataTypeInfo/edit?id=${o.id}&rel=${rel}" rel="${rel}"  width="610" height="550"><span>修改</span></a>&nbsp;</td>
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