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
import javax.persistence.Transient;
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

import fi.hut.soberit.agilefant.business.impl.StoryHierarchyBusinessImpl;
import fi.hut.soberit.agilefant.transfer.StoryTreeBranchMetrics;
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

    private Set<Label> labels = new HashSet<Label>();

    private Set<User> responsibles = new HashSet<User>();
    private Set<Task> tasks = new HashSet<Task>();
    private Set<StoryHourEntry> hourEntries = new HashSet<StoryHourEntry>();
    private Set<StoryRank> storyRanks = new HashSet<StoryRank>();
    private Set<StoryAccess> storyAccesses;
    private Set<WhatsNextStoryEntry> whatsNextStoryEntries = new HashSet<WhatsNextStoryEntry>();

    private Integer storyPoints;
    private Integer storyValue;
    
    private Story fullInfoStory;
    
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
    
    @NotAudited
    @OneToMany(
            targetEntity = fi.hut.soberit.agilefant.model.WhatsNextStoryEntry.class,
            fetch = FetchType.LAZY,
            mappedBy = "story",
            cascade = CascadeType.REMOVE
    )
    @JSON(include = false)
    public Set<WhatsNextStoryEntry> getWhatsNextStoryEntries() {
        return whatsNextStoryEntries;
    }
    
    public void setWhatsNextStoryEntries(Set<WhatsNextStoryEntry> entries) {
        this.whatsNextStoryEntries = entries;
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
    
    public void setFullInfoStory(Story fullInfoStory) {
        this.fullInfoStory = fullInfoStory;
    }
    
    @JSON
    @XmlAttribute
    @Transient
    public String getHighestPoints() {
    	Integer storyPoints = 0;
    	if (getStoryPoints() != null) {
    		storyPoints = getStoryPoints();
    	}
    	StoryHierarchyBusinessImpl impl = new StoryHierarchyBusinessImpl();
    	StoryTreeBranchMetrics metrics;
    	if (this.fullInfoStory != null) {
    	    metrics = impl.calculateStoryTreeMetrics(this.fullInfoStory);
    	} else {
            metrics = impl.calculateStoryTreeMetrics(this);
    	}
    	long estimatedPoints = metrics.getEstimatedPoints();
    	if (estimatedPoints > storyPoints) {
    		return "<span class='treeChildStoryPoints treeStoryPoints' title='Story child points'>" + metrics.getEstimatedDonePoints() + " / " + estimatedPoints + "</span>";
    	} else if (storyPoints == 0) {
    		return "<span class='treeStoryPoints' title='Story points'> - </span>";
    	} else if (this.hasChildren() && estimatedPoints > 0) {
    		return "<span class='treeStoryPoints' title='Story points'>" + metrics.getEstimatedDonePoints() + " / " + storyPoints + "</span>";
    	} else {
    		return "<span class='treeStoryPoints' title='Story points'>" + storyPoints + "</span>";
    	}
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
    
    public Story()
    { }
    
    public Story(Story otherStory) {
        copyStory(otherStory, false);
    }
    
    public Story(Story otherStory, boolean moveFinishedTasks) {
        copyStory(otherStory, moveFinishedTasks);
    }
    
    /**
     * Copying Constructor
     * @author bradens
     * @param otherStory
     */
    public void copyStory(Story otherStory, boolean moveFinishedTasks)
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
            if (moveFinishedTasks) {
                if (t.getState() != TaskState.DONE && t.getState() != TaskState.IMPLEMENTED) {
                    t.setStory(this); // To make sure we set the tasks to the new story.
                }
            } else {
                // TODO @bradens find way to persist this task in this entity?  for now persisting it in the
                // StoryBusinessImpl.
                t.setStory(this); // To make sure we set the tasks to the new story.
                Task newTask = new Task(t);
                t.setStory(otherStory); // set it back
                this.getTasks().add(newTask);
            }
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
        for (WhatsNextStoryEntry we : otherStory.getWhatsNextStoryEntries())
        {
            WhatsNextStoryEntry newEntry = new WhatsNextStoryEntry(we);
            this.getWhatsNextStoryEntries().add(newEntry);
        }
    }
}
