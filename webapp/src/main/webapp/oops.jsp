<%@taglib uri="WEB-INF/tlds/aef_structure.tld" prefix="struct" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/struts-tags" prefix="ww" %>
<%@taglib uri="/WEB-INF/tlds/aef.tld" prefix="aef" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

<head>
  <title>Agilefant</title>
  <link rel="stylesheet" type="text/css" href="/global/static/css/main.css" />  
  <!--[if IE 7]><aef:css path="IE7styles.css" /><![endif]-->
  <!--[if IE 8]><aef:css path="IE8styles.css" /><![endif]-->
  
  <link rel="shortcut icon" href="/global/static/img/favicon.png" type="image/png" />

  <style type="text/css">  
    #outerWrapper {
      background: url('/global/static/img/login_gradient.png') repeat-x;
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

<div id="outerWrapper">

  <div id="loginWrapper" class="dynamictable ui-widget-content ui-corner-all ${errorAddedClass}">
    <div id="heading" class="ui-widget-header ui-corner-all dynamictable-caption dynamictable-caption-block">
      Oops!
    </div>
    <div id="login">
		The page you tried to load was not found. Please continue to <a href="/login.jsp">Login Page</a>.
    </div>
  </div>
  
  <div id="footerWrapper">
    <struct:footer />
  </div>
  
</div>
  
</body>

</html>

