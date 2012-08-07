<%@ include file="./inc/_taglibs.jsp"%>

<struct:htmlWrapper navi="backlog">
<jsp:body>

<aef:backlogBreadCrumb backlog="${iteration}" />


<div class="structure-main-block" id="backlogInfo">
<ul class="backlogTabs">
  <li class=""><a href="#backlogDetails"><span><img
    alt="Edit" src="static/img/info.png" /> Info</span></a></li>
  <li class=""><a href="#backlogAssignees_cont"><span><img
    alt="Edit" src="static/img/team.png" /> Workload</span></a></li>
  <!-- <li class=""><a href="#iterationHistory"><span>History</span></a></li> --> <!-- Hide history tab until it's fixed to show right info -->
  <li id="iterationActions" class="ui-state-disabled dynamictable-captionaction ui-corner-all" style="float: right; opacity: 1 !important; filter: alpha(opacity = 100) !important; border-width: 1px !important;">
    Actions
  </li>
  
  
</ul>


<div class="details" id="backlogDetails" style="overflow: auto;">
<div id="detailContainer" style="width: 65%; float: left; padding: 8px;"></div>
<div style="width: 28%; float: right">
<div class="smallBurndown" style="background-image: url('drawSmallIterationBurndown.action?backlogId=${iteration.id}');">
 &nbsp;
</div>
<div id="iterationMetrics"><%@ include
  file="./inc/iterationMetrics.jsp"%></div>
</div>
</div>
<div class="details" id="backlogAssignees_cont">
    <div class="details" id="backlogAssignees"></div>
    Iteration availability denotes how unassigned load should bee divided within this iteration. If all assignees have the same iteration availability they will receive the same amount of unassigned load.
    <br/>
    Personal adjustment adjusts the iteration baseline load for each user.
  </div>
  <!-- <div class="details" id="iterationHistory">
    <div style="text-align:center; vertical-align: middle;">
      <img src="static/img/pleasewait.gif" style="display: inline-block; vertical-align: middle;"/> Loading...
    </div>-->
</div>
</div>


<script type="text/javascript">
$(document).ready(function() {
 
  $("#backlogInfo").tabs();
  var controller = new IterationController({
      id: ${iteration.id}, 
      storyListElement: $('#stories'), 
      backlogDetailElement: $('#detailContainer'),
      smallBurndownElement: null,
      burndownElement: null,
      assigmentListElement: $("#backlogAssignees"),
      hourEntryListElement: $("#backlogSpentEffort"),
      taskListElement: $("#tasksWithoutStory"),
      metricsElement: $("#iterationMetrics"),
      smallBurndownElement: $("#smallChart"),
      burndownElement: $("#bigChart"),
      tabs: $("#backlogInfo"),
      //historyElement: $("#iterationHistory")
  });

  /* Actions menu */
  var actionMenu = $('<ul class="actionCell backlogActions"/>').appendTo(document.body).hide();
  $('<li/>').text('Spent effort').click(function() {
    closeMenu();
    controller.openLogEffort();
  }).appendTo(actionMenu);

  $('<li/>').text('Export').click(function() {
    closeMenu();
    window.location="exportIteration.action?iterationId=${iterationId}";
  }).appendTo(actionMenu);
 
  $('<li/>').text('Delete').click(function() {
    closeMenu();
    controller.removeIteration();
  }).appendTo(actionMenu);

  $('<li/>').text('Share').click(function() {
	    closeMenu();
	    controller.shareIteration();
  }).appendTo(actionMenu);
  
  $('<li/>').text('Unshare').click(function() {
	    closeMenu();
	    controller.unshareIteration();
  }).appendTo(actionMenu);
  
  var closeMenu = function() {
    actionMenu.fadeOut('fast');
    actionMenu.menuTimer('destroy');
  };
  var openMenu = function(element) {
    var pos = $("#iterationActions").offset();
    actionMenu.css({
      position: 'absolute',
      top: pos.top + 20,
      left: pos.left
    });

    actionMenu.show();
    actionMenu.menuTimer({
      closeCallback: function() {
        closeMenu();
      }
    });
  };

 $('#iterationActions').click(function() { openMenu(); });
 
 var d = new Date();
 $("#chartid").attr("src", $("#chartid").attr('src') + -d.getTimezoneOffset());
 $("#chartlink").attr("href", $("#chartlink").attr('href') + -d.getTimezoneOffset());
});
</script>

<form onsubmit="return false;"><div id="stories" class="structure-main-block">&nbsp;</div></form>

<form onsubmit="return false;"><div id="tasksWithoutStory" class="structure-main-block">&nbsp;</div></form>

<p style="text-align: center;"><img id="chartid" src="drawIterationBurndown.action?backlogId=${iteration.id}&timeZoneOffset="
	id="bigChart" width="780" height="600" />
	<br>
	<a id="chartlink" href="drawCustomIterationBurndown.action?backlogId=${iteration.id}&customBdWidth=1280&customBdHeight=1024&timeZoneOffset=">Enlarge</a></p>

  </jsp:body>
</struct:htmlWrapper>
