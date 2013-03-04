package fi.hut.soberit.agilefant.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import flexjson.JSON;

@Entity
@Audited()
@Table(name = "task_comments")
@XmlAccessorType( XmlAccessType.NONE )
public class 
TaskComment implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1876621931319585681L;
        // Fields

        private Integer id;
        private Task tasks;
        private User users;
        private String comment;
        private Timestamp time;
        private Set<CommentAttachments> attachments = new HashSet<CommentAttachments>(0);
        
        @Transient
        private String parsedTime;
        @Transient
        private boolean deletable;

        private String[] taskId;
        // Constructors

        /** default constructor */
        public TaskComment() {
        }

        /** minimal constructor */
        public TaskComment(Task tasks, User users) {
                this.tasks = tasks;
                this.users = users;
        }

        /** full constructor */
        public TaskComment(Task tasks, User users, String comment) {
                this.tasks = tasks;
                this.users = users;
                this.comment = comment;
        }

        @Id
        @GeneratedValue
        @Column(name = "id", unique = true, nullable = false)
        @XmlAttribute(name = "ObjectId")
        public Integer getId() {
                return this.id;
        }

        public void setId(Integer id) {
                this.id = id;
        }
        
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "taskId", nullable = false)
        @Type(type = "escaped_truncated_varchar")
        @JSON
        @XmlAttribute
        public Task getTasks() {
                return this.tasks;
        }

        public void setTasks(Task tasks) {
                this.tasks = tasks;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "userId", nullable = false)
        @Type(type = "escaped_truncated_varchar")
        @JSON
        @XmlAttribute
        public User getUsers() {
                return this.users;
        }

        public void setUsers(User users) {
                this.users = users;
        }

        @Column(name = "comment", length = 5000)
        @Type(type = "escaped_text")
        @JSON
        @XmlAttribute
        public String getComment() {
                return this.comment;
        }

        public void setComment(String comment) {
                this.comment = comment;
        }
        
        public void setTime(Timestamp time) {
            this.time = time;
        }
        @Column(name = "created" )
     // @Type(type = "escaped_text")
        @JSON
        @XmlAttribute
        public Timestamp getTime() {
            return time;
        }
        
        @Type(type = "escaped_text")
        @JSON
        @XmlAttribute
        @Transient
        public String getParsedTime() {
            return parsedTime;
        }        

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "taskComment")
        @XmlElementWrapper
        @XmlElement(name = "attachments")
        public Set<CommentAttachments> getAttachments() {
            return attachments;
        }

        public void setAttachments(Set<CommentAttachments> attachments) {
            this.attachments = attachments;
        }

        public void setParsedTime(String parsedTime) {
            this.parsedTime = parsedTime;
        }
        
        @Type(type = "escaped_text")
        @JSON
        @XmlAttribute
        @Transient
        public boolean isDeletable() {
            return deletable;
        }

        public void setDeletable(boolean deletable) {
            this.deletable = deletable;
        }

        public boolean hasAttachment() {
            if(attachments != null && attachments.size() > 0){
                return true;
            }else{
                return false;
            }
        }
        
     
}