package fi.hut.soberit.agilefant.business;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import fi.hut.soberit.agilefant.business.impl.MenuBusinessImpl;
import fi.hut.soberit.agilefant.db.IterationDAO;
import fi.hut.soberit.agilefant.db.ProjectDAO;
import fi.hut.soberit.agilefant.db.StoryDAO;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Team;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import fi.hut.soberit.agilefant.transfer.MenuDataNode;
import fi.hut.soberit.agilefant.transfer.ScheduleStatus;

public class MenuBusinessTest {

    MenuBusinessImpl menuBusiness;
    
    ProductBusiness productBusiness;
    
    IterationBusiness iterationBusiness;
    
    ProjectDAO projectDAO;
    
    IterationDAO iterationDAO;
    
    StoryDAO storyDAO;
    
    TransferObjectBusiness transferObjectBusiness;
    
    UserBusiness userBusiness;
    
    AuthorizationBusiness authorizationBusiness;
    
    Set<Product> products;
    
    @Before
    public void setUp_dependencies() {
        menuBusiness = new MenuBusinessImpl();
        
        productBusiness = createStrictMock(ProductBusiness.class);
        menuBusiness.setProductBusiness(productBusiness);
        
        transferObjectBusiness = createStrictMock(TransferObjectBusiness.class);
        menuBusiness.setTransferObjectBusiness(transferObjectBusiness);
        
        iterationBusiness = createStrictMock(IterationBusiness.class);
        menuBusiness.setIterationBusiness(iterationBusiness);
        
        iterationDAO = createStrictMock(IterationDAO.class);
        menuBusiness.setIterationDAO(iterationDAO);
        
        projectDAO = createStrictMock(ProjectDAO.class);
        menuBusiness.setProjectDAO(projectDAO);
        
        storyDAO = createStrictMock(StoryDAO.class);
        menuBusiness.setStoryDAO(storyDAO);
        
        userBusiness = createStrictMock(UserBusiness.class);
        menuBusiness.setUserBusiness(userBusiness);
        
        authorizationBusiness = createStrictMock(AuthorizationBusiness.class);
        menuBusiness.setAuthorizationBusiness(authorizationBusiness);
    }

    @Before
    public void setUp_dataset() {
        products = new HashSet<Product>();
        Product prod1 = new Product();
        prod1.setName("zzz");
        prod1.setId(2);
        products.add(prod1);
        Product prod2 = new Product();
        prod2.setName("aaa");
        products.add(prod2);
        prod2.setId(1);
        
        Project proj1 = new Project();
        proj1.setStartDate(new DateTime(2009,1,1,0,0,0,0));
        proj1.setId(3);
        Project proj2 = new Project();
        proj2.setStartDate(new DateTime(2009,10,1,0,0,0,0));
        proj2.setId(5);
        Project proj3 = new Project();
        proj3.setStartDate(new DateTime(2009,6,1,0,0,0,0));
        proj3.setId(4);
        
        prod1.getChildren().add(proj1);
        prod1.getChildren().add(proj2);
        prod1.getChildren().add(proj3);
        
        Iteration iter1 = new Iteration();
        iter1.setStartDate(new DateTime(2009,12,1,0,0,0,0));
        iter1.setId(8);
        Iteration iter2 = new Iteration();
        iter2.setStartDate(new DateTime(2009,2,1,0,0,0,0));
        iter2.setId(6);
        Iteration iter3 = new Iteration();
        iter3.setStartDate(new DateTime(2009,7,1,0,0,0,0));
        iter3.setId(7);
        
        proj1.getChildren().add(iter1);
        proj1.getChildren().add(iter2);
        proj1.getChildren().add(iter3);
    }
    private void replayAll() {
        replay(iterationDAO, projectDAO, storyDAO, productBusiness, transferObjectBusiness, iterationBusiness, userBusiness, authorizationBusiness);
    }

    private void verifyAll() {
        verify(iterationDAO, projectDAO, storyDAO, productBusiness, transferObjectBusiness, iterationBusiness, userBusiness, authorizationBusiness);
    }
    
    @Test
    public void constructBacklogMenuData() {  
        User user = new User();
        user.setId(10);
        SecurityUtil.setLoggedUser(user);
        Team team = new Team();
        Collection<User> users = new ArrayList<User>();
        users.add(user);
        team.setUsers(users);
        Collection<Team> teams = new ArrayList<Team>();
        teams.add(team);
        user.setTeams(teams);
        team.setProducts(products);
        
        expect(productBusiness.retrieveAllOrderByName()).andReturn(
                products);
        expect(authorizationBusiness.isBacklogAccessible(1, SecurityUtil.getLoggedUser())).andReturn(true);
        expect(authorizationBusiness.isBacklogAccessible(2, SecurityUtil.getLoggedUser())).andReturn(true);

        final ArrayList<Iteration> emptyStandAloneIterations = new ArrayList<Iteration>();
        expect(iterationBusiness.retrieveAllStandAloneIterations())
            .andReturn(emptyStandAloneIterations);
        
        expect(transferObjectBusiness.getBacklogScheduleStatus(isA(Backlog.class)))
            .andReturn(ScheduleStatus.FUTURE).times(8);
        
        replayAll();
               
        List<MenuDataNode> actual = menuBusiness.constructBacklogMenuData();
        verifyAll();
        
        assertEquals(2, actual.size());
        checkProducts(actual);
    }
    
    @Test
    public void testConstructMyAssigmentsData() {
        User user = new User();
        user.setId(10);
        Project project = new Project();
        project.setId(1);
        project.setName("Project");
        Iteration iteration = new Iteration();
        iteration.setId(2);
        iteration.setName("Iteration");
        iteration.setParent(project);
        Story story = new Story();
        story.setId(3);
        story.setName("Story");
        story.setIteration(iteration);
        expect(projectDAO.retrieveActiveWithUserAssigned(user.getId())).andReturn(Arrays.asList(project));
        expect(iterationDAO.retrieveActiveWithUserAssigned(user.getId())).andReturn(Arrays.asList(iteration));
        expect(storyDAO.retrieveActiveIterationStoriesWithUserResponsible(user.getId())).andReturn(Arrays.asList(story));
        replayAll();
        List<MenuDataNode> nodes = menuBusiness.constructMyAssignmentsData(user);
        verifyAll();
        assertEquals(1, nodes.size());
        MenuDataNode projectNode = nodes.get(0);
        assertEquals("Project", projectNode.getTitle());
        assertEquals(1, projectNode.getChildren().size());
        MenuDataNode iterationNode = projectNode.getChildren().get(0);
        assertEquals("Iteration", iterationNode.getTitle());
        assertEquals(1, iterationNode.getChildren().size());
        MenuDataNode storyNode = iterationNode.getChildren().get(0);
        assertEquals("Story", storyNode.getTitle());
    }
    
    private void checkProducts(List<MenuDataNode> nodes) {
        assertEquals(1, nodes.get(0).getId());
        assertEquals(2, nodes.get(1).getId());
        
        checkProjects(nodes.get(1).getChildren());
    }
    
    private void checkProjects(List<MenuDataNode> nodes) {
        assertEquals(3, nodes.get(0).getId());
        assertEquals(4, nodes.get(1).getId());
        assertEquals(5, nodes.get(2).getId());
        checkIterations(nodes.get(0).getChildren());
    }
    
    private void checkIterations(List<MenuDataNode> nodes) {
        assertEquals(6, nodes.get(0).getId());
        assertEquals(7, nodes.get(1).getId());
        assertEquals(8, nodes.get(2).getId());
    }

}
