
/**
 * Controller for the left hand my assignments hierarchy menu.
 * 
 * @param element the root element of the tree.
 * @constructor
 */
var AdministrationMenuController = function AdministrationMenuController(element, toggleElement) {
  this.init(element, toggleElement);
};

AdministrationMenuController.prototype.init = function(element, toggleElement) {
  this.element = element;
  this.element.html("");
  this.toggleControl = toggleElement;
  this.tree = null;
  this.initTree();
};

AdministrationMenuController.prototype.initTree = function() {   
  this.element.dynatree({
    keyboard: false,
    autoFocus: false,
    onClick: function(dtnode, event) {
      if ($(event.target).hasClass("ui-dynatree-title")) {
        window.location.href = event.target.href;
      }
    },
    debugLevel: 0,
    initAjax: null
  });

  this.tree = this.element.dynatree("getTree");
  
  var rootNode = this.element.dynatree("getRoot");
  var currentUser = PageController.getInstance().getCurrentUser();
  
  /* Both admin and non-admin can edit his/her personal account. */
  
  rootNode.addChild({
    title: "My Account",
    icon: false,
    key: "editUser.action"
  });

  /* Only admin has access to these. */
  if (currentUser.getAdmin()) {
	  
	  rootNode.addChild({
		    title: "Users",
		    icon: false,
		    key: "listUsers.action"
		  });
		  
	  rootNode.addChild({
		    title: "Teams",
		    icon: false,
		    key: "listTeams.action"
		  });
		  
	  rootNode.addChild({
	    title: "Access rights",
	    icon: false,
	    key: "accessRights.action"
	  });
  
	  rootNode.addChild({
	    title: "System settings",
	    icon: false,
	    key: "systemSettings.action"
	  });
  
	  rootNode.addChild({
		  title: "Database export",
		  icon:false,
		  key: "databaseExport.action"
	  });  
  }
  
  this.element.find("a.ui-dynatree-title").each(function(key, item) {
    item.href = $(item.parentNode).attr("dtnode").data.key;
  });
};

AdministrationMenuController.prototype.reload = function() {};
