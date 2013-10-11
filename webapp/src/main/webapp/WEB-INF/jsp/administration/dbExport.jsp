<%@ include file="../inc/_taglibs.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<struct:htmlWrapper navi="settings">

<jsp:body>

<h2>Database export</h2>


<c:choose>
<c:when test="${currentUser.admin}"> 

<div id="databaseExportDiv" class="structure-main-block">
<div class="dynamictable ui-widget-content ui-corner-all">
  
  <div class="dynamictable-caption dynamictable-caption-block ui-widget-header ui-corner-all">
    Database export
  </div> 
  
  <div class="warning-note">
    You can create a zipped dump of the database your Agilefant
	instance uses to save it on your computer. You can also
        use this to e.g. create manual backups of your database.
  </div>
  
  <form action="generateDbExport.action">
  	<input type="submit" value="Export database" class="dynamics-button" />
  </form>
  
  <c:choose>
    <c:when test="${importEnabled}"> 
    <s:form action="resultAction" namespace="/" method="POST" enctype="multipart/form-data">
    <s:file name="fileUpload" label="Select a zip file to upload" size="40" />
    <c:choose>
      <c:when test="${databaseHasExistingData}"> 
        <s:submit value="Import database" name="submit" 
      onClick="return confirm('Your current database is not empty. Are you sure you want to import more data to it?');"/>
      </c:when>
      <c:otherwise>
        <s:submit value="Import database" name="submit" />
      </c:otherwise>
    </c:choose>
    </s:form>
    </c:when>
    <c:otherwise>
      <div>Import database is disabled.</div>
    </c:otherwise>
  </c:choose>
  
</div>
</div> 

</c:when>
<c:otherwise>
  <h3>You are not an administrator, therefore you do not have permission to perform database export.</h3>
</c:otherwise>
</c:choose>

</jsp:body>
</struct:htmlWrapper>
