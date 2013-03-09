package fi.hut.soberit.agilefant.business;

import java.io.File;
import java.util.Collection;

import fi.hut.soberit.agilefant.model.TaskComment;
import fi.hut.soberit.agilefant.transfer.TaskCommentTO;
import fi.hut.soberit.agilefant.util.CommentType;

public interface TaskCommentBusiness extends GenericBusiness<TaskComment> {
    
    public TaskComment store(TaskComment object, Integer userId, Integer taskId);
    public TaskComment storeWithAttachment(TaskComment object, Integer userId, Integer taskId, File[] files, String[] fileNames, String[] filesContentTypes);
    public Collection<TaskCommentTO> getComments(Integer taskId);
    public void   deleteComment(Integer commentId);
    public TaskComment   retrive(int commentId);
    public boolean deleteAttachmentsInTaskComments(int id);

}
