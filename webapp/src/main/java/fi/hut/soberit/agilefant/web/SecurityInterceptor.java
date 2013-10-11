package fi.hut.soberit.agilefant.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

import fi.hut.soberit.agilefant.business.AuthorizationBusiness;
import fi.hut.soberit.agilefant.business.BacklogBusiness;
import fi.hut.soberit.agilefant.business.IterationBusiness;
import fi.hut.soberit.agilefant.business.StoryBusiness;
import fi.hut.soberit.agilefant.business.TaskBusiness;
import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.security.SecurityUtil;

@Component("securityInterceptor")
public class SecurityInterceptor implements Interceptor {

	private static final long serialVersionUID = -7924331771689956188L;

	@Autowired
    private BacklogBusiness backlogBusiness;
    
    @Autowired
    private IterationBusiness iterationBusiness;

    @Autowired
    private StoryBusiness storyBusiness;
    
    @Autowired
    private TaskBusiness taskBusiness;
    
    @Autowired
    private UserBusiness userBusiness;
    
    @Autowired
    private AuthorizationBusiness authorizationBusiness;
    
    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        //System.out.println("URL: " + ServletActionContext.getRequest().getRequestURL().toString());
        HttpServletRequest req = ServletActionContext.getRequest();
        String actionName = ServletActionContext.getActionMapping().getName();
        
        User loggedUser = SecurityUtil.getLoggedUser(); // SecurityUtil.getLoggedUser() can't get all needed information of user -> should retrieve by making new user.
        User user = userBusiness.retrieve(loggedUser.getId());
        
        boolean admin = user.isAdmin();
        boolean readOnly = user.getName().equals("readonly");
        boolean access = false;
        
        if(admin){
            //if admin, everything is fine
            access = true;
        } else if(readOnly){
            //check read only operations
            if(actionName.equals("ROIterationHistoryByToken") 
                    || actionName.equals("ROIterationMetricsByToken")
                    || actionName.equals(("ROIterationData"))){
                access = true;
            }
        } else {
            if(actionName.equals("createTeam")
                    || actionName.equals("deleteTeam")
                    || actionName.equals("deleteTeamForm")
                    || actionName.equals("storeTeam")
                    || actionName.equals("storeNewTeam")){
                
                //these are admin-only operations
                access = false;
            
            } else if(actionName.equals("storeUser")){
            
                //check if ID is of current user, and what is being stored
                //can't set user.admin or team
                Map params = req.getParameterMap();
                boolean attemptAdmin = params.containsKey("user.admin");
                boolean attemptTeam = params.containsKey("teamsChanged") || params.containsKey("teamIds");
                int id = Integer.parseInt(((String[]) params.get("userId"))[0]);

                if(id == user.getId() && !attemptAdmin && !attemptTeam){
                    //check not setting user.admin
                    access = true;
                }
            } else if(actionName.equals("storeNewUser")) {
                Map params = req.getParameterMap();
                boolean attemptToCreateNonAdmin = params.containsKey("user.admin") && ((String[]) params.get("user.admin"))[0].equals("false");
                // Non admins can create only other non admin users
                if(attemptToCreateNonAdmin) {
                	access = true;
                }
            } else if(actionName.equals("retrieveAllProducts")
                    || actionName.equals("retrieveAllSAIterations")){
                //access matrix operations
                access = false;
            } else if(actionName.equals("storeNewIteration")
                    || actionName.equals("storeNewProduct")) {
                // these are operations available to everyone
                access = true;
            } else {
                // Default case: Try to find a backlog id of some kind to check.
                
                Map params = req.getParameterMap();
                int id = -1;
                if(params.containsKey("iterationId"))
                    id = Integer.parseInt(((String[]) params.get("iterationId"))[0]);
                else if (params.containsKey("backlogId"))
                    id = Integer.parseInt(((String[]) params.get("backlogId"))[0]);
                else if (params.containsKey("productId"))
                    id = Integer.parseInt(((String[]) params.get("productId"))[0]);
                else if (params.containsKey("projectId"))
                    id = Integer.parseInt(((String[]) params.get("projectId"))[0]);
                else if (params.containsKey("taskId")){
                    int taskId = Integer.parseInt(((String[]) params.get("taskId"))[0]);
                    Task task = taskBusiness.retrieve(taskId);
                    if(task.getIteration() != null)
                        id = task.getIteration().getId();
                    else if (task.getStory().getIteration() != null)
                        id = task.getStory().getIteration().getId();
                    else 
                        id = task.getStory().getBacklog().getId(); // story in project/product w/a iteration
                } else if (params.containsKey("storyId")){
                    int storyId = Integer.parseInt(((String[]) params.get("storyId"))[0]);
                    Story story = storyBusiness.retrieve(storyId);
                    if (story.getIteration() != null) {
                        id = story.getIteration().getId();
                    } else {
                        id = story.getBacklog().getId();
                    }
                }
                
                boolean attemptTeam = params.containsKey("teamsChanged");
                if(!attemptTeam){
                    if (id != -1)
                        access = this.authorizationBusiness.isBacklogAccessible(id, user);
                    else
                        // Operations without ids must be allowed
                        access = true;
                }
            }
        }
                
        if(access)
            return invocation.invoke();
        else
            return "noauth";
    }
}

