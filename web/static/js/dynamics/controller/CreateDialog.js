
var CreateDialog = {};

/**
 * @member CreateDialog
 */
CreateDialog.configurations = {
    product: {
      title: "Create a new product"
    },
    project: {
      title: "Create a new project"
    },
    iteration: {
      title: "Create a new iteration"
    },
    story: {
      title: "Create a new story"
    },
    user: {
      title: "Create a new user"
    },
    team: {
      title: "Create a new team"
    },
    effortEntry: { 
      title: "Log effort"
    }
  };

/**
 * Convenience method to return null.
 */
CreateDialog.returnNull = function() {
  return null;
};

/**
 * Convenience method to return an empty string.
 */
CreateDialog.returnEmptyString = function() {
  return "";
};


/**
 * Constructor for a creation dialog.
 * @constructor
 */
var CreateDialogClass = function() {};
CreateDialogClass.prototype = new ViewPart();

/**
 * Initialize a new creation dialog.
 */
CreateDialogClass.prototype.init = function(config) {
  var me = this;
  this.element = $('<div/>').appendTo(document.body);
  
  var opts = {
    modal: true,
    resizable: false,
    draggable: true,
    width: 750,
    position: 'top',
    buttons: {
      "Cancel": function() { me._cancel(); },
      "Ok": function() { me._ok(); }
    }
  };
  jQuery.extend(opts, config);
  this.element.dialog(opts);
  
  this.initializeForm();
};

CreateDialogClass.prototype.initFormConfig = function() {
  throw "Abstract method called: initFormConfig";
};

CreateDialogClass.prototype._ok = function() {
  if (this.view.getValidationManager().isValid()) {
    this.model.commit();
    this.close();
  }
};



CreateDialogClass.prototype._cancel = function() {
  this.close();
};

CreateDialogClass.prototype.close = function() {
  this.element.dialog("destroy").remove();
  if (this.closeCallback) {
    this.closeCallback();
  }
};

CreateDialogClass.prototype.getModel = function() {
  return this.model;
};

CreateDialogClass.prototype.getElement = function() {
  return this.element;
};

CreateDialogClass.prototype.setCloseCallback = function(callback) {
  this.closeCallback = callback;
};

/**
 * Create the fields for the dialog.
 */
CreateDialogClass.prototype.initializeForm = function() {
  this.form = $('<form/>').appendTo(this.element);
  this.formArea = $('<div/>').appendTo(this.form);
  this.view = new DynamicVerticalTable(
      this,
      this.model,
      this.formConfig,
      this.formArea);
  this.view.render();
  this.view.openFullEdit();
};

/**
 * Product creation dialog.
 * @constructor
 */
CreateDialog.Product = function() {
  this.model = ModelFactory.createObject(ModelFactory.typeToClassName.product);
  this.initFormConfig();
  this.init(CreateDialog.configurations.product);
};
CreateDialog.Product.prototype = new CreateDialogClass();
CreateDialog.Product.columnIndices = {
    name: 0,
    description: 1,
    teams: 2,
    warning: 3
};

CreateDialog.Product.prototype.initFormConfig = function() {
  var currentUser = PageController.getInstance().getCurrentUser();
  var config = new DynamicTableConfiguration({
    leftWidth: '20%',
    rightWidth: '75%',
    closeRowCallback: CreateDialogClass.prototype.close
  });
  
  config.addColumnConfiguration(CreateDialog.Product.columnIndices.name,{
    title: "Name",
    editable: true,
    get: ProductModel.prototype.getName,
    edit: {
      editor: "Text",
      required: true,
      set: ProductModel.prototype.setName
    }
  });
  
  config.addColumnConfiguration(CreateDialog.Product.columnIndices.description, {
    title: "Description",
    get: ProductModel.prototype.getDescription,
    editable: true,
    edit: {
      editor: "Wysiwyg",
      set: ProductModel.prototype.setDescription
    }
  });
  
  if (currentUser.getAdmin()) {
	config.addColumnConfiguration(CreateDialog.Product.columnIndices.teams, {
	title: "Grant all teams access to product?",
    	get: currentUser.getAdmin,
    	editable: true,
    	edit: {
      		editor : "Selection",
      		items : DynamicsDecorators.adminOptions,
      		set: ProductModel.prototype.setAllTeams,
      		size: '20ex',
      		required: true
    }
  	});
  }
  
  var warningFunction = function() {
		return "Warning! You will only be able to see the product if you belong to a team that has access to it. You can modify access rights later from the Administration tab.";
	}
  config.addColumnConfiguration(CreateDialog.Product.columnIndices.warning, {
      title: "",
  	cssClass: "warning-text",
      editable: false,
      get: warningFunction,
    });

  this.formConfig = config;
};


/**
 * Project creation dialog.
 * @constructor
 */
CreateDialog.Project = function() {
  // Create the mock model
  this.model = ModelFactory.createObject(ModelFactory.typeToClassName.project);
  
  this.model.setStartDate(new Date().getTime());
  this.model.setEndDate(new Date().getTime());

  this.initFormConfig();
  this.init(CreateDialog.configurations.project);
};
CreateDialog.Project.prototype = new CreateDialogClass();
CreateDialog.Project.columnIndices = {
  name: 0,
  parent: 1,
  startDate: 2,
  endDate: 3,
  plannedSize: 4,
  baselineLoad: 5,
  assignees: 6,
  description: 7
};
CreateDialog.Project.prototype.initFormConfig = function() {
  var config = new DynamicTableConfiguration({
    leftWidth: '24%',
    rightWidth: '75%',
    validators: [ BacklogModel.Validators.dateValidator, BacklogModel.Validators.parentValidator ],
    closeRowCallback: CreateDialogClass.prototype.close
  });
  
  config.addColumnConfiguration(CreateDialog.Project.columnIndices.name,
      ProjectController.columnConfigs.name);
  config.addColumnConfiguration(CreateDialog.Project.columnIndices.parent,{
    title : "Product",
    get : ProjectModel.prototype.getParent,
    decorator: DynamicsDecorators.backlogSelectDecorator,
    editable : true,
    edit : {
      editor : "InlineAutocomplete",
      dataType: "products",
      decorator: DynamicsDecorators.propertyDecoratorFactory(BacklogModel.prototype.getName),
      set: ProjectModel.prototype.setParent
    }
  });
  config.addColumnConfiguration(CreateDialog.Project.columnIndices.startDate,
      ProjectController.columnConfigs.startDate);
  config.addColumnConfiguration(CreateDialog.Project.columnIndices.endDate,
      ProjectController.columnConfigs.endDate);
  config.addColumnConfiguration(
      CreateDialog.Project.columnIndices.plannedSize,
      ProjectController.columnConfigs.plannedSize);
  config.addColumnConfiguration(
      CreateDialog.Project.columnIndices.baselineLoad,
      ProjectController.columnConfigs.baselineLoad);
  config.addColumnConfiguration(CreateDialog.Project.columnIndices.assignees,
      ProjectController.columnConfigs.assignees);
  config.addColumnConfiguration(
      CreateDialog.Project.columnIndices.description,
      ProjectController.columnConfigs.description);
  
  
  this.formConfig = config;
};

/**
 * Iteration creation dialog.
 * @constructor
 */
CreateDialog.Iteration = function() {
  // Create the mock model
  this.model = ModelFactory.createObject(ModelFactory.typeToClassName.iteration);
  
  this.model.setStartDate(new Date().getTime());
  this.model.setEndDate(new Date().getTime());
 
  this.initFormConfig();
  this.init(CreateDialog.configurations.iteration);
};
CreateDialog.Iteration.prototype = new CreateDialogClass();
CreateDialog.Iteration.columnIndices = {
  name:       0,
  parent:     1,
  startDate:  2,
  endDate:    3,
  plannedSize:4,
  baselineLoad:5,
  assignees:  6,
  description:7,
  teams: 8,
  warning: 9
};
CreateDialog.Iteration.prototype.initFormConfig = function() {
  var currentUser = PageController.getInstance().getCurrentUser();
  var config = new DynamicTableConfiguration({
    leftWidth: '24%',
    rightWidth: '75%',
    closeRowCallback: CreateDialogClass.prototype.close,
    validators: [ BacklogModel.Validators.dateValidator, BacklogModel.Validators.parentValidator ]
  });
  
  config.addColumnConfiguration(CreateDialog.Iteration.columnIndices.name,IterationController.columnConfigs.name);
  
  config.addColumnConfiguration(CreateDialog.Iteration.columnIndices.parent,{
    title : "Project (leave empty to create a standalone iteration)",
    get: IterationModel.prototype.getParent,
    decorator: DynamicsDecorators.backlogSelectDecorator,
    editable : true,
    edit : {
      required : false,
      editor : "InlineAutocomplete",
      dataType: "projects",
      decorator: DynamicsDecorators.propertyDecoratorFactory(BacklogModel.prototype.getName),
      set: IterationModel.prototype.setParent
    }
  });
    
  config.addColumnConfiguration(CreateDialog.Iteration.columnIndices.startDate,
      IterationController.columnConfigs.startDate);
  config.addColumnConfiguration(CreateDialog.Iteration.columnIndices.endDate,
      IterationController.columnConfigs.endDate);
  config.addColumnConfiguration(
      CreateDialog.Iteration.columnIndices.plannedSize,
      IterationController.columnConfigs.plannedSize);
  config.addColumnConfiguration(
      CreateDialog.Iteration.columnIndices.baselineLoad,
      IterationController.columnConfigs.baselineLoad);
  config.addColumnConfiguration(CreateDialog.Iteration.columnIndices.assignees,
      IterationController.columnConfigs.assignees);
  config.addColumnConfiguration(
      CreateDialog.Iteration.columnIndices.description,
      IterationController.columnConfigs.description);
  if (currentUser.getAdmin()) {
  	config.addColumnConfiguration(CreateDialog.Iteration.columnIndices.teams, {
  		title: "Grant all teams access to iteration?",
    	get: currentUser.getAdmin,
    	editable: true,
    	edit: {
      		editor : "Selection",
      		items : DynamicsDecorators.adminOptions,
      		set: IterationModel.prototype.setAllTeams,
      		size: '20ex',
      		required: true
    	}
  	});
  }
  
  var warningFunction = function() {
		return "Warning! If you create a standalone iteration, you will only be able to see it if you belong to a team that has access to it. You can modify access rights later from the Administration tab.";
	}
  config.addColumnConfiguration(CreateDialog.Iteration.columnIndices.warning, {
      title: "",
  	cssClass: "warning-text",
      editable: false,
      get: warningFunction,
    });
  
  this.formConfig = config;
};

/**
 * Story creation dialog.
 * @constructor
 */
CreateDialog.Story = function() {
  // Create the mock model
  this.model = ModelFactory.createObject(ModelFactory.typeToClassName.story);
  
  // Assign user
  var user = PageController.getInstance().getCurrentUser();
  if (user.isAutoassignToStories()) {
    this.model.setResponsibles([user.getId()]);
  }
  
  this.initFormConfig();
  this.init(CreateDialog.configurations.story);
};
CreateDialog.Story.prototype = new CreateDialogClass();
CreateDialog.Story.columnIndices = {
  name:       0,
  backlog:    1,
  state:      2,
  storyValue: 3,
  storyPoints:4,
  responsibles:5,
  labels:     6,
  description:7,
};
CreateDialog.Story.prototype.initFormConfig = function() {
  var config = new DynamicTableConfiguration({
    leftWidth: '24%',
    rightWidth: '75%',
    closeRowCallback: CreateDialogClass.prototype.close,
    validators: [ ]
  });
  
  config.addColumnConfiguration(CreateDialog.Story.columnIndices.name,{
    title: "Name",
    editable: true,
    get: StoryModel.prototype.getName,
    edit: {
      editor: "Text",
      required: true,
      set: StoryModel.prototype.setName
    }
  });
  
  config.addColumnConfiguration(CreateDialog.Story.columnIndices.storyValue,{
	    title: "Story value",
	    editable: true,
	    get: StoryModel.prototype.getStoryValue,
	    edit: {
	      editor: "Number",
	      required: false,
	      set: StoryModel.prototype.setStoryValue
	    }
	  });
  
  config.addColumnConfiguration(CreateDialog.Story.columnIndices.backlog,{
    title : "Backlog",
    get : StoryModel.prototype.getBacklog,
    decorator: DynamicsDecorators.backlogSelectDecorator,
    editable : true,
    edit : {
      editor : "InlineAutocomplete",
      dataType: "backlogs",
      decorator: DynamicsDecorators.propertyDecoratorFactory(BacklogModel.prototype.getName),
      set: StoryModel.prototype.setBacklog
    }
  });
  
  config.addColumnConfiguration(CreateDialog.Story.columnIndices.state,{
    title: "State",
    editable: true,
    get: StoryModel.prototype.getState,
    edit: {
      editor : "Selection",
      set : StoryModel.prototype.setState,
      items : DynamicsDecorators.stateOptions
    }
  });
  
  config.addColumnConfiguration(CreateDialog.Story.columnIndices.storyValue,{
    title: "Story value",
    editable: true,
    get: StoryModel.prototype.getStoryValue,
    edit: {
      editor: "Number",
      required: false,
      set: StoryModel.prototype.setStoryValue
    }
  });
  
  config.addColumnConfiguration(CreateDialog.Story.columnIndices.storyPoints,{
    title: "Story points",
    editable: true,
    get: StoryModel.prototype.getStoryPoints,
    edit: {
      editor: "Number",
      required: false,
      set: StoryModel.prototype.setStoryPoints
    }
  });
  
  config.addColumnConfiguration(CreateDialog.Story.columnIndices.responsibles,{
    title : "Responsibles",
    get : StoryModel.prototype.getResponsibles,
    decorator: DynamicsDecorators.emptyValueWrapper(DynamicsDecorators.userInitialsListDecorator, "(Select users)"),
    editable : true,
    openOnRowEdit: false,
    edit : {
      editor : "Autocomplete",
      dialogTitle: "Select users",
      dataType: "usersAndTeams",
      set : StoryModel.prototype.setResponsibles
    }
  });
  
  config.addColumnConfiguration(CreateDialog.Story.columnIndices.labels,{
    title : "Labels",
    get : StoryModel.prototype.getLabels,
    editable : true,
    openOnRowEdit: true,
    edit : {
      editor : "Labels",
      set : StoryModel.prototype.setLabels
    }
  });

  config.addColumnConfiguration(CreateDialog.Story.columnIndices.description, {
    title: "Description",
    get: StoryModel.prototype.getDescription,
    editable: true,
    edit: {
      editor: "Wysiwyg",
      set: StoryModel.prototype.setDescription
    }
  });
  
  this.formConfig = config;
};


/**
 * Create dialog for stories that are created in tree. 
 */
CreateDialog.StoryFromTree = function(model, ajax) {
  // Create the mock model
  this.model = model;
  this.ajax = ajax;
   
  // Assign user
  var user = PageController.getInstance().getCurrentUser();
  if (user.isAutoassignToStories()) {
    this.model.setResponsibles([user.getId()]);
  }
  
  this.initFormConfig();
  
  this.formConfig.columns[CreateDialog.Story.columnIndices.backlog].options.editable = false;
  
  this.init(CreateDialog.configurations.story);
};
extendObject(CreateDialog.StoryFromTree, CreateDialog.Story);

CreateDialog.StoryFromTree.prototype._ok = function() {
  if (this.view.getValidationManager().isValid()) {
    this.ajax(this.model);
    this.close();
  }
};




/**
 * User creation dialog.
 * @constructor
 */
CreateDialog.User = function() {
  // Create the mock model
  this.model = ModelFactory.createObject(ModelFactory.typeToClassName.user);
  
  
  this.initFormConfig();
  this.init(CreateDialog.configurations.user);
};
CreateDialog.User.prototype = new CreateDialogClass();
CreateDialog.User.columnIndices = {
  name:      0,
  loginName: 1,  
  initials:  2,
  email:     3,
  password1: 4,
  password2: 5,
  admin:     6
};
CreateDialog.User.prototype.initFormConfig = function() {
  var config = new DynamicTableConfiguration({
    leftWidth: '24%',
    rightWidth: '75%',
    validators: [ UserModel.Validators.passwordValidator ],
    closeRowCallback: CreateDialogClass.prototype.close
  });
  
  config.addColumnConfiguration(CreateDialog.User.columnIndices.name,{
    title: "Name",
    editable: true,
    get: UserModel.prototype.getFullName,
    edit: {
      editor: "Text",
      required: true,
      size: '40ex',
      set: UserModel.prototype.setFullName
    }
  });
  
  config.addColumnConfiguration(CreateDialog.User.columnIndices.loginName,{
    title: "Login name",
    editable: true,
    get: UserModel.prototype.getLoginName,
    edit: {
      editor: "Text",
      size: '10ex',
      required: true,
      set: UserModel.prototype.setLoginName
    }
  });
  
  config.addColumnConfiguration(CreateDialog.User.columnIndices.initials,{
    title: "Initials",
    editable: true,
    get: UserModel.prototype.getInitials,
    edit: {
      editor: "Text",
      required: true,
      size: '10ex',
      set: UserModel.prototype.setInitials
    }
  });
  
  config.addColumnConfiguration(CreateDialog.User.columnIndices.email, {
    title: "Email",
    editable: true,
    get: UserModel.prototype.getEmail,
    edit: {
      editor: "Email",
      required: true,
      size: '40ex',
      set: UserModel.prototype.setEmail
    }
  });
  
  config.addColumnConfiguration(CreateDialog.User.columnIndices.password1,{
    title: "Password",
    editable: true,
    get: UserModel.prototype.getPassword1,
    edit: {
      editor: "Password",
      size: '20ex',
      set: UserModel.prototype.setPassword1,
      required: true
    }
  });
  
  config.addColumnConfiguration(CreateDialog.User.columnIndices.password2,{
    title: "Confirm password",
    editable: true,
    get: UserModel.prototype.getPassword2,
    edit: {
      editor: "Password",
      size: '20ex',
      set: UserModel.prototype.setPassword2,
      required: true
    }
  });
  
  config.addColumnConfiguration(CreateDialog.User.columnIndices.admin,{
    title: "Administrator",
    editable: true,
    get: UserModel.prototype.getAdmin,
    edit: {
      editor : "Selection",
      items : DynamicsDecorators.adminOptions,
      size: '20ex',
      set: UserModel.prototype.setAdmin,
      required: true
    }
  });

  this.formConfig = config;
};

/**
 * Team creation dialog.
 * @constructor
 */
CreateDialog.Team = function() {
  // Create the mock model
  this.model = ModelFactory.createObject(ModelFactory.typeToClassName.team);
  
  
  this.initFormConfig();
  this.init(CreateDialog.configurations.team);
};
CreateDialog.Team.prototype = new CreateDialogClass();
CreateDialog.Team.columnIndices = {
  name:      0,
  users:     1,
  products:  2,
  iterations: 3
};
CreateDialog.Team.prototype.initFormConfig = function() {
  var currentUser = PageController.getInstance().getCurrentUser();
  var config = new DynamicTableConfiguration({
    leftWidth: '24%',
    rightWidth: '75%',
    closeRowCallback: CreateDialogClass.prototype.close
  });
  
  config.addColumnConfiguration(CreateDialog.Team.columnIndices.name,{
    title: "Name",
    editable: true,
    get: TeamModel.prototype.getName,
    edit: {
      editor: "TeamName",
      required: true,
      set: TeamModel.prototype.setName
    }
  });
  
  config.addColumnConfiguration(CreateDialog.Team.columnIndices.users, {
    minWidth : 60,
    autoScale : true,
    title : "Users",
    cssClass: "user-chooser",
    headerTooltip : 'Users',
    get : TeamModel.prototype.getUsers,
    decorator: DynamicsDecorators.teamUserInitialsListDecorator,
    editable : true,
    openOnRowEdit: false,
    edit : {
      editor : "Autocomplete",
      dialogTitle: "Select users",
      dialogClose: function() { jQuery('.ui-button:eq(' + 1 + ')').focus(); }, // After selecting users focus on ok button
      dataType: "usersAndTeams",
      set : TeamModel.prototype.setUsers
    }
  });
  
  if (currentUser.getAdmin()) {
	var falseFunction = function() {
		  return "false";
	  }
  	config.addColumnConfiguration(CreateDialog.Team.columnIndices.products, {
    	title: "Grant the team access to all products?",
    	get: falseFunction,
    	editable: true,
    	edit: {
      		editor : "Selection",
      		items : DynamicsDecorators.adminOptions,
      		set: TeamModel.prototype.setAllProducts,
      		size: '20ex',
      		required: true
    	}
  	});
  	config.addColumnConfiguration(CreateDialog.Team.columnIndices.iterations, {
    	title: "Grant the team access to all standalone iterations?",
    	get: falseFunction,
    	editable: true,
    	edit: {
      		editor : "Selection",
      		items : DynamicsDecorators.adminOptions,
      		set: TeamModel.prototype.setAllIterations,
      		size: '20ex',
      		required: true
    	}
  	});
  }


  this.formConfig = config;
};

/**
 * Spent effort entry creation dialog.
 * @constructor
 */
CreateDialog.EffortEntry = function() {
  this.model = ModelFactory.createObject(ModelFactory.typeToClassName.hourEntry);
  
  this.model.setUsers([], [PageController.getInstance().getCurrentUser()]);
  this.model.setDate(new Date().asString());
  this.initFormConfig();
  this.init(CreateDialog.configurations.effortEntry);
};
CreateDialog.EffortEntry.prototype = new CreateDialogClass();
CreateDialog.EffortEntry.columnIndices = {
    effortSpent: 0,
    date: 1,
    users: 2,
    comment: 3
};

CreateDialog.EffortEntry.prototype.initFormConfig = function() {
  var config = new DynamicTableConfiguration({
    leftWidth: '20%',
    rightWidth: '75%',
    closeRowCallback: CreateDialogClass.prototype.close,
    validators: [HourEntryModel.Validators.usersValidator]
  });
  
  config.addColumnConfiguration(CreateDialog.EffortEntry.columnIndices.effortSpent,{
    title: "Effort Spent",
    editable: true,
    get: HourEntryModel.prototype.getMinutesSpent,
    edit: {
      editor: "ExactEstimate",
      required: true,
      size: "8em",
      set: HourEntryModel.prototype.setEffortSpent
    }
  });
  config.addColumnConfiguration(CreateDialog.EffortEntry.columnIndices.date,{
    title: "Date",
    editable: true,
    get: HourEntryModel.prototype.getDate,
    edit: {
      editor: "Date",
      required: true,
      withTime: true,
      set: HourEntryModel.prototype.setDate
    }
  });
  config.addColumnConfiguration(CreateDialog.EffortEntry.columnIndices.users,{
    title: "Users",
    editable: true,
    get: HourEntryModel.prototype.getUsers,
    decorator: DynamicsDecorators.userInitialsListDecorator,
    openOnRowEdit: false,
    edit: {
      editor : "Autocomplete",
      dialogTitle: "Select users",
      dataType: "usersAndTeams",
      required: true,
      set: HourEntryModel.prototype.setUsers
    }
  });
  
  config.addColumnConfiguration(CreateDialog.EffortEntry.columnIndices.comment, {
    title: "Comment",
    get: HourEntryModel.prototype.getDescription,
    editable: true,
    edit: {
      editor: "Text",
      set: HourEntryModel.prototype.setDescription
    }
  });
  
  this.formConfig = config;
};


/*
 * CREATION BY LINK ID.
 */

CreateDialog.idToClass = {
  /** @member CreateDialog */
  "createNewProduct": CreateDialog.Product,
  /** @member CreateDialog */
  "createNewProject": CreateDialog.Project,
  /** @member CreateDialog */
  "createNewIteration": CreateDialog.Iteration,
  /** @member CreateDialog */
  "createNewStory": CreateDialog.Story,
  /** @member CreateDialog */
  "createNewUser": CreateDialog.User,
  /** @member CreateDialog */
  "createNewTeam": CreateDialog.Team,
  /** @member CreateDialog */
  "createNewEffortEntry": CreateDialog.EffortEntry
};

/**
 * Instantiate a creation dialog by type string.
 * <p>
 * @see CreateDialog.typeToClass
 */
CreateDialog.createById = function(id) {
  var C = CreateDialog.idToClass[id];
  var dialog = new C();
  return dialog;
};


