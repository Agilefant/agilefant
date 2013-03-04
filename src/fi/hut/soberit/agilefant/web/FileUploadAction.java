package fi.hut.soberit.agilefant.web;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.business.CommentAttachmentsBusiness;
import fi.hut.soberit.agilefant.business.SettingBusiness;
import fi.hut.soberit.agilefant.model.CommentAttachments;
import fi.hut.soberit.agilefant.util.CommentType;

@Component("fileUploadAction")
@Scope("prototype")
public class FileUploadAction extends ActionSupport implements Prefetching,CRUDAction,ContextAware{

    private static final long serialVersionUID = -3672495790230717472L;
    
    private static final String STORY_COMMENT_ACTION = "storeStoryComment";
    private static final String TASK_COMMENT_ACTION = "storeTaskComment";
    
    @Autowired
    private SettingBusiness  settingBusiness;
    @Autowired
    private CommentAttachmentsBusiness commentAttachmentsBusiness;
    
    private InputStream fileInputStream;    
    private String contentDisposition;
    private String contentType;
    
    private Integer fileId;

    private Integer maxFileSize;
    
    
    private String commentType;
    
    private int maxFIles;
    private String actionMapping;
    private String objectId;
    

    
    
    public String initialize(){   
        System.out.println("Comment Type : "+commentType+"  Object Id : " +objectId);
        if(commentType != null && CommentType.STORY_COMMENT.toString().equals(commentType)){
            actionMapping = STORY_COMMENT_ACTION;
        }else if(commentType != null && CommentType.TASK_COMMENT.toString().equals(commentType)){
            actionMapping = TASK_COMMENT_ACTION;
        }else{
            System.out.println("Unknown Comment type requested");
        }
        if(maxFIles == 0 ){
            maxFIles = settingBusiness.getMaxNumberOfAttachment();            
        }    
        return Action.SUCCESS;
    }
    
    @Override
    public String execute() throws Exception {
     
        if(fileId != 0){
            CommentAttachments attachment = commentAttachmentsBusiness.getFile(fileId);
            if(attachment != null) {
                this.contentType = attachment.getFileContentType();
                this.contentDisposition = "attachment;filename="+attachment.getOriginalFileName();
                try{
                    fileInputStream = new FileInputStream(new File(attachment.getAttachmentLocation()));
                }catch (FileNotFoundException e) {
                    addActionError("Sorry the requested file not found on the server");
                    return Action.ERROR;
                }catch (NullPointerException e) {
                    addActionError("Unhandled exeception while downloading file");
                    System.out.println("Unable to get file nullpointer exception");
                    return Action.ERROR;
                }
            }
        }
        return Action.SUCCESS;
    }
    
    public Integer getFileId() {
        return fileId;
    }
    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }
    
    public String getContentDisposition() {
        return contentDisposition;
    }
    public String getContentType() {
        return contentType;
    }

    public InputStream getFileInputStream() {
        return fileInputStream;
    }
    
    
    @Override
    public String getContextName() {
        // TODO Auto-generated method stub
        return "fileUpload";
    }

    public int getMaxFIles() {
        return maxFIles;
    }

    public void setMaxFIles(int maxFIles) {
        this.maxFIles = settingBusiness.getMaxNumberOfAttachment();
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getActionMapping() {
        return actionMapping;
    }


    public void setActionMapping(String actionMapping) {
        this.actionMapping = actionMapping;
    }
    
    public SettingBusiness getSettingBusiness() {
        return settingBusiness;
    }

    public void setSettingBusiness(SettingBusiness settingBusiness) {
        this.settingBusiness = settingBusiness;
    }
    
    public void setCommentAttachmentsBusiness(
            CommentAttachmentsBusiness commentAttachmentsBusiness) {
        this.commentAttachmentsBusiness = commentAttachmentsBusiness;
    }
    public CommentAttachmentsBusiness getCommentAttachmentsBusiness() {
        return commentAttachmentsBusiness;
    }
    
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    
    public String getObjectId() {
        return objectId;
    }
    @Override
    public int getContextObjectId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String create() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String delete() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String store() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String retrieve() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void initializePrefetchedData(int objectId) {
        
    }
    
    @Override
    public void validate() {
        // TODO Auto-generated method stub
        super.validate();
    }
    
    public Integer getMaxFileSize() {
        return maxFileSize;
    }
    
        
    
    public void setMaxFileSize(Integer maxFileSize) {
        this.maxFileSize =  settingBusiness.getMaxSizeForAllAttachment() * 1024 * 1024;
        if (maxFileSize <= 1){
            this.maxFileSize = 1024 * 1024 * 10;
        }
    }
    
}
