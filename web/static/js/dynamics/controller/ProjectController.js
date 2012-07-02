/**
 * Project controller.
 * 
 * @constructor
 * @base BacklogController
 * @param {Integer}
 *            id Project id.
 * @param {DOMElement}
 *            element DOM parent node for the story table.
 */
var ProjectController = function ProjectController(options) {
	var me = this;
	this.id = options.id;
	this.tabs = options.tabs;
	this.parentView = options.storyListElement;
	this.projectDetailsElement = options.projectDetailsElement;

	if ($.cookie("agilefant-leafstory-filters")) {
		this.leafStoriesStateFilters = $.cookie("agilefant-leafstory-filters")
				.split(",");
	}

	this.textFilterElement = options.textFilterElement;
	this.textFilter = new SearchByTextWidget($(options.textFilterElement), {
		searchCallback : function() {
			me.filter();
		}
	});

	this.init();
	this.initializeProjectDetailsConfig();
	this.initializeIterationListConfig();
	this.initializeStoryConfig();

	this.changeTabIfFragmentFound();

	this.paint();
	window.pageController.setMainController(this);
};
ProjectController.prototype = new BacklogController();

ProjectController.prototype.filter = function() {
  var me = this;
  var activeTab = this.tabs.tabs("option","selected");
  if (activeTab === 1) { // leaf stories
    this.storyListView.showInfoMessage("Searching...");
    this.model.reloadLeafStories(this.getLeafStoryFilters(), function() {
      me.storyListView.hideInfoMessage("Searching...");
    });
  }
  else if (activeTab === 0) { // story tree
    this.storyTreeController.filter(this.getTextFilter(),
        this.getStoryTreeStateFilters());
  }
  else if (activeTab === 2) { // iterations
// Project-Level Auto-Refresh
//    this.iterationsView.showInfoMessage("Loading...");
//    this.model.reloadIterations(null, function() {
//      me.iterationsView.hideInfoMessage("Loading...");
//      //me.iterationsView.render();
//    });
    this.iterationsView.filter();
  }
};

ProjectController.prototype.filterLeafStories = function() {
	this.storyListView.showInfoMessage("Filtering...");
	this.model.reloadLeafStories(this.getLeafStoryFilters(), jQuery.proxy(
			function() {
				this.storyListView.hideInfoMessage("Filtering...");
			}, this));
};

ProjectController.prototype.getLeafStoryFilters = function() {
	var filters = {};
	if (this.getTextFilter()) {
		filters.name = this.getTextFilter();
	}
	if (this.leafStoriesStateFilters) {
		filters.states = this.leafStoriesStateFilters;
	}
	return filters;
};

ProjectController.prototype.getStoryTreeStateFilters = function() {
	return this.storyTreeController.storyFilters.statesToKeep;
};

ProjectController.prototype.getTextFilter = function() {
	return this.textFilter.getValue();
};

ProjectController.prototype.filterLeafStoriesByState = function(element) {
	var me = this;

	var widget = new StateFilterWidget(element, {
		bubbleOptions : {
			title : "Filter by state",
			offsetX : -15,
			minWidth : 100,
			minHeight : 20
		},
		filterCallback : function() {
			me.leafStoriesStateFilters = widget.getFilter();
			me.filterLeafStories();
		},
		callback : function(isActive) {
			if (isActive) {
				me.storyListView.activateColumnFilter("State");
				$.cookie("agilefant-leafstory-filters",
						me.leafStoriesStateFilters.join(","), {
							expires : 31
						});
			} else {
				me.storyListView.disableColumnFilter("State");
				$.cookie("agilefant-leafstory-filters", null);
			}
		},
		activeStates : me.leafStoriesStateFilters
	});
};

/**
 * Indices for column configuration
 * 
 * @member ProjectController
 */
ProjectController.columnNames = [ "name", "reference", "startDate", "endDate",
		"plannedSize", "baselineLoad", "assignees", "description" ];
ProjectController.columnIndices = CommonController
		.createColumnIndices(ProjectController.columnNames);

ProjectController.columnConfigs = {
	name : {
		title : "Name",
		get : ProjectModel.prototype.getName,
		editable : true,
		edit : {
			editor : "Text",
			required : true,
			set : ProjectModel.prototype.setName
		}
	},
	reference : {
		title : "Reference ID",
		get : BacklogModel.prototype.getId,
		decorator : DynamicsDecorators.quickReference
	},
	startDate : {
		title : "Start Date",
		get : ProjectModel.prototype.getStartDate,
		decorator : DynamicsDecorators.dateTimeDecorator,
		editable : true,
		edit : {
			editor : "Date",
			size : '18ex',
			decorator : DynamicsDecorators.dateTimeDecorator,
			required : true,
			withTime : true,
			set : ProjectModel.prototype.setStartDate
		}
	},
	endDate : {
		title : "End Date",
		get : ProjectModel.prototype.getEndDate,
		decorator : DynamicsDecorators.dateTimeDecorator,
		editable : true,
		edit : {
			editor : "Date",
			decorator : DynamicsDecorators.dateTimeDecorator,
			required : true,
			withTime : true,
			size : '18ex',
			set : ProjectModel.prototype.setEndDate
		}
	},
	plannedSize : {
		title : "Planned Size",
		get : ProjectModel.prototype.getBacklogSize,
		decorator : DynamicsDecorators.exactEstimateAppendManHourDecorator,
		editable : true,
		edit : {
			editor : "ExactEstimate",
			decorator : DynamicsDecorators.exactEstimateEditDecorator,
			size : '10ex',
			set : ProjectModel.prototype.setBacklogSize
		}
	},
	baselineLoad : {
		title : "Baseline load",
		get : ProjectModel.prototype.getBaselineLoad,
		decorator : DynamicsDecorators.exactEstimateDecorator,
		editable : true,
		edit : {
			editor : "ExactEstimate",
			decorator : DynamicsDecorators.exactEstimateEditDecorator,
			size : '10ex',
			set : ProjectModel.prototype.setBaselineLoad
		}
	},
	assignees : {
		title : "Assignees",
		headerTooltip : 'Project assignees',
		get : BacklogModel.prototype.getAssignees,
		decorator : DynamicsDecorators.responsiblesDecorator,
		editable : true,
		openOnRowEdit : false,
		edit : {
			editor : "Autocomplete",
			dialogTitle : "Select users",
			dataType : "usersAndTeams",
			set : BacklogModel.prototype.setAssignees
		}
	},
	description : {
		title : "Description",
		get : ProjectModel.prototype.getDescription,
		editable : true,
		decorator : DynamicsDecorators.emptyDescriptionDecorator,
		edit : {
			editor : "Wysiwyg",
			set : ProjectModel.prototype.setDescription
		}
	}
};

ProjectController.prototype.handleModelEvents = function(event) {
	var me = this;
	if (event instanceof DynamicsEvents.RankChanged
			&& event.getRankedType() === "story") {
		this.model.reloadLeafStories(this.getLeafStoryFilters(), function() {
			me.storyListView.resort();
		});
	}
};
ProjectController.prototype._paintLeafStories = function(element) {
	var me = this;
	if (!this.storyListView) {
		this.storyListView = new DynamicTable(this, this.model,
				this.storyListConfig, element);
		if (this.leafStoriesStateFilters) {
			this.storyListView.activateColumnFilter("State");
		}
	}
	this.storyListView.showInfoMessage("Loading...");
	this.model.reloadLeafStories(this.getLeafStoryFilters(), function() {
		me.storyListView.hideInfoMessage("Loading...");
	});
};

ProjectController.prototype._paintIterations = function(element) {
	var me = this;
	if (!this.iterationsView) {
		this.iterationFilters = [ "ONGOING", "FUTURE" ];
		$(
				'<span>Show <input type="checkbox" name="ONGOING" checked="checked"/>'
						+ 'Ongoing <input type="checkbox" name="FUTURE" checked="checked"/> '
						+ 'Future <input type="checkbox" name="PAST" /> '
						+ 'Past iterations </span>')
				.appendTo(element)
				.find("input")
				.click(
						function(event) {
							var el = $(this);
							var type = el.attr("name");
							if (el.is(":checked")
									&& jQuery
											.inArray(me.iterationFilters, type) === -1) {
								me.iterationFilters.push(type);
							} else {
								ArrayUtils.remove(me.iterationFilters, type);
							}
							me.iterationsView.filter();
						});
		this.iterationsView = new DynamicTable(this, this.model,
				this.iterationListConfig, element);
		this.iterationsView
				.setFilter(function(iterationObj) {
					if (iterationObj.getScheduleStatus()
							&& jQuery.inArray(iterationObj.getScheduleStatus(),
									me.iterationFilters) === -1) {
						return false;
					}
					if (me.getTextFilter().length > 0
							&& iterationObj.getName().indexOf(
									me.getTextFilter()) === -1) {
						return false;
					}
					return true;
				});
	}
	this.iterationsView.showInfoMessage("Loading...");
	this.model.reloadIterations(null, function() {
		me.iterationsView.hideInfoMessage("Loading...");
		me.iterationsView.render();
	});
};

ProjectController.prototype._paintStoryTree = function(element) {
	if (!this.storyTreeController) {
		this.storyTreeController = new StoryTreeController(this.id, "project",
				element, {}, this);
	}
	this.storyTreeController.refresh();

};

ProjectController.prototype.paint = function() {
  var me = this;
  var selectedTab = this.tabs.tabs("option","selected");
  var tmpSel = (selectedTab === 2) ? 0 : 2;
  this.tabs.tabs("select", tmpSel);
  this.tabs.bind("tabsselect",function(event, ui){
    me.textFilter.clear();
    if(me.storyTreeController) {
      me.storyTreeController.resetFilter();
    }
    if(ui.index === 1) { //leaf stories
      me._paintLeafStories(ui.panel);
    } else if(ui.index === 0) { //Story tree
      me._paintStoryTree(ui.panel);
    } else if(ui.index === 2) { //iteration list
      me._paintIterations(ui.panel);
    }
  });
  ModelFactory.getInstance()._getData(ModelFactory.initializeForTypes.project,
      this.id, function(model) {
// Project-Level Auto-Refresh
//        ModelFactory.callEvery(30000,
//          function() {
//            model.reload(); // reload the base page and iteration tab
//            
//            me.filter(); // reload the tab content (story tree, leaf stories)
//          }
//        );
        me.model = model;
        me.attachModelListener();
        me.paintProjectDetails();
        me.tabs.tabs("select", selectedTab);
      });
};

ProjectController.prototype.removeProject = function() {
	var me = this;
	var dialog = new LazyLoadedFormDialog();
	dialog
			.init({
				title : "Delete project",
				url : "ajax/deleteProjectForm.action",
				disableClose : true,
				data : {
					ProjectId : me.model.getId()
				},
				okCallback : function(extraData) {
					var confirmation = extraData.confirmationString;
					if (confirmation && confirmation.toLowerCase() == 'yes') {
						confirmation = extraData.confirmationString;
						if (confirmation && confirmation.toLowerCase() == 'yes') {
							window.location.href = "deleteProject.action?confirmationString=yes&projectId="
									+ me.model.getId();
						}
					}
				},
				closeCallback : function() {
					dialog.close();
				}
			});
};

ProjectController.prototype.paintProjectDetails = function() {
	this.projectDetailsView = new DynamicVerticalTable(this, this.model,
			this.projectDetailConfig, this.projectDetailsElement);
	this.projectDetailsView.render();
};

ProjectController.prototype.iterationRowControllerFactory = function(view,
		model) {
	var iterationController = new IterationRowController(model, view, this);
	this.addChildController("iteration", iterationController);
	return iterationController;
};

/**
 * Initialize and render the story list.
 */

/**
 * Populate a new, editable iteration row to the iterations table.
 */
ProjectController.prototype.createIteration = function() {
	var mockModel = ModelFactory
			.createObject(ModelFactory.typeToClassName.iteration);
	mockModel.setParent(this.model);
	
    // Startdate: today at 8:00am
	// Enddate: 2 weeks from now at 6:00pm
	var startdate = new Date();
	var enddate   = new Date();
	  
	startdate.zeroTime();
	startdate.addHours(8);
	enddate.zeroTime();
	enddate.addDays(14);
	enddate.addHours(18);
	
	mockModel.setStartDate(startdate.getTime());
	mockModel.setEndDate  (enddate.getTime());
		
	var controller = new IterationRowController(mockModel, null, this);
	var row = this.iterationsView.createRow(controller, mockModel, "top");
	controller.view = row;
	row.autoCreateCells([ 0, IterationRowController.columnIndices.actions ]);
	row.render();
	controller.openRowEdit();
};

/**
 * Populate a new, editable story row to the story table.
 */
ProjectController.prototype.createStory = function() {
	var mockModel = ModelFactory.createObject(ModelFactory.types.story);
	mockModel.setBacklog(this.model);

	// Assign user if option is set
	var user = PageController.getInstance().getCurrentUser();
	if (user.isAutoassignToStories()) {
		mockModel.setResponsibles([ user.getId() ]);
	}

	var controller = new StoryController(mockModel, null, this);
	var row = this.storyListView.createRow(controller, mockModel, "top");
	controller.view = row;
	row.autoCreateCells([ 0, 1, 6, 7 ]);
	row.getCellByName("description").show();
	row.getCellByName("buttons").show();
	row.getCellByName("backlog").hide();
	// row.getCell(13).show();
	row.render();
	controller.openRowEdit();
};

/**
 * Creates a new story controller.
 */
ProjectController.prototype.storyControllerFactory = function(view, model) {
	var storyController = new StoryController(model, view, this);
	this.addChildController("story", storyController);
	return storyController;
};

/**
 * Get backlogs selectable for a story in the project.
 */
ProjectController.prototype.getSelectableBacklogs = function() {
	var returned = {};

	returned[this.model.getId()] = this.model.getName();

	var children = this.model.getChildren();
	for ( var i = 0; i < children.length; i++) {
		returned[children[i].getId()] = children[i].getName();
	}

	return returned;
};

ProjectController.prototype.changeTabIfFragmentFound = function() {
	var hash = window.location.hash;
	if (hash.match(/fi\.hut\.soberit\.agilefant\.model\.Story_(\d+)/)) {
		this.tabs.tabs('select', 0);
	}
};

/**
 * Initialize project details configuration.
 */
ProjectController.prototype.initializeProjectDetailsConfig = function() {
	var config = new DynamicTableConfiguration({
		leftWidth : '20%',
		rightWidth : '79%',
		closeRowCallback : null,
		validators : [ BacklogModel.Validators.dateValidator ]
	});
	config.addColumnConfiguration(ProjectController.columnIndices.name,
			ProjectController.columnConfigs.name);
	config.addColumnConfiguration(ProjectController.columnIndices.reference,
			ProjectController.columnConfigs.reference);
	config.addColumnConfiguration(ProjectController.columnIndices.startDate,
			ProjectController.columnConfigs.startDate);
	config.addColumnConfiguration(ProjectController.columnIndices.endDate,
			ProjectController.columnConfigs.endDate);
	config.addColumnConfiguration(ProjectController.columnIndices.plannedSize,
			ProjectController.columnConfigs.plannedSize);
	config.addColumnConfiguration(ProjectController.columnIndices.baselineLoad,
			ProjectController.columnConfigs.baselineLoad);
	config.addColumnConfiguration(ProjectController.columnIndices.assignees,
			ProjectController.columnConfigs.assignees);
	config.addColumnConfiguration(ProjectController.columnIndices.description,
			ProjectController.columnConfigs.description);
	this.projectDetailConfig = config;
};

/**
 * Initialize configuration for iteration lists.
 */
ProjectController.prototype.initializeIterationListConfig = function() {
	var config = new DynamicTableConfiguration(
			{
				rowControllerFactory : ProjectController.prototype.iterationRowControllerFactory,
				dataSource : ProjectModel.prototype.getIterations,
				dataType : "iteration",
				caption : "Iterations",
				cssClass : "project-iteration-table",
				validators : [ BacklogModel.Validators.dateValidator ]
			});
	this._iterationListColumnConfig(config);
	config.addCaptionItem({
		name : "createIteration",
		text : "Create iteration",
		cssClass : "create",
		callback : ProjectController.prototype.createIteration
	});
	this.iterationListConfig = config;
};

ProjectController.prototype._iterationListColumnConfig = function(config) {
	config.addColumnConfiguration(IterationRowController.columnIndices.expand,
			{
				minWidth : 16,
				autoScale : true,
				title : "",
				headerTooltip : 'Expand/collapse',
				defaultSortColumn : false,
				subViewFactory : IterationRowController.prototype.toggleFactory
			});

	config.addColumnConfiguration(IterationRowController.columnIndices.link, {
		minWidth : 25,
		autoScale : true,
		title : "ID",
		headerTooltip : 'Iteration id [link to page]',
		cssClass : "backlog-id-link",
		get : IterationModel.prototype.getId,
		decorator : DynamicsDecorators.backlogNameLinkDecorator,
		sortCallback : DynamicsComparators
				.valueComparatorFactory(IterationModel.prototype.getId)
	});

	config.addColumnConfiguration(IterationRowController.columnIndices.name, {
		minWidth : 150,
		autoScale : true,
		title : "Name",
		headerTooltip : 'Iteration name',
		get : IterationModel.prototype.getName,
		sortCallback : DynamicsComparators
				.valueComparatorFactory(IterationModel.prototype.getName),
		editable : true,
		dragHandle : true,
		edit : {
			editor : "Text",
			set : IterationModel.prototype.setName,
			required : true
		}
	});
	config.addColumnConfiguration(
			IterationRowController.columnIndices.assignees, {
				minWidth : 130,
				autoScale : true,
				title : "Assignees",
				headerTooltip : 'Iteration assignees',
				get : BacklogModel.prototype.getAssignees,
				decorator : DynamicsDecorators.responsiblesDecorator,
				editable : true,
				openOnRowEdit : false,
				edit : {
					editor : "Autocomplete",
					dataType : "usersAndTeams",
					set : BacklogModel.prototype.setAssignees
				}
			});
	config
			.addColumnConfiguration(
					IterationRowController.columnIndices.startDate,
					{
						minWidth : 80,
						autoScale : true,
						title : "Start date",
						headerTooltip : 'Start date',
						get : IterationModel.prototype.getStartDate,
						sortCallback : DynamicsComparators
								.valueComparatorFactory(IterationModel.prototype.getStartDate),
						decorator : DynamicsDecorators.dateTimeDecorator,
						defaultSortColumn : true,
						editable : true,
						dragHandle : true,
						edit : {
							editor : "Date",
							decorator : DynamicsDecorators.dateTimeDecorator,
							set : IterationModel.prototype.setStartDate,
							required : true,
							withTime : true
						}
					});
	config
			.addColumnConfiguration(
					IterationRowController.columnIndices.endDate,
					{
						minWidth : 80,
						autoScale : true,
						title : "End date",
						headerTooltip : 'End date',
						get : IterationModel.prototype.getEndDate,
						sortCallback : DynamicsComparators
								.valueComparatorFactory(IterationModel.prototype.getEndDate),
						decorator : DynamicsDecorators.dateTimeDecorator,
						editable : true,
						dragHandle : true,
						edit : {
							editor : "Date",
							decorator : DynamicsDecorators.dateTimeDecorator,
							set : IterationModel.prototype.setEndDate,
							required : true,
							withTime : true
						}
					});
	config.addColumnConfiguration(
			IterationRowController.columnIndices.storiesData, {
				fullWidth : true,
				visible : false,
				delayedRender : true
			});
	config.addColumnConfiguration(IterationRowController.columnIndices.buttons,
			{
				fullWidth : true,
				visible : false,
				subViewFactory : DynamicsButtons.commonButtonFactory
			});
};

/**
 * Initialize configuration for story lists.
 */
ProjectController.prototype.initializeStoryConfig = function() {
  var me = this;
  var config = new DynamicTableConfiguration( {
    rowControllerFactory : ProjectController.prototype.storyControllerFactory,
    dataSource : ProjectModel.prototype.getLeafStories,
    dataType: "story",
    cssClass: "project-story-table",
    sortCallback: StoryController.prototype.rankStory,
    caption : "Project Backlog"
  });

  config.addCaptionItem( {
    name : "createStory",
    text : "Create story",
    cssClass : "create",
    callback : ProjectController.prototype.createStory
  });
  
  config.addColumnConfiguration(0, {
    minWidth : 24,
    autoScale : true,
    title : "#",
    columnName: "expand",
    headerTooltip : 'Priority',
    defaultSortColumn: true,
    subViewFactory: StoryController.prototype.descriptionToggleFactory,
    sortCallback: DynamicsComparators.valueComparatorFactory(StoryModel.prototype.getRank)
  });
  
  config.addColumnConfiguration(1, {
    minWidth : 30,
    autoScale : true,
    title : "ID",
    headerTooltip : 'Story id [link to page]',
	cssClass : "story-id-link",
    get : CommonModel.prototype.getId,
    decorator : DynamicsDecorators.storyNameLinkDecorator,
	sortCallback : DynamicsComparators
			.valueComparatorFactory(StoryModel.prototype.getId),
    editable : false
  });
  
  config.addColumnConfiguration(2, {
    minWidth : 280,
    autoScale : true,
    title : "Name",
    headerTooltip : 'Story name',
    get : StoryModel.prototype.getName,
    sortCallback: DynamicsComparators.valueComparatorFactory(StoryModel.prototype.getName),
    dragHandle: true,
    editable : true,
    edit : {
      editor : "Text",
      set : StoryModel.prototype.setName,
      required: true
    }
  });
  
  config.addColumnConfiguration(3, {
    minWidth : 50,
    autoScale : true,
    title : "Value",
    headerTooltip : 'A given story value or weight',
    get : StoryModel.prototype.getStoryValue,
    sortCallback: DynamicsComparators.valueComparatorFactory(StoryModel.prototype.getStoryValue),
    editable : true,
    editableCallback: StoryController.prototype.storyValueOrPointsEditable,
    decorator: DynamicsDecorators.estimateDecorator,
    edit : {
      editor : "StoryValue",
      set : StoryModel.prototype.setStoryValue
    }
  });

  config.addColumnConfiguration(4, {
    minWidth : 50,
    autoScale : true,
    title : "Points",
    headerTooltip : 'Estimate in story points',
    get : StoryModel.prototype.getStoryPoints,
    sortCallback: DynamicsComparators.valueComparatorFactory(StoryModel.prototype.getStoryPoints),
    editable : true,
    editableCallback: StoryController.prototype.storyValueOrPointsEditable,
    decorator: DynamicsDecorators.estimateDecorator,
    edit : {
      editor : "Estimate",
      set : StoryModel.prototype.setStoryPoints
    }
  });
  
  config.addColumnConfiguration(5, {
    minWidth : 70,
    autoScale : true,
    title : 'State',
    headerTooltip : 'Story state',
    get : StoryModel.prototype.getState,
    decorator: DynamicsDecorators.storyStateColorDecorator,
    editable : true,
    filter: ProjectController.prototype.filterLeafStoriesByState,
    edit : {
      editor : "Selection",
      set : StoryModel.prototype.setState,
      items : DynamicsDecorators.stateOptions
    }
  });
  
  config.addColumnConfiguration(6, {
    minWidth : 70,
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
  });

  config.addColumnConfiguration(7, {
    minWidth : 100,
    autoScale : true,
    columnName: "backlog",
    title : "Iteration", // From Agilefant 3.0 onwards, this column displays the project in question, and its iterations (if any) => title change accordingly
    headerTooltip : 'The iteration the story has been scheduled to (if any)',
    get : StoryModel.prototype.getIteration,
    decorator: DynamicsDecorators.iterationSelectDecorator,
    sortCallback: DynamicsComparators.storyBacklogNameComparator,
    editable : false, // had to remove direct editing because then we would had to have both Backlogs and Iterations in the same select list; even if that would have worked, the current logic would break when both backlog and iteration have the same id
    openOnRowEdit: false,
    edit: {  // dead code; resurrect if and when this is made editable again
      editor: "Selection",
      items: function() { return me.getSelectableBacklogs(); },
      set: StoryModel.prototype.moveStory
    }
  });
  config.addColumnConfiguration(8, {
    minWidth : 40,
    columnName: "edit",
    autoScale : true,
    title : "Actions",  // Hopefully making a bit more obvious that stories can be moved via the edit button (and edit button only)
    subViewFactory : StoryController.prototype.projectStoryActionFactory
  });
  config.addColumnConfiguration(9, {
    minWidth : 40,
    columnName: "ranktotop",
    autoScale : true,
    title : "",
    subViewFactory : StoryController.prototype.rankToTopAction
  });
  config.addColumnConfiguration(10, {
    minWidth : 60,
    columnName: "ranktobottom",
    autoScale : true,
    title : "",
    subViewFactory : StoryController.prototype.rankToBottomAction
  });
  
  if (Configuration.isLabelsInStoryList()) {
	  config.addColumnConfiguration(11, StoryListController.columnConfig.labels);
  }
  
  config.addColumnConfiguration(12, {
    columnName: "description",
    fullWidth: true,
    visible: false,
    editable : true,
    get: StoryModel.prototype.getDescription,
    edit : {
      editor : "Wysiwyg",
      set : StoryModel.prototype.setDescription
    }
  });
  config.addColumnConfiguration(13, {
    fullWidth : true,
    visible : false,
    columnName: "details",
    subViewFactory : StoryController.prototype.storyDetailsFactory,
    delayedRender: true
  });
  config.addColumnConfiguration(14, {
    columnName: "buttons",
    fullWidth : true,
    visible : false,
    cssClass : 'projectstory-data',
    subViewFactory : DynamicsButtons.commonButtonFactory
  });

  this.storyListConfig = config;
};
