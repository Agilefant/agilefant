package fi.hut.soberit.agilefant.db;

import java.util.Collection;
import java.util.List;

import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryRank;

public interface StoryRankDAO extends GenericDAO<StoryRank> {
    StoryRank retrieveByBacklogAndStory(Backlog backlog, Story story);
        
    List<StoryRank> retrieveRanksByBacklog(Backlog backlog);
    
    public Collection<StoryRank> getIterationRanksForStories(Collection<Story> stories);
    public Collection<StoryRank> getProjectRanksForStories(Collection<Story> stories);
}
