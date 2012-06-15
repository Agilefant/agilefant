<%@tag description="Agilefant main tabs" %>

<%@attribute name="navi" required="true" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript">
$(document).ready(function() {
  var selector = '#navitab-${navi}';
  $(selector).addClass('navitab-selected');
});
</script>

<ul>

<!-- Daily Work -->
<c:if test="${settings.dailyWork}">

<li id="navitab-dailyWork">
  <a href="dailyWork.action">
  <span>
  <img src="static/img/dailyWork.png" alt="Daily Work" />
  Daily Work
  </span>
  </a>
</li>
</c:if>

<!-- Backlogs -->
<li id="navitab-backlog">
  <a href="contextView.action?contextName=backlog">
  <span>
  <img src="static/img/backlog.png" alt="Backlogs" />
  Backlog
  </span>
  </a>
</li>


<%-- Timesheet --%>
<c:if test="${settings.hourReportingEnabled}">
<li id="navitab-timesheet">
  <a href="timesheet.action">
  <span>
  <img src="static/img/timesheets.png" alt="Timesheets" />
  Timesheets
  </span>
  </a>
</li>
</c:if>


<!-- Portlets -->
<c:if test="${currentUser.admin}">
<c:if test="${settings.devPortfolio}">
<li id="navitab-portfolio">
  <a href="contextView.action?contextName=portfolio">
  <span>
  <img src="static/img/portfolio.png" alt="Dev Portfolio" />
  Portfolio
  </span>
  </a>
</li>
</c:if>
</c:if>

<%-- Settings --%>
<c:if test="${currentUser.admin}">
<li id="navitab-settings">
    <a href="settings.action">
    <span>
    <img src="static/img/settings.png" alt="Administration" />
    Administration
    </span>
    </a>
</li>
</c:if>

</ul>