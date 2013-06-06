var DynamicTableRowActions = function DynamicTableRowActions(items, controller, model, parentView) {
  this.items = items;
  this.controller = controller;
  this.model = model;
  this.parentView = parentView;
  this.menuOpen = false;
};

DynamicTableRowActions.prototype = new CommonFragmentSubView();

/**
 * @private
 */
DynamicTableRowActions.prototype.getHTML = function() {
  this.toggleMenuListener = $.proxy(function() {
    if (this.menuOpen) {
      this.close();
    } else {
      this.open();
    }
    return false;
  }, this);
  return '<div id="'+this.getId()+'" style="width: 68px;"><div class="actionColumn"><div class="edit" onclick="'+DelegateFactory.create(this.toggleMenuListener)+'">Edit &#8711;</div></div></div>';
};

/**
 * Display the menu
 */
DynamicTableRowActions.prototype.open = function() {
  var me = this;
  this.menuOpen = true;
  this.menu = $('<ul/>').appendTo(document.body).addClass("actionCell");
  
  var off = this.parentView.getElement().offset();
  var menuCss = {
    "position" : "absolute",
    "overflow" : "visible",
    "z-index" : "100",
    "white-space" : "nowrap",
    "top" : off.top + 18,
    "left" : off.left - 32,
    "-webkit-border-radius": "5px",
    "-moz-border-radius": "5px",
    "border-radius": "5px"
  };
  this.menu.css(menuCss);
  $.each(this.items, function(index, item) {
	var title = "";
	if (item.tooltip != null) {
		title = 'title="' + item.tooltip + '"';
	}
    var it = $('<li ' + title + '"/>').text(item.text).appendTo(me.menu);
    
    if (me._isEnabled(item)) {
      it.click(function() {
        me._click(item);
        me.close();
        return false;
      });
    }
    else {
      it.addClass("actionCell-rowaction-disabled");
      it.click(function() { return false; });
    }
  });
  this.menu.menuTimer({
    closeCallback: function() {
      me.close();
    }
  });
};

/**
 * Close the menu
 */
DynamicTableRowActions.prototype.close = function() {
  this.menu.menuTimer('destroy');
  this.menu.remove();
  this.menuOpen = false;
};

DynamicTableRowActions.prototype._click = function(item) {
  item.callback.call(this.controller, this.model, this.parentView);
};

DynamicTableRowActions.prototype._isEnabled = function(item) {
  var typeofEn = typeof item.enabled;
  if (typeofEn == "undefined") {
    return true;
  }
  if (typeofEn == "function") {
    var returned = item.enabled.call(this.controller, this.model, this.parentView);
    return !! returned;
  }
  return !! item.enabled;
};