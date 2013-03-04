package fi.hut.soberit.agilefant.db.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.TaskCommentDAO;
import fi.hut.soberit.agilefant.model.TaskComment;
@Repository("taskCommentDAO")
public class TaskCommentDAOHibernate extends GenericDAOHibernate<TaskComment> implements TaskCommentDAO {

    public TaskCommentDAOHibernate() {
        super(TaskComment.class);
    }
    @Override
    public List<TaskComment> getTaskComments(Integer taskId) {
        Criteria comment = getCurrentSession().createCriteria(TaskComment.class);
        comment.add(Restrictions.eq("tasks.id", taskId));
        comment.addOrder(Order.desc("time"));
        return asList(comment);
    }

    @Override
    public TaskComment storeTaskComments(TaskComment taskComment) {
        return storeTaskComments(taskComment);
    }

}
