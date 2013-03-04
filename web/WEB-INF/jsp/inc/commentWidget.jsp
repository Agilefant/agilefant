<% pageContext.setAttribute("newLineChar", "\n"); %>
<%@ include file="./_taglibs.jsp"%>

<script type="text/javascript">

$(document).ready( function() {
	var timeSpanCollection = $('.timeSpan');
	jQuery.each(timeSpanCollection ,function(){
		var time = $(this).text();		
		if(time.indexOf('GMT')!==-1)
		{
			$(this).html(new Date(time).toDateString() +' <br /> at '+new Date(time).toTimeString().replace(/GMT.*/g,""));
		}
		});
});
</script>

</script>

<c:if test="${fn:length(comments) gt 0}">
	<c:if test="${type =='story'}">
		<div style="display: none;" class ="comment_head_story_${comments[0].stories.id}">${comments[0].comment}</div>
	</c:if>
	<c:if test="${type =='task'}">
		<div style="display: none;" class ="comment_head_task_${comments[0].tasks.id}">${comments[0].comment}</div>
	</c:if>
</c:if>

<table class="table_comment_custom " cellpadding="0" cellspacing="0" width="100%">
<c:forEach items="${comments}" var="comment">
<tr >
	<td  class="pc_leftblock" >
		<a class="pc_username pcsnd" href="#">${comment.users.fullName}</a>		
		<span class="timeSpan">${comment.parsedTime}</span>		
	</td>
	
	
	<td class="pc_rightblock">
		<table>
			<tr>
				<td  style="border-bottom: none;">
					<p>${fn:replace(comment.comment, newLineChar, "<br /> ")}</p>
				</td>
			</tr>
			<tr>
				<td  style="border-bottom: none;">
					<c:if test="${type =='story' && !empty comment.attachments  }">
						<div class="" ></div><b > Attachments </b> 
						<div>
							<c:forEach items="${comment.attachments}" var="attachment">
								<a href="ajax/downloadFile.action?fileId=${attachment.id}" >${attachment.originalFileName}</a> <br />
							</c:forEach>
						</div>
						</c:if>
					<c:if test="${type =='task' && !empty comment.attachments  }">
					<b><u>Attachments</u> </b> 
						<div >
							<c:forEach items="${comment.attachments}" var="attachment">
							<br />
								<a href="ajax/downloadFile.action?fileId=${attachment.id}" >${attachment.originalFileName}</a>
							</c:forEach>
						</div>
					</c:if>	
						
									
				</td>
			</tr>
		</table>
	</td>
</tr>
</c:forEach>
</table>