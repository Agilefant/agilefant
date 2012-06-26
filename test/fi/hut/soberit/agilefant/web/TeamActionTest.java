package fi.hut.soberit.agilefant.web;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

import fi.hut.soberit.agilefant.business.TeamBusiness;
import fi.hut.soberit.agilefant.exception.ObjectNotFoundException;
import fi.hut.soberit.agilefant.model.Team;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.security.SecurityUtil;

public class TeamActionTest {
    
    // Class under test
    TeamAction teamAction;
    
    // Dependencies
    TeamBusiness teamBusiness;
    
    // Test data
    Team team1;
    Team team2;
    
    User user1;
    User user2;
    
    @Before
    public void setUp_dependencies() {
        teamAction = new TeamAction();
        
        teamBusiness = createStrictMock(TeamBusiness.class);
        teamAction.setTeamBusiness(teamBusiness);
    }
    
    private void replayAll() {
        replay(teamBusiness);
    }
    
    private void verifyAll() {
        verify(teamBusiness);
    }
    
    @Before
    public void setUp_data() {
        team1 = new Team();
        team1.setName("First team");
        team2 = new Team();
        team2.setName("Second team");
        
        user1 = new User();
        user1.setAdmin(true);
        user2 = new User();
        user2.setAdmin(false);
        
        SecurityUtil.setLoggedUser(user1);
        
        Collection<User> userList = new ArrayList<User>();
        userList.add(user1);
        userList.add(user2);
        team1.setUsers(userList);
    }
    
    @Test
    public void testRetrieveAll() {
        Collection<Team> teamList = Arrays.asList(team1, team2);
        expect(teamBusiness.retrieveAll()).andReturn(teamList);
        replayAll();
        
        assertEquals(Action.SUCCESS, teamAction.retrieveAll());
        assertEquals(teamList, teamAction.getTeamList());
        
        verifyAll();
    }
    
    @Test
    public void testRetrieve() {
        teamAction.setTeamId(123);
        expect(teamBusiness.retrieve(123)).andReturn(team2);
        
        replayAll();
        assertEquals(Action.SUCCESS, teamAction.retrieve());
        verifyAll();
        
        assertEquals(team2, teamAction.getTeam());
    }
    
    @Test(expected = ObjectNotFoundException.class)
    public void testRetrieve_notFound() {
        teamAction.setTeamId(-1);
        expect(teamBusiness.retrieve(-1)).andThrow(new ObjectNotFoundException());
        
        replayAll();
        assertEquals(Action.SUCCESS, teamAction.retrieve());
        verifyAll();
    }
    
    @Test
    public void testStore() {
        teamAction.setTeam(team1);
        Team returned = new Team();
        Set<Integer> userIds = new HashSet<Integer>(Arrays.asList(1,2,3));
        teamAction.setUserIds(userIds);
        teamAction.setUsersChanged(true);
        expect(teamBusiness.storeTeam(team1, userIds, null, null)).andReturn(returned);
        
        replayAll();
        assertEquals(Action.SUCCESS, teamAction.store());
        verifyAll();
        
        assertEquals(returned, teamAction.getTeam());
    }
    
    @Test
    public void testStore_doNotChangeUsers() {
        teamAction.setTeam(team1);
        Team returned = new Team();
        teamAction.setUserIds(new HashSet<Integer>(Arrays.asList(1,2,3)));
        teamAction.setUsersChanged(false);
        expect(teamBusiness.storeTeam(team1, null, null, null)).andReturn(returned);
        
        replayAll();
        assertEquals(Action.SUCCESS, teamAction.store());
        verifyAll();
        
        assertEquals(returned, teamAction.getTeam());
    }
}