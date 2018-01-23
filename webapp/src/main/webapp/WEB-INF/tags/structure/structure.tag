<%@tag description="Wrapper for the Agilefant html structure" %>

<%@taglib uri="/WEB-INF/tlds/aef_structure.tld" prefix="struct" %>
<%@taglib uri="/WEB-INF/tlds/aef.tld" prefix="aef" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/struts-tags" prefix="ww" %>

<%@attribute name="navi" fragment="false" required="true"%>

<%@attribute name="includeInHeader" fragment="true" %>
<%@attribute name="headerContent" fragment="true" %>
<%@attribute name="menuContent" fragment="true" %>

<%@attribute name="hideLogout" fragment="false" required="false" %>
<%@attribute name="hideControl" fragment="false" required="false" %>
<%@attribute name="hidecreateAndSearchWrapper" fragment="false" required="false" %>
<%@attribute name="hideMenu" fragment="false" required="false" %>
<%@attribute name="hideFooter" fragment="false" required="false" %>
<%@attribute name="hideHeader" fragment="false" required="false" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

<head>
  <title>Agilefant</title>
  <aef:css path="main" minify="true" />
  <!--[if IE 7]><aef:css path="IE7styles.css" /><![endif]-->
  <!--[if IE 8]><aef:css path="IE8styles.css" /><![endif]-->
  
  <link rel="shortcut icon" href="static/img/favicon.png" type="image/png" />

  <script type="text/javascript">
  if (!console) {
    var console = { log: function() {} }; 
  }
  </script>

	<c:choose>
		<c:when test="${aef:releaseMode()}">
			<aef:javascript path="main" minify="always" />
		</c:when>
		<c:otherwise>
			<aef:javascript path="jquery" />
			<aef:javascript path="jquery.cookie" />
			<aef:javascript path="jquery-ui" minify="always" />
			<aef:javascript path="jquery.dynatree" />
			<aef:javascript path="date" />
			<aef:javascript path="jquery.wysiwyg" />
			<aef:javascript path="backlogChooser" />
			<aef:javascript path="backlogSelector" />
			<aef:javascript path="jquery.hotkeys" />
			<aef:javascript path="jquery.jstree" />
			<aef:javascript path="jquery.tooltip" />
			<aef:javascript path="jquery.autoSuggest" />
			<aef:javascript path="jquery.labelify" />
			<aef:javascript path="jquery.tagcloud" minify="always" />

			<aef:javascript path="utils/HelpUtils" />
			<aef:javascript path="utils/menuTimer" />
			<aef:javascript path="utils/quickSearch" />
			<aef:javascript path="utils/refLinkDisplay" />
			<aef:javascript path="utils/aef.jstree.plugin" />
			<aef:javascript path="utils/purl" />

			<aef:javascript path="dynamics/controller/PageController" />
			<aef:javascript path="dynamics/controller/MenuController" />
		</c:otherwise>
  </c:choose>

<c:if test="${settings != null}">
  <script type="text/javascript">
  $.ajaxSetup({
    traditional: true, //force jquery back to < 1.4 series style data serialization
    dataFilter: function(data, type) {
      if (data === "AGILEFANT_AUTHENTICATION_ERROR") {
      	window.location.reload(); 
      }
      return data;
    },
    error: function(xhr,status,error) {
      MessageDisplay.Error("An unknown error occurred",xhr);
    }
  }); 
  Configuration.setConfiguration({ timesheets: ${settings.hourReportingEnabled}, branchMetricsType: '${settings.branchMetricsType}', labelsInStoryList: ${settings.labelsInStoryList} });
  </script>
  </c:if>
  
  <%@include file="../../jsp/inc/includeDynamics.jsp" %>
  

  
  <script type="text/javascript">  
  var DelegateFactoryClass = function() {
    this.handlers = {};
    this.currentId = 1;
  };
  DelegateFactoryClass.prototype.create = function(callback) {
    var id = "handle_" + this.currentId++;
    this.handlers[id] = callback;
    return "DelegateFactory.handle('"+id+"'); return false;";
  };
  DelegateFactoryClass.prototype.handle = function(id) {
    this.handlers[id].apply(arguments);
  };
  var DelegateFactory = new DelegateFactoryClass();
  
  PageController.initialize(${currentUserJson});
  $(document).ready(function() {
    window.pageController.init();

    // Initialize the quick search
    var searchInput = $('#quickSearchInput');
    var searchBox = $('#quickSearchBox');
    var searchLink = $('#quickSearchLink');
    
    searchInput.agilefantQuickSearch({
      source: "ajax/search.action",
      minLength: 3,
      select: function(event, ui) {
        if (ui.item.originalObject['class'] !== 'noclass') {
          window.location.href = "searchResult.action?targetClassName=" + ui.item.originalObject['class'] + "&targetObjectId=" + ui.item.originalObject.id;
        }
      },
      close: function(event, ui) {
        console.log("Search input close", event, ui);
      }
    });

    searchInput.keydown(function(event) {
      if (event.keyCode === 27) {
        $(this).blur();
        return false;
      }
    });

    searchInput.blur(function(event) {
      if (searchBox.data('open')) {
        searchBox.data('open',false);
        searchBox.hide('blind',{},'fast');
        $(this).val('');
      }
      return false;
    });

    searchLink.click(function() {
      if (searchBox.is(':hidden')) {
        window.pageController.openMenu();
        searchBox.show('blind',{},'fast',function() {
          searchBox.data('open',true);
          searchInput.focus();
        });
      }
      return false;
    });

  });
  </script>
  
  <%-- HEAD-includes from other jsp files --%>
  <c:if test="${includeInHeader != null}">
    <jsp:invoke fragment="includeInHeader" />
  </c:if>
</head>

<body>

<div id="outerWrapper"><!-- Start of outer wrapper -->

<c:if test="${hideHeader != true}">
  <div id="headerWrapper">
    <c:choose>
    <c:when test="${headerContent != null}">
      <jsp:invoke fragment="headerContent" />
    </c:when>
    <c:otherwise>
      <struct:header />
    </c:otherwise>
    </c:choose>
    
    <c:if test="${hideLogout != true}">
      <struct:defaultRightHeader />
    </c:if>
  </div>
</c:if>

<div id="controlWrapper">
  <c:if test="${hideControl != true}">
    
    <div id="navigationTabsWrapper">
      <struct:mainTabs navi="${navi}" />
    </div>
      
  </c:if>
  <struct:createNewMenu />
</div>

<c:if test="${hidecreateAndSearchWrapper != true}">
  <div id="createAndSearchWrapper">
    <c:if test="${hideControl != true}">
    
      <div style="position: relative; left: 1em;">
        <a href="#" id="createNewMenuLink" onclick="return false;"><img src="static/img/add.png" alt="+" /><span style="font-size: 120%;">Create new</span></a> 
      </div>
      
      <div style="position: relative; left: 1em; top:0.5em;">
        <a id="quickSearchLink" href="#"><img src="static/img/search_small.png" alt="Search items" /><span id="quickSearchLinkText" style="font-size: 120%;">Search items</span></a>
      </div>
      
    </c:if>
  </div>
</c:if>

<div id="resizableMenuWrapper">
  <div id="menuWrapper">
    <c:if test="${hideMenu != true}">
      
      
      <div id="menuContent">
        <div id="quickSearchBox" class="ui-widget-header quickSearchBox">
          <div style="white-space: nowrap;">Search: <input id="quickSearchInput" size="10" type="search" class="ui-autocomplete-input" style="display: inline-block;" /></div>
        </div>
      
        <c:choose>
        <c:when test="${menuContent != null}">
          <jsp:invoke fragment="menuContent" />
        </c:when>
        <c:otherwise>
          <struct:backlogMenu navi="${navi}"/>
        </c:otherwise>
        </c:choose>
      </div>
    </c:if>
  </div>
</div>

  <c:if test="${hideMenu != true}">
    <div id="menuControlPanel"> 
      <div id="menuToggleControl"> </div>
    </div>
  </c:if>

<c:choose>
	<c:when test="${hideMenu != true}">
		<div id="bodyWrapper">
	</c:when>
	<c:otherwise>
		<div id="bodyWrapperNoMenu">
	</c:otherwise>
</c:choose>

  <jsp:doBody />
  
  <div id="layoutEmptyDiv"> </div>
</div>

<c:if test="${hideFooter != true}">
  <div id="footerWrapper">
    <struct:footer />
  </div>
</c:if>

<script type="text/javascript">
function setMenuWidths(width) {
    $("#resizableMenuWrapper").css("width", width);
    $("#resizableMenuWrapper").css("height", "auto");  // without this line: resizing width and then changing window height ==> wrapper height does not change
    $("#menuWrapper").css("width", width);
    $("#createAndSearchWrapper").css("width", width);
    $("#bodyWrapper").css("margin-left", width);
    $("#menuToggleControl").css("margin-left", width);
}
if($.cookie("agilefantMenuWidth")) {
	var newWidth = $.cookie("agilefantMenuWidth");
    setMenuWidths(newWidth)
}
$("#resizableMenuWrapper").resizable({
    handles: 'e',
    resize: function(event, ui) {
    	var newWidth = $(this).css("width");
        setMenuWidths(newWidth)
   },
    stop: function(event, ui) {
        var newWidth = $(this).css("width");
        setMenuWidths(newWidth)
        $.cookie("agilefantMenuWidth", newWidth);
   }
});
</script>

</div> <!-- End of outer wrapper -->
</body>

</html>
