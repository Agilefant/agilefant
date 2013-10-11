var LazyLoadedDialog = function LazyLoadedDialog() {
};

LazyLoadedDialog.prototype.init = function(options) {
  this.url = options.url;
  this.data = options.data;
  this.title = options.title;
  this.okCallback = options.okCallback;
  this.cancelCallback = options.cancelCallback;
  this.loadCallback = options.loadCallback;
  this.disableClose = options.disableClose;
  this.contentElement = $('<div><div style="text-align: center"><img src="static/img/pleasewait.gif" alt="Loading..." /></div></div>').appendTo(document.body);
  var me = this; 
  this.contentElement.dialog({
    modal: true,
    title: me.title,
    minHeight: '200',
    minWidth: 600,
    width: 600,
    position: 'center',
    resizable: false,
    open: function() {
      me.contentElement.load(me.url, me.data, function(responseText, textStatus, xhr) {
        if (textStatus === 'success') {
          if (options.hideCancelButton) {
            me.contentElement.dialog('option', 'buttons', {
               "Ok": function() {
                 me._ok();
               }
            });
          } else {
             me.contentElement.dialog('option', 'buttons', {
               "Cancel": function() {
                  me._cancel();
               },
               "Ok": function() {
                 me._ok();
               }
             });
          }
          if (me.loadCallback) {
        	//$(this.parentElement).find('Button')[0].focus();
            me.loadCallback(me.contentElement);
          }
        } else if (textStatus === 'error') {
          me.contentElement.empty();
          me.contentElement.dialog('option', 'buttons', {
            "Cancel": function() {
              me._cancel();
            }
          });
          $("<p>Error loading dialog</p>").appendTo(me.contentElement);
        }
        
        $(this.nextElementSibling).find('Button')[0].focus();
      });
  //    $(this.nextElementSibling).find('Button')[0].focus();
      //this.nextElementSibling.firstChild.focus(); // Focus to Cancel button
    }
  });
};

LazyLoadedDialog.prototype._ok = function() {
  if (this.okCallback) {
    this.okCallback();
  }
  if (!this.disableClose) {
    this.close();
  }
};
LazyLoadedDialog.prototype._cancel = function() {
  if (this.closeCallback) {
    this.closeCallback();
  }
    this.close();
};

LazyLoadedDialog.prototype.close = function() {
  this.contentElement.dialog("destroy").remove();
};

var LazyLoadedFormDialog = function LazyLoadedFormDialog() {
};

LazyLoadedFormDialog.prototype = new LazyLoadedDialog();

LazyLoadedFormDialog.prototype._ok = function() {
  if (this.okCallback) {
    var data = this.contentElement.find('form:eq(0)').serializeArray();
    var finalData = {};
    for (var i = 0, len = data.length; i < len; i++) {
      var element = data[i];
      finalData[element.name] = element.value;
    }
    this.okCallback(finalData);
  }
  if (!this.disableClose) {
    this.close();
  }
};