<%@ include file="./inc/_taglibs.jsp" %>
<style type="text/css">
.errors {
	background-color:#FFCCCC;
	border:1px solid #CC0000;
	width:100%;
	margin-bottom:10px;
	margin-top: 35px;
	text-align: center;
}
.errors li{ 
	list-style: none; 
}
</style>
<div class="errors">
     <ww:actionerror/>
</div>