package fi.hut.soberit.agilefant.db.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.model.TaskState;
import fi.hut.soberit.agilefant.db.IterationHistoryEntryDAO;
import fi.hut.soberit.agilefant.model.ExactEstimate;
import fi.hut.soberit.agilefant.model.IterationHistoryEntry;
import fi.hut.soberit.agilefant.model.StoryState;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.util.Pair;

@Repository("iterationHistoryEntryDAO")
public class IterationHistoryEntryDAOHibernate extends
        GenericDAOHibernate<IterationHistoryEntry> implements
        IterationHistoryEntryDAO {

    public IterationHistoryEntryDAOHibernate() {
        super(IterationHistoryEntry.class);
    }

    public IterationHistoryEntry retrieveByDate(int iterationId, LocalDate timestamp) {
        return retrieveByDateInternal(iterationId, timestamp);
    }

    private IterationHistoryEntry retrieveByDateInternal(int iterationId, LocalDate timestamp) {
        Criteria crit = this.createCriteria(IterationHistoryEntry.class);
        crit.setMaxResults(1);
        if (timestamp != null) {
            crit.add(Restrictions.le("timestamp", timestamp));
        }
        crit.addOrder(Order.desc("timestamp"));
        crit.add(Restrictions.eq("iteration.id", iterationId));
        return (IterationHistoryEntry) this.uniqueResult(crit);
    }

    public IterationHistoryEntry retrieveLatest(int iterationId) {
        return retrieveByDateInternal(iterationId, null);
    }

    public Pair<ExactEstimate, ExactEstimate> calculateCurrentHistoryData(int iterationId) {
        Pair<ExactEstimate, ExactEstimate> tasksWithoutStorySum = this.calculateCurrentHistoryData_tasksWithoutStory(iterationId);
        Pair<ExactEstimate, ExactEstimate> tasksInsideStorySum = this.calculateCurrentHistoryData_tasksInsideStory(iterationId);
        return extractPairSum(tasksWithoutStorySum, tasksInsideStorySum);
    }

    private Pair<ExactEstimate, ExactEstimate> extractPairSum(
            Pair<ExactEstimate, ExactEstimate> tasksWithoutStorySum,
            Pair<ExactEstimate, ExactEstimate> tasksInsideStorySum) {
        long first = tasksWithoutStorySum.getFirst().getMinorUnits() + tasksInsideStorySum.getFirst().getMinorUnits();
        long second = tasksWithoutStorySum.getSecond().getMinorUnits() + tasksInsideStorySum.getSecond().getMinorUnits();
        
        return Pair.create(new ExactEstimate(first), new ExactEstimate(second));
    }
    
    private Pair<ExactEstimate, ExactEstimate> calculateCurrentHistoryData_tasksWithoutStory(int iterationId) {
        Criteria crit = this.createCriteria(Task.class);
        crit.add(Restrictions.eq("iteration.id", iterationId));
        crit.add(Restrictions.ne("state", TaskState.DEFERRED));
        crit.setProjection(Projections.projectionList().add(
                Projections.sum("effortLeft")).add(
                Projections.sum("originalEstimate")));
        Object[] results = (Object[]) this.uniqueResult(crit);
        
        return parseResultToPair(results);        
    }
    
    private Pair<ExactEstimate, ExactEstimate> calculateCurrentHistoryData_tasksInsideStory(int iterationId) {
        Criteria crit = this.createCriteria(Task.class);
        
        crit.setProjection(Projections.projectionList().add(
                Projections.sum("effortLeft")).add(
                Projections.sum("originalEstimate")));
        crit.add(Restrictions.ne("state", TaskState.DEFERRED));
        crit = crit.createCriteria("story");

        crit.setFetchMode("story", FetchMode.SELECT);
        crit.add(Restrictions.ne("state", StoryState.DEFERRED));
        
        crit = crit.createCriteria("iteration");
        crit.add(Restrictions.idEq(iterationId));
        
        Object[] results = (Object[]) this.uniqueResult(crit);
        
        return parseResultToPair(results);
    }
    
    private Pair<ExactEstimate, ExactEstimate> parseResultToPair(Object[] results) {
        long first = 0;
        long second = 0;
        if (results[0] != null) {
            first = (Long)results[0];
        }
        if (results[1] != null) {
            second = (Long)results[1];
        }
        return Pair.create(new ExactEstimate(first), new ExactEstimate(second));
    }
    
    
    public List<IterationHistoryEntry> getHistoryEntriesForIteration(
            int iterationId) {
        Criteria crit = this.createCriteria(IterationHistoryEntry.class);
        crit.add(Restrictions.eq("iteration.id", iterationId));
        crit.addOrder(Order.asc("timestamp"));
        return asList(crit);
    }
    
}
