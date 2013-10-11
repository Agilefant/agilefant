package fi.hut.soberit.agilefant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "history_backlogs")
@XmlAccessorType( XmlAccessType.NONE )
public class BacklogHistoryEntry {

    private int id;

    private Project project;
    
    private long branchMax;

    private long estimateSum;

    private long doneSum;
    
    private long rootSum;

    private DateTime timestamp;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * This class is used only to draw project burnups.
     * If we allow general Backlogs then we would need to adjust
     * other places to work properly (for example deletion to cascade).
     * @return
     */
    @ManyToOne
    public Project getBacklog() {
        return project;
    }

    public void setBacklog(Project project) {
        this.project = project;
    }

    public long getEstimateSum() {
        return estimateSum;
    }

    public void setEstimateSum(long estimateSum) {
        this.estimateSum = estimateSum;
    }

    public long getDoneSum() {
        return doneSum;
    }

    public void setDoneSum(long doneSum) {
        this.doneSum = doneSum;
    }
    
    public long getBranchMax() {
        return branchMax;
    }
    
    public void setBranchMax(long branchMax) {
        this.branchMax = branchMax;
    }
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime", parameters = @Parameter(name = "databaseZone", value = "jvm"))
    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Column(nullable=false)
    public long getRootSum() {
        return rootSum;
    }
    
    public void setRootSum(long rootSum) {
        this.rootSum = rootSum;
    }

}
