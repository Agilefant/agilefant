package fi.hut.soberit.agilefant.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.business.SearchBusiness;
import fi.hut.soberit.agilefant.business.StoryAccessBusiness;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.NamedObject;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;

@Component("referenceIDAction")
@Scope("prototype")
public class ReferenceIDAction extends ActionSupport {
    private static final long serialVersionUID = 6149968990623932750L;

    @Autowired
    private SearchBusiness searchBusiness;
    
    private String hash;
    
    private int backlogId;
    
    private String q;
    
    @Override
    public String execute() {
        NamedObject res = this.searchBusiness.searchByReference(q);
        if(res == null) {
            return Action.ERROR;
        }
        
        hash = res.getClass().getCanonicalName() + "_" + res.getId();
        Backlog backlog = null;
        
        if(res instanceof Backlog) {
            backlog = (Backlog)res;
            backlogId = backlog.getId();
        } 
        
        if(res instanceof Story) {
            if (((Story)res).getIteration() != null) {
                backlog = ((Story)res).getIteration();
                backlogId = backlog.getId();
            } else {
                backlog = ((Story)res).getBacklog();
                backlogId = backlog.getId();
                }
            }
        
        if(backlog instanceof Iteration) {
            return "iteration";
        } else if(backlog instanceof Project) {
            return "project";
        } else if(backlog instanceof Product) {
            return "product";
        } 
        
        return Action.ERROR;
    }
    
    public String execute1() {
        NamedObject res = this.searchBusiness.searchByReference(q);
        if(res == null) {
            return Action.ERROR;
        }
        
        hash = res.getClass().getCanonicalName() + "_" + res.getId();
        Backlog backlog = null;
        
        if(res instanceof Backlog) {
            backlog = (Backlog)res;
            backlogId = backlog.getId();
        } 
        
        if(res instanceof Story) {
            if (((Story)res).getBacklog() != null) {
                backlog = ((Story)res).getBacklog();
                backlogId = backlog.getId();
            } else {
                backlog = ((Story)res).getIteration();
                backlogId = backlog.getId();
                }
            }
        
        if(backlog instanceof Iteration) {
            return "iteration";
        } else if(backlog instanceof Project) {
            return "project";
        } else if(backlog instanceof Product) {
            return "product";
        } 
        
        return Action.ERROR;
    }

    public String getHash() {
        return hash;
    }

    public int getBacklogId() {
        return backlogId;
    }

    public void setQ(String q) {
        this.q = q;
    }
}

