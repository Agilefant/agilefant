package fi.hut.soberit.agilefant.business;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.hut.soberit.agilefant.business.impl.RankingBusinessImpl;
import fi.hut.soberit.agilefant.business.impl.TaskBusinessImpl;
import fi.hut.soberit.agilefant.db.TaskDAO;
import fi.hut.soberit.agilefant.exception.ObjectNotFoundException;
import fi.hut.soberit.agilefant.exception.OperationNotPermittedException;
import fi.hut.soberit.agilefant.model.ExactEstimate;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryState;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.TaskHourEntry;
import fi.hut.soberit.agilefant.model.TaskState;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.test.Mock;
import fi.hut.soberit.agilefant.test.MockContextLoader;
import fi.hut.soberit.agilefant.test.MockedTestCase;
import fi.hut.soberit.agilefant.test.TestedBean;
import fi.hut.soberit.agilefant.util.HourEntryHandlingChoice;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = MockContextLoader.class)
public class TaskBusinessTest extends MockedTestCase {

    @TestedBean
    private TaskBusinessImpl taskBusiness;
    @Mock(strict = true)
    private IterationBusiness iterationBusiness;
    @Mock(strict = true)
    private IterationHistoryEntryBusiness iterationHistoryEntryBusiness;
    @Mock(strict = true)
    private HourEntryBusiness hourEntryBusiness;
    @Mock(strict = true)
    private StoryBusiness storyBusiness;
    @Mock(strict=true)
    private TaskDAO taskDAO;
    @TestedBean
    private RankingBusinessImpl rankingBusiness;
    @Mock
    private TaskCommentBusiness taskCommentBusiness;

    private Iteration iteration;
    private Story story;
    private Task task;
    private User loggedInUser;

    @Mock(strict=true)
    private DailyWorkBusiness dailyWorkBusiness;

    @Before
    public void setUp() {
        task = new Task();
        iteration = new Iteration();
        iteration.setId(2);
        story = new Story();
        story.setId(123);
        task.setId(0);

        loggedInUser = new User();
        loggedInUser.setId(666);
    }

    /*
     * TEST STORING.
     */

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testStoreTask_storyAndIterationGiven() {
        taskBusiness.storeTask(task, iteration.getId(), story.getId(), false);
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testStoreTask_nullTask() {
        taskBusiness.storeTask(null, iteration.getId(), null, false);
    }

    /**
     * Helper method for testing that the ranking method is called.
     */
    private void expectRankToBottom(Task rankable, Story story,
            Iteration iteration) {
        Task lastTask = new Task();
        lastTask.setRank(11);

        if (story != null) {
            expect(storyBusiness.retrieve(story.getId())).andReturn(story);
        } else if (iteration != null) {
            expect(iterationBusiness.retrieve(iteration.getId())).andReturn(
                    iteration);
        }

        expect(taskDAO.getLastTaskInRank(story, iteration)).andReturn(lastTask);
        rankingBusiness.rankToBottom(rankable, lastTask);
    }

    @Test
    @DirtiesContext
    public void testStoreTask_newTaskToIteration() {
        Task lastTask = new Task();
        lastTask.setRank(22);

        expect(iterationBusiness.retrieve(iteration.getId())).andReturn(
                iteration).anyTimes();
        expect(taskDAO.create(task)).andReturn(1351);
        expect(taskDAO.get(1351)).andReturn(task);
        expect(taskDAO.getLastTaskInRank(null, iteration)).andReturn(lastTask);
        iterationHistoryEntryBusiness.updateIterationHistory(iteration.getId());

        replayAll();

        Task actualTask = taskBusiness.storeTask(task, iteration.getId(), null,
                false);

        assertEquals(iteration, actualTask.getIteration());
        assertEquals(23, actualTask.getRank());

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testStoreTask_newTaskToStory() {
        Task lastTask = new Task();
        lastTask.setStory(story);
        lastTask.setRank(222);

        story.setIteration(iteration);

        expect(storyBusiness.retrieve(story.getId())).andReturn(story)
                .anyTimes();
        expect(taskDAO.create(task)).andReturn(1351);
        expect(taskDAO.get(1351)).andReturn(task);
        expect(taskDAO.getLastTaskInRank(story, null)).andReturn(lastTask);
        iterationHistoryEntryBusiness.updateIterationHistory(iteration.getId());

        replayAll();

        Task actualTask = taskBusiness.storeTask(task, null, story.getId(),
                false);

        assertEquals(story, actualTask.getStory());
        assertEquals(223, actualTask.getRank());

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testStoreTask_dontChangeParent() {
        task.setId(54326);
        task.setIteration(iteration);

        expect(taskDAO.getAndDetach(task.getId())).andReturn(task);
        taskDAO.store(task);
        iterationHistoryEntryBusiness.updateIterationHistory(iteration.getId());

        replayAll();

        Task actualTask = taskBusiness.storeTask(task, null, null, false);

        assertEquals(task.getId(), actualTask.getId());

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testStoreTask_existingTask() {
        task.setId(54326);
        task.setIteration(new Iteration());

        expect(taskDAO.getAndDetach(task.getId())).andReturn(task);
        expect(iterationBusiness.retrieve(iteration.getId())).andReturn(
                iteration);
        taskDAO.store(task);

        expectRankToBottom(task, null, iteration);

        iterationHistoryEntryBusiness.updateIterationHistory(iteration.getId());

        replayAll();

        Task actualTask = taskBusiness.storeTask(task, iteration.getId(), null,
                false);

        assertEquals(task.getId(), actualTask.getId());

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testStoreTask_existingTaskStateSetToDone() {
        task.setId(54326);
        task.setState(TaskState.DONE);
        expect(taskDAO.getAndDetach(task.getId())).andReturn(task);
        expect(iterationBusiness.retrieve(iteration.getId())).andReturn(
                iteration);
        taskDAO.store(task);
        expectRankToBottom(task, null, iteration);
        dailyWorkBusiness.removeTaskFromWorkQueues(task);
        iterationHistoryEntryBusiness.updateIterationHistory(iteration.getId());

        replayAll();

        taskBusiness.storeTask(task, iteration.getId(), null, false);

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testStoreTask_updateEffortLeftWhenSettingOriginalEstimate() {
        task.setId(12);
        task.setEffortLeft(null);
        task.setOriginalEstimate(new ExactEstimate(120));

        expect(taskDAO.getAndDetach(task.getId())).andReturn(task);
        expect(storyBusiness.retrieve(story.getId())).andReturn(story);
        taskDAO.store(task);
        expectRankToBottom(task, story, null);

        replayAll();

        Task actualTask = taskBusiness.storeTask(task, null, story.getId(),
                false);

        assertEquals(new ExactEstimate(120).getMinorUnits(), actualTask
                .getOriginalEstimate().getMinorUnits());
        assertEquals(new ExactEstimate(120).getMinorUnits(), actualTask
                .getEffortLeft().getMinorUnits());

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testStoreTask_updateNullOriginalEstimateWhenSettingEffortLeft() {
        task.setId(12);
        task.setEffortLeft(new ExactEstimate(90));
        task.setOriginalEstimate(null);

        expect(taskDAO.getAndDetach(task.getId())).andReturn(task);
        expect(storyBusiness.retrieve(story.getId())).andReturn(story);
        taskDAO.store(task);
        expectRankToBottom(task, story, null);

        replayAll();

        Task actualTask = taskBusiness.storeTask(task, null, story.getId(),
                false);

        assertEquals(new ExactEstimate(90).getMinorUnits(), actualTask
                .getOriginalEstimate().getMinorUnits());
        assertEquals(new ExactEstimate(90).getMinorUnits(), actualTask
                .getEffortLeft().getMinorUnits());

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testStoreTask_responsibles() {
        task.setId(123515);
        task.setIteration(iteration);

        expect(taskDAO.getAndDetach(task.getId())).andReturn(task);
        taskDAO.store(task);
        iterationHistoryEntryBusiness.updateIterationHistory(iteration.getId());

        replayAll();

        taskBusiness.storeTask(task, null, null, false);

        verifyAll();
    }

    @Test(expected = ObjectNotFoundException.class)
    @DirtiesContext
    public void testStoreTask_nonExistentIteration() {
        expect(iterationBusiness.retrieve(0)).andThrow(
                new ObjectNotFoundException("Iteration not found"));
        replayAll();

        taskBusiness.storeTask(task, 0, null, false);

        verifyAll();
    }

    @Test(expected = ObjectNotFoundException.class)
    @DirtiesContext
    public void testStoreTask_nonExistentStory() {
        expect(storyBusiness.retrieve(0)).andThrow(
                new ObjectNotFoundException("Story not found"));
        replayAll();

        taskBusiness.storeTask(task, null, 0, false);

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testStoreTask_storyToStarted() {
        task.setId(12);
        story.setState(StoryState.NOT_STARTED);

        Task task2 = new Task();
        task2.setState(TaskState.NOT_STARTED);

        expect(taskDAO.getAndDetach(task.getId())).andReturn(task2);

        expect(storyBusiness.retrieve(story.getId())).andReturn(story);
        taskDAO.store(task);
        expectRankToBottom(task, story, null);

        replayAll();

        taskBusiness.storeTask(task, null, story.getId(), true);

        verifyAll();

        assertEquals(StoryState.STARTED, story.getState());
    }

    @Test
    @DirtiesContext
    public void testStoreTask_doneStoryToStarted() {
        task.setId(12);
        story.setState(StoryState.DONE);

        Task task2 = new Task();
        task2.setState(TaskState.NOT_STARTED);

        expect(taskDAO.getAndDetach(task.getId())).andReturn(task2);

        expect(storyBusiness.retrieve(story.getId())).andReturn(story);
        taskDAO.store(task);
        expectRankToBottom(task, story, null);

        replayAll();

        taskBusiness.storeTask(task, null, story.getId(), true);

        verifyAll();

        assertEquals(StoryState.DONE, story.getState());
    }

    @Test
    @DirtiesContext
    public void testStoreTask_StartedTaskStoryToDone() {
        task.setId(12);
        task.setState(TaskState.STARTED);
        story.setState(StoryState.NOT_STARTED);
        Task task2 = new Task();
        task2.setState(TaskState.STARTED);

        expect(taskDAO.getAndDetach(task.getId())).andReturn(task2);
        expect(storyBusiness.retrieve(story.getId())).andReturn(story);
        taskDAO.store(task);
        expectRankToBottom(task, story, null);

        replayAll();

        taskBusiness.storeTask(task, null, story.getId(), true);

        verifyAll();

        assertEquals(StoryState.NOT_STARTED, story.getState());
    }

    /*
     * TEST ORIGINAL ESTIMATE RESETING
     */

    @Test
    @DirtiesContext
    public void testResetOriginalEstimate_underIteration() {
        task.setEffortLeft(new ExactEstimate());
        task.setOriginalEstimate(new ExactEstimate());
        task.setIteration(iteration);
        task.setStory(null);
        expect(taskDAO.get(task.getId())).andReturn(task);
        taskDAO.store(task);

        iterationHistoryEntryBusiness.updateIterationHistory(iteration.getId());

        replayAll();

        Task returnedTask = taskBusiness.resetOriginalEstimate(task.getId());

        assertNull(returnedTask.getEffortLeft());
        assertNull(returnedTask.getOriginalEstimate());

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testResetOriginalEstimate_underStory() {
        task.setEffortLeft(new ExactEstimate());
        task.setOriginalEstimate(new ExactEstimate());
        task.setStory(story);
        story.setIteration(iteration);
        task.setIteration(null);
        expect(taskDAO.get(task.getId())).andReturn(task);
        taskDAO.store(task);

        iterationHistoryEntryBusiness.updateIterationHistory(iteration.getId());

        replayAll();

        Task returnedTask = taskBusiness.resetOriginalEstimate(task.getId());

        assertNull(returnedTask.getEffortLeft());
        assertNull(returnedTask.getOriginalEstimate());

        verifyAll();
    }

    @Test(expected = ObjectNotFoundException.class)
    @DirtiesContext
    public void testResetOriginalEstimate_nonExistentTask() {
        task.setEffortLeft(new ExactEstimate());
        task.setOriginalEstimate(new ExactEstimate());
        expect(taskDAO.get(task.getId())).andReturn(null);

        replayAll();

        taskBusiness.resetOriginalEstimate(task.getId());

        verifyAll();
    }

    /*
     * TEST DELETION
     */

    @Test
    @DirtiesContext
    public void testDeleteAndUpdateHistory_underIteration() {
        task.setStory(null);
        task.setIteration(iteration);

        expect(taskDAO.get(task.getId())).andReturn(task);
        taskDAO.remove(task.getId());
        iterationHistoryEntryBusiness.updateIterationHistory(iteration.getId());
        replayAll();

        taskBusiness.deleteAndUpdateHistory(task.getId(), null);

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testDeleteAndUpdateHistory_underIterationStory() {
        task.setStory(story);
        task.setIteration(null);
        story.setIteration(iteration);

        expect(taskDAO.get(task.getId())).andReturn(task);
        taskDAO.remove(task.getId());
        iterationHistoryEntryBusiness.updateIterationHistory(iteration.getId());
        replayAll();

        taskBusiness.deleteAndUpdateHistory(task.getId(), null);

        verifyAll();
    }

    @Test(expected = OperationNotPermittedException.class)
    @DirtiesContext
    public void testDelete_containsHourEntries() {
        TaskHourEntry he = new TaskHourEntry();
        Task task = new Task();
        task.getHourEntries().add(he);
        taskBusiness.delete(task);
    }

    @Test
    @DirtiesContext
    public void testDelete_underProductStory() {
        task.setStory(story);
        task.setIteration(null);
        story.setBacklog(new Product());

        taskDAO.remove(task.getId());
        replayAll();

        taskBusiness.delete(task);

        verifyAll();
    }

    /*
     * TEST ASSIGNING PARENT
     */

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testAssignParentForTask_nullTask() {
        taskBusiness.assignParentForTask(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testAssignParentForTask_bothIdsNull() {
        taskBusiness.assignParentForTask(task, null, null);
    }

    @Test
    @DirtiesContext
    public void testAssignParentForTask_storyIdGiven() {
        expect(storyBusiness.retrieve(story.getId())).andReturn(story);
        replayAll();

        taskBusiness.assignParentForTask(task, null, story.getId());

        assertEquals(story, task.getStory());
        assertNull(task.getIteration());

        verifyAll();
    }

    @Test(expected = ObjectNotFoundException.class)
    @DirtiesContext
    public void testAssignParentForTask_nonExistentStoryIdGiven() {
        expect(storyBusiness.retrieve(-1)).andThrow(
                new ObjectNotFoundException());
        replayAll();

        taskBusiness.assignParentForTask(task, null, -1);

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testAssignParentForTask_iterationIdGiven() {
        expect(iterationBusiness.retrieve(iteration.getId())).andReturn(
                iteration);
        replayAll();

        taskBusiness.assignParentForTask(task, iteration.getId(), null);

        assertEquals(iteration, task.getIteration());
        assertNull(task.getStory());

        verifyAll();
    }

    @Test(expected = ObjectNotFoundException.class)
    @DirtiesContext
    public void testAssignParentForTask_nonExistentIterationIdGiven() {
        expect(iterationBusiness.retrieve(-1)).andThrow(
                new ObjectNotFoundException());
        replayAll();

        taskBusiness.assignParentForTask(task, -1, null);

        verifyAll();
    }

    @Test(expected = ObjectNotFoundException.class)
    @DirtiesContext
    public void testAssignParentForTask_backlogNotIteration() {
        expect(iterationBusiness.retrieve(123)).andThrow(
                new ObjectNotFoundException());
        replayAll();

        taskBusiness.assignParentForTask(task, 123, null);

        verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testAssignParentForTask_bothIdsGiven() {
        taskBusiness.assignParentForTask(task, 123, 456);
    }

    /*
     * TEST MOVING TASKS.
     * 
     * Moving tasks should update both the source and destination backlogs'
     * histories.
     */

    @Test
    @DirtiesContext
    public void testMove_fromIterationToIteration() {
        Iteration source = new Iteration();
        source.setId(222);
        Iteration destination = new Iteration();
        destination.setId(666);

        task.setIteration(source);

        expect(iterationBusiness.retrieve(destination.getId())).andReturn(
                destination);
        taskDAO.store(task);
        iterationHistoryEntryBusiness.updateIterationHistory(source.getId());
        iterationHistoryEntryBusiness.updateIterationHistory(destination
                .getId());

        replayAll();

        taskBusiness.move(task, destination.getId(), null);

        assertEquals(destination, task.getIteration());
        assertNull(task.getStory());

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testMove_fromIterationStoryToIterationStory() {
        Iteration iter = new Iteration();
        iter.setId(654);
        Iteration iter2 = new Iteration();
        iter2.setId(999);

        Story source = new Story();
        source.setId(397);
        source.setIteration(iter);

        Story destination = new Story();
        destination.setId(1223);
        destination.setIteration(iter2);

        task.setStory(source);

        expect(storyBusiness.retrieve(destination.getId())).andReturn(
                destination);
        taskDAO.store(task);
        iterationHistoryEntryBusiness.updateIterationHistory(iter.getId());
        iterationHistoryEntryBusiness.updateIterationHistory(iter2.getId());

        replayAll();

        taskBusiness.move(task, null, destination.getId());

        assertEquals(destination, task.getStory());
        assertNull(task.getIteration());

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testMove_fromProjectStoryToIterationStory() {
        Project proj = new Project();
        proj.setId(654);
        Iteration iter2 = new Iteration();
        iter2.setId(999);

        Story source = new Story();
        source.setId(397);
        source.setBacklog(proj);

        Story destination = new Story();
        destination.setId(1223);
        destination.setIteration(iter2);

        task.setStory(source);

        expect(storyBusiness.retrieve(destination.getId())).andReturn(
                destination);
        taskDAO.store(task);
        iterationHistoryEntryBusiness.updateIterationHistory(iter2.getId());

        replayAll();

        taskBusiness.move(task, null, destination.getId());

        assertEquals(destination, task.getStory());
        assertNull(task.getIteration());

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testMove_fromIterationToProject() {
        Project proj = new Project();
        proj.setId(654);
        Iteration iter = new Iteration();
        iter.setId(999);

        Story source = new Story();
        source.setId(397);
        source.setIteration(iter);

        Story destination = new Story();
        destination.setId(1223);
        destination.setBacklog(proj);

        task.setStory(source);

        expect(storyBusiness.retrieve(destination.getId())).andReturn(
                destination);
        taskDAO.store(task);
        iterationHistoryEntryBusiness.updateIterationHistory(iter.getId());

        replayAll();

        taskBusiness.move(task, null, destination.getId());

        assertEquals(destination, task.getStory());
        assertNull(task.getIteration());

        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testMove_toStoryUnderSameIteration() {
        Iteration iter = new Iteration();

        Story source = new Story();
        source.setId(397);
        source.setIteration(iter);

        Story destination = new Story();
        destination.setId(1223);
        destination.setIteration(iter);

        task.setStory(source);

        expect(storyBusiness.retrieve(destination.getId())).andReturn(
                destination);
        taskDAO.store(task);
        replayAll();

        taskBusiness.move(task, null, destination.getId());

        assertEquals(destination, task.getStory());
        assertNull(task.getIteration());

        verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testMove_nullTask() {
        taskBusiness.move(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testMove_bothParentIdsNull() {
        taskBusiness.move(task, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testMove_bothIdsGiven() {
        taskBusiness.move(task, 123, 456);
    }

    /*
     * RANKING
     */

    Story rankParentStory;

    Task firstTaskInRank;
    Task secondTaskInRank;
    Task thirdTaskInRank;
    Task fourthTaskInRank;

    @Before
    public void setUp_ranking() {
        firstTaskInRank = new Task();
        secondTaskInRank = new Task();
        thirdTaskInRank = new Task();
        fourthTaskInRank = new Task();

        firstTaskInRank.setId(111);
        secondTaskInRank.setId(222);
        thirdTaskInRank.setId(333);
        fourthTaskInRank.setId(444);

        firstTaskInRank.setRank(0);
        secondTaskInRank.setRank(1);
        thirdTaskInRank.setRank(5);
        fourthTaskInRank.setRank(6);

        rankParentStory = new Story();
        rankParentStory.setId(22);

        firstTaskInRank.setStory(rankParentStory);
        secondTaskInRank.setStory(rankParentStory);
        thirdTaskInRank.setStory(rankParentStory);
        fourthTaskInRank.setStory(rankParentStory);
    }

    private void checkRanks(int first, int second, int third, int fourth) {
        assertEquals("First rank does not match", first, firstTaskInRank
                .getRank());
        assertEquals("Second rank does not match", second, secondTaskInRank
                .getRank());
        assertEquals("Third rank does not match", third, thirdTaskInRank
                .getRank());
        assertEquals("Fourth rank does not match", fourth, fourthTaskInRank
                .getRank());
    }

    @Test
    @DirtiesContext
    public void testRankUnderTask_twoUpwards() {
        Collection<Task> returnedTasksWithRankBetween = Arrays.asList(
                secondTaskInRank, thirdTaskInRank);

        expect(taskDAO.getTasksWithRankBetween(1, 5, null, rankParentStory))
                .andReturn(returnedTasksWithRankBetween);

        replayAll();
        Task actual = taskBusiness.rankUnderTask(fourthTaskInRank,
                firstTaskInRank);
        verifyAll();

        checkRanks(0, 2, 6, 1);
        assertSame(fourthTaskInRank, actual);
    }

    @Test
    @DirtiesContext
    public void testRankUnderTask_twoDownwards() {
        expect(taskDAO.getTasksWithRankBetween(1, 5, null, rankParentStory))
                .andReturn(Arrays.asList(secondTaskInRank, thirdTaskInRank));
        replayAll();
        taskBusiness.rankUnderTask(firstTaskInRank, thirdTaskInRank);
        verifyAll();
        checkRanks(5, 0, 4, 6);
    }

    @Test
    @DirtiesContext
    public void testRankUnderTask_toTop() {
        expect(taskDAO.getTasksWithRankBetween(0, 4, null, rankParentStory))
                .andReturn(
                        Arrays.asList(firstTaskInRank, secondTaskInRank,
                                thirdTaskInRank));
        replayAll();
        taskBusiness.rankUnderTask(thirdTaskInRank, null);
        verifyAll();
        checkRanks(1, 2, 0, 6);
    }

    @Test
    @DirtiesContext
    public void testRankUnderTask_toBottom() {
        expect(taskDAO.getTasksWithRankBetween(1, 6, null, rankParentStory))
                .andReturn(
                        Arrays.asList(secondTaskInRank, thirdTaskInRank,
                                fourthTaskInRank));
        replayAll();
        taskBusiness.rankUnderTask(firstTaskInRank, fourthTaskInRank);
        verifyAll();
        checkRanks(6, 0, 4, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testRankUnderTask_nullTaskGiven() {
        replayAll();
        taskBusiness.rankUnderTask(null, null);
        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testRankUnderTask_checkUnderSameIteration() {
        Task first = new Task();
        Task second = new Task();
        Iteration iter = new Iteration();

        first.setIteration(iter);
        second.setIteration(iter);

        expect(taskDAO.getTasksWithRankBetween(1, 0, iter, null)).andReturn(
                new ArrayList<Task>());
        replayAll();
        taskBusiness.rankUnderTask(first, second);
        verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testRankUnderTask_noStory_differentIteration() {
        Task first = new Task();
        Task second = new Task();

        first.setIteration(new Iteration());
        second.setIteration(new Iteration());

        replayAll();
        taskBusiness.rankUnderTask(first, second);
        verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testRankUnderTask_tasksNotUnderSameStory() {
        Task newTask = new Task();
        newTask.setStory(new Story());

        replayAll();
        taskBusiness.rankUnderTask(firstTaskInRank, newTask);
        verifyAll();
    }

    /*
     * RANK TO BOTTOM
     */
    @Test
    @DirtiesContext
    public void testRankToBottom_story() {
        Task last = new Task();
        last.setRank(230);
        Story story = new Story();
        expect(storyBusiness.retrieve(22)).andReturn(story);
        expect(taskDAO.getLastTaskInRank(story, null)).andReturn(last);
        replayAll();
        Task actual = taskBusiness.rankToBottom(task, 22, null);
        verifyAll();
        assertEquals(task.getId(), actual.getId());
        assertEquals(231, actual.getRank());
    }

    @Test
    @DirtiesContext
    public void testRankToBottom_iteration() {
        Task last = new Task();
        last.setRank(22);
        Iteration iter = new Iteration();
        expect(iterationBusiness.retrieve(561)).andReturn(iter);
        expect(taskDAO.getLastTaskInRank(null, iter)).andReturn(last);
        replayAll();
        Task actual = taskBusiness.rankToBottom(task, null, 561);
        verifyAll();
        assertEquals(task.getId(), actual.getId());
        assertEquals(23, actual.getRank());
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testRankToBottom_noTaskGiven() {
        taskBusiness.rankToBottom(null, null, 561);
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testRankToBottom_noParentGiven() {
        taskBusiness.rankToBottom(task, null, null);
    }

    /*
     * RANK AND MOVE
     */

    @Test
    @DirtiesContext
    public void testRankAndMove_underStoryToTop() {
        Task rankedTask = new Task();
        Story expectedParent = new Story();
        expectedParent.setId(123);
        expect(storyBusiness.retrieve(123)).andReturn(expectedParent).times(2);
        expect(taskDAO.getLastTaskInRank(expectedParent, null)).andReturn(
                fourthTaskInRank);
        expect(
                taskDAO.getTasksWithRankBetween(0, fourthTaskInRank.getRank(),
                        null, expectedParent)).andReturn(
                Arrays.asList(firstTaskInRank, secondTaskInRank,
                        thirdTaskInRank, fourthTaskInRank));
        replayAll();
        Task actual = taskBusiness.rankAndMove(rankedTask, null, 123, null);
        verifyAll();

        assertEquals(expectedParent, actual.getStory());
        assertEquals(0, actual.getRank());
    }

    @Test
    @DirtiesContext
    public void testRankAndMove_underIteration() {
        Iteration expectedParent = new Iteration();
        expectedParent.setId(222);
        Task upperTask = new Task();
        upperTask.setRank(4);
        upperTask.setIteration(expectedParent);
        Task lastTask = new Task();
        lastTask.setRank(12);
        expect(iterationBusiness.retrieve(222)).andReturn(expectedParent)
                .times(2);
        expect(taskDAO.getLastTaskInRank(null, expectedParent)).andReturn(
                lastTask);
        expect(taskDAO.getTasksWithRankBetween(5, 12, expectedParent, null))
                .andReturn(Arrays.asList(lastTask));
        replayAll();
        Task actual = taskBusiness
                .rankAndMove(new Task(), upperTask, null, 222);
        verifyAll();

        assertEquals(expectedParent, actual.getIteration());
        assertEquals(5, actual.getRank());
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testRankAndMove_nullTask() {
        replayAll();
        taskBusiness.rankAndMove(null, null, null, null);
        verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testRankAndMove_noParent() {
        replayAll();
        taskBusiness.rankAndMove(new Task(), null, null, null);
        verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testRankAndMove_bothParents() {
        replayAll();
        taskBusiness.rankAndMove(new Task(), null, 123, 345);
        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testDeleteWithHandlingChoice_delete() {
        Task task = new Task();
        task.setIteration(new Iteration());
        task.setId(50);
        expect(taskDAO.get(50)).andReturn(task);
        hourEntryBusiness.deleteAll(task.getHourEntries());
        taskDAO.remove(50);
        replayAll();
        taskBusiness.delete(50, HourEntryHandlingChoice.DELETE);
        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testDeleteWithHandlingChoice_move_toStory() {
        Task task = new Task();
        task.setStory(new Story());
        task.setId(50);
        expect(taskDAO.get(50)).andReturn(task);
        hourEntryBusiness.moveToStory(task.getHourEntries(), task.getStory());
        taskDAO.remove(50);
        replayAll();
        taskBusiness.delete(50, HourEntryHandlingChoice.MOVE);
        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testDeleteWithHandlingChoice_move_toBacklog() {
        Task task = new Task();
        task.setIteration(new Iteration());
        task.setId(50);
        expect(taskDAO.get(50)).andReturn(task);
        hourEntryBusiness.moveToBacklog(task.getHourEntries(), task
                .getIteration());
        taskDAO.remove(50);
        replayAll();
        taskBusiness.delete(50, HourEntryHandlingChoice.MOVE);
        verifyAll();
    }

}
