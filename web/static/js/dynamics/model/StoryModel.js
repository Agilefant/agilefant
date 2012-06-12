
/**
 * Constructor for the <code>StoryModel</code> class.
 * 
 * @constructor
 * @base CommonModel
 * @see CommonModel#initialize
 */
var StoryModel = function StoryModel() {
  this.initialize();
  this.persistedClassName = "fi.hut.soberit.agilefant.model.Story";
  this.relations = {
    backlog: null,
    project: null,
    iteration: null,
    task: [],
    hourEntry: [],
    user: [],
    story: [],
    label: [],
    parent: null
  };
  this.metrics = {};
  this.copiedFields = {
    "name": "name",
    "storyValue": "storyValue",
    "description": "description",
    "state": "state",
    "storyPoints": "storyPoints",
    "rank": "rank"
  };
  this.classNameToRelation = {
      "fi.hut.soberit.agilefant.model.Product":       "backlog",
      "fi.hut.soberit.agilefant.model.Project":       "backlog",
      "fi.hut.soberit.agilefant.model.Iteration":     "iteration",
      "fi.hut.soberit.agilefant.model.User":          "user",
      "fi.hut.soberit.agilefant.model.Label":         "label",
      "fi.hut.soberit.agilefant.model.Task":          "task",
      "fi.hut.soberit.agilefant.model.StoryHourEntry": "hourEntry",
      "fi.hut.soberit.agilefant.model.Story":         "story"
  };
  this.metricFields = ["storyValue", "state"];
  this.metricFields = ["storyPoints", "state"];
};

StoryModel.prototype = new CommonModel();

StoryModel.Validators = {
  backlogValidator: function(model) {
    if (!model.getBacklog()) {
      throw "Please select a parent backlog";
    }
  }
};

/**
 * Internal function to parse data.
 * @throws {String "Invalid data"} if data is invalid
 */
StoryModel.prototype._setData = function(newData) { 
  // Set the id
  this.id = newData.id;
    
  //set the rank by hand if it exists in the data
//  if(newData.rank !== undefined && newData.rank !== null) {
//    this.setRank(newData.rank);
//  }
  
  // Set the tasks
  if (newData.tasks) {
    this._updateRelations(ModelFactory.types.task, newData.tasks);
  }
  
  if(newData.responsibles) {
    this._updateRelations(ModelFactory.types.user, newData.responsibles);
  }
  if(newData.children) {
    this._updateRelations(ModelFactory.types.story, newData.children);
  }
  if(newData.parent) {
    this.relations.parent = ModelFactory.updateObject(newData.parent, true);
  }
  if(newData.backlog) {
    this.setBacklog(ModelFactory.updateObject(newData.backlog, true));
  }
  if (newData.iteration) {
    this.setIteration(ModelFactory.updateObject(newData.iteration, true));
  }
  if (newData.labels) {
    this._updateRelations(ModelFactory.types.label, newData.labels);
  }
  if(newData.metrics) {
    this.metrics = newData.metrics;
  }
};

/**
 * Saves a copy of the given story as a sibling of the current. 
 * @author braden
 */
StoryModel.prototype._copyStory = function(story)
{
  var me = this;
  var idClosure = function() { return story.id; };	// Create closure to access the story
  var data = {};
  var url = "ajax/copyStorySibling.action";
  data.storyId = story.id;
  document.body.style.cursor = "wait";
  jQuery.ajax({
    type: "POST",
    url: url,
    async: true,
    cache: false,
    data: data,
    dataType: "json",
    success: function(newData, status) {    	
      var object = ModelFactory.updateObject(newData);
      possibleIteration = story.getIteration();
      if(newData && newData.id && possibleIteration) {
        possibleIteration.addStory(object);
        object.callListeners(new DynamicsEvents.AddEvent(object));
      }
      object.rankUnder(story.id, object);
      MessageDisplay.Ok("Story created successfully");
    },
    error: function(xhr, status, error) {
      MessageDisplay.Error("Error saving story", xhr);
    }
  });
}

/**
 * Internal function to send the data to server.
 */
StoryModel.prototype._saveData = function(id, changedData) {
  var me = this;
  var possibleBacklog = this.getBacklog();
  var possibleIteration = this.getIteration();
  
  var url = "ajax/storeStory.action";
  var data = {};
  
  if (changedData.usersChanged) {
    jQuery.extend(data, {userIds: changedData.userIds, usersChanged: true});
    delete changedData.userIds;
    delete changedData.usersChanged;
    delete this.currentData.userIds;
    delete this.currentData.usersChanged;
  }
  
  if (changedData.tasksToDone) {
    data.tasksToDone = true;
    delete changedData.tasksToDone;
    delete this.currentData.tasksToDone;
  }
  
  if(this.currentData.backlog) {
    delete this.currentData.backlog;
  }

  if (changedData.labels) {
    data.labelNames = changedData.labels;
    delete this.currentData.labels;
    delete changedData.labels;
  }
  
  jQuery.extend(data, this.serializeFields("story", changedData));
  if(ArrayUtils.countObjectFields(data) === 0) {
    return;
  }
  
  // Add the id
  if (id) {
    data.storyId = id;
  }
  else {
    url = "ajax/createStory.action";
  }

  if (possibleBacklog) {
    data.backlogId = possibleBacklog.getId();
  } else if (possibleIteration) {
    possibleBacklog = possibleIteration;
    data.backlogId = possibleIteration.getId();
  }
  if (possibleIteration) {
    data.iteration = possibleIteration.getId();
  }
  
  
  jQuery.ajax({
    type: "POST",
    url: url,
    async: true,
    cache: false,
    data: data,
    dataType: "json",
    success: function(newData, status) {
      MessageDisplay.Ok("Story saved successfully");
      var object = ModelFactory.updateObject(newData);
      
      if(!id) {
    	// Set rank to be negative temporarily, otherwise the new story will be second on the list as there would be two 0 rank stories
    	// The rank will be have the correct value after the listeners callbacks are executed
    	object.setRank(-1);
        if (possibleBacklog) {
          possibleBacklog.addStory(object);
        }
        object.callListeners(new DynamicsEvents.AddEvent(object));
        possibleBacklog.callListeners(new DynamicsEvents.RankChanged(possibleBacklog,"story"));
      }
    },
    error: function(xhr, status, error) {
      MessageDisplay.Error("Error saving story", xhr);
    }
  });
};

StoryModel.prototype.reload = function(callback) {
  var me = this;
  jQuery.getJSON(
    "ajax/retrieveStory.action",
    {storyId: me.getId()},
    function(data,status) {
      me.setData(data, false);
      me.callListeners(new DynamicsEvents.EditEvent(me));
      if (callback) {
        callback();
      }
    }
  );
};

StoryModel.prototype.reloadMetrics = function() {
  var me = this;
  jQuery.getJSON(
    "ajax/retrieveStoryMetrics.action",
    {storyId: me.getId()},
    function(data,status) {
      me.setData(data);
      me.callListeners(new DynamicsEvents.EditEvent(me));
      me.callListeners(new DynamicsEvents.MetricsEvent(me));
    }
  );
};

StoryModel.prototype.canMoveStory = function(backlogId) {
  var sendAjax = false, me = this;
  jQuery.ajax({
    url: "ajax/checkChangeBacklog.action",
    data: { storyId: this.getId(), backlogId: backlogId },
    async: false,
    cache: false,
    type: 'POST',
    dataType: 'html',
    success: function(data, status) {
      if (jQuery.trim(data).length === 0) {
        sendAjax = true;
      }
      else {
        me.callListeners(new DynamicsEvents.StoryTreeIntegrityViolation(me, data, backlogId));
      }
    }
  });
  return sendAjax;
};
StoryModel.prototype.moveStory = function(backlogId) {
  this._moveStory(backlogId, "ajax/moveStory.action", false);
};

StoryModel.prototype.moveStoryOnly = function(backlogOrIterationId, moveParents) {
  this._moveStory(backlogOrIterationId, "ajax/safeMoveSingleStory.action", moveParents);
};

StoryModel.prototype.moveStoryAndChildren = function(backlogOrIterationId, moveParents) {
  this._moveStory(backlogOrIterationId, "ajax/moveStoryAndChildren.action", moveParents);
};

StoryModel.prototype._moveStory = function(backlogOrIterationId, url, moveParents) {
  var me = this;
  var oldBacklog = this.relations.backlog;
  var oldProject = this.relations.project;
  var oldIteration = this.relations.iteration;

  jQuery.ajax({
    url: url,
    data: {storyId: me.getId(), backlogId: backlogOrIterationId, moveParents: moveParents},
    dataType: 'json',
    type: 'post',
    async: true,
    cache: false,
    success: function(data,status) {
      me.callListeners(new DynamicsEvents.NamedEvent(me, "storyMoved"));
      me.relations.backlog = null;
      me.relations.project = null;
      me.relations.iteration = null;
      me._setData(data);
      
      //remove unneccesary old backlog relations
      if (oldProject && oldProject !== me.relations.project) {
        oldProject.removeStory(me);
        //LEAF STORIES: moved to another project
        oldProject.reloadStoryRanks();
      }
      
      if (oldBacklog && oldBacklog !== me.relations.backlog) {        
        oldBacklog.removeStory(me);
      }
      
      // moved from assigned iteration
      if (oldIteration && oldIteration !== me.relations.iteration) {
    	oldIteration.removeStory(me);
      }
      
      me.callListeners(new DynamicsEvents.EditEvent(me));
      MessageDisplay.Ok("Story moved");
    },
    error: function(xhr) {
      MessageDisplay.Error("An error occurred moving the story", xhr);
    }
  });
 };

StoryModel.prototype.rankUnder = function(rankUnderId, moveUnder) {
  this._rank("under", rankUnderId, moveUnder);
};

StoryModel.prototype.rankOver = function(rankOverId, moveUnder) {
  this._rank("over", rankOverId, moveUnder);
};

StoryModel.prototype.rankToTop = function(backlog) {
  this._rank("top", 0, backlog);
};

StoryModel.prototype.rankToBottom = function(backlog) {
  this._rank("bottom", 0, backlog);
};

StoryModel.prototype._rank = function(direction, targetStoryId, targetBacklog) {
  var me = this;
  var postData = {
    "storyId": me.getId(),
    "targetStoryId": targetStoryId
  };
  
  if ((targetBacklog && targetBacklog != this.getParent()) || direction === "top" || direction === "bottom" ) {
    postData.backlogId = targetBacklog.getId();
  }
  
  var urls = {
    "over": "ajax/rankStoryOver.action",
    "under": "ajax/rankStoryUnder.action",
    "top":  "ajax/rankStoryToTop.action",
    "bottom":  "ajax/rankStoryToBottom.action"
  };
  
  jQuery.ajax({
    url: urls[direction],
    type: "post",
    dataType: "json",
    data: postData,
    async: true,
    cache: false,
    success: function(data, status) {
      MessageDisplay.Ok("Story ranked");
      var oldParent = me.getParent();
      if (!oldParent) {
        oldParent = me.getIteration();
      }
      me.setData(data);
      //and again hack!
      if(me.relations.project) {
        //the story is being ranked in the project context in which the stories may have different parent backlogs
        me.relations.project.callListeners(new DynamicsEvents.RankChanged(me.relations.project,"story"));
      } else {
        oldParent.callListeners(new DynamicsEvents.RankChanged(oldParent,"story"));
        if (oldParent !== targetBacklog) {
          targetBacklog.callListeners(new DynamicsEvents.RankChanged(targetBacklog,"story"));
        }
      }
    },
    error: function(xhr) {
      MessageDisplay.Error("An error occurred while ranking the story", xhr);
    }
  });
};



StoryModel.prototype._remove = function(successCallback, extraData) {
  var me = this;
  var data = {
      storyId: me.getId()
  };
  jQuery.extend(data, extraData);
  jQuery.ajax({
      type: "POST",
      dataType: "text",
      url: "ajax/deleteStory.action",
      data: data,
      async: true,
      cache: false,
      success: function(data, status) {
        MessageDisplay.Ok("Story removed");
        if (successCallback) {
          successCallback();
        }
      },
      error: function(data, status) {
        MessageDisplay.Error("Error deleting story.", data);
      }
  });
};

StoryModel.prototype.addTask = function(task) {
  this.addRelation(task);
  this.callListeners(new DynamicsEvents.RelationUpdatedEvent(this,"task"));
};



// Getters and setters in property alphabetical order
StoryModel.prototype.getBacklog = function() {
  if (this.currentData.backlog) {
    return ModelFactory.getObject(ModelFactory.types.backlog, this.currentData.backlog);
  }
  //hack! in case of leaf stories story may have multiple backlogs
  if(this.relations.project && !this.relations.backlog) {
    return this.relations.project;
  }
  return this.relations.backlog;
};

StoryModel.prototype.getIteration = function() {
  if (this.currentData.iteration) {
    return ModelFactory.getObject(ModelFactory.types.iteration, this.currentData.iteration);
  }
  return this.relations.iteration;
};

StoryModel.prototype.setBacklog = function(backlog) {
  this.addRelation(backlog);
};

StoryModel.prototype.setIteration = function(backlog) {
  this.addRelation(backlog);
};


StoryModel.prototype.setBacklogByModel = function(backlog) {
  this.currentData.backlog = backlog.getId();
};

StoryModel.prototype.setIterationByModel = function(iteration) {
  this.currentData.iteration = iteration.getId();
};

StoryModel.prototype.getDescription = function() {
  return this.currentData.description;
};
StoryModel.prototype.setDescription = function(description) {
  this.currentData.description = description;
};

StoryModel.prototype.getName = function() {
  return this.currentData.name;
};
StoryModel.prototype.setName = function(name) {
  this.currentData.name = name;
};

StoryModel.prototype.getParent = function() {
	if(this.getIteration())
		return this.getIteration();
	else
		return this.getBacklog();
};


StoryModel.prototype.getRank = function() {
  return this.currentData.rank;
};
StoryModel.prototype.setRank = function(newRank) {
  this.currentData.rank = newRank;
};


StoryModel.prototype.getResponsibles = function() {
  if (this.currentData.userIds) {
    var users = [];
    $.each(this.currentData.userIds, function(k, id) {
      users.push(ModelFactory.getObject(ModelFactory.types.user, id));
    });
    return users;
  }
  return this.relations.user;
};
StoryModel.prototype.setResponsibles = function(userIds, userJson) {
  if (userJson) {
    $.each(userJson, function(k,v) {
      ModelFactory.updateObject(v);    
    });
  }
  this.currentData.userIds = userIds;
  this.currentData.usersChanged = true;
};


StoryModel.prototype.getState = function() {
  return this.currentData.state;
};
StoryModel.prototype.setState = function(state) {
  this.currentData.state = state;
};


StoryModel.prototype.getStoryPoints = function() {
  return this.currentData.storyPoints;
};
StoryModel.prototype.setStoryPoints = function(storyPoints) {
  this.currentData.storyPoints = storyPoints;
};


StoryModel.prototype.getStoryValue = function() {
  return this.currentData.storyValue;
};
StoryModel.prototype.setStoryValue = function(storyValue) {
  this.currentData.storyValue = storyValue;
};


StoryModel.prototype.getTasks = function() {
  return this.relations.task;
};

StoryModel.prototype.getChildren = function() {
  return this.relations.story;
};
StoryModel.prototype.getParentStory = function() {
  return this.relations.parent;
};
StoryModel.prototype.getParentStoryName = function() {
  var parentName = "";
  if(this.relations.parent) {
    parentName = this.relations.parent.getName();
  }
  return parentName;
};

StoryModel.prototype.getTotalEffortSpent = function() {
  return this.metrics.effortSpent;
};
StoryModel.prototype.getTotalEffortLeft = function() {
  return this.metrics.effortLeft;
};
StoryModel.prototype.getTotalOriginalEstimate = function() {
  return this.metrics.originalEstimate;
};

StoryModel.prototype.getLabels = function() {
  return this.relations.label;
};

StoryModel.prototype.setLabels = function(labels) {
  this.currentData.labels = labels;
};


