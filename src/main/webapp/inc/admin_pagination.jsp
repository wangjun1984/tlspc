<%@ page language="java" pageEncoding="utf-8"%>

<div class="panelBar">
	<div class="pages">
		<span>显示</span>
		<select name="numPerPage" id="numPerPage${rel }" onchange="navTabPageBreak({numPerPage:this.value})">
			<option value="20">20</option>
			<option value="50">50</option>
			<option value="100">100</option>
			<option value="200">200</option>
		</select>
		
		<script type="text/javascript">
			$('#numPerPage${rel}').val("${pageTurn.pageSize}");
		</script>
		<span>条，共${pageTurn.rowCount}条</span>
	</div>
	<div class="pagination" 
		<c:choose>
			<c:when test="${!empty isLookup }">
				targetType="dialog" 
			</c:when>
			<c:otherwise>
				targetType="navTab"
			</c:otherwise>
		</c:choose>
		 totalCount="${pageTurn.rowCount }" numPerPage="${pageTurn.pageSize }" pageNumShown="10" currentPage="${pageTurn.page }"></div>
</div>
