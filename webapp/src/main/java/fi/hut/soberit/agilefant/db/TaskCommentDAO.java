package fi.hut.soberit.agilefant.db;

import java.util.List;

import fi.hut.soberit.agilefant.model.TaskComment;

public interface TaskCommentDAO extends GenericDAO<TaskComment> {
    
    public List<TaskComment> getTaskComments(Integer taskId);
    public TaskComment storeTaskComments(TaskComment taskComment);

}
