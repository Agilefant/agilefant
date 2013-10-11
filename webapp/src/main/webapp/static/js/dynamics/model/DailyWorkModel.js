var DailyWorkModel = function DailyWorkModel() {
  this.initialize();
  this.persistedClassName = "non.existent.DailyWork";
  this.relations = {
      tasksWithoutStory: [],
      stories: [],
      queuedTasks: []
    };
    this.copiedFields = { };
    this.classNameToRelation = {
        "fi.hut.soberit.agilefant.transfer.DailyWorkTaskTO":  "queuedTasks",
        "fi.hut.soberit.agilefant.transfer.StoryTO":  "stories",
        "fi.hut.soberit.agilefant.model.Story":  "stories",
        "fi.hut.soberit.agilefant.transfer.TaskTO":  "tasksWithoutStory",
        "fi.hut.soberit.agilefant.model.Task":  "tasksWithoutStory"
    };
};

DailyWorkModel.prototype = new CommonModel();

DailyWorkModel.prototype._setData = function(newData) {
  if(newData.stories) {
    this._updateRelations("stories", newData.stories);
  }
  if(newData.tasksWithoutStory) {
    this._updateRelations("tasksWithoutStory", newData.tasksWithoutStory);
  }
  if(newData.queuedTasks) {
    this._updateRelations("queuedTasks", newData.queuedTasks);
  }
};

DailyWorkModel.prototype.reload = function() {
  
};

DailyWorkModel.prototype.reloadWorkQueue = function(userId, callback) {
  var me = this;
  $.ajax({
    url: "ajax/workQueue.action",
    type: "post",
    dataType: "json",
    data: {userId: userId},
    success: function(data, status) {
      if(data) {
        me._updateRelations("queuedTasks", data);
      }
      if (callback) {
        callback();
      }
    },
    error: function(xhr, status) {
      MessageDisplay.Error("Unable to refresh task queue.", xhr);
    }
  });
};

DailyWorkModel.prototype.reloadTasksWithoutStory = function(userId, callback) {
  var me = this;
  $.ajax({
    url: "ajax/dailyWorkTasks.action",
    type: "post",
    dataType: "json",
    data: {userId: userId},
    success: function(data, status) {
      if(data) {
        me._updateRelations("tasksWithoutStory", data);
      }
      if (callback) {
        callback();
      }
    },
    error: function(xhr, status) {
      MessageDisplay.Error("Unable to refresh tasks without story.", xhr);
    }
  });
};

DailyWorkModel.prototype.reloadMyStories = function(userId, callback) {
  var me = this;
  $.ajax({
    url: "ajax/dailyWorkStories.action",
    type: "post",
    dataType: "json",
    data: {userId: userId},
    success: function(data, status) {
      if(data) {
        me._updateRelations("stories", data);
      }
      if (callback) {
        callback();
      }
    },
    error: function(xhr, status) {
      MessageDisplay.Error("Unable to refresh story queue.", xhr);
    }
  });
};

DailyWorkModel.prototype.addTask = function(task) {
  this.relations.tasksWithoutStory.push(task);
  this.callListeners(new DynamicsEvents.RelationUpdatedEvent(this, "tasksWithoutStory"));
};

DailyWorkModel.prototype.addStory = function(story) {
  this.relations.stories.push(story);
  this.callListeners(new DynamicsEvents.RelationUpdatedEvent(this, "stories"));
};

DailyWorkModel.prototype.getWorkQueue = function() {
  return this.relations.queuedTasks;
};
DailyWorkModel.prototype.getAssignedStories = function() {
  return this.relations.stories;
};
DailyWorkModel.prototype.getTasksWithoutStory = function() {
  return this.relations.tasksWithoutStory;
};
