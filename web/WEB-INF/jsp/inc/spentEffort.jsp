<%@ include file="./_taglibs.jsp"%>
<table style="width: 100%;" class="listTable">
	<tr>
		<th rowspan="2" class="spentEffortArrow"><a title="Previous week" 
			href="weeklySpentEffort.action?userId=${userId}&amp;userTimeZone=${userTimeZone}&amp;week=${prevWeek.weekOfWeekyear}&amp;year=${prevWeek.weekyear} ">&laquo;</a></th>
		<c:forEach items="${dailyEffort}" var="dayEffort">
			<th style="min-width: 8ex;">${dayEffort.date}.${dayEffort.month}.</th>
		</c:forEach>
		<th style="min-width: 10ex;">Total</th>
		<th rowspan="2" class="spentEffortArrow"><a title="Next week" 
			href="weeklySpentEffort.action?userId=${userId}&userTimeZone=${userTimeZone}&week=${nextWeek.weekOfWeekyear}&year=${nextWeek.weekyear}">&raquo;</a></th>
			<td>
			 <select>
			   <c:forEach items="${weeks}" var="curWeek">
			     <c:choose>
			       <c:when test="${curWeek.weekOfWeekyear == week}">
			         <option selected="selected" value="${curWeek.weekyear}-${curWeek.weekOfWeekyear}">Week ${curWeek.weekOfWeekyear} (${curWeek.dayOfMonth}.${curWeek.monthOfYear}.)</option>
			       </c:when>
			       <c:otherwise>
			         <option value="${curWeek.weekyear}-${curWeek.weekOfWeekyear}">Week ${curWeek.weekOfWeekyear} (${curWeek.dayOfMonth}.${curWeek.monthOfYear}.)</option>
			       </c:otherwise>
			     </c:choose>
			   </c:forEach>
			 </select>
			</td>
	</tr>
	<tr>
		<c:forEach items="${dailyEffort}" var="dayEffort">
			<td>
				<c:choose>
				  <c:when test="${dayEffort.spentEffort == null}">
				    &mdash;
				  </c:when>
				  <c:otherwise>
				    <a class="detailLink" href="ajax/hourEntriesByUserAndDay.action?userId=${userId}&amp;day=${dayEffort.dayOfYear}&amp;year=${year}">
              ${aef:minutesToString(dayEffort.spentEffort)}</a>
				  </c:otherwise>
				</c:choose>
			</td>
		</c:forEach>
    <td>${aef:minutesToString(weekEffort)}</td>
		<td><a href="weeklySpentEffort.action?userId=${userId}&userTimeZone=${userTimeZone}&week=${currentWeek}&year=${currentYear}">Current week</a></td>
	</tr>
</table>
