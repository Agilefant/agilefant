<%@tag description="Logout div" %>

<%@taglib uri="/WEB-INF/tlds/aef_structure.tld" prefix="struct" %>
<%@taglib uri="/WEB-INF/tlds/aef.tld" prefix="aef" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/struts-tags" prefix="ww" %>

<div id="logoutDiv">
  <a href="editUser.action">${currentUser.fullName}</a>
  <c:if test="${currentUser.admin}">(Administrator)</c:if>
  |
  <a href="helpWithoutFrames.action" target="_blank">Help</a>
  |
  <a target="_blank" href="http://tinyurl.com/agilefant-registration-2013">Register</a>
  |
  <a href="j_spring_security_logout?exit=Logout">Logout</a>
  
</div>
<div id="updateMessage"></div>
<div id="cloudMessage"></div>

<script type="text/javascript">
  var cloudMessageCookie = jQuery.cookie("cloudmessage");
  
  var appendCloudMessage = function(cloudMessage) {
    if (cloudMessage == null) {
      cloudMessage = 'Agilefant is now available as a hosted cloud version!<br><a target="_blank" href="http://agilefant.com/pricing/?utm_campaign=osVersion&utm_source=topBar">Click here for more information</a>';
      jQuery.cookie("cloudmessage", cloudMessage, { expires: 1 });
    }
    var cloudMessageDiv = jQuery("#cloudMessage");
    cloudMessageDiv.append('<img id="cloudImage" src="static/img/cloud.png"></img>');
    cloudMessageDiv.append('<div id="cloudMessageBox">' + cloudMessage + '</div>');
    cloudMessageDiv.append('<div style="float:right">&nbsp|</div>');
    var cloudMessageBoxDiv = jQuery("#cloudMessageBox");
    cloudMessageDiv.mouseover(function() {
      if(cloudMessageBoxDiv.is(':animated')) {
        cloudMessageBoxDiv.stop().animate({opacity:'100'});
      }
      cloudMessageBoxDiv.show();
    }).mouseout(function() {
      cloudMessageBoxDiv.hide();
    });
    if (!cloudMessageCookie) {
      cloudMessageBoxDiv.show().delay(5000).fadeOut(2000);
    }
  };

  var myVersion = "${aef:version()}";
  var latestVersionCookie = jQuery.cookie("latestversion");
  var updateMessageCookie = jQuery.cookie("updatemessage");

  var compareVersions = function(latestVersion, updateMessage) {
    if (latestVersion != "CONNECTIONFAILURE" && myVersion != latestVersion && isMyVersionOld(myVersion, latestVersion)) {
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
    appendCloudMessage(cloudMessageCookie);
  }
  
  var callback = function(data) {
    var latestVersion = data.version;
    var updateMessage = data.message;
    var cloudMessage = data.cloudmessage;
    jQuery.cookie("latestversion", latestVersion, { expires: 1 });
    jQuery.cookie("updatemessage", updateMessage, { expires: 1 });
    jQuery.cookie("cloudmessage", cloudMessage, { expires: 1 });
    compareVersions(latestVersion, updateMessage);
    appendCloudMessage(cloudMessage);
  };
  
  function isMyVersionOld(myVersion, latestVersion) {
    var latestVersionParts = latestVersion.split('.');
    var myVersionParts = myVersion.split('.');
    
    for (var i = 0; i < latestVersionParts.length; ++i) {
      if (myVersionParts.length == i) {
        return true;
      }
        
      if (latestVersionParts[i] == myVersionParts[i]) {
        continue;
      }
      else if (latestVersionParts[i] > myVersionParts[i]) {
        return true;
      }
      else {
        return false;
      }
    }
    
    if (latestVersionParts.length != myVersionParts.length) {
      return false;
    }
    
    return false;
  }

  var secondsBeforeExpire = ${pageContext.session.maxInactiveInterval};
  if (secondsBeforeExpire < 1) {
    secondsBeforeExpire = 3600;
  }
  var pollingInterval = 1000 * secondsBeforeExpire / 4;
  // Send http request every once per hour to keep the session active, in case the user is not doing anything.
  setInterval(function(){
    jQuery.ajax({
      type: "GET",
      url: "static/sessionkeepalive.json",
      async: true,
      cache: false,
      data: {},
      dataType: "json",
      timeout: 30000,
      success: function(data, status) {
      },
      error: function(xhr, status, error) {
        // Reload the page if the user was disconnected from server.
        if (status == "timeout") {
          alert("It seems that your Internet connection was disconnected. Click ok to reload the page.");
          location.reload();
        }
      }
    });
   }, pollingInterval);
</script>
