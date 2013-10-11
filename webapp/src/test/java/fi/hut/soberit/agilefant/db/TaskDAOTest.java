package fi.hut.soberit.agilefant.db;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.test.AbstractHibernateTests;
import fi.hut.soberit.agilefant.transfer.UnassignedLoadTO;

@ContextConfiguration
@Transactional
public class TaskDAOTest extends AbstractHibernateTests {

    @Autowired  
    private TaskDAO taskDAO;
    
    
    @Test
    public void testGetIterationTasksWithEffortLeft() {
        executeClassSql();
        DateTime start = new DateTime(2009,6,10,1,0,0,0);
        Interval interval = new Interval(start, start.plusDays(5));
        User user = new User();
        user.setId(1);
        List<Task> actual = this.taskDAO.getIterationTasksWithEffortLeft(user, interval);
        assertEquals(2, actual.size());
    }
    
    @Test
    public void testGetIterationTasksWithEffortLeft_notInTimeframe() {
        executeClassSql();
        DateTime start = new DateTime(2008,6,10,1,0,0,0);
        User user = new User();
        user.setId(1);
        Interval interval = new Interval(start, start.plusDays(5));
        List<Task> actual = this.taskDAO.getIterationTasksWithEffortLeft(user, interval);
        assertEquals(0, actual.size());
    }
    
    @Test
    public void testGetStoryTasksWithEffortLeft() {
        executeClassSql();
        DateTime start = new DateTime(2009,6,10,1,0,0,0);
        User user = new User();
        user.setId(1);
        Interval interval = new Interval(start, start.plusDays(5));
        List<Task> actual = this.taskDAO.getStoryTasksWithEffortLeft(user, interval);
        assertEquals(3, actual.size());
    }
    
    @Test
    public void testGetStoryTasksWithEffortLeft_beginsWithin() {
        executeClassSql();
        DateTime start = new DateTime(2009,5,15,1,0,0,0);
        User user = new User();
        user.setId(1);
        Interval interval = new Interval(start, start.plusDays(6));
        List<Task> actual = this.taskDAO.getStoryTasksWithEffortLeft(user, interval);
        assertEquals(3, actual.size());
    }
    
    @Test
    public void testGetStoryTasksWithEffortLeft_EndsWithin() {
        executeClassSql();
        DateTime start = new DateTime(2009,6,29,1,0,0,0);
        User user = new User();
        user.setId(1);
        Interval interval = new Interval(start, start.plusDays(5));
        List<Task> actual = this.taskDAO.getStoryTasksWithEffortLeft(user, interval);
        assertEquals(3, actual.size());
    }
    
    @Test
    public void testGetStoryTasksWithEffortLeft_notInTimeframe() {
        executeClassSql();
        DateTime start = new DateTime(2008,6,10,1,0,0,0);
        User user = new User();
        user.setId(1);
        Interval interval = new Interval(start, start.plusDays(5));
        List<Task> actual = this.taskDAO.getStoryTasksWithEffortLeft(user, interval);
        assertEquals(0, actual.size());
    }
    
    @Test
    public void testGetNumOfResponsiblesByTask() {
        executeClassSql();
        Set<Integer> taskIds = new HashSet<Integer>(Arrays.asList(1,2,3,4,5));
        Map<Integer, Integer> actual = this.taskDAO.getNumOfResponsiblesByTask(taskIds);
        assertEquals(1, (int)actual.get(1));
        assertEquals(2, (int)actual.get(2));
        assertEquals(1, (int)actual.get(3));
        assertEquals(2, (int)actual.get(4));
        assertEquals(1, (int)actual.get(5));
    }
    
    @Test
    public void testGetUnassignedStoryTasksWithEffortLeft() {
        executeClassSql();
        DateTime start = new DateTime(2009,6,10,1,0,0,0);
        User user = new User();
        user.setId(1);
        Interval interval = new Interval(start, start.plusDays(5));
        List<UnassignedLoadTO> actual = this.taskDAO.getUnassignedStoryTasksWithEffortLeft(user, interval);
        assertEquals(1, actual.size());
        assertEquals(60000, actual.get(0).effortLeft.intValue());
        assertEquals(100, actual.get(0).availability);
        assertEquals(1, actual.get(0).iterationId);
    }
    
    @Test
    public void testGetUnassignedStoryTasksWithEffortLeftWithNullEffortSpent() {
        executeClassSql();
        DateTime start = new DateTime(2009,6,10,1,0,0,0);
        User user = new User();
        user.setId(3);
        Interval interval = new Interval(start, start.plusDays(5));
        List<UnassignedLoadTO> actual = this.taskDAO.getUnassignedStoryTasksWithEffortLeft(user, interval);
        assertTrue(actual.isEmpty());
    }    
    
    @Test
    public void testGetUnassignedIterationTasksWithEffortLeft() {
        executeClassSql();
        DateTime start = new DateTime(2009,6,10,1,0,0,0);
        User user = new User();
        user.setId(1);
        Interval interval = new Interval(start, start.plusDays(5));
        List<UnassignedLoadTO> actual = this.taskDAO.getUnassignedIterationTasksWithEffortLeft(user, interval);
        assertEquals(2, actual.size());
        assertEquals(970, actual.get(0).effortLeft.intValue());
        assertEquals(100, actual.get(0).availability);
        assertEquals(1, actual.get(0).iterationId);
    }
    
    Iteration iter;
    Story story;
    
    @Before
    public void setUp() {
        iter = new Iteration();
        story = new Story();
    }
    
    @Test
    public void testGetTasksWithRankBetween_iterationTop() {
        iter.setId(1);
        executeClassSql();
        Collection<Task> actual = taskDAO.getTasksWithRankBetween(0, 2, iter, null);
        assertEquals(3, actual.size());
    }
    
    @Test
    public void testGetTasksWithRankBetween_iterationBottom() {
        iter.setId(1);
        executeClassSql();
        Collection<Task> actual = taskDAO.getTasksWithRankBetween(3, 6, iter, null);
        assertEquals(1, actual.size());
    }
    
    @Test
    public void testGetTasksWithRankBetween_iterationEmptyCollection() {
        iter.setId(1);
        executeClassSql();
        Collection<Task> actual = taskDAO.getTasksWithRankBetween(2, 0, iter, null);
        assertEquals(0, actual.size());
    }
    
    @Test
    public void testGetTasksWithRankBetween_storyTop() {
        story.setId(1);
        executeClassSql();
        Collection<Task> actual = taskDAO.getTasksWithRankBetween(0, 0, null, story);
        assertEquals(1, actual.size());
    }
    
    @Test
    public void testGetTasksWithRankBetween_storyBottom() {
        story.setId(3);
        executeClassSql();
        Collection<Task> actual = taskDAO.getTasksWithRankBetween(1, 5, null, story);
        assertEquals(1, actual.size());
    }
    
    @Test
    public void testGetTasksWithRankBetween_storyEmptyCollection() {
        story.setId(1);
        executeClassSql();
        Collection<Task> actual = taskDAO.getTasksWithRankBetween(2, 1, null, story);
        assertEquals(0, actual.size());
    }
    
    
    @Test
    public void testGetNextTaskInRank_iteration() {
        iter.setId(1);
        executeClassSql();
        Task actual = taskDAO.getNextTaskInRank(0, iter, null);       
        assertEquals(1, actual.getRank().intValue());
    }
    
    @Test
    public void testGetNextTaskInRank_iteration_largeCap() {
        iter.setId(3);
        executeClassSql();
        Task actual = taskDAO.getNextTaskInRank(25, iter, null);       
        assertEquals(17, actual.getId());
        assertEquals(1500, actual.getRank().intValue());
    }
    
    @Test
    public void testGetNextTaskInRank_iteration_notFound() {
        iter.setId(1);
        executeClassSql();
        assertNull(taskDAO.getNextTaskInRank(999, iter, null));
    }
    
    @Test
    public void testGetNextTaskInRank_story() {
        story.setId(55);
        executeClassSql();
        Task actual = taskDAO.getNextTaskInRank(0, null, story);
        assertEquals(1, actual.getRank().intValue());
        assertEquals(21, actual.getId());
    }
    
    @Test
    public void testGetNextTaskInRank_story_largeCap() {
        story.setId(55);
        executeClassSql();
        Task actual = taskDAO.getNextTaskInRank(25, null, story);
        
        assertEquals(22, actual.getId());
        assertEquals(666, actual.getRank().intValue());
    }
    
    @Test
    public void testGetNextTaskInRank_story_notFound() {
        story.setId(55);
        executeClassSql();
        assertNull(taskDAO.getNextTaskInRank(999, null, story));
    }
    
    @Test
    public void testGetLastTaskInRank_iteration() {
        iter.setId(3);
        executeClassSql();
        Task actual = taskDAO.getLastTaskInRank(null, iter);
        assertEquals(17, actual.getId());
        assertEquals(1500, actual.getRank().intValue());
    }
    
    @Test
    public void testGetLastTaskInRank_story() {
        story.setId(55);
        executeClassSql();
        Task actual = taskDAO.getLastTaskInRank(story, null);
        assertEquals(22, actual.getId());
        assertEquals(666, actual.getRank().intValue());
    }
    
    @Test
    public void testGetAllIterationAndStoryTasks() {
        executeClassSql();
        DateTime start = new DateTime(2009,6,10,1,0,0,0);
        Interval interval = new Interval(start, start.plusDays(5));
        User user = new User();
        user.setId(1);
        List<Task> actual = this.taskDAO.getAllIterationAndStoryTasks(user, interval);
       
        HashSet<Integer> actualIds = new HashSet<Integer>();
        for (Task t: actual) {
            actualIds.add(t.getId()); 
        }
        
        HashSet<Integer> expectedIds = new HashSet<Integer>(
            Arrays.asList(2,3,4)
        );        
        assertEquals(expectedIds, actualIds);
    }
        
    @Test
    public void testGetAllIterationAndStoryTasks_user_hasNoAssigned() {
        executeClassSql();
        DateTime start = new DateTime(2009,6,10,1,0,0,0);
        Interval interval = new Interval(start, start.plusDays(5));
        User user = new User();
        user.setId(4);
       
        List<Task> actual = this.taskDAO.getAllIterationAndStoryTasks(user, interval);
        assertEquals(0, actual.size());
    }
}
