package fi.hut.soberit.agilefant.db.hibernate;

import java.io.File;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.CommentAttachmentsDAO;
import fi.hut.soberit.agilefant.model.CommentAttachments;
import fi.hut.soberit.agilefant.model.StoryComment;
import fi.hut.soberit.agilefant.model.TaskComment;
import fi.hut.soberit.agilefant.util.CommentType;
@Repository
public class CommentAttachmentsDAOHibernate extends
        GenericDAOHibernate<CommentAttachments> implements
        CommentAttachmentsDAO {
    
    public CommentAttachmentsDAOHibernate() {
        super(CommentAttachments.class);
    }

    @Override
    public CommentAttachments store(Object parent, int objectId,
            String commentType, File[] files, String[] fileNames,
            String[] contentTypes) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(int objectId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAttachmentsWithId(int objectId, String type) {
        // TODO Auto-generated method stub

    }

    @Override
    public CommentAttachments[] retrive(int objectId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(Object object, String type) {
        Session session = getCurrentSession();
        String query = "";
        Query deleteQuery;
        
        if(type.equals(CommentType.STORY_COMMENT.toString())){
            query = "Delete from CommentAttachments where storyCommentId="+((StoryComment)object).getId();
            deleteQuery = session.createQuery(query);
            deleteQuery.executeUpdate();
        }else if(type.equals(CommentType.TASK_COMMENT.toString())){
            query = "Delete from CommentAttachments where taskCommentId="+((TaskComment)object).getId();
            deleteQuery = session.createQuery(query);
            deleteQuery.executeUpdate();
       }
      
           
    }

}
