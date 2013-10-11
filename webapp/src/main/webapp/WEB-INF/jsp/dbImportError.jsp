<%@ include file="./inc/_taglibs.jsp"%>

<struct:htmlWrapper navi="">

<h2>An error occurred</h2>
<p>The database import failed.
<br/><a
	href="javascript:history.back(-1)">Try again</a>
<p><ww:actionerror /> <ww:fielderror /> <ww:actionmessage /> 

<p>Error:</p>
<p>${errorStacktrace}</p>

</struct:htmlWrapper>
