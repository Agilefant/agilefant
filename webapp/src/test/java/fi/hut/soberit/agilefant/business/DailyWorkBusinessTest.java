package fi.hut.soberit.agilefant.business;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;

import fi.hut.soberit.agilefant.business.impl.DailyWorkBusinessImpl;
import fi.hut.soberit.agilefant.db.StoryDAO;
import fi.hut.soberit.agilefant.db.StoryRankDAO;
import fi.hut.soberit.agilefant.db.TaskDAO;
import fi.hut.soberit.agilefant.db.WhatsNextEntryDAO;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Rankable;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryRank;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.model.WhatsNextEntry;
import fi.hut.soberit.agilefant.transfer.AssignedWorkTO;
import fi.hut.soberit.agilefant.transfer.DailyWorkTaskTO;

public class DailyWorkBusinessTest {
    private DailyWorkBusinessImpl testable;
    
    private TaskDAO taskDAO;
    private WhatsNextEntryDAO whatsNextEntryDAO;

    private RankingBusiness rankingBusiness;
    private TaskBusiness taskBusiness;

    private Task task1;

    private Task task3;

    private Task task2;

    private Story story;

    private User user;

    private Product backlog;

    private WhatsNextEntry whatsNextEntry_forTask1AndUser;

    private WhatsNextEntry whatsNextEntry_forTask2AndUser;

    private TransferObjectBusiness transferObjectBusiness;
    
    private StoryRankDAO storyRankDAO;

    private StoryDAO storyDAO;
    
    @Before
    public void setUp() {
        testable = new DailyWorkBusinessImpl();
        
        taskDAO = createMock(TaskDAO.class);
        testable.setTaskDAO(taskDAO);
        
        storyDAO = createMock(StoryDAO.class);
        testable.setStoryDAO(storyDAO);

        whatsNextEntryDAO = createMock(WhatsNextEntryDAO.class);
        testable.setWhatsNextEntryDAO(whatsNextEntryDAO);
        
        rankingBusiness = createMock(RankingBusiness.class);
        testable.setRankingBusiness(rankingBusiness);
        
        taskBusiness = createMock(TaskBusiness.class);
        testable.setTaskBusiness(taskBusiness);
        
        transferObjectBusiness = createMock(TransferObjectBusiness.class);
        testable.setTransferObjectBusiness(transferObjectBusiness);
        
        storyRankDAO = createMock(StoryRankDAO.class);
        testable.setStoryRankDAO(storyRankDAO);

        backlog = new Product();
        backlog.setId(5);
        
        user = new User();
        
        task1 = new Task();
        task1.setId(1);
        task2 = new Task();
        task2.setId(2);
        task3 = new Task();
        task3.setId(3);
        
        story = new Story();
        story.setId(4);

        whatsNextEntry_forTask1AndUser = new WhatsNextEntry();
        whatsNextEntry_forTask1AndUser.setUser(user);
        whatsNextEntry_forTask1AndUser.setTask(task1);

        whatsNextEntry_forTask2AndUser = new WhatsNextEntry();
        whatsNextEntry_forTask2AndUser.setUser(user);
        whatsNextEntry_forTask2AndUser.setTask(task2);
    }

    private void replayAll() {
        replay(taskDAO, storyDAO, whatsNextEntryDAO, rankingBusiness, taskBusiness, transferObjectBusiness, storyRankDAO);
    }
    
    private void verifyAll() {
        verify(taskDAO, storyDAO, whatsNextEntryDAO, rankingBusiness, taskBusiness, transferObjectBusiness, storyRankDAO);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAssignedWorkFor() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        tasks.addAll(Arrays.asList(task1, task2, task3));
        
        Story story = new Story();
        ArrayList<Story> stories = new ArrayList<Story>();
        stories.addAll(Arrays.asList(story));

        Capture<Interval> interval = new Capture<Interval>();
        Capture<Interval> interval2 = new Capture<Interval>();
        
        expect(taskDAO.getAllTasks(EasyMock.eq(user), 
                EasyMock.and(EasyMock.capture(interval), EasyMock.isA(Interval.class)))).andReturn(tasks);
        expect(storyDAO.getAllIterationStoriesByResponsibleAndInterval(EasyMock.eq(user), 
                EasyMock.and(EasyMock.capture(interval2), EasyMock.isA(Interval.class)))).andReturn(stories);
        AssignedWorkTO assignedWork = new AssignedWorkTO();
        
        expect(transferObjectBusiness.constructAssignedWorkTO(tasks, stories)).andReturn(assignedWork);

        expect(storyRankDAO.getIterationRanksForStories(EasyMock.isA(Collection.class))).andReturn(new ArrayList<StoryRank>());
        expect(storyRankDAO.getProjectRanksForStories(EasyMock.isA(Collection.class))).andReturn(new ArrayList<StoryRank>());
        
        replayAll();
        AssignedWorkTO returned = testable.getAssignedWorkFor(user);
        verifyAll();
        
        assertSame(assignedWork, returned);

        assertTrue(interval.hasCaptured());
        assertTrue(interval2.hasCaptured());
        Interval intervalValue = interval.getValue();
        Interval interval2Value = interval2.getValue();
        assertNotNull(intervalValue);
        assertEquals(intervalValue, interval2Value);

        // race condition during midnight... ;) must contain today!
        assertTrue(intervalValue.containsNow());
        
        // duration of interval is exactly 24 hours
        assertEquals(new Duration(24*60*60*1000), intervalValue.toDuration());

        // and the milliseconds within the day must be 0 
        assertEquals(0, intervalValue.getStart().getMillisOfDay());
    }
    
    @Test
    public void testGetQueuedTasksForUser() {
        expect(whatsNextEntryDAO.getWhatsNextEntriesFor(user)).andReturn(
                Arrays.asList(whatsNextEntry_forTask1AndUser, whatsNextEntry_forTask2AndUser));

        DailyWorkTaskTO dwtto1 = new DailyWorkTaskTO(task1);
        DailyWorkTaskTO dwtto2 = new DailyWorkTaskTO(task2);
        
        expect(transferObjectBusiness.constructQueuedDailyWorkTaskTO(whatsNextEntry_forTask1AndUser)).andReturn(dwtto1);
        expect(transferObjectBusiness.constructQueuedDailyWorkTaskTO(whatsNextEntry_forTask2AndUser)).andReturn(dwtto2);
        
        replayAll();
        
        Collection<DailyWorkTaskTO> returned = testable.getQueuedTasksForUser(user);

        verifyAll();
        
        assertEquals(Arrays.asList(dwtto1, dwtto2), returned);
    }
    
    @Test
    public void testRankToBottomOnWhatsNext() {
        expect(whatsNextEntryDAO.getWhatsNextEntryFor(user, task1)).andReturn(whatsNextEntry_forTask1AndUser);
        expect(whatsNextEntryDAO.getLastTaskInRank(user)).andReturn(whatsNextEntry_forTask2AndUser);
        rankingBusiness.rankToBottom(whatsNextEntry_forTask1AndUser, whatsNextEntry_forTask2AndUser);
        
        DailyWorkTaskTO originalTO = new DailyWorkTaskTO(task1);
        
        expect(transferObjectBusiness.constructQueuedDailyWorkTaskTO(whatsNextEntry_forTask1AndUser)).andReturn(
            originalTO
        );
        replayAll();
        
        DailyWorkTaskTO returnedTO = testable.rankToBottomOnWhatsNext(user, task1);
        verifyAll();
        assertSame(originalTO, returnedTO);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testRankToBottom_noEntry() {
        expect(whatsNextEntryDAO.getWhatsNextEntryFor(user, task1)).andReturn(null);
        
        replayAll();

        testable.rankToBottomOnWhatsNext(user, task1);
        verifyAll();
    }
    
    @Test
    public void testRankUnder_addEntry_upperTaskNotNull() {
        expect(whatsNextEntryDAO.getWhatsNextEntryFor(user, task1)).andReturn(null);

        Capture<WhatsNextEntry> entryCapture    = new Capture<WhatsNextEntry>();
        Capture<WhatsNextEntry> entryCapture2   = new Capture<WhatsNextEntry>();
        Capture<RankUnderDelegate> delegateCapture = new Capture<RankUnderDelegate>();

        whatsNextEntryDAO.store(and(isA(WhatsNextEntry.class), EasyMock.capture(entryCapture)));
        taskBusiness.addResponsible(task1, user);
        expect(whatsNextEntryDAO.getLastTaskInRank(user)).andReturn(whatsNextEntry_forTask2AndUser);
        expect(whatsNextEntryDAO.getWhatsNextEntryFor(user, task2)).andReturn(null);
        
        rankingBusiness.rankToBottom(and(isA(WhatsNextEntry.class), 
            EasyMock.capture(entryCapture2)), 
            eq(whatsNextEntry_forTask2AndUser)
        );
        
        rankingBusiness.rankUnder(
            isA(Rankable.class), 
            eq((Rankable)null),
            and(isA(RankUnderDelegate.class), capture(delegateCapture))
        );
        
        // from delegate:
        expect(whatsNextEntryDAO.getTasksWithRankBetween(1, 2, user)).andReturn(new ArrayList<WhatsNextEntry>());
        
        DailyWorkTaskTO originalTO = new DailyWorkTaskTO(task1);
        expect(transferObjectBusiness.constructQueuedDailyWorkTaskTO(isA(WhatsNextEntry.class))).andReturn(
            originalTO
        );

        replayAll();

        DailyWorkTaskTO returnedTO = testable.rankUnderTaskOnWhatsNext(user, task1, task2);

        assertTrue(delegateCapture.hasCaptured());
        delegateCapture.getValue().getWithRankBetween(1, 2);
        verifyAll();

        assertTrue(entryCapture.hasCaptured());
        assertTrue(entryCapture2.hasCaptured());

        assertSame(originalTO, returnedTO);
    }
    
    @Test
    public void testRemoveFromWhatsNext() {
        expect(whatsNextEntryDAO.getWhatsNextEntryFor(user, task1)).andReturn(whatsNextEntry_forTask1AndUser);
        whatsNextEntryDAO.remove(whatsNextEntry_forTask1AndUser);

        replayAll();
        testable.removeFromWhatsNext(user, task1);
        
        verifyAll();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testRankUnderTaskOnWhatsNext_withNullEntry() {
        testable.rankUnderTaskOnWhatsNext(null, new WhatsNextEntry());
    }
    
    @Test
    public void testRankUnder_existingEntry_rankToTop() {
        expect(whatsNextEntryDAO.getWhatsNextEntryFor(user, task1)).andReturn(whatsNextEntry_forTask1AndUser);
        
        rankingBusiness.rankUnder(
            isA(Rankable.class), 
            eq((Rankable)null),
            isA(RankUnderDelegate.class)
        );

        DailyWorkTaskTO dwtto1 = new DailyWorkTaskTO(task1);
        expect(transferObjectBusiness.constructQueuedDailyWorkTaskTO(whatsNextEntry_forTask1AndUser)).andReturn(dwtto1);
        replayAll();

        DailyWorkTaskTO returnedTO = testable.rankUnderTaskOnWhatsNext(user, task1, null);

        verifyAll();
        assertSame(dwtto1, returnedTO);
    }

    @Test
    public void testRemoveTaskFromWorkQueues() {
        whatsNextEntryDAO.removeAllByTask(task1);
        
        replayAll();
        testable.removeTaskFromWorkQueues(task1);
        verifyAll();
    }
    
    @Test
    public void testRemoveFromWhatsNext_notFound() {
        expect(whatsNextEntryDAO.getWhatsNextEntryFor(user, task1)).andReturn(null);
        replayAll();
        
        // should not throw whenever an entry is not found, just be happy.
        testable.removeFromWhatsNext(user, task1);
        verifyAll();
    }
}
