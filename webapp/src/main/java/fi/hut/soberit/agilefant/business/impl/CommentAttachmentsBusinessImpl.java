package fi.hut.soberit.agilefant.business.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.CommentAttachmentsBusiness;
import fi.hut.soberit.agilefant.business.SettingBusiness;
import fi.hut.soberit.agilefant.business.StoryCommentBusiness;
import fi.hut.soberit.agilefant.business.TaskCommentBusiness;
import fi.hut.soberit.agilefant.db.CommentAttachmentsDAO;
import fi.hut.soberit.agilefant.model.CommentAttachments;
import fi.hut.soberit.agilefant.model.StoryComment;
import fi.hut.soberit.agilefant.model.TaskComment;
import fi.hut.soberit.agilefant.util.CommentType;
@Service("commentAttachBusiness")
public class CommentAttachmentsBusinessImpl extends GenericBusinessImpl<CommentAttachments> implements CommentAttachmentsBusiness  {

    @Autowired
    private SettingBusiness settingBusiness;
    @Autowired
    private StoryCommentBusiness storyCommentBusiness;
    @Autowired
    private TaskCommentBusiness taskCommentBusiness;
    
    private CommentAttachmentsDAO commentAttachmentsDAO;
    
    
    public CommentAttachmentsBusinessImpl() {
        super(CommentAttachments.class);
    } 

    public CommentAttachmentsDAO getCommentAttachmentsDAO() {
        return commentAttachmentsDAO;
    }

    @Autowired
    public void setCommentAttachmentsDAO(CommentAttachmentsDAO commentAttachmentsDAO) {
        this.genericDAO = commentAttachmentsDAO;
        this.commentAttachmentsDAO = commentAttachmentsDAO;        
    }



    @Override
    public Collection<CommentAttachments> retrieveAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CommentAttachments retrieve(int id) {
        // TODO Auto-generated method stub
        return commentAttachmentsDAO.get(id);
    }

    @Override
    public Collection<CommentAttachments> retrieveMultiple(
            Collection<Integer> ids) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CommentAttachments retrieveDetached(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(CommentAttachments object) {
    }

    @Override
    public int create(CommentAttachments object) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int countAll() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean exists(int id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public CommentAttachments retrieveIfExists(int id) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    @Transactional
    public CommentAttachments store(Object parent,int objectId, String commentType, File[] files, String[] fileNames, String[] contentTypes) {
        
        String locationForSavingAttachments = settingBusiness.getAttachmentSaveLocation();
        
        if(!locationForSavingAttachments.endsWith(System.getProperty("file.separator"))){
            locationForSavingAttachments+=System.getProperty("file.separator");
        }       
       
        try{
            File subDirectoryForSavingAttachment = null;
            if(commentType.equals("story")){  
                subDirectoryForSavingAttachment = new File(locationForSavingAttachments+commentType+System.getProperty("file.separator")+"story_comment_"+objectId);
                locationForSavingAttachments+= "story_comment_"+objectId;
            }else{
                subDirectoryForSavingAttachment =new File(locationForSavingAttachments+commentType+System.getProperty("file.separator")+"task_comment_"+objectId);
                locationForSavingAttachments+= "task_comment_"+objectId;
            }
            
            if(!subDirectoryForSavingAttachment.exists()){
                if(subDirectoryForSavingAttachment.mkdirs()){
                    System.out.println("Directory created : "+subDirectoryForSavingAttachment.getAbsolutePath());
                    locationForSavingAttachments = subDirectoryForSavingAttachment.getAbsolutePath();
                }else{
                    throw new FileNotFoundException();
                }
            }            
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
      //// Here starts writing files       
      
        File finalDirectoryToWrite = new File(locationForSavingAttachments);
        System.err.println(" Can write to directory " + finalDirectoryToWrite.canWrite());
        System.err.println("Directory Exists : "+finalDirectoryToWrite.exists());
        // Checking the directory existing or not and writable or not 
        if(finalDirectoryToWrite.exists() && finalDirectoryToWrite.canWrite()){ 
            // Yes proceed 
            try{
                for(int i = 0 ; i < files.length; i++){
                    String fileNameToSave = locationForSavingAttachments+System.getProperty("file.separator")+objectId+"_"+fileNames[i]+"_"+i;
                    if(files[i].renameTo(new File(fileNameToSave))){
                        // OK file moved successfully now update database
                        if (!storeInfo(fileNameToSave, fileNames[i], contentTypes[i], commentType, parent)){
                            throw new Exception("Updating attachment table failed");
                        }
                    }else{ // Unable to move file from the location trying to copy the file
                        try {
                            FileUtils.copyFile(files[i], new File(fileNameToSave), true);
                            if (!storeInfo(fileNameToSave, fileNames[i], contentTypes[i], commentType, parent)){
                                throw new Exception("Updating attachment table failed");
                            }
                        } catch (IOException e) {
                            // File Copying failed report to admin & user
                            e.printStackTrace();
                        }
                    }
                }
            }catch (NullPointerException e) {
                System.out.println("Nullpointer exception while file write operation ");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Unhandled exception while trying to write files ");
                e.printStackTrace();
            }
        } else {
            System.out.println("Unable to write files because the given directoy dosen't exists or permission denied while writing file");
        }            
        return null;
    }


    public boolean storeInfo(String filePath, String fileName, String contentType, String attachmentType, Object parent){
        
        CommentAttachments attachments = new CommentAttachments();
        attachments.setAttachmentLocation(filePath);
        attachments.setOriginalFileName(fileName);
        attachments.setFileContentType(contentType);
        if(attachmentType.equals("story")){
            attachments.setStoryComment((StoryComment) parent);
        }
        else{
            attachments.setTaskComment((TaskComment) parent);
        }
        
        this.store(attachments);
        if(attachments.getId() != 0 || attachments.getId() != null){
            return true;
        }else{
            return false;
        }
    }
    
    @Override
    public void delete(int objectId) {
        // TODO Auto-generated method stub
        
    }
public void delete(Object comment , String type){
        commentAttachmentsDAO.delete(comment, type);
}
   
    public CommentAttachments[] retrive(int objectId) {
       
        return this.retrive(objectId);
    }
    
    public void deleteAttachmentsWithId(Object commentObject, CommentType type){
        try {
            if(commentObject != null ){                
                /**
                 * Write condition for checking the directory changes
                 * 
                 */               
                String directoryToDelete = "";
                String parent = "";
                if(CommentType.STORY_COMMENT == type){
                    StoryComment comment = (StoryComment) commentObject;
                    Set<CommentAttachments> attachments = comment.getAttachments();
                    if(!attachments.isEmpty()){
                        parent = attachments.iterator().next().getAttachmentLocation();
                        directoryToDelete+="story_comment_"+comment.getId();
                        directoryToDelete = parent.substring(0, parent.indexOf(System.getProperty("file.separator")+"story_comment_"+comment.getId()+System.getProperty("file.separator")));
                    }                    
                    System.out.println("Directory for delete   :  "+parent.indexOf(System.getProperty("file.separator")+"story_comment_"+comment.getId()+System.getProperty("file.separator")));
                    deleteFile(directoryToDelete+System.getProperty("file.separator")+"story_comment_"+comment.getId()+System.getProperty("file.separator"));
                    delete(commentObject, CommentType.STORY_COMMENT.toString());
                 
                }else if(CommentType.TASK_COMMENT == type){
                    TaskComment comment = (TaskComment) commentObject;
                    Set<CommentAttachments> attachments = comment.getAttachments();
                    if(!attachments.isEmpty()){
                        parent = attachments.iterator().next().getAttachmentLocation();
                        directoryToDelete+="task_comment_"+comment.getId();
                        directoryToDelete = parent.substring(0, parent.indexOf(System.getProperty("file.separator")+"task_comment_"+comment.getId()+System.getProperty("file.separator")));
                    }
                    System.out.println("Directory for delete   :  "+parent.indexOf(System.getProperty("file.separator")+"task_comment_"+comment.getId()+System.getProperty("file.separator")));
                    deleteFile(directoryToDelete+System.getProperty("file.separator")+"task_comment_"+comment.getId()+System.getProperty("file.separator"));
                    delete(commentObject, CommentType.TASK_COMMENT.toString());
                    
                }else{
                    return;
                }
          
            }
        }catch (Exception e) {
            System.out.println("Unhandled exception while deleting files");
            e.printStackTrace();
        }
    }

    public void deleteFile(String path){
        try{            
            File directory = new File(path);
            System.out.println("Directory  : " +path);
            if(!directory.exists()){
                System.out.println("Directory not exists");
            }else{
                System.out.println("Directory found ");
                FileUtils.deleteDirectory(directory);
                System.out.println("Directory removed ");
            }                
        } catch (IOException e) {
            System.out.println("Unable to delete directory");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unhandled exception while deleting files");
        }                  
    }
    
    @Override
    public CommentAttachments getFile(int fileId) {
        return retrieve(fileId);
    }

    

}
