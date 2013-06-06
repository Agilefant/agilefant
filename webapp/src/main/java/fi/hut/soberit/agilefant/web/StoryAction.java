package fi.hut.soberit.agilefant.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.annotations.PrefetchId;
import fi.hut.soberit.agilefant.business.BacklogBusiness;
import fi.hut.soberit.agilefant.business.StoryBusiness;
import fi.hut.soberit.agilefant.business.StoryRankBusiness;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryRank;
import fi.hut.soberit.agilefant.model.StoryState;
import fi.hut.soberit.agilefant.transfer.HistoryRowTO;
import fi.hut.soberit.agilefant.transfer.StoryTO;
import fi.hut.soberit.agilefant.util.ChildHandlingChoice;
import fi.hut.soberit.agilefant.util.HourEntryHandlingChoice;
import fi.hut.soberit.agilefant.util.StoryMetrics;
import fi.hut.soberit.agilefant.util.TaskHandlingChoice;

@Component("storyAction")
@Scope("prototype")
public class StoryAction extends ActionSupport implements CRUDAction, Prefetching {

    private static final long serialVersionUID = -4289013472775815522L;

    private int backlogId = 0;

    @PrefetchId
    private Integer storyId;
    
    private Integer targetStoryId;

    private StoryState state;
    
    private int iteration;

    private int priority;

    private Story story;

    private Backlog backlog;

    private Set<Integer> userIds = new HashSet<Integer>();
    
    private boolean usersChanged = false;
        
    private String storyListContext;
    
    private StoryMetrics metrics;
        
    private List<HistoryRowTO> storyHistory;
    
    private boolean tasksToDone = false;
    
    private List<String> labelNames;

    

    @Autowired
    private StoryBusiness storyBusiness;
    
    @Autowired
    private StoryRankBusiness storyRankBusiness;
    
    @Autowired
    private BacklogBusiness backlogBusiness;
    
    private HourEntryHandlingChoice taskHourEntryHandlingChoice;
    private HourEntryHandlingChoice storyHourEntryHandlingChoice;
    private TaskHandlingChoice taskHandlingChoice;
    private ChildHandlingChoice childHandlingChoice;


    @Override
    public String execute() throws Exception {
        story = new Story();
        return super.execute();
    }
    
    // CRUD
    
    public String create() {
        story = this.storyBusiness.create(story, backlogId, iteration, userIds, labelNames);
        StoryRank rank = storyRankBusiness.getRankByBacklog(story, story.getBacklog());
        
        story = new StoryTO(story);
        if (rank != null) ((StoryTO)story).setRank(rank.getRank());
        
        return Action.SUCCESS;
    }
    
    /**
     * Creates a new deep copy of a given story and places it
     * as a sibling.
     * @author braden
     * 
     * @return Successful action.
     */
    public String copyStorySibling() {
        story = storyBusiness.copyStorySibling(storyId, story);
        return Action.SUCCESS;
    }
    
    /**
     * Creates a new copy of a given story and
     * moves the unfinished tasks to the new story
     * 
     * @return Successful action.
     */
    public String extractUnfinishedStorySibling() {
        story = storyBusiness.extractUnfinishedStorySibling(storyId, story);
        return Action.SUCCESS;
    }

    public String delete() {
        storyBusiness.deleteAndUpdateHistory(storyId, taskHandlingChoice, storyHourEntryHandlingChoice, taskHourEntryHandlingChoice, childHandlingChoice);
        return Action.SUCCESS;
    }

    public String retrieve() {
        story = storyBusiness.retrieveStoryWithMetrics(storyId);
        //story = this.toTransferObject(story);
        return Action.SUCCESS;
    }

    public String deleteStoryForm() {
        story = storyBusiness.retrieve(storyId);
        return Action.SUCCESS;
    }
    
    public String store() {
        Set<Integer> users = null;
        if (usersChanged) {
            users = this.userIds;
        }
        story = storyBusiness.store(storyId, story, iteration!=0?iteration:null, users, tasksToDone);
        if (tasksToDone) {
            return Action.SUCCESS + "_withTasks";
        }
        return Action.SUCCESS;
    }
    
    
    // OTHER FUNCTIONS
    
    public String moveStoryAway() {
        story = storyBusiness.retrieve(storyId);
        backlog = backlogBusiness.retrieve(backlogId);
        storyBusiness.moveStoryAway(story, backlog);
        return Action.SUCCESS;
    }
    
    public String safeMoveSingleStory() {
        story = storyBusiness.retrieve(storyId);
        backlog = backlogBusiness.retrieve(backlogId);
        storyBusiness.moveSingleStoryToBacklog(story, backlog);
        return Action.SUCCESS;
    }
    
    public String moveStoryAndChildren() {
        story = storyBusiness.retrieve(storyId);
        backlog = backlogBusiness.retrieve(backlogId);
        storyBusiness.moveStoryAndChildren(story, backlog);
        return Action.SUCCESS;
    }
    
    public String rankStoryUnder() {
        story = storyBusiness.retrieve(storyId);
        Story upper = storyBusiness.retrieveIfExists(targetStoryId);
        Backlog backlog = backlogBusiness.retrieveIfExists(backlogId);
        story = storyBusiness.rankStoryUnder(story, upper, backlog);
        return Action.SUCCESS;
    }
    
    public String rankStoryOver() {
        story = storyBusiness.retrieve(storyId);
        Story lower = storyBusiness.retrieveIfExists(targetStoryId);
        Backlog backlog = backlogBusiness.retrieveIfExists(backlogId);
        story = storyBusiness.rankStoryOver(story, lower, backlog);
        return Action.SUCCESS;
    }
    
    public String rankStoryToTop() {
        story = storyBusiness.retrieve(storyId);
        backlog = backlogBusiness.retrieve(backlogId);
        story = storyBusiness.rankStoryToTop(story, backlog);
        return Action.SUCCESS;
    }
    
    public String rankStoryToBottom() {
        story = storyBusiness.retrieve(storyId);
        backlog = backlogBusiness.retrieve(backlogId);
        story = storyBusiness.rankStoryToBottom(story, backlog);
        return Action.SUCCESS;
    }
    
    public String createStoryUnder() {
        story = storyBusiness.createStoryUnder(storyId, backlogId, story, userIds, labelNames);
        return Action.SUCCESS;
    }
    
    public String createStorySibling() {
        story = storyBusiness.createStorySibling(storyId, backlogId, story, userIds, labelNames);
        return Action.SUCCESS;
    }

    // PREFETCHING
    
    public void initializePrefetchedData(int objectId) {
        story = storyBusiness.retrieveDetached(objectId);
    }
    
    
    // AUTOGENERATED
    
    public Backlog getBacklog() {
        return backlog;
    }

    public void setBacklog(Backlog backlog) {
        this.backlog = backlog;
    }

    public int getBacklogId() {
        return backlogId;
    }

    public void setBacklogId(int backlogId) {
        this.backlogId = backlogId;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
        this.storyId = story.getId();
    }
   
    public String getStoryName() {
        return story.getName();
    }

    public void setStoryName(String storyName) {
        story.setName(storyName);
    }

    public StoryState getState() {
        return state;
    }

    public void setState(StoryState state) {
        this.state = state;
    }

    public Set<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Integer> userIds) {
        this.userIds = userIds;
    }

    public Integer getStoryId() {
        return storyId;
    }

    public void setStoryId(Integer storyId) {
        this.storyId = storyId;
    }

    public void setIteration(int iterationId) {
        this.iteration = iterationId;
    }
    
    public int getIterationId() {
        return iteration;
    }
    
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return priority;
    }
    
    public String getStoryListContext() {
        return storyListContext;
    }

    public void setStoryListContext(String storyListContext) {
        this.storyListContext = storyListContext;
    }
    
    public void setStoryBusiness(StoryBusiness storyBusiness) {
        this.storyBusiness = storyBusiness;
    }

    public StoryMetrics getMetrics() {
        return metrics;
    }

    public void setUsersChanged(boolean usersChanged) {
        this.usersChanged = usersChanged;
    }

    public List<HistoryRowTO> getStoryHistory() {
        return storyHistory;
    }

    public void setBacklogBusiness(BacklogBusiness backlogBusiness) {
        this.backlogBusiness = backlogBusiness;
    }

    public void setTargetStoryId(Integer targetStoryId) {
        this.targetStoryId = targetStoryId;
    }
    public void setTaskHandlingChoice(TaskHandlingChoice taskHandlingChoice) {
        this.taskHandlingChoice = taskHandlingChoice;
    }
    public void setTaskHourEntryHandlingChoice(
            HourEntryHandlingChoice taskHourEntryHandlingChoice) {
        this.taskHourEntryHandlingChoice = taskHourEntryHandlingChoice;
    }
    public void setStoryHourEntryHandlingChoice(
            HourEntryHandlingChoice storyHourEntryHandlingChoice) {
        this.storyHourEntryHandlingChoice = storyHourEntryHandlingChoice;
    }
    public HourEntryHandlingChoice getStoryHourEntryHandlingChoice() {
        return storyHourEntryHandlingChoice;
    }
    public TaskHandlingChoice getTaskHandlingChoice() {
        return taskHandlingChoice;
    }
    public HourEntryHandlingChoice getTaskHourEntryHandlingChoice() {
        return taskHourEntryHandlingChoice;
    }
    public void setTasksToDone(boolean tasksToDone) {
        this.tasksToDone = tasksToDone;
    }

    public void setChildHandlingChoice(ChildHandlingChoice childHandlingChoice) {
        this.childHandlingChoice = childHandlingChoice;
    }

    public List<String> getLabelNames() {
        return labelNames;
    }

    public void setLabelNames(List<String> labelNames) {
        this.labelNames = labelNames;
    }
}
