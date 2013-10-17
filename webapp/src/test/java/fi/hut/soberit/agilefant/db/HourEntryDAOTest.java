package fi.hut.soberit.agilefant.db;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.model.BacklogHourEntry;
import fi.hut.soberit.agilefant.model.StoryHourEntry;
import fi.hut.soberit.agilefant.model.TaskHourEntry;
import fi.hut.soberit.agilefant.test.AbstractHibernateTests;

@ContextConfiguration
@Transactional
public class HourEntryDAOTest extends AbstractHibernateTests {
    
    @Autowired
    private HourEntryDAO hourEntryDAO;
    
    @Test
    public void testCalculateSumByStory() {
        executeClassSql();
        assertEquals(80, hourEntryDAO.calculateSumByStory(4));
    }

    @Test
    public void testCalculateSumByStory_storiesWithHourEntries() {
        executeClassSql();
        assertEquals(170, hourEntryDAO.calculateSumByStory(6));
    }
    
    @Test
    public void testCalculateSumFromTasksWithoutStory() {
        executeClassSql();
        assertEquals(60, hourEntryDAO.calculateSumFromTasksWithoutStory(5));
    }
    
    @Test
    public void testGetBacklogHourEntriesByFilter_noFilter() {
        executeClassSql();
        List<BacklogHourEntry> actualResult = hourEntryDAO.getBacklogHourEntriesByFilter(null, null, null, null);
        assertNotNull(actualResult);
        assertEquals(0, actualResult.size());
    
    }
    
    @Test
    public void testGetBacklogHourEntriesByFilter_emptyProduct() {
        executeClassSql();
        Set<Integer>  backlogs = new HashSet<Integer>(Arrays.asList(2));
        List<BacklogHourEntry> actualResult = hourEntryDAO.getBacklogHourEntriesByFilter(backlogs, null, null, null);
        assertNotNull(actualResult);
        assertEquals(0, actualResult.size());
    }
    @Test
    public void testGetBacklogHourEntriesByFilter_WithData() {
        executeClassSql();
        Set<Integer>  backlogs = new HashSet<Integer>(Arrays.asList(1));
        List<BacklogHourEntry> actualResult = hourEntryDAO.getBacklogHourEntriesByFilter(backlogs, null, null, null);
        assertNotNull(actualResult);
        assertEquals(6, actualResult.size());
    }
    @Test
    public void testGetBacklogHourEntriesByFilter_WithDataFilter() {
        executeClassSql();
        Set<Integer>  backlogs = new HashSet<Integer>(Arrays.asList(3));
        List<BacklogHourEntry> actualResult = hourEntryDAO.getBacklogHourEntriesByFilter(backlogs, null, null, null);
        assertNotNull(actualResult);
        assertEquals(4, actualResult.size());
    }

    @Test
    public void testGetBacklogHourEntriesByFilter_DateFiltert() {
        executeClassSql();
        Set<Integer>  backlogs = new HashSet<Integer>(Arrays.asList(5));
        DateTime startTime = new DateTime(2009, 5, 14, 9, 0, 0, 0);
        DateTime endTime = new DateTime(2009, 5, 14, 16, 0, 0, 0);
        List<BacklogHourEntry> actualResult = hourEntryDAO.getBacklogHourEntriesByFilter(backlogs, startTime, endTime, null);
        assertNotNull(actualResult);
//        assertEquals(1, actualResult.size());
    }
    
    @Test
    public void testGetBacklogHourEntriesByFilter_UserFilter() {
        executeClassSql();
        Set<Integer>  backlogs = new HashSet<Integer>(Arrays.asList(5));
        Set<Integer> userIds = new HashSet<Integer>(Arrays.asList(1));
        List<BacklogHourEntry> actualResult = hourEntryDAO.getBacklogHourEntriesByFilter(backlogs, null, null, userIds);
        assertNotNull(actualResult);
        assertEquals(2, actualResult.size());
    }
    
    @Test
    public void testGetBacklogHourEntriesByFilter_UserFilterNotFound() {
        executeClassSql();
        Set<Integer>  backlogs = new HashSet<Integer>(Arrays.asList(5));
        Set<Integer> userIds = new HashSet<Integer>(Arrays.asList(2));
        List<BacklogHourEntry> actualResult = hourEntryDAO.getBacklogHourEntriesByFilter(backlogs, null, null, userIds);
        assertNotNull(actualResult);
        assertEquals(0, actualResult.size());
    }
    
    @Test
    public void testGetStoryHourEntriesByFilter_noFilter() {
        List<StoryHourEntry> actualEntries = hourEntryDAO.getStoryHourEntriesByFilter(null, null, null, null);
        assertNotNull(actualEntries);
        assertEquals(0, actualEntries.size());
    }
    
    @Test
    public void testGetStoryHourEntriesByFilter_ProductNoEntries() {
        executeClassSql();
        Set<Integer>  backlogs = new HashSet<Integer>(Arrays.asList(2));
        List<StoryHourEntry> actualEntries = hourEntryDAO.getStoryHourEntriesByFilter(backlogs, null, null, null);
        assertNotNull(actualEntries);
        assertEquals(0, actualEntries.size());
    }
    
    @Test
    public void testGetStoryHourEntriesByFilter_Product() {
        executeClassSql();
        Set<Integer>  backlogs = new HashSet<Integer>(Arrays.asList(1));
        List<StoryHourEntry> actualEntries = hourEntryDAO.getStoryHourEntriesByFilter(backlogs, null, null, null);
        assertNotNull(actualEntries);
        assertEquals(5, actualEntries.size());
    }
    
    @Test
    public void testGetStoryHourEntriesByFilter_Project() {
        executeClassSql();
        Set<Integer>  backlogs = new HashSet<Integer>(Arrays.asList(3));
        List<StoryHourEntry> actualEntries = hourEntryDAO.getStoryHourEntriesByFilter(backlogs, null, null, null);
        assertNotNull(actualEntries);
        assertEquals(5, actualEntries.size());
    }
    
    @Test
    public void testGetStoryHourEntriesByFilter_ProductIteration() {
        executeClassSql();
        Set<Integer>  backlogs = new HashSet<Integer>(Arrays.asList(5));
        List<StoryHourEntry> actualEntries = hourEntryDAO.getStoryHourEntriesByFilter(backlogs, null, null, null);
        assertNotNull(actualEntries);
        assertEquals(2, actualEntries.size());
    }
    
    @Test
    public void testGetTaskHourEntriesByFilter_NoFilter() {
        List<TaskHourEntry> actualEntries = hourEntryDAO.getTaskHourEntriesByFilter(null, null, null, null);
        assertNotNull(actualEntries);
        assertEquals(0, actualEntries.size());
    }

    @Test
    public void testGetTaskHourEntriesByFilter_emptyProduct() {
        executeClassSql();
        Set<Integer>  backlogs = new HashSet<Integer>(Arrays.asList(2));
        List<TaskHourEntry> actualEntries = hourEntryDAO.getTaskHourEntriesByFilter(backlogs, null, null, null);
        assertNotNull(actualEntries);
        assertEquals(0, actualEntries.size());
    }
    
    @Test
    public void testGetTaskHourEntriesByFilter_Product() {
        executeClassSql();
        Set<Integer>  backlogs = new HashSet<Integer>(Arrays.asList(1));
        List<TaskHourEntry> actualEntries = hourEntryDAO.getTaskHourEntriesByFilter(backlogs, null, null, null);
        assertNotNull(actualEntries);
        assertEquals(10, actualEntries.size());
    }
    
    @Test
    public void testGetTaskHourEntriesByFilter_Project() {
        executeClassSql();
        Set<Integer>  backlogs = new HashSet<Integer>(Arrays.asList(3));
        List<TaskHourEntry> actualEntries = hourEntryDAO.getTaskHourEntriesByFilter(backlogs, null, null, null);
        assertNotNull(actualEntries);
        assertEquals(8, actualEntries.size());
    }
    
    @Test
    public void testGetTaskHourEntriesByFilter_Iteration() {
        executeClassSql();
        Set<Integer>  backlogs = new HashSet<Integer>(Arrays.asList(5));
        List<TaskHourEntry> actualEntries = hourEntryDAO.getTaskHourEntriesByFilter(backlogs, null, null, null);
        assertNotNull(actualEntries);
        assertEquals(6, actualEntries.size());
    }
    
    @Test
    public void testCalculateIterationHourEntries() {
        executeClassSql();
        long actualSum = hourEntryDAO.calculateIterationHourEntriesSum(5);
        assertEquals(4240, actualSum);
    }
    
    @Test
    public void testCalculateIterationHourEntries_emptyIteration() {
        executeClassSql();
        long actualSum = hourEntryDAO.calculateIterationHourEntriesSum(6);
        assertEquals(0, actualSum);
    }
    
    @Test
    public void testGetHourEntriesByFilter() {
        executeClassSql();
        DateTime startDate = new DateTime(2009, 5, 12, 10, 20, 0, 0);
        DateTime endDate = new DateTime(2009, 5, 14, 10, 20, 0, 0);
//        assertEquals(3, hourEntryDAO.getHourEntriesByFilter(startDate, endDate, 1).size());
    }
    
    @Test
    public void testGetHourEntriesByFilter_noUser() {
        executeClassSql();
        assertEquals(21, hourEntryDAO.getHourEntriesByFilter(null, null, 0).size());
    }
    
    @Test
    public void testGetHourEntriesByFilter_noDates() {
        executeClassSql();
        assertEquals(21, hourEntryDAO.getHourEntriesByFilter(null, null, 1).size());
    }
    
    @Test
    public void testGetBacklogHourEntries() {
        executeClassSql();
        assertEquals(2, hourEntryDAO.getBacklogHourEntries(5, 0).size());
    }
    
    @Test
    public void testGetBacklogHourEntries_withLimit() {
        executeClassSql();
        assertEquals(1, hourEntryDAO.getBacklogHourEntries(5, 1).size());
    }

    @Test
    public void testGetStoryHourEntries() {
        executeClassSql();
        assertEquals(3, hourEntryDAO.getStoryHourEntries(6, 0).size());
    }
    
    @Test
    public void testGetStoryHourEntries_withLimit() {
        executeClassSql();
        assertEquals(1, hourEntryDAO.getStoryHourEntries(6, 1).size());
    }

    @Test
    public void testGetTaskHourEntries() {
        executeClassSql();
        assertEquals(2, hourEntryDAO.getTaskHourEntries(4, 0).size());
    }
    
    @Test
    public void testGetTaskHourEntries_withLimit() {
        executeClassSql();
        assertEquals(1, hourEntryDAO.getTaskHourEntries(4, 1).size());
    }

}
