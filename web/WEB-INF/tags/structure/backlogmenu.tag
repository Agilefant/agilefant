<%@ include file="../../jsp/inc/_taglibs.jsp" %>
<%@tag description="Agilefant backlog menu" %>

<%@attribute name="navi" fragment="false" required="false"%>

<div id="menuAccordion">
    <h3 id="menuAccordion-myAssignments"><a href="#">My Stories</a></h3>
    <div id="assignmentsMenuTree">&nbsp;</div>
    <h3 id="menuAccordion-products"><a href="#">All Backlogs</a></h3>
    <div id="backlogMenuTree">&nbsp;</div>
    <h3 id="menuAccordion-administration"><a href="#">Administration</a></h3>
    <div id="administrationMenu">&nbsp;</div>
</div>


<script type="text/javascript">
$(document).ready(function() {
  var navi = "${navi}";

  $("#menuAccordion").accordion({
    active: false,
    autoHeight: false,
    change: function(event, ui) {
      var selectedId = ui.newHeader[0].id;
      if (typeof(selectedId) === 'string' && selectedId !== "") {
        $.cookie('agilefant-menu-accordion', '#' + selectedId);
      }
      if (selectedId === 'menuAccordion-myAssignments') {
        if (window.myAssignmentsMenuController == null) {
          window.myAssignmentsMenuController = new MyAssignmentsMenuController($('#assignmentsMenuTree'), $('#menuControl'));
        } else {
          window.myAssignmentsMenuController.reload();
        }
      } else if (selectedId === 'menuAccordion-products') {
        if (window.menuController == null) {
          window.menuController = new BacklogMenuController($('#backlogMenuTree'), $('#menuControl'));
        }
        else {
          window.menuController.reload();
        }
    	} else if (selectedId === 'menuAccordion-administration') {
        if (window.administrationMenuController == null) {
          window.administrationMenuController = new AdministrationMenuController($('#administrationMenu'), $('#menuControl'));
        }
    	}
  	}
  });

  var activatedSection = $.cookie('agilefant-menu-accordion');
  if (navi === "settings") {
    $('#menuAccordion').accordion('activate', '#menuAccordion-administration');
  } else if (typeof(activatedSection) === 'string' && activatedSection !== "") {
    $("#menuAccordion").accordion('activate', activatedSection);
  } else {
    window.myAssignmentsMenuController = new MyAssignmentsMenuController($('#assignmentsMenuTree'), $('#menuControl'));
    $('#menuAccordion').accordion('activate', 0);
  }
});
</script>

