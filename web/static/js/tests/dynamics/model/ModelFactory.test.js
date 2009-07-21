
$(document).ready(function() {
  
  module("Dynamics: Model factory",{
    setup: function() {
      ModelFactory.instance = null;
      this.instance = ModelFactory.getInstance();
      this.instance.initialized = true;
      this.testObject = {
          id: 222,
          name: "Test Object"
      };
    },  
    teardown: function() { }
  });
  
  
  test("Get instance", function() {
    ModelFactory.instance = null;
    var instance = ModelFactory.getInstance();
    
    ok(ModelFactory.instance, "Instance has been created");
    
    var anotherInstance = ModelFactory.getInstance();
    equals(anotherInstance, instance, "Instance is singleton");
  });
  
  
  
  test("Initialization", function() {
    var expectedId = 222;
    var expectedType = "iteration";
    var internalInitializeCallCount = 0;
    
    var cb = function() {};
    
    this.instance._initialize = function(type, id, callback) {
      same(type, expectedType, "Type was correct");
      same(id, expectedId, "Id was correct");
      same(callback, cb, "Callback is correct");
      internalInitializeCallCount++;
    };
    
    
    var actual = ModelFactory.initializeFor("iteration", 222, cb);
    
    same(internalInitializeCallCount, 1, "Internal initialize called");
   });
  
  
  
  
  test("Initialization invalid checks", function() {
    var exceptionCount = 0;
    try {
      ModelFactory.initializeFor();
    }
    catch (e) { exceptionCount++; }
    
    try {
      ModelFactory.initializeFor(null);
    }
    catch (e) { exceptionCount++; }
    
    try {
      ModelFactory.initializeFor(ModelFactory.initializeForTypes.iteration, null);
    }
    catch (e) { exceptionCount++; }
    
    try {
      ModelFactory.initializeFor("Incorrect type", 555);
    }
    catch (e) { exceptionCount++; }
    
    same(exceptionCount, 4, "Correct number of exceptions")
  });
  
  
  test("Internal initialize", function() {
    var expectedId = 212;
    var expectedType = "iteration";
    
    var iter = new IterationModel();
    
    var getDataCallCount = 0; 
    this.instance._getData = function(type, id) {
      getDataCallCount++;
      same(type, expectedType, "Type matches");
      same(id, expectedId, "Id matches");
    };
    
    this.instance._initialize(expectedType, expectedId);
    
    ok(this.instance.initialized, "Initialized field set");
    same(getDataCallCount, 1, "Get data called once");
  });
  
  
  test("Static add object", function() {
    var task = new TaskModel();
    var story = new StoryModel();
    
    var addObjectCallCount = 0;
    var taskAdded = false;
    var storyAdded = false;
    this.instance._addObject = function(obj) {
      addObjectCallCount++;
      if (obj === task) {
        taskAdded = true;
      }
      else if (obj === story) {
        storyAdded = true;
      }
    };
    
    ModelFactory.addObject(task);
    ModelFactory.addObject(story);
    
    same(addObjectCallCount, 2, "Internal add object called twice");
    ok(taskAdded, "Task is added");
    ok(storyAdded, "Story is added");
  });
  
  
  test("Static add object - invalid checks", function() {
    var invalidObject = {};
    var UnknownClass = function() {
      this.initialize();
      this.persistedClassName = "faulty name";
    };
    UnknownClass.prototype = new CommonModel();
    
    var exceptionsThrown = 0;
    
    try {
      ModelFactory.addObject();
    }
    catch(e) { exceptionsThrown++; }
    try {
      ModelFactory.addObject(null);
    }
    catch(e) { exceptionsThrown++; }
    try {
      ModelFactory.addObject(new UnknownClass());
    }
    catch(e) { exceptionsThrown++; }
    try {
      ModelFactory.addObject(invalidObject);
    }
    catch(e) { exceptionsThrown++; }
    
    same(exceptionsThrown, 4, "Correct number of exceptions thrown");
  });
  
  
  test("Internal add object", function() {
    
    var task = new TaskModel();
    task.id = 3;
    var story = new StoryModel();
    story.id = 465;
    
    this.instance._addObject(task);
    this.instance._addObject(story);
    
    same(this.instance.data.task[3], task, "Task is added");
    same(this.instance.data.story[465], story, "Story is added");
  });
  
  
  test("Static get object", function() {   
    this.instance.data.task[222] = this.testObject;
    equals(ModelFactory.getObject("task", 222), this.testObject, "Correct object returned");
    
    var exceptionThrown = false;
    try {
      ModelFactory.getObject("task", "not found id");
    }
    catch(e) {
      if (e === "Not found") {
       exceptionThrown = true; 
      }
    }
    ok(exceptionThrown, "Not found exception thrown");
  });
  
  test("Static get object if exists", function () {
    this.instance.data.task[222] = this.testObject;
    equals(ModelFactory.getObjectIfExists("task", 222), this.testObject, "Correct object returned");
    equals(ModelFactory.getObjectIfExists("task", "not found id"), null, "Null object returned");
  });
  
  test("Static get object null checks", function() {    
    var internalCallCount = 0
    var exceptionCount = 0;
    
    this.instance._getObject = function() {
      internalCallCount++;
    };
    
    // Undefined
    try {
      ModelFactory.getObject();
    }
    catch (e) {
      exceptionCount++;
    }
    
    // Null
    try {
      ModelFactory.getObject(null);
    }
    catch (e) {
      exceptionCount++;
    }
    
    // Invalid
    try {
      ModelFactory.getObject("This is invalid");
    }
    catch (e) {
      exceptionCount++;
    }
    
    same(exceptionCount, 3, "Correct number of exceptions thrown");
    same(internalCallCount, 0, "Internal getObject not called");
  });
  
  test("Static create object", function() {
    var expectedType = "task";
    var newObject = {};
    
    var internalCreateObjectCallCount = 0;
    this.instance._createObject = function(type) {
      same(type, expectedType, "Type matches");     
      internalCreateObjectCallCount++;
      return newObject;
    };
    
    equals(ModelFactory.createObject(expectedType), newObject, "Correct object returned");
    same(internalCreateObjectCallCount, 1, "Internal createObject function called");
  });
  
  test("Static create object null checks", function() {    
    var exceptionCount = 0;
    var internalCreateCallCount = 0;
    
    this.instance._createObject = function() {
      internalCreateCallCount++;
    };
    
    // Undefined
    try {
      ModelFactory.createObject();
    }
    catch (e) {
      exceptionCount++;
    }
    
    // Null
    try {
      ModelFactory.createObject(null);
    }
    catch (e) {
      exceptionCount++;
    }
    
    // Invalid
    try {
      ModelFactory.createObject("This is invalid");
    }
    catch (e) {
      exceptionCount++;
    }
    
    same(exceptionCount, 3, "Correct number of exceptions thrown");
    same(internalCreateCallCount, 0, "Internal create object was not called");
  });
  
  
  test("Internal get object", function() {
    this.instance.data = {
      story: {
        123: {
          id: 123,
          name: "Test story with id 123"
        }
      },
      task: {
        123: {
          id: 123,
          name: "Test task with id 123"
        },
        7: {
          id: 7,
          name: "Test task with id 7"
        }
      }
    };
    
    var task123 = this.instance._getObject(ModelFactory.types.task, 123);
    var task7 = this.instance._getObject(ModelFactory.types.task, 7);
    var story123 = this.instance._getObject(ModelFactory.types.story, 123);
    
    var notFoundStory = this.instance._getObject(ModelFactory.types.story, 9876);

    ok(task123, "Task 123 is defined");
    ok(task7, "Task 7 is defined");
    ok(story123, "Story 123 is defined");
    
    equals(task123, this.instance.data.task[123], "Task with id 123 is returned");
    equals(task7, this.instance.data.task[7], "Task with id 7 is returned");    
    equals(story123, this.instance.data.story[123], "Story with id 123 is returned");
    
    equals(notFoundStory, null, "Null story is returned");
  });

  

  
  
  test("Internal create object", function() {
    var actualTask = this.instance._createObject(ModelFactory.types.task);
    var actualStory = this.instance._createObject(ModelFactory.types.story);
    
    ok(actualTask instanceof TaskModel, "Task created correctly");
    ok(actualStory instanceof StoryModel, "Story created correctly");
    
    ok(jQuery.inArray(this.instance.listener, actualTask.listeners) !== -1,
        "ModelFactory listener set for task");
    ok(jQuery.inArray(this.instance.listener, actualStory.listeners) !== -1,
        "ModelFactory listener set for story");
  });
  

  
  
  test("ModelFactory not initialized", function() {
    this.instance.initialized = false;
    
    var exceptionCount = 0;
    
    try {
      ModelFactory.addObject(new TaskModel());
    }
    catch (e) { if (e === "Not initialized") { exceptionCount++; }}
    
    try {
      ModelFactory.getObject(ModelFactory.types.task, 111)
    }
    catch (e) { if (e === "Not initialized") { exceptionCount++; }}
    
    try {
      ModelFactory.createObject(ModelFactory.types.task);
    }
    catch (e) { if (e === "Not initialized") { exceptionCount++; }}
    
    same(exceptionCount, 3, "Correct number of exceptions thrown");
  });

  
  module("Dynamics: ModelFactory: constructs",{
    setup: function() {
    
    },
    teardown: function() {
      
    }
  });
  
  test("Construct iteration", function() {
    
    
  });
  
});

