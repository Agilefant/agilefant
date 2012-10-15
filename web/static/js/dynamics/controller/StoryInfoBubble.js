/**
 * Story info bubble for tree
 * 
 * @constructor
 * @base CommonController
 * @param {int} id the story's id
 * @param {DOMElement} storyElement the story's row in the tree
 */
var StoryInfoBubble = function StoryInfoBubble(id, treeController, storyElement, options) {
  this.id = id;
  this.treeController = treeController;
  this.storyElement = storyElement;
  this.init();
};

extendObject(StoryInfoBubble, StoryController);

/**
 * Initialize the info bubble.
 */
StoryInfoBubble.prototype.init = function() {
  // Check that id exists
  if (!this.id) {
    return;
  }
  this.createBubble();
//  this.addLinks();
  this.populateContent();
};

StoryInfoBubble.prototype.checkForMoveStory = function(model) {
  if(model.currentData.backlog || model.currentData.iteration) {
	  if (model.currentData.backlog) {
	    this._openMoveStoryDialog(model, model.currentData.backlog);
	  } else {
	    this._openMoveStoryDialog(model, model.currentData.iteration);
	  }
    var me = this;
    //ensure that dialog is open
    setTimeout(function() {
      // backlog changed
      if (model.currentData.backlog && !model.currentData.iteration) {
        if(model.canMoveStory(model.currentData.backlog)) {
          me._closeMoveDialog();
          model.commit();
        }
      // iteration changed
      } else if (!model.currentData.backlog && model.currentData.iteration) {
        if(model.canMoveStory(model.currentData.iteration)) {
          me._closeMoveDialog();
          model.commit();
        }
      }
    }, 200);
  } else {
    model.commit();
  }
};

StoryInfoBubble.prototype.confirmTasksAndChildrenToDone = function(model, storyTree, isTopStory) {
	var tasks = model.getTasks();
	var children = model.getChildren();
	var changedData = model.getChangedData();

	if (changedData.state !== "DONE") {
	  return;
	}	
	
	var nonDoneChildren = false;
	var nonDoneTasks = false;
	if (children.length > 0) {
		for (var i = 0; i < children.length; i++) {
		  if (children[i].getState() !== "DONE") {
			nonDoneChildren = true;
		  }
		}
	}
	if (tasks.length > 0) {
		for (var i = 0; i < tasks.length; i++) {
		  if (tasks[i].getState() !== "DONE") {
			nonDoneTasks = true;
		  }
		}
	}
	if (nonDoneChildren || nonDoneTasks) {
	  if (isTopStory) {
		if (changedData.state && changedData.state === "DONE" && tasks.length > 0) {
		  var msg = new DynamicsConfirmationDialog(
			  "Set all tasks' and stories' states to done?",
			  "The '" + model.getName() + "' story has undone child tasks/stories! Do you want to set them Done as well?",
			  function() {
				for (var i = 0; i < children.length; i++) {
				  if (children[i].getState() !== "DONE") {
					 children[i].setState("DONE");
					 children[i].commit();
				  }					 storyTree._getStoryForId(children[i].getId(), function(object) {
						StoryInfoBubble.prototype.confirmTasksAndChildrenToDone(object, storyTree, false);
					});
				}
				if (nonDoneTasks)
					model.currentData.tasksToDone = true;
				model.commit();
			  },
			  function() {
				model.commit();
			  }
			);
		} else {
			model.commit();
		}
	} else {
			for (var i = 0; i < children.length; i++) {
				if (children[i].getState() !== "DONE") {
					children[i].setState("DONE");
					children[i].commit();
					storyTree._getStoryForId(children[i].getId(), function(object) {
						StoryInfoBubble.prototype.confirmTasksAndChildrenToDone(object, storyTree, false);
					});
				}
			}
			if (nonDoneTasks)
				model.currentData.tasksToDone = true;
			model.commit();
		}
	}
	if (!nonDoneChildren && (!isTopStory))		storyTree.refresh(); // this ensures refreshal when a child story is marked as done};
};

StoryInfoBubble.prototype.handleModelEvents = function(event) {
  StoryController.prototype.handleModelEvents.call(this, event);
  if(event instanceof DynamicsEvents.NamedEvent && event.getEventName() === "storyMoved") {
    this.treeController.refresh();
    this.bubble.destroy();
  }
  
  else if (Configuration.getBranchMetricsType() !== 'off' && event instanceof DynamicsEvents.MetricsEvent && event.getObject().getId() == this.id) {
    this.refreshBranchMetrics();
  }
};

StoryInfoBubble.prototype.createBubble = function() {
  var me = this;
  this.bubble = new Bubble(this.storyElement, {
    closeCallback: function() {
      me.treeController.refreshNode(me.storyElement);
      Bubble.closeAll();
    },
    title: "Story info"
  });
  this.element = this.bubble.getElement();
};

StoryInfoBubble.prototype.populateContent = function() {
  var me = this;
  
  // Initialize config
  this.storyInfoConfig = this._createConfig();
  
  // Add the content
  this.storyInfoElement = $('<div style="clear: both;"/>').appendTo(this.element);
  
  $('<div style="width: 100%; text-align:center;"><span><img src="static/img/pleasewait.gif" /></span></div>').appendTo(this.storyInfoElement);
  var sid = this.id;
  this.treeController._getStoryForId(this.id, function(object) {
    me.model = object;
    me.attachModelListener();
    me.storyInfoElement.html('');
    me.storyInfoView = new DynamicVerticalTable(me, me.model, me.storyInfoConfig, me.storyInfoElement);
    me.storyInfoView.render();
    me.addLinks();
    $.post("ajax/storyViewed.action", {storyId: sid});
  });
  
  if (Configuration.getBranchMetricsType() !== 'off') {
    this.branchMetricsElement = $('<div style="clear: both;"></div>').appendTo(this.element);
    this.refreshBranchMetrics();
  }
};

StoryInfoBubble.prototype.refreshBranchMetrics = function() {
  if (!this.branchContent) {
    this.branchContent = $('<div/>').appendTo(this.branchMetricsElement);
  }
  
  this.branchContent.html('<div style="text-align:center;"><img src="static/img/working.gif" alt="Please wait..." style="display: inline-block;"/></div>')
    .load('ajax/retrieveBranchMetrics.action?storyId=' + this.id);
};


/**
 * Add the action links to bubble.
 */
StoryInfoBubble.prototype.addLinks = function() {
  var me = this;
  
  var links = $('<div style="height: 1.5em;" />').addClass('details-links');
  this.element.find('.close-button').after(links);

  $('<a>spent effort</a>').click(function() {
    me.bubble.destroy();
    me.openLogEffort();
  }).appendTo(links);

  $('<a>add child</a>').click(function() {
    me.bubble.destroy();
    me.treeController.createNode(me.storyElement,"inside", me.model);
  }).appendTo(links);
  
  $('<a>add sibling</a>').click(function() {
    me.bubble.destroy();
    me.treeController.createNode(me.storyElement,"after", me.model);
  }).appendTo(links);
  
  $('<a>delete</a>').click(function() {
    me.bubble.destroy();
    me.treeController._getStoryForId(me.id, function(storyModel) {
      storyModel.addListener(function(evt) {
        if(evt instanceof DynamicsEvents.DeleteEvent) {
          me.treeController.removeNode(me.storyElement);
        }
      });
      var controller = new StoryController(storyModel, null, null);
      controller.removeStory();
    });
  }).appendTo(links);

};


/**
 * Create the configuration for the dynamic table.
 */
StoryInfoBubble.prototype._createConfig = function() {
  var checkDoneAndMovedFunction = function(model) {
    StoryInfoBubble.prototype.confirmTasksAndChildrenToDone (model, this.treeController, true);
	StoryInfoBubble.prototype.checkForMoveStory(model);
  };
  var config = new DynamicTableConfiguration( {
    leftWidth: '28%',
    rightWidth: '71%',
    closeRowCallback: null,
	beforeCommitFunction: checkDoneAndMovedFunction,
    validators: [ ]
  });
  config.addColumnConfiguration(0, {
    title : "Name",
    get : StoryModel.prototype.getName,
    editable : true,
    edit : {
      editor : "Text",
      required: true,
      set: StoryModel.prototype.setName
    }
  });
    config.addColumnConfiguration(1, {
	    title : "Value",
	    get : StoryModel.prototype.getStoryValue,
	    decorator: DynamicsDecorators.estimateDecorator,
	    editable : true,
	    edit : {
	      editor : "Number",
	      set : StoryModel.prototype.setStoryValue
	    }
	  });
  
  config.addColumnConfiguration(2, {
    title: "Reference ID",
    get: StoryModel.prototype.getId,
    decorator: DynamicsDecorators.linkToWorkItem ,
  });
  config.addColumnConfiguration(3, {
    title : "State",
    get : StoryModel.prototype.getState,
    decorator: DynamicsDecorators.storyStateColorDecorator,
    editable : true,
    edit : {
      editor : "Selection",
      set : StoryModel.prototype.setState,
      items : DynamicsDecorators.stateOptions
    }
  });
  config.addColumnConfiguration(4, {
    title : "Points",
    get : StoryModel.prototype.getStoryPoints,
    decorator: DynamicsDecorators.estimateDecorator,
    editable : true,
    edit : {
      editor : "Number",
      set : StoryModel.prototype.setStoryPoints
    }
  });
  config.addColumnConfiguration(5, {
    title : "Product&nbsp;/&nbsp;Project",
    headerTooltip : 'The backlog, where the story resides',
    get : StoryModel.prototype.getBacklog,
    decorator: DynamicsDecorators.backlogSelectDecorator,
    editable : true,
    edit: {
      editor: "AutocompleteSingle",
      dialogTitle: "Select backlog",
      dataType: "backlogs",
      set: StoryModel.prototype.setBacklogByModel
    }
  });
  config.addColumnConfiguration(6, {
	    title : "Iteration",
	    headerTooltip : 'The iteration where the story has been assigned to',
	    get : StoryModel.prototype.getIteration,
	    decorator: DynamicsDecorators.iterationSelectDecorator,
	    editable : true,
	    edit: {
	      editor: "AutocompleteSingle",
	      dialogTitle: "Select iteration",
	      dataType: "currentIterations",
	      set: StoryModel.prototype.setIterationByModel
	    }
  });
  config.addColumnConfiguration(7, {
    title : "Responsibles",
    get : StoryModel.prototype.getResponsibles,
    decorator: DynamicsDecorators.responsiblesDecorator,
    editable : true,
    edit : {
      editor : "Autocomplete",
      dialogTitle: "Select users",
      dataType: "usersAndTeams",
      set : StoryModel.prototype.setResponsibles
    }
  });
  config.addColumnConfiguration(8, {
    title : "Labels",
    subViewFactory: StoryInfoBubble.prototype.labelsViewFactory
  });
  config.addColumnConfiguration(9, {
    title : "Description",
    get : StoryModel.prototype.getDescription,
    editable : true,
    decorator: StoryInfoBubble.prototype.descriptionDecorator,
    edit : {
      editor : "Wysiwyg",
      set : StoryModel.prototype.setDescription
    }
  });
  return config;
};

StoryInfoBubble.prototype.descriptionDecorator = function(value) {
  if (!value) {
    return DynamicsDecorators.emptyDescriptionDecorator();
  }
  return '<div style="max-height: 20em; overflow: auto;">' + value + '</div>';
};

StoryInfoBubble.prototype.labelsViewFactory = function(view, model) {
  return new LabelsView({}, this, model, view); 
};
