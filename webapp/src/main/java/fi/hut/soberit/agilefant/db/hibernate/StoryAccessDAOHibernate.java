package fi.hut.soberit.agilefant.db.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.StoryAccessDAO;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryAccess;
import fi.hut.soberit.agilefant.model.User;

@Repository("storyAccessDAO")
public class StoryAccessDAOHibernate extends GenericDAOHibernate<StoryAccess> implements
        StoryAccessDAO {

    protected StoryAccessDAOHibernate() {
        super(StoryAccess.class);
    }

    public Map<Story, Long> calculateAccessCounts(DateTime start,
            DateTime end, User user) {
        Criteria crit = this.createCriteria(StoryAccess.class);
        crit.add(Restrictions.eq("user", user));
        crit.add(Restrictions.between("date", start, end));
        ProjectionList proj = Projections.projectionList();
        proj.add(Projections.groupProperty("story"));
        proj.add(Projections.count("id"));
        crit.setProjection(proj);
        
        Map<Story, Long> res = new HashMap<Story, Long>();
        
        List<Object[]> data = asList(crit);
        for(Object[] row : data) {
            res.put((Story)row[0], (Long)row[1]);
        }
        return res;
    }

    
}
