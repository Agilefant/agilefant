package fi.hut.soberit.agilefant.business;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.hut.soberit.agilefant.business.impl.ProjectBusinessImpl;
import fi.hut.soberit.agilefant.db.BacklogDAO;
import fi.hut.soberit.agilefant.db.ProjectDAO;
import fi.hut.soberit.agilefant.exception.ObjectNotFoundException;
import fi.hut.soberit.agilefant.model.Assignment;
import fi.hut.soberit.agilefant.model.BacklogHistoryEntry;
import fi.hut.soberit.agilefant.model.BacklogHourEntry;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.test.Mock;
import fi.hut.soberit.agilefant.test.MockContextLoader;
import fi.hut.soberit.agilefant.test.MockedTestCase;
import fi.hut.soberit.agilefant.test.TestedBean;
import fi.hut.soberit.agilefant.transfer.ProjectMetrics;
import fi.hut.soberit.agilefant.transfer.ProjectTO;
import fi.hut.soberit.agilefant.util.StoryFilters;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = MockContextLoader.class)
public class ProjectBusinessTest  extends MockedTestCase { 
    @TestedBean
    ProjectBusinessImpl projectBusiness;
    @Mock
    ProjectDAO projectDAO;
    @Mock
    BacklogDAO backlogDAO;
    @Mock
    ProductBusiness productBusiness;
    @Mock
    TransferObjectBusiness transferObjectBusiness;
    @Mock
    SettingBusiness settingBusiness;
    @Mock
    RankingBusiness rankingBusiness;
    @Mock
    AssignmentBusiness assignmentBusiness;
    @Mock
    BacklogHistoryEntryBusiness backlogHistoryEntryBusiness;
    @Mock
    IterationBusiness iterationBusiness;
    @Mock
    StoryBusiness storyBusiness;
    @Mock
    HourEntryBusiness hourEntryBusiness;
    @Mock
    StoryRankBusiness storyRankBusiness;
    @Mock
    StoryFilterBusiness storyFilterBusiness;
    @Mock
    BacklogBusiness backlogBusiness;
    
    Project project;
    Product product;
    User user1;
    User user2;
    Story story1;
    Story story2;


    @Before
    public void setUp_data() {
        product = new Product();
        product.setId(313);

        project = new Project();
        project.setId(123);
        project.setStartDate(new DateTime(2008, 1, 1, 12, 0, 0, 0));
        project.setEndDate(new DateTime(2008, 3, 1, 12, 0, 0, 0));

        user1 = new User();
        user1.setId(1);
        user2 = new User();
        user2.setId(2);

        story1 = new Story();
        story1.setId(127);
        story2 = new Story();
        story2.setId(130);
        story2.setResponsibles(new HashSet<User>(Arrays.asList(user2)));

        Task task = new Task();
        task.setId(86);
        task.setResponsibles(new HashSet<User>(Arrays.asList(user1)));
        story1.setTasks(new HashSet<Task>(Arrays.asList(task)));
    }

    @Test
    @DirtiesContext
    public void testGetProjectMetrics() {
        ProjectMetrics metrics = new ProjectMetrics();
        List<Story> leafStories = new ArrayList<Story>();
        project.setStartDate(new DateTime().minusDays(7));
        project.setEndDate(project.getStartDate().plusDays(10));

        metrics.setTotalDays(10);
        metrics.setDaysLeft(3);
        
        metrics.setNumberOfStories(100);
        metrics.setNumberOfDoneStories(40);
        
        metrics.setStoryPoints(1000);
        metrics.setCompletedStoryPoints(10);
        
        metrics.setCompletedValue(3);
        metrics.setTotalValue(10);
        
        
        expect(projectDAO.calculateProjectStoryMetrics(project.getId())).andReturn(metrics);
        expect(backlogBusiness.daysLeftInSchedulableBacklog(project)).andReturn(Days.days(3));
        expect(storyRankBusiness.retrieveByRankingContext(project)).andReturn(leafStories);
        for(Story story : leafStories)
            expect(storyBusiness.calculateMetrics(story));
        

        replayAll();

        ProjectMetrics actualMetrics = projectBusiness
                .getProjectMetrics(project);

        assertSame(metrics, actualMetrics);
        assertEquals(40, actualMetrics.getCompletedStoriesPercentage());
        assertEquals(1, actualMetrics.getStoryPointsCompletedPercentage());
        assertEquals(30, actualMetrics.getDaysLeftPercentage());

        verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testGetProjectMetrics_nullProject() {
        projectBusiness.getProjectMetrics(null);
    }

    @Test
    @DirtiesContext
    public void testStoreProject_oldProject() {
        ProjectTO actual = new ProjectTO(project);
        
        expect(projectDAO.get(project.getId())).andReturn(project).anyTimes();
        projectDAO.store(project);
        expect(transferObjectBusiness.constructProjectTO(project)).andReturn(actual);

        final Story leafStory = new Story();
        leafStory.setId(12345);
        List<Story> leafStoryList = new ArrayList<Story>() {{
            add(leafStory);
        }};
        expect(storyRankBusiness.retrieveByRankingContext(project)).andReturn(leafStoryList);
        expect(storyFilterBusiness.filterStoryList(leafStoryList, new StoryFilters(null, null))).andReturn(leafStoryList);

        replayAll();
        assertSame(actual, projectBusiness.store(project.getId(), null, project, null));
        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testStoreProject_newProject() {
        ProjectTO actual = new ProjectTO(project);
        project.setId(0);
        expect(productBusiness.retrieve(313)).andReturn(product);
        expect(projectDAO.create(EasyMock.isA(Project.class))).andReturn(123);
        expect(projectDAO.get(123)).andReturn(project);
        expect(transferObjectBusiness.constructProjectTO(project)).andReturn(actual);
        replayAll();
        assertSame(actual, projectBusiness.store(0, 313, project, null));
        verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testStoreProject_invalidDates() {
        project.setStartDate(new DateTime(2008, 1, 1, 1, 0, 0, 0));
        project.setEndDate(new DateTime(2007, 1, 1, 1, 0, 0, 0));

        expect(projectDAO.get(project.getId())).andReturn(project);
        projectDAO.store(project);
        replayAll();
        projectBusiness.store(project.getId(), null, project, null);
        verifyAll();
    }

    @Test(expected = ObjectNotFoundException.class)
    @DirtiesContext
    public void testStoreProject_illegalProjectId() {
        Project falseProject = new Project();
        falseProject.setId(4);
        expect(projectDAO.get(falseProject.getId())).andReturn(null);
        replayAll();
        projectBusiness.store(falseProject.getId(), null, falseProject, null);
        verifyAll();
    }

    @Test
    @DirtiesContext
    public void testStoreProject_withProduct() {
        ProjectTO actual = new ProjectTO(project);

        expect(projectDAO.get(project.getId())).andReturn(project).anyTimes();
        expect(productBusiness.retrieve(313)).andReturn(product);
        projectDAO.store(project);
        expect(transferObjectBusiness.constructProjectTO(project)).andReturn(actual);

        final Story leafStory = new Story();
        leafStory.setId(12345);
        List<Story> leafStoryList = new ArrayList<Story>() {{
            add(leafStory);
        }};
        expect(storyRankBusiness.retrieveByRankingContext(project)).andReturn(leafStoryList);
        expect(storyFilterBusiness.filterStoryList(leafStoryList, new StoryFilters(null, null))).andReturn(leafStoryList);

        replayAll();
        assertSame(actual, projectBusiness.store(project.getId(), 313, project, null));
        verifyAll();
        assertEquals(product, project.getParent());
    }

    @Test(expected = ObjectNotFoundException.class)
    @DirtiesContext
    public void testStoreProject_invalidProduct() {
        expect(projectDAO.get(123)).andReturn(project);
        expect(productBusiness.retrieve(313)).andThrow(new ObjectNotFoundException());
        replayAll();
        projectBusiness.store(123, 313, project, null);
        verifyAll();
    }
    
    @Test(expected = IllegalArgumentException.class)
    @DirtiesContext
    public void testStoreProject_newProjectWithoutParent() {
        projectBusiness.store(0, null, project, null);
    }

    @Test
    @DirtiesContext
    public void testGetAssignedUsers() {
        Collection<User> expected = Arrays.asList(user1, user2);
        expect(projectDAO.getAssignedUsers(project)).andReturn(expected);
        replayAll();

        assertSame("List doesn't contain expected users", expected,
                projectBusiness.getAssignedUsers(project));

        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testGetProjectData() {
        Project proj = new Project();
        proj.setId(111);
        proj.setName("Foo faa");
               
               
        expect(projectDAO.get(111)).andReturn(proj).times(2);
        expect(transferObjectBusiness.constructProjectTO(proj)).andReturn(new ProjectTO(proj));
        replayAll();
        ProjectTO actual = projectBusiness.getProjectData(111);
        verifyAll();
        
        assertEquals(111, actual.getId());
        assertEquals("Foo faa", actual.getName());
    }
    
    
    @Test(expected = ObjectNotFoundException.class)
    @DirtiesContext
    public void testGetProjectData_noSuchProject() {
        expect(projectDAO.get(-1)).andReturn(null);
        replayAll();
        projectBusiness.getProjectData(-1);
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testUnrank() {
        Project project = new Project();
        project.setId(500);
        project.setRank(999);
        expect(projectDAO.get(500)).andReturn(project);
        replayAll();
        projectBusiness.unrankProject(project.getId());
        assertEquals(0, project.getRank().intValue());
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testRankOver_rankToTop() {
        Project project = new Project();
        project.setId(1);
        project.setRank(2);
        Project rankOver = new Project();
        rankOver.setRank(1);
        rankOver.setId(5);
        expect(projectDAO.get(project.getId())).andReturn(project);
        expect(projectDAO.get(rankOver.getId())).andReturn(rankOver);
        expect(projectDAO.getProjectWithRankLessThan(1)).andReturn(null);
        projectDAO.increaseRankedProjectRanks();
        replayAll();
        projectBusiness.rankOverProject(project.getId(), rankOver.getId());
        verifyAll();
        assertEquals(1, project.getId());
    }
             
    @Test
    @DirtiesContext
    public void testRankOver() {
        Project project = new Project();
        project.setId(1);
        project.setRank(3);
        Project rankOver = new Project();
        Project rankUnder = new Project();
        rankUnder.setRank(1);
        rankOver.setRank(2);
        rankOver.setId(5);
        expect(projectDAO.get(project.getId())).andReturn(project);
        expect(projectDAO.get(rankOver.getId())).andReturn(rankOver);
        expect(projectDAO.getProjectWithRankLessThan(2)).andReturn(rankUnder);
        rankingBusiness.rankUnder(eq(project), eq(rankUnder), isA(RankUnderDelegate.class));
        replayAll();
        projectBusiness.rankOverProject(project.getId(), rankOver.getId());
        verifyAll();
        assertEquals(1, project.getId());
    }
    
    @Test
    @DirtiesContext
    public void testDeleteProject() {
        Product product = new Product();
        product.setId(123);
        Iteration iter = new Iteration();
        Project project = new Project();
        iter.setParent(project);
        iter.setId(111);
        project.getChildren().add(iter);
        project.setParent(product);
        project.setId(112);
        Story story = new Story();
        Set<Story> stories = new HashSet<Story>();
        stories.add(story);
        project.setStories(stories);
        Set<Assignment> assignments = new HashSet<Assignment>();
        Assignment assignment = new Assignment();
        assignments.add(assignment);
        project.setAssignments(assignments);
        Set<BacklogHistoryEntry> historyEntries = new HashSet<BacklogHistoryEntry>();
        BacklogHistoryEntry historyEntry = new BacklogHistoryEntry();
        historyEntries.add(historyEntry);
        project.setBacklogHistoryEntries(historyEntries);
        Set<BacklogHourEntry> hourEntries = new HashSet<BacklogHourEntry>();
        BacklogHourEntry hourEntry = new BacklogHourEntry();
        hourEntries.add(hourEntry);
        project.setHourEntries(hourEntries);
        
        expect(projectDAO.get(project.getId())).andReturn(project);
        
        storyRankBusiness.removeBacklogRanks(project);
        
        iterationBusiness.delete(iter.getId());
        
        storyBusiness.forceDelete(story);
        backlogHistoryEntryBusiness.delete(historyEntry.getId());
        assignmentBusiness.delete(assignment.getId());
        hourEntryBusiness.deleteAll(project.getHourEntries());
        
        projectDAO.remove(project);
        
        replayAll();
        projectBusiness.delete(project.getId());
        verifyAll();
    }

}
