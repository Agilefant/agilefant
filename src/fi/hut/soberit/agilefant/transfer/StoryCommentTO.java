package fi.hut.soberit.agilefant.transfer;

import fi.hut.soberit.agilefant.model.StoryComment;
import fi.hut.soberit.agilefant.util.BeanCopier;

public class StoryCommentTO extends StoryComment {

    /**
     * 
     */
    private static final long serialVersionUID = 1402349542691334439L;
    
    public StoryCommentTO() {
        // TODO Auto-generated constructor stub
    }
    
    public StoryCommentTO(StoryComment storyComment){
        BeanCopier.copy(storyComment, this);
    }

}
