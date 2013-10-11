package fi.hut.soberit.agilefant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import fi.hut.soberit.agilefant.util.XmlDateTimeAdapter;

/**
 * Hibernate entity bean which represents an hour entry.
 * 
 * Conceptually, hour entry represents logged effort for a given user in given
 * date with given description. One entry can be associated with only one user.
 * Hour entry is the base information container for the timesheet functionality.
 * 
 * This is a base class for all logged efforts and thus has no information
 * whatsoever of the parent object. Child classes for HourEntry add ownership
 * information. Class is not defined abstract as it us used as common logged
 * effort information container.
 * 
 * @see fi.hut.soberit.agilefant.model.StoryHourEntry
 * @see fi.hut.soberit.agilefant.model.BacklogHourEntry
 * @author Pasi Pekkanen, Roni Tammisalo
 * 
 */
@BatchSize(size = 20)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "hourentries")
@XmlAccessorType( XmlAccessType.NONE )
public class HourEntry {

    private int id;

    private DateTime date;

    private long minutesSpent;

    private User user;

    private String description;

    public HourEntry() { }
    
    public HourEntry(HourEntry other)
    {
        this.setDate(other.getDate());
        this.setDescription(other.getDescription());
        this.setMinutesSpent(other.getMinutesSpent());
        this.setUser(other.getUser());
    }
    
    /**
     * Get the id of this object.
     * <p>
     * The id is unique among all tasks.
     */
    // tag this field as the id
    @Id
    // generate automatically
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute(name = "objectId")
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @ManyToOne(optional = false)
    @XmlElement
    public User getUser() {
        return this.user;
    }

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime", parameters = @Parameter(name = "databaseZone", value = "jvm"))
    @XmlJavaTypeAdapter(XmlDateTimeAdapter.class)
    @XmlAttribute
    public DateTime getDate() {
        return this.date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    @Type(type = "escaped_text")
    @XmlElement
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    @XmlTransient
    public boolean isBacklogEffortEntry() {
        return (this instanceof BacklogHourEntry);
    }

    @Transient
    @XmlTransient
    public boolean isStoryEffortEntry() {
        return (this instanceof StoryHourEntry);
    }

    @Column(nullable = false)
    @XmlAttribute
    public long getMinutesSpent() {
        return minutesSpent;
    }

    public void setMinutesSpent(long minutesSpent) {
        this.minutesSpent = minutesSpent;
    }
}
