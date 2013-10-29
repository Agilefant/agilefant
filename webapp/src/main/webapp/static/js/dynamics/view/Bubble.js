var Bubble = function Bubble(referenceElement, options) {
  this.referenceElement = referenceElement;
  this.parentElement = null;
  this.element = null;
  this.options = {
    closeCallback: function() { return false; },
    removeOthers: true,
    removeSelector: null,
    autoClose: true,
    title:     null,
    offsetX:   100,
    offsetY:   35,
    minWidth:  400,
    minHeight: 80,
    zIndex:    800,
    maxWidth:  null,
    cssClass:  null
  };
  jQuery.extend(this.options, options);
  this.init();
};
Bubble.prototype = new ViewPart();

/**
 * Removes all bubbles.
 */
Bubble.closeAll = function() {
  $('body > div.infobubble').trigger('destroyBubble');
};
/**
 * Returns the content element of the bubble.
 */
Bubble.prototype.getElement = function() {
  return this.element;
};

/**
 * Initialize a bubble
 */
Bubble.prototype.init = function() {
  this._removeOthersIfNeeded();
  this._createElements();
  this._position();
  this._bindEvents();
};

/**
 * Destroy the bubble.
 * <p>
 * Pass on the arguments to the close callback function.
 */
Bubble.prototype.destroy = function() {
  this.parentElement.remove();
  
  //Remove the esc listener
  $(document).unbind('keydown.agilefantBubble', this.keypressListener);
  
  if (this.options.closeCallback) {
    this.options.closeCallback(arguments);
  }
};

Bubble.prototype._createElements = function() {
  // Create the structure
  this.parentElement = $('<div/>').addClass('infobubble');
  this.element = $('<div/>').appendTo(this.parentElement);
  $('<div>&nbsp;</div>').addClass('infobubble-helperarrow').appendTo(this.parentElement);
  
  // Additional css classes
  if (this.options.cssClass) {
    this.parentElement.addClass(this.options.cssClass);
  }
  
  // Header
  var me = this;
  this.header = $('<div style="height: 1.5em;"></div>').appendTo(this.element);
  
  // Title
  if (this.options.title !== null) {
    $('<h3 style="float: left;">' + this.options.title + '</h3>)').appendTo(this.header);    
  }
  
  $('<a title="Close bubble" class="close-button">X</a>').click(function() {
    me.destroy();
  }).appendTo(this.header);
  
  if (me.options.autoClose) {
    var initBubbleTime = new Date().getTime();
    document.onclick = function(event) {
      // Do not close the bubble if it was created just before the click event.
      // The bubble is drawn before click event. There is 10-20ms delay between, but use 200ms value to be sure.
      if (me.parentElement.width() > 0 && new Date().getTime() - initBubbleTime > 200) {
        var offset = me.parentElement.offset();
        var leftBorder = offset.left;
        var topBorder = offset.top;
        var rightBorder = leftBorder + me.parentElement.width() + 20;
        var bottomBorder = topBorder + me.parentElement.height() + 20;
        if (!intersects(leftBorder, topBorder, rightBorder, bottomBorder, event.pageX, event.pageY)) {
          me.destroy();
        }
      }
    };
  }
};

function intersects(leftBorder, topBorder, rightBorder, bottomBorder, x, y) {
  return !(rightBorder < x || leftBorder > x || bottomBorder < y || topBorder > y);
}

Bubble.prototype._position = function() {
  // Position the bubble
  var pos = this.referenceElement.offset();
  this.parentElement.css({
    'top': pos.top + this.options.offsetY + 'px',
    'left': pos.left + this.options.offsetX + 'px',
    'min-width': this.options.minWidth,
    'min-height': this.options.minHeight,
    'max-width': this.options.maxWidth,
    'z-index': this.options.zIndex
  });
  // Add to document
  this.parentElement.appendTo(document.body);
};

Bubble.prototype._removeOthersIfNeeded = function() {
  // Fire the delete event for others
  if (this.options.removeOthers) {
    $('body > div.infobubble').trigger('destroyBubble');
  }
  else if (this.options.removeSelector) {
    var removeSelector = this.options.removeSelector;
    $('body > div.infobubble').each(function() {
      if ($(this).is(removeSelector)) {
        $(this).trigger('destroyBubble');
      }
    });
  }
};

Bubble.prototype._bindEvents = function() {
  var me = this;
  // Add the delete listener
  this.parentElement.bind('destroyBubble', function(event) {
    me.destroy();
    event.stopPropagation();
    return false;
  });
  
  // Add esc press listener
  this.keypressListener = function(event) {
    if (event.keyCode === 27) {
      me.destroy();
      return false;
    }
  };
  
  $(document).bind("keydown.agilefantBubble", this.keypressListener);
};

