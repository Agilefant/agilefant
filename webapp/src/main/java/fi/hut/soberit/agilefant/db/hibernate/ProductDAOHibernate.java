package fi.hut.soberit.agilefant.db.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.ProductDAO;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.util.Pair;

/**
 * Hibernate implementation of ProductDAO interface using GenericDAOHibernate.
 */
@Repository("productDAO")
public class ProductDAOHibernate extends GenericDAOHibernate<Product> implements
        ProductDAO {

    public ProductDAOHibernate() {
        super(Product.class);
    }

    public Collection<Product> getAllOrderByName() {
    	Criteria criteria = this.createCriteria(this.getPersistentClass());
    	criteria.addOrder(Order.asc("name"));
    	return this.asList(criteria);    	
    }
    
    public List<Product> retrieveBacklogTree() {
        Criteria crit = this.createCriteria(this.getPersistentClass());
        crit.createAlias("children", "projects", CriteriaSpecification.LEFT_JOIN);
        crit.createAlias("projects.children", "iterations", CriteriaSpecification.LEFT_JOIN);
        crit.addOrder(Order.asc("name"));
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return asList(crit);
    }

    public List<Story> retrieveLeafStories(Product product) {
        int productId = product.getId();
        
        Criteria leaftStoryCrit = this.createCriteria(Story.class);
        leaftStoryCrit.createAlias(
                "backlog.parent", "secondParent", CriteriaSpecification.LEFT_JOIN)
                .createAlias("secondParent.parent", "thirdParent",
                        CriteriaSpecification.LEFT_JOIN);
        leaftStoryCrit.add(Restrictions.or(Restrictions.or(Restrictions.eq(
                "backlog.id", productId), Restrictions.eq("secondParent.id",
                productId)), Restrictions.eq("thirdParent.id", productId)));
        
        leaftStoryCrit.add(Restrictions.isEmpty("children"));
        return asList(leaftStoryCrit);
    }

    public Pair<DateTime, DateTime> retrieveScheduleStartAndEnd(Product product) {
        Criteria iterations = this.createCriteria(Iteration.class);
        Criteria projects = this.createCriteria(Project.class);
        
        iterations.createCriteria("parent").add(Restrictions.eq("parent", product));
        
        projects.add(Restrictions.eq("parent", product));
        
        ProjectionList minMax = Projections.projectionList();
        minMax.add(Projections.min("startDate"));
        minMax.add(Projections.max("endDate"));
        
        iterations.setProjection(minMax);
        projects.setProjection(minMax);
        
        Object[] iterationDates = uniqueResult(iterations);
        Object[] projectDates = uniqueResult(projects);
        
        DateTime iterationStart = (DateTime)iterationDates[0];
        DateTime iterationEnd = (DateTime)iterationDates[1];
        
        DateTime projectStart = (DateTime)projectDates[0];
        DateTime projectEnd = (DateTime)projectDates[1];
        
        DateTime startDate;
        if(iterationStart == null) {
            startDate = projectStart;
        } else if(projectStart == null) {
            startDate = iterationStart;
        } else if(projectStart.isBefore(iterationStart)) {
            startDate = projectStart;
        } else {
            startDate = iterationStart;
        }
        
        DateTime endDate;
        if(projectEnd == null) {
            endDate = iterationEnd;
        } else if(iterationEnd == null) {
            endDate = projectEnd;
        } else if(projectEnd.isAfter(iterationEnd)) {
            endDate = projectEnd;
        } else {
            endDate = iterationEnd;
        }
        return new Pair<DateTime, DateTime>(startDate, endDate);
    }
}
