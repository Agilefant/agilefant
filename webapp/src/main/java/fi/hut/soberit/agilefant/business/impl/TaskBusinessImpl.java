package fi.hut.soberit.agilefant.business.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.DailyWorkBusiness;
import fi.hut.soberit.agilefant.business.HourEntryBusiness;
import fi.hut.soberit.agilefant.business.IterationBusiness;
import fi.hut.soberit.agilefant.business.IterationHistoryEntryBusiness;
import fi.hut.soberit.agilefant.business.RankUnderDelegate;
import fi.hut.soberit.agilefant.business.RankingBusiness;
import fi.hut.soberit.agilefant.business.StoryBusiness;
import fi.hut.soberit.agilefant.business.TaskBusiness;
import fi.hut.soberit.agilefant.business.TaskCommentBusiness;
import fi.hut.soberit.agilefant.db.TaskDAO;
import fi.hut.soberit.agilefant.exception.ObjectNotFoundException;
import fi.hut.soberit.agilefant.exception.OperationNotPermittedException;
import fi.hut.soberit.agilefant.model.ExactEstimate;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Rankable;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryState;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.TaskState;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.util.HourEntryHandlingChoice;

@Service("taskBusiness")
@Transactional
public class TaskBusinessImpl extends GenericBusinessImpl<Task> implements
        TaskBusiness {

    @Autowired
    private IterationBusiness iterationBusiness;

    @Autowired
    private StoryBusiness storyBusiness;

    @Autowired
    private IterationHistoryEntryBusiness iterationHistoryEntryBusiness;

    @Autowired
    private DailyWorkBusiness dailyWorkBusiness;

    @Autowired
    private RankingBusiness rankingBusiness;

    @Autowired
    private HourEntryBusiness hourEntryBusiness;
    
    @Autowired
    private TaskCommentBusiness taskCommentBusiness;


    private TaskDAO taskDAO;

    public TaskBusinessImpl() {
        super(Task.class);
    }

    @Autowired
    public void setTaskDAO(TaskDAO taskDAO) {
        this.genericDAO = taskDAO;
        this.taskDAO = taskDAO;
    }

    /** {@inheritDoc} */
    public Task storeTask(Task task, Integer iterationId, Integer storyId, boolean storyToStarted) {
        Task storedTask = null;
        TaskState currentTaskState = null;

        if (task == null) {
            throw new IllegalArgumentException("Task should be given");
        }

        if(task.getId() != 0) {
            currentTaskState = this.retrieveDetached(task.getId()).getState();
        }
        
        // allow storing existing task without relations
        if (task.getId() == 0 || iterationId != null || storyId != null) {
            assignParentForTask(task, iterationId, storyId);
        }

        updateEffortLeftAndOriginalEstimate(task);

        // populateUserData(task, userIds);

        if (task.getId() == 0) {
            int newTaskId = this.create(task);
            storedTask = this.retrieve(newTaskId);
            this.rankToBottom(storedTask, storyId, iterationId);
        } else {
            this.store(task);
            storedTask = task;
            if (iterationId != null || storyId != null) {
                this.rankToBottom(task, storyId, iterationId);
            }
        }
    
        Story parent = task.getStory();
        Story grandFather=null;
        if(currentTaskState == TaskState.NOT_STARTED && task.getState() != TaskState.NOT_STARTED) {
            if (storyToStarted && parent != null && parent.getState() == StoryState.NOT_STARTED) {
                parent.setState(StoryState.STARTED);
                grandFather=parent.getParent();
                while (grandFather != null && grandFather.getState() == StoryState.NOT_STARTED){
                    grandFather.setState(StoryState.STARTED);
                    grandFather=grandFather.getParent();
                }
               
            }
        }

        updateIterationHistoryIfApplicable(task);

        if (task.getState() == TaskState.DONE) {
            dailyWorkBusiness.removeTaskFromWorkQueues(task);
        }

        return storedTask;
    }
        
    public void setTaskToDone(Task task) {
        task.setState(TaskState.DONE);
        updateEffortLeftAndOriginalEstimate(task);
        dailyWorkBusiness.removeTaskFromWorkQueues(task);
        this.store(task);
    }

    private void updateIterationHistoryIfApplicable(Task task) {
        Integer iterationId = getTaskIterationId(task);
        updateIterationHistoryIfNotNull(iterationId);
    }

    private void updateEffortLeftAndOriginalEstimate(Task task) {
        if (task.getEffortLeft() == null && task.getOriginalEstimate() != null) {
            task.setEffortLeft(task.getOriginalEstimate());
        }

        if (task.getOriginalEstimate() == null && task.getEffortLeft() != null) {
            task.setOriginalEstimate(task.getEffortLeft());
        }

        if (task.getState() == TaskState.DONE) {
            task.setEffortLeft(new ExactEstimate(0));
        }
    }

    /** {@inheritDoc} */
    @Transactional
    public void assignParentForTask(Task task, Integer iterationId,
            Integer storyId) throws IllegalArgumentException,
            ObjectNotFoundException {
        // 1. Error handling
        checkArgumentsForMoving(task, iterationId, storyId);

        // 2. The logic
        if (iterationId != null) {
            task.setIteration(iterationBusiness.retrieve(iterationId));
            task.setStory(null);
        } else {
            task.setStory(storyBusiness.retrieve(storyId));
            task.setIteration(null);
        }
    }

    private void checkArgumentsForMoving(Task task, Integer iterationId,
            Integer storyId) {
        if (task == null) {
            throw new IllegalArgumentException("Task should be given.");
        } else if (iterationId == null && storyId == null) {
            throw new IllegalArgumentException("The parent id should be given");
        } else if (iterationId != null && storyId != null) {
            throw new IllegalArgumentException("Only one parent can be given");
        }
    }

    /** {@inheritDoc} */
    @Transactional
    public Task move(Task task, Integer iterationId, Integer storyId) {
        checkArgumentsForMoving(task, iterationId, storyId);

        Integer sourceIterationId = getTaskIterationId(task);

        assignParentForTask(task, iterationId, storyId);
        this.store(task);

        Integer destinationIterationId = getTaskIterationId(task);

        if (sourceIterationId != destinationIterationId) {
            updateIterationHistoryIfNotNull(sourceIterationId);
            updateIterationHistoryIfNotNull(destinationIterationId);
        }

        return task;
    }

    private void updateIterationHistoryIfNotNull(Integer iterationId) {
        if (iterationId == null) {
            return;
        }
        iterationHistoryEntryBusiness.updateIterationHistory(iterationId);
    }

    /**
     * Gets the tasks parent iteration's id.
     * <p>
     * If task resides under a story, get the story's parent iteration id.
     * 
     * If story's parent backlog is not and iteration, return null.
     */
    private Integer getTaskIterationId(Task task) {
        Integer iterationId = null;
        Iteration tasksIteration = task.getIteration();
        if (tasksIteration != null) {
            iterationId = tasksIteration.getId();
        } else if (task.getStory().getIteration() != null) {
            iterationId = task.getStory().getIteration().getId();
        }
        return iterationId;
    }

    public Task resetOriginalEstimate(int taskId) {
        Task task = retrieve(taskId);
        task.setEffortLeft(null);
        task.setOriginalEstimate(null);
        taskDAO.store(task);

        updateIterationHistoryIfApplicable(task);

        return task;
    }

    @Override
    public void delete(int id) {
        delete(retrieve(id), null);
    }
    
    @Override
    public void delete(Task task) {
        delete(task, null);
    }

    public void delete(int id, HourEntryHandlingChoice hourEntryHandlingChoice) {
        delete(retrieve(id), hourEntryHandlingChoice);
    }

    public void deleteAndUpdateHistory(int id,
            HourEntryHandlingChoice hourEntryHandlingChoice) {
        Task task = retrieve(id);
        delete(task, hourEntryHandlingChoice);
        if (task.getIteration() != null) {
            iterationHistoryEntryBusiness.updateIterationHistory(task
                    .getIteration().getId());
        } else if (task.getStory().getIteration() != null) {
            iterationHistoryEntryBusiness.updateIterationHistory(task
                    .getStory().getIteration().getId());
        }
    }

    public void delete(Task task,
            HourEntryHandlingChoice hourEntryHandlingChoice) {
        if (hourEntryHandlingChoice != null) {
            switch (hourEntryHandlingChoice) {
            case DELETE:
                hourEntryBusiness.deleteAll(task.getHourEntries());
                task.getHourEntries().clear();
                break;
            case MOVE:
                if (task.getStory() == null) {
                    hourEntryBusiness.moveToBacklog(task.getHourEntries(), task
                            .getIteration());
                } else {
                    hourEntryBusiness.moveToStory(task.getHourEntries(), task
                            .getStory());
                }
                task.getHourEntries().clear();
                break;
            }
        }
        if (task.getHourEntries().size() != 0) {
            throw new OperationNotPermittedException(
                    "Task contains spent effort entries.");
        }
        try{
        	taskCommentBusiness.deleteAttachmentsInTaskComments(task.getId());
        }catch (Exception e) {
			
		}
        taskDAO.remove(task.getId());
    }

    /* RANKING */
    /** {@inheritDoc} */
    @Transactional
    public Task rankUnderTask(final Task task, Task upperTask)
            throws IllegalArgumentException {
        if (task == null) {
            throw new IllegalArgumentException("Task should be given");
        } else if (upperTask != null) {
            if (task.getStory() != upperTask.getStory()) {
                throw new IllegalArgumentException(
                        "The tasks' parent's should be the same");
            } else if (task.getIteration() != upperTask.getIteration()) {
                throw new IllegalArgumentException(
                        "The tasks' parent's should be the same");
            }
        }

        rankingBusiness.rankUnder(task, upperTask, new RankUnderDelegate() {
            public Collection<? extends Rankable> getWithRankBetween(
                    Integer first, Integer second) {
                return taskDAO.getTasksWithRankBetween(first, second, task
                        .getIteration(), task.getStory());
            }
        });

        return task;
    }

    /** {@inheritDoc} */
    @Transactional
    public Task rankToBottom(Task task, Integer parentStoryId,
            Integer parentIterationId) throws IllegalArgumentException {
        Story story = null;
        Iteration iter = null;

        if (parentStoryId != null) {
            story = storyBusiness.retrieve(parentStoryId);
        } else if (parentIterationId != null) {
            iter = iterationBusiness.retrieve(parentIterationId);
        }

        return this.rankToBottom(task, story, iter);
    }

    private Task rankToBottom(Task task, Story story, Iteration iteration) {
        if (task == null || (story == null && iteration == null)) {
            throw new IllegalArgumentException();
        }
        Task lastInRank = null;

        if (story != null) {
            lastInRank = taskDAO.getLastTaskInRank(story, null);
        } else {
            lastInRank = taskDAO.getLastTaskInRank(null, iteration);
        }

        rankingBusiness.rankToBottom(task, lastInRank);

        return task;
    }

    /** {@inheritDoc} */
    @Transactional
    public Task rankAndMove(Task task, Task upperTask, Integer parentStoryId,
            Integer parentIterationId) throws IllegalArgumentException {

        assignParentForTask(task, parentIterationId, parentStoryId);
        rankToBottom(task, parentStoryId, parentIterationId);
        rankUnderTask(task, upperTask);

        return task;
    }

    public void addResponsible(Task task, User user) {
        task.getResponsibles().add(user);
    }

    // AUTOGENERATED

    public void setIterationBusiness(IterationBusiness iterationBusiness) {
        this.iterationBusiness = iterationBusiness;
    }

    public void setStoryBusiness(StoryBusiness storyBusiness) {
        this.storyBusiness = storyBusiness;
    }

    public void setIterationHistoryEntryBusiness(
            IterationHistoryEntryBusiness iterationHistoryEntryBusiness) {
        this.iterationHistoryEntryBusiness = iterationHistoryEntryBusiness;
    }

    public void setRankingBusiness(RankingBusiness rankingBusiness) {
        this.rankingBusiness = rankingBusiness;
    }

    public void setDailyWorkBusiness(DailyWorkBusiness dailyWorkBusiness) {
        this.dailyWorkBusiness = dailyWorkBusiness;
    }

    public void setHourEntryBusiness(HourEntryBusiness hourEntryBusiness) {
        this.hourEntryBusiness = hourEntryBusiness;
    }
}