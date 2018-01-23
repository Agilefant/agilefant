<%@tag description="Create new -menu"%>

<%@taglib uri="/WEB-INF/tlds/aef_structure.tld" prefix="struct" %>
<%@taglib uri="/WEB-INF/tlds/aef.tld" prefix="aef" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/struts-tags" prefix="ww" %>

<aef:existingObjects />

<script type="text/javascript">
$(document).ready(function() {
  var createNewMenu = $('#createNewMenu');

  $('#createNewMenuLink').click(function() {
    createNewMenu.show();
    createNewMenu.menuTimer();
  });
  
  $('#createNewMenu a').click(function() {
    createNewMenu.hide();
    createNewMenu.menuTimer('destroy');
    var id = $(this).attr('id');
    <c:choose>
      <c:when test="${product.id != null}">
        if (id == "createNewIteration") {
          CreateDialog.createById(id);
        } else {
          CreateDialog.createByIdWithAutofilledBacklogId(id, ${product.id});
        }
      </c:when>
      <c:when test="${project.id != null}">
        if (id == "createNewProject") {
          CreateDialog.createById(id);
        } else {
          CreateDialog.createByIdWithAutofilledBacklogId(id, ${project.id});
        }
      </c:when>
      <c:when test="${iteration.id != null}">
        if (id == "createNewStory") {
          CreateDialog.createByIdWithAutofilledBacklogId(id, null, ${iteration.id});
        } else {
          CreateDialog.createById(id);
        }
      </c:when>
      <c:otherwise>
        CreateDialog.createById(id);
      </c:otherwise>
    </c:choose>
  });
  
});
</script>

<ul id="createNewMenu" style="display: none">
    <li>
        <a href="#" id="createNewProduct" onclick="return false;"  title="Create a new product">Product &raquo;</a>
    </li>

    <li>
    <c:choose>
        <c:when test="${hasProducts}">
            <a href="#" id="createNewProject" onclick="return false;" title="Create a new project">Project &raquo;</a>
        </c:when>
        <c:otherwise>
            <span class="inactive" title="Create a product before creating a project">
            Project &raquo;</span>
        </c:otherwise>
    </c:choose>
    </li>
    
    <li>
        <a href="#" id="createNewIteration" onclick="return false;"  title="Create a new iteration">Iteration &raquo;</a>
    </li>
    
    
    <li>
    <c:choose>
        <c:when test="${hasProducts || hasIterations}">
            <a href="#" id="createNewStory" onclick="return false;"  title="Create a new story">Story &raquo;</a>
        </c:when>
        <c:otherwise>
            <span class="inactive"
                title="Create a product or iteration before creating a story">
            Story &raquo;</span>
        </c:otherwise>
    </c:choose>
    </li>
    
    <li class="separator"></li>
    
    <c:if test="${currentUser.admin}">
    <li>
      <a href="#" id="createNewTeam" onclick="return false;"  title="Create a new team">Team &raquo;</a>
    </li>
     </c:if>
     
    <li>
      <a href="#" id="createNewUser" onclick="return false;"  title="Create a new user">User &raquo;</a>
    </li>
    
</ul>

