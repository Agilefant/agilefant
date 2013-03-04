package fi.hut.soberit.agilefant.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import flexjson.JSON;

/**
 * An entity bean representing a task.
 * 
 * @author rjokelai
 * 
 */

@Entity
@Table(name = "tasks")
@Audited
@XmlAccessorType( XmlAccessType.NONE )
public class Task implements TimesheetLoggable, NamedObject, Rankable {

    private int id;
    private String name;
    private String description;
    private Iteration iteration;
    private Story story;
    
    private TaskState state;
    private int rank = 0;
    
    public Set<TaskComment> taskComments = new HashSet<TaskComment>();
    
    private ExactEstimate effortLeft;
    private ExactEstimate originalEstimate;
    private Set<User> responsibles = new HashSet<User>();
    private Set<TaskHourEntry> hourEntries = new HashSet<TaskHourEntry>();
    private Set<WhatsNextEntry> whatsNextEntries = new HashSet<WhatsNextEntry>();
    
    public Task() { }
    
    public Task(Task other)
    {
        this.setDescription(other.getDescription());
        this.setEffortLeft(other.getEffortLeft());
        this.setIteration(other.getIteration());
        this.setName(other.getName());
        this.setOriginalEstimate(other.getOriginalEstimate());
        this.setRank(other.getRank());
        this.setState(other.getState());
        this.setStory(other.getStory());
        this.getResponsibles().addAll(other.getResponsibles());
        
        // Complex members
        for (TaskHourEntry t : other.getHourEntries())
        {
            TaskHourEntry he = new TaskHourEntry(t);
            this.getHourEntries().add(he);
        }
        for (WhatsNextEntry we : other.getWhatsNextEntries())
        {
            WhatsNextEntry newEntry = new WhatsNextEntry(we);
            this.getWhatsNextEntries().add(newEntry);
        }
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute(name = "objectId")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Type(type = "escaped_truncated_varchar")
    @JSON
    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JSON
    @Type(type = "escaped_text")
    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    @JSON(include = false)
    public Iteration getIteration() {
        return iteration;
    }

    public void setIteration(Iteration iteration) {
        this.iteration = iteration;
    }

    @ManyToOne
    @JSON(include = false)
    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }
    
    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "minorUnits", column = @Column(name = "effortleft")))
    @XmlAttribute
    public ExactEstimate getEffortLeft() {
        return effortLeft;
    }

    public void setEffortLeft(ExactEstimate estimate) {
        this.effortLeft = estimate;
    }
    
    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "minorUnits", column = @Column(name = "originalestimate")))
    @XmlAttribute
    public ExactEstimate getOriginalEstimate() {
        return originalEstimate;
    }

    public void setOriginalEstimate(ExactEstimate originalEstimate) {
        this.originalEstimate = originalEstimate;
    }

    @JSON
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @XmlAttribute
    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    @ManyToMany(
            targetEntity = fi.hut.soberit.agilefant.model.User.class,
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "task_user"
    )
    @OrderBy("initials asc")
    @JSON(include = false)
    @XmlElementWrapper(name="responsibles")
    @XmlElement(name="user")
    public Set<User> getResponsibles() {
        return responsibles;
    }
    
    public void setResponsibles(Set<User> responsibles) {
        this.responsibles = responsibles;
    }

    @NotAudited
    @OneToMany(
            targetEntity = fi.hut.soberit.agilefant.model.WhatsNextEntry.class,
            fetch = FetchType.LAZY,
            mappedBy = "task",
            cascade = CascadeType.REMOVE
    )
    @JSON(include = false)
    public Set<WhatsNextEntry> getWhatsNextEntries() {
        return whatsNextEntries;
    }
    
    public void setWhatsNextEntries(Set<WhatsNextEntry> entries) {
        this.whatsNextEntries = entries;
    }

    @OneToMany(mappedBy="task")
    @NotAudited
    @XmlElementWrapper(name="hourEntries")
    @XmlElement(name="hourEntry")
    public Set<TaskHourEntry> getHourEntries() {
        return hourEntries;
    }
    
    public void setHourEntries(Set<TaskHourEntry> hourEntries) {
        this.hourEntries = hourEntries;
    }

    @Column(nullable = false, columnDefinition = "int default 0")
    @XmlAttribute
    public int getRank() {
        return rank;
    }
    
    public void setRank(int rank) {
        this.rank = rank;
    }
    
/*    @JSON
    public void setTaskComments(Set<TaskComment> taskComments) {
        this.taskComments = taskComments;
    }
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tasks")
    @JSON
    public Set<TaskComment> getTaskComments() {
        return taskComments;
    }*/

}
