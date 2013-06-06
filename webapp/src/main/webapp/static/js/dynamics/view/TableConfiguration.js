/**
 * @constructor
 * @param options
 */
var DynamicTableColumnConfiguration = function DynamicTableColumnConfiguration(options) {
	this.options = {
			width: null,
			minWidth: 0,
			fullWidth: false,
			autoScale: false,
			get: null,
			getView: null,
			tooltip: "",
			headerTooltip: "",
			decorator: null,
			title: "",
			sortCallback: null,
			defaultSortColumn: false,
			cssClass: null,
			editable: false,
			subViewFactory: null,
			visible: true,
			dragHandle: false,
			onDoubleClick: null,
			onClick: null,
			delayedRender: false,
			editableCallback: function() { return true; },
			draggableOnly: false,
			cssClassResolver: null,
			openOnRowEdit: true,
			columnName: null,
			edit: {
				decorator: null,
				items: null,
				set: function() {},
				editor: null,
				buttons: null
				//required: false,
				//minlength: null
			},
			filter: null
	};
	$.extend(this.options,options);
	this.options.edit.get = this.options.get;
};
DynamicTableColumnConfiguration.prototype.hasDelayedRender = function() {
  return this.options.delayedRender;
};
DynamicTableColumnConfiguration.prototype.getWidth = function() {
	return this.options.width;
};
DynamicTableColumnConfiguration.prototype.isAutoScale = function() {
	return this.options.autoScale;
};
DynamicTableColumnConfiguration.prototype.setWidth = function(width) {
	this.options.width = width;
};
DynamicTableColumnConfiguration.prototype.getMinWidth = function() {
	return this.options.minWidth;
};
DynamicTableColumnConfiguration.prototype.getGetter = function() {
	return this.options.get;
};
DynamicTableColumnConfiguration.prototype.getViewGetter = function() {
    if (this.options.getView) {
        return this.options.getView;
    }
    return this.options.get;
};
DynamicTableColumnConfiguration.prototype.getTooltip = function() {
	return this.options.tooltip;
};
DynamicTableColumnConfiguration.prototype.getTitle = function() {
	return this.options.title;
};
DynamicTableColumnConfiguration.prototype.getHeaderTooltip = function() {
	return this.options.headerTooltip;
};
DynamicTableColumnConfiguration.prototype.getDecorator = function() {
	return this.options.decorator;
};
DynamicTableColumnConfiguration.prototype.isFullWidth = function() {
	return this.options.fullWidth;
};
DynamicTableColumnConfiguration.prototype.isDragHandle = function() {
  return this.options.dragHandle;
};
DynamicTableColumnConfiguration.prototype.getCssClass = function() {
	return this.options.cssClass;
};
DynamicTableColumnConfiguration.prototype.getSortCallback = function() {
	return this.options.sortCallback;
};
DynamicTableColumnConfiguration.prototype.isDefaultSortColumn = function() {
	return this.options.defaultSortColumn;
};
DynamicTableColumnConfiguration.prototype.setDefaultSortColumn = function(defaultSortColumn) {
	this.options.defaultSortColumn = defaultSortColumn;
};
DynamicTableColumnConfiguration.prototype.isEditable = function() {
	return this.options.editable;
};
DynamicTableColumnConfiguration.prototype.isOpenOnRowEdit = function() {
  return this.options.openOnRowEdit;
};
DynamicTableColumnConfiguration.prototype.isSortable = function() {
	return this.options.sortCallback !== null;
};
DynamicTableColumnConfiguration.prototype.isFilterable = function() {
  return this.options.filter !== null;
};
DynamicTableColumnConfiguration.prototype.getFilter = function() {
  return this.options.filter;
};
DynamicTableColumnConfiguration.prototype.isVisible = function() {
  return this.options.visible;
};
DynamicTableColumnConfiguration.prototype.getEditOptions = function() {
	return this.options.edit;
};
DynamicTableColumnConfiguration.prototype.getEditableCallback = function() {
  return this.options.editableCallback;
};
DynamicTableColumnConfiguration.prototype.getSubViewFactory = function() {
	return this.options.subViewFactory;
};
DynamicTableColumnConfiguration.prototype.getDoubleClickCallback = function() {
  return this.options.onDoubleClick;
};
DynamicTableColumnConfiguration.prototype.getClickCallback = function() {
  return this.options.onClick;
};
DynamicTableColumnConfiguration.prototype.getColumnName = function() {
  return this.options.columnName;
};

DynamicTableCaptionItemConfiguration = function(options) {
  
  this.options = {
      text: '',
      name: '',
      tooltip: null,
      callback: function() {},
      visible: true,
      connectWith: null,
      cssClass: null
  };
  jQuery.extend(this.options, options);
};

DynamicTableCaptionItemConfiguration.prototype.getText = function() {
  return this.options.text;
};

DynamicTableCaptionItemConfiguration.prototype.getTooltip = function() {
  return this.options.tooltip;
};

DynamicTableCaptionItemConfiguration.prototype.getCallback = function() {
  return this.options.callback;
};

DynamicTableCaptionItemConfiguration.prototype.isVisible = function() {
  return this.options.visible;
};

DynamicTableCaptionItemConfiguration.prototype.getConnected = function() {
  return this.options.connectWith;
};

DynamicTableCaptionItemConfiguration.prototype.getName = function() {
  return this.options.name;
};

DynamicTableCaptionItemConfiguration.prototype.getCssClass = function() {
  return this.options.cssClass;
};

	
/**
 * @constructor
 * @param options
 */
var DynamicTableConfiguration = function(options) {
	this.columns = [];
	this.captionConfig = {
	    captionItems: [],
	    cssClasses: ""
	};
	
	this.options = {
			rowControllerFactory: $.noop,
			afterFirstRender: $.noop,
			dataSource: null,
			dropOptions: null,
			tableDroppable: false,
			rowDroppable: false,
			alwaysDrop: false,
			caption: null,
			captionStyles: null,
			closeRowCallback: CommonController.prototype.closeRowEdit,
			sortCallback: null,
			editableCallback: function() { return true; },
	    validators: [ ],
	    beforeCommitFunction: null,
	    preventCommit: false,
			sortOptions: {
			  items: "> div.dynamicTableDataRow",
	      handle: "div." + DynamicTable.cssClasses.dragHandle
			}
	};
	jQuery.extend(this.options, options);
	jQuery.extend(this.captionConfig, this.options.captionConfig);
};

DynamicTableConfiguration.prototype.getCloseRowCallback = function() {
  return this.options.closeRowCallback;
};
DynamicTableConfiguration.prototype.getRowControllerFactory = function() {
	return this.options.rowControllerFactory;
};
DynamicTableConfiguration.prototype.getDataSource = function() {
	return this.options.dataSource;
};
DynamicTableConfiguration.prototype.isSortable = function() {
  return this.options.sortCallback !== null;
};
DynamicTableConfiguration.prototype.isDraggableOnly = function() {
    return this.options.draggableOnly;
  };
DynamicTableConfiguration.prototype.getSortOptions = function() {
  return this.options.sortOptions;
};
DynamicTableConfiguration.prototype.getSortCallback = function() {
  return this.options.sortCallback;
};
DynamicTableConfiguration.prototype.getCssClassResolver = function() {
  return this.options.cssClassResolver;
};
DynamicTableConfiguration.prototype.getColumnConfiguration = function(columnNum) {
	return this.columns[columnNum];
};
DynamicTableConfiguration.prototype.addCaptionItem = function(options) {
  this.captionConfig.captionItems.push(new DynamicTableCaptionItemConfiguration(options));
};
DynamicTableConfiguration.prototype.getCaptionConfiguration = function() {
	return this.captionConfig;
};
DynamicTableConfiguration.prototype.getColumns = function() {
	return this.columns;
};
DynamicTableConfiguration.prototype.getCaption = function() {
  return this.options.caption;
};
DynamicTableConfiguration.prototype.getEditableCallback = function() {
  return this.options.editableCallback;
};
DynamicTableConfiguration.prototype.addColumnConfiguration = function(columnNum, options) {
	this.columns[columnNum] = new DynamicTableColumnConfiguration(options);
};
DynamicTableConfiguration.prototype.setColumnConfiguration = function(columnNum, config) {
	this.columns[columnNum] = config;
};
DynamicTableConfiguration.prototype.getDropOptions = function() {
  return this.options.dropOptions;
};
DynamicTableConfiguration.prototype.isTableDroppable = function() {
  return this.options.tableDroppable;
};
DynamicTableConfiguration.prototype.isRowDroppable = function() {
  return this.options.rowDroppable;
};
DynamicTableConfiguration.prototype.getDataType = function() {
  return this.options.dataType;
};
DynamicTableConfiguration.prototype.getAfterFirstRender = function() {
  return this.options.afterFirstRender; 
};