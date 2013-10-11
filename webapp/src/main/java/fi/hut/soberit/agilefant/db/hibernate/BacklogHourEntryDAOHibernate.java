package fi.hut.soberit.agilefant.db.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.BacklogHourEntryDAO;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.BacklogHourEntry;

@Repository("backlogHourEntryDAO")
public class BacklogHourEntryDAOHibernate extends GenericDAOHibernate<BacklogHourEntry> implements
        BacklogHourEntryDAO {

    public BacklogHourEntryDAOHibernate() {
        super(BacklogHourEntry.class);
    }
    
    @SuppressWarnings("unchecked")
    public List<BacklogHourEntry> retrieveByBacklog(Backlog target) {
    	Criteria criteria = this.createCriteria(this.getPersistentClass());
    	criteria.add(Restrictions.eq("backlog", target));
    	return this.asList(criteria);
    }

}
