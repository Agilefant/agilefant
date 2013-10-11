package fi.hut.soberit.agilefant.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.business.DailyWorkBusiness;
import fi.hut.soberit.agilefant.business.StoryBusiness;
import fi.hut.soberit.agilefant.business.TaskBusiness;
import fi.hut.soberit.agilefant.business.TeamBusiness;
import fi.hut.soberit.agilefant.business.TransferObjectBusiness;
import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.Team;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import fi.hut.soberit.agilefant.transfer.AssignedWorkTO;
import fi.hut.soberit.agilefant.transfer.DailyWorkTaskTO;
import fi.hut.soberit.agilefant.transfer.StoryTO;

@Component("dailyWorkAction")
@Scope("prototype")
public class DailyWorkAction extends ActionSupport {
    private static final long serialVersionUID = -1891256847796843738L;
    
    @Autowired
    private DailyWorkBusiness dailyWorkBusiness;
    
    @Autowired
    private UserBusiness userBusiness;

    @Autowired
    private TaskBusiness taskBusiness;
    
    @Autowired
    private StoryBusiness storyBusiness;

    @Autowired
    private TransferObjectBusiness transferObjectBusiness;
    
    @Autowired
    private TeamBusiness teamBusiness;

    private int  userId;
    private User user; 

    private List<User> enabledUsers                 = new ArrayList<User>();
    private Collection<DailyWorkTaskTO> queuedTasks = new ArrayList<DailyWorkTaskTO>();
    private Collection<StoryTO> stories = new ArrayList<StoryTO>();
    private Collection<Task> tasksWithoutStory = new ArrayList<Task>();

    private int  taskId;
    private int  rankUnderId;
    private Task task;
    
    private int storyId;


    private int storyRankUnderId;


    private Story story;


    private boolean containsUser(Collection<User> users, int userId) {
    	for(User user : users) {
    		if(user.getId()==userId) {
    			return true;
    		}
    	}
    	return false;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public String execute() {
        /* 
         * Non-admin user can watch his/her own daily work and also of other users who belong to a same team
         * Admin has priviledge to view daily works of all users.
         * This also prevent non-admin to try url-hack other user's daily work who do not belong to his/her team. 
         * Non-admin user trying to see daily work of someone not in his/her team -> userId = 0
         */
        User loggedUser = getLoggedInUser();
        
        if(this.userId==0) {
        	this.userId = loggedUser.getId();
        }

        if(loggedUser.isAdmin()) {
            enabledUsers.addAll(userBusiness.getEnabledUsers());        	
        } else {
        	// verify that logged in user shares a team with the supplied user
            Collection<User> teamUsers = this.teamBusiness.getUsersInSameTeams(loggedUser.getId());
        	if(!this.containsUser(teamUsers, userId)) {
        		int storedUserId = getStoredDailyWorkUserId();
        		if(this.containsUser(teamUsers, storedUserId)) {
                    this.userId = storedUserId;        			
        		} else {
        			this.userId = loggedUser.getId();
        		}
        	}
        	
        	// update list of users used in the dropdown
            enabledUsers.addAll(teamUsers);        	
        }
        
        user = this.userBusiness.retrieve(userId);

        Collections.sort(enabledUsers, new PropertyComparator("fullName", true, true));
        
        return Action.SUCCESS;
    }
    /**
     * Retrieve for JSON data.
     * @return
     */
    public String retrieve() {
        user = this.userBusiness.retrieve(userId);
        queuedTasks = dailyWorkBusiness.getQueuedTasksForUser(user);
        AssignedWorkTO assignedWork = dailyWorkBusiness.getAssignedWorkFor(user);
        this.tasksWithoutStory = assignedWork.getTasksWithoutStory();
        
        retrieveStories(assignedWork);
        
        return Action.SUCCESS;
    }
    
    private void retrieveStories(AssignedWorkTO assignedWork) {
        Collection<StoryTO> rankedStories = dailyWorkBusiness.getQueuedStoriesForUser(user);
        Collection<StoryTO> unrankedStories = assignedWork.getStories();
        this.stories = new ArrayList<StoryTO>();
        int unrankedNumber = 10000;
        for (StoryTO storyTO: unrankedStories) {
            Integer rank = getStoryRank(storyTO, rankedStories);
            if (rank == null) {
                rank = unrankedNumber;
                unrankedNumber++;
            }
            storyTO.setWorkQueueRank(rank);
            this.stories.add(storyTO);
        }
    }
    
    public String retrieveWorkQueue() {
        user = this.userBusiness.retrieve(userId);
        queuedTasks = dailyWorkBusiness.getQueuedTasksForUser(user);
       
        return Action.SUCCESS;
    }
    
    public String retrieveAssignedStories() {
        user = this.userBusiness.retrieve(userId);
        AssignedWorkTO assignedWork = dailyWorkBusiness.getAssignedWorkFor(user);
        retrieveStories(assignedWork);
        
        return Action.SUCCESS;
    }
    
    private Integer getStoryRank(StoryTO storyTO, Collection<StoryTO> rankedStories) {
        for (StoryTO rankedStory: rankedStories) {
            if (rankedStory.getId() == storyTO.getId()) {
                return rankedStory.getWorkQueueRank();
            }
        }
        return null;
    }
    
    public String retrieveAssignedTasks() {
        user = this.userBusiness.retrieve(userId);
        AssignedWorkTO assignedWork = dailyWorkBusiness.getAssignedWorkFor(user);
        this.tasksWithoutStory = assignedWork.getTasksWithoutStory();
        
        return Action.SUCCESS;
    }

    public String deleteFromWorkQueue() {
        User thisUser = getDefaultUser();
        Task task = taskBusiness.retrieve(taskId);
        
        dailyWorkBusiness.removeFromWhatsNext(thisUser, task);
        
        this.setTask(transferObjectBusiness.constructTaskTO(task));
        return Action.SUCCESS;
    }
    
    public String addToWorkQueue() {
        User thisUser = getDefaultUser();
        Task task = taskBusiness.retrieve(taskId);

        dailyWorkBusiness.addToWhatsNext(thisUser, task);
        
        this.setTask(transferObjectBusiness.constructTaskTO(task));
        return Action.SUCCESS;
    }
    
    public String rankQueueTaskAndMoveUnder() {
        User user = getDefaultUser();
        Task task = taskBusiness.retrieve(taskId);
        
        dailyWorkBusiness.rankUnderTaskOnWhatsNext(user, task, taskBusiness.retrieveIfExists(rankUnderId));
        
        return Action.SUCCESS;
    }
    
    public String rankMyStoryAndMoveUnder() {
        User user = getDefaultUser();
        Story story = storyBusiness.retrieve(storyId);

        Story storyRankUnder = storyBusiness.retrieveIfExists(storyRankUnderId);
        if (storyRankUnder != null) {
            AssignedWorkTO assignedWork = dailyWorkBusiness.getAssignedWorkFor(user);
            Collection<StoryTO> rankedStories = dailyWorkBusiness.getQueuedStoriesForUser(user);
            Collection<StoryTO> unrankedStories = assignedWork.getStories();
            Integer storyRankUnderIdRank = null;
            for (StoryTO storyTO: unrankedStories) {
                if (storyTO.getId() == storyRankUnderId) {
                    storyRankUnderIdRank = getStoryRank(storyTO, rankedStories);
                }
            }
            if (storyRankUnderIdRank == null) {
                for (StoryTO storyTO: unrankedStories) {
                    Integer rank = getStoryRank(storyTO, rankedStories);
                    if (rank == null && storyTO.getId() != storyId) {
                        addToWhatsNext(storyTO.getId());
                    }
                    
                    if (storyTO.getId() == storyRankUnderId) {
                        break;
                    }
                }
            }
        }
        
        dailyWorkBusiness.rankUnderStoryOnWhatsNext(user, story, storyBusiness.retrieveIfExists(storyRankUnderId));
        
        return Action.SUCCESS;
    }
    
    private void addToWhatsNext(int id) {
        User user = getDefaultUser();
        Story story = storyBusiness.retrieve(id);
        
        dailyWorkBusiness.addToWhatsNext(user, story);
    }
    
    protected User getDefaultUser() {
        if (userId == 0) {
            userId = getLoggedInUserId();
        }
        
        return userBusiness.retrieve(userId);
    }
    
    protected int getStoredDailyWorkUserId() {
        /*
         * Get the user id from session variables. This enables the Daily Work
         * page to remember the selected user.
         */
        int dailyWorkUserId = 0;
        if (ActionContext.getContext() != null
                && ActionContext.getContext().getSession() != null) {
            Object sessionUser = ActionContext.getContext().getSession().get(
                    "dailyWorkUserId");

            if (sessionUser != null) {
                dailyWorkUserId = (Integer) sessionUser;
            }
        }
        
        return dailyWorkUserId;
    }
    
    protected int getLoggedInUserId() {
        return SecurityUtil.getLoggedUserId();
    }
    
    protected User getLoggedInUser() {
        return SecurityUtil.getLoggedUser();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Collection<DailyWorkTaskTO> getQueuedTasks() {
        return queuedTasks;
    }
    
    public User getUser() {
        return user;
    }
    
    public Collection<User> getEnabledUsers() {
        return enabledUsers;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId; 
    }
    
    public void setRankUnderId(int rankUnderId) {
        this.rankUnderId = rankUnderId;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
    
    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }
    
    public void setStoryRankUnderId(int storyRankUnderId) {
        this.storyRankUnderId = storyRankUnderId;
    }
    
    public Story getStory() {
        return story;
    }
    
    public void setStory(Story story) {
        this.story = story;
    }

    public void setUserBusiness(UserBusiness userBusiness) {
        this.userBusiness = userBusiness;
    }

    public void setDailyWorkBusiness(DailyWorkBusiness dailyWorkBusiness) {
        this.dailyWorkBusiness = dailyWorkBusiness;
    }

    public void setTaskBusiness(TaskBusiness taskBusiness) {
        this.taskBusiness = taskBusiness;
    }

    public void setTransferObjectBusiness(TransferObjectBusiness transferObjectBusiness) {
        this.transferObjectBusiness = transferObjectBusiness;
    }

    public Collection<StoryTO> getStories() {
        return stories;
    }

    public Collection<Task> getTasksWithoutStory() {
        return tasksWithoutStory;
    }
}