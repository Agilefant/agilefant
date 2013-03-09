package fi.hut.soberit.agilefant.transfer;

import fi.hut.soberit.agilefant.model.TaskComment;
import fi.hut.soberit.agilefant.util.BeanCopier;

public class TaskCommentTO extends TaskComment{
    
    /**
     * 
     */
    private static final long serialVersionUID = -5336826034149614755L;

    public TaskCommentTO() {
        // TODO Auto-generated constructor stub
    }
    
    public TaskCommentTO(TaskComment taskComment){
        BeanCopier.copy(taskComment, this);
    }

}
