var TaskController = function TaskController(model, view, parentController) {
  this.model = model;
  this.view = view;
  this.parentController = parentController;
  this.init();
  this.autohideCells = [ "buttons" ];
};

TaskController.columnNames =
	  ["prio", "name", "state", "responsibles", "el", "oe", "es", "actions", "description", "buttons","comments"];
TaskController.columnIndices = CommonController.createColumnIndices(TaskController.columnNames);


TaskController.prototype = new CommonController();

TaskController.prototype.handleModelEvents = function(event) {
  if(this.parentController) {
    this.parentController.handleModelEvents(event);
  }
  // listen for task metrics changes
  if (event instanceof DynamicsEvents.MetricsEvent) {
    // reload the parent story (if within a story)
    if (this.model.getParent() instanceof StoryModel) {
      this.model.getParent().reloadMetrics();
    }
  }
  
};

TaskController.prototype.sortAndMoveTask = function(view, model, previousModel) {
  var targetTask = null;
  var targetModel = view.getParentView().getModel();
  if (previousModel) {
    targetTask = previousModel;
    model.rankUnder(targetTask.getId(), targetModel);
  }
  else {
    model.rankUnder(-1, targetModel);
  }
};

TaskController.prototype.moveToIteration = function() {
  var me = this;
  $(window).autocompleteSingleDialog({
    dataType: "currentIterations",
    cancel: function() { return; },
    callback: function(id) { me.model.moveToIteration(id); },
    title: "Select iteration to move to"
  });
};

TaskController.prototype.moveTask = function(targetModel) {
  this.model.rankUnder(-1, targetModel);
};


TaskController.prototype.toggleFactory = function(view, model) {
  var options = {
      collapse : TaskController.prototype.hideDetails,
      expand : TaskController.prototype.showDetails,
      expanded: false
    };
    this.toggleView = new DynamicTableToggleView(options, this, view);
    return this.toggleView;
};

TaskController.prototype.addToMyWorkQueue = function() {
  this.model.addToMyWorkQueue();
};

TaskController.prototype.removeFromMyWorkQueue = function() {
  this.model.removeFromMyWorkQueue();
};

TaskController.prototype.removeTask = function() {
  var me = this;
  var dialog = new LazyLoadedFormDialog();
  dialog.init({
    title: "Delete task",
    url: "ajax/deleteTaskForm.action",
    data: {
      taskId: me.model.getId()
    },
    okCallback: function(extraData) {
      me.model.remove(function() {
        me.parentController.removeChildController("task", this);
      }, extraData);
    }
  });
};

TaskController.prototype.showDetails = function() {
  var cell = this.view.getCellByName("description");
  if (cell) {
    cell.show();
  }
  var commentCell = this.view.getCellByName("comments");
  if (commentCell) {
	  commentCell.show();
  }
};

TaskController.prototype.hideDetails = function() {
  var cell = this.view.getCellByName("description");
  if (cell) {
    cell.hide();
  }
  var commentCell = this.view.getCellByName("comments");
  if (commentCell) {
	  commentCell.hide();
  }
};

TaskController.prototype.openDetails = function() {
  var details = new TaskInfoDialog(this.model);
};

TaskController.prototype.createSplitTask = function() {
    var parentController = this.parentController;
    var dialog = new TaskSplitDialog(this.model, function() {
        parentController.model.reload();
    });
};


TaskController.prototype.checkTaskAndCommit = function(model) {
  var changedData = model.getChangedData();
  var story = model.getStory();
  var currentUser = PageController.getInstance().getCurrentUser();
	
  if (changedData.state && changedData.state === "IMPLEMENTED" && model.currentData.effortLeft > 0) {
	// User has changed a task to "READY" while there is still effort left. 

	this.clearEffortLeftWhenTaskReady(model);
  } 
  else if (story && changedData.state && currentUser.getMarkStoryStarted() === "ask" && story.getState() === "NOT_STARTED" 
      && changedData.state !== "NOT_STARTED" && model.persistedData.state === "NOT_STARTED") {
// User has changed a task from "NOT_STARTED" state while the containing story is still "NOT_STARTED". 

	this.markStoryAsStarted(model);	
  } 
  else {
  	// Don't need to wait for user to complete dialog boxes, so save task.
  	
  	model.commit();
  }
}

TaskController.prototype.markStoryAsStarted = function(model) {
	var msg = new DynamicsConfirmationDialog(
	    "Mark the story started as well?",
	    "Do you want to mark the story started as well?",
	    function() {
	      model.currentData.storyToStarted = true;
	      model.commit();
	    },
	    function() {
	      model.commit();
	    }
	  );
};

TaskController.prototype.clearEffortLeftWhenTaskReady = function(model) {
	var msg = new DynamicsConfirmationDialog(
	    "Set effort left to zero?",
	    "Story marked as ready. Set effort left to zero as well?",
	    function() {
	      model.currentData.effortLeft = 0;
	      model.commit();
	    },
	    function() {
	      model.commit();
	    }
	  );
};


TaskController.prototype.actionColumnFactory = function(view, model) {
  var actionItems = [{
    text : "Split",
    callback : TaskController.prototype.createSplitTask
  }, {
    text : "Move",
    callback : TaskController.prototype.moveToIteration
  },{
    text : "Delete",
    callback : TaskController.prototype.removeTask
  }, {
    text : "Append to my work queue",
    callback : TaskController.prototype.addToMyWorkQueue,
    enabled : TaskController.prototype.addToQueueEnabled
  }, {
    text : "Remove from work queue",
    callback : TaskController.prototype.removeFromMyWorkQueue,
    enabled : TaskController.prototype.removeFromQueueEnabled
  }, {
    text: "Spent effort",
    callback: TaskController.prototype.openLogEffort,
    enabled: function() { return Configuration.isTimesheetsEnabled(); }
  }, {
    text : "Reset original estimate",
    callback : TaskController.prototype.resetOriginalEstimate
  }];
  var actionView = new DynamicTableRowActions(actionItems, this, this.model,
      view);
  return actionView;
};

TaskController.prototype.addToQueueEnabled = function(model, parentView) {
  if (model.getState() === "DONE") {
    return false;
  }
  
  return ! model.isWorkingOnTask(PageController.getInstance().getCurrentUser());
};

TaskController.prototype.removeFromQueueEnabled = function(model, parentView) {
  if (model.getState() === "DONE") {
    return false;
  }
  
  return model.isWorkingOnTask(PageController.getInstance().getCurrentUser());
};

TaskController.prototype.resetOriginalEstimate = function() {
  var me = this;
  var msg = new DynamicsConfirmationDialog("Reset original estimate?", "Do you want to reset the task's original estimate?", function() {
    me.model.resetOriginalEstimate();  
  });
};

TaskController.prototype.contextEditable = function() {
  if (this.getCurrentView().isInRowEdit()) {
    return true;
  }
  return false;
};

TaskController.prototype.effortLeftEditable = function() {
  if (this.model.getState() === "DONE") {
    return false;
  }
  return true;
};

TaskController.prototype.originalEstimateEditable = function() {
  if (this.model.getState() === "DONE") {
    MessageDisplay.Warning("Editing original estimate is not allowed for done tasks.");
    return false;
  } else if(this.model.getOriginalEstimate() || this.model.getEffortLeft()) {
    this.resetOriginalEstimate();
    return false;
  }
  return true;
};

TaskController.prototype.isEditable = function () {
  return true;
};

TaskController.prototype.openQuickLogEffort = function(model, view) {
  if (model.id !== null) {
  	this.openLogEffort();
    //view.openEditor(false, null, true);
  }
};

TaskController.prototype.quickLogEffort = function (spentEffort) {
  if (spentEffort !== "") {
    HourEntryModel.logEffortForCurrentUser(this, spentEffort);
  }
};

TaskController.prototype.openLogEffort = function() {
  var widget = new SpentEffortWidget(this.model);
};

