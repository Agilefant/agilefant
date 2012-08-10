var SpentEffortWidget = function SpentEffortWidget(model, onClose) {
  this.model = model;
  this.onClose = onClose;
  this.initDialog();
};

SpentEffortWidget.prototype.initDialog = function() {
  var me = this;
  this.element = $('<div/>');
  $('<h2>Effort logger on this element</h2>').appendTo(this.element);
  this.objectEffortEl = $('<div />').appendTo(this.element);
  $('<h2>My spent effort</h2>').appendTo(this.element);
  this.userEffortEl = $('<div />').addClass("ui-widget-content").addClass("ui-corner-all")
    .css({padding: "10px", "margin-bottom": "2em"}).appendTo(this.element);
 
  
  this.element.appendTo(document.body);
  this.element.dialog( {
    width : 800,
    position : 'top',
    modal : true,
    draggable : true,
    resizable : true,
    title : "Spent effort",
    close : function() {
      me.close();
    },
    buttons: {Close: function() { me.close(); }}
  });
  
  this.hourEntryListController = new HourEntryListController( {
    parentModel : this.model,
    hourEntryListElement : this.objectEffortEl,
    onUpdate: function() { me.entriesChanged(); }
  });
  this.userSpentEffort = new UserSpentEffortWidget(this.userEffortEl,
      window.pageController.getCurrentUser().getId());
  
};

SpentEffortWidget.prototype.entriesChanged = function() {
  this.userSpentEffort.reload();
  // $(this.element[0]).parent().find('Button').focus(); -> Should first focus to ES box and after save to Close-button. This focus direct to Close-button.
};

/**
 * Close and destroy the dialog.
 */
SpentEffortWidget.prototype.close = function() {
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


