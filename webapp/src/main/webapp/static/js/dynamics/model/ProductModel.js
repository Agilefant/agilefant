
/**
 * Model class for products.
 * @constructor
 * @base BacklogModel
 * @see BacklogModel#initializeBacklogModel
 */
var ProductModel = function ProductModel() {
  this.initializeBacklogModel();
  this.persistedClassName = "fi.hut.soberit.agilefant.model.Product";
  this.relations = {
    project: [],
    iteration: [],
    story: [],
    team: []
  };
  this.currentData = {
  };
  this.copiedFields = {
    "name":   "name",
    "description": "description"
  };
  this.classNameToRelation = {
      "fi.hut.soberit.agilefant.model.Project":       "project",
      "fi.hut.soberit.agilefant.model.Iteration":     "iteration",
      "fi.hut.soberit.agilefant.model.Story":         "story",
      "fi.hut.soberit.agilefant.model.Team":         "team"
  };
};

ProductModel.prototype = new BacklogModel();


/**
 * Internal function to set data
 * @see CommonModel#setData
 */
ProductModel.prototype._setData = function(newData) {
  var data = {};
  
  // Set the id
  this.id = newData.id;
  
  // Copy fields
  this._copyFields(newData);
  
  // Set stories
  if (newData.stories) {
    this._updateRelations(ModelFactory.types.story, newData.stories);
  }
  
  if (newData.projects) {
    this._updateRelations("project", newData.projects);
  }
//  
//  if (newData.iterations) {
//    this._updateRelations("iteration", newData.iterations);
//  }

  if (newData.teams) {
  	this._updateRelations(ModelFactory.types.team, newData.teams);
  }
  
};

ProductModel.prototype.reloadProjects = function(filters, callback) {
  var me = this;
  var data = {productId: this.id};
  jQuery.ajax({
    url: "ajax/retrieveProductProjects.action",
    data: data,
    type: "post",
    dataType: "json",
    success: function(data, type) {
      me._updateRelations("project", data);
      if(callback) {
        callback();
      }
    }
  });
};

ProductModel.prototype._saveData = function(id, changedData) {
  var me = this;
  
  var url = "ajax/storeProduct.action";
  var data = {};
  
  if (changedData.teamsChanged) {
    data.teamIds = changedData.teamIds;
    data.teamsChanged = true;
    delete changedData.teamIds;
    delete changedData.teamsChanged;
  }
  jQuery.extend(data, this.serializeFields("product", changedData));
  
  if (!id) {
    url = "ajax/storeNewProduct.action";
  } else {
	  data.productId = id;
  }
 
  jQuery.ajax({
    type: "POST",
    url: url,
    async: true,
    cache: false,
    data: data,
    dataType: "json",
    success: function(data, status) {
      MessageDisplay.Ok("Product saved successfully");
      var object = ModelFactory.updateObject(data);
      if(!id) {
        object.callListeners(new DynamicsEvents.AddEvent(object));
      }
      me.callListeners(new DynamicsEvents.EditEvent(me));
    },
    error: function(xhr, status, error) {
      MessageDisplay.Error("Error saving product", xhr);
      me.rollback();
    }
  });
};

ProductModel.prototype.reload = function() {
  var me = this;
  jQuery.getJSON(
    "ajax/retrieveProduct.action",
    {productId: me.getId()},
    function(data,status) {
      me.setData(data);
      me.callListeners(new DynamicsEvents.EditEvent(me));
    }
  );
};

ProductModel.prototype.addIteration = function(iteration) {
  this.addRelation(iteration);
  this.callListeners(new DynamicsEvents.RelationUpdatedEvent(this,"iteration"));
};

ProductModel.prototype.addProject = function(project) {
  this.addRelation(project);
  this.callListeners(new DynamicsEvents.RelationUpdatedEvent(this,"project"));
};

ProductModel.prototype.addTeam = function(team) {
  this.addRelation(team);
  this.callListeners(new DynamicsEvents.RelationUpdatedEvent(this,"team"));
};

/* GETTERS AND SETTERS */

ProductModel.prototype.getDescription = function() {
  return this.currentData.description;
};
ProductModel.prototype.setDescription = function(description) {
  this.currentData.description = description;
};

ProductModel.prototype.getIterations = function() {
  return this.relations.iteration;
};

ProductModel.prototype.getName = function() {
  return this.currentData.name;
};
ProductModel.prototype.setName = function(name) {
  this.currentData.name = name;
};

ProductModel.prototype.getProjects = function() {
  return this.relations.project;
};

ProductModel.prototype.getStories = function() {
  return this.relations.story;
};

ProductModel.prototype.getTeams = function() {
  if (this.currentData.teamIds) {
    var teams = [];
    $.each(this.currentData.teamIds, function(k, id) {
      teams.push(ModelFactory.getObject(ModelFactory.types.team, id));
    });
    return teams;
  }
  return this.relations.team;
};

ProductModel.prototype.setTeams = function(teamIds, teamJson) {
  if (teamJson) {
    $.each(teamJson, function(k,v) {
      ModelFactory.updateObject(v);
    });
  }
  this.currentData.teamIds = teamIds;
  this.currentData.teamsChanged = true;
};

ProductModel.prototype.setAllTeams = function(teams) {
  var me = this;
  if(teams == "MYTEAMS" || teams == "ALL"){
    var ajaxUrl = "ajax/retrieveAllTeams.action";
    if (teams == "MYTEAMS") {
    	ajaxUrl = "ajax/retrieveMyTeams.action";
    }
  	var teams = [];
  	var data = {};
  	jQuery.ajax({
  		type: "POST",
  		url: ajaxUrl,
    	async: false,
    	cache: false,
    	data: data,
    	dataType: "json",
    	success: function(data,status) {
      		for(var i = 0; i < data.length; i++) {
      			teams.push(data[i].id);
      		}
    	}
    });
    
    this.currentData.teamsChanged = true;
    this.currentData.teamIds = teams;
  }
};