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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import fi.hut.soberit.agilefant.util.XmlDateTimeAdapter;

@BatchSize(size = 20)
@Entity
@Table(name = "logins")
@XmlAccessorType( XmlAccessType.NONE )
public class Login {
    private int id;
    private User user;
    private DateTime time;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @ManyToOne
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime", parameters = @Parameter(name = "databaseZone", value = "jvm"))
    @XmlJavaTypeAdapter(XmlDateTimeAdapter.class)
    @XmlAttribute
    public DateTime getTime() {
        return time;
    }
    public void setTime(DateTime time) {
        this.time = time;
    }

}
