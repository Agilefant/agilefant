var StoryInfoWidget = function StoryInfoWidget(model, controller, parentView) {
  this.parentView = parentView;
  this.element = parentView.getElement();
  this.model = model;
  this.controller = controller;
};

StoryInfoWidget.prototype = new CommonSubView();

StoryInfoWidget.prototype.storyContextFactory = function(cellView, storyModel) {
  return new CellBubble({
    title: 'Context',
    url: 'ajax/getStoryHierarchy.action?storyId=' + storyModel.getId(),
    text: storyModel.getParentStoryName()
  }, cellView);
};



StoryInfoWidget.prototype.renderAlways = function() {
  return true;
};

StoryInfoWidget.prototype.getElement = function() {
  return this.container;
};

StoryInfoWidget.prototype._draw = function() {
  this.container = $('<div />');
  
  this.hr = $('<div class="ruler">&nbsp;</div>').appendTo(this.container);

  var config = new DynamicTableConfiguration({
    leftWidth: '10%',
    rightWidth: '88%',
    closeRowCallback: null
  });
  config.addColumnConfiguration(0, {
    title: "Labels",
    subViewFactory:  StoryController.prototype.labelsViewFactory
  });
  config.addColumnConfiguration(1, {
    title: "Parent story",
    subViewFactory: StoryInfoWidget.prototype.storyContextFactory
  });
  config.addColumnConfiguration(2, {
    title: "Reference ID",
    get: StoryModel.prototype.getId,
    decorator: DynamicsDecorators.quickReference
  });
  config.addColumnConfiguration(3, {
    title: 'Description',
    get : StoryModel.prototype.getDescription,
    decorator: DynamicsDecorators.emptyDescriptionDecorator,
    editable : true,
    edit : {
      editor : "Wysiwyg",
      set : StoryModel.prototype.setDescription
    }
  });
  config.addColumnConfiguration(4, {
	    title: "",
	    cssClass :"custom_align01",	  
	    subViewFactory: StoryController.prototype.storyCommentsFactory	   
	  });
  this.storyInfo = new DynamicVerticalTable(this, this.model, config, this.container);
  
  this.container.appendTo(this.parentView.getElement());
};

StoryInfoWidget.prototype.onEdit = function() {
  this.render();
};

StoryInfoWidget.prototype.render = function() {
  this.storyInfo.render();
};
