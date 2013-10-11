package fi.hut.soberit.agilefant.db.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.IterationDAO;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryHourEntry;
import fi.hut.soberit.agilefant.model.StoryState;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.TaskHourEntry;
import fi.hut.soberit.agilefant.model.TaskState;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.util.Pair;
import fi.hut.soberit.agilefant.util.StoryMetrics;

/**
 * Hibernate implementation of IterationDAO interface using GenericDAOHibernate.
 */
@Repository("iterationDAO")
public class IterationDAOHibernate extends GenericDAOHibernate<Iteration>
        implements IterationDAO {

    public IterationDAOHibernate() {
        super(Iteration.class);
    }

    private Criteria addIterationRestriction(Criteria criteria,
            Collection<String> joins, Iteration iteration) {
        for (String join : joins)
            criteria = criteria.createCriteria(join);
        criteria.add(Restrictions.idEq(iteration.getId()));
        return criteria;
    }

    public List<Task> getAllTasksForIteration(Iteration iteration) {
        Criteria storyTaskCrit = this.createCriteria(Task.class);
        storyTaskCrit.setFetchMode("iteration",FetchMode.SELECT);
        storyTaskCrit = storyTaskCrit.createCriteria("story").createCriteria("iteration");
        storyTaskCrit.add(Restrictions.idEq(iteration.getId()));
        
        Criteria tasksWoStoryCrit = this.createCriteria(Task.class);
        tasksWoStoryCrit.setFetchMode("story", FetchMode.SELECT);
        tasksWoStoryCrit = tasksWoStoryCrit.createCriteria("iteration");
        tasksWoStoryCrit.add(Restrictions.idEq(iteration.getId()));
        
        List<Task> tasks = new ArrayList<Task>();
        List<Task> storyTasks = asList(storyTaskCrit);
        tasks.addAll(storyTasks);
        List<Task> iterationTasks = asList(tasksWoStoryCrit);
        tasks.addAll(iterationTasks);

        return tasks;
    }

    public Map<StoryState, Integer> countIterationStoriesByState(int iterationId) {
        Criteria criteria = this.createCriteria(Story.class);
        criteria.add(Restrictions.eq("iteration.id", iterationId));
        criteria.setProjection(Projections.projectionList().add(
                Projections.property("state")).add(Projections.rowCount(),
                "storyCount").add(Projections.groupProperty("state"), "state"));

        Map<StoryState, Integer> results = new EnumMap<StoryState, Integer>(
                StoryState.class);

        for (StoryState state : StoryState.values()) {
            results.put(state, 0);
        }

        List<Object[]> queryResults = asList(criteria);

        for (Object[] row : queryResults) {
            StoryState state = (StoryState) row[0];
            Integer count = ((Long) row[1]).intValue();
            results.put(state, count);
        }

        return results;
    }

    private Pair<Integer, Integer> getGenericCountDoneNonDeferred(Class<?> type,
            Collection<String> joins, Iteration iteration) {
            Criteria criteria = this.createCriteria(type);
            criteria.setProjection(Projections.projectionList().add(
                    Projections.property("state")).add(Projections.rowCount(),
                    "taskCount").add(Projections.groupProperty("state"), "state"));
            criteria = addIterationRestriction(criteria, joins, iteration);
            
            List<Object[]> results = asList(criteria);
            int total = 0;
            int done = 0;
            for (Object[] row : results) {
                Long count = (Long) row[1];
                total += (row[0].equals(TaskState.DEFERRED)) ? 0 : count;
                done  += (row[0].equals(TaskState.DONE)) ? count : 0;
            }
        return Pair.create(done, total);
    }
    
    private Pair<Integer, Integer> getCounOfDoneAndAllNonDeffered(Class<?> type,
            Object doneValue, Collection<String> joins, Iteration iteration) {
        Criteria criteria = this.createCriteria(type);
        criteria.setProjection(Projections.projectionList().add(
                Projections.property("state")).add(Projections.rowCount(),
                "taskCount").add(Projections.groupProperty("state"), "state"));
        criteria = addIterationRestriction(criteria, joins, iteration);

        List<Object[]> results = asList(criteria);

        int total = 0;
        int done = 0;

        for (Object[] row : results) {
            Long count = (Long) row[1];
            if(type == Story.class){
                total += (row[0].equals(StoryState.DEFERRED)) ? 0 : count;
            }
            else if(type == Task.class){
                total += (row[0].equals(TaskState.DEFERRED)) ? 0 : count;
            }
            if (row[0].equals(doneValue))
                done += count;
        }

        return Pair.create(done, total);
    }
    
    public Pair<Integer, Integer> getCountOfDoneAndAllNonDeferredTasks(Iteration iteration) {
        List<Task> tasks = getAllTasksForIteration(iteration);
        
        int done = 0;
        int total = 0;
        for(Task task: tasks) {
            if(task.getStory() != null && task.getStory().getState() != StoryState.DEFERRED && task.getState() != TaskState.DEFERRED) {
                total++;
                if(task.getState() == TaskState.DONE){
                    done++;
                }
            }
            else if(task.getStory() == null && task.getState() != TaskState.DEFERRED) {
                total++;
                if(task.getState() == TaskState.DONE){
                    done++;
                }
            }
        }
        
        return Pair.create(done, total);
    }

    public Pair<Integer, Integer> getCountOfDoneAndAllTasks(Iteration iteration) {
        Pair<Integer, Integer> noStory = getCounOfDoneAndAllNonDeffered(Task.class,
                TaskState.DONE, Arrays.asList("iteration"), iteration);
        Pair<Integer, Integer> inStory = getCounOfDoneAndAllNonDeffered(Task.class,
                TaskState.DONE, Arrays.asList("story", "iteration"), iteration);
        return Pair.create(noStory.first + inStory.first, noStory.second
                + inStory.second);
    }
    
    public Pair<Integer, Integer> getCountOfDoneAndNonDeferred(Iteration iteration) {
        Pair<Integer, Integer> tasks = getCountOfDoneAndAllNonDeferredTasks(iteration);
        return tasks;
    }
    
    public Pair<Integer, Integer> getCountOfDoneAndAllStories(
            Iteration iteration) {
        return getCounOfDoneAndAllNonDeffered(Story.class, StoryState.DONE, Arrays
                .asList("iteration"), iteration);
    }

    public Map<Integer, Integer> getTotalAvailability(Set<Integer> iterationIds) {
        if (iterationIds == null || iterationIds.size() == 0) {
            return Collections.emptyMap();
        }
        Criteria crit = this.createCriteria(Iteration.class);
        crit.add(Restrictions.in("id", iterationIds));
        crit.createAlias("assignments", "assignments");
        crit.setProjection(Projections.projectionList().add(
                Projections.groupProperty("id")).add(
                Projections.sum("assignments.availability")));
        List<Object[]> data = asList(crit);
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        for (Object[] row : data) {
            result.put((Integer) row[0], ((Long) row[1]).intValue());
        }
        return result;
    }

    public List<Iteration> retrieveEmptyIterationsWithPlannedSize(
            DateTime startDate, DateTime endDate, User assignee) {
        Criteria crit = this.createCriteria(Iteration.class);

        // interval limitations
        Criterion startDateLimit = Restrictions.between("startDate", startDate,
                endDate);
        // iteration end during the interval
        Criterion endDateLimit = Restrictions.between("endDate", startDate,
                endDate);
        // interval may be within the iteration
        Criterion overlaps = Restrictions.or(startDateLimit, endDateLimit);
        Criterion withinIteration = Restrictions.and(Restrictions.le(
                "startDate", startDate), Restrictions.ge("endDate", endDate));
        crit.add(Restrictions.or(overlaps, withinIteration));

        // limit by assignee
        crit.createCriteria("assignments").createCriteria("user").add(
                Restrictions.eq("id", assignee.getId()));

        // must have planned size set
        crit.add(Restrictions.isNotNull("backlogSize"));

        // must be empty
        crit.add(Restrictions.isEmpty("assignedStories"));
        crit.add(Restrictions.isEmpty("tasks"));

        return asList(crit);
    }

    public List<Iteration> retrieveCurrentAndFutureIterationsAt(DateTime point) {

        Criteria crit = this.createCriteria(Iteration.class);
        crit.add(Restrictions.ge("endDate", point));
        return asList(crit);
    }
    
    public List<Iteration> retrieveAllStandAloneIterations() {
        final Criteria crit = this.createCriteria(Iteration.class);
        crit.add(Restrictions.isNull("parent"));
        crit.addOrder(Order.asc("name"));
        return asList(crit);
    }
    
    public Iteration retrieveDeep(int iterationId) {
        Criteria crit = this.createCriteria(Iteration.class);

        //doesn't work: will cause the same task responsible to be inserted too many times
        //Criteria iterationTasksCrit = crit.createCriteria("tasks", CriteriaSpecification.LEFT_JOIN);

        //iterationTasksCrit.setFetchMode("responsibles", FetchMode.JOIN);
        //iterationTasksCrit.setFetchMode("whatsNextEntries", FetchMode.JOIN);

        Criteria storiesCrit = crit.createCriteria("stories", CriteriaSpecification.LEFT_JOIN);

        storiesCrit.setFetchMode("labels", FetchMode.JOIN);

        Criteria storyTasksCrit = storiesCrit.createCriteria("tasks", CriteriaSpecification.LEFT_JOIN);

        storyTasksCrit.setFetchMode("responsibles", FetchMode.JOIN);
        storyTasksCrit.setFetchMode("whatsNextEntries", FetchMode.JOIN);

        crit.add(Restrictions.idEq(iterationId));

        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return this.uniqueResult(crit);
    }

    public Map<Integer, StoryMetrics> calculateIterationDirectStoryMetrics(
            Iteration iteration) {
        
        Criteria taskMetrics = this.createCriteria(
                Task.class);
        taskMetrics.add(Restrictions.ne("state", TaskState.DEFERRED));
        taskMetrics.createCriteria("story", "story").add(
                Restrictions.eq("iteration", iteration));
        ProjectionList taskSums = Projections.projectionList();
        taskSums.add(Projections.groupProperty("story"));
        taskSums.add(Projections.sum("effortLeft"));
        taskSums.add(Projections.sum("originalEstimate"));
        taskMetrics.setProjection(taskSums);

        Criteria storySpentEffort = this.createCriteria(
                StoryHourEntry.class);
        storySpentEffort.createCriteria("story", "story").add(
                Restrictions.eq("iteration", iteration));
        ProjectionList storySpentEffortSums = Projections.projectionList();
        storySpentEffortSums.add(Projections.groupProperty("story"));
        storySpentEffortSums.add(Projections.sum("minutesSpent"));
        storySpentEffort.setProjection(storySpentEffortSums);

        Criteria taskSpentEffort = this.createCriteria(
                TaskHourEntry.class);
        taskSpentEffort.createCriteria("task", "task").createCriteria("story",
                "story").add(Restrictions.eq("iteration", iteration));
        ProjectionList taskSpentEffortSums = Projections.projectionList();
        taskSpentEffortSums.add(Projections.groupProperty("task.story"));
        taskSpentEffortSums.add(Projections.sum("minutesSpent"));
        taskSpentEffort.setProjection(taskSpentEffortSums);
        
        List<Object[]> taskData = asList(taskMetrics);
        List<Object[]> storySpentEffortData = asList(storySpentEffort);
        List<Object[]> taskSpentEffortData = asList(taskSpentEffort);      
        
        Map<Integer, StoryMetrics> result = new HashMap<Integer, StoryMetrics>();
        
        for(Object[] row : taskData) {
            Story story = (Story)row[0];
//            ExactEstimate el = (ExactEstimate)row[1];
//            ExactEstimate oe = (ExactEstimate)row[2];
            result.put(story.getId(), new StoryMetrics());
            if(row[1] != null)
                result.get(story.getId()).setEffortLeft((Long)row[1]);
            if(row[2] != null)
                result.get(story.getId()).setOriginalEstimate((Long)row[2]);
        }
        
        for (Object[] row : storySpentEffortData) {
            Story story = (Story) row[0];
            if (!result.containsKey(story.getId())) {
                result.put(story.getId(), new StoryMetrics());
            }
            result.get(story.getId()).setEffortSpent((Long) row[1]);
        }

        for (Object[] row : taskSpentEffortData) {
            Story story = (Story) row[0];
            if (!result.containsKey(story.getId())) {
                result.put(story.getId(), new StoryMetrics());
            }
            result.get(story.getId()).setEffortSpent(
                    (Long) row[1] + result.get(story.getId()).getEffortSpent());
        }
        return result;
    }
    
    public Map<Integer, Long> calculateIterationTaskEffortSpent(Iteration iteration) {
        Criteria crit = this.createCriteria(TaskHourEntry.class);
        Criteria taskCrit = crit.createCriteria("task");
        taskCrit.createAlias("story", "story", CriteriaSpecification.LEFT_JOIN);
        taskCrit.add(Restrictions.or(Restrictions.eq("iteration", iteration), Restrictions.eq("story.iteration", iteration)));
        ProjectionList sumsProj = Projections.projectionList();
        sumsProj.add(Projections.groupProperty("task"));
        sumsProj.add(Projections.sum("minutesSpent"));
        crit.setProjection(sumsProj);
        List<Object[]> data = asList(crit);
        
        Map<Integer, Long> ret = new HashMap<Integer, Long>();
        for(Object[] row : data) {
            Task task = (Task)row[0];
            ret.put(task.getId(), (Long)row[1]);
        }
        return ret;
    }

    public List<Iteration> retrieveActiveWithUserAssigned(int userId) {
        Criteria crit = this.createCriteria(Iteration.class);
        crit.setFetchMode("parent", FetchMode.JOIN);
        crit.add(Restrictions.gt("endDate", new DateTime()));
        crit = crit.createCriteria("assignments");
        crit = crit.createCriteria("user");
        crit.add(Restrictions.idEq(userId));
        return asList(crit);
    }
    
    /**
     * This function determines whether or not an iteration has an associated 
     * token for readonly access. 
     * 
     * @param iterationId
     * @return true if iteration has an associated readonly token, false otherwise
     */
    public boolean hasReadonlyToken(int iterationId) {
        Criteria crit = this.createCriteria(Iteration.class);
        crit.add(Restrictions.idEq(iterationId));
        crit.add(Restrictions.and(Restrictions.isNotNull("readonlyToken"), Restrictions.ne("readonlyToken", "")));
        
        return !asList(crit).isEmpty();
    }
    
    /**
     * This function determines whether a given readonly token is valid of not. 
     * A token is valid if it exists in the backlog db. 
     * 
     * @param token
     * @return true if the readonly token exists in the database, false otherwise.
     */
    public boolean isValidReadonlyToken(String token) {
        
        // Empty or null tokens can never be valid.
        if (token == null || token.equals("")) {
            return false;
        }
        
        Criteria crit = this.createCriteria(Iteration.class);
        crit.add(Restrictions.eq("readonlyToken", token));
        
        return !asList(crit).isEmpty();
    }
    
    /**
     * This function fetches the iteration id associated with a given token. 
     * 
     * @param token
     * @return if the token is valid the associated iteration id is returned, 
     *          if the token is not valid null is returned. 
     */
    public Iteration getIterationFromReadonlyToken(String token) {
        
        // First ensure token is valid.
        if (!isValidReadonlyToken(token)) {
            return null;
        }
        
        Criteria crit = this.createCriteria(Iteration.class);
        crit.add(Restrictions.eq("readonlyToken", token));
        List<Iteration> dummy = asList(crit);
        return dummy.get(0);
    }
    
    /**
     * This function fetches the count associated with a given token in case we need to
     * create another one to be unique
     * 
     * @param token
     * @return if the token is valid the associated iteration id is returned, 
     *          if the token is not valid null is returned. 
     */
    public int getIterationCountFromReadonlyToken(String token) {
        
        // First ensure token is valid.
        if (!isValidReadonlyToken(token)) {
            return 0;
        }
        
        Criteria crit = this.createCriteria(Iteration.class);
        crit.add(Restrictions.eq("readonlyToken", token));
        List<Iteration> dummy = asList(crit);
        return dummy.size();
    }
}
