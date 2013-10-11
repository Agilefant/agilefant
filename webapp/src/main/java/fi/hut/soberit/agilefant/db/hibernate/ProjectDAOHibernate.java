package fi.hut.soberit.agilefant.db.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.ProjectDAO;
import fi.hut.soberit.agilefant.model.BacklogHistoryEntry;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryState;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.transfer.ProjectMetrics;

/**
 * Hibernate implementation of ProjectDAO interface using
 * GenericDAOHibernate.
 */
@Repository("projectDAO")
public class ProjectDAOHibernate extends GenericDAOHibernate<Project> implements
        ProjectDAO {

    public ProjectDAOHibernate() {
        super(Project.class);
    }
    
    public Collection<User> getAssignedUsers(Project project) {
        Criteria crit = this.createCriteria(User.class);
        crit = crit.createCriteria("assignments");
        crit = crit.createCriteria("backlog");
        crit.add(Restrictions.idEq(project.getId()));
        return asCollection(crit);
    }
    
    public Collection<Project> getProjectsWithUserAssigned(User user) {
        Criteria criteria = this.createCriteria(User.class);
        criteria.add(Restrictions.idEq(user.getId()));
        criteria = criteria.createCriteria("assignments");
        criteria = criteria.createCriteria("backlog");
        return asCollection(criteria);
    }

    public List<Project> getActiveProjectsSortedByRank() {
        Criteria crit = this.createCriteria(Project.class);
        crit.add(Restrictions.gt("endDate", new DateTime()));
        crit.addOrder(Order.desc("rank"));
        return asList(crit);
    }
    
    public Collection<Project> getProjectsWithRankBetween(int lower, int upper) {
        Criteria crit = this.createCriteria(Project.class);
        crit.add(Restrictions.between("rank", lower, upper));
        return asCollection(crit);
    }
    
    public Collection<Project> getUnrankedProjects(LocalDate startDate, LocalDate endDate) {
        Criteria crit = this.createCriteria(Project.class);
        crit.add(Restrictions.ge("endDate", startDate.toDateTimeAtStartOfDay()));
        crit.add(Restrictions.le("startDate", endDate.toDateTimeAtStartOfDay()));
        crit.add(Restrictions.lt("rank", 1));
        return asCollection(crit);
    }
           
    public List<Project> getRankedProjects(LocalDate startDate, LocalDate endDate) {
        Criteria crit = this.createCriteria(Project.class);
        crit.add(Restrictions.ge("endDate", startDate.toDateTimeAtStartOfDay()));
        crit.add(Restrictions.le("startDate", endDate.toDateTimeAtStartOfDay()));
        crit.add(Restrictions.gt("rank", 0));
        crit.addOrder(Order.asc("rank"));
        return asList(crit);
    }
    
    public Project getMaxRankedProject() {
        Criteria crit = this.createCriteria(Project.class);
        crit.add(Restrictions.gt("rank", 0));
        crit.addOrder(Order.desc("rank"));
        crit.setMaxResults(1);
        return uniqueResult(crit);
    }

    public Project getProjectWithRankLessThan(int rank) {
        Criteria crit = this.createCriteria(Project.class);
        crit.add(Restrictions.lt("rank", rank));
        crit.add(Restrictions.gt("rank", 0));
        crit.addOrder(Order.asc("rank"));
        crit.setMaxResults(1);
        return uniqueResult(crit);
    }
    
    public void increaseRankedProjectRanks() {
        Query query = getCurrentSession().createQuery("UPDATE Project project SET project.rank = project.rank + 1 WHERE project.rank > 0");
        query.executeUpdate();
    }
    
    public List<Project> retrieveActiveWithUserAssigned(int userId) {
        Criteria crit = this.createCriteria(Project.class);
        crit.add(Restrictions.gt("endDate", new DateTime()));
        crit = crit.createCriteria("assignments");
        crit = crit.createCriteria("user");
        crit.add(Restrictions.idEq(userId));
        return asList(crit);
    }
    
    public List<BacklogHistoryEntry> getHistoryEntriesForProject(
            int projectId) {
        Criteria crit = this.createCriteria(BacklogHistoryEntry.class);
        crit.add(Restrictions.eq("backlog.id", projectId));
        crit.addOrder(Order.asc("timestamp"));
        return asList(crit);
    }
    
    private int toInt(Object obj) {
        if(obj != null) {
            return ((Long)obj).intValue();
        }
        return 0;
    }
    public ProjectMetrics calculateProjectStoryMetrics(int backlogId) {
        Criteria crit = this.createCriteria(Story.class);
        ProjectionList proj = Projections.projectionList();
        proj.add(Projections.sum("storyPoints"));
        proj.add(Projections.sum("storyValue"));
        proj.add(Projections.count("id"));
        proj.add(Projections.groupProperty("state"));
        crit.setProjection(proj);
        crit.createCriteria("storyRanks", "ranks").createCriteria("backlog", "backlog").add(Restrictions.idEq(backlogId));
        List<Object[]> res = asList(crit);
        ProjectMetrics metrics = new ProjectMetrics();
        for(Object[] row : res) {
            if((StoryState)row[3] == StoryState.DONE) {
                metrics.setCompletedStoryPoints(metrics.getCompletedStoryPoints() + toInt(row[0]));
                metrics.setNumberOfDoneStories(metrics.getNumberOfDoneStories() + toInt(row[2]));
                
                // Value metric
                metrics.setCompletedValue(metrics.getCompletedValue() + toInt(row[1]));
            } 
            if((StoryState)row[3] != StoryState.DEFERRED) {
                metrics.setStoryPoints(metrics.getStoryPoints() + toInt(row[0]));
                metrics.setNumberOfStories(metrics.getNumberOfStories() + toInt(row[2]));
                
                // Value metric
                metrics.setTotalValue(metrics.getTotalValue() + toInt(row[1]));
            }
        }
        return metrics;
    }
    
}
