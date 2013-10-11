package fi.hut.soberit.agilefant.business.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.AssignmentBusiness;
import fi.hut.soberit.agilefant.business.BacklogBusiness;
import fi.hut.soberit.agilefant.business.BacklogHistoryEntryBusiness;
import fi.hut.soberit.agilefant.business.HourEntryBusiness;
import fi.hut.soberit.agilefant.business.IterationBusiness;
import fi.hut.soberit.agilefant.business.IterationHistoryEntryBusiness;
import fi.hut.soberit.agilefant.business.StoryBusiness;
import fi.hut.soberit.agilefant.business.StoryRankBusiness;
import fi.hut.soberit.agilefant.business.TaskBusiness;
import fi.hut.soberit.agilefant.business.TeamBusiness;
import fi.hut.soberit.agilefant.business.TransferObjectBusiness;
import fi.hut.soberit.agilefant.db.IterationDAO;
import fi.hut.soberit.agilefant.db.IterationHistoryEntryDAO;
import fi.hut.soberit.agilefant.db.history.BacklogHistoryDAO;
import fi.hut.soberit.agilefant.db.history.StoryHistoryDAO;
import fi.hut.soberit.agilefant.db.history.TaskHistoryDAO;
import fi.hut.soberit.agilefant.exception.ObjectNotFoundException;
import fi.hut.soberit.agilefant.model.Assignment;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.ExactDoubleEstimate;
import fi.hut.soberit.agilefant.model.ExactEstimate;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.IterationHistoryEntry;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.SignedExactEstimate;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.Team;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.transfer.AgilefantHistoryEntry;
import fi.hut.soberit.agilefant.transfer.AssignmentTO;
import fi.hut.soberit.agilefant.transfer.IterationMetrics;
import fi.hut.soberit.agilefant.transfer.IterationTO;
import fi.hut.soberit.agilefant.transfer.StoryTO;
import fi.hut.soberit.agilefant.transfer.TaskTO;
import fi.hut.soberit.agilefant.util.HourEntryHandlingChoice;
import fi.hut.soberit.agilefant.util.Pair;
import fi.hut.soberit.agilefant.util.StoryMetrics;

@Service("iterationBusiness")
@Transactional
public class IterationBusinessImpl extends GenericBusinessImpl<Iteration>
        implements IterationBusiness {

    private IterationDAO iterationDAO;

    @Autowired
    private TransferObjectBusiness transferObjectBusiness;
    @Autowired
    private StoryBusiness storyBusiness;
    @Autowired
    private HourEntryBusiness hourEntryBusiness;
    @Autowired
    private BacklogHistoryEntryBusiness backlogHistoryEntryBusiness;
    @Autowired
    private IterationHistoryEntryBusiness iterationHistoryEntryBusiness;
    @Autowired
    private IterationHistoryEntryDAO iterationHistoryEntryDAO;
    @Autowired
    private BacklogBusiness backlogBusiness;
    @Autowired
    private AssignmentBusiness assignmentBusiness;
    @Autowired 
    private StoryRankBusiness storyRankBusiness;
    @Autowired
    private TaskBusiness taskBusiness;
    @Autowired
    private TeamBusiness teamBusiness;
    @Autowired
    private BacklogHistoryDAO backlogHistoryDAO;
    @Autowired
    private StoryHistoryDAO storyHistoryDAO;
    @Autowired
    private TaskHistoryDAO taskHistoryDAO;
    
    public IterationBusinessImpl() {
        super(Iteration.class);
    }
    
    @Autowired
    public void setIterationDAO(IterationDAO iterationDAO) {
        this.genericDAO = iterationDAO;
        this.iterationDAO = iterationDAO;
    }

    @Override
    public void delete(int id) {
        delete(retrieve(id));
    }
    
    
    public void deleteAndUpdateHistory(int id) {
        Iteration iteration = retrieve(id);
        Backlog project = iteration.getParent();
        delete(iteration);
        if (project != null) {
            backlogHistoryEntryBusiness.updateHistory(project.getId());      
        }
    }

    @Override
    public void delete(Iteration iteration) {

        storyRankBusiness.removeBacklogRanks(iteration);
        Boolean isStandalone = iteration.isStandAlone();
        
        Set<Task> tasks = new HashSet<Task>(iteration.getTasks());
        for (Task item : tasks) {
            taskBusiness.delete(item.getId(), HourEntryHandlingChoice.DELETE);
        }

        Set<Story> stories = new HashSet<Story>(iteration.getAssignedStories());
        for (Story item : stories) {
            storyBusiness.forceDelete(item);
            /* In case of normal iteration, story is removed from iteration and also from it's parent project.*/
            if (!isStandalone && iteration.getParent().getStories().contains(item))
                iteration.getParent().getStories().remove(item);
        }
        Set<Assignment> assignments = new HashSet<Assignment>(iteration
                .getAssignments());
        for (Assignment item : assignments) {
            assignmentBusiness.delete(item.getId());
        }

        hourEntryBusiness.deleteAll(iteration.getHourEntries());

        iteration.getHourEntries().clear();

        Set<IterationHistoryEntry> historyEntries = new HashSet<IterationHistoryEntry>(
                iteration.getHistoryEntries());
        for (IterationHistoryEntry item : historyEntries) {
            iterationHistoryEntryBusiness.delete(item.getId());
        }
        super.delete(iteration);
    }
    

    @Transactional
    public IterationTO getIterationContents(int iterationId) {
        Iteration iteration = this.iterationDAO.retrieveDeep(iterationId);
        if (iteration == null) {
            throw new ObjectNotFoundException("Iteration not found");
        }
        IterationTO iterationTO = transferObjectBusiness.constructIterationTO(iteration);

        List<Story> stories = this.storyBusiness.retrieveStoriesInIteration(iteration);

        Map<Integer, StoryMetrics> metricsData = this.iterationDAO.calculateIterationDirectStoryMetrics(iteration);
        int rank = 0;
        List<StoryTO> rankedStories = new ArrayList<StoryTO>();

        for (Story story : stories) {
            StoryTO storyTO = new StoryTO(story);
            if (metricsData.containsKey(story.getId())) {
                storyTO.setMetrics(metricsData.get(story.getId()));
            }
            storyTO.setRank(rank++);
            rankedStories.add(storyTO);
        }
        iterationTO.setRankedStories(rankedStories);
        
        // Set the tasks without a story
        Collection<Task> tasksWithoutStory = iteration.getTasks();

        iterationTO.setTasks(new HashSet<Task>());

        Map<Integer, Long> taskEsData = this.iterationDAO
                .calculateIterationTaskEffortSpent(iteration);

        for (Task task : tasksWithoutStory) {
            TaskTO taskTO = new TaskTO(task);
            iterationTO.getTasks().add(taskTO);
            if (taskEsData.containsKey(taskTO.getId())) {
                taskTO.setEffortSpent(taskEsData.get(taskTO.getId()));
            }
        }

        for (Story story : iterationTO.getRankedStories()) {
            Set<Task> tasks = new HashSet<Task>();
            for (Task task : story.getTasks()) {
                TaskTO taskTO = new TaskTO(task);
                if (taskEsData.containsKey(taskTO.getId())) {
                    taskTO.setEffortSpent(taskEsData.get(taskTO.getId()));
                }
                tasks.add(taskTO);
            }
            story.setTasks(tasks);
        }

        return iterationTO;
    }

    public IterationTO retrieveIterationOnlyLeafStories(int iterationId) {
        IterationTO iteration = this.getIterationContents(iterationId);
        iteration.setStories(null);
        return iteration;
    }
    
    public ExactEstimate calculateDailyVelocity(LocalDate start, LocalDate end, IterationHistoryEntry yesterdayEntry) {
        final LocalDate today = new LocalDate();

        if (yesterdayEntry == null)
            return new ExactEstimate(0);
        final LocalDate day = (today.isBefore(end)) ? today : end;

        double length = Days.daysBetween(start, day).getDays();
        if (length < 1) {
            length = 1;
        }

        final long origEst = yesterdayEntry.getOriginalEstimateSum();
        final long effLeft = yesterdayEntry.getEffortLeftSum();

        final double velocity = (origEst - effLeft) / length;

        return new ExactEstimate((long) velocity);
    }

    public ExactEstimate calculateDailyVelocity(Iteration iteration) {
        final LocalDate today = new LocalDate();
        final IterationHistoryEntry entry = iterationHistoryEntryDAO.retrieveByDate(iteration.getId(), today.minusDays(1));
        return calculateDailyVelocity(new LocalDate(iteration.getStartDate()), new LocalDate(iteration.getEndDate()), entry);
    }
    
    public ExactDoubleEstimate calculateDailyStoryPointsVelocity(LocalDate start, LocalDate end, int points) {
        final LocalDate today = new LocalDate();

        if (points == 0)
            return new ExactDoubleEstimate(0);
        final LocalDate day = (today.isBefore(end)) ? today : end;

        double length = Days.daysBetween(start, day).getDays();
        if (length < 1) {
            length = 1;
        }

        final double velocity = points / length;

        return new ExactDoubleEstimate(velocity);
    }

    private Integer calculatePercent(Integer part, Integer total) {
        if(total == 0) {
            return 0;
        }
        return Math.round(100.0f * part / total);
    }

    public IterationMetrics getIterationMetrics(Iteration iteration) {
        if (iteration == null) {
            throw new IllegalArgumentException("Iteration must be not null.");
        }

        IterationMetrics metrics = new IterationMetrics();

        IterationHistoryEntry latestHistoryEntry = iterationHistoryEntryBusiness
                .retrieveLatest(iteration);

        // 1. Set original estimate and effort left
        if (latestHistoryEntry == null) {
            metrics.setOriginalEstimate(new ExactEstimate(0));
            metrics.setEffortLeft(new ExactEstimate(0));
        } else {
            metrics.setOriginalEstimate(new ExactEstimate(latestHistoryEntry
                    .getOriginalEstimateSum()));
            metrics.setEffortLeft(new ExactEstimate(latestHistoryEntry
                    .getEffortLeftSum()));
        }
        
        // Set the planned size
        if(iteration.getBacklogSize() == null)
            metrics.setPlannedSize(new ExactEstimate(0));
        else
            metrics.setPlannedSize(new ExactEstimate(iteration.getBacklogSize().intValue()));

        int doneStoryPoints = backlogBusiness.calculateDoneStoryPointSum(iteration.getId());
        metrics.setDailyVelocity(calculateDailyStoryPointsVelocity(new LocalDate(iteration.getStartDate()), new LocalDate(iteration.getEndDate()), doneStoryPoints));

        // 2. Set story points
        metrics.setStoryPoints(backlogBusiness.getStoryPointSumByIteration(iteration));
        metrics.setDoneStoryPoints(doneStoryPoints);

        // 3. Set spent effort
        long spentEffort = hourEntryBusiness
                .calculateSumOfIterationsHourEntries(iteration);
        metrics.setSpentEffort(new ExactEstimate(spentEffort));

        // 3. Tasks done and Total
        Pair<Integer, Integer> pairTasks = iterationDAO
                .getCountOfDoneAndNonDeferred(iteration);
        metrics.setTotalTasks(pairTasks.second);
        metrics.setCompletedTasks(pairTasks.first);
        

        Pair<Integer, Integer>  pairStories = iterationDAO.getCountOfDoneAndAllStories(iteration);
        metrics.setTotalStories(pairStories.second);
        metrics.setCompletedStories(pairStories.first);

        //4. iteration interval
        LocalDate today = new LocalDate();
        
        metrics.setTotalDays(Days.daysBetween(iteration.getStartDate(), iteration.getEndDate()).getDays());
        if (today.isBefore(iteration.getEndDate().toLocalDate())) {
            metrics.setDaysLeft(backlogBusiness.daysLeftInSchedulableBacklog(iteration).getDays());
        }
        
        //5. variance
        metrics.setVariance(calculateVariance(iteration));
        
        //6. calculate percentages
        metrics.setPercentDoneTasks(calculatePercent(pairTasks.first, pairTasks.second));
        metrics.setPercentDoneStories(calculatePercent(pairStories.first, pairStories.second));
        metrics.setPercentSpentEffort(calculatePercent(metrics.getSpentEffort().intValue(), metrics.getPlannedSize().intValue()));
        if(metrics.getPercentSpentEffort() > 100)
            metrics.setPercentSpentEffort(100);
        metrics.setDoneStoryPointsPercentage(calculatePercent(metrics.getDoneStoryPoints(), metrics.getStoryPoints()));
        metrics.setDaysLeftPercentage(calculatePercent(metrics.getDaysLeft(), metrics.getTotalDays()));
        if(metrics.getEffortLeft() != null && metrics.getOriginalEstimate() != null) {
            int effortDone = metrics.getOriginalEstimate().intValue() - metrics.getEffortLeft().intValue();
            metrics.setCompletedEffortPercentage(calculatePercent(effortDone, metrics.getOriginalEstimate().intValue()));
        }
        
        //7. calculate 'Value'
        metrics.setTotalValue(backlogBusiness.getStoryValueSumByIteration(iteration));
        metrics.setCompletedValue(backlogBusiness.getCompletedStoryValueSumByIteration(iteration));
        metrics.setPercentCompletedValue(calculatePercent(metrics.getCompletedValue(), metrics.getTotalValue()));
        return metrics;
    }

    
    public IterationTO storeStandAlone(int iterationId, Iteration iterationData, Set<Integer> assigneeIds, Set<Integer> teams) {
        final int emptyParentId = 0;
        return store(iterationId, emptyParentId, iterationData, assigneeIds, teams);
    }
    
    
    public IterationTO store(int iterationId, int parentBacklogId, Iteration iterationData, Set<Integer> assigneeIds, Set<Integer> teamIds) {
        Backlog parent = null;
        if(parentBacklogId != 0) {
            parent = this.backlogBusiness.retrieve(parentBacklogId);
        }
        if (parent instanceof Iteration) {
            throw new IllegalArgumentException(
                    "Nested iterations are not allowed.");
        }
        if (iterationData.getEndDate().isBefore(iterationData.getStartDate())) {
            throw new IllegalArgumentException("End date before start date");
        }
        if (iterationId == 0) {
            return transferObjectBusiness.constructIterationTO(this.create(parent, iterationData, assigneeIds, teamIds));
        }
 
        Iteration iter = this.retrieve(iterationId);
        
        // Get teams
        Set<Team> teams = new HashSet<Team>();
        if (teamIds != null) {
            for (Integer tid : teamIds) {
                teams.add(teamBusiness.retrieve(tid));
            }
            iter.setTeams(teams);
        }
        
        iter.setStartDate(iterationData.getStartDate());
        iter.setEndDate(iterationData.getEndDate());
        iter.setBacklogSize(iterationData.getBacklogSize());
        iter.setBaselineLoad(iterationData.getBaselineLoad());
        iter.setDescription(iterationData.getDescription());
        iter.setName(iterationData.getName());
        iter.setReadonlyToken(iterationData.getReadonlyToken());
        setAssignees(iter, assigneeIds);
        this.iterationDAO.store(iter);
        if (parent != null && iter.getParent() != parent) {
            this.moveTo(iter, parent);
        }
        return transferObjectBusiness.constructIterationTO(iter);
    }

    private Iteration create(Backlog parentBacklog, Iteration iterationData, Set<Integer> assigneeIds, Set<Integer> teamIds) {
        if (parentBacklog != null) {
            iterationData.setParent(parentBacklog);
        }
        int iterationId = (Integer) this.iterationDAO.create(iterationData);
        Iteration iter = this.retrieve(iterationId);
        
        setAssignees(iter, assigneeIds);
        
        //only set teams for standalone iterations
        if(parentBacklog == null)
            setTeams(iter, teamIds);
        
        return iter;
    }
    
    private void setAssignees(Iteration iteration, Set<Integer> assigneeIds) {
        if (assigneeIds != null) {
            for(Assignment assignment : iteration.getAssignments()) {
                if(!assigneeIds.contains(assignment.getUser().getId())) {
                    assignmentBusiness.delete(assignment.getId());
                }
            }
            assignmentBusiness.addMultiple(iteration, assigneeIds);
        }
    }

    private void setTeams(Iteration iteration, Set<Integer> teamIds) {
        // Get teams
        Set<Team> teams = new HashSet<Team>();
        if (teamIds != null) {
            for (Integer tid : teamIds) {
                teams.add(teamBusiness.retrieve(tid));
            }
            iteration.setTeams(teams);
        }
    }
    public void moveTo(Iteration iter, Backlog parent) {
        Backlog oldParent = iter.getParent();
        iter.setParent(parent);
        this.iterationDAO.store(iter);
        if (oldParent instanceof Project) {
            this.backlogHistoryEntryBusiness.updateHistory(oldParent.getId());
        }
        if (parent instanceof Project) {
            this.backlogHistoryEntryBusiness.updateHistory(parent.getId());
        }
    }

    public Collection<Iteration> retrieveCurrentAndFutureIterations() {
        DateTime now = new DateTime();
        DateTime dayStart = now.withMillisOfDay(0);

        return iterationDAO.retrieveCurrentAndFutureIterationsAt(dayStart);
    }
    
    
    public Collection<Iteration> retrieveAllStandAloneIterations() {
        return iterationDAO.retrieveAllStandAloneIterations();
    }
    
    public Set<AssignmentTO> calculateAssignedLoadPerAssignee(Iteration iter) {
        List<Task> iterationTasks = this.iterationDAO
                .getAllTasksForIteration(iter);

        Map<Integer, AssignmentTO> assignments = new HashMap<Integer, AssignmentTO>();
        long unassignedLoad = 0L;
        int totalAvailability = 0;

        for (Assignment assignment : iter.getAssignments()) {
            AssignmentTO to = new AssignmentTO(assignment);
            assignments.put(assignment.getUser().getId(), to);
            totalAvailability += assignment.getAvailability();
        }
        if (totalAvailability == 0) {
            totalAvailability = 1;
        }

        for (Task task : iterationTasks) {
            int responsibleCount = task.getResponsibles().size();
            long taskEffort = (task.getEffortLeft() != null) ? task
                    .getEffortLeft().longValue() : 0;
            if (responsibleCount != 0) {
                Collection<User> responsibles = task.getResponsibles();
                divideTaskLoad(assignments, responsibleCount, taskEffort,
                        responsibles);
            } else if (task.getStory() != null
                    && !task.getStory().getResponsibles().isEmpty()) {
                responsibleCount = task.getStory().getResponsibles().size();
                Collection<User> responsibles = task.getStory()
                        .getResponsibles();
                divideTaskLoad(assignments, responsibleCount, taskEffort,
                        responsibles);

            } else {
                unassignedLoad += taskEffort;
            }
        }
        float timeframeLeft = backlogBusiness
                .calculateBacklogTimeframePercentageLeft(iter);
        float weeksLeft = (float) backlogBusiness.daysLeftInSchedulableBacklog(iter).getDays() / 7.0f;
        long iterationBaselineLoad = 0l;
        if (iter.getBaselineLoad() != null) {
            iterationBaselineLoad = (long) (iter.getBaselineLoad().floatValue() * weeksLeft);
        }
        for (AssignmentTO assignment : assignments.values()) {
            float assignedPortion = (float) assignment.getAvailability()
                    / (float) totalAvailability;
            assignment.getBaselineLoad().add(iterationBaselineLoad);
            assignment.setUnassignedLoad(new ExactEstimate(
                    (long) (assignedPortion * (float) unassignedLoad)));

            ExactEstimate iterationWorkHours;
            if(iter.getBacklogSize() != null) {
                iterationWorkHours = new ExactEstimate(Math
                        .round(timeframeLeft * iter.getBacklogSize().floatValue()
                                * assignedPortion));
            } else {
                iterationWorkHours = new ExactEstimate(0);
            }
            assignment.setAvailableWorktime(iterationWorkHours);
            SignedExactEstimate totalLoad = new SignedExactEstimate(0);
            totalLoad.add(assignment.getUnassignedLoad().longValue());
            totalLoad.add(assignment.getAssignedLoad().longValue());
            if (assignment.getPersonalLoad() != null) {
                assignment.getBaselineLoad().add(
                        (long) (weeksLeft * assignment.getPersonalLoad()
                                .floatValue()));
            }
            if(assignment.getBaselineLoad() != null) {
                totalLoad.add(assignment.getBaselineLoad().longValue());
            }
            assignment.setTotalLoad(totalLoad);
            if (iterationWorkHours.longValue() > 0l) {
                assignment.setLoadPercentage(Math.round(100f
                        * assignment.getTotalLoad().floatValue()
                        / assignment.getAvailableWorktime().floatValue()));
            }
        }
        return new HashSet<AssignmentTO>(assignments.values());
    }

    private void divideTaskLoad(Map<Integer, AssignmentTO> assignments,
            int responsibleCount, long taskEffort, Collection<User> responsibles) {
        for (User user : responsibles) {
            AssignmentTO assignment = assignments.get(user.getId());
            if (assignment == null) {
                assignment = new AssignmentTO(new Assignment());
                assignment.setAvailability(0);
                assignment.setId(-1 * user.getId()); // user negative and
                                                     // context unique id for
                                                     // non-persited assignment
                assignment.setUser(user);
                assignment.setUnassigned(true);
                assignments.put(user.getId(), assignment);
            }
            assignment.getAssignedLoad().add(taskEffort / responsibleCount);
        }
    }
    
    public Integer calculateVariance(Iteration iter) {
        IterationHistoryEntry latestHistoryEntry = iterationHistoryEntryBusiness
        .retrieveLatest(iter);
        if(latestHistoryEntry == null) {
            return null;
        }
        long effortLeft = latestHistoryEntry.getEffortLeftSum();
        long dailyVelocity = calculateDailyVelocity(iter).longValue();
        if(dailyVelocity != 0 && iter.getStartDate().isBeforeNow() && effortLeft != 0) {
            int daysLeft = (int) (effortLeft / dailyVelocity);
            return daysLeft - backlogBusiness.daysLeftInSchedulableBacklog(iter).getDays(); 
        } else {
            return null;
        }      
    }
    
    @SuppressWarnings("unchecked")
    public List<AgilefantHistoryEntry> retrieveChangesInIterationStories(
            Iteration iteration) {
        List<AgilefantHistoryEntry> added = this.backlogHistoryDAO
                .retrieveAddedStories(iteration);
        List<AgilefantHistoryEntry> deleted = this.backlogHistoryDAO
                .retrieveDeletedStories(iteration);
        for (AgilefantHistoryEntry entry : deleted) {
            Story story = this.storyHistoryDAO.retrieveClosestRevision(entry
                    .getObjectId(), entry.getRevision().getId());
            entry.setObject(story);
        }
        
        List<AgilefantHistoryEntry> modified = this.backlogHistoryDAO
                .retrieveModifiedStories(iteration);
        for (AgilefantHistoryEntry entry : modified) {
            Story story = this.storyHistoryDAO.retrieveClosestRevision(entry
                    .getObjectId(), entry.getRevision().getId());
            entry.setObject(story);
        }
        
        List<AgilefantHistoryEntry> ret = new ArrayList<AgilefantHistoryEntry>();
        ret.addAll(added);
        ret.addAll(deleted);
        ret.addAll(modified);
        
        Collections.sort(ret, new PropertyComparator("revision.timestamp",
                true, false));
        return ret;
    }

    /**
     * This is to retrieve task history given an iteration, for all possible
     * task modifications
     * 
     * Task revisions are stored in table 'tasks_AUD' given the current
     * iteration (iteration_id). Note that if story_id is NULL, the selected
     * task does not belong to any story.     
     *       
     * Interface needed: TaskHistoryDAO in db.history package
     *  --> add function retrieveAllTaskRevisions(iteration)
     * Implementation: TaskHistoryDAOImpl in db.history.impl package
     *  --> implement the added function. --> Note that this is the same
     *  as BacklogHistoryDAO, except that it uses the Iteration class instead
     *  of the Backlog class.
     * 
     * @author aborici
     * 
     */
    @SuppressWarnings("unchecked")
    public List<AgilefantHistoryEntry> retrieveChangesInIterationTasks(
            Iteration iteration) {
        
        List<AgilefantHistoryEntry> allTasks = this.taskHistoryDAO
                .retrieveAllTaskRevisions(iteration);
       
        List<AgilefantHistoryEntry> ret = new ArrayList<AgilefantHistoryEntry>();
        ret.addAll(allTasks);
        
        Collections.sort(ret, new PropertyComparator("revision.timestamp",
                true, false));
        
        return ret;
    }
    
    /**
     * This is to mingle task and story revisions by sorting them based on
     * timestamp.
     * 
     * @author aborici
     * 
     */
    @SuppressWarnings("unchecked")
    public List<AgilefantHistoryEntry> renderSortedTaskAndStoryRevisions (
            Iteration iteration) {

        // merge tasks and stories into list 'ret':
        List<AgilefantHistoryEntry> ret = new ArrayList<AgilefantHistoryEntry>();
        ret.addAll(this.retrieveChangesInIterationStories(iteration));
        ret.addAll(this.retrieveChangesInIterationTasks(iteration));
        
        Collections.sort(ret, new PropertyComparator("revision.timestamp",
                true, false));

        return ret;
    }
    
    public Iteration retreiveIterationByReadonlyToken(String readonlyToken) {
        Iteration iteration = iterationDAO.getIterationFromReadonlyToken(readonlyToken);
        return iteration;
    }
    
    public int getIterationCountFromReadonlyToken(String readonlyToken) {
        int count = iterationDAO.getIterationCountFromReadonlyToken(readonlyToken);
        return count;
    }
}
