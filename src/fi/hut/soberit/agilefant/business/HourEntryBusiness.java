package fi.hut.soberit.agilefant.business;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.BacklogHourEntry;
import fi.hut.soberit.agilefant.model.HourEntry;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.transfer.DailySpentEffort;

/**
 * Business interface for handling functionality related to Hour Entries
 * 
 * @author kjniiran
 * @author Pasi Pekkanen
 * 
 */
public interface HourEntryBusiness extends GenericBusiness<HourEntry> {

    public static int ENTRY_LIMIT = 5;
    /**
     * Create one effort entry for each given user under the given story.
     */
    public void logStoryEffort(int storyId, HourEntry effortEntry,
            Set<Integer> userIds);

    /**
     * Create one effort entry for each given user under the given task.
     */
    public void logTaskEffort(int taskId, HourEntry effortEntry,
            Set<Integer> userIds);

    /**
     * Create one effort entry for each given user under the given backlog.
     */
    public void logBacklogEffort(int backlogId, HourEntry effortEntry,
            Set<Integer> userIds);

    /**
     * Gets all task, story and backlog hour entries for an iteration and
     * calculates their sum.
     */
    long calculateSumOfIterationsHourEntries(Iteration iteration);

    List<BacklogHourEntry> retrieveByParent(Backlog item);

    long calculateSumByUserAndTimeInterval(int userId, DateTime startDate,
            DateTime endDate);

    long calculateSum(Collection<? extends HourEntry> hourEntries);

    public List<HourEntry> getEntriesByUserAndTimeInterval(int userId,
            DateTime startDate, DateTime endDate);

    public List<HourEntry> getEntriesByUserAndDay(LocalDate day, int userId, int hourTimeZoneDifference, int minuteTimeZoneDifference);

    public List<DailySpentEffort> getDailySpentEffortByWeek(LocalDate week,
            int userId, int hourTimeZoneDifference, int minuteTimeZoneDifference);

    public List<DailySpentEffort> getDailySpentEffortByInterval(DateTime start,
            DateTime end, int userId);
    
    public List<DailySpentEffort> getDailySpentEffortByInterval(DateTime start,
            DateTime end, int userId, int hourTimeZoneDifference, int minuteTimeZoneDifference);

    List<DailySpentEffort> getDailySpentEffortForHourEntries(List<? extends HourEntry> entries,
            DateTime start, DateTime end);
    
    List<DailySpentEffort> getDailySpentEffortByIteration(Iteration iteration);
    
    List<HourEntry> getHourEntriesForIteration(Iteration iteration);
    

    public long calculateWeekSum(LocalDate week, int userId, int hourTimeZoneDifference, int minuteTimeZoneDifference);

    List<HourEntry> retrieveBacklogHourEntries(int backlogId,
            boolean limited);

    List<HourEntry> retrieveStoryHourEntries(int storyId,
            boolean limited);

    List<HourEntry> retrieveTaskHourEntries(int taskId,
            boolean limited);

    void deleteAll(Collection<? extends HourEntry> hourEntries);

    void moveToStory(Collection<? extends HourEntry> hourEntries, Story story);

    void moveToBacklog(Collection<? extends HourEntry> hourEntries,
            Backlog backlog);
    long retrieveLatestHourEntryDelta(int userId);

}
