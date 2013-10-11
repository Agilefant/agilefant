package fi.hut.soberit.agilefant.business;

import java.util.Collection;
import java.util.List;

import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.WhatsNextEntry;
import fi.hut.soberit.agilefant.model.WhatsNextStoryEntry;
import fi.hut.soberit.agilefant.transfer.AssignedWorkTO;
import fi.hut.soberit.agilefant.transfer.AutocompleteDataNode;
import fi.hut.soberit.agilefant.transfer.DailyWorkTaskTO;
import fi.hut.soberit.agilefant.transfer.IterationTO;
import fi.hut.soberit.agilefant.transfer.ProjectTO;
import fi.hut.soberit.agilefant.transfer.ScheduleStatus;
import fi.hut.soberit.agilefant.transfer.StoryTO;
import fi.hut.soberit.agilefant.transfer.TaskTO;

public interface TransferObjectBusiness {

    /**
     * Constructs a new transfer object based on given task.
     */
    public TaskTO constructTaskTO(Task task);
    
    /**
     * Constructs a new transfer object based on given story.
     */
    public StoryTO constructStoryTO(Story story);
    
    /**
     * Constructs a new transfer object based on given task.
     */
    public IterationTO constructIterationTO(Iteration iteration);
    
    /**
     * 
     */
    public ProjectTO constructProjectTO(Project project);
    
    /**
     * Get all users in AutoCompleteData containers.
     */
    public List<AutocompleteDataNode> constructUserAutocompleteData();
    
    /**
     * Get all teams in AutoCompleteData containers.
     * @param listUserIds Add user ids as idList.
     */
    public List<AutocompleteDataNode> constructTeamAutocompleteData(boolean listUserIds);
    
    /**
     * Get all backlogs in AutoCompleteData containers.
     * 
     * Use <code>backlogId</code> to filter out other products' child backlogs.
     *   
     * @param backlogId Filter backlogs by parent product of the backlog with id <code>backlogId</code>
     */
    public List<AutocompleteDataNode> constructBacklogAutocompleteData(Integer backlogId);
    
    /**
     * Get all backlogs and iterations in AutoCompleteData containers.
     * 
     * Use <code>backlogId</code> to filter out other products' child backlogs.
     *   
     * @param backlogId Filter backlogs by parent product of the backlog with id <code>backlogId</code>
     */
    public List<AutocompleteDataNode> constructBacklogAndIterationAutocompleteData(Integer backlogId);
    
    /**
     * Get all products in <code>AutoCompleteData</code> containers.
     */
    public List<AutocompleteDataNode> constructProductAutocompleteData();
    
    /**
     * Get all projects in <code>AutoCompleteData</code> containers.
     */
    public List<AutocompleteDataNode> constructProjectAutocompleteData();
    
    /**
     * Get all backlogs in AutoCompleteData containers.
     */
    public List<AutocompleteDataNode> constructCurrentIterationAutocompleteData();

    /**    
     * Checks whether the backlog is ongoing, past or future.
     */
    public ScheduleStatus getBacklogScheduleStatus(Backlog backlog);
    
    /**
     * Constructs a new daily work task transfer object for queue entry
     */
    public DailyWorkTaskTO constructQueuedDailyWorkTaskTO(WhatsNextEntry task);
    
    /**
     * Constructs a new daily work story transfer object for queue entry
     */
    public StoryTO constructQueuedStoryTO(WhatsNextStoryEntry story);

    /**
     * Constructs a new assigned work transfer object for the given tasks
     * @param stories 
     */
    public AssignedWorkTO constructAssignedWorkTO(Collection<Task> tasks, Collection<Story> stories);
}
