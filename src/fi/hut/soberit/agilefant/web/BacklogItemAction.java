package fi.hut.soberit.agilefant.web;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionSupport;

import fi.hut.soberit.agilefant.business.BacklogBusiness;
import fi.hut.soberit.agilefant.business.BacklogItemBusiness;
import fi.hut.soberit.agilefant.business.BusinessThemeBusiness;
import fi.hut.soberit.agilefant.business.HistoryBusiness;
import fi.hut.soberit.agilefant.business.HourEntryBusiness;
import fi.hut.soberit.agilefant.exception.ObjectNotFoundException;
import fi.hut.soberit.agilefant.model.AFTime;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.BacklogItem;
import fi.hut.soberit.agilefant.model.BusinessTheme;
import fi.hut.soberit.agilefant.model.State;
import fi.hut.soberit.agilefant.model.Task;

public class BacklogItemAction extends ActionSupport implements CRUDAction {

    private static final long serialVersionUID = -4289013472775815522L;

    private HistoryBusiness historyBusiness;

    private int backlogId = 0;

    private int backlogItemId;

    private State state;

    private AFTime effortLeft;

    private BacklogItem backlogItem;

    private Backlog backlog;

    private int iterationGoalId;
    
    private Map<Integer, String> userIds = new HashMap<Integer, String>();
    
    private Set<Integer> themeIds = new HashSet<Integer>();
    
    private BacklogBusiness backlogBusiness;

    private BacklogItemBusiness backlogItemBusiness;
    
    private BusinessThemeBusiness businessThemeBusiness;
        
    private HourEntryBusiness hourEntryBusiness;

    private Map<Integer, State> taskStates = new HashMap<Integer, State>();
    
    private Map<Integer, String> taskNames = new HashMap<Integer, String>();
    
    private boolean tasksToDone = false; 
    
    private String spentEffort = null;
    
    private String spentEffortComment = null;
        
    private String bliListContext;
    
    private List<BusinessTheme> bliActiveOrSelectedThemes;

    public String getBliListContext() {
        return bliListContext;
    }

    public void setBliListContext(String bliListContext) {
        this.bliListContext = bliListContext;
    }

    public Map<Integer, State> getTaskStates() {
        return taskStates;
    }

    public void setTaskStates(Map<Integer, State> taskStates) {
        this.taskStates = taskStates;
    }

    public BacklogItemBusiness getBacklogItemBusiness() {
        return backlogItemBusiness;
    }

    public void setBacklogItemBusiness(BacklogItemBusiness backlogItemBusiness) {
        this.backlogItemBusiness = backlogItemBusiness;
    }

    public BacklogBusiness getBacklogBusiness() {
        return backlogBusiness;
    }

    public void setBacklogBusiness(BacklogBusiness backlogBusiness) {
        this.backlogBusiness = backlogBusiness;
    }

    public String create() {
        // Id of newly created, not yet persisted backlog item is 0
        backlogItemId = 0;
        
        if (backlogId == 0) {
            backlogItem = new BacklogItem();
            return Action.SUCCESS;
        } else {
            backlogItem = backlogBusiness.createBacklogItemToBacklog(backlogId);
            if (backlogItem == null) {
                super.addActionError(super.getText("backlog.notFound"));
                return Action.ERROR;
            }
            backlog = backlogItem.getBacklog();
            backlogId = backlog.getId();
            return Action.SUCCESS;
        }
    }
    
    public String delete() {
        try {
            BacklogItem bli;
            if((bli = this.backlogItemBusiness.getBacklogItem(backlogItemId)) != null) {
                backlogId = bli.getBacklog().getId();
            }
            backlogItemBusiness.removeBacklogItem(backlogItemId);
        } catch (ObjectNotFoundException e) {
            super.addActionError(super.getText("backlogItem.notFound"));
            return ERROR;
        }

        // If exception was not thrown from business method, return success.
        return SUCCESS;
    }
    public String ajaxDeleteBacklogItem() {
        try {
            backlogItemBusiness.removeBacklogItem(backlogItemId);
        } catch (ObjectNotFoundException e) {
            super.addActionError(super.getText("backlogItem.notFound"));
            return CRUDAction.AJAX_ERROR;
        }

        // If exception was not thrown from business method, return success.
        return CRUDAction.AJAX_SUCCESS;
    }

    public String edit() {
        backlogItem = backlogItemBusiness.getBacklogItem(backlogItemId);
        if (backlogItem == null) {
            super.addActionError(super.getText("backlogItem.notFound"));
            return Action.ERROR;
        }
        backlog = backlogItem.getBacklog();
        backlogId = backlog.getId();
        
        historyBusiness.updateBacklogHistory(backlog.getId());
        bliActiveOrSelectedThemes = businessThemeBusiness.getBacklogItemActiveOrSelectedThemes(backlogItemId);

        return Action.SUCCESS;
    }
    
    public String store() {
        if(this.bliStore() == false) {
            return ERROR;
        }
        return SUCCESS;
    }
    public String ajaxStoreBacklogItem() {
        if(this.bliStore() == false) {
            return CRUDAction.AJAX_ERROR;
        }
        return CRUDAction.AJAX_SUCCESS;
    }
    
    private boolean bliStore() {
        //validate original estimate, name and effort left
        if (this.backlogItem.getName() == null || 
                this.backlogItem.getName().trim().equals("")) {
            return false;
        }
        if (this.backlogItem.getEffortLeft() != null && this.backlogItem.getEffortLeft().getTime() < 0) {
            return false;
        }        
        if (this.backlogItem.getOriginalEstimate() != null && this.backlogItem.getOriginalEstimate().getTime() < 0) {
            return false;
        }
        
        //save backlog item, update todos and store backlog item themes
        try {
            BacklogItem bli = backlogItemBusiness.storeBacklogItem(backlogItemId, backlogId, backlogItem, userIds.keySet(), iterationGoalId);
            if (tasksToDone) {
                backlogItemBusiness.setTasksToDone(backlogItemId);
            }
            businessThemeBusiness.setBacklogItemThemes(themeIds, bli);
            backlogItem = bli;
            backlogItemId = bli.getId();
          }
        catch(ObjectNotFoundException onfe) {
            return false;
        }
        catch(IllegalArgumentException e) {
            return false;
        }
        
        return true; 
    }

    
    /**
     * Updates backlog item's state and effort left and its tasks' states. Used
     * by tasklist tag.
     */

    public String quickStoreTaskList() {               
        
        // check that AFTime is not negative
        if (this.effortLeft != null && this.effortLeft.getTime() < 0) {
            return CRUDAction.AJAX_ERROR;
        }        
           
        try {
            backlogItemBusiness.updateBacklogItemEffortLeftStateAndTaskStates(
                    backlogItemId, this.state, this.effortLeft, taskStates, taskNames);
        } catch (ObjectNotFoundException e) {
            return CRUDAction.AJAX_ERROR;
        }
        //should be refactored to the business layer
        if(spentEffort != null) {
            AFTime eff = null;
            try {
               eff = new AFTime(spentEffort,false);
               BacklogItem parent = backlogItemBusiness.getBacklogItem(backlogItemId);
               if(parent != null) {
                   hourEntryBusiness.addEntryForCurrentUser(parent, eff, spentEffortComment);
               }
            } catch ( IllegalArgumentException e ) {
                addActionError("Invalid format in spent effort.");
            } 
        }
        return CRUDAction.AJAX_SUCCESS;
    }

    public String resetBliOrigEstAndEffortLeft() {
        try {
            backlogItemBusiness.resetBliOrigEstAndEffortLeft(backlogItemId);
        } catch (ObjectNotFoundException e) {
            addActionError(e.getMessage());
            return Action.ERROR;
        }
        return Action.SUCCESS;
    }    

    public Backlog getBacklog() {
        return backlog;
    }

    public void setBacklog(Backlog backlog) {
        this.backlog = backlog;
    }

    public int getBacklogId() {
        return backlogId;
    }

    public void setBacklogId(int backlogId) {
        this.backlogId = backlogId;
    }

    public BacklogItem getBacklogItem() {
        return backlogItem;
    }

    public void setBacklogItem(BacklogItem backlogItem) {
        this.backlogItem = backlogItem;
    }

    public int getBacklogItemId() {
        return backlogItemId;
    }

    public void setBacklogItemId(int backlogItemId) {
        this.backlogItemId = backlogItemId;
    }

    public void setIterationGoalId(int iterationGoalId) {
        this.iterationGoalId = iterationGoalId;
    }

    public int getIterationGoalId() {
        return iterationGoalId;
    }

    public String getBacklogItemName() {
        return backlogItem.getName();
    }

    public void setBacklogItemName(String backlogItemName) {
        backlogItem.setName(backlogItemName);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public AFTime getEffortLeft() {
        return effortLeft;
    }

    public void setEffortLeft(AFTime effortLeft) {
        this.effortLeft = effortLeft;
    }
    
    public Map<Integer, String> getUserIds() {
        return userIds;
    }

    public void setUserIds(Map<Integer, String> userIds) {
        this.userIds = userIds;
    }

    public void setHistoryBusiness(HistoryBusiness historyBusiness) {
        this.historyBusiness = historyBusiness;
    }

    public String getSpentEffort() {
        return spentEffort;
    }

    public void setSpentEffort(String spentEffort) {
        this.spentEffort = spentEffort;
    }

    public void setHourEntryBusiness(HourEntryBusiness hourEntryBusiness) {
        this.hourEntryBusiness = hourEntryBusiness;
    }

    public void setBusinessThemeBusiness(BusinessThemeBusiness businessThemeBusiness) {
        this.businessThemeBusiness = businessThemeBusiness;
    }

    public Map<Integer, String> getTaskNames() {
        return taskNames;
    }

    public void setTaskNames(Map<Integer, String> taskNames) {
        this.taskNames = taskNames;
    }
   
    public boolean getUndoneTasks() {
        backlogItem = backlogItemBusiness.getBacklogItem(backlogItemId);
        if (backlogItem == null) {
            return false;
        }
        if (backlogItem.getTasks() == null || backlogItem.getTasks().size() == 0) {
            return false;
        }
        for (Task t: backlogItem.getTasks()) {
            if (t.getState() != State.DONE) {
                return true;
            }
        }
        return false;    
    }
    
    public boolean isTasksToDone() {
        return tasksToDone;
    }

    public void setTasksToDone(boolean tasksToDone) {
        this.tasksToDone = tasksToDone;
    }
    
    public List<BusinessTheme> getBliActiveOrSelectedThemes() {
        return this.bliActiveOrSelectedThemes;
    }

    public void setBliActiveOrSelectedThemes(List<BusinessTheme> activeOrSelectedThemes) {
        this.bliActiveOrSelectedThemes = activeOrSelectedThemes;
    }
    
    public void setThemeIds(Set<Integer> themeIds) {
        this.themeIds = themeIds;
    }

    public Set<Integer> getThemeIds() {
        return themeIds;
    }

    public void setSpentEffortComment(String spentEffortComment) {
        this.spentEffortComment = spentEffortComment;
    }
}
