package fi.hut.soberit.agilefant.db;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.model.WhatsNextStoryEntry;

public interface WhatsNextStoryEntryDAO extends GenericDAO<WhatsNextStoryEntry> {
    /**
     * Gets the user's "what's next" stories with rank between and including
     * lower and upper borders.
     * <p>
     * @param lower lower border of the rank (0 if topmost included)
     * @param upper upper border of the rank
     * @param user the user
     * 
     * @return
     */
    public Collection<WhatsNextStoryEntry> getStoriesWithRankBetween(int lower, int upper, User user);
        
    /**
     * Gets the last ranked "what's next" story for given user.
     */
    public WhatsNextStoryEntry getLastStoryInRank(User user);
    
    /**
     * Gets the What's next entry for given user and given story, if exists
     */
    public WhatsNextStoryEntry getWhatsNextStoryEntryFor(User user, Story story);

    /**
     * Gets the What's next entry for given user and given story, if exists
     */
    public Collection<WhatsNextStoryEntry> getWhatsNextStoryEntriesFor(User user);

    /**
     * Removes all entries by story
     * @param story
     */
    public void removeAllByStory(Story story);
    
    /**
     * Fetches User->Story mappings, for each user the first story on their work queue.
     * @return
     */
    public Map<User, List<Story>> getTopmostWorkQueueEntries();
}
