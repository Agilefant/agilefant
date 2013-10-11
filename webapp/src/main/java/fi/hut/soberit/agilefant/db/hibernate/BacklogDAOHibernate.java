package fi.hut.soberit.agilefant.db.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.business.SearchBusiness;
import fi.hut.soberit.agilefant.db.BacklogDAO;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryState;

/**
 * Hibernate implementation of BacklogDAO interface using GenericDAOHibernate.
 */
@Repository("backlogDAO")
public class BacklogDAOHibernate extends GenericDAOHibernate<Backlog> implements
        BacklogDAO {

    public BacklogDAOHibernate() {
        super(Backlog.class);
    }

    /** {@inheritDoc} */
    public int getNumberOfChildren(Backlog backlog) {
        Criteria criteria = this.createCriteria(Backlog.class);
        criteria.add(Restrictions.idEq(backlog.getId()));
        criteria.createCriteria("children");
        criteria.setProjection(Projections.rowCount());
        return ((Long)this.asList(criteria).get(0)).intValue();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getResponsiblesByBacklog(Backlog backlog) {
        String hql = "from Story as story left outer join story.responsibles as resp WHERE story.backlog = ?";
        return this.asTuplesList(this.getCurrentSession().createQuery(hql).setEntity(0, backlog));
    }

    public int calculateStoryPointSum(int iterationId) {
        Criteria crit = this.createCriteria(Story.class);
        crit.setProjection(Projections.sum("storyPoints"));
        crit.createCriteria("iteration").add(Restrictions.idEq(iterationId));
        Long result = uniqueResult(crit);
        if (result == null) return 0;
        return result.intValue();
    }
    
    public int calculateDoneStoryPointSum(int iterationId) {
        Criteria crit = this.createCriteria(Story.class);
        crit.setProjection(Projections.sum("storyPoints"));
        crit.createCriteria("iteration").add(Restrictions.idEq(iterationId));
        crit.add(Restrictions.eq("state", StoryState.DONE));
        Long result = uniqueResult(crit);
        if (result == null) return 0;
        return result.intValue();
    }
    
    
    public List<Backlog> searchByName(String name) {
        return searchByName(name, Backlog.class);
    }
    
    public List<Backlog> searchByName(String name, Class<?> type) {
        Criteria crit = this.createCriteria(type);
        crit.add(Restrictions.like("name", name, MatchMode.ANYWHERE));
        crit.addOrder(Order.asc("class"));
        crit.addOrder(Order.asc("name"));
        crit.setMaxResults(SearchBusiness.MAX_RESULTS_PER_TYPE);
        return asList(crit);
    }
    
    public Collection<Backlog> retrieveStandaloneIterations() {
        Criteria crit = this.createCriteria(Backlog.class);
        crit.add(Restrictions.sqlRestriction("{alias}.parent_id is NULL"));
        crit.add(Restrictions.sqlRestriction("{alias}.backlogType like 'Iteration'"));
        crit.setMaxResults(SearchBusiness.MAX_RESULTS_PER_TYPE);
        return asCollection(crit);
    }
}
