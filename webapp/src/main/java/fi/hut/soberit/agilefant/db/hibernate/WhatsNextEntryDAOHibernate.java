package fi.hut.soberit.agilefant.db.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.WhatsNextEntryDAO;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.TaskState;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.model.WhatsNextEntry;

@Repository("whatsNextEntryDAO")
public class WhatsNextEntryDAOHibernate extends GenericDAOHibernate<WhatsNextEntry> implements
WhatsNextEntryDAO {

    public WhatsNextEntryDAOHibernate() {
        super(WhatsNextEntry.class);
    }

    private Criteria filterDoneTasks(Criteria crit) {
        Criteria returned = crit.createCriteria("task");
        returned.add(Restrictions.ne("state", TaskState.DONE));
        return returned;
    }

    public WhatsNextEntry getLastTaskInRank(User user) {
        Criteria entry = this.createCriteria(WhatsNextEntry.class);

        entry.add(Restrictions.eq("user", user));
        entry.addOrder(Order.desc("rank"));
        entry.setFetchMode("user", FetchMode.SELECT);
        entry.setFetchMode("task", FetchMode.JOIN);
        entry.setMaxResults(1);
        filterDoneTasks(entry);

        return uniqueResult(entry);
    }

    public Collection<WhatsNextEntry> getTasksWithRankBetween(int lower, int upper, User user) {
        Criteria entry = this.createCriteria(WhatsNextEntry.class);
        entry.add(Restrictions.eq("user", user));
        entry.add(Restrictions.between("rank", lower, upper));
        entry.setFetchMode("user", FetchMode.SELECT);
        entry.setFetchMode("task", FetchMode.JOIN);
        return asList(entry);
    }

    public WhatsNextEntry getWhatsNextEntryFor(User user, Task task) {
        Criteria crit = this.createCriteria(WhatsNextEntry.class);
        crit.add(Restrictions.eq("user", user));
        crit.add(Restrictions.eq("task", task));

        // Filter out tasks that are done!
        // filterDoneTasks(crit);
        crit.setMaxResults(1);
        return uniqueResult(crit);
    }

    public Collection<WhatsNextEntry> getWhatsNextEntriesFor(User user) {
        Criteria crit = this.createCriteria(WhatsNextEntry.class);
        crit.add(Restrictions.eq("user", user));
        crit.setFetchMode("task", FetchMode.JOIN);

        filterDoneTasks(crit);

        crit.addOrder(Order.asc("rank"));
        return asList(crit);
    }
    
    public Collection<WhatsNextEntry> getAllWorkQueueEntriesFor(Task task) {
        Criteria crit = this.createCriteria(WhatsNextEntry.class);
        crit.add(Restrictions.eq("task", task));
        crit.setFetchMode("task", FetchMode.SELECT);
        return asList(crit);
    }

    public void removeAllByTask(Task task) {
        // needs to use this for cascading rules to work!
        for (WhatsNextEntry entry: getAllWorkQueueEntriesFor(task)) {
            remove(entry);
        };
    }
    
    public Map<User, List<Task>> getTopmostWorkQueueEntries() {
        String hqlQuery = "SELECT user, entry.task " +
                          "FROM User as user, WhatsNextEntry as entry " +
                          "WHERE entry.user = user AND " +
                          "entry.rank = (" +
                              "SELECT min(e.rank) FROM WhatsNextEntry as e "+
                              "WHERE e.user = user" +
                          ")"; 
        
        Query q = getCurrentSession().createQuery(hqlQuery);

        List<?> returned = this.asList(q);
        
        Map<User, List<Task>> returnValue = new HashMap<User, List<Task>>(returned.size()); 

        for (Object o: returned) {
            Object[] array = (Object[])o;
            User user = (User)array[0];
            Task task = (Task)array[1];

            List<Task> tasks = returnValue.get(user);
            if (tasks == null) {
                tasks = new ArrayList<Task>();
                returnValue.put(user, tasks);
            }

            tasks.add(task);
        }        
        return returnValue;
    };
}
