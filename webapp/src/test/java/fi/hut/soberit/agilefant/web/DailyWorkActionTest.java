package fi.hut.soberit.agilefant.web;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

import fi.hut.soberit.agilefant.business.DailyWorkBusiness;
import fi.hut.soberit.agilefant.business.TaskBusiness;
import fi.hut.soberit.agilefant.business.TransferObjectBusiness;
import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.model.StoryRank;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.model.WhatsNextEntry;
import fi.hut.soberit.agilefant.transfer.AssignedWorkTO;
import fi.hut.soberit.agilefant.transfer.DailyWorkTaskTO;
import fi.hut.soberit.agilefant.transfer.StoryTO;
import fi.hut.soberit.agilefant.transfer.TaskTO;

public class DailyWorkActionTest {
    private DailyWorkAction testable;

        
    private DailyWorkBusiness dailyWorkBusiness;
    private UserBusiness userBusiness;
    private TaskBusiness taskBusiness;
    private TransferObjectBusiness transferObjectBusiness;

    protected int LOGGED_IN_USER = 456;
    
    @SuppressWarnings("serial")
    @Before
    public void setUp_dependencies() {
        testable = new DailyWorkAction() {
            @Override
            protected int getLoggedInUserId() {
                return LOGGED_IN_USER;
            }
        };
        
        dailyWorkBusiness = createStrictMock(DailyWorkBusiness.class);
        testable.setDailyWorkBusiness(dailyWorkBusiness);

        userBusiness = createStrictMock(UserBusiness.class);
        testable.setUserBusiness(userBusiness);

        taskBusiness = createStrictMock(TaskBusiness.class);
        testable.setTaskBusiness(taskBusiness);
        
        transferObjectBusiness = createStrictMock(TransferObjectBusiness.class);
        testable.setTransferObjectBusiness(transferObjectBusiness);
    }
    
    private void replayAll() {
        replay(dailyWorkBusiness, taskBusiness, userBusiness, transferObjectBusiness);
    }

    private void verifyAll() {
        verify(dailyWorkBusiness, taskBusiness, userBusiness, transferObjectBusiness);
    }
    
    @Test
    public void testRetrieve() {
        final int USER_ID = 42;
        User user = new User(); 
        testable.setUserId(USER_ID);

        Collection<DailyWorkTaskTO> returnedList  = Arrays.asList(
            new DailyWorkTaskTO(new Task(), 1), 
            new DailyWorkTaskTO(new Task(), 2), 
            new DailyWorkTaskTO(new Task(), 4)
        );

        expect(userBusiness.retrieve(USER_ID)).andReturn(user);
        expect(dailyWorkBusiness.getQueuedTasksForUser(user))
            .andReturn(returnedList);

        AssignedWorkTO assignedWork = new AssignedWorkTO();
        expect(dailyWorkBusiness.getAssignedWorkFor(user))
            .andReturn(assignedWork);
        
        expect(dailyWorkBusiness.getQueuedStoriesForUser(user)).andReturn(testable.getStories());
        
        replayAll();

        assertEquals(Action.SUCCESS, testable.retrieve());

        verifyAll();

        assertEquals(returnedList,  testable.getQueuedTasks());
                
        assertEquals(user, testable.getUser());
        assertEquals(USER_ID, testable.getUserId());
        //assertSame(assignedWork.getStories(), testable.getStories());
        assertSame(assignedWork.getTasksWithoutStory(), testable.getTasksWithoutStory());
    }
    
    @Test
    public void testDeleteFromQueue() {
        User user = new User();
        user.setId(LOGGED_IN_USER);
        
        Task task = new Task();
        task.setId(1);
        
        testable.setTaskId(1);
        
        expect(userBusiness.retrieve(LOGGED_IN_USER)).andReturn(user);
        expect(taskBusiness.retrieve(1)).andReturn(task);
        dailyWorkBusiness.removeFromWhatsNext(user, task);
        
        TaskTO taskTO = new TaskTO(task);
        expect(transferObjectBusiness.constructTaskTO(task)).andReturn(taskTO);

        replayAll();
        testable.deleteFromWorkQueue();
        
        verifyAll();
        
        // This is to be provided in JSON
        assertSame(taskTO, testable.getTask());
    }

    @Test
    public void testAddToQueue() {
        User user = new User();
        user.setId(3);
        
        Task task = new Task();
        task.setId(1);
        
        testable.setTaskId(1);
        testable.setUserId(3);
        
        expect(userBusiness.retrieve(3)).andReturn(user);
        expect(taskBusiness.retrieve(1)).andReturn(task);
        
        WhatsNextEntry entry = new WhatsNextEntry();
        expect(dailyWorkBusiness.addToWhatsNext(user, task)).andReturn(entry);

        TaskTO taskTO = new TaskTO(task);
        expect(transferObjectBusiness.constructTaskTO(task)).andReturn(taskTO);

        replayAll();
        testable.addToWorkQueue();
        
        verifyAll();
        
        // This is to be provided in JSON
        assertSame(taskTO, testable.getTask());
    }
    
    @Test
    public void testRankQueueTaskAndMoveUnder() {
        Task task = new Task();
        task.setId(1);
        
        Task rankUnder = new Task();
        rankUnder.setId(2);
        
        User user = new User();
        user.setId(3);
        
        testable.setTaskId(1);
        testable.setRankUnderId(2);
        testable.setUserId(3);

        expect(userBusiness.retrieve(3)).andReturn(user);
        expect(taskBusiness.retrieve(1)).andReturn(task);
        expect(taskBusiness.retrieveIfExists(2)).andReturn(rankUnder);
        expect(dailyWorkBusiness.rankUnderTaskOnWhatsNext(user, task, rankUnder)).andReturn(new DailyWorkTaskTO(task));
        
        replayAll();
        
        testable.rankQueueTaskAndMoveUnder();
        
        verifyAll();
    }
    
    @Test
    public void testRetrieveWorkQueue() {
        final int USER_ID = 42;
        User user = new User(); 
        testable.setUserId(USER_ID);

        Collection<DailyWorkTaskTO> returnedList  = Arrays.asList(
            new DailyWorkTaskTO(new Task(), 1), 
            new DailyWorkTaskTO(new Task(), 2), 
            new DailyWorkTaskTO(new Task(), 4)
        );

        expect(userBusiness.retrieve(USER_ID)).andReturn(user);
        expect(dailyWorkBusiness.getQueuedTasksForUser(user))
            .andReturn(returnedList);
        
        replayAll();

        assertEquals(Action.SUCCESS, testable.retrieveWorkQueue());

        verifyAll();

        assertEquals(returnedList,  testable.getQueuedTasks());
    }
    
    @Test
    public void testRetrieveAssignedTasks() {
        final int USER_ID = 42;
        User user = new User(); 
        testable.setUserId(USER_ID);

        expect(userBusiness.retrieve(USER_ID)).andReturn(user);

        AssignedWorkTO assignedWork = new AssignedWorkTO();
        expect(dailyWorkBusiness.getAssignedWorkFor(user))
            .andReturn(assignedWork);
        
        expect(dailyWorkBusiness.getQueuedStoriesForUser(user)).andReturn(testable.getStories());
        
        replayAll();

        assertEquals(Action.SUCCESS, testable.retrieveAssignedStories());

        verifyAll();
                
        assertEquals(user, testable.getUser());
        assertEquals(USER_ID, testable.getUserId());
        //assertSame(assignedWork.getStories(), testable.getStories());
    }
    
    @Test
    public void testRetrieveAssignedStories() {
        final int USER_ID = 42;
        User user = new User(); 
        testable.setUserId(USER_ID);

        expect(userBusiness.retrieve(USER_ID)).andReturn(user);

        AssignedWorkTO assignedWork = new AssignedWorkTO();
        expect(dailyWorkBusiness.getAssignedWorkFor(user))
            .andReturn(assignedWork);
        
        replayAll();

        assertEquals(Action.SUCCESS, testable.retrieveAssignedTasks());

        verifyAll();

                
        assertEquals(user, testable.getUser());
        assertEquals(USER_ID, testable.getUserId());
        assertSame(assignedWork.getTasksWithoutStory(), testable.getTasksWithoutStory());
    }
}
