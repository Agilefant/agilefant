package fi.hut.soberit.agilefant.business;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.hut.soberit.agilefant.business.impl.SearchBusinessImpl;
import fi.hut.soberit.agilefant.db.BacklogDAO;
import fi.hut.soberit.agilefant.db.StoryDAO;
import fi.hut.soberit.agilefant.db.TaskDAO;
import fi.hut.soberit.agilefant.db.UserDAO;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.Team;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import fi.hut.soberit.agilefant.test.Mock;
import fi.hut.soberit.agilefant.test.MockContextLoader;
import fi.hut.soberit.agilefant.test.MockedTestCase;
import fi.hut.soberit.agilefant.test.TestedBean;
import fi.hut.soberit.agilefant.transfer.SearchResultRow;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = MockContextLoader.class)
public class SearchBusinessTest extends MockedTestCase {
    
    @TestedBean
    private SearchBusinessImpl searchBusiness;
    @Mock
    private StoryDAO storyDAO;
    @Mock
    private BacklogDAO backlogDAO;
    @Mock
    private TaskDAO taskDAO;
    @Mock
    private UserDAO userDAO;
    @Mock
    private AuthorizationBusiness authorizationBusiness;
    
    private Team team;
    
    @Test
    @DirtiesContext
    public void testSearchStoriesAndBacklogs() {
        setAccess();
        
        Backlog product = new Product();
        Backlog project = new Project();
        Iteration iteration = new Iteration();
        iteration.setParent(project);
        project.setParent(product);
        Set<Product> products = new HashSet<Product>();
        products.add((Product)product);
        team.setProducts(products);
        
        String search = "foo";
        Story story = new Story();
        story.setIteration(iteration);
        story.setBacklog(project);
        expect(authorizationBusiness.isBacklogAccessible(0, SecurityUtil.getLoggedUser())).andReturn(true).anyTimes();
        expect(backlogDAO.searchByName(search)).andReturn(Arrays.asList((Backlog)(iteration)));
        expect(storyDAO.searchByName(search)).andReturn(Arrays.asList(story));
        expect(storyDAO.searchByID(search)).andReturn(Arrays.asList(story));
        expect(taskDAO.searchByName(search)).andReturn(Arrays.asList((Task)(new Task())));
        replayAll();
        List<SearchResultRow> result = searchBusiness.searchStoriesAndBacklog(search);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getOriginalObject() instanceof Iteration);
        assertTrue(result.get(1).getOriginalObject() instanceof Story);
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testSearchStoriesAndBacklogs_reference() {
        setAccess();
        
        String search = "story:123";
        Backlog product = new Product();
        Backlog project = new Project();
        Iteration iteration = new Iteration();
        Story story = new Story();
        story.setIteration(iteration);
        story.setBacklog(project);
        iteration.setParent(project);
        project.setParent(product);
        Set<Product> products = new HashSet<Product>();
        products.add((Product)product);
        team.setProducts(products);
        
        expect(authorizationBusiness.isBacklogAccessible(0, SecurityUtil.getLoggedUser())).andReturn(true).anyTimes();
        expect(backlogDAO.searchByName(search)).andReturn(new ArrayList<Backlog>());
        expect(storyDAO.searchByName(search)).andReturn(new ArrayList<Story>());
        expect(storyDAO.searchByID(search)).andReturn(new ArrayList<Story>());
        expect(storyDAO.get(123)).andReturn(story);
        expect(taskDAO.searchByName(search)).andReturn(Arrays.asList((Task)(new Task())));
        replayAll();
        List<SearchResultRow> result = searchBusiness.searchStoriesAndBacklog(search);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getOriginalObject() instanceof Story);
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testSearchByReference_story() {
        setAccess();
        
        String term = "story:123";
        Story story = new Story();
        
        Backlog product = new Product();
        Backlog project = new Project();
        Iteration iteration = new Iteration();
        iteration.setParent(project);
        project.setParent(product);
        story.setIteration(iteration);
        story.setBacklog(project);
        Set<Product> products = new HashSet<Product>();
        products.add((Product)product);
        team.setProducts(products);

        expect(authorizationBusiness.isBacklogAccessible(0, SecurityUtil.getLoggedUser())).andReturn(true).anyTimes();
        expect(storyDAO.get(123)).andReturn(story);
        replayAll();
        assertEquals(story, searchBusiness.searchByReference(term));
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testSearchByReference_storyNotFound() {
        User user = new User();
        user.setId(10);
        SecurityUtil.setLoggedUser(user);
        
        String term = "story:123";
        expect(storyDAO.get(123)).andReturn(null);
        replayAll();
        assertNull(searchBusiness.searchByReference(term));
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testSearchByReference_backlog() {
        setAccess();
        
        String term = "backlog:123";
        Backlog product = new Product();
        Backlog project = new Project();
        Backlog iteration = new Iteration();
        iteration.setParent(project);
        project.setParent(product);
        Set<Product> products = new HashSet<Product>();
        products.add((Product)product);
        team.setProducts(products);
        
        expect(authorizationBusiness.isBacklogAccessible(0, SecurityUtil.getLoggedUser())).andReturn(true).anyTimes();
        expect(backlogDAO.get(123)).andReturn(iteration);
        replayAll();
        assertEquals(iteration, searchBusiness.searchByReference(term));
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testSearchByReference_backlogNotFound() {
        User user = new User();
        user.setId(10);
        SecurityUtil.setLoggedUser(user);
        
        String term = "backlog:123";
        expect(backlogDAO.get(123)).andReturn(null);
        replayAll();
        assertNull(searchBusiness.searchByReference(term));
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testSearchByReference_invalidTerm1() {
        User user = new User();
        user.setId(10);
        SecurityUtil.setLoggedUser(user);
        
        String term = "foo:123";
        replayAll();
        assertNull(searchBusiness.searchByReference(term));
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testSearchByReference_invalidTerm2() {
        User user = new User();
        user.setId(10);
        SecurityUtil.setLoggedUser(user);
        
        String term = "backlog:123:foo:faa";
        replayAll();
        assertNull(searchBusiness.searchByReference(term));
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testSearchByReference_invalidTerm3() {
        User user = new User();
        user.setId(10);
        SecurityUtil.setLoggedUser(user);
        
        String term = "backlog:aaa";
        replayAll();
        assertNull(searchBusiness.searchByReference(term));
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testSearchByReference_invalidTermEmpty() {
        User user = new User();
        user.setId(10);
        SecurityUtil.setLoggedUser(user);
        
        String term = "";
        replayAll();
        assertNull(searchBusiness.searchByReference(term));
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testSearchIterations() {
        setAccess();
        
        String term = "";
        
        Backlog product = new Product();
        Backlog project = new Project();
        Backlog iteration = new Iteration();
        iteration.setParent(project);
        project.setParent(product);
        Set<Product> products = new HashSet<Product>();
        products.add((Product)product);
        team.setProducts(products);
        List<Backlog> res = Arrays.asList((Backlog)(iteration));
        
        expect(authorizationBusiness.isBacklogAccessible(0, SecurityUtil.getLoggedUser())).andReturn(true).anyTimes();
        expect(backlogDAO.searchByName(term, Iteration.class)).andReturn(res);
        replayAll();
        List<SearchResultRow> actual = searchBusiness.searchIterations(term);
        verifyAll();
        assertSame(res.get(0), actual.get(0).getOriginalObject());
    }
    
    @Test
    @DirtiesContext
    public void testSearchProjects() {
        setAccess();
        
        String term = "";
        
        Backlog product = new Product();
        Backlog project = new Project();
        project.setParent(product);
        Set<Product> products = new HashSet<Product>();
        products.add((Product)product);
        team.setProducts(products);
        
        List<Backlog> res = Arrays.asList((Backlog)(project));
        
        expect(authorizationBusiness.isBacklogAccessible(0, SecurityUtil.getLoggedUser())).andReturn(true).anyTimes();
        expect(backlogDAO.searchByName(term, Project.class)).andReturn(res);
        replayAll();
        List<SearchResultRow> actual = searchBusiness.searchProjects(term);
        verifyAll();
        assertSame(res.get(0), actual.get(0).getOriginalObject());
    }
    
    @Test
    @DirtiesContext
    public void testSearchStories() {
        setAccess();
        
        String term = "";
        
        Backlog product = new Product();
        Backlog project = new Project();
        project.setParent(product);
        
        Iteration iteration = new Iteration();
        iteration.setName("foo");
        iteration.setParent(project);
        
        Story story = new Story();
        story.setName("faa");
        story.setIteration(iteration);
        story.setBacklog(project);
        
        Set<Product> products = new HashSet<Product>();
        products.add((Product)product);
        team.setProducts(products);
        
        List<Story> res = Arrays.asList(story);
        expect(authorizationBusiness.isBacklogAccessible(0, SecurityUtil.getLoggedUser())).andReturn(true).anyTimes();
        expect(storyDAO.searchByName(term)).andReturn(res);
        replayAll();
        List<SearchResultRow> actual = searchBusiness.searchStories(term);
        verifyAll();
        assertSame(story, actual.get(0).getOriginalObject());
        assertTrue(actual.get(0).getLabel().contains("foo"));
        assertTrue(actual.get(0).getLabel().contains("faa"));
    }
    
    @Test
    @DirtiesContext
    public void testSearchUsers() {
        String term = "";
        List<User> res = Arrays.asList(new User());
        
        expect(userDAO.searchByName(term)).andReturn(res);
        replayAll();
        List<SearchResultRow> actual = searchBusiness.searchUsers(term);
        verifyAll();
        assertSame(res.get(0), actual.get(0).getOriginalObject());
    }
    
    private void setAccess(){
        User user = new User();
        team = new Team();
        Collection<User> users = new ArrayList<User>();
        users.add(user);
        team.setUsers(users);
        Collection<Team> teams = new ArrayList<Team>();
        teams.add(team);
        user.setTeams(teams);
        SecurityUtil.setLoggedUser(user);
    }
}
