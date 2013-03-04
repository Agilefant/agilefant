package fi.hut.soberit.agilefant.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import fi.hut.soberit.agilefant.db.hibernate.Email;
import flexjson.JSON;

/**
 * Hibernate entity bean representing a user. User represents a person using the
 * webapp: it's more a thing of the implementation than anything conceptual.
 * <p>
 * The user carries information on username, password, full name and email. Also
 * there're different collections of items, where this user is assigned.
 */
@BatchSize(size = 20)
@Entity
@Table(name = "users")
@Audited
@XmlRootElement(name = "user")
@XmlAccessorType( XmlAccessType.NONE )
public class User implements NamedObject {

    private int id;

    private String password;

    private String loginName;

    private String fullName;

    private String email;

    private String initials;

    private boolean enabled = true;
    
    private boolean admin = true;

    private Collection<Team> teams = new HashSet<Team>();

    private Collection<Assignment> assignments = new HashSet<Assignment>();
    
    private Collection<Story> stories = new HashSet<Story>();
    
    private Set<Task> tasks = new HashSet<Task>();
    
    private Set<StoryComment> storyComments = new HashSet<StoryComment>(0);
    
    private Set<TaskComment> taskComments = new HashSet<TaskComment>(0);
    
    private ExactEstimate weekEffort = new ExactEstimate(0);
    
    private Collection<Holiday> holidays = new HashSet<Holiday>();
    
    private Collection<HolidayAnomaly> holidayAnomalies = new HashSet<HolidayAnomaly>();
    
    private int recentItemsNumberOfWeeks = 16;
    
    /*
     * User-specific settings
     */
    @XmlAccessorType( XmlAccessType.NONE )
    public enum UserSettingType { never, ask, always };
    
    private boolean autoassignToTasks = true;
    private boolean autoassignToStories = true;
    private UserSettingType markStoryStarted = UserSettingType.ask;
    
    
    /**
     * Get the id of this object.
     * <p>
     * The id is unique among all users.
     */
    // tag this field as the id
    @Id
    // generate automatically
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JSON
    @XmlAttribute(name = "objectId")
    public int getId() {
        return id;
    }

    /**
     * Set the id of this object.
     * <p>
     * You shouldn't normally call this.
     */
    public void setId(int id) {
        this.id = id;
    }

    /** Get full name. */
    @Type(type = "escaped_truncated_varchar")
    @JSON
    @XmlAttribute
    public String getFullName() {
        return fullName;
    }

    /** Set full name. */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /** Get login name. */
    @Column(unique = true)
    @Type(type = "escaped_truncated_varchar")
    @JSON
    public String getLoginName() {
        return loginName;
    }

    /** Set login name. */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /** Get password. */
    @Type(type = "truncated_varchar")
    @JSON(include = false)
    @NotAudited
    public String getPassword() {
        return password;
    }

    /** Set password. */
    public void setPassword(String password) {
        this.password = password;
    }

    /** Get login name. */
    @Transient
    @JSON
    public String getName() {
        return this.loginName;
    }

    /**
     * Get email addresses. Note that the field is validated to be a valid a
     * email address: an exception is thrown on store, if it's invalid.
     */
    @Column(nullable = true)
    @Email
    @Type(type = "truncated_varchar")
    @JSON
    public String getEmail() {
        return email;
    }

    /**
     * Set email addresses. Note that the field is validated to be a valid a
     * email address: an exception is thrown on store, if it's invalid.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user's initials.
     * 
     * @return the initials
     */
    @JSON
    @XmlAttribute
    public String getInitials() {
        return initials;
    }

    /**
     * Set the user's initials.
     * 
     * @param initials
     *            the initials to set
     */
    public void setInitials(String initials) {
        this.initials = initials;
    }

    /**
     * Get the user's teams.
     * 
     * @return the teams
     */
    @ManyToMany(targetEntity = Team.class, fetch=FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "team_user", joinColumns = { @JoinColumn(name = "User_id") }, inverseJoinColumns = { @JoinColumn(name = "Team_id") })
    @JSON(include = false)
    @NotAudited
    public Collection<Team> getTeams() {
        return teams;
    }

    /**
     * Set the user's teams.
     * 
     * @param teams
     */
    public void setTeams(Collection<Team> teams) {
        this.teams = teams;
    }

    /**
     * Check, if the user is disabled
     * 
     * @return true, if user is disabled, false otherwise
     */
    @JSON
    @XmlAttribute
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set the user's enabled status.
     * 
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * Check, if the user is an admin
     * 
     * @return true, if user is admin, false otherwise
     */
    @JSON
    @NotAudited
    @Column(columnDefinition = "bit default 1")
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Set the user's admin status.
     * 
     * @param admin
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * Get the users assignments to backlogs.
     * @return
     */
    @OneToMany(mappedBy = "user")
    @JSON(include = false)
    @NotAudited
    public Collection<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Collection<Assignment> assignments) {
        this.assignments = assignments;
    }

    public void setStories(Collection<Story> stories) {
        this.stories = stories;
    }

    /** Get stories, of which the user is responsible. */
    @ManyToMany(mappedBy = "responsibles",
            targetEntity = fi.hut.soberit.agilefant.model.Story.class,
            fetch = FetchType.LAZY)
    @NotAudited
    public Collection<Story> getStories() {
        return stories;
    }
    
    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    /** Get tasks, of which the user is responsible. */
    @ManyToMany(mappedBy = "responsibles",
            targetEntity = fi.hut.soberit.agilefant.model.Task.class,
            fetch = FetchType.LAZY)
    @NotAudited
    public Set<Task> getTasks() {
        return tasks;
    }
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "users")
    public Set<StoryComment> getStoryComments() {
            return this.storyComments;
    }

    public void setStoryComments(Set<StoryComment> commentses) {
            this.storyComments = commentses;
    }
        
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "users")
    public Set<TaskComment> getTaskComments() {
        return taskComments;
    }

    public void setTaskComments(Set<TaskComment> taskComments) {
        this.taskComments = taskComments;
    }

    public void setWeekEffort(ExactEstimate weekEffort) {
        this.weekEffort = weekEffort;
    }
    
    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "minorUnits", column = @Column(name = "weekEffort")))
    public ExactEstimate getWeekEffort() {
        return weekEffort;
    }

    @OneToMany(mappedBy="user", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
    @NotAudited
    public Collection<Holiday> getHolidays() {
        return holidays;
    }

    public void setHolidays(Collection<Holiday> holidays) {
        this.holidays = holidays;
    }

    @OneToMany(mappedBy="user", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
    @NotAudited
    public Collection<HolidayAnomaly> getHolidayAnomalies() {
        return holidayAnomalies;
    }

    public void setHolidayAnomalies(Collection<HolidayAnomaly> holidayAnomalies) {
        this.holidayAnomalies = holidayAnomalies;
    }

    @JSON
    @NotAudited
    @Column(columnDefinition = "bit default 1")
    public boolean isAutoassignToTasks() {
        return autoassignToTasks;
    }

    public void setAutoassignToTasks(boolean autoassignToTasks) {
        this.autoassignToTasks = autoassignToTasks;
    }

    @JSON
    @NotAudited
    @Column(columnDefinition = "bit default 0")
    public boolean isAutoassignToStories() {
        return autoassignToStories;
    }

    public void setAutoassignToStories(boolean autoassignToStories) {
        this.autoassignToStories = autoassignToStories;
    }

    @JSON
    @NotAudited
    @Column(columnDefinition = "integer default 1")
    public UserSettingType getMarkStoryStarted() {
        return markStoryStarted;
    }

    public void setMarkStoryStarted(UserSettingType markStoryStarted) {
        this.markStoryStarted = markStoryStarted;
    }

    @JSON
    @NotAudited
    public int getRecentItemsNumberOfWeeks() {
        return recentItemsNumberOfWeeks;
    }

    public void setRecentItemsNumberOfWeeks(int recentItemsNumberOfWeeks) {
        this.recentItemsNumberOfWeeks = recentItemsNumberOfWeeks;
    }

    
}
