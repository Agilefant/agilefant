package fi.hut.soberit.agilefant.db.history.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.history.BacklogHistoryDAO;
import fi.hut.soberit.agilefant.model.AgilefantRevisionEntity;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryRank;
import fi.hut.soberit.agilefant.transfer.AgilefantHistoryEntry;
import fi.hut.soberit.agilefant.transfer.HistoryRowTO;

@Repository("backlogHistoryDAO")
public class BacklogHistoryDAOImpl extends GenericHistoryDAOImpl<Backlog>
        implements BacklogHistoryDAO {

    public BacklogHistoryDAOImpl() {
        super(Backlog.class);
    }

    @SuppressWarnings("unchecked")
    public List<AgilefantHistoryEntry> retrieveDeletedStories(Backlog backlog) {
        AuditQuery query = this.getAuditReader().createQuery()
                .forRevisionsOfEntity(StoryRank.class, false, true);
        query.add(AuditEntity.property("backlog_id").eq(backlog.getId()));
        query.add(AuditEntity.revisionType().eq(RevisionType.DEL));
        query.addProjection(AuditEntity.revisionType());
        query.addProjection(AuditEntity.property("story_id"));
        
        query.addProjection(AuditEntity.revisionNumber());
        query.addProjection(AuditEntity.revisionProperty("timestamp"));
        query.addProjection(AuditEntity.revisionProperty("userId"));
        query.addProjection(AuditEntity.revisionProperty("userName"));

        List<Object[]> data = query.getResultList();

        List<AgilefantHistoryEntry> result = new ArrayList<AgilefantHistoryEntry>();
        
        for (Object[] row : data) {
            Integer storyId = (Integer) row[1];
            AgilefantRevisionEntity rev = new AgilefantRevisionEntity();
            rev.setId((Integer)row[2]);
            rev.setTimestamp((Long)row[3]);
            rev.setUserId((Integer)row[4]);
            rev.setUserName((String)row[5]);
            result.add(new AgilefantHistoryEntry(storyId, (RevisionType)row[0], rev));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<AgilefantHistoryEntry> retrieveAddedStories(Backlog backlog) {
        AuditQuery query = this.getAuditReader().createQuery()
                .forRevisionsOfEntity(StoryRank.class, false, true);
        query.add(AuditEntity.property("backlog_id").eq(backlog.getId()));
        query.add(AuditEntity.revisionType().eq(RevisionType.ADD));

        List<Object[]> data = query.getResultList();

        List<AgilefantHistoryEntry> result = new ArrayList<AgilefantHistoryEntry>();

        for (Object[] row : data) {
            Story story = ((StoryRank) row[0]).getStory();
            result.add(new AgilefantHistoryEntry(story,
                    (AgilefantRevisionEntity) row[1], (RevisionType) row[2]));
        }
        return result;
    }

    
    @SuppressWarnings("unchecked")
    public List<AgilefantHistoryEntry> retrieveModifiedStories(Backlog backlog) {
        AuditQuery query = this.getAuditReader().createQuery()
                .forRevisionsOfEntity(StoryRank.class, false, true);
        query.add(AuditEntity.property("backlog_id").eq(backlog.getId()));
        query.add(AuditEntity.revisionType().eq(RevisionType.MOD));
        query.addProjection(AuditEntity.revisionType());
        query.addProjection(AuditEntity.property("story_id"));
        
        query.addProjection(AuditEntity.revisionNumber());
        query.addProjection(AuditEntity.revisionProperty("timestamp"));
        query.addProjection(AuditEntity.revisionProperty("userId"));
        query.addProjection(AuditEntity.revisionProperty("userName"));

        List<Object[]> data = query.getResultList();

        List<AgilefantHistoryEntry> result = new ArrayList<AgilefantHistoryEntry>();
        
        for (Object[] row : data) {
            Integer storyId = (Integer) row[1];
            AgilefantRevisionEntity rev = new AgilefantRevisionEntity();
            rev.setId((Integer)row[2]);
            rev.setTimestamp((Long)row[3]);
            rev.setUserId((Integer)row[4]);
            rev.setUserName((String)row[5]);
            result.add(new AgilefantHistoryEntry(storyId, (RevisionType)row[0], rev));
        }
        return result;
    }
    
    
    public List<HistoryRowTO> retrieveLatestChanges(int objectId,
            Integer numberOfChanges) {
        // TODO Auto-generated method stub
        return null;
    }

}
