package fi.hut.soberit.agilefant.model;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlValue;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import flexjson.JSON;

@Entity
@Table(name = "labels")
@XmlAccessorType( XmlAccessType.NONE )
public class Label implements NamedObject, Serializable {

    private static final long serialVersionUID = 175091151639389468L;

    public Label() { }
    
    public Label(Label other)
    {
        this.setCreator(other.getCreator());
        this.setDisplayName(other.getDisplayName());
        this.setName(other.getName());
        this.setStory(other.getStory());
        this.setTimestamp(other.getTimestamp());
    }
    
    @ManyToOne
    private User creator;

    @Column(nullable = false)
    private String displayName;

    @Id
    @GeneratedValue
    private Integer id;

    @Index(name = "label_name")
    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Story story;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime", parameters = @Parameter(name = "databaseZone", value = "jvm"))
    private DateTime timestamp;

    public User getCreator() {
        return creator;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getId() {
        return id;
    }

    @XmlValue
    public String getName() {
        return name;
    }

    @JSON(include = false)
    public Story getStory() {
        return story;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /*
     * Non-default setter!
     */
    public void setName(String name) {
        this.name = name.toLowerCase(Locale.ENGLISH);
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

}
