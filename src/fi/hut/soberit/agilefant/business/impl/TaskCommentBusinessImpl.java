package fi.hut.soberit.agilefant.business.impl;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.CommentAttachmentsBusiness;
import fi.hut.soberit.agilefant.business.TaskBusiness;
import fi.hut.soberit.agilefant.business.TaskCommentBusiness;
import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.db.TaskCommentDAO;
import fi.hut.soberit.agilefant.model.StoryComment;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.TaskComment;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import fi.hut.soberit.agilefant.transfer.TaskCommentTO;
import fi.hut.soberit.agilefant.util.CommentType;


@Service("taskCommentBusiness")
@Transactional
public class 
TaskCommentBusinessImpl extends GenericBusinessImpl<TaskComment> implements TaskCommentBusiness {
    
    private TaskCommentDAO taskCommentDAO;
    
    @Autowired
    private TaskBusiness taskBusiness;
    
    @Autowired
    private UserBusiness userBusiness;
    
    @Autowired
    private CommentAttachmentsBusiness commentAttachmentsBusiness;

    public TaskCommentBusinessImpl() {
        super(TaskComment.class);
    }

    @Autowired
    public void setTaskCommentDAO(TaskCommentDAO taskCommentDAO) {
        this.genericDAO = taskCommentDAO;
        this.taskCommentDAO = taskCommentDAO;
    }
        
    @Override
    public Collection<TaskComment> retrieveAll() {
        return null;
    }

    @Override
    public TaskComment retrieve(int id) {
        // TODO Auto-generated method stub
        return retrieve(id);
    }

    @Override
    public void delete(int id) {
        delete(id);
    }

    @Override
    public void delete(TaskComment object) {
       delete(object);
        
    }

    @Override
    public int create(TaskComment object) {
        return super.create(object);
    }

    @Override
    public int countAll() {
        // TODO Auto-generated method stub
        return this.countAll();
    }

    @Override
    public boolean exists(int id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public TaskComment retrieveIfExists(int id) {
        // TODO Auto-generated method stub
        return retrieve(id);
    }

    @Override
    public TaskComment store(TaskComment object, Integer userId, Integer taskId) {
        Task task = taskBusiness.retrieve(taskId);
        User user = userBusiness.retrieve(userId);
        
        TaskComment taskCommentStorable = object;
        taskCommentStorable.setTasks(task);
        taskCommentStorable.setUsers(user);
        
        store(taskCommentStorable);
        
        return taskCommentStorable;
    }

    @Override
    public Collection<TaskCommentTO> getComments(Integer taskId) {
        // TODO Auto-generated method stub
        return convertToTaskCommentTo(taskCommentDAO.getTaskComments(taskId));
    }

    @Override
    public void deleteComment(Integer commentId) {
        this.delete(commentId);        
    }

    @Override
    public TaskComment retrive(int commentId) {
        // TODO Auto-generated method stub
        return this.retrieve(commentId);
    }
    
    public List<TaskCommentTO> convertToTaskCommentTo(List<TaskComment> taskComments){
        List<TaskCommentTO> commentToList = new ArrayList<TaskCommentTO>();
        int loggedUserId = SecurityUtil.getLoggedUserId();
        boolean isAdmin = SecurityUtil.getLoggedUser().isAdmin();
        if(taskComments != null && taskComments.size() > 0){
            for (TaskComment taskComment : taskComments) {
             taskComment.setParsedTime(convertSqlTimeToPrettyPrintFormat(taskComment));
             if(isAdmin){
                 taskComment.setDeletable(true);
             }else if(taskComment.getUsers().getId() == loggedUserId){
                 taskComment.setDeletable(true);
             }else{
                 taskComment.setDeletable(false);
             }
             
             commentToList.add(new TaskCommentTO(taskComment));
         }
        }
        return commentToList;
    }
    
    public TaskComment getTaskComment(TaskComment object, Integer userId, Integer taskId){
        TaskComment commentStorable = object;
        
        Task task = taskBusiness.retrieve(taskId);
        User user = userBusiness.retrieve(userId);
        commentStorable.setUsers(user);
        commentStorable.setTasks(task);
        
        return commentStorable;
    }
    
    
    
    private String convertSqlTimeToPrettyPrintFormat(TaskComment taskComment){
        try{
            Timestamp timeStamp = taskComment.getTime();  
            Format timeFormatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            String dateTime = timeFormatter.format(timeStamp).trim();
            return dateTime+" GMT";
        }catch (Exception e) {
         return taskComment.getTime().toString();
     }
    }

    @Override
    public TaskComment storeWithAttachment(TaskComment object, Integer userId,
            Integer taskId, File[] files, String[] fileNames,
            String[] filesContentTypes) {
        
        String commentType = CommentType.TASK_COMMENT.toString();
    
        
        TaskComment commentStorable = getTaskComment(object, userId, taskId);
        this.store(commentStorable);        
        try{
            commentAttachmentsBusiness.store(commentStorable, commentStorable.getId(), commentType, files, fileNames, filesContentTypes);
        }catch (Exception e) {
            System.out.println("Exception while writing files "+e.getMessage());
        }                
        return commentStorable;
    }
    
    public boolean deleteAttachmentsInTaskComments(int taskId){
        if(taskId != 0){
            List<TaskComment> taskComments =  new ArrayList<TaskComment>();
            taskComments = taskCommentDAO.getTaskComments(taskId);
            for (TaskComment taskComment : taskComments) {
                if(taskComment.hasAttachment()){
                    commentAttachmentsBusiness.deleteAttachmentsWithId(taskComment, CommentType.TASK_COMMENT);
                }
            }
            return true;        
        }else {
            return false;
        }
        
    }

 }
