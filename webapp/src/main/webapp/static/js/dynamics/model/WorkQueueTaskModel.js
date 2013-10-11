var WorkQueueTaskModel = function WorkQueueTaskModel() {
  TaskModel.call(this);
  this.copiedFields.workQueueRank = "workQueueRank";
  this.persistedClassName = "fi.hut.soberit.agilefant.transfer.DailyWorkTaskTO";
};
extendObject(WorkQueueTaskModel, TaskModel);
WorkQueueTaskModel.prototype.getWorkQueueRank = function() {
  return this.currentData.workQueueRank;
};
WorkQueueTaskModel.prototype.rankInWorkQueue = function(previousTaskId, userId) {
  var me = this;
  $.ajax({
    url:  'ajax/rankDailyTaskAndMoveUnder.action',
    data: { taskId: this.id, rankUnderId: previousTaskId, userId: userId },
    type: "post",
    success: function(data, status) {
      MessageDisplay.Ok("Task ranked in task queue");
      me.callListeners(new DynamicsEvents.RankChanged(me, "workQueueTask"));
    }
  });
};
