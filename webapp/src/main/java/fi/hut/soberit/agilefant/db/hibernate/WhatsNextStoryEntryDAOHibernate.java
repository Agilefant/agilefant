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

import fi.hut.soberit.agilefant.db.WhatsNextStoryEntryDAO;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryState;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.model.WhatsNextStoryEntry;

@Repository("whatsNextStoryEntryDAO")
public class WhatsNextStoryEntryDAOHibernate extends GenericDAOHibernate<WhatsNextStoryEntry> implements
WhatsNextStoryEntryDAO {

    public WhatsNextStoryEntryDAOHibernate() {
        super(WhatsNextStoryEntry.class);
    }

    private Criteria filterDoneStories(Criteria crit) {
        Criteria returned = crit.createCriteria("story");
        returned.add(Restrictions.ne("state", StoryState.DONE));
        return returned;
    }

    public WhatsNextStoryEntry getLastStoryInRank(User user) {
        Criteria entry = this.createCriteria(WhatsNextStoryEntry.class);

        entry.add(Restrictions.eq("user", user));
        entry.addOrder(Order.desc("rank"));
        entry.setFetchMode("user", FetchMode.SELECT);
        entry.setFetchMode("story", FetchMode.JOIN);
        entry.setMaxResults(1);
        filterDoneStories(entry);

        return uniqueResult(entry);
    }

    public Collection<WhatsNextStoryEntry> getStoriesWithRankBetween(int lower, int upper, User user) {
        Criteria entry = this.createCriteria(WhatsNextStoryEntry.class);
        entry.add(Restrictions.eq("user", user));
        entry.add(Restrictions.between("rank", lower, upper));
        entry.setFetchMode("user", FetchMode.SELECT);
        entry.setFetchMode("story", FetchMode.JOIN);
        return asList(entry);
    }

    public WhatsNextStoryEntry getWhatsNextStoryEntryFor(User user, Story story) {
        Criteria crit = this.createCriteria(WhatsNextStoryEntry.class);
        crit.add(Restrictions.eq("user", user));
        crit.add(Restrictions.eq("story", story));

        // Filter out stories that are done!
        // filterDoneStories(crit);
        crit.setMaxResults(1);
        return uniqueResult(crit);
    }

    public Collection<WhatsNextStoryEntry> getWhatsNextStoryEntriesFor(User user) {
        Criteria crit = this.createCriteria(WhatsNextStoryEntry.class);
        crit.add(Restrictions.eq("user", user));
        crit.setFetchMode("story", FetchMode.JOIN);

        filterDoneStories(crit);

        crit.addOrder(Order.asc("rank"));
        return asList(crit);
    }
    
    public Collection<WhatsNextStoryEntry> getAllWorkQueueEntriesFor(Story story) {
        Criteria crit = this.createCriteria(WhatsNextStoryEntry.class);
        crit.add(Restrictions.eq("story", story));
        crit.setFetchMode("story", FetchMode.SELECT);
        return asList(crit);
    }

    public void removeAllByStory(Story story) {
        // needs to use this for cascading rules to work!
        for (WhatsNextStoryEntry entry: getAllWorkQueueEntriesFor(story)) {
            remove(entry);
        };
    }
    
    public Map<User, List<Story>> getTopmostWorkQueueEntries() {
        String hqlQuery = "SELECT user, entry.story " +
                          "FROM User as user, WhatsNextStoryEntry as entry " +
                          "WHERE entry.user = user AND " +
                          "entry.rank = (" +
                              "SELECT min(e.rank) FROM WhatsNextStoryEntry as e "+
                              "WHERE e.user = user" +
                          ")"; 
        
        Query q = getCurrentSession().createQuery(hqlQuery);

        List<?> returned = this.asList(q);
        
        Map<User, List<Story>> returnValue = new HashMap<User, List<Story>>(returned.size()); 

        for (Object o: returned) {
            Object[] array = (Object[])o;
            User user = (User)array[0];
            Story story = (Story)array[1];

            List<Story> stories = returnValue.get(user);
            if (stories == null) {
                stories = new ArrayList<Story>();
                returnValue.put(user, stories);
            }

            stories.add(story);
        }        
        return returnValue;
    };
}
