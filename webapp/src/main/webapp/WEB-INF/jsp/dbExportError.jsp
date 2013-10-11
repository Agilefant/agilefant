<%@ include file="./inc/_taglibs.jsp"%>

<struct:htmlWrapper navi="">

<h2>An error occurred</h2>
<p>The database export connection could not be established. Most likely
your Agilefant installation's database connection is configured
differently from what is described in the installation guide. Please
contact the person who is responsible for your Agilefant
installation.

<p>Error:</p>
<p>${errorStacktrace}</p>

<br/><a
	href="javascript:history.back(-1)">Try again</a>
<p><ww:actionerror /> <ww:fielderror /> <ww:actionmessage /> 

</struct:htmlWrapper>