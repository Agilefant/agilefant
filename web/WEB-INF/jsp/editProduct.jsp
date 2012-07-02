<%@ include file="./inc/_taglibs.jsp"%>

<%
org.joda.time.DateTime now = new org.joda.time.DateTime();
pageContext.setAttribute("defaultStartDate", now.minusWeeks(2));
pageContext.setAttribute("defaultEndDate", now.plusMonths(3));
%>


<struct:htmlWrapper navi="backlog">
<jsp:attribute name="includeInHeader">
  
<script type="text/javascript" src="static/js/widgets/agilefantWidget.js"></script>

<style type="text/css">
.placeholder {
  height: 1em;
  width: 100%;
  background: #ffc;
  border: 1px dashed #ccc; 
}
.widgetContainer {
  width: 25% !important;
}
.widget {
  min-width: 200px !important;
}
.widgetHeader, .widgetHeader > div {
  cursor: auto !important;
}
.productWidget {
  max-height: 600px !important;
}
.productWidget .widgetContent {
  max-height: 500px !important;
}
.storyList {
  list-style-type: none;
  margin: 0;
  padding: 0;
}
.storyList li {
  list-style-type: none;
  margin-bottom: 3px;
  cursor: move;
  font-weight: normal !important;
}
.storyList li:hover {
  font-weight: bold;
}.
.ui-draggable-dragging {
  list-style-type: none !important;
  white-space: normal !important;
  max-width: 200px !important;
}
.ui-droppable-widget-hover {
  background-color: #e5f0f9;
  background-image: url('static/img/ui/ui-widget-droppable-gradient.png');
  background-repeat: repeat-x;
  font-weight: bold;
  color: #1d5987;
}
.timeframe {
  font-size: 8pt;
  margin-bottom: 5px;
}
.timeframe span {
  color: #666;
}
</style>
  
</jsp:attribute>

<jsp:body>


<aef:backlogBreadCrumb backlog="${product}" />

<div class="structure-main-block" id="backlogInfo">
<ul class="backlogTabs">
  <li class=""><a href="#backlogDetails"><span><img
    alt="Edit" src="static/img/info.png" /> Info</span></a></li>
  <li id="productActions" class="ui-state-disabled dynamictable-captionaction ui-corner-all" style="float: right; opacity: 1 !important; filter: alpha(opacity = 100) !important; border-width: 1px !important;">
    Actions
  </li>
</ul>

<div class="details" id="backlogDetails" style="overflow: auto;"></div>
</div>


<script type="text/javascript">
var filterByTime = function() {
  var filterStartDate = Date.fromString($('#startDateInput').val());
  var filterEndDate = Date.fromString($('#endDateInput').val()); 

  $('.scheduled').each(function() {
    var me = $(this);
    var startDate = Date.fromString(me.find('input[name=startDate]').val());
    var endDate = Date.fromString(me.find('input[name=endDate]').val());
    
    if (endDate.before(filterStartDate) || startDate.after(filterEndDate)) {
      me.hide();
    }
    else {
      me.show();
    }
  });

  /* Save to cookie */
  var cookieData = [ filterStartDate.getTime(), filterEndDate.getTime() ];
  jQuery.cookie('agilefant_productleafstories_timeframe_${product.id}', cookieData, {expires: 60});
};

$(document).ready(function() {
  $("#backlogInfo").tabs();
  $('#productContents').tabs({
    cookie: { name: 'agilefant-product-tabs' }
  });

  var controller = new ProductController({
    id: ${product.id},
    productDetailsElement: $("#backlogDetails"),
    projectListElement: $("#projects"),
    storyTreeElement: $('#storyTreeContainer'),
    hourEntryListElement: $("#backlogSpentEffort"),
    searchByTextElement: $('#searchByText'),
    backlogsElement: $('#leafStories'),
    tabs: $('#productContents')
  });
  if(Configuration.isTimesheetsEnabled()) {
  	$("#backlogInfo").bind('tabsselect', function(event, ui) {
	    if (ui.index == 1) {
      	controller.selectSpentEffortTab();
    	}
  	});
  }

  /*
   * PRODUCT ACTIONS MENU
   */
  var actionMenu = $('<ul class="actionCell backlogActions"/>').appendTo(document.body).hide();
  $('<li/>').text('Spent effort').click(function() {
    closeMenu();
    controller.openLogEffort();
  }).appendTo(actionMenu);

  $('<li/>').text('Delete').click(function() {
    closeMenu();
    controller.removeProduct();
  }).appendTo(actionMenu);

  
  var closeMenu = function() {
    actionMenu.fadeOut('fast');
    actionMenu.menuTimer('destroy');
  };
  var openMenu = function(element) {
    var pos = $("#productActions").offset();
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

  $('#productActions').click(function() { openMenu(); });

  /**
   * DATE FILTER TO LEAF STORIES
   */
 
  var datepickerOptions = {
    dateFormat: 'yy-mm-dd',
    numberOfMonths: 3,
    restrictInput: true
  };
 
  $('#startDateInput').datepicker(datepickerOptions);
  $('#endDateInput').datepicker(datepickerOptions);


  
  $('#filterByTime').click(function() {
    $('#productTimelineSlider').slider("values",[ Date.fromString($("#startDateInput").val()).getTime(), Date.fromString($("#endDateInput").val()).getTime() ]);
    filterByTime();
    $('.togglable').toggle();
  });
  
  /* Get the dates from cookie */
  var timeframeCookie = jQuery.cookie('agilefant_productleafstories_timeframe_${product.id}');
  var start = new Date(${defaultStartDate.millis});
  var end = new Date(${defaultEndDate.millis});
  if (timeframeCookie) {
    var cookieData = timeframeCookie.split(',');
    start = new Date(parseInt(cookieData[0],10));
    end = new Date(parseInt(cookieData[1],10));

    /* Set the textfields to match the cookie */
    $('#startDateInput').val(start.asString().substr(0,10));
    $('#startDateDisplay').text(start.asString().substr(0,10));
    $('#endDateInput').val(end.asString().substr(0,10));
    $('#endDateDisplay').text(end.asString().substr(0,10));
  }
  
  
  <c:if test="${scheduleStart != null && scheduleEnd != null}">
  var slideTimer = null;
  $('#productTimelineSlider').slider({
    range: true,
    animate: true,
    min: ${scheduleStart.millis},
    max: ${scheduleEnd.millis},
    values: [ start.getTime(), end.getTime() ],
    step: 86400000,
    slide: function(event, ui) {
      // Clear previous timer
      if (slideTimer) {
        clearTimeout(slideTimer);
      }
      
      // Update field
      var sliderStart = new Date(ui.values[0]);
      var sliderEnd = new Date(ui.values[1]);
    
      if (sliderStart.getTime() === sliderEnd.getTime()) {
        return false;
      }
      
      $('#startDateInput').val(sliderStart.asString().substr(0,10));
      $('#startDateDisplay').text(sliderStart.asString().substr(0,10));
      $('#endDateInput').val(sliderEnd.asString().substr(0,10));
      $('#endDateDisplay').text(sliderEnd.asString().substr(0,10));
      
      // Set the slide timer
      slideTimer = setTimeout(function() {
        filterByTime();
      }, 300);
    }
  });

  $('#showInputs').click(function() {
    $('.togglable').toggle();
  });
  </c:if>


});

</script>

<div style="margin-top: 3em;" class="structure-main-block project-color-header" id="productContents">
<ul class="backlogTabs">
  <li class=""><a href="#storyTreeContainer"><span><img
        alt="Story tree" src="static/img/story_tree.png" /> Breakdown</span></a></li>
  <li class=""><a href="#leafStories"><span><img
        alt="Backlogs" src="static/img/leaf_stories.png" /> Backlog</span></a></li>
  <li class=""><a href="#projects"><span><img
        alt="Projects" src="static/img/backlog.png" /> Projects</span></a></li>
  <li id="searchByText" style="float: right;"> </li>
</ul>

<form onsubmit="return false;">
  <div class="details" id="storyTreeContainer" style="position: relative;"></div>
  <div class="details" id="leafStories" style="position: relative;">
    
    <c:choose>
    <c:when test="${empty product.children}">
      <div class="static">
        <h2>Product Backlog</h2>
        <p>This product has no projects</p>
      </div>
    </c:when>
    <c:otherwise>
      <div class="static">
        <h2>Product Backlog</h2>
        <div>
          <p style="font-size: 8pt; color: #666;">
            Displaying projects and iterations between
            <span id="startDateDisplay" class="togglable"><joda:format value="${defaultStartDate}" pattern="YYYY-MM-dd" /></span>
            <input type="text" style="display: none;" name="startDate" class="togglable" id="startDateInput" size="10" value='<joda:format value="${defaultStartDate}" pattern="YYYY-MM-dd" />'/>
            
            and
            
            <span id="endDateDisplay" class="togglable"><joda:format value="${defaultEndDate}" pattern="YYYY-MM-dd" /></span><span class="togglable">.</span>
            <input type="text" style="display: none;" name="endDate" class="togglable" id="endDateInput" size="10" value='<joda:format value="${defaultEndDate}" pattern="YYYY-MM-dd" />' />
            
            <a href="#" id="showInputs" class="togglable linkColors">Change dates</a>
            <button class="dynamics-button togglable" style="display: none;" id="filterByTime">Filter</button>
          </p>
          <div>
            <div id="productTimelineSlider" style="margin-bottom: 0.4em;"> </div>
            <div style="font-size: 8pt; color: #666; float: right; width: 12ex; text-align: right;"><joda:format value="${scheduleEnd}" pattern="YYYY-MM-dd" /></div>
            <div style="font-size: 8pt; color: #666; width: 12ex;"><joda:format value="${scheduleStart}" pattern="YYYY-MM-dd" /></div>
          </div>
          
          
          
        </div>
      </div>

      <%@ include file="/WEB-INF/jsp/inc/overlay.jsp" %>

      <div class="content">
        
      </div>
    </c:otherwise>
    </c:choose>
    
  </div>
  <div class="details" id="projects"></div>
</form>

</div>


<div class="iterationStoryToolTip">
	<span>Stories in an iteration can't have child stories.</span>
</div>
</jsp:body>
</struct:htmlWrapper>
