package fi.hut.soberit.agilefant.business;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.matchers.Each;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.hut.soberit.agilefant.business.impl.StoryBusinessImpl;
import fi.hut.soberit.agilefant.db.HourEntryDAO;
import fi.hut.soberit.agilefant.db.IterationDAO;
import fi.hut.soberit.agilefant.db.StoryDAO;
import fi.hut.soberit.agilefant.db.UserDAO;
import fi.hut.soberit.agilefant.db.history.StoryHistoryDAO;
import fi.hut.soberit.agilefant.exception.ObjectNotFoundException;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryState;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.Label;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.test.Mock;
import fi.hut.soberit.agilefant.test.MockContextLoader;
import fi.hut.soberit.agilefant.test.MockedTestCase;
import fi.hut.soberit.agilefant.test.TestedBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = MockContextLoader.class)
@SuppressWarnings("unused")
public class StoryBusinessCreateStoryTest extends MockedTestCase {
    
    @TestedBean
    private StoryBusinessImpl storyBusiness;
    
    @Mock
    private IterationHistoryEntryBusiness iterationHistoryEntryBusiness;
    @Mock
    private BacklogHistoryEntryBusiness backlogHistoryEntryBusiness;
    @Mock(strict=true)
    private StoryDAO storyDAO;
    @Mock
    private BacklogBusiness backlogBusiness;
    @Mock
    private IterationDAO iterationDAO;
    @Mock
    private UserDAO userDAO;
    @Mock
    private HourEntryDAO hourEntryDAO;
    @Mock
    private IterationBusiness iterationBusiness;
    @Mock
    private ProjectBusiness projectBusiness;
    @Mock
    private StoryHistoryDAO storyHistoryDAO;
    @Mock
    private StoryRankBusiness storyRankBusiness;
    @Mock
    private TransferObjectBusiness transferObjectBusiness;
    @Mock
    private HourEntryBusiness hourEntryBusiness;
    @Mock
    private TaskBusiness taskBusiness;
    @Mock
    private StoryHierarchyBusiness storyHierarchyBusiness;
    @Mock
    private LabelBusiness labelBusiness;
    
    @Test
    @DirtiesContext
    public void testCreateStory_noResponsibles() {
        Story tmp = new Story();
        Iteration blog = new Iteration();
        blog.setId(5);
        Project proj = new Project();
        proj.setId(12);
        blog.setParent(proj);
        expect(backlogBusiness.retrieve(5)).andReturn(blog);
        expect(backlogBusiness.retrieve(5)).andReturn(blog);
        expect(backlogBusiness.retrieve(5)).andReturn(blog);
        expect(iterationBusiness.retrieve(5)).andReturn(blog);
        
        Capture<Story> capturedStory = new Capture<Story>();
        
        expect(storyDAO.create(EasyMock.capture(capturedStory))).andReturn(88);
        expect(storyDAO.get(88)).andReturn(tmp);
        storyRankBusiness.rankToBottom(tmp, proj);
        storyRankBusiness.rankToBottom(tmp, blog);
        storyRankBusiness.rankToBottom(tmp, proj);
        
        Story returnedStory = new Story();
        returnedStory.setId(88);
        expect(storyDAO.get(88)).andReturn(returnedStory);
        
        Story dataItem = new Story();
        dataItem.setName("Foofaa");
        dataItem.setDescription("Foofaa");
        dataItem.setStoryPoints(22);
        dataItem.setState(StoryState.STARTED);
        
        storyHierarchyBusiness.moveToTop(returnedStory);
        //storyRankBusiness.rankToHead(returnedStory, blog);
        storyRankBusiness.rankToHead(returnedStory, blog);
        labelBusiness.createStoryLabels(null, 88);
        backlogHistoryEntryBusiness.updateHistory(12);
        iterationHistoryEntryBusiness.updateIterationHistory(5);
        
        replayAll();
        Story actual = this.storyBusiness.create(dataItem, 5, 5, null, null);
        verifyAll();
        
        assertEquals(actual.getClass(), Story.class);
        assertEquals(blog, capturedStory.getValue().getIteration());
        assertEquals(proj, capturedStory.getValue().getBacklog());
        
        assertEquals(dataItem.getName(), capturedStory.getValue().getName());
        assertEquals(dataItem.getDescription(), capturedStory.getValue().getDescription());
        assertEquals(dataItem.getStoryPoints(), capturedStory.getValue().getStoryPoints());
        assertEquals(dataItem.getState(), capturedStory.getValue().getState());
    }
    
    @Test
    @DirtiesContext
    public void testCreateStory_withResponsibles() {
        User user1 = new User();
        User user2 = new User();
        Story tmp = new Story();
        List<String> labels = new ArrayList<String>();
        Backlog blog = new Project();
        expect(backlogBusiness.retrieve(5)).andReturn(blog);
        expect(userDAO.get(2)).andReturn(user1);
        expect(userDAO.get(23)).andReturn(user2);
        
        Capture<Story> capturedStory = new Capture<Story>();
        
        expect(storyDAO.create(EasyMock.capture(capturedStory))).andReturn(88);
        expect(storyDAO.get(88)).andReturn(tmp);
        
        storyRankBusiness.rankToBottom(EasyMock.isA(Story.class), EasyMock.isA(Backlog.class));
        
        backlogHistoryEntryBusiness.updateHistory(blog.getId());
        
        Story returnedStory = new Story();
        returnedStory.setId(88);
        expect(storyDAO.get(88)).andReturn(returnedStory);
        
        storyHierarchyBusiness.moveToTop(returnedStory);
        Backlog bl = null;
        expect(backlogBusiness.retrieve(5)).andReturn(bl);
        storyRankBusiness.rankToHead(returnedStory, bl); 
        labelBusiness.createStoryLabels(labels, 88);
        
        replayAll();
        Story actual = this.storyBusiness.create(new Story(), 5, null,
                new HashSet<Integer>(Arrays.asList(2,23)), labels);
        verifyAll();
        
        assertSame(actual, returnedStory);
        assertTrue(capturedStory.getValue().getResponsibles().contains(user1));
        assertTrue(capturedStory.getValue().getResponsibles().contains(user2));
        assertEquals(blog, capturedStory.getValue().getBacklog());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateStory_nullDataItem() {
        this.storyBusiness.create(null, 123, null, new HashSet<Integer>(), null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateStory_nullBacklogId() {
        this.storyBusiness.create(new Story(), null, null, new HashSet<Integer>(), null);
    }
    
    @Test(expected = ObjectNotFoundException.class)
    @DirtiesContext
    public void testCreateStory_backlogNotFound() {
        expect(backlogBusiness.retrieve(5)).andThrow(new ObjectNotFoundException());
        this.storyBusiness.create(new Story(), 222, null, new HashSet<Integer>(), null);
    }
    
    @Test
    @DirtiesContext
    public void createStoryToIteration() {
        Project project = new Project();
        project.setId(1);
        
        Iteration iteration = new Iteration();
        iteration.setId(2);
        iteration.setParent(project);
        
        Story story = new Story();
        story.setIteration(iteration);
        
        expect(storyDAO.create(story)).andReturn(new Integer(1));
        expect(storyDAO.get(1)).andReturn(story);
        
        //storyRankBusiness.rankToBottom(story, project);
        storyRankBusiness.rankToHead(story, iteration);
        storyRankBusiness.rankToBottom(story, project);
        
        iterationHistoryEntryBusiness.updateIterationHistory(2);
        
        replayAll();
        storyBusiness.create(story);
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void createStoryToProject() {
        Project project = new Project();
        project.setId(1);
        
        Story story = new Story();
        story.setBacklog(project);
        
        expect(storyDAO.create(story)).andReturn(new Integer(1));
        expect(storyDAO.get(1)).andReturn(story);
        
        storyRankBusiness.rankToBottom(story, project);
        
        backlogHistoryEntryBusiness.updateHistory(1);
        
        replayAll();
        storyBusiness.create(story);
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testCreateStoryUnderCurrentBacklog() {
        Product product = new Product();
        product.setId(10);
        
        Project project = new Project();
        project.setId(11);
        
        Story reference = new Story();
        reference.setBacklog(product);
       
        Story data = new Story();
        data.setId(2);
        data.setBacklog(project);
        
        expect(storyDAO.get(1)).andReturn(reference);

        expect(backlogBusiness.retrieve(10)).andReturn(product);
        expect(backlogBusiness.retrieve(11)).andReturn(project);
        
        expect(storyDAO.create(EasyMock.isA(Story.class))).andReturn(new Integer(2));
        expect(storyDAO.get(2)).andReturn(data).times(2);
        
        storyHierarchyBusiness.moveUnder(data, reference);
        labelBusiness.createStoryLabels(null, 2);
        
        replayAll();
        // Case 1: Reference story's backlog is product (or project) and current view is project
        // Assert: Newly created story's backlog should be project
        assertSame(data.getBacklog(), project);
        storyBusiness.createStoryUnder(1, project.getId(), data, null, null);
        
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testCreateStoryUnderReferenceStoryWithProductBacklog() {
        Product product = new Product();
        product.setId(10);
        
        Story reference = new Story();
        reference.setBacklog(product);
        
        Story data = new Story();
        data.setId(2);
        data.setBacklog(product);
        expect(storyDAO.get(1)).andReturn(reference);

        expect(backlogBusiness.retrieve(10)).andReturn(product).times(2);
                
        expect(storyDAO.create(EasyMock.isA(Story.class))).andReturn(new Integer(2));
        expect(storyDAO.get(2)).andReturn(data).times(2);
        
        storyHierarchyBusiness.moveUnder(data, reference);
        labelBusiness.createStoryLabels(null, 2);
        
        replayAll();
        // Case 2: Reference story's backlog is product and current view is product
        // Assert: Newly created story's backlog should be product 
        assertSame(data.getBacklog(), product);
        storyBusiness.createStoryUnder(1, data.getBacklog().getId(), data, null, null);
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testCreateStoryUnderReferenceStoryWithProjectBacklog() {
        Project project = new Project();
        project.setId(11);
        
        Story reference = new Story();
        reference.setBacklog(project);
        
        Story data = new Story();
        data.setId(2);
        data.setBacklog(project);
        
        expect(storyDAO.get(1)).andReturn(reference);
        expect(backlogBusiness.retrieve(11)).andReturn(project).times(2);
        expect(storyDAO.create(EasyMock.isA(Story.class))).andReturn(new Integer(2));
        expect(storyDAO.get(2)).andReturn(data).times(2);
        
        storyRankBusiness.rankToBottom(data, project);
        EasyMock.expectLastCall().once();
        
        storyHierarchyBusiness.moveUnder(data, reference);
        labelBusiness.createStoryLabels(null, 2);
        
        backlogHistoryEntryBusiness.updateHistory(11);
        EasyMock.expectLastCall().once();
       
        replayAll();
        // Case 3: Reference story's backlog is project and current view is product
        // Assert: Newly created story's backlog should be project  
        assertSame(data.getBacklog(), project);
        storyBusiness.createStoryUnder(1, data.getBacklog().getId(), data, null, null);
        
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testCreateSiblingCurrentBacklog() { 
        Product product = new Product();
        product.setId(10);
        
        Project project = new Project();
        project.setId(11);

        
        Story reference = new Story();
        reference.setBacklog(product);
        
        Story data = new Story();
        data.setId(2);
        data.setBacklog(project);
        
        expect(storyDAO.get(1)).andReturn(reference);
        
        expect(backlogBusiness.retrieve(10)).andReturn(product);
        expect(backlogBusiness.retrieve(11)).andReturn(project);
        
        expect(storyDAO.create(EasyMock.isA(Story.class))).andReturn(new Integer(2));
        
        expect(storyDAO.get(2)).andReturn(data).times(2);
        storyHierarchyBusiness.moveAfter(data, reference);
        labelBusiness.createStoryLabels(null, 2);
        
        replayAll();
        
        Story newStory = storyBusiness.createStorySibling(1, project.getId(), data, null, null);
        
        // Case 1: Reference story's backlog is product (or project) and current view is project
        // Assert: Newly created sibling story's backlog should be project
        assertSame(newStory.getBacklog(), project);
        
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testCreateSiblingUnderReferenceStoryWithProductBacklog() { 
        Product product = new Product();
        product.setId(10);
        
        Story reference = new Story();
        reference.setBacklog(product);
        
        Story data = new Story();
        data.setId(2);
        data.setBacklog(product);
        expect(storyDAO.get(1)).andReturn(reference);

        expect(backlogBusiness.retrieve(10)).andReturn(product).times(2);
                
        expect(storyDAO.create(EasyMock.isA(Story.class))).andReturn(new Integer(2));
        expect(storyDAO.get(2)).andReturn(data).times(2);
        
        storyHierarchyBusiness.moveAfter(data, reference);
        labelBusiness.createStoryLabels(null, 2);
        
        replayAll();
        
        Story newStory = storyBusiness.createStorySibling(1, data.getBacklog().getId(), data, null, null);
        
        // Case 2: Reference story's backlog is product and current view is product
        // Assert: Newly created sibling story's backlog should be product 
        assertSame(newStory.getBacklog(), product);
        
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testCreateSiblingUnderReferenceStoryWithProjectBacklog() { 
        Project project = new Project();
        project.setId(11);
        
        Story reference = new Story();
        reference.setBacklog(project);
          
        Story data = new Story();
        data.setId(2);
        data.setBacklog(project);
        
        expect(storyDAO.get(1)).andReturn(reference);
        
        expect(backlogBusiness.retrieve(11)).andReturn(project).times(2);
                
        expect(storyDAO.create(EasyMock.isA(Story.class))).andReturn(new Integer(2));
        expect(storyDAO.get(2)).andReturn(data).times(2);

        storyRankBusiness.rankToBottom(data, project);
        EasyMock.expectLastCall().once();
        
        backlogHistoryEntryBusiness.updateHistory(11);
        EasyMock.expectLastCall().once();
        
        storyHierarchyBusiness.moveAfter(data, reference);
        labelBusiness.createStoryLabels(null, 2);
        
        replayAll();
        
        Story newStory = storyBusiness.createStorySibling(1, data.getBacklog().getId(), data, null, null);
        
        // Case 3: Reference story's backlog is project and current view is project
        // Assert: Newly created sibling story's backlog should be project 
        assertSame(newStory.getBacklog(), project);
        
        verifyAll();
    }
    
    
    @Test
    @DirtiesContext
    public void testCopyStory_withoutResponsibles() {
        Product product = new Product();
        product.setId(10);
        
        Story reference = new Story();
        reference.setBacklog(product);
        reference.setName("origin");
        reference.setId(1);
        reference.setTreeRank(5);
        reference.setStoryPoints(4);
        reference.setStoryValue(10);
        reference.setState(StoryState.STARTED);
        reference.setDescription("Foofaa");
               
        // Copy new story
        Story data = new Story(reference);
        data.setId(2);
        
        expect(storyDAO.get(1)).andReturn(reference);
        
        expect(backlogBusiness.retrieve(10)).andReturn(product);
        expect(storyDAO.create(EasyMock.isA(Story.class))).andReturn(new Integer(2));
        expect(storyDAO.get(2)).andReturn(data).times(2);
        expect(storyDAO.get(2)).andReturn(data).times(2);
        assertSame(data.getName(), "origin");
        assertSame(data.getTreeRank(), 4);
        assertSame(data.getStoryPoints(), 4);
        assertSame(data.getStoryValue(), 10);
        assertSame(data.getState(), StoryState.STARTED);
        assertSame(data.getDescription(), "Foofaa");
       }
    
    @Test
    @DirtiesContext
    public void testCopyStory_withResponsibles() {
        User user1 = new User();
        User user2 = new User();
        Story tmp = new Story();
        
        Set<User> users = new HashSet<User>();
        users.add(user1);
        users.add(user2);
        tmp.setResponsibles(users);
        
        Story returnedStory = new Story(tmp);
        assertTrue(returnedStory.getResponsibles().contains(user1));
        assertTrue(returnedStory.getResponsibles().contains(user2));
    }
    
    @Test
    @DirtiesContext
    public void testCopyStory_withTask() {
        Task t1 = new Task();
        t1.setName("task1");
        Task t2 = new Task(t1);
        Story tmp = new Story();
        t2.setStory(tmp);
        
        tmp.getTasks().add(t2);
        
        Story returnedStory = new Story(tmp);
        assertTrue(returnedStory.getTasks().size() == 1);
        assertSame(returnedStory.getTasks().iterator().next().getName(),"task1");
    }
    
    @Test
    @DirtiesContext
    public void testCopyStory_withLabels() {
        Label label1 = new Label();
        label1.setName("label1");
        Label label2 = new Label(label1);
        label2.setName("label2");
        
        // Copy story
        Story tmp = new Story();
        tmp.getLabels().add(label1);
        tmp.getLabels().add(label2);
        Story returnedStory = new Story(tmp);
        
        // Check if labels get copied over
        assertTrue(returnedStory.getLabels().size() == 2);
        boolean label1Exist = false;
        boolean label2Exist = false;
        for( Label l : returnedStory.getLabels())
        {
            if(l.getName() == "label1")
                label1Exist = true;
            if(l.getName() == "label2")
                label2Exist = true;
        }
        assertTrue(label1Exist);
        assertTrue(label2Exist);
    }
    
    @Test
    @DirtiesContext
    public void testCopyStory_withChildStories() {
        
        // Create stories
        Story originStory = new Story();
        
        List<Story> storyList = new ArrayList<Story>();
        for(Integer i=0; i<10; i++)
        {
            Story tmp = new Story();
            tmp.setName(i.toString());
            //tmp.setParent(originStory); TODO: promperror if we set the child's parent here
            storyList.add(tmp);
        }
        
        originStory.setChildren(storyList);
        Story copyStory = new Story(originStory);
                
        // Check if child stories get copied over
        assertTrue(copyStory.getChildren().size() == 10);
        int count = 0;
        for( Story st : copyStory.getChildren())
        {
            Integer countInt = count;
            String countName = countInt.toString();
            assertTrue(st.getName().compareTo(countInt.toString()) == 0);
            count++;
        }
    }
}
