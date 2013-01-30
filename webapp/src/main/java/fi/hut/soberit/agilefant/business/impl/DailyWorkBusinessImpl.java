package fi.hut.soberit.agilefant.business.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.DailyWorkBusiness;
import fi.hut.soberit.agilefant.business.RankUnderDelegate;
import fi.hut.soberit.agilefant.business.RankingBusiness;
import fi.hut.soberit.agilefant.business.TaskBusiness;
import fi.hut.soberit.agilefant.business.TransferObjectBusiness;
import fi.hut.soberit.agilefant.db.StoryDAO;
import fi.hut.soberit.agilefant.db.StoryRankDAO;
import fi.hut.soberit.agilefant.db.TaskDAO;
import fi.hut.soberit.agilefant.db.WhatsNextEntryDAO;
import fi.hut.soberit.agilefant.model.Rankable;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryRank;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.model.WhatsNextEntry;
import fi.hut.soberit.agilefant.transfer.AssignedWorkTO;
import fi.hut.soberit.agilefant.transfer.DailyWorkTaskTO;
import fi.hut.soberit.agilefant.transfer.StoryTO;

@Service("dailyWorkBusiness")
@Transactional
public class DailyWorkBusinessImpl implements DailyWorkBusiness {
    private TaskDAO taskDAO;
    private StoryDAO storyDAO;
    private StoryRankDAO storyRankDAO;
    private WhatsNextEntryDAO whatsNextEntryDAO;
    private RankingBusiness rankingBusiness;
    private TaskBusiness taskBusiness;
    private TransferObjectBusiness transferObjectBusiness;
    
    @Autowired
    public void setTaskDAO(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    @Autowired
    public void setStoryDAO(StoryDAO storyDAO) {
        this.storyDAO = storyDAO;
    }

    @Autowired
    public void setWhatsNextEntryDAO(WhatsNextEntryDAO dao) {
        this.whatsNextEntryDAO = dao;
    } 

    @Autowired
    public void setRankingBusiness(RankingBusiness rankingBusiness) {
        this.rankingBusiness = rankingBusiness;
    }

    @Autowired
    public void setTaskBusiness(TaskBusiness taskBusiness) {
        this.taskBusiness = taskBusiness;
    }
    
    @Autowired
    public void setTransferObjectBusiness(TransferObjectBusiness transferObjectBusiness) {
        this.transferObjectBusiness = transferObjectBusiness;
    }

    @Autowired
    public void setStoryRankDAO(StoryRankDAO storyRankDAO) {
        this.storyRankDAO = storyRankDAO;
    }

    public Collection<DailyWorkTaskTO> getQueuedTasksForUser(User user) {
        Collection<WhatsNextEntry> entries = whatsNextEntryDAO.getWhatsNextEntriesFor(user);
        Collection<DailyWorkTaskTO> returned = new ArrayList<DailyWorkTaskTO>();
        
        for (WhatsNextEntry entry: entries) {
            DailyWorkTaskTO item = transferObjectBusiness.constructQueuedDailyWorkTaskTO(entry);
            returned.add(item);
        }
        
        return returned;
    }

    @Transactional
    private void doRankToBottomOnWhatsNext(WhatsNextEntry entry) throws IllegalArgumentException {
        rankingBusiness.rankToBottom(entry, whatsNextEntryDAO.getLastTaskInRank(entry.getUser()));
    }

    @Transactional
    public DailyWorkTaskTO rankToBottomOnWhatsNext(final WhatsNextEntry entry) throws IllegalArgumentException {
        if (entry == null) {
            throw new IllegalArgumentException();
        }
        
        doRankToBottomOnWhatsNext(entry);
        DailyWorkTaskTO transferObj = transferObjectBusiness.constructQueuedDailyWorkTaskTO(entry);
        return transferObj;
    }
    
    @Transactional
    public DailyWorkTaskTO rankToBottomOnWhatsNext(User user, Task task)
            throws IllegalArgumentException {
        return rankToBottomOnWhatsNext(whatsNextEntryDAO.getWhatsNextEntryFor(user, task));
    }

    @Transactional
    public DailyWorkTaskTO rankUnderTaskOnWhatsNext(final WhatsNextEntry entry, WhatsNextEntry upperEntry) {
        if (entry == null) {
            throw new IllegalArgumentException();
        }

        RankUnderDelegate delegate = new RankUnderDelegate() {
            public Collection<? extends Rankable> getWithRankBetween(Integer lower,
                    Integer upper) {
                return whatsNextEntryDAO.getTasksWithRankBetween(lower, upper, entry.getUser());
            }
        };
        
        rankingBusiness.rankUnder(entry, upperEntry, delegate);
        DailyWorkTaskTO transferObj = transferObjectBusiness.constructQueuedDailyWorkTaskTO(entry);
        return transferObj;
    }
    
    @Transactional
    public DailyWorkTaskTO rankUnderTaskOnWhatsNext(User user, Task task,
            Task upperTask) throws IllegalArgumentException {
        WhatsNextEntry entry = whatsNextEntryDAO.getWhatsNextEntryFor(user, task);
        
        if (entry == null) {
            entry = addToWhatsNext(user, task);
        }
        
        WhatsNextEntry upperEntry = null;
        if (upperTask != null) {
            upperEntry = whatsNextEntryDAO.getWhatsNextEntryFor(user, upperTask);
        }
        
        return rankUnderTaskOnWhatsNext(entry, upperEntry);
    }

    @Transactional
    public void removeFromWhatsNext(User user, Task task)
            throws IllegalArgumentException {
        WhatsNextEntry entry = whatsNextEntryDAO.getWhatsNextEntryFor(user, task);
        if (entry != null) {
            whatsNextEntryDAO.remove(entry);
        }
    }
    
    @Transactional
    public WhatsNextEntry addToWhatsNext(User user, Task task) {
        WhatsNextEntry entry = new WhatsNextEntry();
        entry.setTask(task);
        entry.setUser(user);
        whatsNextEntryDAO.store(entry);
        taskBusiness.addResponsible(task, user);
        doRankToBottomOnWhatsNext(entry);
        return entry;
    }

    public void removeTaskFromWorkQueues(Task task) {
        whatsNextEntryDAO.removeAllByTask(task);
    }

    public AssignedWorkTO getAssignedWorkFor(User user) {
        DateTime now = new DateTime();
        DateTime dayStart = now.withMillisOfDay(0);
        DateTime dayEnd   = dayStart.plusDays(1);
        Interval interval = new Interval(dayStart, dayEnd);
        
        Collection<Task> tasks = taskDAO.getAllIterationAndStoryTasks(user, interval);
        Collection<Story> stories = storyDAO.getAllIterationStoriesByResponsibleAndInterval(user, interval);
        
        AssignedWorkTO returnable = transferObjectBusiness.constructAssignedWorkTO(tasks, stories);

        Collection<Story> finalStories = new ArrayList<Story>();
        finalStories.addAll(returnable.getStories());
        Collection<StoryRank> ranks = storyRankDAO.getIterationRanksForStories(finalStories);

        setStoryRanks(returnable.getStories(), ranks);
        
        return returnable; 
    }
    
    @SuppressWarnings("unchecked")
    private static void setStoryRanks(List<StoryTO> stories, Collection<StoryRank> ranks) {
        Map<Integer, Integer> rankMap = new HashMap<Integer, Integer>();
        for (StoryRank rank : ranks) {
            rankMap.put(rank.getStory().getId(), rank.getRank());
        }
        // Set the ranks for the stories
        for (StoryTO s : stories) {
            s.setRank(rankMap.get(s.getId()));
        }
        
        Collections.sort(stories, new PropertyComparator("rank", true, true));
        Collections.sort(stories, new PropertyComparator("backlog.name", true, true));
        
        int i = 0;
        for (StoryTO s : stories) {
            s.setRank(i++);
        }
    }
}
