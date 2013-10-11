package fi.hut.soberit.agilefant.db.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.Interval;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.business.SearchBusiness;
import fi.hut.soberit.agilefant.db.TaskDAO;
import fi.hut.soberit.agilefant.model.ExactEstimate;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryState;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.TaskState;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.transfer.UnassignedLoadTO;

@Repository("taskDAO")
public class TaskDAOHibernate extends GenericDAOHibernate<Task> implements
        TaskDAO {

    public TaskDAOHibernate() {
        super(Task.class);
    }

    public List<Task> getIterationTasksWithEffortLeft(User user,
            Interval interval) {
        Criteria crit = this.createCriteria(Task.class);
        crit.createCriteria("responsibles")
                .add(Restrictions.idEq(user.getId()));
        Criteria iteration = crit.createCriteria("iteration");
        iteration.setFetchMode("parent", FetchMode.SELECT);
        IterationDAOHelpers.addIterationIntervalLimit(iteration, interval);
        crit.add(Restrictions.isNull("story"));
        crit.add(Restrictions.gt("effortLeft", ExactEstimate.ZERO));
        crit.setFetchMode("creator", FetchMode.SELECT);
        return asList(crit);
    }

    public List<Task> getStoryTasksWithEffortLeft(User user, Interval interval) {
        Criteria crit = this.createCriteria(Task.class);
        crit.createCriteria("responsibles")
                .add(Restrictions.idEq(user.getId()));
       
        Criteria iteration = crit.createCriteria("story").createCriteria("iteration");
        iteration.setFetchMode("parent",FetchMode.SELECT);
        IterationDAOHelpers.addIterationIntervalLimit(iteration, interval);
        crit.add(Restrictions.gt("effortLeft", ExactEstimate.ZERO));
        crit.setFetchMode("creator", FetchMode.SELECT);
        return asList(crit);
    }

    public Map<Integer, Integer> getNumOfResponsiblesByTask(Set<Integer> taskIds) {
        if(taskIds == null || taskIds.size() == 0) {
            return Collections.emptyMap();
        }
        Criteria crit = this.createCriteria(Task.class);
        crit.add(Restrictions.in("id", taskIds));
        crit.createAlias("responsibles", "responsible");
        ProjectionList sums = Projections.projectionList();
        sums.add(Projections.groupProperty("id"));
        sums.add(Projections.count("responsible.id"));
        
        crit.setProjection(sums);
        List<Object[]> rawData = asList(crit);
        
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        for(Object[] row : rawData) {
            result.put((Integer)row[0], ((Long)row[1]).intValue());
        }
        return result;
    }

    public List<Task> getStoryAssignedTasksWithEffortLeft(User user,
            Interval interval) {
        Criteria crit = this.createCriteria(Task.class);
        crit.add(Restrictions.isEmpty("responsibles"));
        Criteria story = crit.createCriteria("story");
        story.createCriteria("responsibles").add(Restrictions.idEq(user.getId()));
        IterationDAOHelpers.addIterationIntervalLimit(story.createCriteria("iteration"), interval);
        crit.setFetchMode("creator", FetchMode.SELECT);
        return asList(crit);
    }

    public List<Task> getAllIterationAndStoryTasks(User user, Interval interval) {
        List<Task> tasks = new ArrayList<Task>();
        
        Criteria crit = this.createCriteria(Task.class);
        crit.createCriteria("responsibles")
            .add(Restrictions.idEq(user.getId()));
        
        Criteria iteration = crit.createCriteria("iteration");
        iteration.setFetchMode("parent", FetchMode.SELECT);
        IterationDAOHelpers.addIterationIntervalLimit(iteration, interval);
        crit.add(Restrictions.isNull("story"));
        crit.add(Restrictions.ne("state", TaskState.DONE));
        crit.setFetchMode("creator", FetchMode.SELECT);

        List<Task> dummy = asList(crit); 
        tasks.addAll(dummy);
        
        crit = this.createCriteria(Task.class);
        crit.createCriteria("responsibles")
            .add(Restrictions.idEq(user.getId()));
        crit.add(Restrictions.ne("state", TaskState.DONE));

        Criteria storyIteration = crit.createCriteria("story").createCriteria("iteration");
        storyIteration.setFetchMode("parent",FetchMode.SELECT);
        IterationDAOHelpers.addIterationIntervalLimit(storyIteration, interval);
        crit.setFetchMode("creator", FetchMode.SELECT);
        
        dummy = asList(crit);
        tasks.addAll(dummy);

        return tasks;
    }
    
    public List<Task> getAllTasks(User user, Interval interval) {
        List<Task> tasks = new ArrayList<Task>();
        
        Criteria crit = this.createCriteria(Task.class);
        crit.createCriteria("responsibles")
            .add(Restrictions.idEq(user.getId()));
        
        Criteria iteration = crit.createCriteria("iteration");
        iteration.setFetchMode("parent", FetchMode.SELECT);
        IterationDAOHelpers.addIterationIntervalLimit(iteration, interval);
        crit.add(Restrictions.isNull("story"));
        crit.add(Restrictions.ne("state", TaskState.DONE));
        crit.setFetchMode("creator", FetchMode.SELECT);

        List<Task> dummy = asList(crit); 
        tasks.addAll(dummy);
        
        crit = this.createCriteria(Task.class);
        crit.createCriteria("responsibles")
            .add(Restrictions.idEq(user.getId()));
        crit.add(Restrictions.ne("state", TaskState.DONE));

        Criteria storyIteration = crit.createCriteria("story");
        storyIteration.setFetchMode("parent",FetchMode.SELECT);
        storyIteration = storyIteration.createCriteria("iteration");
        IterationDAOHelpers.addIterationIntervalLimit(storyIteration, interval);
        crit.setFetchMode("creator", FetchMode.SELECT);
        
        dummy = asList(crit);
        tasks.addAll(dummy);
        
        crit = this.createCriteria(Task.class);
        crit.createCriteria("responsibles")
            .add(Restrictions.idEq(user.getId()));
        crit.add(Restrictions.ne("state", TaskState.DONE));
        crit.add(Restrictions.isNull("iteration"));

        Criteria storyBacklog = crit.createCriteria("story");
        storyBacklog.add(Restrictions.isNull("iteration"));
        storyBacklog.setFetchMode("parent",FetchMode.SELECT);
        storyBacklog = storyBacklog.createCriteria("backlog");
        IterationDAOHelpers.addBacklogIntervalLimit(storyBacklog, interval);
        crit.setFetchMode("creator", FetchMode.SELECT);
        
        dummy = asList(crit);
        tasks.addAll(dummy);

        return tasks;
    }

    public List<UnassignedLoadTO> getUnassignedStoryTasksWithEffortLeft(User user,
            Interval interval) {
        
        Criteria iteration = this.createCriteria(Iteration.class,"iter");
        iteration.createCriteria("assignments","assigments").createCriteria("user").add(Restrictions.idEq(user.getId()));     
        
        Criteria stories = iteration.createCriteria("assignedStories");
        stories.add(Restrictions.isEmpty("responsibles"));
                
        IterationDAOHelpers.addIterationIntervalLimit(iteration, interval);
        
        Criteria tasks = stories.createCriteria("tasks","task");
        tasks.add(Restrictions.isEmpty("responsibles"));
        tasks.add(Restrictions.isNotNull("effortLeft"));
        
        ProjectionList effortLeftSum = Projections.projectionList();
        effortLeftSum.add(Projections.sum("task.effortLeft"));
        effortLeftSum.add(Projections.groupProperty("iter.id"));
        effortLeftSum.add(Projections.property("assigments.availability"));
        
        iteration.setProjection(effortLeftSum);
        
        List<Object[]> data = asList(iteration);
        List<UnassignedLoadTO> result = new ArrayList<UnassignedLoadTO>();
        for(Object[] rowData : data) {             
            UnassignedLoadTO row = new UnassignedLoadTO(new ExactEstimate((Long)rowData[0]),(Integer)rowData[1], ((Integer)rowData[2]).intValue());
            
            result.add(row);
        }
        
        return result;
    }

    public List<UnassignedLoadTO> getUnassignedIterationTasksWithEffortLeft(User user,
            Interval interval) {
        Criteria iteration = this.createCriteria(Iteration.class,"iter");
        iteration.createCriteria("assignments","assigments").createCriteria("user").add(Restrictions.idEq(user.getId()));     
                
        IterationDAOHelpers.addIterationIntervalLimit(iteration, interval);
        
        Criteria tasks = iteration.createCriteria("tasks","task");
        tasks.add(Restrictions.isEmpty("responsibles"));
        tasks.add(Restrictions.isNull("story"));
        
        ProjectionList effortLeftSum = Projections.projectionList();
        effortLeftSum.add(Projections.sum("task.effortLeft"));
        effortLeftSum.add(Projections.groupProperty("iter.id"));
        effortLeftSum.add(Projections.property("assigments.availability"));
        
        iteration.setProjection(effortLeftSum);
        
        List<Object[]> data = asList(iteration);
        List<UnassignedLoadTO> result = new ArrayList<UnassignedLoadTO>();
        for(Object[] rowData : data) {
            long effortLeft = 0l;
            int iterationId = (Integer)rowData[1];
            int availability = (Integer)rowData[2];
            if(rowData[0] != null) {
                effortLeft = (Long)rowData[0];
            }
            UnassignedLoadTO row = new UnassignedLoadTO(new ExactEstimate(effortLeft), iterationId, availability);
            result.add(row);
        }
        
        return result;
    }
    
    /** {@inheritDoc} */
    public Collection<Task> getTasksWithRankBetween(int lower, int upper,
            Iteration parentIteration, Story parentStory) {
        Criteria task = this.createCriteria(Task.class);
        addParentRestriction(task, parentIteration, parentStory);
        task.add(Restrictions.between("rank", lower, upper));
        return asList(task);
    }
    
    /** {@inheritDoc} */
    public Task getNextTaskInRank(int rank, Iteration iteration, Story story) {
        Criteria task = this.createCriteria(Task.class);
        addParentRestriction(task, iteration, story);
        task.add(Restrictions.gt("rank", rank));
        task.addOrder(Order.asc("rank"));
        task.setMaxResults(1);
        return uniqueResult(task);
    }
    
    
    /** {@inheritDoc} */
    public Task getLastTaskInRank(Story story, Iteration iteration) {
        Criteria task = this.createCriteria(Task.class);
        
        addParentRestriction(task, iteration, story);
        
        task.addOrder(Order.desc("rank"));
        task.setMaxResults(1);
        return uniqueResult(task);
    }
    
    private void addParentRestriction(Criteria crit, Iteration iteration, Story story) {
        if (iteration != null) {
            crit.add(Restrictions.eq("iteration.id", iteration.getId()));
        }
        else if (story != null) {
            crit.add(Restrictions.eq("story.id", story.getId()));
        }
    }
    
    /** {@inheritDoc} */
    public List<Task> searchByName(String name)
    {
        Criteria crit = this.createCriteria(Task.class);
        crit.add(Restrictions.like("name", name, MatchMode.ANYWHERE));
        crit.addOrder(Order.asc("name"));
        crit.setMaxResults(SearchBusiness.MAX_RESULTS_PER_TYPE);
        return asList(crit);
    }
}
