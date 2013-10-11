package fi.hut.soberit.agilefant.business.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.AuthorizationBusiness;
import fi.hut.soberit.agilefant.business.BacklogBusiness;
import fi.hut.soberit.agilefant.business.HourEntryBusiness;
import fi.hut.soberit.agilefant.business.IterationBusiness;
import fi.hut.soberit.agilefant.business.ProductBusiness;
import fi.hut.soberit.agilefant.business.ProjectBusiness;
import fi.hut.soberit.agilefant.business.StoryBusiness;
import fi.hut.soberit.agilefant.business.TeamBusiness;
import fi.hut.soberit.agilefant.business.TransferObjectBusiness;
import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.model.Assignment;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Schedulable;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.Team;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.model.WhatsNextEntry;
import fi.hut.soberit.agilefant.model.WhatsNextStoryEntry;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import fi.hut.soberit.agilefant.transfer.AssignedWorkTO;
import fi.hut.soberit.agilefant.transfer.AutocompleteDataNode;
import fi.hut.soberit.agilefant.transfer.DailyWorkTaskTO;
import fi.hut.soberit.agilefant.transfer.IterationTO;
import fi.hut.soberit.agilefant.transfer.ProjectTO;
import fi.hut.soberit.agilefant.transfer.ScheduleStatus;
import fi.hut.soberit.agilefant.transfer.StoryTO;
import fi.hut.soberit.agilefant.transfer.TaskTO;

@Service("transferObjectBusiness")
@Transactional(readOnly = true)
public class TransferObjectBusinessImpl implements TransferObjectBusiness {

    @Autowired
    private BacklogBusiness backlogBusiness;
    
    @Autowired
    private ProductBusiness productBusiness;
    
    @Autowired
    private ProjectBusiness projectBusiness;
    
    @Autowired
    private HourEntryBusiness hourEntryBusiness;
    
    @Autowired
    private UserBusiness userBusiness;
    
    @Autowired
    private TeamBusiness teamBusiness;

    @Autowired
    private IterationBusiness iterationBusiness;
    
    @Autowired
    private StoryBusiness storyBusiness;
   
    @Autowired
    private AuthorizationBusiness authorizationBusiness;
  
    private void fillInEffortSpent(TaskTO taskTO) {
        taskTO.setEffortSpent(hourEntryBusiness.calculateSum(taskTO.getHourEntries()));
    }
    
    private void fillInEffortSpent(StoryTO storyTO) {
        storyTO.setEffortSpent(hourEntryBusiness.calculateSum(storyTO.getHourEntries()));
    }
    
    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public TaskTO constructTaskTO(Task task) {
        TaskTO taskTO = new TaskTO(task);
        fillInEffortSpent(taskTO);
        return taskTO;
    }
    
    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public StoryTO constructStoryTO(Story story) {
        StoryTO returned = new StoryTO(story);
        returned.setTasks(new HashSet<Task>());
        
        returned.setMetrics(storyBusiness.calculateMetrics(story));
        
        for (Task task : story.getTasks()) {
            TaskTO taskTO = this.constructTaskTO(task);
            returned.getTasks().add(taskTO);
        }
        
        return returned;
    }
    
    @Transactional(readOnly = true)
    public IterationTO constructIterationTO(Iteration iteration) {
        IterationTO returned = new IterationTO(iteration);
        returned.setScheduleStatus(this.getBacklogScheduleStatus(iteration));
        
        returned.setAssignees(new HashSet<User>());
        
        for(Assignment assignment : iteration.getAssignments()) {
            returned.getAssignees().add(assignment.getUser());
        }
        
        returned.setTeams(new HashSet<Team>());
        
        for(Team team : iteration.getTeams()) {
            returned.getTeams().add(team);
        }
        
        return returned;
    }
    
    @Transactional(readOnly = true)
    public ProjectTO constructProjectTO(Project project) {
        ProjectTO returned = new ProjectTO(project);
        returned.setScheduleStatus(this.getBacklogScheduleStatus(project));        
        returned.setAssignees(new HashSet<User>());
        
        for(Assignment assignment : project.getAssignments()) {
            returned.getAssignees().add(assignment.getUser());
        }
        
        return returned;
    }
    
    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public List<AutocompleteDataNode> constructUserAutocompleteData() {
        User loggedUser = getloggedUser();
        Collection<User> allUsers = new ArrayList<User>();
        if (loggedUser.isAdmin()) {
            allUsers = this.userBusiness.retrieveAll();
        } else {
            Collection<Team> teams = loggedUser.getTeams();
            if (teams != null)
                for (Team team : teams) {
                    Collection<User> users = team.getUsers();
                    if (users != null)
                        for (User user : users) {
                            if (!allUsers.contains(user)) {
                                allUsers.add(user);
                            }
                        }
                    users.clear();
                }
            teams.clear();
        }
        List<AutocompleteDataNode> autocompleteData = new ArrayList<AutocompleteDataNode>();
        for(User user : allUsers) {
            AutocompleteDataNode curNode = new AutocompleteDataNode(User.class,
                    user.getId(), user.getFullName(), user.isEnabled());
            curNode.setMatchedString(user.getFullName() + " " + user.getLoginName());
            curNode.setOriginalObject(user);
            autocompleteData.add(curNode);
        }
        return autocompleteData;
    }
    
    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public List<AutocompleteDataNode> constructTeamAutocompleteData(boolean listUserIds) {
        User loggedUser = SecurityUtil.getLoggedUser();
        User user = userBusiness.retrieve(loggedUser.getId()); // TODO: Why user.getTeams() shows null?
        Collection<Team> allTeams = new ArrayList<Team>();
        if (user.isAdmin()) {
            allTeams = this.teamBusiness.retrieveAll();
        } else {
            allTeams = user.getTeams();
        }
        List<AutocompleteDataNode> autocompleteData = new ArrayList<AutocompleteDataNode>();
        if (allTeams != null)
        for(Team team : allTeams) {
                Set<Integer> userIds = null;
                if (listUserIds) {
                    userIds = new HashSet<Integer>();
                    for (User u : team.getUsers()) {
                        userIds.add(u.getId());
                    }
                }
                AutocompleteDataNode curNode = new AutocompleteDataNode(Team.class,
                        team.getId(), team.getName(), userIds);
                curNode.setMatchedString(team.getName());
                curNode.setOriginalObject(team);
                autocompleteData.add(curNode);
        }
        return autocompleteData;
    }
    

    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public List<AutocompleteDataNode> constructBacklogAutocompleteData(Integer backlogId) {
        Collection<Backlog> allBacklogs = this.backlogBusiness.retrieveAll();
        if (backlogId != null) {
            Collection<Backlog> filteredBacklogs = new ArrayList<Backlog>();
            Backlog original = this.backlogBusiness.retrieve(backlogId);
            Product filterBy = this.backlogBusiness.getParentProduct(original);
            for (Backlog backlog : allBacklogs) {
                if (this.backlogBusiness.getParentProduct(backlog) == filterBy) {
                    filteredBacklogs.add(backlog);
                }
            }
            allBacklogs = filteredBacklogs;
        }
        List<AutocompleteDataNode> autocompleteData = getBacklogDataRecurseNames(allBacklogs);
        return autocompleteData; 
    }

    private boolean checkAccess(Backlog bl){
        return this.authorizationBusiness.isBacklogAccessible(bl.getId(), SecurityUtil.getLoggedUser());
    }

    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public List<AutocompleteDataNode> constructProductAutocompleteData() {
        Collection<Product> allBacklogs = this.productBusiness.retrieveAll();
        List<AutocompleteDataNode> autocompleteData = getBacklogDataRecurseNames(allBacklogs);
        return autocompleteData; 
    }
    
    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public List<AutocompleteDataNode> constructProjectAutocompleteData() {
        Collection<Project> allBacklogs = this.projectBusiness.retrieveAll();
        List<AutocompleteDataNode> autocompleteData = getBacklogDataRecurseNames(allBacklogs);
        return autocompleteData; 
    }

    
    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public List<AutocompleteDataNode> constructBacklogAndIterationAutocompleteData(Integer backlogId) {
        List<AutocompleteDataNode> autocompleteData = constructBacklogAutocompleteData(backlogId);
        autocompleteData.addAll(constructCurrentIterationAutocompleteData());
        return autocompleteData; 
    }
    
    private List<AutocompleteDataNode> getBacklogDataRecurseNames(
            Collection<? extends Backlog> allBacklogs) {
        List<AutocompleteDataNode> autocompleteData = new ArrayList<AutocompleteDataNode>();
        
        for (Backlog blog : allBacklogs) { 
            if(blog instanceof Iteration){
                continue; // iterations should not be included in backlogs list.
            }
            if (checkAccess(blog)) {
                String name = recurseBacklogNameWithParents(blog);
                AutocompleteDataNode node = new AutocompleteDataNode(Backlog.class, blog.getId(), name);
                node.setMatchedString(name);
                autocompleteData.add(node);
                node.setOriginalObject(blog);
            } 
        }
        
        return autocompleteData;
    }
    
    private String recurseBacklogNameWithParents(Backlog blog) {
        Backlog parent = blog.getParent();
        String name = blog.getName();
        while (parent != null) {
            name = parent.getName() + " > " + name;
            parent = parent.getParent();
        }
        return name;
    }
    
    
    
   
    @Transactional(readOnly = true)
    public ScheduleStatus getBacklogScheduleStatus(Backlog backlog) {
        if (backlog instanceof Product) {
            return ScheduleStatus.ONGOING;
        }
        Schedulable blog = (Schedulable)backlog;
        Interval interval = new Interval(blog.getStartDate(), blog.getEndDate());
        if (interval.isBeforeNow()) {
            return ScheduleStatus.PAST;
        }
        else if (interval.isAfterNow()) {
            return ScheduleStatus.FUTURE;
        }
        return ScheduleStatus.ONGOING;
    }
    
    @Transactional(readOnly = true)
    public List<AutocompleteDataNode> constructCurrentIterationAutocompleteData() {
        Collection<Iteration> currentAndFutureIterations = this.iterationBusiness.retrieveCurrentAndFutureIterations();
        List<AutocompleteDataNode> autocompleteData = new ArrayList<AutocompleteDataNode>();
        for (Backlog blog : currentAndFutureIterations) {
            if (checkAccess(blog)) {
                String name = recurseBacklogNameWithParents(blog);
                AutocompleteDataNode node = new AutocompleteDataNode(Backlog.class,
                        blog.getId(), name);
                node.setOriginalObject(blog);
                autocompleteData.add(node);
            }
        }
        return autocompleteData; 
    }

    
    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public DailyWorkTaskTO constructQueuedDailyWorkTaskTO(WhatsNextEntry entry) {
        Task task = entry.getTask();
        DailyWorkTaskTO toReturn = new DailyWorkTaskTO(task);
        fillInEffortSpent(toReturn);
        toReturn.setWorkQueueRank(entry.getRank());

        return toReturn;
    }
    
    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public StoryTO constructQueuedStoryTO(WhatsNextStoryEntry entry) {
        Story story = entry.getStory();
        StoryTO toReturn = new StoryTO(story);
        fillInEffortSpent(toReturn);
        toReturn.setWorkQueueRank(entry.getRank());

        return toReturn;
    }
    
    protected StoryTO createStoryTOWithTaskTOs(Story story) {
        StoryTO to = constructStoryTO(story);
        Set<Task> storyTasks = to.getTasks();
        Set<Task> taskTos = new HashSet<Task>();
        
        for (Task t: storyTasks) {
            taskTos.add(constructTaskTO(t));
        }
        
        to.setTasks(taskTos);

        to.setMetrics(storyBusiness.calculateMetrics(to));
        return to;
    }
    
    public AssignedWorkTO constructAssignedWorkTO(Collection<Task> tasks, Collection<Story> assignedStories) {
        AssignedWorkTO returned = new AssignedWorkTO();
        
        Set<Story> stories = new HashSet<Story>();
        List<StoryTO> storyTOs = new ArrayList<StoryTO>();
        List<Task> tasksWithoutStory = new ArrayList<Task>();
        
        for (Task task: tasks) {
            Story story = task.getStory();
            if (task.getStory() != null) {
                if (stories.contains(story)) {
                    continue;
                }
                
                stories.add(story);
                storyTOs.add(createStoryTOWithTaskTOs(story));
            }
            else {
                tasksWithoutStory.add(constructTaskTO(task));
            }
        }
        
        for (Story story: assignedStories) {
            if (! stories.contains(story)) {
                if (!story.getState().equals("Done")) {
                    stories.add(story);
                    storyTOs.add(createStoryTOWithTaskTOs(story));
                }
            }
        }
        
        returned.setTasksWithoutStory(tasksWithoutStory);
        returned.setStories(storyTOs);
        return returned;
    }
    
    /*
     * GETTERS AND SETTERS
     */
    
    public void setHourEntryBusiness(HourEntryBusiness hourEntryBusiness) {
        this.hourEntryBusiness = hourEntryBusiness;
    }

    public void setUserBusiness(UserBusiness userBusiness) {
        this.userBusiness = userBusiness;
    }

    public void setTeamBusiness(TeamBusiness teamBusiness) {
        this.teamBusiness = teamBusiness;
    }

    public void setBacklogBusiness(BacklogBusiness backlogBusiness) {
        this.backlogBusiness = backlogBusiness;
    }

    public void setIterationBusiness(IterationBusiness iterationBusiness) {
        this.iterationBusiness = iterationBusiness;
    }

    public void setProductBusiness(ProductBusiness productBusiness) {
        this.productBusiness = productBusiness;
    }

    public void setProjectBusiness(ProjectBusiness projectBusiness) {
        this.projectBusiness = projectBusiness;
    }

    public void setStoryBusiness(StoryBusiness storyBusiness) {
        this.storyBusiness = storyBusiness;
    }
    
    public void setAuthorizationBusiness(AuthorizationBusiness authorizationBusiness) {
        this.authorizationBusiness = authorizationBusiness;
    }
    
    private User getloggedUser() {
        User loggedUser = SecurityUtil.getLoggedUser();
        User user = userBusiness.retrieve(loggedUser.getId());
        return user;
    }

}
