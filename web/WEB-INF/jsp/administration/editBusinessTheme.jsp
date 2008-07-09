<%@ include file="../inc/_taglibs.jsp"%>
<%@ include file="../inc/_header.jsp"%>
<aef:menu navi="administration" subnavi="themes" pageHierarchy="${pageHierarchy}" />
 
<ww:actionerror />
<ww:actionmessage />
<c:choose>
	<c:when test="${businessTheme.id == 0}">
		<h2>Create theme</h2>
	</c:when>
	<c:otherwise>
		<h2>Edit theme</h2>
	</c:otherwise>
</c:choose>
<ww:form action="storeBusinessTheme" method="post">
	<ww:hidden name="businessThemeId" value="${businessTheme.id}" />
	<table class="formTable">
		<tr>
			<td>Name</td>
			<td>*</td>
			<td colspan="2"><ww:textfield size="20" name="businessTheme.name" maxlength="20" /></td>
		</tr>
		<tr>
			<td>Description</td>
			<td></td>
			<td colspan="2"><ww:textarea cols="50" rows="7"
				name="businessTheme.description"/></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<c:choose>
				<c:when test="${businessTheme.id == 0}">
					<td><ww:submit value="Create" /></td>
				</c:when>
				<c:otherwise>
					<td><ww:submit value="Save" /></td>
					<td class="deleteButton"> <ww:submit onclick="return confirmDelete()"
						action="deleteBusinessTheme" value="Delete" /> </td>
				</c:otherwise>
			</c:choose>
		</tr>
	</table>
</ww:form>

<%@ include file="../inc/_footer.jsp"%>