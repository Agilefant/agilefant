var CommentWidget = function CommentWidget(model, onClose) {
  this.model = model;
  this.onClose = onClose;
  this.initDialog();
  this.renderEditor();
};

CommentWidget.prototype.initDialog = function() {
  var me = this;
  this.element = $('<div/>');
  this.commentEditorEl = $('<div />').appendTo(this.element);
  $('<div class="clear" />').appendTo(this.element);
  $('<h3>Comments</h3>').appendTo(this.element);
  this.commentEl = $('<div />').addClass("ui-widget-content").addClass("ui-corner-all").addClass("custom_style_add01")
    .css({"padding": "10px 37px", "margin-bottom": "2em"}).appendTo(this.element);
 
  
  this.element.appendTo(document.body);
  this.element.dialog( {
    width : 800,
    position : 'top',
    modal : true,
    draggable : true,
    resizable : true,
    title : "Comments",
    close : function() {
      me.close();
    },
    buttons: {Close: function() { me.close(); }}
  });
  
/*  this.hourEntryListController = new HourEntryListController( {
    parentModel : this.model,
    hourEntryListElement : this.objectEffortEl,
    onUpdate: function() { me.entriesChanged(); }
  });*/
  this.comments = new UserCommentWidget(this.commentEl,
      this.model.id);
  
};

CommentWidget.prototype.entriesChanged = function() {
  this.comments.reload();
  // $(this.element[0]).parent().find('Button').focus(); -> Should first focus to ES box and after save to Close-button. This focus direct to Close-button.
};
CommentWidget.prototype.renderEditor = function(){

	// Refer StoryController.js for comments and functionality
	var modelId = this.model.id;	
	var commentHeader = $('<div class="post_new_outer" />');	
	var clickLink = $('<div class="custom_span_01"> ( Click here to add new comment ) </div>').appendTo(this.commentEditorEl);	
	clickLink.click(jQuery.proxy(function() {
		newCommentPostBlock.toggle();
		postCommentFrame.attr('src',"ajax/doFileUpload.action?commentType=story&objectId="+modelId);
		postCommentFrame.show();
		if(newCommentPostBlock.is(":hidden")){
			clickLink.html(' ( Click here to add new comment ) ');
		}else{
			clickLink.html(' ( Click here to hide ) ');
		}
	}));
	
	var header = $('<input type="button"	 class="customrefresh_span" value="Refresh" />').click(jQuery.proxy(function() {
		UserCommentWidget.prototype.reload();
	}));
	
    var newCommentPostBlock = $('<div />');
	var postCommentFrame ;
    postCommentFrame = $('<iFrame src="" width="96%" height="" scrolling="no" style="border:none;"/>');
	
	postCommentFrame.load(function() {
		if(!postCommentFrame.attr("src")){
			newCommentPostBlock.hide();
		}
		if(newCommentPostBlock.is(":hidden")){
			clickLink.html(' ( Click here to add new comment ) ');
		}else{
			clickLink.html(' ( Click here to hide ) ');
		}
		
	});

	postCommentFrame.attr("id","story_"+modelId);
	postCommentFrame.attr("name","story_"+modelId);
	
	postCommentFrame.appendTo(newCommentPostBlock);
	
	newCommentPostBlock.hide();
	newCommentPostBlock.appendTo(this.commentEditorEl);	
    
    
}
/**
 * Close and destroy the dialog.
 */
CommentWidget.prototype.close = function() {
  this.element.dialog('destroy').remove();
  if(this.model instanceof TaskModel) {
    this.model.reload();
  } else if(this.model instanceof StoryModel) {
    this.model.reloadMetrics();
  }
  if(this.onClose) {
    this.onClose();
  }
};


