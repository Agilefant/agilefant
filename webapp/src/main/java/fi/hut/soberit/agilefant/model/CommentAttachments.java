package fi.hut.soberit.agilefant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import flexjson.JSON;


@Entity
@Table(name = "attachments")
@Audited()
@XmlAccessorType( XmlAccessType.NONE )
public class CommentAttachments implements java.io.Serializable  {
    
    /**
     * 
     */
    private static final long serialVersionUID = 2012534265791630644L;
    
    private Integer id;
    private StoryComment  storyComment;
    private TaskComment  taskComment;
    private String  attachmentLocation;
    private String  originalFileName;
    private String  fileContentType;
    
    
    public CommentAttachments() {
    }
    
    
    
   
    
    /**
     * @param id
     * @param storyComment
     * @param taskComment
     * @param attachmentLocation
     * @param originalFileName
     * @param fileContentType
     */
    public CommentAttachments(Integer id, StoryComment storyComment,
            TaskComment taskComment, String attachmentLocation,
            String originalFileName, String fileContentType) {
        super();
        this.id = id;
        this.storyComment = storyComment;
        this.taskComment = taskComment;
        this.attachmentLocation = attachmentLocation;
        this.originalFileName = originalFileName;
        this.fileContentType = fileContentType;
    }

    
    public CommentAttachments(Integer id, StoryComment storyComment,
            TaskComment taskComment, String attachmentLocation) {
        super();
        this.id = id;
        this.storyComment = storyComment;
        this.taskComment = taskComment;
        this.attachmentLocation = attachmentLocation;
    }





    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storyCommentId")
    @Type(type = "escaped_truncated_varchar")
    @JSON
    @XmlAttribute
    public StoryComment getStoryComment() {
        return storyComment;
    }

    public void setStoryComment(StoryComment storyComment) {
        this.storyComment = storyComment;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskCommentId")
    @Type(type = "escaped_truncated_varchar")
    @JSON
    @XmlAttribute
    public TaskComment getTaskComment() {
        return taskComment;
    }



    public void setTaskComment(TaskComment taskComment) {
        this.taskComment = taskComment;
    }

    @Column(name = "attachmentLocation", length = 500)
    @Type(type = "escaped_text")
    @JSON
    @XmlAttribute
    public String getAttachmentLocation() {
        return attachmentLocation;
    }
    
    public void setAttachmentLocation(String attachmentLocation) {
        this.attachmentLocation = attachmentLocation;
    }
    
    @Column(name = "orginalFileName", length = 500)
    @Type(type = "escaped_text")
    @JSON
    @XmlAttribute
    public String getOriginalFileName() {
        return originalFileName;
    }
    
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
    
    @Column(name = "fileContentType", length = 100)
    @Type(type = "escaped_text")
    @JSON
    @XmlAttribute
    public String getFileContentType() {
        return fileContentType;
    }
    
    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }
    
    
       

}
