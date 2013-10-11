<%@taglib uri="WEB-INF/tlds/aef_structure.tld" prefix="struct" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/struts-tags" prefix="ww" %>
<%@taglib uri="/WEB-INF/tlds/aef.tld" prefix="aef" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

<head>
  <title>Agilefant</title>
  <aef:css path="main" minify="true" />
  <!--[if IE 7]><aef:css path="IE7styles.css" /><![endif]-->
  <!--[if IE 8]><aef:css path="IE8styles.css" /><![endif]-->
  
  <link rel="shortcut icon" href="static/img/favicon.png" type="image/png" />

  <aef:javascript path="jquery" minify="true" />

  <style type="text/css">  
    #outerWrapper {
      background: url('static/img/login_gradient.png') repeat-x;
      height: 300px;
    }
  
    #loginWrapper {
      width: 28em;
      height: 15em;
      margin: -8em 0 0 -12em;
      position: absolute;
      top: 35%;
      left: 50%;
      border-width: 3px;
      -moz-border-radius: 10px;
      -webkit-border-radius: 10px;
      border-radius: 10px;
      z-index: 100;
      overflow: visible;      
    }
    
    .loginWrapperWithError {
      height: 16em !important;
    }
    
    #login {
      font-family: Verdana, Arial, Helvetica, sans-serif;
      margin: 1em auto;
      width: 25em;
    }
    
    #loginWrapper tr {
      line-height: 2em;
    }
    
    #disclaimer {
      position: relative;
      top: 10em;
      left: 50%;
      z-index: 101;
      
      width: 22em;
      height: 8em;
      margin: -4em 0 0 -12em;
      padding: 0 1em;
      
      border-width: 3px;
      -moz-border-radius: 10px;
      -webkit-border-radius: 10px;
      border-radius: 10px;
      
      font-size: 80%;
      color: #f00;
    }
    
    #disclaimer a {
      color: rgb(30, 94, 238) !important;
    }
    
    #disclaimer a:hover {
      color:  #e50 !important;
    }
    
    #agilefantText {
      margin: 0;
      position: absolute;
      bottom: 0;
      z-index: 50;
      font-size: 72pt;
      font-style: italic;
      font-weight: bold;
    }
    
    #agilefantText img {
      display: block;
      float: left;
    }
    
    #agilefantText span {
      display: block;
      margin: 0.5em 0 0 150px;
    }
    
    #footerWrapper {
      position: fixed;
      bottom: 0;
      width: 100%;
      padding: 0 0 1em 0;
      background-color: white;
      z-index: 200;
    }
    
    #footerText {
      margin: 1em 0 0 1em;
      white-space: nowrap;
    }

  </style>

</head>

<body>

<c:if test="${param.error == 1}">
  <c:set var="errorAddedClass" value="loginWrapperWithError" />
</c:if>

<script type="text/javascript">
window.onload = function() {
  document.getElementById("username").focus();
}
</script>

<div id="outerWrapper">

  <div id="loginWrapper" class="dynamictable ui-widget-content ui-corner-all ${errorAddedClass}">
    <div id="heading" class="ui-widget-header ui-corner-all dynamictable-caption dynamictable-caption-block">
      Agilefant login
    </div>


    <div id="messageContainer">
      <c:choose>
      <c:when test="${param.error == 1}">
        <p style="color: #f00; margin-bottom:20px">Invalid username or password, please try again.</p>
      </c:when>
      <c:when test="${param.error == 2}">
        <p style="color: #f00; margin-bottom:20px">Session timed out. Please log in.</p>
      </c:when>
      </c:choose>
    </div>

    <div id="login">
      <form method="post" action="j_spring_security_check">
        <table border="0">
        <tr>
          <td rowspan="4" style="padding: 0 1em 0 0;"><img src="static/img/agilefant-logo-94px.png" width="94" /></td>
          <td>Username</td><td><input id="username" name="j_username" type="text" style="width: 150px;"/></td>
        </tr>
        <tr>
          <td>Password</td><td><input name="j_password" type="password" style="width: 150px ;"/></td>
        </tr>
        <tr>
          <td>&nbsp;</td><td>Remember me <input type="checkbox" name="_spring_security_remember_me" /></td>
        </tr>
        <tr>
          <td>&nbsp;</td><td><input value="Log in" type="submit" /></td>
        </tr>
        </table>                
      </form>
    </div>
<!--     
    <div id="disclaimer" class="ui-widget-content ui-corner-all">
      Please report found bugs at <a href="http://www.agilefant.org/mantis/">http://www.agilefant.org/mantis/</a></p>
    </div>
-->    
  </div>
  
  <div id="footerWrapper">
    <struct:footer />
  </div>
  
</div>
  
</body>

</html>

