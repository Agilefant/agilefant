package fi.hut.soberit.agilefant.web;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.annotations.PrefetchId;
import fi.hut.soberit.agilefant.business.SettingBusiness;
import fi.hut.soberit.agilefant.business.StoryCommentBusiness;
import fi.hut.soberit.agilefant.model.StoryComment;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import fi.hut.soberit.agilefant.transfer.StoryCommentTO;

@Component("storyCommentAction")
@Scope("prototype")
public class StoryCommentAction extends ActionSupport implements Prefetching,CRUDAction,ContextAware {

    /**
     * 
     */
    private static final long serialVersionUID = 950827656676118173L;
    
    
    @Autowired
    private StoryCommentBusiness storyCommentBusiness;  
    @Autowired
    private SettingBusiness settingBusiness;
    
    private File[] attachments;
    private String[] attachmentsContentType;
    private String[] attachmentsFileNames;
      
    private StoryComment storyComment;
    
    private Story story;
    
    private String commentEnterd;
    
    private int maximumSize;
    
    private String headline;
    private final String type = "story";

    @PrefetchId
    private int commentId;
    private int objectId;
    private int userId;
        
    private Collection<StoryCommentTO> comments = new ArrayList<StoryCommentTO>(); 

    
    public StoryCommentAction() {

    }


    @Override
    public String create() {
        commentId = 0;
        storyComment = new StoryComment();
        return Action.SUCCESS;
    }


    @Override
    public String delete() {
        storyCommentBusiness.deleteComment(commentId);
        return Action.SUCCESS;
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
    
    
    @Override
    public String store() {
        System.out.println("Reached for storing comments");
        
        
        java.util.Date date= new java.util.Date();
        if(storyComment == null){
            storyComment = new StoryComment();
        }
        this.storyComment.setTime(new Timestamp(getCurrentTimeInGMT().getTime()));
        this.storyComment.setComment(commentEnterd);
        
        if(attachments != null && attachments.length != 0){
            if(!validateFiles()){
                return Action.ERROR;
            }
            this.storyComment = storyCommentBusiness.storeWithAttachment(storyComment, SecurityUtil.getLoggedUserId(), objectId,attachments,attachmentsFileNames,attachmentsContentType);
        } else{
            this.storyComment = storyCommentBusiness.store(storyComment, SecurityUtil.getLoggedUserId(), objectId);
        }
  
        
        this.commentId = this.storyComment.getId();
        if(commentId != 0){
            commentEnterd ="";
        }
        return Action.SUCCESS;
    }

    @Override
    public String retrieve() {
        comments = storyCommentBusiness.getComments(objectId);
        return Action.SUCCESS;
    }

    @Override
    public void initializePrefetchedData(int objectId) {
       storyComment = storyCommentBusiness.retrieveDetached(commentId);        
    }


    public String getStoryCommentHeadline(){
        headline = storyCommentBusiness.getHeadlineComment(objectId);
       return Action.SUCCESS;
    }
    
    
    public StoryCommentBusiness getCommentBusiness() {
        return storyCommentBusiness;
    }


    public void setCommentBusiness(StoryCommentBusiness storyCommentBusiness) {
        this.storyCommentBusiness = storyCommentBusiness;
    }

    public SettingBusiness getSettingBusiness() {
        return settingBusiness;
    }


    public void setSettingBusiness(SettingBusiness settingBusiness) {
        this.settingBusiness = settingBusiness;
    }


    public int getCommentId() {
        return commentId;
    }


    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public Collection<StoryCommentTO> getComments() {
        return comments;
    }


    public void setComments(Collection<StoryCommentTO> comments) {
        this.comments = comments;
    }


    public int getObjectId() {
        return objectId;
    }


    public void setObjectId(int storyId) {
        this.objectId = storyId;
    }


    public int getUserId() {
        return userId;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }


    @Override
    public String getContextName() {
        return "storyComment";
    }


    public StoryComment getComment() {
        return storyComment;
    }


    public void setComment(StoryComment storyComment) {
        this.storyComment = storyComment;
    }


    @Override
    public int getContextObjectId() {
        return commentId;
    } 
    
    public String getCommentEnterd() {
        return commentEnterd;
    }


    public void setCommentEnterd(String commentEnterd) {
        this.commentEnterd = commentEnterd;
    }


    public Story getStory() {
        return story;
    }
    
    public void setStory(Story story) {
        this.story = story;
    }


    public String getHeadline() {
        return headline;
    }


    public void setHeadline(String headline) {
        this.headline = headline;
    }
 
    public int getMaximumSize() {
        this.maximumSize =  settingBusiness.getMaxSizeForAllAttachment() * 1024 * 1024;
        if (maximumSize <= 1){
            this.maximumSize = 1024 * 1024 * 10;
        }
        return maximumSize;
    }
    
    public void setMaximumSize(int maximumSize) {
        
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
