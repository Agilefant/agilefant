package fi.hut.soberit.agilefant.business;

import java.util.List;
import java.util.Set;

import fi.hut.soberit.agilefant.exception.ObjectNotFoundException;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.transfer.StoryTO;
import fi.hut.soberit.agilefant.util.ChildHandlingChoice;
import fi.hut.soberit.agilefant.util.HourEntryHandlingChoice;
import fi.hut.soberit.agilefant.util.StoryMetrics;
import fi.hut.soberit.agilefant.util.TaskHandlingChoice;

public interface StoryBusiness extends GenericBusiness<Story> {

    /**
     * Copies the dataItem's data to the persisted <code>Story</code> object.
     * @param tasksToDone TODO
     * 
     * @return the newly persisted story
     */
    Story store(Integer storyId, Story dataItem, Integer backlogId,
            Set<Integer> responsibleIds, boolean tasksToDone) throws ObjectNotFoundException,
            IllegalArgumentException;

    /**
     * Create and persist a new story.
     */
    Story create(Story dataItem, Integer backlogId, Integer iterationId, Set<Integer> responsibleIds, List<String> labelNames)
            throws IllegalArgumentException, ObjectNotFoundException;

    int create(Story story);

    StoryMetrics calculateMetrics(Story story);

    /**
     * Moves a story to another backlog.
     */
    public void moveStoryAway(Story story, Backlog backlog);

    /**
     * Rank story under the give upperStory.
     */
    public Story rankStoryUnder(final Story story, final Story upperStory,
            Backlog backlog);

    /**
     * Rank story over the given lowerStory.
     */
    public Story rankStoryOver(final Story story, final Story lowerStory,
            Backlog backlog);

    public Story updateStoryRanks(Story story);
    
    /**
     * Ranks the story to the top of the given backlog.
     */
    public Story rankStoryToTop(Story story, Backlog context);
    
    /**
     * Ranks the story to the bottom of the given backlog.
     */
    public Story rankStoryToBottom(Story story, Backlog context);

    public StoryTO retrieveStoryWithMetrics(int storyId);

    public List<Story> retrieveStoriesInIteration(Iteration iteration);
    
    void delete(Story story, TaskHandlingChoice taskHandlingChoice,
            HourEntryHandlingChoice storyHourEntryHandlingChoice,
            HourEntryHandlingChoice taskHourEntryHandlingChoice,
            ChildHandlingChoice childHandlingChoice);

    void deleteAndUpdateHistory(int id, TaskHandlingChoice taskHandlingChoice,
            HourEntryHandlingChoice storyHourEntryHandlingChoice,
            HourEntryHandlingChoice taskHourEntryHandlingChoice,
            ChildHandlingChoice childHandlingChoice);

    void forceDelete(Story story);
    
    public Story createStoryUnder(int referenceStoryId, int backlogId, Story data,
            Set<Integer> responsibleIds, List<String> labelNames);

    public Story createStorySibling(int referenceStoryId, int backlogId, Story data,
            Set<Integer> responsibleIds, List<String> labelNames);
    
    public void moveSingleStoryToBacklog(Story story, Backlog backlog);
    
    public void moveStoryAndChildren(Story story, Backlog backlog);

    public Story copyStorySibling(Integer storyID, Story story);
    
    /**
     * If the story is in progress (state is anything but not_started or deferred) then set parent stories as started.
     */
    public void updateParentStates(Story story);
}
