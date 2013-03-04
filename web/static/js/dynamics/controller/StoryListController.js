
var StoryListController = function(model, element, parentController) {
  this.model = model;
  this.element = element;
  this.parentController = parentController;
  this.init();
  
  this.initConfig();
  this.initializeView();
};
StoryListController.prototype = new CommonController();

StoryListController.prototype.handleModelEvents = function(event) {
  if(this.parentController) {
    this.parentController.handleModelEvents(event);
  }
  if(event instanceof DynamicsEvents.RankChanged && event.getRankedType() === "story") {
    var me = this;
    this.model.reloadStoryRanks(function() {
      me.getCurrentView().resort();
    });
  }
};

StoryListController.prototype.initializeView = function() {
  this.view = new DynamicTable(this, this.model, this.getCurrentConfig(),
      this.element);
  this.view.render();
};

StoryListController.prototype.getCurrentConfig = function() {
  return this.storyListConfig;
};

StoryListController.prototype.initConfig = function() {
  this.storyListConfig = this._getTableConfig();
  this._addColumnConfigs(this.storyListConfig);
};


StoryListController.prototype.filterStoriesByState = function(element) {
  var me = this;
  var filterFunc = function(story) {
    return (!me.stateFilters || jQuery.inArray(story.getState(), me.stateFilters) !== -1);
  };
  
  var widget = new StateFilterWidget(element, {
   bubbleOptions: {
     title: "Filter by state",
     offsetX: -15,
     minWidth: 100,
     minHeight: 20
   },
   callback: function(isActive) {
      me.stateFilters = widget.getFilter();
      if(isActive) {
        me.getCurrentView().activateColumnFilter("State");
        me.getCurrentView().setFilter(filterFunc);
      } else {
        me.getCurrentView().disableColumnFilter("State");
        me.getCurrentView().setFilter(null);
      }
      me.getCurrentView().filter();
    },
    activeStates: me.stateFilters
  });
};



/**
 * Creates a new story controller.
 */
StoryListController.prototype.storyControllerFactory = function(view, model) {
  var storyController = new StoryController(model, view, this);
  this.addChildController("story", storyController);
  return storyController;
};

/**
 * Populate a new, editable story row to the story table. 
 */
StoryListController.prototype.createStory = function() {
  var mockModel = ModelFactory.createObject(ModelFactory.types.story);
  mockModel.setBacklog(this.model);
  
  // Assign user if option is set
  var user = PageController.getInstance().getCurrentUser();
  if (user.isAutoassignToStories()) {
    mockModel.setResponsibles([user.getId()]);
  }
  
  var controller = new StoryController(mockModel, null, this);
  var row = this.getCurrentView().createRow(controller, mockModel, "top");
  controller.view = row;
  row.autoCreateCells([StoryController.columnIndices.priority, StoryController.columnIndices.labels, StoryController.columnIndices.actions, StoryController.columnIndices.tasksData]);
  row.render();
  controller.openRowEdit();
  row.getCellByName("tasksData").hide();
};

StoryListController.prototype.copyStorySibling = function(originalStory) { 
  var mockModel = ModelFactory.createObject(ModelFactory.types.story);
  mockModel.setIteration(this.model);
  mockModel._copyStory(originalStory);
};

/**
 * Show all tasks lists.
 */
StoryListController.prototype.showTasks = function() {
  this.callChildcontrollers("story", StoryController.prototype.showTasks);
};

/**
 * Hide all task lists.
 */
StoryListController.prototype.hideTasks = function() {
  this.callChildcontrollers("story", StoryController.prototype.hideTasks);
};

StoryListController.prototype.confirmTasksToDone = function(model) {
  var changedData = model.getChangedData();
  var tasks = model.getTasks();
  if (changedData.state && changedData.state === "DONE" && tasks.length > 0) {
    var nonDoneTasks = false;
    for (var i = 0; i < tasks.length; i++) {
      if (tasks[i].getState() !== "DONE") {
        nonDoneTasks = true;
      }
    }
    if (nonDoneTasks) {
      var msg = new DynamicsConfirmationDialog(
          "Set all tasks' states to done?",
          "Do you want to mark all tasks as done as well?",
          function() {
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
  }
  else {
    model.commit();
  }
};

StoryListController.prototype.firstRenderComplete = function() {
  if(window.location.hash) {
    var hash = window.location.hash;
    var row = this.view.getRowById(hash.substring(1));
    if(row) {
      if(!$.browser.msie) {
        window.location.hash = "#";
      }
      var controller = row.getController();
      controller.showTasks();
      var pos = row.getElement().offset();
      window.scrollTo(pos.left, pos.top);
    }
    else {
    	var type = "story";
    	var invocationTarget = StoryController.prototype.showTasks
    	if (this.childControllers[type]) {
    		for ( var i = 0; i < this.childControllers[type].length; i++) {
      			invocationTarget.call(this.childControllers[type][i]);
      			StoryController.prototype.searchForTask.call(this.childControllers[type][i]);
      			if(!window.location.hash) {
      				break;
      			}
      			else {
      				StoryController.prototype.hideTasks.call(this.childControllers[type][i]);
      			}
    		}
  		}
    }
  }
};

StoryListController.prototype._getTableConfig = function() {
  var config = new DynamicTableConfiguration( {
    rowControllerFactory : StoryListController.prototype.storyControllerFactory,
    dataSource : IterationModel.prototype.getStories,
    sortCallback: StoryController.prototype.rankStory,
    caption : "Stories",
    dataType: "story",
    captionConfig: {
      cssClasses: "dynamictable-caption-block ui-widget-header ui-corner-all"
    },
    cssClass: "ui-widget-content ui-corner-all iteration-story-table",
    rowDroppable: true,
    dropOptions: {
      callback: TaskController.prototype.moveTask,
      accepts: StoryController.prototype.acceptsDraggable
    },
    beforeCommitFunction: StoryListController.prototype.confirmTasksToDone,
    afterFirstRender: $.proxy(function() {
      this.firstRenderComplete();
    }, this)
  });

  config.addCaptionItem( {
    name : "createStory",
    text : "Create story",
    cssClass : "create",
    callback : StoryListController.prototype.createStory
  });

  config.addCaptionItem( {
    name : "showTasks",
    text : "Show tasks",
    connectWith : "hideTasks",
    cssClass : "hide",
    visible: true,
    callback : StoryListController.prototype.showTasks
  });
  config.addCaptionItem( {
    name : "hideTasks",
    text : "Hide tasks",
    visible : false,
    connectWith : "showTasks",
    cssClass : "show",
    callback : StoryListController.prototype.hideTasks
  });
  return config;
};

StoryListController.prototype._addColumnConfigs = function(config) {
  var a = StoryListController.columnConfig.state;
  config.addColumnConfiguration(StoryController.columnIndices.priority, StoryListController.columnConfig.prio);
  if (Configuration.isLabelsInStoryList()) {
    config.addColumnConfiguration(StoryController.columnIndices.labelsIcon, StoryListController.columnConfig.labelsIcon);
  }
  config.addColumnConfiguration(StoryController.columnIndices.id, StoryListController.columnConfig.id);
  config.addColumnConfiguration(StoryController.columnIndices.name, StoryListController.columnConfig.name);
  config.addColumnConfiguration(StoryController.columnIndices.value, StoryListController.columnConfig.value);
  config.addColumnConfiguration(StoryController.columnIndices.points, StoryListController.columnConfig.points);
  config.addColumnConfiguration(StoryController.columnIndices.state, StoryListController.columnConfig.state);
  config.addColumnConfiguration(StoryController.columnIndices.responsibles, StoryListController.columnConfig.responsibles);
  config.addColumnConfiguration(StoryController.columnIndices.el, StoryListController.columnConfig.effortLeft);
  config.addColumnConfiguration(StoryController.columnIndices.oe, StoryListController.columnConfig.originalEstimate);
  if (Configuration.isTimesheetsEnabled()) {
    config.addColumnConfiguration(StoryController.columnIndices.es, StoryListController.columnConfig.effortSpent);
  }
  config.addColumnConfiguration(StoryController.columnIndices.actions, StoryListController.columnConfig.actions);
  config.addColumnConfiguration(StoryController.columnIndices.labels, StoryListController.columnConfig.labels);
  config.addColumnConfiguration(StoryController.columnIndices.description, StoryListController.columnConfig.description);
  config.addColumnConfiguration(StoryController.columnIndices.buttons, StoryListController.columnConfig.buttons);
  config.addColumnConfiguration(StoryController.columnIndices.details, StoryListController.columnConfig.details);
  config.addColumnConfiguration(StoryController.columnIndices.tasksData, StoryListController.columnConfig.tasks);

};





StoryListController.columnConfig = {};
StoryListController.columnConfig.prio = {
  minWidth : 24,
  autoScale : true,
  title : "#",
  headerTooltip : 'Priority',
  /*get: StoryModel.prototype.getRank,*/
  sortCallback: DynamicsComparators.valueComparatorFactory(StoryModel.prototype.getRank),
  defaultSortColumn: true,
  subViewFactory : StoryController.prototype.taskToggleFactory
};
StoryListController.columnConfig.id = {
  minWidth: 30,
  autoScale: true,
  title: "ID",
  headerTooltip: 'Story ID',
  get: CommonModel.prototype.getId,
  decorator: DynamicsDecorators.storyIdWithHash,
  editable: false
};
StoryListController.columnConfig.labelsIcon = {
  minWidth: 40,
  autoScale: true,
  title: "Labels",
  headerTooltip: "Story's labels",
  subViewFactory : StoryController.prototype.labelsIconFactory
};
StoryListController.columnConfig.labels = {
  columnName: "labels",
  fullWidth : true,
  visible : false,
  get : StoryModel.prototype.getLabels,
//  decorator: DynamicsDecorators.emptyDescriptionDecorator,
  editable : true,
  edit : {
    editor : "Labels",
    showText: true,
    set : StoryModel.prototype.setLabels
  }
};

StoryListController.columnConfig.name = {
  minWidth : 200,
  autoScale : true,
  title : "Name",
  headerTooltip : 'Story name',
  get : StoryModel.prototype.getName,
  sortCallback: DynamicsComparators.valueComparatorFactory(StoryModel.prototype.getName),
//  defaultSortColumn: true,
  editable : true,
  dragHandle: true,
  edit : {
    editor : "Text",
    set : StoryModel.prototype.setName,
    required: true
  }
};

StoryListController.columnConfig.points = {
  minWidth : 50,
  autoScale : true,
  title : "Points",
  headerTooltip : 'Estimate in story points',
  get : StoryModel.prototype.getStoryPoints,
  sortCallback: DynamicsComparators.valueComparatorFactory(StoryModel.prototype.getStoryPoints),
  decorator: DynamicsDecorators.estimateDecorator,
  editable : true,
  editableCallback: StoryController.prototype.storyValueOrPointsEditable,
  edit : {
    editor : "Estimate",
    set : StoryModel.prototype.setStoryPoints
  }
};

StoryListController.columnConfig.value = {
  minWidth : 50,
  autoScale : true,
  title : "Value",
  headerTooltip : 'Give a story value',
  get : StoryModel.prototype.getStoryValue,
  sortCallback: DynamicsComparators.valueComparatorFactory(StoryModel.prototype.getStoryValue),
  decorator: DynamicsDecorators.estimateDecorator,
  editable : true,
  editableCallback: StoryController.prototype.storyValueOrPointsEditable,
  edit : {
    editor : "StoryValue",
    set : StoryModel.prototype.setStoryValue
  }
};

StoryListController.columnConfig.state = {
  minWidth : 70,
  autoScale : true,
  title : "State",
  headerTooltip : 'Story state',
  get : StoryModel.prototype.getState,
  decorator: DynamicsDecorators.storyStateColorDecorator,
  filter: StoryListController.prototype.filterStoriesByState,
  editable : true,
  edit : {
    editor : "Selection",
    set : StoryModel.prototype.setState,
    items : DynamicsDecorators.stateOptions
  }
};
StoryListController.columnConfig.responsibles = {
  minWidth : 60,
  autoScale : true,
  title : "Responsibles",
  headerTooltip : 'Story responsibles',
  get : StoryModel.prototype.getResponsibles,
  decorator: DynamicsDecorators.responsiblesDecorator,
  editable : true,
  openOnRowEdit: false,
  edit : {
    editor : "Autocomplete",
    dialogTitle: "Select users",
    dataType: "usersAndTeams",
    set : StoryModel.prototype.setResponsibles
  }
};
StoryListController.columnConfig.effortLeft = {
  minWidth : 30,
  autoScale : true,
  cssClass : 'sum-column',
  title : "Σ(EL)",
  headerTooltip : "Total sum of stories' tasks' effort left estimates",
  decorator: DynamicsDecorators.exactEstimateSumDecorator,
  get : StoryModel.prototype.getTotalEffortLeft
};
StoryListController.columnConfig.originalEstimate = {
  minWidth : 30,
  autoScale : true,
  cssClass : 'sum-column',
  title : "Σ(OE)",
  headerTooltip : 'Total task original estimate',
  decorator: DynamicsDecorators.exactEstimateSumDecorator,
  get : StoryModel.prototype.getTotalOriginalEstimate
};
StoryListController.columnConfig.effortSpent = {
  minWidth : 30,
  autoScale : true,
  title : "ES",
  headerTooltip : 'Total task spent effort',
  decorator: DynamicsDecorators.exactEstimateDecorator,
  get : StoryModel.prototype.getTotalEffortSpent,
  editable : false,
  onClick: StoryController.prototype.openQuickLogEffort,
  edit : {
    editor : "ExactEstimate",
    decorator: DynamicsDecorators.empty,
    set : StoryController.prototype.quickLogEffort
  }
};
StoryListController.columnConfig.actions = {
  minWidth : 33,
  autoScale : true,
  title : "Edit",
  subViewFactory : StoryController.prototype.storyActionFactory
};
StoryListController.columnConfig.description = {
  columnName: "description",
  fullWidth : true,
  visible : false,
  get : StoryModel.prototype.getDescription,
  decorator: DynamicsDecorators.emptyDescriptionDecorator,
  editable : true,
  edit : {
    editor : "Wysiwyg",
    set : StoryModel.prototype.setDescription
  }
};
StoryListController.columnConfig.buttons = {
  columnName: "buttons",
  fullWidth : true,
  visible : false,
  subViewFactory : DynamicsButtons.commonButtonFactory
};
StoryListController.columnConfig.details = {
  columnName: "details",
  fullWidth : true,
  visible : false,
  targetCell: StoryController.columnIndices.details,
  subViewFactory :  StoryController.prototype.storyDetailsFactory,
  delayedRender: true
};
StoryListController.columnConfig.tasks = {
  columnName: "tasksData",
  fullWidth : true,
  visible : false,
  cssClass : 'story-task-container',
  targetCell: StoryController.columnIndices.tasksData,
  subViewFactory : StoryController.prototype.storyTaskListFactory,
  delayedRender: true
};

