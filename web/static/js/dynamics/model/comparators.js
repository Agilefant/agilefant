var DynamicsComparators = {
  doCompare : function(value1, value2) {
    if (value1 > value2) {
      return 1;
    } else if (value1 < value2) {
      return -1;
    } else {
      return 0;
    }
  },
  genericComparator : function(obj1, obj2, targetMethod) {
    var value1 = targetMethod.call(obj1);
    var value2 = targetMethod.call(obj2);
    return DynamicsComparators.doCompare(value1, value2);
  },
  valueComparatorFactory: function(targetMethod) {
    return function(obj1, obj2) {
        return DynamicsComparators.genericComparator(obj1, obj2, targetMethod);
    };
  },
  genericFilteredComparator: function(obj1, obj2, targetMethod, filterFunc) {
      var value1 = filterFunc(targetMethod.call(obj1));
      var value2 = filterFunc(targetMethod.call(obj2));
      return DynamicsComparators.doCompare(value1, value2);
  },
  filteredValueComparatorFactory: function(targetMethod, filterFunc) {
      return function(obj1, obj2) {
          return DynamicsComparators.genericFilteredComparator(obj1, obj2, targetMethod, filterFunc);
      };
  },
  storyBacklogNameComparator: function(obj1, obj2) {
	  return DynamicsComparators.doCompare(obj1.getBacklog().getName(), obj2.getBacklog().getName());
  },
  storyIterationNameComparator: function(obj1, obj2) {
    var name1 = "";
    var name2 = "";
    if (obj1.getIteration() != null) {
      name1 = obj1.getIteration().getName();
    }
    if (obj2.getIteration() != null) {
        name2 = obj2.getIteration().getName();
    }
    return DynamicsComparators.doCompare(name1, name2);
  }
};
