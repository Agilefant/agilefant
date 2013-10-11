package fi.hut.soberit.agilefant.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.joda.time.DateTime;

import fi.hut.soberit.agilefant.util.XmlDateTimeAdapter;
import fi.hut.soberit.agilefant.util.XmlExactEstimateAdapter;
import flexjson.JSON;

/**
 * A Hibernate entity bean which represents an iteration.
 * <p>
 * Conceptually, an iteration is a type of a backlog. A iteration-backlog
 * represents work (stories, tasks) to be done during an iteration.
 * Iteration is a time period, a conceptual tool, used to divide and manage
 * work. It's usually a few weeks in length.
 * <p>
 * Since a project is a backlog, it can contain stories, which, in turn,
 * are smaller containers for work. An iteration is a part of a bigger work
 * container, the project.
 * <p>
 * An iteration is part of a project. Start- and ending dates can be defined, as
 * well as effort estimate.
 * 
 * @see fi.hut.soberit.agilefant.model.Backlog
 * @see fi.hut.soberit.agilefant.model.Story
 * @see fi.hut.soberit.agilefant.model.Iteration
 */
@Entity
@BatchSize(size = 20)
@Audited
@XmlRootElement
@XmlAccessorType( XmlAccessType.NONE )
public class Iteration extends Backlog implements Schedulable, TaskContainer {
 
    private DateTime startDate;

    private DateTime endDate;
    
    private ExactEstimate backlogSize = new ExactEstimate(0);
    
    private Set<Assignment> assignments = new HashSet<Assignment>();
    
    private Set<Task> tasks = new HashSet<Task>();

    private Set<IterationHistoryEntry> historyEntries = new HashSet<IterationHistoryEntry>();
    
    private ExactEstimate baselineLoad = new ExactEstimate(0);
    
    private String readonlyToken;
    
    private Set<Story> assignedStories = new HashSet<Story>();
    
    private Collection<Team> teams = new HashSet<Team>();

    @JSON
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime", parameters = @Parameter(name = "databaseZone", value = "jvm"))
    @XmlAttribute
    @XmlJavaTypeAdapter(XmlDateTimeAdapter.class)
    public DateTime getEndDate() {
        return endDate;
    }
    
    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    
    @JSON
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime", parameters = @Parameter(name = "databaseZone", value = "jvm"))
    @XmlAttribute
    @XmlJavaTypeAdapter(XmlDateTimeAdapter.class)
    public DateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }


    @JSON
    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "minorUnits", column = @Column(name = "backlogSize")))
    @XmlAttribute
    @XmlJavaTypeAdapter(XmlExactEstimateAdapter.class)
    public ExactEstimate getBacklogSize() {
        return backlogSize;
    }

    public void setBacklogSize(ExactEstimate backlogSize) {
        this.backlogSize = backlogSize;
    }
    
    
    @OneToMany(mappedBy = "iteration")
    @JSON(include = false)
    @NotAudited
    @XmlElementWrapper
    @XmlElement(name = "task")
    public Set<Task> getTasks() {
        return tasks;
    }
    
    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
    
    @OneToMany(targetEntity = fi.hut.soberit.agilefant.model.Assignment.class,
            mappedBy = "backlog", cascade = javax.persistence.CascadeType.REMOVE)
    @JSON(include = false)
    public Set<Assignment> getAssignments() {
        return assignments;
    }
    
    public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }
 
    @OneToMany(mappedBy = "iteration", cascade=javax.persistence.CascadeType.REMOVE)
    @JSON(include = false)
    @NotAudited
    public Set<IterationHistoryEntry> getHistoryEntries() {
        return historyEntries;
    }

    public void setHistoryEntries(Set<IterationHistoryEntry> historyEntries) {
        this.historyEntries = historyEntries;
    }
    
    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "minorUnits", column = @Column(name = "baselineLoad")))
    @XmlAttribute
    @XmlJavaTypeAdapter(XmlExactEstimateAdapter.class)
    public ExactEstimate getBaselineLoad() {
        return baselineLoad;
    }

    public void setBaselineLoad(ExactEstimate baselineLoad) {
        this.baselineLoad = baselineLoad;
    }
    
    public void setReadonlyToken(String readonlyToken) {
        this.readonlyToken = readonlyToken;
    }
    
    @Column(unique=true)
    @JSON
    @NotAudited
    public String getReadonlyToken() {
        return readonlyToken;
    }
    
    @OneToMany(mappedBy = "iteration")
    @NotAudited
    @XmlElementWrapper
    @XmlElement(name = "story")
    public Set<Story> getAssignedStories() {
        return assignedStories;
    }
    
    public void setAssignedStories(Set<Story> stories) {
        this.assignedStories = stories;
    }

    @Transient
    @Override
    public boolean isStandAlone() {
        return this.getParent() == null;
    }
    
    /**
     * Get the iteration's teams
     * 
     * return the teams
     */
    @ManyToMany(targetEntity = Team.class)
    @JoinTable(name = "team_iteration", joinColumns = { @JoinColumn(name = "Iteration_id") }, inverseJoinColumns = { @JoinColumn(name = "Team_id") })
    @BatchSize(size = 5)
    @JSON(include = false)
    @NotAudited
    public Collection<Team> getTeams() {
        return teams;
    }
    
    /**
     * Set the iterations's teams.
     * 
     * @param teams the teams to be set
     */
    public void setTeams(Collection<Team> teams) {
        this.teams = teams;
    }
    
}
