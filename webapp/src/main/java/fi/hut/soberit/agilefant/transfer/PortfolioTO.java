package fi.hut.soberit.agilefant.transfer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fi.hut.soberit.agilefant.model.Project;

public class PortfolioTO {

    private List<Project> rankedProjects = Collections.emptyList();
    private Collection<Project> unrankedProjects = Collections.emptySet();
    private Integer timeSpanInDays = 0;

    public List<Project> getRankedProjects() {
    	return this.rankedProjects;
    }

    public void setRankedProjects(List<Project> rankedProjects) {
        this.rankedProjects = rankedProjects;
    }

    public Collection<Project> getUnrankedProjects() {
    	return this.unrankedProjects;
    }
    
    public void setUnrankedProjects(Collection<Project> unrankedProjects) {
        this.unrankedProjects = unrankedProjects;
    }

    public void setTimeSpanInDays(Integer timeSpanInDays) {
        this.timeSpanInDays = timeSpanInDays;
    }

    public Integer getTimeSpanInDays() {
        return timeSpanInDays;
    }

}
