package fi.hut.soberit.agilefant.db;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.test.AbstractHibernateTests;
import fi.hut.soberit.agilefant.transfer.ProjectMetrics;

@ContextConfiguration
@Transactional
public class ProjectDAOTest extends AbstractHibernateTests {

    @Autowired
    private ProjectDAO projectDAO;
    
    @Test
    public void testGetAssignedUsers_noAssignees() {
        executeClassSql();
        Project proj = new Project();
        proj.setId(3);
        assertEquals(0, projectDAO.getAssignedUsers(proj).size());
    }
    
    @Test
    public void testGetAssignedUsers_hasAssignees() {
        executeClassSql();
        Project proj = new Project();
        proj.setId(1);
        assertEquals(2, projectDAO.getAssignedUsers(proj).size());
    }
    
    @Test 
    public void testGetProjectsWithUserAssigned_noAssignments() {
        executeClassSql();
        User user = new User();
        user.setId(2);
        assertEquals(0, projectDAO.getProjectsWithUserAssigned(user).size());
    }
    
    @Test 
    public void testGetProjectsWithUserAssigned_hasAssignments() {
        executeClassSql();
        User user = new User();
        user.setId(1);
        assertEquals(2, projectDAO.getProjectsWithUserAssigned(user).size());
    }
    
    @Test 
    public void testGetProject() {
        executeClassSql();
        Project proj = projectDAO.get(1);
        assertNotNull(proj);
        assertEquals(2, proj.getAssignments().size());
    }
    
    @Test
    public void testGetUnrankedProjects() {
        executeSql("classpath:fi/hut/soberit/agilefant/db/ProjectDAOTest-portfolio-data.sql");
        LocalDate startDate = new LocalDate(2009, 10, 1);
        LocalDate endDate = new LocalDate(2009, 12, 30);
        Collection<Project> projects = projectDAO.getUnrankedProjects(startDate, endDate);
        assertNotNull(projects);
        assertEquals(1, projects.size());
        
        for (Project project : projects) {
          assertTrue(project.getRank() < 1);
          assertFalse(project.getEndDate().isBefore(startDate.toDateTimeAtStartOfDay()));
          assertFalse(project.getStartDate().isAfter(endDate.toDateTimeAtStartOfDay()));
        }
        
        
    }
    
    @Test
    public void testGetRankedProjects() {
        executeSql("classpath:fi/hut/soberit/agilefant/db/ProjectDAOTest-portfolio-data.sql");
        LocalDate startDate = new LocalDate(2009, 10, 1);
        LocalDate endDate = new LocalDate(2009, 12, 30);
        List<Project> projects = projectDAO.getRankedProjects(startDate, endDate);
        assertNotNull(projects);
        assertEquals(4, projects.size());
        
        for (Project project : projects) {
          assertTrue(project.getRank() > 0);
          assertFalse(project.getEndDate().isBefore(startDate.toDateTimeAtStartOfDay()));
          assertFalse(project.getStartDate().isAfter(endDate.toDateTimeAtStartOfDay()));
        }      
        
    }
    
    @Test
    public void testGetMaxRankedProject() {
        executeSql("classpath:fi/hut/soberit/agilefant/db/ProjectDAOTest-portfolio-data.sql");
        Project project = projectDAO.getMaxRankedProject();
        assertNotNull(project);
        assertEquals(6, project.getRank().intValue());       
    }
    
    @Test
    public void testIncreaseRankedProjectsRank() {
        executeClassSql();
        projectDAO.increaseRankedProjectRanks();
        Collection<Project> projects = projectDAO.getAll();
        int lowest = Integer.MAX_VALUE;
        int highest = Integer.MIN_VALUE;
        for (Project project : projects) {
            if (project.getRank() < lowest) {
                lowest = project.getRank();
            }
            if (project.getRank() > highest) {
                highest = project.getRank();
            }
        }
        assertEquals(2, lowest);
        assertEquals(4, highest);
    }
    
    @Test
    public void testRetrieveActiveWithUserAssigned() {
        executeSql("classpath:fi/hut/soberit/agilefant/db/ProjectDAOTest-assignments-data.sql");
        List<Project> projects = projectDAO.retrieveActiveWithUserAssigned(1);
        assertEquals(2, projects.size());
    }
    
    @Test
    public void testCalculateStoryMetrics() {
        executeClassSql();
        ProjectMetrics actual = projectDAO.calculateProjectStoryMetrics(3);
        assertEquals(10110, actual.getCompletedStoryPoints());
        assertEquals(11110, actual.getStoryPoints());
        assertEquals(3, actual.getNumberOfDoneStories());
        assertEquals(4, actual.getNumberOfStories());
    }
}
