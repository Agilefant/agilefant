<%@ include file="./_taglibs.jsp"%>
<script type="text/javascript" src="../static/js/jquery.js?<ww:text name="struts.agilefantReleaseId" />"></script>
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<script type="text/javascript">
/*
 * First off am not a javascript expert , so please ignore my coding standards and some wired logics in javascript
 *
 * Main aim of this javascript ( am sure that its not a perfect one) and jsp code is to avoid using of some other third party fileupload plugins. 
 * Working of this javascript is simple , everyone can understand easily, added comments for each function.
 */
<!--
//  Parent ID is the id of this page (iframe)
var parentId = window.frameElement.getAttribute("id")+'_';
var index=0;
/* Setting a unique class name (appending parentId to the existing classname) for each elements ( Only for the elements which have a class name) ,
* 	for some kind of manipulation required later
* */
$(document).ready(function() {
	var iframeView = parent.document.getElementById(window.name);
	
	$(this).find("*").each(function(){
		var elementClass = $(this).attr('class');
		if (elementClass != null & elementClass != '' & elementClass.length != 0){
			/*
			* we are specified number of files can be attached to a comment ( This can be configured via administration panel ), 
			* so we have to make the <ww:form /> element with a index value for easily accessing it.   
			*/
			if(elementClass =='file-attachment-class'){
				$(this).addClass(parentId+elementClass+index);
				$(this).attr("id",parentId+elementClass+index);
				var fileUpload = $(this);
				/*
				* Internet explorer is really a problem while playing with <input type='file' />
				* IE dosen't submit a form if the  <input type='file' /> is clicked by programatically
				* So the work around I found is to create a <label /> for each  <input type='file' />, and then style the label. 
				* So the users will click on the label and this will activate the <input type='file' /> element. (Do you have any better idea for doing this)
				*/
				if ($.browser.msie)
				{
					/*
					* Creating a label and style it like a button  
					*/
					var labelForFileSelect = $('<label />');
					var labelButton = $('<input type="button" value="Add Attachments" /> ');
					labelButton.appendTo(labelForFileSelect);
					labelForFileSelect.attr("for",parentId+elementClass+index);
					// for accessing label we have to put a unique identifier so appedning '_lbl' with parentId.  :) 
					labelForFileSelect.addClass(parentId+elementClass+index+"_lbl");
					// another problem for IE is change event , 
					// IE browser dosen't support change event on <input type='file' /> (I don't know that anyother elements supports this).
					// So setting a timeout to track the changes on the click function of <input type='file' />. 
					$('.'+parentId+elementClass+index).click(function(){
						setTimeout(function()
				        { 
							// go to the function definition for finding about this :). 
							fixMultipartContent(fileUpload);
				        }, 0);

					});
					// style for making label to button look and feel
					labelForFileSelect.addClass("custlabel");
					// hidding the <input type='file' /> 
					labelForFileSelect.click(function(){
						// on the click event of label we have to make the <input type='file' /> visibility to true, 
						// otherwise some browsers cause some problems ( browsers making me to do things in bad way). 
						$('.'+parentId+elementClass+index).show().focus().click();
						$(this).hide();
					});
					// The created new labels need to append to the label section 
					labelForFileSelect.appendTo($('.'+parentId+'file-attachment-lbl'));
				}
				index++;
			}else{
				$(this).addClass(parentId+elementClass);
			}
		}
	});
	/*
	* Both story and comments uses the same iframe but the form actions are different, below code is for manipulating struts for actions
	*/
	var formObject =  $('.'+parentId+'form-post');
	formObject.attr("id",parentId+'form-post');
	var actionMapping = $('.'+parentId+'actionMapping');
	if(actionMapping.val() === ''){
		parent.document.getElementById(window.name).src="";
	}
	// once page is ready we are focusing to textarea. 
	$('.'+parentId+'commentEnterd').focus();
	
});
/*
 * function resizeIframe used to rescale depends on the files selected. 
 *
 */
function resizeIframe(obj)
{
	// code is simple 
  obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';
  obj.style.width = obj.contentWindow.document.body.scrollWidth + 'px';                        
}
/*
 *  function for posting comments 
 */
function postNewComment(action){
	// Getting the comment enterd by the user
	// some of the code were simple dosen't need a commenting 
	var comment = $('.'+parentId+'commentEnterd').val();
    if(!comment){
            $('.'+parentId+'comment-error').show();
            $('.'+parentId+'comment-error').html('<font color=red>Please enter a comment before submitting</font>');
            return false;
    }else{
            $('.'+parentId+'comment-error').hide();
    }
    comment = comment.replace(new RegExp('\n','g'), '<br />');
    var formObject = $('#'+parentId+'form-post');
    // The value for action varible is setted already in pageload event.
    formObject.attr("action",action+'.action');    
    $('.'+parentId+'comment-error').html('<font color=blue>Please wait.......</font>').show();
    // Submiting form. 
    var obj = formObject.submit();
    // resize for iframe 
    resizeIframe(parent.document.getElementById(window.name));
    return false;
}
// functionality of this method is to manipulate the attachmentwindow and fileselection , 
function openAttachmentBoxOrWindow(){
	
	var uploaderDiv = $('.'+parentId+'file_attachment');
	var fileUploaderControl;
	// fileUploadLabelToCall variable only used for IE browser.
	var fileUploadLabelToCall;
	var fileUploadChildName;
	/*
	* In this method main task is we need to find a <input type='file' /> element which is not already used
	* so first we need to get all the  <input type='file' /> elements.
	* 
	*/
	$(uploaderDiv).find("input[type='file']").each(function(){
		var element = $(this);
		var elementClass = $(this).attr('class');
		if(elementClass.indexOf("file-attachment-class") !== -1 && elementClass.indexOf("lbl_file-attachment-class") == -1 && element.val() === '') {
			fileUploaderControl = $(this);
			var keyToGetLabel = $(this).attr("id")+"_lbl";
			fileUploadLabelToCall = $('.'+keyToGetLabel);
			return false;
		}		
	});

	if(!fileUploaderControl ){
		$('.'+parentId+'comment-error').html('<font color=red>You have seleted maxmimum number of attachment for comment</font>');
		$('.'+parentId+'comment-error').show();
		resizeIframe(parent.document.getElementById(window.name));
		return false
	}
	/*
	* Below code are self-explanary. 
	*/
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
/*
 * The functionality of this method is , after a user selecting a file as attachment.
 * 1 . we need to show the filename below to the text area.
 * 2 . we have to give the option to the user remove the selected file 
 * 3 . Fix the filename value
 * 4 . Once a file removed we have to make that <input type='file' />
 * 		********************************************************
 * The main problem with this section is playing with  <input type='file'> , 
 * because most of the browser limit the operations on this control due security reason.
 */
function fixMultipartContent(element) {
	
	fileUploaderControl = element;
	// creating simple div for putting the selected <input type='file' /> 
	var fileListChild = $('<div style="line-height:17px" />');
	// variable for filename , some browsers send file name with fakepath so correcting the filename 
	var selectedFileName = $('<b style ="font-size: 14px; font-family: Arial,Helvetica,sans-serif;"> '+fileUploaderControl.val().replace("C:\\fakepath\\","")+ ' &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>');
	// creating a remove label for selected file. 
	var selectedFileRemoveLabel = $('<a href="#" style="font-size: 13px; font-family: Arial,Helvetica,sans-serif; line-height: 100%;" >Remove </a>');	
	selectedFileRemoveLabel.attr("class",fileUploaderControl.attr("class")+'-remove');
	//appending the filename to div that holds the attachment
	selectedFileName.appendTo(fileListChild);
	// appending the remove label to the div that holds attachmet. 
	selectedFileRemoveLabel.appendTo(fileListChild);
	// appending new filelist to the group
	fileListChild.appendTo($('.'+parentId+'selectedFiles'));	
	$('<br />').appendTo(selectedFileRemoveLabel);
	// creating a event for remove link , for clearing the selected file and making the element reusable 
	selectedFileRemoveLabel.click(function(){
		// need to do some browser specific stuff. 
		if ($.browser.msie)
		{
			// clear the exsiting value of the file 
			fileUploaderControl.val('');
			// Once <input type='file' /> is cleared we need to show its corresponding Label for the only we can reuse it. 
			$('.'+fileUploaderControl.attr("class").split(' ')[1]+'_lbl').show();
		}else{
			// Getting <input type='file' /> element from its class of its remove label. 
			var controlName = selectedFileRemoveLabel.attr('class').split(' ')[1].replace('-remove','');
			fileUploaderControl = $('#'+controlName);
			// replacing the element 
			fileUploaderControl.replaceWith( fileUploaderControl.val('').clone( false ) );
		}
		// removing the labels  
		selectedFileRemoveLabel.remove();
		// removing the list child. 
		fileListChild.remove();
		// need to recalulate iframe width and height 
		resizeIframe(parent.document.getElementById(window.name));
	});	
	// need to recalulate iframe width and height 
	resizeIframe(parent.document.getElementById(window.name));
}

// functionality of this method is to check that is any files selected while he click the cancel button. 
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
// cancel comment posting. 
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
	// reseting iframe source will reload the page. That's what we need 
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