
var DailyWorkTasksWithoutStoryController = function(model, element, parentController) { 
  TasksWithoutStoryController.call(this, model, element, parentController);
  this.autohideCells = [ DailyWorkTasksWithoutStoryController.columnIndices.description, DailyWorkTasksWithoutStoryController.columnIndices.actions, DailyWorkTasksWithoutStoryController.columnIndices.detailedContext ];
};
extendObject(DailyWorkTasksWithoutStoryController, TasksWithoutStoryController);


DailyWorkTasksWithoutStoryController.columnNames =
  [ "prio", "name", "state", "context", "detailedContext", "responsibles", "el", "oe", "es", "actions", "description", "buttons","comments"];
DailyWorkTasksWithoutStoryController.columnIndices = CommonController.createColumnIndices(DailyWorkTasksWithoutStoryController.columnNames);


DailyWorkTasksWithoutStoryController.prototype.createTask = function() {
  TasksWithoutStoryController.prototype.createTask.call(this, true);
};

DailyWorkTasksWithoutStoryController.prototype._getTableConfig = function() {
  var config = new DynamicTableConfiguration({
    caption: "My tasks without story",
    dataType: "stories",
    captionConfig: {
      cssClasses: "dynamictable-caption-block ui-widget-header ui-corner-all"
    },
    cssClass: "dynamicTable-sortable-tasklist ui-widget-content ui-corner-all task-table tasksWithoutStory-table",
    dataType: "tasksWithoutStory",
    rowControllerFactory: TasksWithoutStoryController.prototype.taskControllerFactory,
    dataSource: DailyWorkModel.prototype.getTasksWithoutStory,
    validators: [ TaskModel.Validators.backlogSelectedValidator ],
    beforeCommitFunction: TaskController.prototype.checkTaskAndCommit
  });

  config.addCaptionItem({
    name:   "createTask",
    text:   "Create task",
    cssClass:"create",
    callback: DailyWorkTasksWithoutStoryController.prototype.createTask
  });
  
  return config;
};

DailyWorkTasksWithoutStoryController.prototype.createTask = function(forceAssignCurrentUser) {
  var mockModel = ModelFactory.createObject(ModelFactory.types.task);
  if (this.model && this.model instanceof IterationModel) {
    mockModel.setIteration(this.model);
  }
  // Check whether to add the current user as a responsible.
  var currentUser = PageController.getInstance().getCurrentUser(); 
  if (currentUser.isAutoassignToTasks() || forceAssignCurrentUser) {
    mockModel.addResponsible(currentUser.getId());
  }
  
  var controller = new TaskController(mockModel, null, this);
  var row = this.getCurrentView().createRow(controller, mockModel, "top");
  controller.view = row;
  row.autoCreateCells([DailyWorkTasksWithoutStoryController.columnIndices.prio, DailyWorkTasksWithoutStoryController.columnIndices.comments]);
  row.render();
  controller.openRowEdit();
  row.getCellByName("actions").hide();
  row.getCellByName("buttons").show();
  row.getCellByName("description").hide();
};

DailyWorkTasksWithoutStoryController.prototype._addColumnConfigs = function(config) {
  config.addColumnConfiguration(DailyWorkTasksWithoutStoryController.columnIndices.prio, TasksWithoutStoryController.columnConfig.prio);
  config.addColumnConfiguration(DailyWorkTasksWithoutStoryController.columnIndices.name, TasksWithoutStoryController.columnConfig.name);
  config.addColumnConfiguration(DailyWorkTasksWithoutStoryController.columnIndices.state, TasksWithoutStoryController.columnConfig.state);
  
  config.addColumnConfiguration(DailyWorkTasksWithoutStoryController.columnIndices.context, TasksWithoutStoryController.columnConfig.context);
 // config.addColumnConfiguration(DailyWorkTasksWithoutStoryController.columnIndices.detailedContext, WorkQueueController.columnConfig.detailedContext);
  
  config.addColumnConfiguration(DailyWorkTasksWithoutStoryController.columnIndices.responsibles, TasksWithoutStoryController.columnConfig.responsibles);
  config.addColumnConfiguration(DailyWorkTasksWithoutStoryController.columnIndices.el, TasksWithoutStoryController.columnConfig.effortLeft);
  config.addColumnConfiguration(DailyWorkTasksWithoutStoryController.columnIndices.oe, TasksWithoutStoryController.columnConfig.originalEstimate);
  if (Configuration.isTimesheetsEnabled()) {
    config.addColumnConfiguration(DailyWorkTasksWithoutStoryController.columnIndices.es, TasksWithoutStoryController.columnConfig.effortSpent);
  }
  config.addColumnConfiguration(DailyWorkTasksWithoutStoryController.columnIndices.actions, TasksWithoutStoryController.columnConfig.actions);
  config.addColumnConfiguration(DailyWorkTasksWithoutStoryController.columnIndices.description, TasksWithoutStoryController.columnConfig.description);
  config.addColumnConfiguration(DailyWorkTasksWithoutStoryController.columnIndices.buttons, TasksWithoutStoryController.columnConfig.buttons);
  config.addColumnConfiguration(DailyWorkTasksWithoutStoryController.columnIndices.comments, TasksWithoutStoryController.columnConfig.comments);

};




