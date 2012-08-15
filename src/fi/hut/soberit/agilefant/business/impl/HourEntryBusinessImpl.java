package fi.hut.soberit.agilefant.business.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.MutableDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.BacklogBusiness;
import fi.hut.soberit.agilefant.business.HourEntryBusiness;
import fi.hut.soberit.agilefant.business.StoryBusiness;
import fi.hut.soberit.agilefant.business.TaskBusiness;
import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.db.BacklogHourEntryDAO;
import fi.hut.soberit.agilefant.db.HourEntryDAO;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.BacklogHourEntry;
import fi.hut.soberit.agilefant.model.HourEntry;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryHourEntry;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.TaskHourEntry;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.transfer.DailySpentEffort;
import org.joda.time.DateMidnight;
import org.joda.time.DateTimeZone;


@Service("hourEntryBusiness")
@Transactional
public class HourEntryBusinessImpl extends GenericBusinessImpl<HourEntry>
        implements HourEntryBusiness {

    private HourEntryDAO hourEntryDAO;

    @Autowired
    private StoryBusiness storyBusiness;
    @Autowired
    private TaskBusiness taskBusiness;
    @Autowired
    private UserBusiness userBusiness;
    @Autowired
    private BacklogBusiness backlogBusiness;
  
    @Autowired
    private BacklogHourEntryDAO backlogHourEntryDAO;

    public HourEntryBusinessImpl() {
        super(HourEntry.class);
    }

    @Autowired
    public void setHourEntryDAO(HourEntryDAO hourEntryDAO) {
        this.genericDAO = hourEntryDAO;
        this.hourEntryDAO = hourEntryDAO;
    }

    @Transactional
    public void logBacklogEffort(int backlogId, HourEntry effortEntry,
            Set<Integer> userIds) {
        Backlog backlog = this.backlogBusiness.retrieve(backlogId);
        
        Collection<User> targetUsers = this.userBusiness.retrieveMultiple(userIds);
        
        for(User targetUser : targetUsers) {
            BacklogHourEntry backlogEntry = new BacklogHourEntry();
            backlogEntry.setBacklog(backlog);
            backlogEntry.setUser(targetUser);
            validateAndCopyFields(backlogEntry, effortEntry);
            this.hourEntryDAO.create(backlogEntry);
        }
    }

    @Transactional
    public void logStoryEffort(int storyId, HourEntry effortEntry,
            Set<Integer> userIds) {
        
        Story story = this.storyBusiness.retrieve(storyId);
        Collection<User> targetUsers = this.userBusiness.retrieveMultiple(userIds);
        
        for(User targetUser : targetUsers) {
            StoryHourEntry storyEntry = new StoryHourEntry();
            storyEntry.setStory(story);
            storyEntry.setUser(targetUser);
            validateAndCopyFields(storyEntry, effortEntry);
            this.hourEntryDAO.create(storyEntry);
        }
    }

    @Transactional
    public void logTaskEffort(int taskId, HourEntry effortEntry,
            Set<Integer> userIds) {
        Task task = this.taskBusiness.retrieve(taskId);
        Collection<User> targetUsers = this.userBusiness.retrieveMultiple(userIds);
        
        for (User targetUser : targetUsers) {
            TaskHourEntry taskEntry = new TaskHourEntry();
            taskEntry.setTask(task);
            taskEntry.setUser(targetUser);
            validateAndCopyFields(taskEntry, effortEntry);
            this.hourEntryDAO.create(taskEntry);
        }
    }
    
    private void validateAndCopyFields(HourEntry target, HourEntry source) {
        if(source.getDate() == null) {
            throw new IllegalArgumentException("Invalid date");
        }
        target.setDescription(source.getDescription());
        target.setDate(source.getDate());
        target.setMinutesSpent(source.getMinutesSpent());
    }

    @Transactional(readOnly = true)
    public List<HourEntry> retrieveBacklogHourEntries(int backlogId, boolean limited) {
        int limit = (limited) ? HourEntryBusiness.ENTRY_LIMIT : 0;
        return hourEntryDAO.getBacklogHourEntries(backlogId, limit);
    }
    
    @Transactional(readOnly = true)
    public List<HourEntry> retrieveStoryHourEntries(int storyId, boolean limited) {
        int limit = (limited) ? HourEntryBusiness.ENTRY_LIMIT : 0;
        return hourEntryDAO.getStoryHourEntries(storyId, limit);
    }

    @Transactional(readOnly = true)
    public List<HourEntry> retrieveTaskHourEntries(int taskId, boolean limited) {
        int limit = (limited) ? HourEntryBusiness.ENTRY_LIMIT : 0;
        return hourEntryDAO.getTaskHourEntries(taskId, limit);
    }

    public List<BacklogHourEntry> retrieveByParent(Backlog parent) {
        return backlogHourEntryDAO.retrieveByBacklog(parent);
    }
 
    @Transactional(readOnly = true)
    public long calculateSumByUserAndTimeInterval(User user,
            DateTime startDate, DateTime endDate) {
        return hourEntryDAO.calculateSumByUserAndTimeInterval(user, startDate, endDate);
    }
    
    @Transactional(readOnly = true)
    public long calculateSumByUserAndTimeInterval(int userId,
            DateTime startDate, DateTime endDate) {
        return hourEntryDAO.calculateSumByUserAndTimeInterval(userId, startDate, endDate);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public long calculateSum(Collection<? extends HourEntry> hourEntries) {
        long effortSpent = 0;
        for (HourEntry hourEntry : hourEntries) {
            effortSpent += hourEntry.getMinutesSpent();
        }
        return effortSpent;
    }
    
    @Transactional(readOnly = true)
    public List<DailySpentEffort> getDailySpentEffortByIteration(Iteration iteration) {
        List<HourEntry> entries = hourEntryDAO.getAllIterationHourEntries(iteration.getId());
        
        return this.getDailySpentEffortForHourEntries(entries, iteration.getStartDate(), iteration.getEndDate());
    }
    
    @Transactional(readOnly = true)
    public List<HourEntry> getHourEntriesForIteration(Iteration iteration) {
        return hourEntryDAO.getAllIterationHourEntries(iteration.getId());        
    }
    
    @Transactional(readOnly = true)
    public long calculateSumOfIterationsHourEntries(Iteration iteration) {
        return hourEntryDAO.calculateIterationHourEntriesSum(iteration.getId());
    }
    
    public List<HourEntry> getEntriesByUserAndTimeInterval(int userId, DateTime startDate,
            DateTime endDate) {
        return this.hourEntryDAO.getHourEntriesByFilter(startDate, endDate, userId);
    }
    
    public List<HourEntry> getEntriesByUserAndDay(LocalDate day, int userId, int userHourTimeZone, int userMinuteTimeZone) {
        DateTime start = day.toDateMidnight().toDateTime();
        DateTime end = start.plusDays(1).minusSeconds(1);
        return this.hourEntryDAO.getHourEntriesByFilter(start, end, userId);
    }

    @Transactional(readOnly = true)
    public long calculateWeekSum(LocalDate week, int userId, int userHourTimeZone, int userMinuteTimeZone) {  
        MutableDateTime tmp = new MutableDateTime(week.toDateMidnight());
        tmp.setDayOfWeek(DateTimeConstants.MONDAY);
        tmp.setSecondOfDay(1);
        DateTime start = tmp.toDateTime(); 
        tmp.addDays(6);
        tmp.setHourOfDay(23);
        tmp.setMinuteOfHour(59);
        tmp.setSecondOfMinute(59);
        DateTime end = tmp.toDateTime();
        return this.hourEntryDAO.calculateSumByUserAndTimeInterval(userId, start, end);
    }
    
    @Transactional(readOnly = true)
    public List<DailySpentEffort> getDailySpentEffortByWeek(LocalDate week, int userId, int userHourTimeZone, int userMinuteTimeZone) {
        MutableDateTime tmp = new MutableDateTime(week.toDateMidnight());
        tmp.setDayOfWeek(DateTimeConstants.MONDAY);
        tmp.setSecondOfDay(1);
        DateTime start = tmp.toDateTime();
        tmp.addDays(6);
        tmp.setHourOfDay(23);
        tmp.setMinuteOfHour(59);
        tmp.setSecondOfMinute(59);
        DateTime end = tmp.toDateTime();
        return this.getDailySpentEffortByInterval(start, end, userId, userHourTimeZone, userMinuteTimeZone);
    }
    
    /**
     * Calculates the daily spent effort hour entries with no timezone calculations.
     */
    @Transactional(readOnly = true)
    public List<DailySpentEffort> getDailySpentEffortByInterval(DateTime start,
            DateTime end, int userId) {
        if(start.compareTo(end) >= 0) {
            return Collections.emptyList();
        }
        List<HourEntry> entries = this.hourEntryDAO.getHourEntriesByFilter(start, end, userId);
        
        return getDailySpentEffortForHourEntries(entries, start, end);
    }
    
    /**
     * Calculates the daily spent effort hour entries with a timezone.
     */
    @Transactional(readOnly = true)
    public List<DailySpentEffort> getDailySpentEffortByInterval(DateTime start,
            DateTime end, int userId, int userHourTimeZone, int userMinuteTimeZone) {
        DateTimeZone zone = DateTimeZone.forOffsetHoursMinutes(userHourTimeZone, userMinuteTimeZone);
        start = start.withZone(zone);
        end = end.withZone(zone);
        Map<DateMidnight, Long> dbData = new HashMap<DateMidnight, Long>();
        List<DailySpentEffort> dailyEffort = new ArrayList<DailySpentEffort>();

        if(start.compareTo(end) >= 0) {
            return Collections.emptyList();
        }
        List<HourEntry> entries = this.hourEntryDAO.getHourEntriesByFilter(start, end, userId);
        
        //sum efforts per day
        for(HourEntry entry : entries) {
            DateMidnight md = entry.getDate().withZone(zone).toDateMidnight();
            if(!dbData.containsKey(md)) {
                dbData.put(md, 0L);
            }
            dbData.put(md, dbData.get(md) + entry.getMinutesSpent());
        }
        MutableDateTime iteratorDate = new MutableDateTime(start.toDateMidnight());
        //construct list that has a single entry per day
        while(iteratorDate.compareTo(end) <= 0) {
            DailySpentEffort effortEntry = new DailySpentEffort();
            
            if(dbData.containsKey(iteratorDate)) {
                effortEntry.setSpentEffort(dbData.get(iteratorDate));
            }
            effortEntry.setDay(iteratorDate.toDateTime());
            dailyEffort.add(effortEntry);
            iteratorDate.addDays(1);
        }
        return dailyEffort;
    }
    
    public List<DailySpentEffort> getDailySpentEffortForHourEntries(List<? extends HourEntry> entries,
            DateTime start, DateTime end) {
        Map<Date, Long> dbData = new HashMap<Date, Long>();
        List<DailySpentEffort> dailyEffort = new ArrayList<DailySpentEffort>();

        //sum efforts per day
        for(HourEntry entry : entries) {
            Date date = entry.getDate().toDateMidnight().toDate();
            
            if(!dbData.containsKey(date)) {
                dbData.put(date, 0L);
            }
            
            dbData.put(date, dbData.get(date) + entry.getMinutesSpent());
        }
        
        MutableDateTime iteratorDate = new MutableDateTime(start.toDateMidnight());
        
        //construct list that has a single entry per day
        while(iteratorDate.compareTo(end) <= 0) {
            DailySpentEffort effortEntry = new DailySpentEffort();
            Date currentDate = iteratorDate.toDate();
            
            if(dbData.containsKey(currentDate)) {
                effortEntry.setSpentEffort(dbData.get(currentDate));
            }
            
            effortEntry.setDay(iteratorDate.toDateTime());
            dailyEffort.add(effortEntry);
            iteratorDate.addDays(1);
        }        
        return dailyEffort;
    }

    public void setBacklogHourEntryDAO(BacklogHourEntryDAO backlogHourEntryDAO) {
        this.backlogHourEntryDAO = backlogHourEntryDAO;
    }

    public void setStoryBusiness(StoryBusiness storyBusiness) {
        this.storyBusiness = storyBusiness;
    }

    public void setUserBusiness(UserBusiness userBusiness) {
        this.userBusiness = userBusiness;
    }
    
    public void setTaskBusiness(TaskBusiness taskBusiness) {
        this.taskBusiness = taskBusiness;
    }
    public void setBacklogBusiness(BacklogBusiness backlogBusiness) {
        this.backlogBusiness = backlogBusiness;
    }

    public void deleteAll(Collection<? extends HourEntry> hourEntries) {
        for (HourEntry hourEntry : hourEntries) {
            hourEntryDAO.remove(hourEntry);
        }
    }
    
    public void moveToBacklog(Collection<? extends HourEntry> hourEntries, Backlog backlog) {
        for (HourEntry hourEntry : hourEntries) {
            BacklogHourEntry newHourEntry = new BacklogHourEntry();
            try {
                PropertyUtils.copyProperties(newHourEntry, hourEntry);
                newHourEntry.setId(0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            hourEntryDAO.remove(hourEntry);
            newHourEntry.setBacklog(backlog);
            hourEntryDAO.store(newHourEntry);
        }
    }

    public void moveToStory(Collection<? extends HourEntry> hourEntries, Story story) {
        for (HourEntry hourEntry : hourEntries) {
            StoryHourEntry newHourEntry = new StoryHourEntry();
            try {
                PropertyUtils.copyProperties(newHourEntry, hourEntry);
                newHourEntry.setId(0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            hourEntryDAO.remove(hourEntry);
            newHourEntry.setStory(story);
            hourEntryDAO.store(newHourEntry);
        }
    }
}
