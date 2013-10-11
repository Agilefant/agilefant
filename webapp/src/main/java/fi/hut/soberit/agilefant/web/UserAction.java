package fi.hut.soberit.agilefant.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.annotations.PrefetchId;
import fi.hut.soberit.agilefant.business.TeamBusiness;
import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.model.Team;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.security.SecurityUtil;

/**
 * UserAction
 * 
 * @author khel
 */
@Component("userAction")
@Scope("prototype")
public class UserAction extends ActionSupport implements CRUDAction, Prefetching {

    private static final long serialVersionUID = 284890678155663442L;

    @PrefetchId
    private int userId;

    private User user;
    private User loggedUser;
    
    private String password1;
    private String password2;
    
    private String loginName;
    private boolean valid;
    private boolean isAdmin;

    private Collection<User> users = new ArrayList<User>();
    
    private Set<Integer> teamIds = new HashSet<Integer>();
    private boolean teamsChanged = false;
    
    @Autowired
    private UserBusiness userBusiness;
    
    @Autowired
    private TeamBusiness teamBusiness;

    @Override
    public String execute() {
        
        if (userId == 0) {
            userId = getLoggedInUserId();
        }

        user = userBusiness.retrieve(userId);

        return Action.SUCCESS;
    }
    
    protected int getLoggedInUserId() {
        return SecurityUtil.getLoggedUserId();
    }
    
    public String create() {
        user = new User();
        user.setEnabled(true);
        return Action.SUCCESS;
    }

    public String retrieve() {
        
        this.getLoggedUserAndCheckAdminStatus();
        
        if (userId == 0) {
            userId = getLoggedInUserId();
        }
        
        if (!isAdmin) {
            this.getTeamMembers(loggedUser);
            user = userBusiness.retrieve(userId);
            if (!users.contains(user)) {
                user = loggedUser;
            }
        } else {
            user = userBusiness.retrieve(userId);
        }
        return Action.SUCCESS;
    }
    
    public String retrieveAll() {
        
        this.getLoggedUserAndCheckAdminStatus();
        
        if (isAdmin) {
            users = userBusiness.retrieveAll();
        } else {
            this.getTeamMembers(loggedUser);
        }
        return Action.SUCCESS;
    }
    
    public String store() {
        Set<Integer> teams = null;
        if (teamsChanged) {
            teams = teamIds;
        }
        user = userBusiness.storeUser(user, teams, password1, password2);
        return Action.SUCCESS;
    }
    
    public String delete() {
        userBusiness.retrieve(userId);
        return Action.SUCCESS;
    }
    
    public String disable() {
        user = userBusiness.retrieve(userId);
        userBusiness.disableUser(user.getId());
        
        return Action.SUCCESS;
    }
    
    public String enable() {
        user = userBusiness.retrieve(userId);
        userBusiness.enableUser(user.getId());
        
        return Action.SUCCESS;
    }
    
    public String checkLoginName() {
        valid = userBusiness.isLoginNameUnique(loginName);
        return Action.SUCCESS;
    }
    
    
    public void initializePrefetchedData(int objectId) {
        user = userBusiness.retrieve(objectId);
    }

    /*
     * GETTERS AND SETTERS
     */
    public void setUserBusiness(UserBusiness userBusiness) {
        this.userBusiness = userBusiness;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }
    
    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public Set<Integer> getTeamIds() {
        return teamIds;
    }

    public void setTeamIds(Set<Integer> teamIds) {
        this.teamIds = teamIds;
    }

    public void setTeamsChanged(boolean teamsChanged) {
        this.teamsChanged = teamsChanged;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isValid() {
        return valid;
    }
    
    protected User getLoggedInUser() {
        return SecurityUtil.getLoggedUser();
    }
    private void getTeamMembers(User loggedUser) {
    	this.users = this.teamBusiness.getUsersInSameTeams(loggedUser.getId());
    }
    private void getLoggedUserAndCheckAdminStatus() {
        loggedUser = getLoggedInUser();
        isAdmin = loggedUser.isAdmin();
    }
}
