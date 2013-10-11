package fi.hut.soberit.agilefant.db.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.business.impl.StoryHierarchyBusinessImpl;
import fi.hut.soberit.agilefant.db.StoryHierarchyDAO;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryState;
import fi.hut.soberit.agilefant.transfer.StoryTreeBranchMetrics;

@Repository("storyHierarchyDAO")
public class StoryHierarchyDAOHibernate extends GenericDAOHibernate<Story>
        implements StoryHierarchyDAO {

    public StoryHierarchyDAOHibernate() {
        super(Story.class);
    }

    private void attachLeafFilters(Criteria projectCrit,
            Criteria iterationCrit, Project project) {
        projectCrit.add(Restrictions.eq("backlog", project));
        projectCrit.add(Restrictions.isEmpty("children"));
	iterationCrit.createCriteria("backlog").add(Restrictions.eq("parent", project));
        iterationCrit.add(Restrictions.isEmpty("children"));
    }
    
    private void attachBranchFilters(Criteria projectCrit,
            Criteria iterationCrit, Project project) {
        projectCrit.add(Restrictions.eq("backlog", project));
        projectCrit.add(Restrictions.isNotEmpty("children"));
        iterationCrit.createCriteria("backlog").add(
                Restrictions.eq("parent", project));
        iterationCrit.add(Restrictions.isNotEmpty("children"));
    }

    /**
     * {@inheritDoc}
     */
    public List<Story> retrieveProjectLeafStories(Project project) {
        Criteria projectCrit = this.createCriteria(Story.class);
        Criteria iterationCrit = this.createCriteria(Story.class);
        this.attachLeafFilters(projectCrit, iterationCrit, project);
        
        List<Story> projectLeaf = asList(projectCrit);
        List<Story> iterationLeaf = asList(iterationCrit);

        List<Story> ret = new ArrayList<Story>();
        ret.addAll(projectLeaf);
        ret.addAll(iterationLeaf);

        return ret;
    }

    private static long sum(Long val1, Long val2) {
        long sum = 0;
        if (val1 != null) {
            sum += val1;
        }
        if (val2 != null) {
            sum += val2;
        }
        return sum;
    }

    /**
     * {@inheritDoc}
     */
    public long totalLeafStoryPoints(Project project) {
        Criteria projectCrit = this.createCriteria(Story.class);
        Criteria iterationCrit = this.createCriteria(Story.class);
        this.attachLeafFilters(projectCrit, iterationCrit, project);
        
        iterationCrit.add(Restrictions.ne("state", StoryState.DEFERRED));
        projectCrit.add(Restrictions.ne("state", StoryState.DEFERRED));
        
        projectCrit.setProjection(Projections.projectionList().add(
                Projections.sum("storyPoints")));
        iterationCrit.setProjection(Projections.projectionList().add(
                Projections.sum("storyPoints")));
        return sum((Long) this.uniqueResult(projectCrit),
                (Long) this.uniqueResult(iterationCrit));
    }

    /**
     * {@inheritDoc}
     */
    public long totalLeafStoryPoints(Iteration iteration) {
        Criteria iterationCrit = this.createCriteria(Story.class);
        
        iterationCrit.add(Restrictions.isEmpty("children"));
        
        iterationCrit.add(Restrictions.ne("state", StoryState.DEFERRED));
        
        iterationCrit.setProjection(Projections.projectionList().add(
                Projections.sum("storyPoints")));
        return sum(0L,
                (Long) this.uniqueResult(iterationCrit));
    }
    
    /**
     * {@inheritDoc}
     */
    public long totalLeafDoneStoryPoints(Project project) {
        Criteria projectCrit = this.createCriteria(Story.class);
        Criteria iterationCrit = this.createCriteria(Story.class);
        this.attachLeafFilters(projectCrit, iterationCrit, project);
        projectCrit.setProjection(Projections.projectionList().add(
                Projections.sum("storyPoints")));
        iterationCrit.setProjection(Projections.projectionList().add(
                Projections.sum("storyPoints")));
        projectCrit.add(Restrictions.eq("state", StoryState.DONE));
        iterationCrit.add(Restrictions.eq("state", StoryState.DONE));
        return sum((Long) this.uniqueResult(projectCrit),
                (Long) this.uniqueResult(iterationCrit));
    }

    /**
     * {@inheritDoc}
     */
    public long totalLeafDoneStoryPoints(Iteration iteration) {
        Criteria iterationCrit = this.createCriteria(Story.class);
        iterationCrit.add(Restrictions.isEmpty("children"));
        iterationCrit.setProjection(Projections.projectionList().add(
                Projections.sum("storyPoints")));
        iterationCrit.add(Restrictions.eq("state", StoryState.DONE));
        iterationCrit.add(Restrictions.isNotNull("storyPoints"));
        iterationCrit.add(Restrictions.eq("iteration", iteration));
        Long result = (Long) this.uniqueResult(iterationCrit);
        return result == null ? 0 : result;
    }
    
    private void attachRootFilters(Criteria projectCrit, Criteria iterationCrit, Criteria standaloneIterationCrit, int projectId) {
        LogicalExpression parentInProductBacklog = Restrictions.and(
                Restrictions.isNotNull("parent"), Restrictions.eqProperty(
                        "parentStory.backlog", "project.parent"));
        // stories attached to the project
        projectCrit.add(Restrictions.eq("backlog.id", projectId));
        projectCrit.createAlias("backlog", "project");

        projectCrit.createAlias("parent", "parentStory",CriteriaSpecification.LEFT_JOIN);
        Criterion parentFilter = Restrictions.or(Restrictions.isNull("parent"),parentInProductBacklog);
        
        projectCrit.add(parentFilter);
        projectCrit.add(Restrictions.isNull("iteration"));
        
        // Stories attached to iterations under the project
        iterationCrit.createAlias("parent", "parentStory", CriteriaSpecification.LEFT_JOIN);
        iterationCrit.createCriteria("iteration").add(Restrictions.eq("parent.id", projectId))
            .createAlias("parent", "project");
        iterationCrit.add(parentFilter);


        // Stories in standalone iterations and this project
        if (standaloneIterationCrit != null) {
            // story's backlog is this project
            standaloneIterationCrit.add(Restrictions.eq("backlog.id", projectId));
            
            // story's iteration is not null
            standaloneIterationCrit.add(Restrictions.isNotNull("iteration"));
            
            // story's iteration doesn't have a parent
            standaloneIterationCrit.createCriteria("iteration").add(Restrictions.isNull("parent"));
            
            standaloneIterationCrit.createAlias("backlog", "project");
            
            // story doesn't have a parent OR parent in product
            standaloneIterationCrit.createAlias("parent", "parentStory", CriteriaSpecification.LEFT_JOIN);
            standaloneIterationCrit.add(parentFilter);
        }
        
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Story> retrieveProjectRootStories(int projectId) {

        Criteria projectCrit = this.createCriteria(Story.class);
        Criteria iterationCrit = this.createCriteria(Story.class);
        Criteria standaloneIterationCrit = this.createCriteria(Story.class);
        this.attachRootFilters(projectCrit, iterationCrit, standaloneIterationCrit, projectId);
        List<Story> directProjectRoots = asList(projectCrit);
        List<Story> iterationRoots = asList(iterationCrit);
        List<Story> standaloneIterationRoots = asList(standaloneIterationCrit);

        List<Story> ret = new ArrayList<Story>();
        ret.addAll(directProjectRoots);
        ret.addAll(iterationRoots);
        ret.addAll(standaloneIterationRoots);
        
        Collections.sort(ret, new PropertyComparator("treeRank", true, true));
        
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    public long totalRootStoryPoints(Project project) {
        Criteria projectCrit = this.createCriteria(Story.class);
        Criteria iterationCrit = this.createCriteria(Story.class);
        Criteria standaloneIterationCrit = this.createCriteria(Story.class);
        this.attachRootFilters(projectCrit, iterationCrit, standaloneIterationCrit, project.getId());
        
        iterationCrit.add(Restrictions.ne("state", StoryState.DEFERRED));
        projectCrit.add(Restrictions.ne("state", StoryState.DEFERRED));
        standaloneIterationCrit.add(Restrictions.ne("state", StoryState.DEFERRED));
        
        projectCrit.setProjection(Projections.projectionList().add(
                Projections.sum("storyPoints")));
        iterationCrit.setProjection(Projections.projectionList().add(
                Projections.sum("storyPoints")));
        standaloneIterationCrit.setProjection(Projections.projectionList().add(
                Projections.sum("storyPoints")));
        long sum = sum((Long) this.uniqueResult(projectCrit), (Long) this.uniqueResult(iterationCrit));
        return sum((Long) this.uniqueResult(standaloneIterationCrit), sum);
    }

    /** {@inheritDoc} */
    public List<Story> retrieveProductRootStories(int productId) {
        Criteria rootFilter = getRootStoryCriteria(productId);
        rootFilter.addOrder(Order.asc("treeRank"));
        return asList(rootFilter);
    }

    /** {@inheritDoc} */
    public int getMaximumTreeRank(int productId) {
        Criteria rootFilter = getRootStoryCriteria(productId);
        rootFilter.addOrder(Order.desc("treeRank"));
        rootFilter.setMaxResults(1);
        Story story = (Story)uniqueResult(rootFilter);
        if (story == null) {
            return 0;
        } else {    
            return story.getTreeRank();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public long totalBranchStoryPoints(Project project)
    {
        StoryHierarchyBusinessImpl impl = new StoryHierarchyBusinessImpl();
        long sum = 0;
        for (Story s : this.retrieveProjectRootStories(project.getId())) {
            StoryTreeBranchMetrics m = impl.calculateStoryTreeMetrics(s);
            sum += m.getEstimatedPoints();
        }
        return sum;
    }
     
    private Criteria getRootStoryCriteria(int productId) {
        Criteria rootFilter = this.createCriteria(Story.class);
        rootFilter.createAlias(
                "backlog.parent", "secondParent", CriteriaSpecification.LEFT_JOIN)
                .createAlias("secondParent.parent", "thirdParent",
                        CriteriaSpecification.LEFT_JOIN);
        rootFilter.add(Restrictions.or(Restrictions.or(Restrictions.eq(
                "backlog.id", productId), Restrictions.eq("secondParent.id",
                productId)), Restrictions.eq("thirdParent.id", productId)));
        rootFilter.add(Restrictions.isNull("parent"));
        return rootFilter;
    }

}
    
