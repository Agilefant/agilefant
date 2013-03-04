var UserCommentWidget = function UserCommentWidget(element, storyId) {
  this.element = $('<div />').appendTo(element);
  this.container = element;
  this.storyId = storyId;
  this.initConfig();
  this.model = new HourEntryListContainer();
  //this.effortByDayElement = $('<div class="details"></div>').appendTo(element);
  this.entriesByDay = new DynamicTable(this, this.model, this.hourEntryTableConfig,
      this.effortByDayElement);
  //this.effortByDayElement.hide();
  this.reload();
};

UserCommentWidget.prototype.reload = function() {
	var me = this;
	var storyId = this.storyId;
	this.element.load("ajax/retrieveStoryComment.action",{objectId: storyId}, function() {
		//me._registerSpentEffortEvents(); 
	});
};
UserCommentWidget.prototype._parseDateValues = function(strValue) {
  var parts = strValue.split("-");
  if(parts.length != 2) {
    console.log("SHOULD NEVER GET HERE.");
    return {};
  }
  return { week: parts[1], year: parts[0] };
};
UserCommentWidget.prototype._registerSpentEffortEvents = function() {
  var me = this;
  //next, prev and current week
  //me.effortByDayElement.hide();
  this.element.find("a:not(.detailLink)").click(function() {
    me.element.load(this.href, function() { 
      me._registerSpentEffortEvents();
    });
    return false;
  });
  
  //week select
  this.element.find("select").change(function() {
    var data = me._parseDateValues($(this).val());
    data.userId = me.userId;
    data.userTimeZone = new Date().getUserTimeZone();
    me.element.load("weeklySpentEffort.action",data, function(data) { 
      me._registerSpentEffortEvents();
    });
  });
  
  //detailed spent effort for day
  this.element.find('a.detailLink').click(function(event) {
    $('a.detailLink',me.element).removeClass("detailedEffort");
    $(this).addClass("detailedEffort");
    $.ajax({
      url: this.href,
      method: 'post',
      cache: false,
      dataType: 'json',
      success: function(data,status) {
        me.model.setData(data);
        //me.effortByDayElement.show();
        me.entriesByDay.render();
      }
    });
    return false;
  });
};


UserCommentWidget.prototype.hourEntryControllerFactory = function(view, model) {
  var hourEntryController = new HourEntryController(model, view, this);
  return hourEntryController;
};

UserCommentWidget.prototype.initConfig = function() {
  this.hourEntryTableConfig = new DynamicTableConfiguration(
      {
        rowControllerFactory : UserCommentWidget.prototype.hourEntryControllerFactory,
        dataSource : HourEntryListContainer.prototype.getHourEntries,
        cssClass: "ui-widget-content ui-corner-all",
        captionConfig: {
          cssClasses: "ui-helper-hidden"
        }
  });
  
  var date = {
    minWidth : 40,
    autoScale : true,
    title : "Time",
    get : HourEntryModel.prototype.getDate,
    decorator: DynamicsDecorators.timeDecorator,
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
  var es = {
    minWidth : 40,
    autoScale : true,
    title : "ES",
    get : HourEntryModel.prototype.getMinutesSpent,
    decorator: DynamicsDecorators.exactEstimateDecorator,
    editable: true,
    columnName: "effortSpent",
    edit : {
      editor : "ExactEstimate",
      decorator: DynamicsDecorators.exactEstimateEditDecorator,
      set : HourEntryModel.prototype.setEffortSpent,
      //required: true
    }
  };
  var el = {
    minWidth : 40,
    autoScale : true,
    title : "EL",
    get : TaskModel.prototype.getEffortLeft,
    decorator: DynamicsDecorators.exactEstimateSumDecorator,
    editable : true,
    columnName : "effortLeft",
    edit : {
	  editor : "ExactEstimate",
      decorator: DynamicsDecorators.exactEstimateEditDecorator,
      set : TaskModel.prototype.setEffortLeft,
      //required: true
    }
  };
  var desc = {
    minWidth : 200,
    autoScale : true,
    title : "Comment",
    editable: true,
    get : HourEntryModel.prototype.getDescription,
    edit : {
      editor : "Text",
      set : HourEntryModel.prototype.setDescription
    }
  };
  
  var context = {
      minWidth : 200,
      autoScale : true,
      title : "Context",
      editable: false,
      get : HourEntryModel.prototype.getContext,
      decorator: DynamicsDecorators.taskContextDecorator
    };
  
  this.hourEntryTableConfig.addColumnConfiguration(0, date);
  this.hourEntryTableConfig.addColumnConfiguration(1, es);
  this.hourEntryTableConfig.addColumnConfiguration(2, el);
  this.hourEntryTableConfig.addColumnConfiguration(3, desc);
  this.hourEntryTableConfig.addColumnConfiguration(4, context);

};