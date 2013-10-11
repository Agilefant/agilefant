/*
 * DYNAMICS - MODEL - Task Model
 */

/**
 * Task model constructor.
 * <p>
 * Calls the <code>initialize</code> method of the super class
 * <code>CommonModel</code>.
 * 
 * @constructor
 * @base CommonModel
 * @see CommonModel
 */
var TaskModel = function TaskModel() {
  this.initialize();
  this.persistedClassName = "fi.hut.soberit.agilefant.model.Task";
  this.relations = {
    backlog: null,
    story: null,
    user: [],
    hourEntry: []
  };
  this.transientData = { };
  this.copiedFields = {
    "name": "name",
    "state": "state",
    "description": "description",
    "effortLeft": "effortLeft",
    "originalEstimate": "originalEstimate",
    "rank": "rank",
    "effortSpent": "effortSpent"
  };
  this.classNameToRelation = {
      "fi.hut.soberit.agilefant.model.Iteration":     "backlog",
      "fi.hut.soberit.agilefant.model.User":          "user",
      "fi.hut.soberit.agilefant.model.Story":         "story",
      "fi.hut.soberit.agilefant.model.HourEntry":     "hourEntry"
  };
  this.transientData.workingOnTaskIds = [];
  this.metricFields = ["effortLeft", "originalEstimate", "effortSpent"];
  
  this.clonedModelTypes = [ ModelFactory.types.workQueueTask ];
};

TaskModel.prototype = new CommonModel();

TaskModel.Validators = {
  backlogSelectedValidator: function(model) {
    if (!model.relations.backlog) {
      throw "Please select a backlog";
    }
  }
};


TaskModel.prototype._setData = function(newData) {
  if (newData.id) {
    this.id = newData.id;
  }
  
  if (newData.responsibles) {
    this._updateRelations(ModelFactory.types.user, newData.responsibles);
  }
  
  if (newData.workingOnTask) {
    var workingOnTaskIds= [];
    
    $.each(newData.workingOnTask, function (k, v) {
      workingOnTaskIds.push(v.id);
    });
    
    this.transientData.workingOnTaskIds = workingOnTaskIds;
  }
  if(newData.story) {
    this._updateRelations(ModelFactory.types.story, newData.story);
  }
  if (newData.iteration) {
    this._updateRelations(ModelFactory.types.iteration, newData.iteration);
  }
};

TaskModel.prototype.reload = function() {
  var me = this;
  $.ajax({
    type: "POST",
    url: "ajax/retrieveTask.action",
    async: true,
    cache: false,
    data: {taskId: this.id},
    dataType: "json",
    success: function(data, type) {
      me.setData(data, false);
    }
  });
};
TaskModel.prototype._saveData = function(id, changedData) {
  var me = this;
  
  var url = "ajax/storeTask.action";
  var data = {};
  
  var responsibleData = {};

  if (changedData.responsiblesChanged) {
    responsibleData.responsiblesChanged = true;
    responsibleData.newResponsibles     = changedData.responsibles;
    delete changedData.responsiblesChanged;
    delete changedData.responsibles;
  }
  
  if (changedData.storyToStarted) {
    data.storyToStarted = true;
    delete changedData.storyToStarted;
    delete this.currentData.storyToStarted;
  }
  
  data.task = changedData;

  
  
  if (id) {
    data.taskId = id;

    if (changedData.iterationChanged) {
        if(this.relations.backlog instanceof BacklogModel) {
            data.iterationId = this.relations.backlog.getId();
            data.iterationChanged = true;

            delete changedData.iterationChanged;
        }
    }
  }
  else {
    url = "ajax/createTask.action";
    if(this.relations.backlog instanceof BacklogModel) {
      data.iterationId = this.relations.backlog.getId();
    }
    else if (this.relations.story instanceof StoryModel) {
      data.storyId = this.relations.story.getId();
    }
    // set story from id only
    else if (changedData.storyId) {
      data.storyId = changedData.storyId;
    }
    // set iteration from backlog id only
    else if (changedData.backlogId) {
      data.iterationId = changedData.backlogId;
    }
  }
  
  data = HttpParamSerializer.serialize(data); 
  
  if (responsibleData.responsiblesChanged) {
    data.responsiblesChanged = true;
    data.newResponsibles     = responsibleData.newResponsibles;
  }

  
  jQuery.ajax({
    type: "POST",
    url: url,
    async: true,
    cache: false,
    data: data,
    dataType: "json",
    success: function(newData, status) {
      MessageDisplay.Ok("Task saved successfully");
      var object = ModelFactory.updateObject(newData);
      if(!id) {
        if (me.relations.story instanceof StoryModel) {
          me.relations.story.addTask(object);
        }
        else if (me.relations.backlog instanceof IterationModel) {
          me.relations.backlog.addTask(object);
        }
        object.callListeners(new DynamicsEvents.AddEvent(object));
      }
      if ((data.storyToStarted || PageController.getInstance().getCurrentUser().getMarkStoryStarted() === "always")&& me.relations.story) {
        me.relations.story.callListeners(new DynamicsEvents.EditEvent(me.relations.story));
      }
      // Reload the metrics.
      if (me.getStory())
      	me.getStory().reloadMetrics();
      if (PageController.getInstance().mainController instanceof IterationController)
      	PageController.getInstance().mainController.reloadMetricsBox();
      jQuery('.ui-button:eq(' + 0 + ')').focus();
    },
    error: function(xhr, status, error) {
      MessageDisplay.Error("Error saving task", xhr);
    }
  });
};

TaskModel.prototype._remove = function(successCallback, extraData) {
  var me = this;
  var data = {
      taskId: me.getId()
  };
  jQuery.extend(data, extraData);
  jQuery.ajax({
      type: "POST",
      url: "ajax/deleteTask.action",
      async: true,
      cache: false,
      dataType: "text",
      data: data,
      success: function(data,status) {
        MessageDisplay.Ok("Task removed");
        if (successCallback) {
          successCallback();
        }
      },
      error: function(xhr,status) {
        MessageDisplay.Error("Error deleting task.", xhr);
      }
  });
};

TaskModel.prototype.moveToIteration = function(iterationId) {
  var oldParent = this.getParent();
  
  var me = this;
  var data = {
    "taskId": this.getId(),
    "iterationId": iterationId
  };
  
  jQuery.ajax({
    type:     "POST",
    url:      "ajax/moveTask.action",
    async:    true,
    cache:    false,
    dataType: "json",
    data:     data,
    success:  function(data, status) {
      MessageDisplay.Ok("Task moved");  
      
      me.relations.backlog = null;
      me.relations.story = null;

      me._setData(data);
      
      var newParent = me.getParent();
      
      oldParent.removeRelation(me);
      newParent.callListeners(new DynamicsEvents.RelationUpdatedEvent(newParent, "task"));
      
      me.callListeners(new DynamicsEvents.RelationUpdatedEvent(me, "parent"));
      
      me.callListeners(new DynamicsEvents.EditEvent(me));
    },
    error: function(xhr, status) {
      MessageDisplay.Error("Error moving task.", xhr);
    }
  });
};

TaskModel.prototype.rankUnder = function(rankUnderId, moveUnder) {
  var me = this;
  
  var data = {
    taskId: me.getId(),
    rankUnderId: rankUnderId
  };
  
  // If the item was moved
  if (moveUnder && moveUnder !== me.getParent()) {
    if (moveUnder instanceof IterationModel) {
      data.iterationId = moveUnder.getId();  
    }
    else if (moveUnder instanceof StoryModel) {
      data.storyId = moveUnder.getId();
    }
  }
  else {
    if (me.getParent() instanceof IterationModel) {
      data.iterationId = me.getParent().getId();  
    }
    else if (me.getParent() instanceof StoryModel) {
      data.storyId = me.getParent().getId();
    }
  }
  if(!data.iterationId && !data.storyId) {
    return; //there has to be a container
  }
  jQuery.ajax({
    url: "ajax/rankTaskAndMoveUnder.action",
    type: "post",
    dataType: "json",
    data: data,
    success: function(data, status) {
      MessageDisplay.Ok("Task ranked successfully.");
      var oldParent = me.getParent();
      me.setData(data);
      if(oldParent === moveUnder) {
        oldParent.reload(function() {
          oldParent.callListeners(new DynamicsEvents.RankChanged(oldParent,"task"));
        });
      }
      if (oldParent !== moveUnder) {
        moveUnder.reload();
        oldParent.reload();
      }
    },
    error: function(xhr, status) {
      MessageDisplay.Error("An error occured while ranking the task.", xhr);
    }
  });
};

TaskModel.prototype.getIteration = function() {
    var parent = this.getParent();
    if (! parent) {
        return null;
    }
    else if (parent instanceof StoryModel) {
        return parent.getBacklog();
    }
    else if (parent instanceof IterationModel) {
        return parent;
    }
    return null;
};

TaskModel.prototype.addToMyWorkQueue = function(successCallback) {
    var me = this;
   
    jQuery.ajax({
        type: "POST",
        url: "ajax/addToWorkQueue.action",
        async: true,
        cache: false,
        dataType: "json",
        data: {
           taskId: me.getId()
        },
        success: function(data,status) {
            MessageDisplay.Ok("Task appended to your task queue");
            me.setData(data);
            if (successCallback) {
               successCallback();
            }
            me.callListeners(new DynamicsEvents.EditEvent(me));
            me.callListeners(new DynamicsEvents.NamedEvent(me, "addedToWorkQueue"));
        },
        error: function(xhr,status) {
            MessageDisplay.Error("Error adding task to task queue.", xhr);
        }
    });
};

TaskModel.prototype.removeFromMyWorkQueue = function(successCallback) {
    var me = this;

    jQuery.ajax({
        type: "POST",
        url: "ajax/deleteFromWorkQueue.action",
        async: true,
        cache: false,
        dataType: "json",
        data: {
           taskId: me.getId()
        },
        success: function(data,status) {
            MessageDisplay.Ok("Task removed from your task queue");
            
            me.setData(data);
            if (successCallback) {
               successCallback();
            }
            me.callListeners(new DynamicsEvents.EditEvent(me));
            me.callListeners(new DynamicsEvents.NamedEvent(me, "removedFromWorkQueue"));
        },
        error: function(xhr,status) {
            MessageDisplay.Error("Error removing task from task queue.", xhr);
        }
    });
};

/**
 * Resets the tasks original estimate and effort left
 */
TaskModel.prototype.resetOriginalEstimate = function() {
  var me = this;
  jQuery.ajax({
    url: "ajax/resetOriginalEstimate.action",
    type: "post",
    dataType: "json",
    data: {taskId: me.getId()},
    success: function(data, status) {
      MessageDisplay.Ok("Original estimate reset.");
      me.setData(data);
    },
    error: function(xhr) {
      MessageDisplay.Error("An error occured while ranking reseting the original estimate.", xhr);
    }
  });
};

TaskModel.prototype.getParent = function() {
  if (this.relations.story) {
    return this.relations.story;
  }
  else {
    return this.relations.backlog;
  }
};

TaskModel.prototype.setStory = function(story) {
  this.addRelation(story);
};

TaskModel.prototype.setIteration = function(iteration) {
  this.addRelation(iteration);
};

/* GETTERS AND SETTERS IN ALPHABETICAL ORDER */
TaskModel.prototype.getDescription = function() {
  return this.currentData.description;
};

TaskModel.prototype.setDescription = function(description) {
  this.currentData.description = description;
};

TaskModel.prototype.getEffortLeft = function() {
  return this.currentData.effortLeft;
};

TaskModel.prototype.setEffortLeft = function(effortLeft) {
  this.currentData.effortLeft = effortLeft;
};

TaskModel.prototype.getName = function() {
  return this.currentData.name;
};

TaskModel.prototype.setName = function(name) {
  this.currentData.name = name;
};

TaskModel.prototype.getOriginalEstimate = function() {
  return this.currentData.originalEstimate;
};

TaskModel.prototype.setOriginalEstimate = function(originalEstimate) {
  this.currentData.originalEstimate = originalEstimate;
};

TaskModel.prototype.getRank = function() {
  return this.currentData.rank;
};

TaskModel.prototype.getState = function() {
  return this.currentData.state;
};
TaskModel.prototype.setState = function(state) {
  this.currentData.state = state;
};

TaskModel.prototype.getStory = function() {
  return this.relations.story;
};

TaskModel.prototype.getResponsibles = function() {
  if (this.currentData.responsibles) {
    var users = [];
    $.each(this.currentData.responsibles, function(k, id) {
      users.push(ModelFactory.getObject(ModelFactory.types.user, id));
    });
    return users;
  }
  return this.relations.user;
};

TaskModel.prototype.getAnnotatedResponsibles = function() {
  var annotated = [];
  var me = this;
  $.each(this.getResponsibles(), function (k, v) {
    var workingOnTask = $.inArray(v.id, me.transientData.workingOnTaskIds) !== -1;
    annotated.push({
        user:    v,
        workingOnTask: workingOnTask
    });
  });
  return annotated;
};

TaskModel.prototype.setResponsibles = function(userIds, userJson) {
  if (userJson) {
    $.each(userJson, function(k,v) {
      ModelFactory.updateObject(v);    
    });
  }
  
  this.currentData.responsibles = userIds;
  this.currentData.responsiblesChanged = true;
};

TaskModel.prototype.isWorkingOnTask = function(user) {
  var userId = user.getId();
  return $.inArray(userId, this.transientData.workingOnTaskIds) != -1;
};


/**
 * Add a responsible for the task and modify the current data.
 * Does not commit the change.
 */
TaskModel.prototype.addResponsible = function(userId) {
  if (this.currentData.responsibles) {
    this.currentData.responsibles.push(userId);
  }
  else {
    this.currentData.responsibles = [userId];
  }
  
  this.currentData.responsiblesChanged = true;
};


TaskModel.prototype.getEffortSpent = function() {
  return this.currentData.effortSpent;
};


/**
 * Get context info.
 * Used at Daily Work
 */
TaskModel.prototype.getContext = function() {
  var parent = this.getParent();
  if (parent instanceof IterationModel) {
    return {"backlog": parent, "story": null};
  }
  else if (this.getParent() instanceof StoryModel) {
    return {"backlog": parent.getIteration(), "story": parent};
  }
  return {"backlog": null, "story": null};
};

