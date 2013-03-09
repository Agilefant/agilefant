package fi.hut.soberit.agilefant.business.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.CommentAttachmentsBusiness;
import fi.hut.soberit.agilefant.business.SettingBusiness;
import fi.hut.soberit.agilefant.business.StoryCommentBusiness;
import fi.hut.soberit.agilefant.business.StoryBusiness;
import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.db.StoryCommentDAO;
import fi.hut.soberit.agilefant.model.CommentAttachments;
import fi.hut.soberit.agilefant.model.StoryComment;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import fi.hut.soberit.agilefant.transfer.StoryCommentTO;
import fi.hut.soberit.agilefant.util.CommentType;
@Service("storyCommentBusiness")
@Transactional
public class StoryCommentBusinessImpl extends GenericBusinessImpl<StoryComment> implements StoryCommentBusiness {

    private StoryCommentDAO storyCommentDAO;
    
    @Autowired
    private StoryBusiness storyBusiness;
    
    @Autowired
    private UserBusiness userBusiness;
    
    @Autowired
    private SettingBusiness settingBusiness;
    
    @Autowired
    private CommentAttachmentsBusiness commentAttachmentsBusiness;
    
    
    public StoryCommentBusinessImpl() {
        super(StoryComment.class);
    }

    @Autowired
    public void setStoryCommentDAO(StoryCommentDAO storyCommentDAO) {
        this.genericDAO = storyCommentDAO;
        this.storyCommentDAO = storyCommentDAO;
    }
    
    @Override
    public int create(StoryComment object) {
        // TODO Auto-generated method stub
        return super.create(object);
    }

    @Override
    public StoryComment store(StoryComment object, Integer userId, Integer storyId) {
        StoryComment commentStorable = getStoryComment(object, userId, storyId);
        
        this.store(commentStorable);
        return commentStorable;      
    }
    
    public StoryComment getStoryComment(StoryComment object, Integer userId, Integer storyId){
        StoryComment commentStorable = object;   
        commentStorable = object;
        
        Story story = storyBusiness.retrieve(storyId);
        User user = userBusiness.retrieve(userId);
        
        commentStorable.setUsers(user);
        commentStorable.setStories(story);
        return commentStorable; 
    }

    
    @Override
    public Collection<StoryCommentTO> getComments(Integer storyId) {               
        return convertToCommentTo(storyCommentDAO.getStoryComments(storyId));
    }

    @Override
    public void deleteComment(Integer commentId) {
         delete(retrieve(commentId));
    }

    @Override
    public StoryComment retrive(int commentId) {
        // TODO Auto-generated method stub
        return retrieve(commentId);
    }
    
    
   public List<StoryCommentTO> convertToCommentTo(List<StoryComment> storyComments){
       List<StoryCommentTO> commentToList = new ArrayList<StoryCommentTO>();
       int loggedUserId = SecurityUtil.getLoggedUserId();
       boolean isAdmin = SecurityUtil.getLoggedUser().isAdmin();
       if(storyComments != null && storyComments.size() > 0){
           for (StoryComment storyComment : storyComments) {
            storyComment.setParsedTime(convertSqlTimeToPrettyPrintFormat(storyComment));
            if(isAdmin){
                storyComment.setDeletable(true);
            }else if(storyComment.getUsers().getId() == loggedUserId){
                storyComment.setDeletable(true);
            }else{
                storyComment.setDeletable(false);
            }
            commentToList.add(new StoryCommentTO(storyComment));
        }
       }
       Collections.reverse(commentToList);
       return commentToList;
   }
   
   private String convertSqlTimeToPrettyPrintFormat(StoryComment storyComment){
       try{
           Timestamp timeStamp = storyComment.getTime();  
           Format timeFormatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
           String dateTime = timeFormatter.format(timeStamp).toString();
           return dateTime+" GMT";
       }catch (Exception e) {
        return storyComment.getTime().toString();
    }
   }
   


@Override
public String getHeadlineComment(int storyId) {
        StoryComment storyComment = storyCommentDAO.getStoryCommentHeadline(storyId);
        String headLine = "";
        if(storyComment != null){
            headLine = storyComment.getComment();
            if(headLine.length() > 120 ){
                headLine = headLine.substring(0, 120)+"...<more>";
            }
        }else{
            headLine = "Nothing to display";
        }
    return headLine;
}

@Override
@Transactional
public StoryComment storeWithAttachment(StoryComment object, Integer userId,
        Integer storyId, File[] files, String[] fileNames,
        String[] filesContentTypes) {
    
        String commentType = CommentType.STORY_COMMENT.toString();
    
        
        StoryComment commentStorable = getStoryComment(object, userId, storyId);
        this.store(commentStorable);        
        System.out.println("Stored Comment ID  : "+commentStorable.getId());
        try{
            commentAttachmentsBusiness.store(commentStorable, commentStorable.getId(), commentType, files, fileNames, filesContentTypes);
        }catch (Exception e) {
            System.out.println("Exception while writing files "+e.getMessage());
        }
       
   return commentStorable;
}



public boolean deleteAttachmentsInStoryComments(int storyId){
    System.out.println("Story Id for delete : "+storyId);
    if(storyId != 0){
        List<StoryComment> storyComments =  new ArrayList<StoryComment>();
        storyComments = storyCommentDAO.getStoryComments(storyId);
        for (StoryComment storyComment : storyComments) {
            if(storyComment.hasAttachment()){
                System.out.println("Story Comment Id : "+storyComment.getId());
                commentAttachmentsBusiness.deleteAttachmentsWithId(storyComment, CommentType.STORY_COMMENT);
            }
        }
        return true;        
    }else {
        return false;
    }
    
}



   

}
