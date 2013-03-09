package fi.hut.soberit.agilefant.web;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.annotations.PrefetchId;
import fi.hut.soberit.agilefant.business.SettingBusiness;
import fi.hut.soberit.agilefant.business.TaskCommentBusiness;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.TaskComment;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import fi.hut.soberit.agilefant.transfer.TaskCommentTO;

/**
 * @author rahul
 *
 */
@Component("taskCommentAction")
@Scope("prototype")
public class TaskCommentAction extends ActionSupport implements Prefetching,CRUDAction,ContextAware {

    /**
     * 
     */
    private static final long serialVersionUID = 1409166819711978648L;

    @Autowired
    private TaskCommentBusiness taskCommentBusiness;
    
    @Autowired
    private SettingBusiness settingBusiness;
        
    @PrefetchId
    private int commentId;

    private TaskComment taskComment;
    private Task task;
    private int maximumSize;
    
    private File[] attachments;
    private String[] attachmentsContentType;
    private String[] attachmentsFileNames;
      
    private final String type = "task";
    private int userId;
    private int objectId;
    private String commentEnterd;
    
    private Collection<TaskCommentTO> comments = new ArrayList<TaskCommentTO>();
    
    public TaskCommentAction() {
        
    }

    @Override
    public String create() {
        commentId = 0;
        taskComment = new TaskComment();
        return Action.SUCCESS;
    }

    @Override
    public String delete() {
         taskCommentBusiness.delete(commentId);
         return Action.SUCCESS;
    }

    @Override
    public String store() {
        java.util.Date date= new java.util.Date();
        if(taskComment == null){
            taskComment = new TaskComment();
        }
        userId = SecurityUtil.getLoggedUserId();
        this.taskComment.setComment(commentEnterd);
        this.taskComment.setTime(new Timestamp(getCurrentTimeInGMT().getTime()));
        if(attachments != null && attachments.length != 0){
            if(!validateFiles()){
                return Action.ERROR;
            }
            this.taskComment = taskCommentBusiness.storeWithAttachment(taskComment, SecurityUtil.getLoggedUserId(), objectId,attachments,attachmentsFileNames,attachmentsContentType);
        } else{
            this.taskComment = taskCommentBusiness.store(taskComment, SecurityUtil.getLoggedUserId(), objectId);
        }
        
       // this.taskComment = taskCommentBusiness.store(taskComment, userId, objectId);
        this.commentId = taskComment.getId();
        
        if(commentId != 0){
            commentEnterd ="";
        }
        return Action.SUCCESS;
    }

    @Override
    public String retrieve() {
        comments.toString();
        comments = taskCommentBusiness.getComments(objectId);
        return Action.SUCCESS;
    }

    @Override
    public void initializePrefetchedData(int objectId) {
       taskComment = taskCommentBusiness.retrieveDetached(commentId);  
       System.out.println();
    }
    
    @Override
    public String getContextName() {
        return "taskComment";
    }
    @Override
    public int getContextObjectId() {
        // TODO Auto-generated method stub
        return this.getContextObjectId();
    }

    public TaskCommentBusiness getTaskCommentBusiness() {
        return taskCommentBusiness;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public TaskComment getTaskComment() {
        return taskComment;
    }

    public void setTaskComment(TaskComment taskComment) {
        this.taskComment = taskComment;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public void setObjectId(String[] objectId) {
        //this.taskId = Integer.taskId;
    }
    public String getCommentEnterd() {
        return commentEnterd;
    }

    public void setCommentEnterd(String commentEnterd) {
        this.commentEnterd = commentEnterd;
    }

    public Collection<TaskCommentTO> getComments() {
        return comments;
    }

    public void setComments(Collection<TaskCommentTO> comments) {
        this.comments = comments;
    }
    public File[] getAttachments() {
        return attachments;
    }


    public void setAttachments(File[] attachments) {
        this.attachments = attachments;
    }


    public String[] getAttachmentsContentType() {
        return attachmentsContentType;
    }


    public void setAttachmentsContentType(String[] attachmentsContentType) {
        this.attachmentsContentType = attachmentsContentType;
    }


    public String[] getAttachmentsFileNames() {
        return attachmentsFileNames;
    }


    public void setAttachmentsFileName(String[] attachmentsFileNames) {
        this.attachmentsFileNames = attachmentsFileNames;
    }

    public int getMaximumSize() {
        this.maximumSize =  settingBusiness.getMaxSizeForAllAttachment() * 1024 * 1024;
        if (maximumSize <= 1){
            this.maximumSize = 1024 * 1024 * 10;
        }
        return maximumSize;
    }

    
    public boolean validateFiles() {
        boolean valid = true;
        long totalFileSize = 0;
        if(attachments != null ){
            for(File file : attachments ){
                totalFileSize += file.length();
            }
            System.out.println(" Total uploaded file size :  "+totalFileSize);
            System.out.println("Total allowed size : "+getMaximumSize());
            if( totalFileSize > getMaximumSize() ){
                addActionError("Your attachment size too large , maximum upload size allowed ( MB ) : "+(maximumSize/1024/1024));
                valid = false;
            }
        }
        return valid; 
    }
    
    private Date getCurrentTimeInGMT(){
        Calendar c = Calendar.getInstance();

        TimeZone z = c.getTimeZone();
        int offset = z.getRawOffset();
        if(z.inDaylightTime(new Date())){
            offset = offset + z.getDSTSavings();
        }
        int offsetHrs = offset / 1000 / 60 / 60;
        int offsetMins = offset / 1000 / 60 % 60;


        c.add(Calendar.HOUR_OF_DAY, (-offsetHrs));
        c.add(Calendar.MINUTE, (-offsetMins));

        return c.getTime();
    }
    
    public String getType() {
        return type;
    }
    
}
