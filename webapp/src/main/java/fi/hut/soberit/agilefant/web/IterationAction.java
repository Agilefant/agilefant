package fi.hut.soberit.agilefant.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;

import fi.hut.soberit.agilefant.annotations.PrefetchId;
import fi.hut.soberit.agilefant.business.IterationBusiness;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.transfer.AssignmentTO;
import fi.hut.soberit.agilefant.transfer.IterationMetrics;
import fi.hut.soberit.agilefant.util.TokenGenerator;

@Component("iterationAction")
@Scope("prototype")
public class IterationAction implements CRUDAction, Prefetching, ContextAware {

    private static final long serialVersionUID = -448825368336871703L;

    @PrefetchId
    private int iterationId;
    
    private String readonlyToken;

    private Iteration iteration;

    private Backlog parentBacklog;

    private int parentBacklogId;

    private IterationMetrics iterationMetrics;
        
    private Set<AssignmentTO> assignments;
    
    private Set<Integer> assigneeIds = new HashSet<Integer>();
    
    private boolean assigneesChanged = false;

    private String confirmationString;
    
    private Collection<Iteration> iterations = new ArrayList<Iteration>();
    
    private Set<Integer> teamIds = new HashSet<Integer>();
    
    private boolean teamsChanged;
    
    @Autowired
    private IterationBusiness iterationBusiness;
    
    @Autowired
    private TokenGenerator tokenGenerator;

    public String create() {
        iterationId = 0;
        iteration = new Iteration();
        iteration.setStartDate(new DateTime());
        iteration.setEndDate(new DateTime());
        return Action.SUCCESS;
    }

    public String retrieve() {
        iteration = iterationBusiness.retrieve(iterationId);
        parentBacklog = iteration.getParent();
        // Load metrics data
        iterationMetrics = iterationBusiness.getIterationMetrics(iteration);
        return Action.SUCCESS;
    }
    
    public String fetchIterationData() {
        iterationBusiness.retrieve(iterationId);
        iteration = iterationBusiness.getIterationContents(iterationId);
        return Action.SUCCESS;
    }

    public String delete() {
        if(confirmationString.equalsIgnoreCase("yes")) {
            parentBacklog = iterationBusiness.retrieve(iterationId).getParent();
            iterationBusiness.deleteAndUpdateHistory(iterationId);
            if(parentBacklog != null) {
              return "redirect-parent";
            } else {
              return "redirect-login-context";
            }
        } else {
            return Action.ERROR;
        }
    }

    public String iterationRowMetrics() {
        iteration = this.iterationBusiness.retrieve(iterationId);
        iterationMetrics = iterationBusiness.getIterationMetrics(iteration);
        return Action.SUCCESS;
    }
    
    public String iterationAssignments() {
        iteration = iterationBusiness.retrieve(iterationId);
        assignments = iterationBusiness.calculateAssignedLoadPerAssignee(iteration);
        return Action.SUCCESS;
    }
    
    public String iterationMetrics() {
        iteration = iterationBusiness.retrieve(iterationId);
        iterationMetrics = iterationBusiness.getIterationMetrics(iteration);
        return Action.SUCCESS;
    }
    
    public String iterationMetricsByToken() {
        iteration = iterationBusiness.retreiveIterationByReadonlyToken(readonlyToken);
        iterationMetrics = iterationBusiness.getIterationMetrics(iteration);
        return Action.SUCCESS;
    }
    
    public String createReadonlyToken() {
        iteration = iterationBusiness.retrieve(iterationId);
        iteration.setReadonlyToken(tokenGenerator.generateReadonlyToken());
        
        Set<Integer> teams = null;
        if (teamsChanged) {
            teams = teamIds;
        }
        
        this.iterationBusiness.store(iterationId, parentBacklogId, iteration, assigneeIds, teams);
        
        this.readonlyToken = iteration.getReadonlyToken();

        return Action.SUCCESS;
    }
    
    public String clearReadonlyToken() {
        iteration = iterationBusiness.retrieve(iterationId);
        iteration.setReadonlyToken(null);
        
        Set<Integer> teams = null;
        if (teamsChanged) {
            teams = teamIds;
        }
        
        this.iterationBusiness.store(iterationId, parentBacklogId, iteration, assigneeIds, teams);
        
        this.readonlyToken = iteration.getReadonlyToken();

        return Action.SUCCESS;
    }
    
    public String setReadonlyTokenForJsp() {
        iteration = iterationBusiness.retrieve(iterationId);
        this.readonlyToken = iteration.getReadonlyToken();
        
        return Action.SUCCESS;
    }
    
    /*
    @Validations(
            requiredFields = {@RequiredFieldValidator(type=ValidatorType.SIMPLE, fieldName="iteration.name", key="iteration.missingName"),
                    @RequiredFieldValidator(type=ValidatorType.SIMPLE, fieldName="iteration.startDate", key="iteration.missingStartDate"),
                    @RequiredFieldValidator(type=ValidatorType.SIMPLE, fieldName="iteration.endDate", key="iteration.missingEndDate")},
            expressions = {@ExpressionValidator(expression = "iteration.startDate < iteration.endDate", key="iteration.startBeforeEnd")}
    )
    */
    public String store() {
        Set<Integer> assignees = null;
        if(this.assigneesChanged) {
            assignees = this.assigneeIds;
        }
        
        Set<Integer> teams = null;
        if (teamsChanged) {
            teams = teamIds;
        }
        
        // store stand alone iteration which has no parent
        if (parentBacklogId == 0) {
            iteration = this.iterationBusiness.storeStandAlone(iterationId, iteration, assignees, teams);        
        } else {
            iteration = this.iterationBusiness.store(iterationId, parentBacklogId, iteration, assignees, teams);
            
        }
        
        return Action.SUCCESS;
    }
    
    public String retrieveAllSA() {
        this.iterations = iterationBusiness.retrieveAllStandAloneIterations();
        return Action.SUCCESS;
    }
    
    public String getContextName() {
        return "backlog";
    }
    
    public int getContextObjectId() {
        return iterationId;
    }
    
    public void initializePrefetchedData(int objectId) {
        this.iteration = this.iterationBusiness.retrieve(objectId);
    }
    
    public int getIterationId() {
        return iterationId;
    }

    public void setIterationId(int iterationId) {
        this.iterationId = iterationId;
    }
    
    public String getReadonlyToken() {
        return readonlyToken;
    }
    
    public void setReadonlyToken(String readonlyToken) {
        this.readonlyToken = readonlyToken;
    }

    public Iteration getIteration() {
        return iteration;
    }

    public void setIteration(Iteration iteration) {
        this.iteration = iteration;
    }

    public int getParentBacklogId() {
        return parentBacklogId;
    }

    public void setParentBacklogId(int parentBacklogId) {
        this.parentBacklogId = parentBacklogId;
    }

    public void setIterationBusiness(IterationBusiness iterationBusiness) {
        this.iterationBusiness = iterationBusiness;
    }

    public IterationMetrics getIterationMetrics() {
        return iterationMetrics;
    }

    public Backlog getParentBacklog() {
        return parentBacklog;
    }

    public Set<AssignmentTO> getAssignments() {
        return assignments;
    }

    public Set<Integer> getAssigneeIds() {
        return assigneeIds;
    }

    public void setAssigneeIds(Set<Integer> assigneeIds) {
        this.assigneeIds = assigneeIds;
    }
    
    public void setConfirmationString(String confirmationString) {
        this.confirmationString = confirmationString;
    }

    public boolean isAssigneesChanged() {
        return assigneesChanged;
    }

    public void setAssigneesChanged(boolean assigneesChanged) {
        this.assigneesChanged = assigneesChanged;
    }
    
    public void setTeamsChanged(boolean teamsChanged) {
        this.teamsChanged = teamsChanged;
    }

    public Set<Integer> getTeamIds() {
        return teamIds;
    }

    public void setTeamIds(Set<Integer> teamIds) {
        this.teamIds = teamIds;
    }
    
    public Collection<Iteration> getIterations() {
        return iterations;
    }

    public void setIterations(Collection<Iteration> iterations) {
        this.iterations = iterations;
    }

}
