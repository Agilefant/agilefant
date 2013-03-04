package fi.hut.soberit.agilefant.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import flexjson.JSON;

@Entity
@Table(name = "stories")
@Audited
@XmlRootElement
@XmlAccessorType( XmlAccessType.NONE )
public class Story implements TimesheetLoggable, LabelContainer, NamedObject, TaskContainer {
    private int id;
    private String name;
    private String description;
    private Backlog backlog;
    private StoryState state = StoryState.NOT_STARTED;
    private int treeRank = 0;
    private Story parent;
    private Iteration iteration;
    private List<Story> children = new ArrayList<Story>();

    private Set<StoryComment> storyComments = new HashSet<StoryComment>(0);
    
    private Set<Label> labels = new HashSet<Label>();

    private Set<User> responsibles = new HashSet<User>();
    private Set<Task> tasks = new HashSet<Task>();
    private Set<StoryHourEntry> hourEntries = new HashSet<StoryHourEntry>();
    private Set<StoryRank> storyRanks = new HashSet<StoryRank>();
    private Set<StoryAccess> storyAccesses;

    private Integer storyPoints;
    private Integer storyValue;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute(name = "objectId")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false)
    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Type(type = "escaped_text")
    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean hasChildren()
    {
        return this.children != null && this.children.size() > 0;
    }

    /**
     * With standalone iterations, backlogs can only be products or 
     * projects; iterations must have an attribute of their own because
     * projects could not know their progress otherwise
     */
    @ManyToOne(optional = true)
    public Backlog getBacklog() {
        return backlog;
    }

    public void setBacklog(Backlog backlog) {
        if(backlog instanceof Iteration) {
            throw new IllegalArgumentException("story backlog can't be an iteration");
        }
        this.backlog = backlog;
    }

    @ManyToOne
    public Iteration getIteration() {
        return iteration;
    }

    public void setIteration(Iteration iteration) {
        this.iteration = iteration;
    }

    @Column(nullable = false)
    @XmlAttribute
    public StoryState getState() {
        return state;
    }

    public void setState(StoryState state) {
        this.state = state;
    }

    /**
     * Get the users responsible for this story item.
     * @return Set of the responsible users
     */
    @ManyToMany(
            targetEntity = fi.hut.soberit.agilefant.model.User.class
    )
    @JoinTable(
            name = "story_user",
            joinColumns={@JoinColumn(name = "Story_id")},
            inverseJoinColumns={@JoinColumn(name = "User_id")}
    )
    @BatchSize(size=20)
    @Fetch(FetchMode.SUBSELECT)
    @XmlElementWrapper
    @XmlElement(name = "user")
    public Set<User> getResponsibles() {
        return responsibles;
    }

    public void setResponsibles(Set<User> responsibles) {
        this.responsibles = responsibles;
    }
    
    @OneToMany(targetEntity = fi.hut.soberit.agilefant.model.Task.class,
            mappedBy = "story"
    )
    @NotAudited
    @XmlElementWrapper
    @XmlElement(name = "task")
    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    @JSON
    @XmlAttribute
    public Integer getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(Integer storyPoints) {
        this.storyPoints = storyPoints;
    }
    
    @JSON
    @XmlAttribute
    public Integer getStoryValue() {
        return storyValue;
    }

    public void setStoryValue(Integer storyValue) {
        this.storyValue = storyValue;
    }

    @OneToMany(mappedBy = "story",
            targetEntity = fi.hut.soberit.agilefant.model.StoryHourEntry.class )
    @NotAudited
    @XmlElementWrapper
    @XmlElement(name = "hourEntry")
    public Set<StoryHourEntry> getHourEntries() {
        return hourEntries;
    }
    
    public void setHourEntries(Set<StoryHourEntry> hourEntries) {
        this.hourEntries = hourEntries;
    }

    @JSON
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    public Story getParent() {
        return parent;
    }

    public void setParent(Story parent) {
        this.parent = parent;
    }

    @JSON(include=false)
    @OneToMany(mappedBy="parent", targetEntity=fi.hut.soberit.agilefant.model.Story.class)
    @Fetch(FetchMode.SELECT)
    @OrderBy("treeRank")
    @NotAudited
    @XmlElementWrapper
    @XmlElement(name = "story")
    public List<Story> getChildren() {
        return children;
    }

    public void setChildren(List<Story> children) {
        this.children = children;
    }
    
    @JSON(include=false)
    @NotAudited
    @OneToMany(fetch = FetchType.LAZY, targetEntity=StoryRank.class, mappedBy="story", cascade=CascadeType.REMOVE )
    public Set<StoryRank> getStoryRanks() {
        return storyRanks;
    }

    public void setStoryRanks(Set<StoryRank> storyRanks) {
        this.storyRanks = storyRanks;
    }

    @Column(nullable = false, columnDefinition = "int default 0")
    @JSON
    public int getTreeRank() {
        return treeRank;
    }

    public void setTreeRank(int treeRank) {
        this.treeRank = treeRank;
    }

    public void setLabels(Set<Label> labels) {
        this.labels = labels;
    }

    @OneToMany(mappedBy = "story", cascade = CascadeType.REMOVE)
    @NotAudited
    @JSON(include = false)
    public Set<Label> getLabels() {
        return labels;
    }
    
    @OneToMany(mappedBy = "story", cascade = CascadeType.REMOVE, orphanRemoval=true)
    @NotAudited
    @JSON(include = false)
    public Set<StoryAccess> getStoryAccesses() {
        return this.storyAccesses;
    }

    public void setStoryAccesses(Set<StoryAccess> storyAccesses) {
        this.storyAccesses = storyAccesses;
    }
    
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "stories")
    @XmlElementWrapper
    @XmlElement(name = "comments")
    public Set<StoryComment> getStoryComments() {
            return this.storyComments;
    }

    public void setStoryComments(Set<StoryComment> storyComments) {
            this.storyComments = storyComments;
    }
    
    public Story()
    { }
    
    /**
     * Copying Constructor
     * @author bradens
     * @param otherStory
     */
    public Story(Story otherStory)
    {
        this.setDescription(otherStory.getDescription());
        this.setStoryValue(otherStory.getStoryValue());
        this.setName(otherStory.getName());
        this.setBacklog(otherStory.getBacklog());
        this.setTreeRank(otherStory.getTreeRank() - 1);
        this.setState(otherStory.getState());
        this.setStoryPoints(otherStory.getStoryPoints());
        this.setParent(otherStory.getParent());
        if (otherStory.getParent() != null)
            otherStory.getParent().getChildren().add(this);
        
        // Copy the complex members: tasks, users, labels, parents
        for (Task t : otherStory.getTasks())
        {
            // TODO @bradens find way to persist this task in this entity?  for now persisting it in the
            // StoryBusinessImpl.
            t.setStory(this); // To make sure we set the tasks to the new story.
            Task newTask = new Task(t);
            t.setStory(otherStory); // set it back
            this.getTasks().add(newTask);
        } 
        this.getResponsibles().addAll(otherStory.getResponsibles());
        for (StoryHourEntry entry : this.getHourEntries())
        {
            entry.setStory(this);
            StoryHourEntry newEntry = new StoryHourEntry(entry);
            this.getHourEntries().add(newEntry);
            entry.setStory(otherStory);
        }
        for (Label l : otherStory.getLabels())
        {
            l.setStory(this);
            Label newLabel = new Label(l);
            this.getLabels().add(newLabel);
            l.setStory(otherStory);
        }
        for (Story childStory : otherStory.getChildren())
        {
            Story newChild = new Story(childStory);
            newChild.setParent(this);
            this.getChildren().add(newChild);
        }
    }
}
