package fi.hut.soberit.agilefant.db;

import java.io.File;

import fi.hut.soberit.agilefant.model.CommentAttachments;

public interface CommentAttachmentsDAO extends GenericDAO<CommentAttachments> {
    
    public CommentAttachments store(Object parent, int objectId, String commentType, File[] files, String[] fileNames, String[] contentTypes);
    
    public void delete(int objectId);
    public void delete(Object comment, String type);
    public void deleteAttachmentsWithId(int objectId, String type);
    
    public CommentAttachments[] retrive(int objectId);
    

}
