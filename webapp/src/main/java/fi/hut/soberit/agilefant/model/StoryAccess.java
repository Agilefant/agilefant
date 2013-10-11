package fi.hut.soberit.agilefant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import fi.hut.soberit.agilefant.util.XmlDateTimeAdapter;
import flexjson.JSON;

@Entity
@Table(name = "story_access")
@XmlRootElement
@XmlAccessorType( XmlAccessType.NONE )
public class StoryAccess {
    
    private Story story;
    private User user;
    private DateTime date;
    private int id;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute(name = "objectId")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @ManyToOne(optional = false)
    @JSON(include=false)
    public Story getStory() {
        return story;
    }
    
    public void setStory(Story story) {
        this.story = story;
    }
    
    @ManyToOne(optional = false)
    @JSON(include=false)
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    @JSON
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime", parameters = @Parameter(name = "databaseZone", value = "jvm"))
    @XmlAttribute
    @XmlJavaTypeAdapter(XmlDateTimeAdapter.class)
    public DateTime getDate() {
        return date;
    }
    public void setDate(DateTime date) {
        this.date = date;
    }   
}
