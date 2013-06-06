<%@tag description="Logout div" %>

<%@taglib uri="../../tlds/aef_structure.tld" prefix="struct" %>
<%@taglib uri="../../tlds/aef.tld" prefix="aef" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/struts-tags" prefix="ww" %>

<div id="logoutDiv">
  <a href="#" id="createNewMenuLink" onclick="return false;">Create new</a> 
  |
  <a href="editUser.action">${currentUser.fullName}</a>
  <c:if test="${currentUser.admin}">(Administrator)</c:if>
  |
  <a href="help.action">Help</a>
  |
  <a target="_blank" href="http://tinyurl.com/agilefant-registration-2013">Register</a>
  |
  <a href="j_spring_security_logout?exit=Logout">Logout</a>
  
  <struct:createNewMenu />
</div>
<div id="updateMessage"></div>

<script type="text/javascript">
  var myVersion = "${aef:version()}";
  var latestVersionCookie = jQuery.cookie("latestversion");
  var updateMessageCookie = jQuery.cookie("updatemessage");

  var compareVersions = function(latestVersion, updateMessage) {
    if (myVersion != latestVersion && myVersion != "CONNECTIONFAILURE") {
      jQuery("#updateMessage").append('<img id="updateImage" src="static/img/star_red.png"></img>');
      jQuery("#updateMessage").append('<div id="updateMessageBox">' + updateMessage + '</div>');
      jQuery("#updateMessage").append('<div style="float:right">&nbsp|</div>');
      jQuery("#updateMessage").mouseover(function() {
        jQuery(this).children("#updateMessageBox").show();
      }).mouseout(function() {
         jQuery(this).children("#updateMessageBox").hide();
      });
    }
  };  
  
  if (latestVersionCookie == null) {
    jQuery.cookie("latestversion", "CONNECTIONFAILURE", { expires: 1 });
    jQuery.ajax({
      url: "https://cloud.agilefant.org/versioncheck/versioncheck.json?version=" + myVersion,
      dataType: "jsonp",
      jsonpCallback: "callback",
      error: function() { }
    });
  } else {
    compareVersions(latestVersionCookie, updateMessageCookie);
  }
  
  var callback = function(data) {
    var latestVersion = data.version;
    var updateMessage = data.message;
    jQuery.cookie("latestversion", latestVersion, { expires: 1 });
    jQuery.cookie("updatemessage", updateMessage, { expires: 1 });
    compareVersions(latestVersion, updateMessage);
  };
</script>
