package fi.hut.soberit.agilefant.db.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.StoryCommentDAO;
import fi.hut.soberit.agilefant.model.StoryComment;

@Repository("storyCommentDAO")
public class StoryCommentDAOHibernate extends GenericDAOHibernate<StoryComment> implements StoryCommentDAO{
    

    

    protected StoryCommentDAOHibernate() {
        super(StoryComment.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<StoryComment> getStoryComments(Integer storyId) {        
        
        Criteria comment = getCurrentSession().createCriteria(StoryComment.class);
        comment.add(Restrictions.eq("stories.id", storyId));
        comment.addOrder(Order.asc("time"));
        return asList(comment);
    }

    @Override
    public StoryComment storeStoryComments(StoryComment storyComment) {               
        return storeStoryComments(storyComment);
    }

    @Override
    public StoryComment getStoryCommentHeadline(Integer storyId) {
        Criteria comment = getCurrentSession().createCriteria(StoryComment.class);
        comment.add(Restrictions.eq("stories.id", storyId));
        comment.addOrder(Order.asc("time"));
        comment.setMaxResults(1);
        List<StoryComment> comments = asList(comment);
        if(comments != null && comments.size() > 0){
            return comments.get(0);
        }
        else {
         return null;
        }
    }
}
