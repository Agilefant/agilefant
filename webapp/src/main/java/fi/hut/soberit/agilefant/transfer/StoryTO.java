package fi.hut.soberit.agilefant.transfer;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.model.WhatsNextStoryEntry;
import fi.hut.soberit.agilefant.util.BeanCopier;
import fi.hut.soberit.agilefant.util.StoryMetrics;
import flexjson.JSON;

@XmlType
@XmlAccessorType( XmlAccessType.NONE )
public class StoryTO extends Story {

    // Additional fields
    private StoryMetrics metrics;
    // Context-specific rank
    private Integer rank;
    // Helper fields
    private long effortSpent;    
    private Integer workQueueRank;

    public StoryTO() {}
    
    public StoryTO(Story story, int workQueueRank) {
        BeanCopier.copy(story, this);
        this.workQueueRank = workQueueRank;
    }
    
    public StoryTO(Story story) {
        BeanCopier.copy(story, this);
    }

    public void setMetrics(StoryMetrics metrics) {
        this.metrics = metrics;
    }

    public StoryMetrics getMetrics() {
        return metrics;
    }

    @JSON
    @XmlAttribute
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
    
    @JSON(include=false)
    public Collection<User> getWorkingOnStory() {
        ArrayList<User> returned = new ArrayList<User>();
        for (WhatsNextStoryEntry e: getWhatsNextStoryEntries()) {
            returned.add(e.getUser());
        }
        
        return returned;
    }
    
    @JSON(include = true)
    public long getEffortSpent() {
        return effortSpent;
    }

    public void setEffortSpent(long effortSpent) {
        this.effortSpent = effortSpent;
    }
    
    @JSON
    @XmlAttribute
    public Integer getWorkQueueRank() {
        return workQueueRank;
    }

    public void setWorkQueueRank(Integer workQueueRank) {
        this.workQueueRank = workQueueRank;
    }

}
