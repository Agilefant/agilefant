
/**
 * Validation messages.
 */
var ValidationMessages = {
  textField: {
    required: "Required field",
    unique: "Already exists"
  },
  email: {
    notValid: "Email not in correct form: abc@def.com"
  },
  number: {
    isNotInteger: "Please enter an integer",
    mustBeGreater: "Value must be greater or equal than ",
    mustBeLower: "Value must be lower or equal than "
  },
  value: {
	  invalid: "Story value must be equal to or greater than zero"
  },
  estimate: {
    invalid: "Incorrect format - Please enter e.g. 10 or 10pt or 10points"
  },
  exactEstimate: {
    invalid: "Incorrect format (e.g. 2, 2.5 or 2h 30min)"
  },
  password: {
    empty: "Password is empty"
  }
};


/**
 * Table cell editors for Agilefant Dynamics library.
 * <code>CommonEditor</code> is the base class for all cell editors.
 */
var TableEditors = {};
TableEditors.getEditorClassByName = function(name) {
    if (TableEditors[name]) {
        return TableEditors[name];
    }
    return null;
};

TableEditors.openOnRowEdit = function(name) {
  var EditorClass = TableEditors.getEditorClassByName(name);
  if (EditorClass.prototype instanceof TableEditors.DialogEditor) {
    return false;
  }
  return true;
};

TableEditors.currentId = 1;

/**
 * Common constructor for all <code>TableEditors</code>
 * @constructor
 * @member TableEditors
 */
TableEditors.CommonEditor = function() {};
TableEditors.CommonEditor.defaultOptions = {
    /**
     * The getter function of the model.
     * 
     * Default: function() {}
     * Will be called in the model object's context.
     * @member TableEditors.CommonEditor
     */
    get: function() {},
    /**
     * The setter function of the model.
     * 
     * Default: function() {}
     * Will be called in the model object's context.
     * @member TableEditors.CommonEditor
     */
    set: function() {}
};

TableEditors.CommonEditor.prototype.generateId = function() {
  return "editor-" + (++TableEditors.currentId);
};

TableEditors.CommonEditor.prototype.init = function(element, model, options) {
  this.editRow = false;
  this.fieldName = "";
  this.element = element;
  this.model   = model;
  
  this.options = {};
  jQuery.extend(this.options, TableEditors.CommonEditor.defaultOptions);
  jQuery.extend(this.options, options);
  
  this.errorMessages = [];
  this.previousValueValid = false;
  
  this.element.trigger("editorOpening");
};
/**
 * Close the editor.
 */
TableEditors.CommonEditor.prototype.close = function() {
  this.element.removeClass('dynamics-validation-invalid');
  this.element.trigger("editorClosing");
};

/**
 * Focus the primary edit field of the editor.
 * Does nothing, meant to be overridden by subclasses.
 */
TableEditors.CommonEditor.prototype.focus = function() {};

TableEditors.CommonEditor.prototype._requestCancel = function() {
  this.element.trigger("cancelRequested", [this]);
};
TableEditors.CommonEditor.prototype._requestCancelIfNotInRowEdit = function() {
  if (!this.editRow) {
    this._requestCancel();
  }
};
TableEditors.CommonEditor.prototype._requestSave = function() {
  this.element.trigger("storeRequested", [this]);
};
TableEditors.CommonEditor.prototype._requestSaveIfNotInRowEdit = function() {
  if (!this.editRow) {
    this._requestSave();
  }
};

TableEditors.CommonEditor.prototype._fireTransactionEditEvent = function() {
  this.element.trigger("transactionEditEvent");
};

/**
 * Sets the editors full row edit state.
 */
TableEditors.CommonEditor.prototype.setInRowEdit = function(inRowEdit) {
  this.editRow = inRowEdit;
};

/**
 * Sets the fields name.
 */
TableEditors.CommonEditor.prototype.setFieldName = function(name) {
  this.fieldName = name;
};

/**
 * 
 */
TableEditors.CommonEditor.prototype.getEditorValue = function() {};
TableEditors.CommonEditor.prototype.setEditorValue = function() {};

TableEditors.CommonEditor.prototype.addErrorMessage = function(message) {
  this.errorMessages.push(message);
};
TableEditors.CommonEditor.prototype._validate = function() {
  return true;
};

TableEditors.CommonEditor.prototype.isFocused = function() {
  return this.focused;
};

/**
 * Change event listener.
 */
TableEditors.CommonEditor.prototype._registerEditField = function(element) {
  var me = this;
  element.keydown(function(event) {
    me._handleKeyEvent(event);
    return true;
  });
  element.blur(function(event) {
    me._requestSaveIfNotInRowEdit();
    me.element.trigger("DynamicsBlur");
    me.focused = false;
  });
  element.focus(function() {
    me.element.trigger("DynamicsFocus");
    me.focused = true;
  });
  
  element.data("editor", this).addClass("dynamics-editor-element");
};

/**
 * Execute field value validators.
 * Does not run composite validations.
 */
TableEditors.CommonEditor.prototype.runValidation = function() {
  this.errorMessages = [];
  var valid = this._validate();
  if (!valid) {
    this.element.trigger("validationInvalid", [new DynamicsEvents.ValidationInvalid(this.fieldName, this.errorMessages)]);
    this.element.addClass('dynamics-validation-invalid');
  }
  else {
    if (!this.previousValueValid) {
      this.element.trigger("validationValid", [new DynamicsEvents.ValidationValid(this.fieldName)]);    
    }
    this.options.set.call(this.model, this.getEditorValue());
    this.element.trigger("transactionEditEvent");
    this.element.removeClass('dynamics-validation-invalid');
  }
  this.previousValueValid = valid;
  return valid;
};

TableEditors.CommonEditor.prototype._handleKeyEvent = function(event) {
  if (event.keyCode === 27) {
    event.stopPropagation();
    event.preventDefault();
    this._requestCancel();
    return false;
  } else if (event.keyCode === 13) {
    event.stopPropagation();
    event.preventDefault();
    this._requestSave();
    return false;
  }
};

/*
 * TEXT FIELD EDITORS
 */

/**
 * Abstract common constructor for all single line text input editors.
 * @constructor
 * @base TableEditors.CommonEditor
 * @member TableEditors
 */
TableEditors.TextFieldEditor = function() {};
TableEditors.TextFieldEditor.prototype = new TableEditors.CommonEditor();
/**
 * Default options for <code>TableEditors.TextFieldEditor</code>
 */
TableEditors.TextFieldEditor.defaultOptions = {
  /**
   * Whether the field is required or not.
   * Default: false
   * @member TableEditors.TextFieldEditor */
  required: false,
  /**
   * The css width of the input element.
   * Default: 95%
   * @member TableEditors.TextFieldEditor */
  size: '95%',
  
  /**
   * The type of the <code>&lt;input&gt;</code> element.
   * Default: "text"
   * @memer TableEditors.TextFieldEditor
   */
  fieldType: "text"
};
/**
 * Initializes a TextFieldEditor.
 * Will call TableEditors.CommonEditor.init.
 */
TableEditors.TextFieldEditor.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.TextFieldEditor.defaultOptions);
  jQuery.extend(opts, options);
  TableEditors.CommonEditor.prototype.init.call(this, element, model, opts);
  
  this.textField = $('<input id="'+this.generateId()+'" type="' + this.options.fieldType + '"/>')
    .appendTo(this.element).width(this.options.size);
  this._registerEditField(this.textField);
};

/**
 * Close the TextFieldEditor and remove its input elements.
 */
TableEditors.TextFieldEditor.prototype.close = function() {
  this.textField.remove();
  TableEditors.CommonEditor.prototype.close.call(this);
};

/**
 * Focuses the text field.
 */
TableEditors.TextFieldEditor.prototype.focus = function() {
  this.textField.focus();
};

/**
 * Sets the new value or uses <code>options.get</code> if the parameter
 * <code>value</code> is <code>undefined</code>.
 * @param {Object} value the value to set
 */
TableEditors.TextFieldEditor.prototype.setEditorValue = function(value) {
  if (!value) {
    value = this.options.get.call(this.model);
  }
  if (this.options.decorator) {
    value = this.options.decorator(value);
  }
  this.textField.val(value);
};
/**
 * Get the text field editor's value.
 */
TableEditors.TextFieldEditor.prototype.getEditorValue = function() {
  return this.textField.val();
};


TableEditors.TextFieldEditor.prototype._validate = function() {
  var valid = true;
  var value = jQuery.trim(this.textField.val());
  
  if (this.options.required) {
    if (!value || value.length === 0) {
      valid = false;
      this.addErrorMessage(ValidationMessages.textField.required);
    }
  }
  return TableEditors.CommonEditor.prototype._validate.call(this) && valid;
};

/**
 * Basic text input.
 * 
 * @constructor
 * @base TableEditors.CommonEditor
 */
TableEditors.Text = function(element, model, options) {
  this.init(element, model, options);
  this.setEditorValue();
  this.focus();
};
TableEditors.Text.prototype = new TableEditors.TextFieldEditor();
/**
 * Default options for <code>TableEditors.Text</code>
 */
TableEditors.Text.defaultOptions = {
  /**
   * Whether the field is required or not.
   * Default: false
   * @member TableEditors.Text */
  required: false
};
/**
 * Initializes a TextFieldEditor.
 * Will call TableEditors.TextFieldEditor.init.
 */
TableEditors.Text.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.Text.defaultOptions);
  jQuery.extend(opts, options);
  TableEditors.TextFieldEditor.prototype.init.call(this, element, model, opts);
};

TableEditors.Text.prototype._validate = function() {
  return TableEditors.TextFieldEditor.prototype._validate.call(this);
};

/**
 * Unique team name input.
 * 
 * @constructor
 * @base TableEditors.Text
 */
TableEditors.TeamName = function(element, model, options) {
  this.init(element, model, options);
  this.setEditorValue();
  this.focus();
};
TableEditors.TeamName.prototype = new TableEditors.TextFieldEditor();
/**
 * Default options for <code>TableEditors.TeamName</code>
 */
TableEditors.TeamName.defaultOptions = {
  /**
   * Whether the field is required or not.
   * Default: false
   * @member TableEditors.TeamName */
  required: true
};

TableEditors.TeamName.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.TeamName.defaultOptions);
  jQuery.extend(opts, options);
  TableEditors.TextFieldEditor.prototype.init.call(this, element, model, opts);
};

TableEditors.TeamName.prototype._validate = function() {
  var value = jQuery.trim(this.textField.val());
  var result = true;
  
  jQuery.ajax({
    async: false,
    url: 'ajax/retrieveAllTeams.action',
    cache: false,
    dataType: "json",
    success: function(data,status) {
      $.each(data, function(index, team) {
    	if(team['name'] == value) {
    	  result = false;
    	}
      })
    },
    error: function(request, status, error) {
      MessageDisplay.Error("Unable to load data for checking teams");
    }
  });
  
  if(!result) {
	this.addErrorMessage(ValidationMessages.textField.unique);
  }
  
  return TableEditors.TextFieldEditor.prototype._validate.call(this) && result;
};

/**
 * Email input.
 * 
 * @constructor
 * @base TableEditors.CommonEditor
 */
TableEditors.Email = function(element, model, options) {
  this.init(element, model, options);
  this.setEditorValue();
  this.focus();
};
TableEditors.Email.prototype = new TableEditors.TextFieldEditor();
/**
 * Default options for <code>TableEditors.Email</code>
 */
TableEditors.Email.defaultOptions = {
  /**
   * Whether the field is required or not.
   * Default: true
   * @member TableEditors.Email */
  required: true
};
/**
 * Initializes a TextFieldEditor.
 * Will call TableEditors.TextFieldEditor.init.
 */
TableEditors.Email.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.Email.defaultOptions);
  jQuery.extend(opts, options);
  TableEditors.TextFieldEditor.prototype.init.call(this, element, model, opts);
};

TableEditors.Email.prototype._validate = function() {
  var valid = true;
  var emailRegEx = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
  if (!this.textField.val().match(emailRegEx)) {
      valid = false;
      this.addErrorMessage(ValidationMessages.email.notValid);
  }
  return TableEditors.TextFieldEditor.prototype._validate.call(this) && valid;
};

/**
 * Number input.
 * 
 * @constructor
 * @base TableEditors.CommonEditor
 */
TableEditors.Number = function(element, model, options) {
  this.init(element, model, options);
  this.setEditorValue();
  this.focus();
};
TableEditors.Number.prototype = new TableEditors.TextFieldEditor();
/**
 * Default options for <code>TableEditors.Number</code>
 */
TableEditors.Number.defaultOptions = {
  /**
   * Whether the field is required or not.
   * Default: true
   * @member TableEditors.Number */
  required: true,
  
  /**
   * Minimum value of the field.
   * Default: null
   * @member TableEditors.Number
   */
  minValue: null,
  
  /**
   * Maximum value of the field.
   * Default: null
   * @member TableEditors.Number
   */
  maxValue: null
};
/**
 * Initializes a Number editor.
 * Will call TableEditors.TextFieldEditor.init.
 */
TableEditors.Number.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.Number.defaultOptions);
  jQuery.extend(opts, options);
  TableEditors.TextFieldEditor.prototype.init.call(this, element, model, opts);
};

TableEditors.Number.prototype._validate = function() {
  var valid = true;
  var value = jQuery.trim(this.textField.val());
  
  var isInt = (value.toString().search(/^[0-9]*$/) === 0);
  var intValue = parseInt(value, 10);
  
  if (!isInt) {
    this.addErrorMessage(ValidationMessages.number.isNotInteger);
    valid = false;
  }
  
  if (this.options.minValue !== null && isInt && this.options.minValue > intValue) {
    this.addErrorMessage(ValidationMessages.number.mustBeGreater + this.options.minValue);
    valid = false;
  }
  
  if (this.options.maxValue !== null && isInt && this.options.maxValue < intValue) {
    this.addErrorMessage(ValidationMessages.number.mustBeLower + this.options.maxValue);
    valid = false;
  }
  
  return TableEditors.TextFieldEditor.prototype._validate.call(this) && valid;
};



/**
 * Estimate input.
 * 
 * @constructor
 * @base TableEditors.CommonEditor
 */
TableEditors.Estimate = function(element, model, options) {
  this.init(element, model, options);
  this.setEditorValue();
  this.focus();
};
TableEditors.Estimate.prototype = new TableEditors.TextFieldEditor();
/**
 * Default options for <code>TableEditors.Estimate</code>
 */
TableEditors.Estimate.defaultOptions = {
  /**
   * Whether the field is required or not.
   * Default: false
   * @member TableEditors.Estimate */
  required: false
};
/**
 * Initializes a Estimate editor.
 * Will call TableEditors.TextFieldEditor.init.
 */
TableEditors.Estimate.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.Estimate.defaultOptions);
  jQuery.extend(opts, options);
  TableEditors.TextFieldEditor.prototype.init.call(this, element, model, opts);
};

TableEditors.Estimate.prototype._validate = function() {
  var valid = true;
  var value = jQuery.trim(this.textField.val());
  
  var format = new RegExp("^([0-9]*)$"); // Removed: (pt|points)?
  
  if (!format.test(value)) {
    valid = false;
    this.addErrorMessage(ValidationMessages.estimate.invalid);
  }
  
  return TableEditors.TextFieldEditor.prototype._validate.call(this) && valid;
};

/**
 * Estimate input.
 * 
 * @constructor
 * @base TableEditors.CommonEditor
 */
TableEditors.StoryValue = function(element, model, options) {
  this.init(element, model, options);
  this.setEditorValue();
  this.focus();
};
TableEditors.StoryValue.prototype = new TableEditors.TextFieldEditor();
/**
 * Default options for <code>TableEditors.Estimate</code>
 */
TableEditors.StoryValue.defaultOptions = {
  /**
   * Whether the field is required or not.
   * Default: false
   * @member TableEditors.Estimate */
  required: false
};
/**
 * Initializes a Estimate editor.
 * Will call TableEditors.TextFieldEditor.init.
 */
TableEditors.StoryValue.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.Estimate.defaultOptions);
  jQuery.extend(opts, options);
  TableEditors.TextFieldEditor.prototype.init.call(this, element, model, opts);
};

TableEditors.StoryValue.prototype._validate = function() {
  var valid = true;
  var value = jQuery.trim(this.textField.val());
  
  var format = new RegExp("^([0-9]*)$"); // Removed: (pt|points)?
  
  if (!format.test(value)) {
    valid = false;
    this.addErrorMessage(ValidationMessages.value.invalid);
  }
  
  return TableEditors.TextFieldEditor.prototype._validate.call(this) && valid;
};


/**
 * ExactEstimate input.
 * 
 * @constructor
 * @base TableEditors.CommonEditor
 */
TableEditors.ExactEstimate = function(element, model, options) {
  this.init(element, model, options);
  this.setEditorValue();
  this.focus();
};
TableEditors.ExactEstimate.prototype = new TableEditors.TextFieldEditor();
/**
 * Default options for <code>TableEditors.ExactEstimate</code>
 */
TableEditors.ExactEstimate.defaultOptions = {
  /**
   * Whether the field is required or not.
   * Default: false
   * @member TableEditors.ExactEstimate */
  required: false
};
/**
 * Initializes a ExactEstimate editor.
 * Will call TableEditors.TextFieldEditor.init.
 */
TableEditors.ExactEstimate.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.ExactEstimate.defaultOptions);
  jQuery.extend(opts, options);
  TableEditors.TextFieldEditor.prototype.init.call(this, element, model, opts);
};

TableEditors.ExactEstimate.prototype._validate = function() {
  var value = jQuery.trim(this.textField.val());
  
  if (this.options.acceptNegative) {
      var minusTest = /^\-[0-9]/;
      if (value.match(minusTest)) {
          value = value.substr(1);
      }
  }
  var majorOnly = /^[0-9]+h?$/; // 10h
  var minorOnly = /^([1-9]|[1-5]\d)min$/; // 10min
  var majorAndMinor = /^[ ]*[0-9]+h[ ]+[0-9]+min$/;
  var shortFormat = /^[0-9]+\.[0-9]+h?$/;
  
  var valid = (value.match(majorOnly) || value.match(minorOnly)
          || value.match(majorAndMinor) || value.match(shortFormat)
          || !value);
  if (!valid) {
      this.addErrorMessage(ValidationMessages.exactEstimate.invalid);
  }
  return TableEditors.TextFieldEditor.prototype._validate.call(this) && valid;
};



/**
 * Date selector.
 * 
 * @constructor
 * @base TableEditors.CommonEditor
 */
TableEditors.Date = function(element, model, options) {
  this.init(element, model, options);
  this.setEditorValue();
  element.attr('inRowEdit') == 'true' ? "" : this.focus();
};
TableEditors.Date.prototype = new TableEditors.TextFieldEditor();
/**
 * Default options for <code>TableEditors.Date</code>.
 * 
 * Date is always required
 */
TableEditors.Date.defaultOptions = {
  /**
   * Whether the editor should include time in HH:MM.
   * Default: true
   * @member TableEditors.Date */
  withTime: true,
  
  /**
   * The input element's width.
   * Default: 80%
   * @member TableEditors.Date
   */
  size: "80%"
};
/**
 * Initializes a Date selector.
 * Will call TableEditors.TextFieldEditor.init.
 */
TableEditors.Date.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.Date.defaultOptions);
  jQuery.extend(opts, options);
  TableEditors.TextFieldEditor.prototype.init.call(this, element, model, opts);
  
  this.datepickerOpen = false;
  var me = this;
  
  this.textField.datepicker( {
      dateFormat : 'yy-mm-dd',
      numberOfMonths : 3,
      showButtonPanel : true,
      beforeShow : function() {
          me.datepickerOpen = true; 
          pattern = /(\d|[0-1][0-9]|2[0-3]):(\d|[0-5][0-9])$/;
          var index = me.textField.val().search(pattern);
          if (index === -1) {
            me.oldHoursAndMinutes = '12:00';
          }
          else {
            me.oldHoursAndMinutes = me.textField.val().substr(index, 5);
          }
      },
      onSelect : function() {
          var newValue = me.textField.val();
          if (me.options.withTime) {
              newValue = me.textField.val() + " " + me.oldHoursAndMinutes;
          }
          me.textField.val(newValue);
          me.textField.focus();
      },
      onClose : function() {
          me.datepickerOpen = false;
          me.textField.focus();
      },
      buttonImage : 'static/img/calendar.gif',
      buttonImageOnly : true,
      constrainInput : false
  });
};

TableEditors.Date.prototype.close = function() {
  this.element.find('img').remove();
  this.textField.datepicker('hide');
  this.textField.datepicker('destroy');
  TableEditors.TextFieldEditor.prototype.close.call(this);
};

TableEditors.Date.prototype._validate = function() {
  var pattern;
  var errorMessage = "";
  if (this.options.withTime) {
      pattern = new RegExp("^\\d{4}-([1-9]|0[1-9]|1[0-2])-([1-9]|0[1-9]|[1-2][0-9]|3[0-1]) ([0-1][0-9]|2[0-3]):([0-5][0-9])$");
      errorMessage = "Invalid format (yyyy-mm-dd hh:mm)";
  } else {
      pattern = new RegExp("^\\d{4}-([1-9]|0[1-9]|1[0-2])-([1-9]|0[1-9]|[1-2][0-9]|3[0-1])$");
      errorMessage = "Invalid format (yyyy-mm-dd)";
  }
  var value = jQuery.trim(this.textField.val());
  var valid = pattern.test(value);
  if(!valid) {
    this.addErrorMessage(errorMessage);
  }
  return TableEditors.TextFieldEditor.prototype._validate.call(this) && valid;
};

TableEditors.Date.prototype._registerEditField = function(element) {
  var me = this;
  element.blur(function(event) {
    if (me.datepickerOpen)
      return false;
    me._requestSaveIfNotInRowEdit();
    me.element.trigger("DynamicsBlur");
    me.focused = false;
  });
  element.keydown(function(event) {
    me._handleKeyEvent(event);
    return true;
  });
  element.focus(function() {
    me.element.trigger("DynamicsFocus");
    me.focused = true;
  });
  element.data("editor", this).addClass("dynamics-editor-element");
};

TableEditors.Date.prototype.getEditorValue = function() {
  return Date.fromString(this.textField.val()).getTime();
};

/*
 * PASSWORD INPUT
 */
/**
 * Password input
 * 
 * @constructor
 * @base TableEditors.CommonEditor
 */
TableEditors.Password = function(element, model, options) {
  this.init(element, model, options);
  this.focus();
};
TableEditors.Password.prototype = new TableEditors.TextFieldEditor();
/**
 * Default options for <code>TableEditors.Password</code>
 */
TableEditors.Password.defaultOptions = {
  /**
   * Whether the field is required or not.
   * Default: true
   * @member TableEditors.Password */
  required: true,
  
  /**
   * The type of the <code>&lt;input&gt;</code> element.
   * Default: "password"
   * @member TableEditors.Password
   */
  fieldType: "password"
};
/**
 * Initializes a TextFieldEditor.
 * Will call TableEditors.TextFieldEditor.init.
 */
TableEditors.Password.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.Password.defaultOptions);
  jQuery.extend(opts, options);
  TableEditors.TextFieldEditor.prototype.init.call(this, element, model, opts);
};

TableEditors.Password.prototype.getEditorValue = function() {
  return this.textField.val();
};

TableEditors.Password.prototype._validate = function() {
  return TableEditors.TextFieldEditor.prototype._validate.call(this);
};


/*
 * INLINE AUTOCOMPLETE 
 */
/**
 * Inline autocomplete element.
 * For single selection only.
 * @base TableEditors.CommonEditor
 */
TableEditors.InlineAutocomplete = function(element, model, options) {
  this.init(element, model, options);
  this.setEditorValue();
  this.focus();
};
TableEditors.InlineAutocomplete.prototype = new TableEditors.CommonEditor();

TableEditors.InlineAutocomplete.defaultOptions = {
  /** @member TableEditors.InlineAutocomplete */
  required: true,
  /** @member TableEditors.InlineAutocomplete */
  dataType: "",
  /** @member TableEditors.InlineAutocomplete */
  size: "95%"
};

TableEditors.InlineAutocomplete.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.InlineAutocomplete.defaultOptions);
  jQuery.extend(opts, options);
  TableEditors.CommonEditor.prototype.init.call(this, element, model, opts);
  
  this.textField = $('<input type="text" />').width(this.options.size).appendTo(this.element);
  
  this._registerEditField(this.textField);
  
  this.textField.autocomplete({
    source: jQuery.proxy(function(request, response) {
      response(this._getData(request.term));
    },this)
  });
  
  this.setEditorValue();
};
TableEditors.InlineAutocomplete.prototype._registerEditField = function(element) {
  element.bind('autocompleteselect', jQuery.proxy(function(event, ui) {
    this.value = ModelFactory.updateObject(ui.item.object);
    this.setEditorValue(this.value);
    this._requestSaveIfNotInRowEdit();
  }, this));
  
  element.bind('autocompleteclose', jQuery.proxy(function(event, ui) {
    this.autocompleteOpen = false;
  }, this));
  
  element.bind('autocompleteopen', jQuery.proxy(function(event, ui) {
    this.autocompleteOpen = true;
  },this));
  
  TableEditors.CommonEditor.prototype._registerEditField.call(this, element);
};

TableEditors.InlineAutocomplete.prototype._handleKeyEvent = function(event) {
  if (event.keyCode === 27) {
    event.stopPropagation();
    event.preventDefault();
    this._requestCancel();
    return false;
  } else if (event.keyCode === 13 && !this.autocompleteOpen) {    
    event.stopPropagation();
    event.preventDefault();
    this._requestSave();
    return false;
  }
};



TableEditors.InlineAutocomplete.prototype._getData = function(searchString) {
  var data = AutocompleteDataProvider.getInstance().get(this.options.dataType);
  var filteredList = this._filterSuggestions(data, searchString);
  return $.map(filteredList, function(item) {
    return {
      label: item.name,
      value: item.name,
      object: item.originalObject 
    };
  });
};

TableEditors.InlineAutocomplete.prototype._filterSuggestions = function(list, match) {
  var me = this;
  var returnedList = jQuery.grep(list, function(element, index) {
    return (element.enabled &&
        (me.matchSearchString(element.matchedString, match) ||
            me.matchSearchString(element.name, match)));
  });
  return returnedList;
};

TableEditors.InlineAutocomplete.prototype.matchSearchString = function(text, match) {
  if (!match || !text) {
    return false;
  }
  
  // Split to fragments
  var replaceRe = new RegExp("[\\]\\[\\\\!#$%&()*+,./:;<=>?@_`{|}~]+");
  var matchFragments = match.replace(replaceRe, ' ').split(' ');
  
  var a = 5;
  // Loop through fragments
  var allMatch = true;
  for (var i = 0; i < matchFragments.length; i++) {
    var fragment = matchFragments[i];
    if (text.toLowerCase().indexOf(fragment.toLowerCase()) === -1) {
      allMatch = false;
      break;
    }  
  }
  
  return allMatch;
};

TableEditors.InlineAutocomplete.prototype.setEditorValue = function(value) {
  if (!value) {
    value = this.options.get.call(this.model);
  }
  this.value = value;
  if (this.options.decorator) {
    value = this.options.decorator(value);
  }
  this.textField.val(value);
};

TableEditors.InlineAutocomplete.prototype.getEditorValue = function() {
  return this.value;
};

TableEditors.InlineAutocomplete.prototype._validate = function() {
  var valid = true;
  if (this.options.required && !this.value) {
    valid = false;
    this.addErrorMessage(ValidationMessages.textField.required);
  }
  return TableEditors.CommonEditor.prototype._validate.call(this) && valid;
};




/*
 * DROP DOWN SELECTION
 */
/**
 * Abstract common constructor for all <code>select</code> editors.
 * @constructor
 * @base TableEditors.CommonEditor
 */
TableEditors.Selection = function(element, model, options) {
  this.init(element, model, options);
  this.setEditorValue();
  this.focus();
};
TableEditors.Selection.prototype = new TableEditors.CommonEditor();
/**
 * Default options for <code>TableEditors.Selection</code>
 */
TableEditors.Selection.defaultOptions = {
  /**
   * Whether the field is required or not.
   * Default: false
   * @member TableEditors.Selection */
  required: false,
  /**
   * The css width of the input element.
   * Default: 95%
   * @member TableEditors.Selection */
  size: '95%',
  /**
   * The items of the selection.
   * Default: {}
   * @member TableEditors.Selection
   */
  items: {}
};
/**
 * Initializes a Selection.
 * Will call TableEditors.CommonEditor.init.
 */
TableEditors.Selection.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.Selection.defaultOptions);
  jQuery.extend(opts, options);
  TableEditors.CommonEditor.prototype.init.call(this, element, model, opts);
  
  this.selectBox = $('<select id="'+this.generateId()+'"/>').appendTo(this.element).width(this.options.size);
  
  var me = this;
  var value = this.options.get.call(this.model);
  var items = this._parseItems();
  jQuery.each(items, function(key, val) {
    var el = $('<option/>').text(val).val(key).appendTo(me.selectBox);
  });
  
  this._registerEditField(this.selectBox);
};

TableEditors.Selection.prototype._parseItems = function() {
  // Check, whether object is a map
  if (typeof this.options.items  === "object") {
    return this.options.items;
  }
  else {
    return this.options.items();
  }
};

TableEditors.Selection.prototype._registerEditField = function(field) {
  var me = this;
  field.change(function() {
    me._requestSaveIfNotInRowEdit();
  });
  
  TableEditors.CommonEditor.prototype._registerEditField.call(this, field);
};

TableEditors.Selection.prototype.focus = function() {
  this.selectBox.focus();
};

TableEditors.Selection.prototype.close = function() {
  this.selectBox.remove();
  TableEditors.CommonEditor.prototype.close.call(this);
};

TableEditors.Selection.prototype.getEditorValue = function() {
  return this.selectBox.val();
};
TableEditors.Selection.prototype.setEditorValue = function(value) {
  if (!value) {
    value = this.options.get.call(this.model);
  }
  if (value instanceof CommonModel) {
    this.selectBox.val(value.getId());
  }
  else {
    this.selectBox.val(value);
  }
};

TableEditors.Selection.prototype._validate = function() {
  var valid = true;
  return TableEditors.CommonEditor.prototype._validate.call(this) && valid;
};





/*
 * WYSIWYG
 */

/**
 * Wysiwyg editor.
 * @constructor
 * @base TableEditors.CommonEditor
 */
TableEditors.Wysiwyg = function(element, model, options) {
  this.init(element, model, options);
  this.setEditorValue();
};
TableEditors.Wysiwyg.prototype = new TableEditors.CommonEditor();
TableEditors.Wysiwyg.defaultOptions = {
  /**
   * The width of the element.
   * Default: 96%
   * @member TableEditors.Wysiwyg */
  width: "96%",
  /**
   * The height of the element.
   * Default: 240
   * @member TableEditors.Wysiwyg */
  height: "240"
};
/**
 * Initialize a Wysiwyg editor
 */
TableEditors.Wysiwyg.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.Wysiwyg.defaultOptions);
  jQuery.extend(opts, options);
  
  TableEditors.CommonEditor.prototype.init.call(this, element, model, opts);
  
  var me = this;
  this.actualElement = $('<textarea></textarea>').appendTo(element);
  this.actualElement.width(this.options.width).height(this.options.height);
  this.actualElement.wysiwyg({
    autoSave: true
  });
  this.wysiwyg = this._getEditorWindow();
  
  this.actualElement.trigger("editorOpening");
  this.actualElement.addClass("tableSortListener");
  this.resetEditor();
  this.actualElement.bind("tableSorted", function() {
    if (!me.isFocused()) {
      me.actualElement.wysiwyg("resetFrame");
      me.wysiwyg = me._getEditorWindow();
      me.resetEditor();
      me.setEditorValue();
//      me.actualElement.focus();
    }
  });

};
TableEditors.Wysiwyg.prototype.resetEditor = function() {
  var iframeElement = this.actualElement.wysiwyg("getFrame")[0];
  var frameWindow = $(iframeElement.contentWindow);
  this._registerEditField(frameWindow);
};

TableEditors.Wysiwyg.prototype._getEditorWindow = function() {
  return $(this.actualElement.wysiwyg("getDocument"));
};
TableEditors.Wysiwyg.prototype.setEditorValue = function(value) {
  if (!value) {
    value = this.options.get.call(this.model) || "";
  }
  this.actualElement.get(0).innerHTML = value;
  this.actualElement.wysiwyg("setValue", value);
};
TableEditors.Wysiwyg.prototype.getEditorValue = function() {
  return this.actualElement.val();
};

TableEditors.Wysiwyg.prototype.focus = function() {
  this.actualElement.focus();
};

TableEditors.Wysiwyg.prototype.close = function() {
  this.actualElement.trigger("editorClosing");
  this.actualElement.wysiwyg("remove");
  this.actualElement.remove();
};
/**
 * Change event listener.
 */
TableEditors.Wysiwyg.prototype._registerEditField = function(element) {
  this.actualElement.wysiwyg("getFrame").data("editor", this).addClass("dynamics-editor-element");
  var me = this;
  element.keydown(function(event) {
    me._handleKeyEvent(event);
    return true;
  });
  element.blur(function(event) {
    if(me.element.find(event.target).length === 0 && !me.actualElement.wysiwyg("getKeepEditorOpen")) {
      me._requestSaveIfNotInRowEdit();
      me.element.trigger("DynamicsBlur");
      me.focused = false;
    }
  });
  element.focus(function(event) {
    me.element.trigger("DynamicsFocus");
    me.focused = true;
  });
  me.actualElement.focus();
};
TableEditors.Wysiwyg.prototype._handleKeyEvent = function(event) {
  if (event.keyCode === 27 && !this.editRow) {
    this.close();
  }
};


/*
 * DIALOG EDITORS
 */

/**
 * Base class for all editors that open dialogs.
 * @constructor
 * @member TableEditors
 */
TableEditors.DialogEditor = function() {};
TableEditors.DialogEditor.prototype = new TableEditors.CommonEditor();
/**
 * Default options for the DialogEditor class.
 */
TableEditors.DialogEditor.defaultOptions = {
    /**
     * Whether the opened dialog should be modal or not.
     * Default: true
     * @member TableEditors.DialogEditor
     */
    modal: true,
    
    /**
     * Whether the dialog should be shown on editor open.
     * Default: true
     * @member TableEditors.DialogEditor
     */
    autoShow: true,
    
    /**
     * Dialog title
     * Default: "(Insert title here)"
     * @member TableEditors.DialogEditor
     */
    dialogTitle: "(Insert title here)",
    /**
     * Dialog close
     * Default: function() { }
     * @member TableEditors.DialogEditor
     */
    dialogClose: function() { return true; }

    	
};

/**
 * Initialize a <code>DialogEditor</code>
 */
TableEditors.DialogEditor.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.DialogEditor.defaultOptions);
  jQuery.extend(opts, options);
  TableEditors.CommonEditor.prototype.init.call(this, element, model, opts);

  if (this.options.autoShow) {
    this._openDialog();
  }
};
TableEditors.DialogEditor.prototype._openDialog = function() {
  var me = this;
  
  // These override the extendedDialogOptions
  var options = {
      autoOpen:   this.options.autoShow,
      modal:      this.options.modal,
      buttons:    {
        'Cancel': function() { me._cancel(); },
        'Ok':     function() { me._ok(); }
      },
      close:      function() { me._cancel(); },
      width:      500,
      minHeight:  300,
      title:      this.options.dialogTitle
  };
  this.dialog = $('<div/>').dialog(options);
};

TableEditors.DialogEditor.prototype._closeDialog = function() {
  if (this.dialog) {
    this.dialog.dialog('destroy');
    this.dialog.remove();
    this.dialog = null;
  }
};

TableEditors.DialogEditor.prototype._ok = function() {
  this.options.set.apply(this.model, this.getEditorValue());
  this._requestSaveIfNotInRowEdit();
  this.close();
};
TableEditors.DialogEditor.prototype._cancel = function() {
  this._requestCancelIfNotInRowEdit();
  this.close();
};

TableEditors.DialogEditor.prototype.close = function() {
  this._closeDialog();
  this._fireTransactionEditEvent();
  TableEditors.CommonEditor.prototype.close.call(this);
  this.options.dialogClose.call();
};



/**
 * Dialog editor for Autocomplete module
 * @constructor
 */
TableEditors.AutocompleteDialog = function() {};
TableEditors.AutocompleteDialog.prototype = new TableEditors.DialogEditor();

TableEditors.AutocompleteDialog.defaultOptions = {
  /**
   * Data type for the autocomplete editor.
   * 
   * Default: ""
   * @see AutocompleteDataProvider
   * @member TableEditors.AutocompleteDialog
   */
  dataType: "",
  /**
   * Whether the Autocomplete should be multiple selection or not.
   * 
   * Default: true
   * @member TableEditors.AutocompleteDialog
   */
  multiSelect: true,
  
  /**
   * Select callback to be supplied to Autocomplete.
   * 
   * Default: null 
   * @member TableEditors.AutocompleteDialog
   */
  selectCallback: null,
  
  /**
   * Parameters passed to <code>AutocompleteDataprovider</code>
   * 
   * Default: {}
   * @member TableEditors.AutocompleteDialog 
   */
  params: {}
};
/**
 * Initialize an Autocomplete dialog editor.
 */
TableEditors.AutocompleteDialog.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.AutocompleteDialog.defaultOptions);
  jQuery.extend(opts, options);
  
  TableEditors.DialogEditor.prototype.init.call(this, element, model, opts);
  
  var autocompleteParams = {
    dataType:       this.options.dataType,
    params:         this.options.params,
    preSelected:    [],
    multiSelect:    this.options.multiSelect,
    selectCallback: this.options.selectCallback
  };
  
  this.autocompleteElement = $('<div/>').appendTo(this.dialog);
  this.autocomplete = new Autocomplete(this.autocompleteElement, autocompleteParams);
  this.autocomplete.initialize();
  
  this.setEditorValue();
};

/**
 * Get current editor value
 */
TableEditors.AutocompleteDialog.prototype.getEditorValue = function() {
  var ids   = this.autocomplete.getSelectedIds();
  var items = this.autocomplete.getSelectedItems();
  return [ids, items];
};

/**
 * Set the preselected models of the editor.
 */
TableEditors.AutocompleteDialog.prototype.setEditorValue = function() {
  var preSelectedModels = this.options.get.call(this.model);
  if (!preSelectedModels) {
    return;
  }
  var preSelectedIds = [];
  for (var i = 0; i < preSelectedModels.length; i++) {
    preSelectedIds.push(preSelectedModels[i].getId());
  }
  this.autocomplete.setSelected(preSelectedIds);
};





/**
 * Single select Autocomplete
 */
TableEditors.AutocompleteSingle = function(element, model, options) {
  this.init(element, model, options);
};
TableEditors.AutocompleteSingle.prototype = new TableEditors.AutocompleteDialog();
TableEditors.AutocompleteSingle.defaultOptions = {};
/**
 * Initialize a single select Autocomplete.
 */
TableEditors.AutocompleteSingle.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.AutocompleteDialog.defaultOptions);
  jQuery.extend(opts, options);
  
  opts.multiSelect = false;
  
  var me = this;
  opts.selectCallback = function(item) { me._selectCallback(item); };
  
  TableEditors.AutocompleteDialog.prototype.init.call(this, element, model, opts);
};
TableEditors.AutocompleteSingle.prototype._selectCallback = function(selected) {
  var item = selected;
  if (selected.originalObject) {
    item = ModelFactory.updateObject(selected.originalObject);
  }
  this.options.set.call(this.model, item);
  this.element.trigger("transactionEditEvent");
  
  this._requestSaveIfNotInRowEdit();
  this.close();
};

/**
 * Multi select Autocomplete.
 */
TableEditors.Autocomplete = function(element, model, options) {
  this.init(element, model, options);
};
TableEditors.Autocomplete.prototype = new TableEditors.AutocompleteDialog();
TableEditors.Autocomplete.defaultOptions = {};
/**
 * Initialize a single select Autocomplete.
 */
TableEditors.Autocomplete.prototype.init = function(element, model, options) {
  var opts = {};
  jQuery.extend(opts, TableEditors.Autocomplete.defaultOptions);
  jQuery.extend(opts, options);
  opts.multiSelect = true;
  
  TableEditors.AutocompleteDialog.prototype.init.call(this, element, model, opts);
};



/**
 * NOTE: This editor is only available, when creating a new story.
 * 
 * No previous values are loaded.
 */
TableEditors.Labels = function(element, model, options) {
  this.init(element, model, options);
  
  if (this.options.showText) {
    var parent = $('<div style="width: 90%;" />').appendTo(this.element);
    parent.append('<span class="labelsText">Labels:</span>');
    this.labelsElement = $('<div class="inline-labeleditor"/>').appendTo(parent);
  } else {
    this.labelsElement = $('<div />').appendTo(this.element);
  }
  
  this.labelsView = new AutoSuggest("ajax/lookupLabels.action", {
    startText: "Enter labels here.",
    queryParam: "labelName",
    searchObj: "name",
    selectedItem: "displayName",
    disableButtons: true,
    cancelCallback: function() {
    },
    successCallback: function(data) {
    },
    retrieveComplete: function(data) {
      var newData = [];
      for (var i = 0, len = data.length; i < len; i++) {
        var oneLabel = {
            value: data[i].displayName,
            name: data[i].name,
            displayName: data[i].displayName
        };
        newData[i] = oneLabel;
      }
      return newData;
    },
    minChars: 1
  }, this.labelsElement);
  
  this._registerEditField(this.labelsElement);
};
TableEditors.Labels.prototype = new TableEditors.CommonEditor();
TableEditors.Labels.defaultOptions = {
  /**
   * {@member TableEditors.Labels}
   * Default: false
   */
  showText: false,
  /**
   * {@member TableEditors.Labels}
   * Default: "Labels"
   */
  text: "Labels"
};
TableEditors.Labels.prototype.getEditorValue = function() {
  return this.labelsView.getValues();
};
TableEditors.Labels.prototype._handleKeyEvent = function(event) {
  if (event.keyCode === 27) {
    event.stopPropagation();
    event.preventDefault();
    this._requestCancel();
    return false;
  } else if (event.keyCode === 13) {
    event.stopPropagation();
    event.preventDefault();
//    this._requestSave();
    return false;
  }
};
