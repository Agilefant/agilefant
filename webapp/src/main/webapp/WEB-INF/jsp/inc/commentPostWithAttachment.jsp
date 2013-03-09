<%@ include file="./_taglibs.jsp"%>
<script type="text/javascript" src="../static/js/jquery.js?<ww:text name="struts.agilefantReleaseId" />"></script>
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<script type="text/javascript">
<!--
var parentId = getParentObjectId()+'_';
var index=0;
$(document).ready(function() {

	$(this).find("*").each(function(){
		var elementClass = $(this).attr('class');
		if (elementClass != null & elementClass != '' & elementClass.length != 0){
			if(elementClass =='file-attachment-class'){
				$(this).addClass(parentId+elementClass+index);
				$(this).attr("id",parentId+elementClass+index);
				var fileUpload = $(this);
				if ($.browser.msie)
				{
					var labelForFileSelect = $('<label />');
					var labelButton = $('<input type="button" value="Add Attachments" /> ');
					labelButton.appendTo(labelForFileSelect);
					labelForFileSelect.attr("for",parentId+elementClass+index);
					labelForFileSelect.addClass(parentId+elementClass+index+"_lbl");
					
					$('.'+parentId+elementClass+index).click(function(){
						setTimeout(function()
				        { 
							fixMultipartContent(fileUpload);
				        }, 0);

					});
					labelForFileSelect.addClass("custlabel");				
					labelForFileSelect.click(function(){
						$('.'+parentId+elementClass+index).show().focus().click();
						$(this).hide();
					});
					labelForFileSelect.appendTo($('.'+parentId+'file-attachment-lbl'));
				}
				index++;
			}else{
				$(this).addClass(parentId+elementClass);
			}
		}
	});
	
	var formObject =  $('.'+parentId+'form-post');
	formObject.attr("id",parentId+'form-post');
	var actionMapping = $('.'+parentId+'actionMapping');
	if(actionMapping.val() === ''){
		parent.document.getElementById(window.name).src="";
	}
	var iframeView = parent.document.getElementById(window.name);
	/* For some hack on IE */
	/* var uploaderDiv = $('.'+parentId+'file_attachment');
	$(uploaderDiv).find("input[type='file']").each(function(){
		
	}); */
	$('.'+parentId+'commentEnterd').focus();
	
});

function resizeIframe(obj)
{
  obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';
  obj.style.width = obj.contentWindow.document.body.scrollWidth + 'px';                        
}

function getParentObjectId() {
	return window.frameElement.getAttribute("id");	
}

function postNewComment(action){
	
	var comment = $('.'+parentId+'commentEnterd').val();
    if(!comment){
            $('.'+parentId+'comment-error').show();
            $('.'+parentId+'comment-error').html('<font color=red>Please enter a comment before submitting</font>');
            return false;
    }else{
            $('.'+parentId+'comment-error').hide();
    }
    var formObject = $('#'+parentId+'form-post');
    formObject.attr("action",action+'.action');
    
    $('.'+parentId+'comment-error').html('<font color=blue>Please wait.......</font>').show();
    
    var obj = formObject.submit();
    resizeIframe(parent.document.getElementById(window.name));
        
   	//cancelCommentPosting(false);
    return false;
}

function openAttachmentBoxOrWindow(){
	
	var uploaderDiv = $('.'+parentId+'file_attachment');
	var fileUploaderControl;
	// fileUploadLabelToCall variable only used for IE browser.
	var fileUploadLabelToCall;
	var fileUploadChildName;
	$(uploaderDiv).find("input[type='file']").each(function(){
		var element = $(this);
		var elementClass = $(this).attr('class');
		if(elementClass.indexOf("file-attachment-class") !== -1 && elementClass.indexOf("lbl_file-attachment-class") == -1 && element.val() === '') {
			console.log("Object Class : "+elementClass);
			fileUploaderControl = $(this);
			console.log("Object : "+fileUploaderControl);
			var keyToGetLabel = $(this).attr("id")+"_lbl";
			fileUploadLabelToCall = $('.'+keyToGetLabel);
			console.log("Label to call (IE only ) : "+fileUploadLabelToCall);
			return false;
		}		
	});

	if(!fileUploaderControl ){
		$('.'+parentId+'comment-error').html('<font color=red>You have seleted maxmimum number of attachment for comment</font>');
		$('.'+parentId+'comment-error').show();
		resizeIframe(parent.document.getElementById(window.name));
		return false
	}
	
	fileUploaderControl.click(function(){		
	if ($.browser.msie)
	{
		
		setTimeout(function()
        { 
			fixMultipartContent(fileUploaderControl);
        }, 0);

	}else{
		fileUploaderControl.change(function(){
			fixMultipartContent(fileUploaderControl);		
		});	
	}
	});
	if ($.browser.msie)
	{
		fileUploadLabelToCall.click();
	}else{
		console.log("Clicked to controller : "+fileUploaderControl);
		fileUploaderControl.show().focus().click();
	}
	resizeIframe(parent.document.getElementById(window.name));
	return false;
}

function fixMultipartContent(element) {
	var i = 0;
	console.log("Enterd in fix multipart count : "+i);
	fileUploaderControl = element;
	
	var fileListChild = $('<div style="line-height:17px" />');
	
	var selectedFileName = $('<b style ="font-size: 14px; font-family: Arial,Helvetica,sans-serif;"> '+fileUploaderControl.val().replace("C:\\fakepath\\","")+ ' &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>');
	
	var selectedFileRemoveLabel = $('<a href="#" style="font-size: 13px; font-family: Arial,Helvetica,sans-serif; line-height: 100%;" >Remove </a>');
	
	selectedFileRemoveLabel.attr("class",fileUploaderControl.attr("class")+'-remove');
	
	selectedFileName.appendTo(fileListChild);
	
	selectedFileRemoveLabel.appendTo(fileListChild);	
	
	fileListChild.appendTo($('.'+parentId+'selectedFiles'));
	
	$('<br />').appendTo(selectedFileRemoveLabel);
	//fileUploaderControl.hide();
	
	selectedFileRemoveLabel.click(function(){
		console.log("Remove click triggerd for : "+selectedFileRemoveLabel.attr('class'));
		//fileUploaderControl.replaceWith( fileUploaderControl.val('').clone( false ) );
		if ($.browser.msie)
		{
			fileUploaderControl.val('');
			//fileUploaderControl.replaceWith( fileUploaderControl.val('').clone( false ) );
			$('.'+fileUploaderControl.attr("class").split(' ')[1]+'_lbl').show();
		}else{
			var controlName = selectedFileRemoveLabel.attr('class').split(' ')[1].replace('-remove','');
			fileUploaderControl = $('#'+controlName);
			fileUploaderControl.replaceWith( fileUploaderControl.val('').clone( false ) );
		}
		selectedFileRemoveLabel.remove();
		fileListChild.remove();
		var iframeView = parent.document.getElementById(window.name);
		resizeIframe(iframeView);
	});	
	var iframeView = parent.document.getElementById(window.name);
	resizeIframe(iframeView);
}


function checkIsFilesSelected(){
	var filesSelected = false;
	var uploaderDiv = $('.'+parentId+'file_attachment');
	$(uploaderDiv).find("input:file").each(function(){
		if($(this).val() !== ''){;
			filesSelected = true;
		}
	});	
	return filesSelected;
}

function cancelCommentPosting(doCheck){
	if(doCheck){
		var isFilesSelected = checkIsFilesSelected();
		if(isFilesSelected){
			var confirmCancel = confirm("You have selected some files, Are you sure want to cancel ? ");
			if(!confirmCancel){
				return false;
			}
		}
	}
	resizeIframe(parent.document.getElementById(window.name));
	parent.document.getElementById(window.name).src="";
	parent.document.getElementById(window.name).style.display="none";	
}

//-->
</script>


<div >
<style>
	html {
		overflow:hidden;
		background-color:#E9EEFF\9;
		*background-color:#E9EEFF;
		border:0 solid #E9EEFF;
	}
	body {
		border:0 solid #E9EEFF;
	}
	.post_cancel_div {
    margin-right: 2%;
    padding: 7px 0;
    text-align: right;
    float:right;
}
.clrs {
	clear:both;
}
.file_attachment{
				float:left;
				padding-top:10px;
				padding-left: 27px;
	}
	.file-attachment-lbl {
		position:relative;
	}
	.custlabel {
		position:absolute;
		left:0;
		top:0;
		height:20px;
	}
</style>

<ww:form action="doFileUpload" cssClass="form-post" method="POST"  enctype="multipart/form-data">
   <ww:hidden name="maxFIles"></ww:hidden>
   <ww:hidden cssClass="hidden-object-Id" name="objectId"></ww:hidden>
    <ww:textarea name="commentEnterd" cssClass="commentEnterd" style="height: 100px; margin-left: 2%; width:96%;" />     			
	<div class="comment-error" style=" margin: 0 auto; width: 96%;" >	</div>	
	<input type="hidden" class="actionMapping" value='${actionMapping}' />
	<div class="post_cancel_div">	
		<input class="customrefresh_span" onclick="return postNewComment('${actionMapping}')" type="submit" role="button" aria-disabled="false" value="Post" /><!-- <span class="ui-button-text">Post</span> </button> -->
		<button class ="customrefresh_span" onclick="return cancelCommentPosting(this)"  type="button" role="button" aria-disabled="false"><span class="ui-button-text">Cancel </span> </button>		
	</div>
	<div class="file_attachment">
		<div class="file-attachment-lbl">
			<input type="button" class="addAttachment-class" onclick='return openAttachmentBoxOrWindow();' value="Add attachment" />
		</div>
		<div class="selectedFiles">
		
		</div>	
	<c:forEach var = "i" begin="1" end="${maxFIles}" step="1" varStatus="status" >
	<!-- style="display: none; opacity: 0.2" -->
		<ww:file label="File" cssClass="file-attachment-class" style="display: none; opacity: 0.0" name="attachments"  />
		
	</c:forEach>
	</div>
	<div class="clrs"></div>
	
	<ww:submit cssClass="submit-comment" style="display: none;" />
</ww:form>

<div class="clrs"></div>
</div>