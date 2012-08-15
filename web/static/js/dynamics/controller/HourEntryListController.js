/**
 * Hour entry list controller.
 * 
 * @constructor
 * @base CommonController

 */
var HourEntryListController = function HourEntryListController(options) {
  this.hourEntryListElement = options.hourEntryListElement;
  this.hourEntryTableElement = $('<div></div>').appendTo(this.hourEntryListElement);
  this.parentModel = options.parentModel;
  this.options = options;
  this.model = null;
  this.init();
  this.initButtonsView();
  this.initConfig();
  this.paint();
};

HourEntryListController.prototype = new CommonController();

/**
 * Creates a new story controller.
 */
HourEntryListController.prototype.hourEntryControllerFactory = function(view, model) {
  var hourEntryController = new HourEntryController(model, view, this);
  this.addChildController("hourEntry", hourEntryController);
  return hourEntryController;
};

HourEntryListController.prototype.paintHourEntryTable = function() {
  this.hourEntryTableView = new DynamicTable(this, this.model, this.hourEntryTableConfig,
      this.hourEntryTableElement);
  
  this.hourEntryTableView.getElement().css({'overflow':'auto','max-height':'20em'});
  
  //need to extend existing table configuration as that's the only way to get same with to columns
  var origConf = this.hourEntryTableConfig.getColumns();
  
  this.addEntryConfig = [];
  this.addEntryConfig[HourEntryController.columnIndices.date] = new DynamicTableColumnConfiguration(origConf[0].options);
  this.addEntryConfig[HourEntryController.columnIndices.user] = new DynamicTableColumnConfiguration(origConf[1].options);
  this.addEntryConfig[HourEntryController.columnIndices.spentEffort] = new DynamicTableColumnConfiguration(origConf[2].options);
  this.addEntryConfig[HourEntryController.columnIndices.effortLeft] = new DynamicTableColumnConfiguration(origConf[3].options);
  this.addEntryConfig[HourEntryController.columnIndices.description] = new DynamicTableColumnConfiguration(origConf[4].options);
  this.addEntryConfig[HourEntryController.columnIndices.actions] = new DynamicTableColumnConfiguration(origConf[5].options);
  this.addEntryConfig[HourEntryController.columnIndices.user].options.editable = true;
  this.addEntryConfig[HourEntryController.columnIndices.user].options.edit = {
      editor : "Autocomplete",
      dialogTitle: "Select users",
      dataType: "usersAndTeams",
      required: true,
      set: HourEntryModel.prototype.setUsers
    };
  this.addEntryConfig[HourEntryController.columnIndices.user].options.get = HourEntryModel.prototype.getUsers;
  this.addEntryConfig[HourEntryController.columnIndices.user].options.openOnRowEdit = false;  
  this.addEntryConfig[HourEntryController.columnIndices.user].options.decorator = DynamicsDecorators.userInitialsListDecorator;
  
  this.addEntryConfig[HourEntryController.columnIndices.actions].options.subViewFactory = HourEntryController.prototype.saveNewEntryButtonFactory;
  //TODO: need validators: [HourEntryModel.Validators.usersValidator] for adding new but not for the others
  //hack the validaiton manager row by row?
  this.showLogEffortRow();
  this.hourEntryTableView.render(); 
  this.logEffortController.openRowEdit();
  
  this.logEffortRow.getCellByName("effortSpent").getEditor().focus();
};
HourEntryListController.prototype.showLogEffortRow = function() {

  this.logEffortModel = ModelFactory.createObject(ModelFactory.types.hourEntry);
  this.logEffortModel.setParent(this.parentModel);
  this.logEffortModel.setHourEntryList(this.parentModel);
  this.logEffortModel.setUsers([], [PageController.getInstance().getCurrentUser()]);
  this.logEffortModel.setDate(new Date());
  this.logEffortModel.setHourEntryList(this.model);
 
  this.logEffortController = new HourEntryController(this.logEffortModel, null, this);
  
  this.logEffortRow = new DynamicTableRow(this.addEntryConfig);
  this.logEffortController.view = this.logEffortRow ;
  this.hourEntryTableView._createRow(this.logEffortRow , this.logEffortController, this.logEffortModel, "top");
  this.logEffortRow.autoCreateCells();
};

//note: will be called in different context
HourEntryListController.prototype.logEffort = function() {
  var me = this.parentController;
  
  if(this === me.logEffortController) {
    me.logEffortRow.remove();
    me.showLogEffortRow();
    me.hourEntryTableView.render();
    me.logEffortController.openRowEdit();
    me.logEffortRow.getCellByName("effortSpent").getEditor().focus();
    me.paintHourEntryButtons();
  }

};

HourEntryListController.prototype.paintHourEntryButtons = function() {
  this.limited = this.model.getHourEntries().length >= 5;
  if(this.limited) {
    this._addSeeAllButton();
  } 
};
HourEntryListController.prototype._addSeeAllButton = function() {
  if(!this.seeAllButton) {
    this.seeAllButton = new DynamicsButtons(this, [ {
      text : "See all",
      callback : jQuery.proxy(function() {
        this.showAllEntries();
      }, this)
    } ], this.hourEntryButtonsView);
    this.seeAllButton.draw();
  }
};


/**
 * Initialize and render the hour entry list.
 */
HourEntryListController.prototype.paint = function() {
  var me = this;
  HourEntryListContainer.initializeFor(this.parentModel, function(model) {
    me.model = model;
    me.paintHourEntryTable();
    me.paintHourEntryButtons();
    if(me.options.onUpdate) {
      me.model.addListener(function() {
        me.options.onUpdate();
      });
    }
  }, true);
};

HourEntryListController.prototype.initButtonsView = function() {  
  var hourEntryButtonsElement = $('<div style="text-align: right"></div>').appendTo(this.hourEntryListElement);
  this.hourEntryButtonsView = new ViewPart();
  this.hourEntryButtonsView.element = hourEntryButtonsElement;
};
HourEntryListController.prototype.reload = function() {
  this.model.reload();
};

HourEntryListController.prototype.showAllEntries = function() {
  this.limited = false;
  this.model.setLimitedEntries(false);
  this.hourEntryButtonsView.hide();
  this.reload();
};

/**
 * Initialize <code>DynamicTableConfiguration</code> for the
 * hour entry table.
 */
HourEntryListController.prototype.initConfig = function() {
  this.hourEntryTableConfig = new DynamicTableConfiguration(
      {
        rowControllerFactory : HourEntryListController.prototype.hourEntryControllerFactory,
        dataSource : HourEntryListContainer.prototype.getHourEntries,
        closeRowCallback: HourEntryListController.prototype.logEffort,
        cssClass: "ui-widget-content ui-corner-all",
        captionConfig: {
          cssClasses: "ui-helper-hidden"
        }
  });
  
  var date = {
    minWidth : 120,
    autoScale : true,
    title : "Date",
    headerTooltip: "Select date",
    get : HourEntryModel.prototype.getDate,
    decorator: DynamicsDecorators.dateTimeDecorator,
    sortCallback: HourEntryModel.dateComparator,
    defaultSortColumn: true,
    editable: true,
    edit: {
      editor: "Date",
      withTime: true,
      set : HourEntryModel.prototype.setDate,
      decorator: DynamicsDecorators.dateTimeDecorator
    }
  };
  var user = {
    minWidth : 120,
    autoScale : true,
    title : "User",
    headerTooltip: "Select users for this effort",
    get : HourEntryModel.prototype.getUser,
    decorator: DynamicsDecorators.userNameDecorator
  };
  var es = {
    minWidth : 30,
    autoScale : true,
    title : "ES",
    headerTooltip: "Effort spent",
    get : HourEntryModel.prototype.getMinutesSpent,
    decorator: DynamicsDecorators.exactEstimateDecorator,
    editable: true,
    columnName: "effortSpent",
    edit : {
      editor : "ExactEstimate",
      decorator: DynamicsDecorators.exactEstimateEditDecorator,
      set : HourEntryModel.prototype.setEffortSpent,
      required: false
    }
  };
  var el = {
	minWidth : 30,
	autoScale : true,
	title : "EL",
	headerTooltip: "Effort left",
	get : HourEntryModel.prototype.getEffortLeft,
	decorator : DynamicsDecorators.exactEstimateDecorator,
	editable: true,
	editableCallback: HourEntryModel.prototype.effortLeftEditable,
	columnName: "effortLeft",
	edit : {
	  editor : "ExactEstimate",
      decorator: DynamicsDecorators.exactEstimateEditDecorator,
      set : HourEntryModel.prototype.setEffortLeft,
      required: false
    }
  };
  var desc = {
    minWidth : 200,
    autoScale : true,
    headerTooltip: "Comment this effort",
    title : "Comment",
    editable: true,
    get : HourEntryModel.prototype.getDescription,
    edit : {
      editor : "Text",
      set : HourEntryModel.prototype.setDescription
    }
  };
  
  this.hourEntryTableConfig.addColumnConfiguration(HourEntryController.columnIndices.date, date);
  this.hourEntryTableConfig.addColumnConfiguration(HourEntryController.columnIndices.user, user);
  this.hourEntryTableConfig.addColumnConfiguration(HourEntryController.columnIndices.spentEffort, es);
  this.hourEntryTableConfig.addColumnConfiguration(HourEntryController.columnIndices.effortLeft, el);
  this.hourEntryTableConfig.addColumnConfiguration(HourEntryController.columnIndices.description, desc);
  
  this.hourEntryTableConfig.addColumnConfiguration(HourEntryController.columnIndices.actions, {
    minWidth : 50,
    autoScale : true,
    cssClass : 'hourEntry-row',
    title : " ",
    subViewFactory: HourEntryController.prototype.deleteButtonFactory
  });

};