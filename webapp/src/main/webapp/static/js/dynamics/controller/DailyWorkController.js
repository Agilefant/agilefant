/** */
var DailyWorkController = function(options) {
  this.model = null;
  this.options = {
    userId: null,
    workQueueElement: null,
    assignedStoriesElement: null,
    tasksWithoutStoryElement: null,
    emptyDailyWorkNoteBox: null,
    onUserLoadUpdate: function() {}
  };
  jQuery.extend(this.options, options);
  
  this.init();
  this.initialize();

  window.pageController.setMainController(this);
};
DailyWorkController.prototype = new CommonController();

DailyWorkController.prototype.pageControllerDispatch = function(event) {
  if(event instanceof DynamicsEvents.AddEvent) {
    //new task is added to user's tasks without story
    if (event.getObject() instanceof TaskModel && event.getObject().getParent() instanceof BacklogModel) {
      this.model.addTask(event.getObject());
      if(event.getObject().getOriginalEstimate() > 0) {
        this.options.onUserLoadUpdate();
      }
    }
    // new story is added to user's stories
    if (event.getObject() instanceof StoryModel) {
      this.model.addStory(event.getObject());
    }
  }
};

DailyWorkController.prototype.handleModelEvents = function(event) {
  if(event instanceof DynamicsEvents.NamedEvent) {
    var eventName = event.getEventName();
    if(eventName === "removedFromWorkQueue" || eventName === "addedToWorkQueue") {
      this.model.reloadWorkQueue(this.options.userId);
    }
  }

  //task oe/el changed
  if (event.getObject() instanceof TaskModel && !(event instanceof DynamicsEvents.RankChanged) && !(event instanceof DynamicsEvents.EditEvent)) {
    this.options.onUserLoadUpdate();
  }
  
  if(event instanceof DynamicsEvents.RelationUpdatedEvent && event.getObject() instanceof TaskModel) {
    //task responsibles changed
    if (event.getRelation() === "user") {
      this.options.onUserLoadUpdate();
    }
    //task moved
    if (event.getRelation() === "parent") {
      var me = this;
      this.model.reloadWorkQueue(this.options.userId, function() {
        me.workQueueController.getCurrentView().render();
      });
      this.model.reloadTasksWithoutStory(this.options.userId);
    }
  }

  
};

DailyWorkController.prototype.initialize = function() {
  var me = this;
  var overlay = $("#dailyworkLoadingOverlay");
  overlay.fadeIn();
  overlay.show();
  ModelFactory.initializeFor(ModelFactory.initializeForTypes.dailyWork,
    this.options.userId,
    function(model) {
      me.model = model;
      me._showNoteBox();
      me._paintLists();
      jQuery("#dailyworkPleasewait").remove();
    }
  );
};

DailyWorkController.prototype._showNoteBox = function() {
  if (this.model.getWorkQueue().length === 0 &&
      this.model.getAssignedStories().length === 0 &&
      this.model.getTasksWithoutStory().length === 0) {
    this.options.emptyDailyWorkNoteBox.show();
  }
};

DailyWorkController.prototype._paintLists = function() {
  this.tasksWithoutStoryController = new DailyWorkTasksWithoutStoryController(
      this.model, this.options.tasksWithoutStoryElement, this);
  this.assignedStoriesController = new DailyWorkStoryListController(this.model,
      this.options.assignedStoriesElement, this, { userId: this.options.userId });
  this.workQueueController = new WorkQueueController(this.model,
      this.options.workQueueElement, this, { userId: this.options.userId });
};