package fi.hut.soberit.agilefant.db.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.StoryRankDAO;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryRank;

@Repository("storyRankDAO")
public class StoryRankDAOHibernate extends GenericDAOHibernate<StoryRank>
        implements StoryRankDAO {

    protected StoryRankDAOHibernate() {
        super(StoryRank.class);
    }

    public StoryRank retrieveByBacklogAndStory(Backlog backlog, Story story) {
        Criteria crit = this.createCriteria(
                StoryRank.class);
        crit.add(Restrictions.eq("backlog", backlog));
        crit.add(Restrictions.eq("story", story));
        return uniqueResult(crit);
    }

    public List<StoryRank> retrieveRanksByBacklog(Backlog backlog) {
        Criteria crit = this.createCriteria(
                StoryRank.class);
        crit.add(Restrictions.eq("backlog", backlog));
        crit.addOrder(Order.asc("rank"));
        return asList(crit);
    }
    
    public Collection<StoryRank> getIterationRanksForStories(Collection<Story> stories) {
        if(stories.isEmpty()) {
            return new ArrayList<StoryRank>();
        }
        Criteria filter = this.createCriteria(StoryRank.class);
        filter.add(Restrictions.in("story", stories));
        
        // Iteration crit
        Criteria iterFilter = filter.createCriteria("backlog");
        iterFilter.add(Restrictions.eq("class", "Iteration"));
        
        return asCollection(filter);
    }
    
    public Collection<StoryRank> getProjectRanksForStories(Collection<Story> stories) {
        if(stories.isEmpty()) {
            return new ArrayList<StoryRank>();
        }
        Criteria filter = this.createCriteria(StoryRank.class);
        filter.add(Restrictions.in("story", stories));
        
        // Project crit
        Criteria projectFilter = filter.createCriteria("backlog");
        projectFilter.add(Restrictions.eq("class", "Project"));
        
        return asCollection(filter);
    }

}
