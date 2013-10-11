<%@include file="./_taglibs.jsp" %>
<link rel="stylesheet" href="static/css/dailywork.css" type="text/css"/>

<style>
.backlogHierarchy li, .backlogHierarchy li a {
  font-size: 100% !important;
}
</style>

<div class="hierarchyContainer">

<c:if test="${parentStory != null}">
<h2>Story hierarchy</h2>
<div class="storyTreeContainer bubbleHierarchyContainer">
  <div class="tree">
    <ul>
      <aef:storyTreeNode node="${parentStory}" displayLinksToStories="true" forceOpen="true"/>
    </ul>
  </div>
</div>
</c:if>

<h2>Backlog hierarchy</h2>

<c:choose>
<c:when test="${task.iteration != null}">
  <c:set var="backlog" value="${task.iteration}"/>
</c:when>
<c:otherwise>
  <c:set var="backlog" value="${task.story.iteration}"/>
</c:otherwise>
</c:choose>

<ul class="backlogHierarchy">
<c:choose>
  <c:when test="${aef:isStandaloneIteration(backlog)}">
  <%-- Task with story (Work Queue display) --%>
  <c:if test="${task.story != null}">
	  <c:set var="storybacklog" value="${task.story.backlog}"/>
	  <c:if test="${storybacklog.parent.name != null}">
	    <li class = "backlogHierarchy-product">
	    <a href="editBacklog.action?backlogId=${storybacklog.parent.id}">
	      <c:out value="Product: ${storybacklog.parent.name}" />
	    </a>
	    </li>
	  </c:if>
	  <c:if test="${storybacklog.name != null}">
	    <li class = "backlogHierarchy-project">
	    <a href="editBacklog.action?backlogId=${storybacklog.id}">
	      <c:out value="Project: ${storybacklog.name}" />
	    </a>
	    </li> 
	  </c:if>
	  <li class = "backlogHierarchy-iteration">
	      <a href="editBacklog.action?backlogId=${backlog.id}">
	        <c:out value="Iteration: ${backlog.name}" />
	      </a>
	  </li>
    </c:if>
    <%-- Task without story --%>
    <c:if test="${task.story == null}">
	    <li class = "backlogHierarchy-iteration">
	      <a href="editBacklog.action?backlogId=${backlog.id}">
	        <c:out value="Iteration: ${backlog.name}" />
	      </a>
	    </li>
    </c:if>
  </c:when>
  
  <c:when test="${aef:isIteration(backlog)}">
  <c:if test="${backlog.parent.parent.name != null}">
    <li class = "backlogHierarchy-product"">
    <a href="editBacklog.action?backlogId=${backlog.parent.parent.id}">
      <c:out value="Product: ${backlog.parent.parent.name}" />
    </a>
    </li>
  </c:if>
  <c:if test="${backlog.parent.name != null}">
    <li class = "backlogHierarchy-project">
    <a href="editBacklog.action?backlogId=${backlog.parent.id}">
      <c:out value="Project: ${backlog.parent.name}" />
    </a>
    </li> 
  </c:if>
    <li class = "backlogHierarchy-iteration">
      <a href="editBacklog.action?backlogId=${backlog.id}">
        <c:out value="Iteration: ${backlog.name}" />
      </a>
    </li>
  </c:when>
  
  <c:when test="${aef:isProject(backlog)}">   
    <li class = "backlogHierarchy-project">
    <a href="editBacklog.action?backlogId=${backlog.parent.id}">
      <c:out value="Project: ${backlog.parent.name}" />
    </a>
    </li> 
    
    <li class = "backlogHierarchy-iteration">
      <a href="editBacklog.action?backlogId=${backlog.id}">
        <c:out value="Iteration: ${backlog.name}" />
      </a>
    </li>
  </c:when>
  
  <c:when test="${aef:isProduct(backlog)}">
    <li class = "backlogHierarchy-iteration">
      <a href="editBacklog.action?backlogId=${backlog.id}">
        <c:out value="Iteration: ${backlog.name}" />
      </a>
    </li>
  </c:when>
</c:choose>
</ul>

</div>