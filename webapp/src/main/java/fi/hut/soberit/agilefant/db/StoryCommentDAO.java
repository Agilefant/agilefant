package fi.hut.soberit.agilefant.db;

import java.util.List;

import fi.hut.soberit.agilefant.model.StoryComment;

public interface StoryCommentDAO extends GenericDAO<StoryComment> {
    
    public List<StoryComment> getStoryComments(Integer storyId);
    public StoryComment storeStoryComments(StoryComment storyComment); 
    public StoryComment getStoryCommentHeadline(Integer storyId);
    
    

}
