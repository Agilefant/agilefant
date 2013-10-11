package fi.hut.soberit.agilefant.model;

/**
 * @author ahaapala
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import flexjson.JSON;


@Entity
@Table(
        name = "whatsnextentry",
        uniqueConstraints={@UniqueConstraint(columnNames={"task_id", "user_id"})}
)
@XmlAccessorType( XmlAccessType.NONE )
public class WhatsNextEntry implements Rankable {
    private int id;
    private Integer rank = 0;
    private User user;
    private Task task;
   
    public WhatsNextEntry() { }
    
    public WhatsNextEntry(WhatsNextEntry other)
    {
        this.setRank(other.getRank());
        this.setTask(other.getTask());
        this.setUser(other.getUser());
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne()
    @JSON(include = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    @ManyToOne
    @JSON(include = false)
    public Task getTask() {
        return task;
    }
    
    public void setTask(Task task) {
        this.task = task;
    }

    @Column(nullable = false, columnDefinition = "int default 0")
    public Integer getRank() {
        return rank ;
    }
    
    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
