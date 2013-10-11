package fi.hut.soberit.agilefant.business;


import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.hibernate.envers.RevisionType;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.hut.soberit.agilefant.business.impl.IterationBusinessImpl;
import fi.hut.soberit.agilefant.db.IterationDAO;
import fi.hut.soberit.agilefant.db.IterationHistoryEntryDAO;
import fi.hut.soberit.agilefant.db.history.BacklogHistoryDAO;
import fi.hut.soberit.agilefant.db.history.StoryHistoryDAO;
import fi.hut.soberit.agilefant.db.history.TaskHistoryDAO;
import fi.hut.soberit.agilefant.exception.ObjectNotFoundException;
import fi.hut.soberit.agilefant.model.AgilefantRevisionEntity;
import fi.hut.soberit.agilefant.model.Assignment;
import fi.hut.soberit.agilefant.model.BacklogHourEntry;
import fi.hut.soberit.agilefant.model.ExactEstimate;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.IterationHistoryEntry;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.test.Mock;
import fi.hut.soberit.agilefant.test.MockContextLoader;
import fi.hut.soberit.agilefant.test.MockedTestCase;
import fi.hut.soberit.agilefant.test.TestedBean;
import fi.hut.soberit.agilefant.transfer.AgilefantHistoryEntry;
import fi.hut.soberit.agilefant.transfer.IterationMetrics;
import fi.hut.soberit.agilefant.transfer.IterationTO;
import fi.hut.soberit.agilefant.transfer.StoryTO;
import fi.hut.soberit.agilefant.transfer.TaskTO;
import fi.hut.soberit.agilefant.util.HourEntryHandlingChoice;
import fi.hut.soberit.agilefant.util.Pair;
import fi.hut.soberit.agilefant.util.StoryMetrics;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = MockContextLoader.class)
public class IterationBusinessTest  extends MockedTestCase {

    @TestedBean
    IterationBusinessImpl iterationBusiness;
    @Mock
    TransferObjectBusiness transferObjectBusiness;
    @Mock
    StoryBusiness storyBusiness;
    @Mock
    HourEntryBusiness hourEntryBusiness;
    @Mock
    IterationDAO iterationDAO;
    @Mock
    IterationHistoryEntryDAO iterationHistoryEntryDAO;
    @Mock
    IterationHistoryEntryBusiness iterationHistoryEntryBusiness;
    @Mock
    BacklogBusiness backlogBusiness;
    @Mock
    AssignmentBusiness assignmentBusiness;
    @Mock
    BacklogHistoryEntryBusiness backlogHistoryEntryBusiness;
    @Mock
    StoryRankBusiness storyRankBusiness;
    @Mock
    TaskBusiness taskBusiness;
    @Mock
    StoryHistoryDAO storyHistoryDAO;
    @Mock
    BacklogHistoryDAO backlogHistoryDAO;
    @Mock
    TeamBusiness teamBusiness;
    @Mock
    TaskHistoryDAO taskHistoryDAO;
    
    Iteration iteration;
    Project project;
    Set<StoryTO> storiesList;
    Set<Task> tasksWithoutStoryList;
    Set<TaskTO> tasksTOsWithoutStoryList;
    IterationTO expectedIterationData;
    Task task;
    TaskTO taskTO;

    @Before
    public void setUp() {
        iteration = new Iteration();
        iteration.setId(123);
        iteration.setStartDate(new DateTime(2010,1,1,0,0,0,0));
        iteration.setEndDate(new DateTime(2010, 1, 20, 0, 0, 0, 0));

        project = new Project();
        project.setId(313);

        Story story1 = new Story();
        story1.setId(666);
        Story story2 = new Story();
        story2.setId(667);
        StoryTO storyTO1 = new StoryTO(story1);
        StoryTO storyTO2 = new StoryTO(story2);
        storiesList = new HashSet<StoryTO>(Arrays.asList(storyTO1, storyTO2));

        task = new Task();
        task.setId(1254);
        taskTO = new TaskTO(task);
        tasksWithoutStoryList = new HashSet<Task>(Arrays.asList(task));
        tasksTOsWithoutStoryList = new HashSet<TaskTO>(Arrays.asList(taskTO));

        expectedIterationData = new IterationTO(new Iteration());
        expectedIterationData.setStories(new HashSet<Story>());
        expectedIterationData.getStories().addAll(storiesList);
        expectedIterationData.setTasks(new HashSet<Task>());
        expectedIterationData.getTasks().addAll(
                tasksTOsWithoutStoryList);
    }

    @Test
    @DirtiesContext
    public void testGetIterationContents() {
        List<Story> stories = new ArrayList<Story>();
        stories.addAll(iteration.getStories());
        IterationTO iterationTO = new IterationTO(iteration);
        
        expect(iterationDAO.retrieveDeep(iteration.getId())).andReturn(iteration);
        expect(transferObjectBusiness.constructIterationTO(iteration)).andReturn(iterationTO);
        expect(storyBusiness.retrieveStoriesInIteration(iteration)).andReturn(stories);
        Map<Integer, StoryMetrics> emptyMetricsMap = Collections.emptyMap();
        expect(iterationDAO.calculateIterationDirectStoryMetrics(iteration)).andReturn(emptyMetricsMap);
        Map<Integer, Long> emptyTaskMap = Collections.emptyMap();
        expect(iterationDAO.calculateIterationTaskEffortSpent(iteration)).andReturn(emptyTaskMap);
        
        replayAll();
        
        iterationBusiness.getIterationContents(iteration.getId());
        
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testGetIterationContents_hasInvalidRank() {
        List<Story> stories = new ArrayList<Story>();
        stories.addAll(iteration.getStories());
        IterationTO iterationTO = new IterationTO(iteration);
        
        List<Story> rankedStories = new ArrayList<Story>(stories);
        Story invalidRank = new Story();
        invalidRank.setId(999);
        rankedStories.add(invalidRank);
        
        expect(iterationDAO.retrieveDeep(iteration.getId())).andReturn(iteration);
        expect(transferObjectBusiness.constructIterationTO(iteration)).andReturn(iterationTO);
        expect(storyBusiness.retrieveStoriesInIteration(iteration)).andReturn(stories);

        Map<Integer, StoryMetrics> emptyMetricsMap = Collections.emptyMap();
        expect(iterationDAO.calculateIterationDirectStoryMetrics(iteration)).andReturn(emptyMetricsMap);
        Map<Integer, Long> emptyTaskMap = Collections.emptyMap();
        expect(iterationDAO.calculateIterationTaskEffortSpent(iteration)).andReturn(emptyTaskMap);
        
        replayAll();
        
        iterationBusiness.getIterationContents(iteration.getId());
        
        verifyAll();
    }

    @Test(expected = ObjectNotFoundException.class)
    @DirtiesContext
    public void testGetIterationContents_nullBacklog() {
        expect(iterationDAO.retrieveDeep(0)).andReturn(null);
        replay(iterationDAO);
        assertNull(iterationBusiness.getIterationContents(0));
        verify(iterationDAO);
    }

    @Test
    @DirtiesContext
    public void testGetIterationMetrics() {
        IterationHistoryEntry latestEntry = new IterationHistoryEntry();
        latestEntry.setEffortLeftSum(112);
        latestEntry.setOriginalEstimateSum(257);

        BacklogHourEntry he1 = new BacklogHourEntry();
        BacklogHourEntry he2 = new BacklogHourEntry();
        he1.setMinutesSpent(39);
        he2.setMinutesSpent(88);

        List<BacklogHourEntry> hourEntries = new ArrayList<BacklogHourEntry>();
        hourEntries.addAll(Arrays.asList(he1, he2));

        int expectedStoryPoints = 68;
        long expectedSpentEffort = 127;
        Integer expectedPercentDoneTasks = 50;
        Integer expectedPercentDoneStories = 50;
        int expectedTaskCompletion = 56;
        int expectedStoryCompletion = 15;

        expect(iterationHistoryEntryBusiness.retrieveLatest(iteration))
                .andReturn(latestEntry).times(2);
        expect(backlogBusiness.getStoryPointSumByIteration(iteration)).andReturn(
                expectedStoryPoints);
        expect(backlogBusiness.calculateDoneStoryPointSum(iteration.getId())).andReturn(10);
        expect(hourEntryBusiness.calculateSumOfIterationsHourEntries(iteration))
                .andReturn(expectedSpentEffort);
        expect(iterationDAO.getCountOfDoneAndNonDeferred(iteration)).andReturn(
                Pair.create(2, 4));
        expect(iterationDAO.getCountOfDoneAndAllStories(iteration)).andReturn(
                Pair.create(1, 2));
        expect(
                iterationHistoryEntryDAO.retrieveByDate(iteration.getId(),
                        new LocalDate().minusDays(1))).andReturn(null).times(1);
        expect(backlogBusiness.getStoryValueSumByIteration(iteration)).andReturn(0);
        expect(backlogBusiness.getCompletedStoryValueSumByIteration(iteration)).andReturn(0);

        replayAll();

        IterationMetrics actualMetrics = iterationBusiness
                .getIterationMetrics(iteration);

        assertNotNull(actualMetrics);
        assertEquals(latestEntry.getEffortLeftSum(), actualMetrics
                .getEffortLeft().getMinorUnits().longValue());
        assertEquals(latestEntry.getOriginalEstimateSum(), actualMetrics
                .getOriginalEstimate().getMinorUnits().longValue());
        assertEquals(expectedStoryPoints, actualMetrics.getStoryPoints()
                .intValue());
        assertEquals(expectedSpentEffort, actualMetrics.getSpentEffort()
                .getMinorUnits().longValue());
        assertEquals(expectedPercentDoneTasks, actualMetrics
                .getPercentDoneTasks());
        assertEquals(expectedPercentDoneStories, actualMetrics
                .getPercentDoneStories());
        assertEquals(10, actualMetrics.getDoneStoryPoints().intValue());
        assertEquals(expectedTaskCompletion, actualMetrics.getCompletedEffortPercentage());
        assertEquals(expectedStoryCompletion, actualMetrics.getDoneStoryPointsPercentage());

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testGetIterationMetricsZeroTotals() {
        expect(iterationHistoryEntryBusiness.retrieveLatest(iteration))
                .andReturn(null).times(2);
        expect(iterationDAO.getCountOfDoneAndNonDeferred(iteration)).andReturn(
                Pair.create(0, 0));
        expect(iterationDAO.getCountOfDoneAndAllStories(iteration)).andReturn(
                Pair.create(0, 0));

        expect(backlogBusiness.getStoryPointSumByIteration(iteration)).andReturn(0);
        expect(backlogBusiness.calculateDoneStoryPointSum(iteration.getId())).andReturn(0);
        expect(hourEntryBusiness.calculateSumOfIterationsHourEntries(iteration))
                .andReturn(0L);
        expect(backlogBusiness.getStoryValueSumByIteration(iteration)).andReturn(0);
        expect(backlogBusiness.getCompletedStoryValueSumByIteration(iteration)).andReturn(0);

        replayAll();

        IterationMetrics actualMetrics = iterationBusiness
                .getIterationMetrics(iteration);

        assertEquals(0, actualMetrics.getPercentDoneStories().intValue());
        assertEquals(0, actualMetrics.getPercentDoneStories().intValue());

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testGetIterationMetrics_nullLatestHistoryEntry() {
        expect(iterationHistoryEntryBusiness.retrieveLatest(iteration))
                .andReturn(null).times(2);
        expect(iterationDAO.getCountOfDoneAndNonDeferred(iteration)).andReturn(
                Pair.create(2, 4));
        expect(iterationDAO.getCountOfDoneAndAllStories(iteration)).andReturn(
                Pair.create(1, 3));

        expect(backlogBusiness.calculateDoneStoryPointSum(iteration.getId())).andReturn(0);
        expect(backlogBusiness.getStoryPointSumByIteration(iteration)).andReturn(0);
        expect(hourEntryBusiness.calculateSumOfIterationsHourEntries(iteration))
                .andReturn(0L);
        expect(backlogBusiness.getStoryValueSumByIteration(iteration)).andReturn(0);
        expect(backlogBusiness.getCompletedStoryValueSumByIteration(iteration)).andReturn(0);

        replayAll();

        IterationMetrics actualMetrics = iterationBusiness
                .getIterationMetrics(iteration);

        assertEquals(0L, actualMetrics.getEffortLeft().getMinorUnits()
                .longValue());
        assertEquals(0L, actualMetrics.getOriginalEstimate().getMinorUnits()
                .longValue());

        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testGetIterationMetrics_withInterval() {
        Iteration iter = new Iteration();
        iter.setId(100);
        iter.setStartDate(new DateTime());
        iter.setEndDate(iter.getStartDate().plusDays(100));
        IterationHistoryEntry latestHistoryEntry = new IterationHistoryEntry();
        latestHistoryEntry.setOriginalEstimateSum(10);
        latestHistoryEntry.setEffortLeftSum(10);

        expect(iterationHistoryEntryBusiness.retrieveLatest(iter)).andReturn(
                latestHistoryEntry).times(2);

        expect(backlogBusiness.daysLeftInSchedulableBacklog(iter)).andReturn(Days.days(100));
        expect(backlogBusiness.getStoryPointSumByIteration(iter)).andReturn(10);
        expect(backlogBusiness.calculateDoneStoryPointSum(iter.getId())).andReturn(5);
        expect(hourEntryBusiness.calculateSumOfIterationsHourEntries(iter))
                .andReturn((long) 10);
        expect(iterationDAO.getCountOfDoneAndNonDeferred(iter)).andReturn(
                Pair.create(2, 4));
        expect(iterationDAO.getCountOfDoneAndAllStories(iter)).andReturn(
                Pair.create(1, 2));
        expect(
                iterationHistoryEntryDAO.retrieveByDate(100, new LocalDate()
                        .minusDays(1))).andReturn(null).times(1);
        expect(backlogBusiness.getStoryValueSumByIteration(iter)).andReturn(0);
        expect(backlogBusiness.getCompletedStoryValueSumByIteration(iter)).andReturn(0);
        replayAll();
        IterationMetrics iterRow = iterationBusiness.getIterationMetrics(iter);
        assertEquals(100, iterRow.getDaysLeft());
        assertEquals(10, iterRow.getEffortLeft().intValue());
        assertEquals(10, iterRow.getOriginalEstimate().intValue());
        assertEquals(5, iterRow.getDoneStoryPoints().intValue());
        verifyAll();
    }
    

    @Test(expected = IllegalArgumentException.class)
    public void testGetIterationMetrics_nullIteration() {
        iterationBusiness.getIterationMetrics(null);
    }

    @Test
    @DirtiesContext
    public void testStoreIteration() {
        DateTime start = new DateTime();
        DateTime end = start.plusDays(3);
        Iteration iter = new Iteration();
        iter.setStartDate(start);
        iter.setEndDate(end);
        iter.setParent(project);
        iteration.setParent(project);
        iter.setBacklogSize(new ExactEstimate(120L));
        iter.setName("foo");
        iter.setDescription("quu");
        iter.setBaselineLoad(new ExactEstimate(100L));

        expect(backlogBusiness.retrieve(11)).andReturn(project);
        expect(iterationDAO.get(10)).andReturn(iteration);
        iterationDAO.store(iteration);
        expect(transferObjectBusiness.constructIterationTO(EasyMock.isA(Iteration.class)))
            .andReturn(new IterationTO(iter));

        replayAll();

        Iteration actual = this.iterationBusiness.store(10, 11, iter, null, null);
        assertEquals(iter.getStartDate(), actual.getStartDate());
        assertEquals(iter.getEndDate(), actual.getEndDate());
        assertEquals(iter.getBacklogSize(), actual.getBacklogSize());
        assertEquals(iter.getBaselineLoad(), actual.getBaselineLoad());
        assertEquals(iter.getName(), actual.getName());
        assertEquals(iter.getDescription(), actual.getDescription());
        verifyAll();

    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testStoreIteration_iterationParent() {
        expect(backlogBusiness.retrieve(11)).andReturn(iteration);
        replayAll();
        this.iterationBusiness.store(10, 11, this.iteration, null, null);
        verifyAll();
    }

    @Test(expected = ObjectNotFoundException.class)
    @DirtiesContext
    public void testStoreIteration_nullParent() {
        expect(backlogBusiness.retrieve(11)).andThrow(new ObjectNotFoundException());
        replayAll();
        this.iterationBusiness.store(10, 11, this.iteration, null, null);
        verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testStoreIteration_invalidInterval() {
        DateTime start = new DateTime();
        DateTime end = start.minusDays(3);
        iteration.setStartDate(start);
        iteration.setEndDate(end);

        expect(backlogBusiness.retrieve(12)).andReturn(project);
        replayAll();
        this.iterationBusiness.store(11, 12, iteration, null, null);
        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testCreateIteration() {
        DateTime start = new DateTime();
        DateTime end = start.plusDays(3);
        Iteration iter = new Iteration();
        iter.setStartDate(start);
        iter.setEndDate(end);
        iter.setParent(project);
        iteration.setParent(project);
        iter.setBacklogSize(new ExactEstimate(120L));
        iter.setName("foo");
        iter.setDescription("quu");
        iter.setBaselineLoad(new ExactEstimate(100L));
        User user = new User();
        user.setId(1);
        Assignment assignment = new Assignment();
        
        Capture<Set<Integer>> userIdCapture = new Capture<Set<Integer>>();
        expect(backlogBusiness.retrieve(11)).andReturn(project);
        expect(iterationDAO.create(EasyMock.isA(Iteration.class))).andReturn(new Integer(16));
        expect(iterationDAO.get(16)).andReturn(iteration);
        expect(assignmentBusiness.addMultiple(EasyMock.eq(iteration), 
                EasyMock.capture(userIdCapture))).andReturn(new HashSet<Assignment>(Arrays.asList(assignment)));
        expect(transferObjectBusiness.constructIterationTO(iteration))
                .andReturn(new IterationTO(iteration));
        
        replayAll();
        
        this.iterationBusiness.store(0, 11, iter, new HashSet<Integer>(Arrays.asList(1)), null);
        assertEquals(1, userIdCapture.getValue().size());
        assertTrue(userIdCapture.getValue().contains(1));
        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testCreateIteration_noAssigments() {
        DateTime start = new DateTime();
        DateTime end = start.plusDays(3);
        Iteration iter = new Iteration();
        iter.setStartDate(start);
        iter.setEndDate(end);
        iter.setParent(project);
        iteration.setParent(project);        
        expect(backlogBusiness.retrieve(11)).andReturn(project);
        expect(iterationDAO.create(EasyMock.isA(Iteration.class))).andReturn(new Integer(16));
        expect(iterationDAO.get(16)).andReturn(iteration);
        expect(transferObjectBusiness.constructIterationTO(iteration))
            .andReturn(new IterationTO(iteration));
        replayAll();
        this.iterationBusiness.store(0, 11, iter, null, null);
        verifyAll();
    }
    @Test
    @DirtiesContext
    public void testMoveTo() {
        Project newParent = new Project();
        newParent.setId(911);
        iteration.setParent(project);
        iterationDAO.store(iteration);
        backlogHistoryEntryBusiness.updateHistory(911);
        backlogHistoryEntryBusiness.updateHistory(313);
        replayAll();
        this.iterationBusiness.moveTo(iteration, newParent);
        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testCalculateVariance() {
        Iteration iter = new Iteration();
        LocalDate today = new LocalDate();
        iter.setId(100);
        iter.setStartDate(today.minusDays(10).toDateMidnight().toDateTime());
        iter.setEndDate(iter.getStartDate().plusDays(100));
        IterationHistoryEntry latestHistoryEntry = new IterationHistoryEntry();
        IterationHistoryEntry yesterdayHistoryEntry = new IterationHistoryEntry();
        yesterdayHistoryEntry.setEffortLeftSum(60);
        yesterdayHistoryEntry.setOriginalEstimateSum(70);
        latestHistoryEntry.setEffortLeftSum(60);
        
        expect(iterationHistoryEntryBusiness.retrieveLatest(iter)).andReturn(latestHistoryEntry);    
        expect(backlogBusiness.daysLeftInSchedulableBacklog(iter)).andReturn(Days.days(90));
        expect(iterationHistoryEntryDAO.retrieveByDate(iter.getId(), today.minusDays(1))).andReturn(yesterdayHistoryEntry);
        replayAll();
        assertEquals((Integer)(-30), iterationBusiness.calculateVariance(iter));
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testCalculateVariance_notStarted() {
        Iteration iter = new Iteration();
        LocalDate today = new LocalDate();
        iter.setId(100);
        iter.setStartDate(today.plusDays(10).toDateMidnight().toDateTime());
        iter.setEndDate(iter.getStartDate().plusDays(100));
        IterationHistoryEntry latestHistoryEntry = new IterationHistoryEntry();
        IterationHistoryEntry yesterdayHistoryEntry = new IterationHistoryEntry();
        yesterdayHistoryEntry.setEffortLeftSum(60);
        yesterdayHistoryEntry.setOriginalEstimateSum(70);
        latestHistoryEntry.setEffortLeftSum(60);
        
        expect(iterationHistoryEntryBusiness.retrieveLatest(iter)).andReturn(latestHistoryEntry);    
        expect(iterationHistoryEntryDAO.retrieveByDate(iter.getId(), today.minusDays(1))).andReturn(yesterdayHistoryEntry);
        replayAll();
        assertEquals(null, iterationBusiness.calculateVariance(iter));
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testCalculateVariance_noEffortLeft() {
        Iteration iter = new Iteration();
        LocalDate today = new LocalDate();
        iter.setId(100);
        iter.setStartDate(today.minusDays(10).toDateMidnight().toDateTime());
        iter.setEndDate(iter.getStartDate().plusDays(100));
        IterationHistoryEntry latestHistoryEntry = new IterationHistoryEntry();
        IterationHistoryEntry yesterdayHistoryEntry = new IterationHistoryEntry();
        yesterdayHistoryEntry.setEffortLeftSum(0);
        yesterdayHistoryEntry.setOriginalEstimateSum(30);
        latestHistoryEntry.setEffortLeftSum(0);
        
        expect(iterationHistoryEntryBusiness.retrieveLatest(iter)).andReturn(latestHistoryEntry);    
        expect(iterationHistoryEntryDAO.retrieveByDate(iter.getId(), today.minusDays(1))).andReturn(yesterdayHistoryEntry);
        replayAll();
        assertEquals(null, iterationBusiness.calculateVariance(iter));
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testDeleteIteration() {
        Iteration iter = new Iteration();
        Project project = new Project();
        iter.setParent(project);
        iter.setId(111);
        Story story = new Story();
        Set<Story> stories = new HashSet<Story>();
        stories.add(story);
        iter.setAssignedStories(stories);
        Set<Task> tasks = new HashSet<Task>();
        Task task = new Task();
        tasks.add(task);
        iter.setTasks(tasks);
        Set<Assignment> assignments = new HashSet<Assignment>();
        Assignment assignment = new Assignment();
        assignments.add(assignment);
        iter.setAssignments(assignments);
        Set<IterationHistoryEntry> historyEntries = new HashSet<IterationHistoryEntry>();
        IterationHistoryEntry historyEntry = new IterationHistoryEntry();
        historyEntries.add(historyEntry);
        iter.setHistoryEntries(historyEntries);
        Set<BacklogHourEntry> hourEntries = new HashSet<BacklogHourEntry>();
        BacklogHourEntry hourEntry = new BacklogHourEntry();
        hourEntries.add(hourEntry);
        iter.setHourEntries(hourEntries);
        
        expect(iterationDAO.get(iter.getId())).andReturn(iter);
        
        storyRankBusiness.removeBacklogRanks(iter);
        
        storyBusiness.forceDelete(story);
        iterationHistoryEntryBusiness.delete(historyEntry.getId());
        assignmentBusiness.delete(assignment.getId());
        taskBusiness.delete(task.getId(), HourEntryHandlingChoice.DELETE);
        hourEntryBusiness.deleteAll(iter.getHourEntries());
        
        iterationDAO.remove(iter);
        
        replayAll();
        iterationBusiness.delete(iter.getId());
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testDeleteStandaloneIteration() {
        Iteration iter = new Iteration();
        iter.setId(112);
        Story story = new Story();
        Set<Story> stories = new HashSet<Story>();
        stories.add(story);
        iter.setAssignedStories(stories);
        Set<Task> tasks = new HashSet<Task>();
        Task task = new Task();
        tasks.add(task);
        iter.setTasks(tasks);
        Set<Assignment> assignments = new HashSet<Assignment>();
        Assignment assignment = new Assignment();
        assignments.add(assignment);
        iter.setAssignments(assignments);
        Set<IterationHistoryEntry> historyEntries = new HashSet<IterationHistoryEntry>();
        IterationHistoryEntry historyEntry = new IterationHistoryEntry();
        historyEntries.add(historyEntry);
        iter.setHistoryEntries(historyEntries);
        Set<BacklogHourEntry> hourEntries = new HashSet<BacklogHourEntry>();
        BacklogHourEntry hourEntry = new BacklogHourEntry();
        hourEntries.add(hourEntry);
        iter.setHourEntries(hourEntries);
        
        expect(iterationDAO.get(iter.getId())).andReturn(iter);
        
        storyRankBusiness.removeBacklogRanks(iter);
        
        storyBusiness.forceDelete(story);
        iterationHistoryEntryBusiness.delete(historyEntry.getId());
        assignmentBusiness.delete(assignment.getId());
        taskBusiness.delete(task.getId(), HourEntryHandlingChoice.DELETE);
        hourEntryBusiness.deleteAll(iter.getHourEntries());
        
        iterationDAO.remove(iter);
        
        replayAll();
        iterationBusiness.delete(iter.getId());
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testdeleteAndUpdateHistory() {
        Iteration iteration = new Iteration();
        iteration.setId(111);
        Project project = new Project();
        iteration.setParent(project);
        project.setId(10);
        expect(iterationDAO.get(iteration.getId())).andReturn(iteration);
        
        storyRankBusiness.removeBacklogRanks(iteration);
        
        hourEntryBusiness.deleteAll(iteration.getHourEntries());
        iterationDAO.remove(iteration);
        backlogHistoryEntryBusiness.updateHistory(project.getId());
        replayAll();
        iterationBusiness.deleteAndUpdateHistory(111);
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testRetrieveChangesInIterationStories() {
        AgilefantRevisionEntity revDeleted = new AgilefantRevisionEntity();
        revDeleted.setId(1200);
        revDeleted.setTimestamp(200);
        AgilefantHistoryEntry deleted = new AgilefantHistoryEntry(200, RevisionType.DEL, revDeleted);
        
        AgilefantRevisionEntity revAdded = new AgilefantRevisionEntity();
        revAdded.setTimestamp(10000);
        AgilefantHistoryEntry added = new AgilefantHistoryEntry(new Story(), revAdded, RevisionType.ADD);
        
        AgilefantRevisionEntity revModified = new AgilefantRevisionEntity();
        revModified.setId(2400);
        revModified.setTimestamp(20000);
        AgilefantHistoryEntry modified = new AgilefantHistoryEntry(20000, RevisionType.MOD, revModified);
        
        expect(backlogHistoryDAO.retrieveAddedStories(iteration)).andReturn(Arrays.asList(added));
        expect(backlogHistoryDAO.retrieveDeletedStories(iteration)).andReturn(Arrays.asList(deleted));
        expect(backlogHistoryDAO.retrieveModifiedStories(iteration)).andReturn(Arrays.asList(modified));
        expect(storyHistoryDAO.retrieveClosestRevision(200, 1200)).andReturn(null);
        expect(storyHistoryDAO.retrieveClosestRevision(20000, 2400)).andReturn(null);
        
        replayAll();
        List<AgilefantHistoryEntry> actual = this.iterationBusiness.retrieveChangesInIterationStories(iteration);
        verifyAll();
        assertEquals(modified, actual.get(0));
        assertEquals(added, actual.get(1));
        assertEquals(deleted, actual.get(2));
    }
}
