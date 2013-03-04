package fi.hut.soberit.agilefant.business;

import java.io.File;
import java.util.Collection;
import fi.hut.soberit.agilefant.model.StoryComment;
import fi.hut.soberit.agilefant.transfer.StoryCommentTO;

public interface StoryCommentBusiness extends GenericBusiness<StoryComment> {
    
    public StoryComment store(StoryComment object, Integer userId, Integer storyId);
    public StoryComment storeWithAttachment(StoryComment object, Integer userId, Integer storyId, File[] files, String[] fileNames, String[] filesContentTypes);
    public String getHeadlineComment(int storyId);
    public Collection<StoryCommentTO> getComments(Integer storyId);
    public void   deleteComment(Integer commentId);
    public boolean deleteAttachmentsInStoryComments(int storyId);
    public StoryComment   retrive(int commentId);
    
}
