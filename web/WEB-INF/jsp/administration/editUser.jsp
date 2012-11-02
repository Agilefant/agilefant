<%@ include file="../inc/_taglibs.jsp"%>

<struct:htmlWrapper navi="settings">

<jsp:body>

<c:choose>
<c:when test="${userId == currentUser.id}">
  <h2>My account</h2>
</c:when>
<c:otherwise>
  <c:if test="${currentUser.admin}">
  	<h2>Edit user</h2>
  </c:if>
  <c:if test="${!currentUser.admin}">
  	<h2>View user</h2>
  </c:if> 
</c:otherwise>
</c:choose>

<c:if test="${currentUser.admin || userId == currentUser.id}">
<script type="text/javascript">
$(document).ready(function() {
    var controller = new UserController({
    id:                  ${userId},
    userInfoElement:     $('#userInfoDiv'),
    passwordElement:     $('#changePasswordDiv')
  });
});
</script>
</c:if>
<c:if test="${!currentUser.admin && userId != currentUser.id}">
<script type="text/javascript">
$(document).ready(function() {
  var controller = new UserController({
    id:                  ${userId},
    userInfoElement:     $('#userInfoDiv')
  });
});
</script>
</c:if>

<div id="userInfoDiv" class="structure-main-block"> </div>

<c:if test="${currentUser.admin || userId == currentUser.id}">
<div id="changePasswordDiv" class="structure-main-block"> </div>

<div id="userSpecificSettingsDiv" class="structure-main-block">
<div class="dynamictable ui-widget-content ui-corner-all">
  
  <div class="dynamictable-caption dynamictable-caption-block ui-widget-header ui-corner-all">
    User specific settings
  </div> 
  
  <div class="warning-note">
    <p>These settings only affect the account of <strong>${user.fullName}</strong></p>
  </div>
  
  <ww:form action="storeUser" method="post">
    <ww:hidden name="userId"  />
    <table class="settings-table" style="margin: 0.3em;">
    
      <c:if test="${currentUser.admin}">
      <tr>
        <td>Assign me as an Administrator</td>
        <td><ww:checkbox fieldValue="true" name="user.admin"/></td>
      </tr>
      </c:if>
      
      <tr>
        <td>Assign me to tasks I create</td>
        <td><ww:radio list="#{'true':'Always','false':'Never'}" name="user.autoassignToTasks"/></td>
      </tr>
      <tr>
        <td>Assign me to stories I create</td>
        <td><ww:radio list="#{'true':'When not in story tree','false':'Never'}" name="user.autoassignToStories"/></td>
      </tr>
      <tr>
        <td>Mark story started when its task is started</td>
        <td><ww:radio list="#{'always':'Always','ask':'Ask','never':'Never'}" name="user.markStoryStarted"/></td>
      </tr>
      <%--      
      <tr>
        <td>Automatically mark story branch started</td>
        <td><ww:radio list="#{'always':'Always','ask':'Ask','never':'Never'}" name="user.markStoryBranchStarted"/></td>
        <td><a href="#" class="quickHelpLink" onclick="HelpUtils.openHelpPopup(this,'Automatically mark story branch started','static/html/help/markBranchStartedPopup.html'); return false;">What's this?</a></td>
      </tr>
      --%>  
    </table>
    
    <input type="submit" class="dynamics-button" value="Save"/>
    
  </ww:form>
  
</div>
</div>
</c:if>

</jsp:body>
</struct:htmlWrapper>
