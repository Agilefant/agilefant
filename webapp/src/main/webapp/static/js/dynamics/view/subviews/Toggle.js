var DynamicTableToggleView = function DynamicTableToggleView(options, controller, parentView) {
  this.currentMode = DynamicTableToggleView.up;
  this.options = {
    collapse : function() {
    },
    expand : function() {
    },
    expanded : false
  };
  jQuery.extend(this.options, options);
  this.controller = controller;
  this.parentView = parentView;
  this.button = null;
};

DynamicTableToggleView.prototype = new CommonFragmentSubView();

DynamicTableToggleView.collapsed = 1;
DynamicTableToggleView.expanded = 2;
DynamicTableToggleView.currentId = 1;

DynamicTableToggleView.prototype._getCell = function(cell) {
  if(typeof cell === "number") {
    return this.parentView.getRow().getCell(cell);
  } else {
    return this.parentView.getRow().getCellByName(cell);
  }
};
DynamicTableToggleView.prototype.renderAlways = function() {
  return !this.rendered;
};

DynamicTableToggleView.prototype.render = function() {
  this.redered = true;
  this.button = $('#' + this.getId());
  if(this.parentView instanceof DynamicTableCell) {
    if(this.options.targetCell) {
      this.targetViews = [this._getCell(this.options.targetCell)];
    } else if(this.options.targetCells) {
      this.targetViews = [];
      for(var i = 0, len = this.options.targetCells.length; i < len; i++) {
        this.targetViews.push(this._getCell(this.options.targetCells[i]));
      }
    }
  }
};

DynamicTableToggleView.prototype.getHTML = function() {
  var cssClass, titleText;
  if(this.options.expanded) {
    cssClass = "dynamictable-collapse";
    titleText = "Collapse";
  } else {
    cssClass = "dynamictable-expand";
    titleText = "Expand";
  }
  var handle = $.proxy(function() {
    if (this.button.hasClass("dynamictable-expand")) {
      this.expand();
    } else {
      this.collapse();
    }
    return false;
  }, this);
  return '<div id="'+this.getId()+'" class="' + cssClass + '" title="' + titleText + '" onclick="'+DelegateFactory.create(handle)+'"><a href="javascript:void(0);"></a></div>';
};
DynamicTableToggleView.prototype.showCollapsed = function() {
  this.button.attr("title", "Expand").removeClass("dynamictable-collapse")
      .addClass("dynamictable-expand");
  this.currentMode = DynamicTableToggleView.collapsed;
};
DynamicTableToggleView.prototype.collapse = function() {
  this.showCollapsed();
  if(this.targetViews) {
    for(var i = 0, len = this.targetViews.length; i < len; i++) {
      this.targetViews[i].hide();
    }
  }
  if(this.options.collapse) {
    this.options.collapse.call(this.controller, this);
  }
};
DynamicTableToggleView.prototype.showExpanded = function() {
  this.button.attr("title", "Collapse").addClass("dynamictable-collapse")
    .removeClass("dynamictable-expand");
  this.currentMode = DynamicTableToggleView.expanded;
};
DynamicTableToggleView.prototype.expand = function() {
  this.showExpanded();
  if(this.targetViews) {
    for(var i = 0, len = this.targetViews.length; i < len; i++) {
      this.targetViews[i].render();
      this.targetViews[i].show();
    }
  }
  if(this.options.expand) {
    this.options.expand.call(this.controller, this);
  }
};
