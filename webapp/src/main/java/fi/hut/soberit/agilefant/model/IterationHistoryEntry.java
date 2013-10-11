package fi.hut.soberit.agilefant.model;

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

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

@Entity
@Table(name = "history_iterations", uniqueConstraints = @UniqueConstraint(columnNames={"iteration_id","timestamp"}))
@XmlAccessorType( XmlAccessType.NONE )
public class IterationHistoryEntry {

    private int id;

    private Iteration iteration;

    private long effortLeftSum;

    private long originalEstimateSum;

    private long deltaOriginalEstimate;

    private LocalDate timestamp;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(optional = false)
    public Iteration getIteration() {
        return iteration;
    }

    public void setIteration(Iteration iteration) {
        this.iteration = iteration;
    }

    public long getEffortLeftSum() {
        return effortLeftSum;
    }

    public void setEffortLeftSum(long effortLeftSum) {
        this.effortLeftSum = effortLeftSum;
    }

    public long getOriginalEstimateSum() {
        return originalEstimateSum;
    }

    public void setOriginalEstimateSum(long originalEstimateSum) {
        this.originalEstimateSum = originalEstimateSum;
    }

    public long getDeltaOriginalEstimate() {
        return deltaOriginalEstimate;
    }

    public void setDeltaOriginalEstimate(long deltaOriginalEstimate) {
        this.deltaOriginalEstimate = deltaOriginalEstimate;
    }

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @Column(nullable = false)
    public LocalDate getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }


}
