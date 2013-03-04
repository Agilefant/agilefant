package fi.hut.soberit.agilefant.business;

import java.io.File;

import fi.hut.soberit.agilefant.model.CommentAttachments;
import fi.hut.soberit.agilefant.util.CommentType;

public interface CommentAttachmentsBusiness extends GenericBusiness<CommentAttachments>{
    
    public CommentAttachments store(Object parent, int objectId, String commentType, File[] files, String[] fileNames, String[] contentTypes);
    
    public void delete(int objectId);
    public void deleteAttachmentsWithId(Object commentObject, CommentType type);    
    public CommentAttachments[] retrive(int objectId);
    public CommentAttachments getFile(int fileId);

}
