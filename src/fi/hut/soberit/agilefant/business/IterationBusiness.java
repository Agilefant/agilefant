package fi.hut.soberit.agilefant.business;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import fi.hut.soberit.agilefant.model.ExactEstimate;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.IterationHistoryEntry;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.transfer.AgilefantHistoryEntry;
import fi.hut.soberit.agilefant.transfer.AssignmentTO;
import fi.hut.soberit.agilefant.transfer.IterationMetrics;
import fi.hut.soberit.agilefant.transfer.IterationTO;

public interface IterationBusiness extends GenericBusiness<Iteration> {

    public IterationTO getIterationContents(int iterationId);

    public IterationMetrics getIterationMetrics(Iteration iteration);

    ExactEstimate calculateDailyVelocity(LocalDate startDate, LocalDate end, IterationHistoryEntry yesterdayEntry);

    public IterationTO storeStandAlone(int iterationId, Iteration iterationData, Set<Integer> assigneeIds, Set<Integer> teams);
    
    public IterationTO store(int iterationId, int parentBacklogId,
            Iteration iterationData, Set<Integer> assigneeIds, Set<Integer> teams);

    public Collection<Iteration> retrieveCurrentAndFutureIterations();
    
    public Collection<Iteration> retrieveAllStandAloneIterations();

    public Set<AssignmentTO> calculateAssignedLoadPerAssignee(Iteration iter);

    public Integer calculateVariance(Iteration iter);

    void delete(int id);

    void delete(Iteration iteration);

    void deleteAndUpdateHistory(int id);

    public List<AgilefantHistoryEntry> retrieveChangesInIterationStories(
            Iteration iteration);
    
    public IterationTO retrieveIterationOnlyLeafStories(int iterationId);

    public List<AgilefantHistoryEntry> retrieveChangesInIterationTasks(
            Iteration iteration);
    
    public List<AgilefantHistoryEntry> renderSortedTaskAndStoryRevisions(
            Iteration iteration);
    
    public Iteration retreiveIterationByReadonlyToken(String readonlyToken);
    
    public int getIterationCountFromReadonlyToken(String readonlyToken);
}