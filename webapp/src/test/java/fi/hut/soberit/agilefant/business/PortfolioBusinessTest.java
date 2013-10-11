package fi.hut.soberit.agilefant.business;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Period;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.hut.soberit.agilefant.business.impl.PortfolioBusinessImpl;
import fi.hut.soberit.agilefant.db.ProjectDAO;
import fi.hut.soberit.agilefant.model.Assignment;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import fi.hut.soberit.agilefant.test.Mock;
import fi.hut.soberit.agilefant.test.MockContextLoader;
import fi.hut.soberit.agilefant.test.SpringAssertions;
import fi.hut.soberit.agilefant.test.TestedBean;
import fi.hut.soberit.agilefant.transfer.PortfolioTO;
import fi.hut.soberit.agilefant.transfer.ProjectTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = MockContextLoader.class)
public class PortfolioBusinessTest {

    @TestedBean
    private PortfolioBusinessImpl portfolioBusiness;

    @Mock
    private SettingBusiness settingBusiness;
    @Mock
    private ProjectDAO projectDAO;
    @Mock
    private AuthorizationBusiness authorizationBusiness;
    
    
    protected void verifyAll() {
        verify(projectDAO, settingBusiness, authorizationBusiness);
    }

    protected void replayAll() {
        replay(projectDAO, settingBusiness, authorizationBusiness);
    }

    // PortfolioBusiness should
    // + have no scope annotation
    @Test
    public void testSpringScope() {
        SpringAssertions.assertNoScopeAnnotation(PortfolioBusiness.class);
    }

    // getPortfolioData() should
    // + retrieve portfolio time span setting from SettingBusiness
    // + retrieve ranked projects from ProjectDAO
    // + retrieve unranked projects from ProjectDAO
    // + return a non-null PortfolioTO that contains
    // a list of ranked projects as ProjectTOs and a list of unranked projects
    // as ProjectTOs
    @Test
    @DirtiesContext
    public void testGetPortfolioData() {
        LocalDate today = new LocalDate();
        Period timeSpan = Months.months(6).toPeriod();
        LocalDate endDate = today.plus(timeSpan);

        Project rankedProject = new Project();
        rankedProject.setId(666);
        User user = new User();
        user.setAdmin(true);
        SecurityUtil.setLoggedUser(user);
        Assignment assignment = new Assignment();
        assignment.setUser(user);

        List<Project> rankedProjects = new ArrayList<Project>();
        rankedProject.getAssignments().add(assignment);
        rankedProjects.add(rankedProject);

        List<Project> unrankedProjects = new ArrayList<Project>();
        Project unrankedProject = new Project();
        unrankedProject.setId(999);
        unrankedProject.getAssignments().add(assignment);
        unrankedProjects.add(unrankedProject);

        expect(settingBusiness.getPortfolioTimeSpan()).andReturn(timeSpan);
        expect(projectDAO.getRankedProjects(today, endDate)).andReturn(
                rankedProjects);
        expect(projectDAO.getUnrankedProjects(today, endDate)).andReturn(
                unrankedProjects);
        expect(authorizationBusiness.isBacklogAccessible(EasyMock.eq(rankedProject.getId()), EasyMock.anyObject(User.class))).andReturn(true).anyTimes();
        expect(authorizationBusiness.isBacklogAccessible(EasyMock.eq(unrankedProject.getId()), EasyMock.anyObject(User.class))).andReturn(true).anyTimes();
        replayAll();

        PortfolioTO result = portfolioBusiness.getPortfolioData();
        assertNotNull(result);
        assertEquals(1, result.getRankedProjects().size());
        ProjectTO rankedTo = (ProjectTO) result.getRankedProjects().get(0);

        assertEquals(1, rankedTo.getAssignees().size());
        assertSame(user, rankedTo.getAssignees().iterator().next());

        assertEquals(1, result.getUnrankedProjects().size());
        ProjectTO unrankedTo = (ProjectTO) result.getUnrankedProjects()
                .iterator().next();

        assertEquals(1, unrankedTo.getAssignees().size());
        assertSame(user, unrankedTo.getAssignees().iterator().next());

        verifyAll();
    }

}
