package fi.hut.soberit.agilefant.business;

import java.util.Collection;

import fi.hut.soberit.agilefant.db.StoryDAO;
import fi.hut.soberit.agilefant.db.TaskDAO;
import fi.hut.soberit.agilefant.db.WhatsNextEntryDAO;
import fi.hut.soberit.agilefant.db.WhatsNextStoryEntryDAO;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.model.WhatsNextEntry;
import fi.hut.soberit.agilefant.model.WhatsNextStoryEntry;
import fi.hut.soberit.agilefant.transfer.AssignedWorkTO;
import fi.hut.soberit.agilefant.transfer.DailyWorkTaskTO;
import fi.hut.soberit.agilefant.transfer.StoryTO;

public interface DailyWorkBusiness {
    /**
     * Retrieves the rank ordered list of next tasks for user
     */
    public Collection<DailyWorkTaskTO> getQueuedTasksForUser(User user);
    
    /**
     * @param user the user whose daily work we are ranking
     * @param task
     * @param upperTask
     * @return
     * @throws IllegalArgumentException
     */
    public DailyWorkTaskTO rankUnderTaskOnWhatsNext(User user, Task task, Task upperTask) throws IllegalArgumentException;
    
    /**
     * @param user the user whose daily work we are ranking
     * @param task the task ...
     */
    public DailyWorkTaskTO rankToBottomOnWhatsNext(User user, Task task) throws IllegalArgumentException;

    /**
     * Removes the task from user's what's next list
     * @param user
     * @param task
     */
    public void removeFromWhatsNext(User user, Task task) throws IllegalArgumentException;

    /**
     * Adds the task to the user's what's next list
     * @param user
     * @param task
     */
    public WhatsNextEntry addToWhatsNext(User user, Task task) throws IllegalArgumentException;
    
    /**
     * Removes all queue entries that are related to this task from the queues
     * @param task
     */
    public void removeTaskFromWorkQueues(Task task);
    
    /**
     * Retrieves the rank ordered list of next stories for user
     */
    public Collection<StoryTO> getQueuedStoriesForUser(User user);
    
    /**
     * @param user the user whose daily work we are ranking
     * @param story
     * @param upperStory
     * @return
     * @throws IllegalArgumentException
     */
    public StoryTO rankUnderStoryOnWhatsNext(User user, Story story, Story upperStory) throws IllegalArgumentException;
    
    /**
     * @param user the user whose daily work we are ranking
     * @param story the story ...
     */
    public StoryTO rankToBottomOnWhatsNext(User user, Story story) throws IllegalArgumentException;

    /**
     * Removes the story from user's what's next list
     * @param user
     * @param story
     */
    public void removeFromWhatsNext(User user, Story story) throws IllegalArgumentException;

    /**
     * Adds the story to the user's what's next list
     * @param user
     * @param story
     */
    public WhatsNextStoryEntry addToWhatsNext(User user, Story story) throws IllegalArgumentException;
    
    /**
     * Removes all queue entries that are related to this story from the queues
     * @param story
     */
    public void removeStoryFromWorkQueues(Story story);

    /**
     * Retrieves all assigned and current work for the given user, in proper format
     * @param user
     * @return
     */
    public AssignedWorkTO getAssignedWorkFor(User user);

    
    public void setTransferObjectBusiness(TransferObjectBusiness transferObjectBusiness);
    public void setTaskDAO(TaskDAO dao);
    public void setWhatsNextEntryDAO(WhatsNextEntryDAO whatsNextEntryDAO);
    public void setWhatsNextStoryEntryDAO(WhatsNextStoryEntryDAO whatsNextStoryEntryDAO);
    public void setTaskBusiness(TaskBusiness taskBusiness);
    public void setRankingBusiness(RankingBusiness rankingBusiness);
    public void setStoryDAO(StoryDAO storyDAO);

}
