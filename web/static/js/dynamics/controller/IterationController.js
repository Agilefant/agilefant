/**
 * Iteration controller.
 * 
 * @constructor
 * @base BacklogController
 * @param {Integer} id Iteration id.
 * @param {DOMElement} element DOM parent node for the story table. 
 */
var IterationController = function IterationController(options) {
  this.id = options.id;
  this.storyListElement = options.storyListElement;
  this.iterationInfoElement = options.backlogDetailElement;
  this.assigmentListElement = options.assigmentListElement;
  this.taskListElement = options.taskListElement;
  this.hourEntryListElement = options.hourEntryListElement;
  this.metricsElement = options.metricsElement;
  this.smallBurndownElement = options.smallBurndownElement;
  this.burndownElement = options.burndownElement;
  this.tabs = options.tabs;
  this.historyElement = options.historyElement;
  this.init();
  
  this.initAssigneeConfiguration();
  this.initIterationInfoConfig();
  
  this.initialize();
  
  var me = this;
  this.tabs.bind('tabsselect', function(event, ui) {
    if(ui.index === 1) {
      me.selectAssigneesTab();
    } //else if(ui.index === 2) {
     //me.historyElement.load("ajax/iterationHistory.action",{iterationId: me.id});
   //}
  });
  window.pageController.setMainController(this);
};
IterationController.columnNames =
  ["name","reference","startDate","endDate","plannedSize","baselineLoad","assignees","description"];
IterationController.columnIndices = CommonController.createColumnIndices(IterationController.columnNames); 
  
IterationController.columnConfigs = {
  name: {
    title : "Name",
    get : IterationModel.prototype.getName,
    editable : true,
    edit : {
      editor : "Text",
      required: true,
      set: IterationModel.prototype.setName
    }
  },
  reference: {
    title: "Reference ID",
    get: BacklogModel.prototype.getId,
    decorator: DynamicsDecorators.quickReference
  },
  startDate: {
    title : "Start Date",
    get : IterationModel.prototype.getStartDate,
    decorator: DynamicsDecorators.dateTimeDecorator,
    editable : true,
    edit : {
      editor : "Date",
      size: '18ex',
      decorator: DynamicsDecorators.dateTimeDecorator,
      required: true,
      withTime: true,
      set: IterationModel.prototype.setStartDate
    }
  },
  endDate: {
    title : "End Date",
    get : IterationModel.prototype.getEndDate,
    decorator: DynamicsDecorators.dateTimeDecorator,
    editable : true,
    edit : {
      editor : "Date",
      decorator: DynamicsDecorators.dateTimeDecorator,
      required: true,
      withTime: true,
      size: '18ex',
      set: IterationModel.prototype.setEndDate
    }
  },
  plannedSize: {
    title : "Planned Size",
    get : IterationModel.prototype.getBacklogSize,
    decorator: DynamicsDecorators.exactEstimateAppendManHourDecorator,
    editable: true,
    edit: {
      editor: "ExactEstimate",
      decorator: DynamicsDecorators.exactEstimateEditDecorator,
      size: '10ex',
      set : IterationModel.prototype.setBacklogSize
    }
  },
  baselineLoad: {
    title : "Baseline load",
    get : IterationModel.prototype.getBaselineLoad,
    decorator: DynamicsDecorators.exactEstimateDecorator,
    editable: true,
    edit: {
      editor: "ExactEstimate",
      decorator: DynamicsDecorators.exactEstimateEditDecorator,
      size: '10ex',
      set : IterationModel.prototype.setBaselineLoad
    }
  },
  assignees: {
    title : "Assignees",
    headerTooltip : 'Project assignees',
    get : BacklogModel.prototype.getAssignees,
    decorator: DynamicsDecorators.responsiblesDecorator,
    editable: true,
    openOnRowEdit: false,
    edit: {
      editor : "Autocomplete",
      dialogTitle: "Select users",
      dataType: "usersAndTeams", 
      set : BacklogModel.prototype.setAssignees
    }
  },
  description: {
    title : "Description",
    get : IterationModel.prototype.getDescription,
    editable : true,
    decorator: DynamicsDecorators.emptyDescriptionDecorator,
    edit : {
      editor : "Wysiwyg",
      set: IterationModel.prototype.setDescription
    }
  }
 };


IterationController.prototype = new BacklogController();


IterationController.prototype.removeIteration = function() {
  var me = this;
  var dialog = new LazyLoadedFormDialog();
  dialog.init({
    title: "Delete iteration",
    url: "ajax/deleteIterationForm.action",
    disableClose: true,
    data: {
      IterationId: me.model.getId()
    },
    okCallback: function(extraData) {
      var confirmation = extraData.confirmationString;
      if (confirmation && confirmation.toLowerCase() == 'yes') {
        window.location.href = "deleteIteration.action?confirmationString=yes&iterationId=" + me.model.getId();
      }
    },
    closeCallback: function() {
      dialog.close();
    }
  });
};

IterationController.prototype.shareIteration = function() {
	var me = this;
	var dialog = new LazyLoadedFormDialog();
	var token = me.model.getReadonlyToken();
	
	if (!token) {
		jQuery.ajax({
		    type: "POST",
		    url: "ajax/createReadonlyToken.action",
		    async: false,
		    cache: false,
		    data: {IterationId: me.model.getId()},
		    dataType: "json",
		    success: function(data, status) {
		      dialog.init({
					title: "Share Iteration",
					url: "ajax/shareIterationForm.action",
					data: {
						IterationId: me.model.getId(),
						ReadonlyToken: token
					}
				});
		      ModelFactory.updateObject(data);
		    },
		    error: function(xhr, status, error) {
		      MessageDisplay.Error("Error creating read only link", xhr);
		      me.rollback();
		    }
		  });
	} else {
		dialog.init({
			title: "Share Iteration",
			url: "ajax/shareIterationForm.action",
			data: {
				IterationId: me.model.getId(),
				ReadonlyToken: token
			}
		});
	}
};

IterationController.prototype.unshareIteration = function() {
	var me = this;
	var dialog = new LazyLoadedFormDialog();
	var token = me.model.getReadonlyToken();	
	
	if (!token) {
		MessageDisplay.Warning("Iteration currently does not have read only URL.");
	} else {
	
		  dialog.init({
		    title: "Unshare iteration",
		    url: "ajax/unshareIterationForm.action",
		    data: {
		      IterationId: me.model.getId()
		    },
		    okCallback: function(extraData) {
				jQuery.ajax({
				    type: "POST",
				    url: "ajax/clearReadonlyToken.action",
				    async: false,
				    cache: false,
				    data: {IterationId: me.model.getId()},
				    dataType: "json",
				    success: function(data, status) {
				      MessageDisplay.Ok("Share link removed");
				      me.model.setReadonlyToken(null);
				    },
				    error: function(xhr, status, error) {
				      MessageDisplay.Error("Error unsharing read only link", xhr);
				      me.rollback();
				    }
				  });
		    },
		    closeCallback: function() {
		      dialog.close();
		    }
		  });
	}
};

/** override backlog controller base class to reload metrics box **/
IterationController.prototype.openLogEffort = function() {
  var widget = new SpentEffortWidget(this.model, jQuery.proxy(function() {
    this.reloadMetricsBox();
  }, this));
};


IterationController.prototype.pageControllerDispatch = function(event) {
  if(event instanceof DynamicsEvents.AddEvent) {
    //new task is added to user's tasks without story
    if (event.getObject() instanceof TaskModel || event.getObject() instanceof StoryModel) {
      this.reloadMetrics();
    }
  }
};


IterationController.prototype.handleModelEvents = function(event) {
  if (event instanceof DynamicsEvents.MetricsEvent 
      || event instanceof DynamicsEvents.RelationUpdatedEvent) {
    if(event.getObject() instanceof TaskModel) {
      this.reloadMetrics();
    }
    else if(event.getObject() instanceof StoryModel) {
      this.reloadMetricsBox();
    }
    else if(event instanceof DynamicsEvents.RelationUpdatedEvent && event.getObject() instanceof IterationModel && event.getRelation() === "story") {
      this.reloadMetricsBox();
    }
  }

};
IterationController.prototype.isAssigneesTabSelected = function() {
  return (this.tabs.tabs("option","selected") === 1);
};

IterationController.prototype.paintIterationInfo = function() {
  this.iterationInfoView = new DynamicVerticalTable(this, this.model, this.iterationDetailConfig, this.iterationInfoElement);
  this.iterationInfoView.render();
};

IterationController.prototype.reloadBurndown = function() {
  var href = this.burndownElement.attr("src");
  this.burndownElement.attr("src", href+"#");
  href = this.smallBurndownElement.attr("src");
  this.smallBurndownElement.attr("src", href+"#");
};
IterationController.prototype.reloadMetricsBox = function() {
  this.metricsElement.load("ajax/iterationMetrics.action", {iterationId: this.id});
  this.reloadBurndown();
  document.body.style.cursor = "default";
};

IterationController.prototype.reloadMetrics = function() {
  this.reloadBurndown();
  this.reloadMetricsBox();
  if(this.isAssigneesTabSelected()) {
    this.selectAssigneesTab();
  }
};


IterationController.prototype.initializeStoryList = function() {
  this.storyListController = new StoryListController(this.model,
      this.storyListElement, this);
};

IterationController.prototype.initializeTaskList = function() {
  this.tasksWithoutStoryController = new TasksWithoutStoryController(
      this.model, this.taskListElement, this);
};
/**
 * Initialize and render the story list.
 */
IterationController.prototype.initialize = function() {
  var me = this;
  ModelFactory.initializeFor(ModelFactory.initializeForTypes.iteration,
      this.id, function(model) {
        me.model = model;
        me.attachModelListener();
        me.paintIterationInfo();
        me.initializeStoryList();
        me.initializeTaskList();
      });
  this.assigneeContainer = new AssignmentContainer(this.id);
  this.assigneeListView = new DynamicTable(this, this.assigneeContainer, this.assigneeListConfiguration,
      this.assigmentListElement);
};

IterationController.prototype.selectAssigneesTab = function() {
  this.assigneeContainer.reload();
};






IterationController.prototype.initIterationInfoConfig = function() {
  var config = new DynamicTableConfiguration( {
    leftWidth: '20%',
    rightWidth: '79%',
    closeRowCallback: null,
    validators: [ BacklogModel.Validators.dateValidator ]
  });
  config.addColumnConfiguration(IterationController.columnIndices.name, IterationController.columnConfigs.name);
  config.addColumnConfiguration(IterationController.columnIndices.reference, IterationController.columnConfigs.reference);
  config.addColumnConfiguration(IterationController.columnIndices.startDate, IterationController.columnConfigs.startDate);  
  config.addColumnConfiguration(IterationController.columnIndices.endDate, IterationController.columnConfigs.endDate);
  config.addColumnConfiguration(IterationController.columnIndices.plannedSize, IterationController.columnConfigs.plannedSize);
  config.addColumnConfiguration(IterationController.columnIndices.baselineLoad, IterationController.columnConfigs.baselineLoad);
  config.addColumnConfiguration(IterationController.columnIndices.assignees, IterationController.columnConfigs.assignees);
  config.addColumnConfiguration(IterationController.columnIndices.description, IterationController.columnConfigs.description);
  this.iterationDetailConfig = config;
};

IterationController.prototype.initAssigneeConfiguration = function() {
  var config = new DynamicTableConfiguration(
      {
        rowControllerFactory : BacklogController.prototype.assignmentControllerFactory,
        dataSource : AssignmentContainer.prototype.getAssignments,
        caption : "Iteration workload by user"
      }); 
  config.addColumnConfiguration(0, {
    minWidth : 150,
    autoScale : true,
    title : "User",
    get : AssignmentModel.prototype.getUser,
    defaultSortColumn: true,
    sortCallback: DynamicsComparators.valueComparatorFactory(AssignmentModel.prototype.getId),
    decorator: DynamicsDecorators.conditionColorDecorator(
        AssignmentModel.prototype.isUnassigned, 
        function(v) { if(v) { return 'red'; } }, 
        DynamicsDecorators.userNameDecorator)
  });
  
  config.addColumnConfiguration(1, {
    minWidth : 100,
    autoScale : true,
    title : "Iteration availability",
    get : AssignmentModel.prototype.getAvailability,
    decorator: DynamicsDecorators.appendDecoratorFactory("%"),
    editableCallback: AssignmentController.prototype.canEdit,
    editable: true,
    edit: {
      editor: "Number",
      minVal: 0,
      maxVal: 100,
      set: AssignmentModel.prototype.setAvailability
    }
  });
  config.addColumnConfiguration(2, {
    minWidth : 100,
    autoScale : true,
    title : "Personal adjustment",
    get : AssignmentModel.prototype.getPersonalLoad,
    decorator: DynamicsDecorators.exactEstimateDecorator,
    editable: true,
    edit: {
      editor: "ExactEstimate",
      acceptNegative: true,
      set: AssignmentModel.prototype.setPersonalLoad,
      decorator: DynamicsDecorators.exactEstimateEditDecorator
    }
  });
  config.addColumnConfiguration(3, {
    minWidth : 100,
    autoScale : true,
    title : "Assigned effort sum",
    get : AssignmentModel.prototype.getAssignedLoad,
    decorator: DynamicsDecorators.exactEstimateEditDecorator
  });
  config.addColumnConfiguration(4, {
    minWidth : 100,
    autoScale : true,
    title : "Unassigned effort sum",
    get : AssignmentModel.prototype.getUnassignedLoad,
    decorator: DynamicsDecorators.exactEstimateEditDecorator
  });
  /*
  config.addColumnConfiguration(5, {
    minWidth : 100,
    autoScale : true,
    title : "Total",
    get : AssignmentModel.prototype.getTotalLoad,
    decorator: DynamicsDecorators.exactEstimateEditDecorator,
    sortCallback: DynamicsComparators.valueComparatorFactory(AssignmentModel.prototype.getTotalLoad)
  });
  config.addColumnConfiguration(6, {
    minWidth : 100,
    autoScale : true,
    title : "Worktime",
    get : AssignmentModel.prototype.getAvailableWorktime,
    decorator: DynamicsDecorators.exactEstimateEditDecorator,
    sortCallback: DynamicsComparators.valueComparatorFactory(AssignmentModel.prototype.getAvailableWorktime)
  });
  config.addColumnConfiguration(7, {
    minWidth : 100,
    autoScale : true,
    title : "Load",
    get : AssignmentModel.prototype.getLoadPercentage,
    decorator: DynamicsDecorators.appendDecoratorFactory("%"),
    sortCallback: DynamicsComparators.valueComparatorFactory(AssignmentModel.prototype.getLoadPercentage)
  });
  */
  this.assigneeListConfiguration = config;
};
