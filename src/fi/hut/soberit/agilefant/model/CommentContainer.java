package fi.hut.soberit.agilefant.model;

import java.util.Set;

public interface CommentContainer {
    
    public Set<StoryComment> getComments();
    public void setComments(Set<StoryComment> storyComments);
    //public String getStory
    public int getId();

}
