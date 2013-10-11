package fi.hut.soberit.agilefant.db.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.AssignmentDAO;
import fi.hut.soberit.agilefant.model.Assignment;
import fi.hut.soberit.agilefant.model.User;

@Repository("assignmentDAO")
public class AssignmentDAOHibernate extends GenericDAOHibernate<Assignment> implements
        AssignmentDAO {

    public AssignmentDAOHibernate() {
        super(Assignment.class);
    }

    public List<Assignment> assigmentsInBacklogTimeframe(Interval interval,
            User user) {
        Criteria crit = this.createCriteria(Assignment.class);
        Criteria backlog = crit.createCriteria("backlog");
        crit.createCriteria("user").add(Restrictions.idEq(user.getId()));
        DateTime startDate = interval.getStart();
        DateTime endDate = interval.getEnd();
        //iteration may start during the interval
        Criterion startDateLimit = Restrictions.between("startDate", startDate,
                endDate);
        //iteration end during the interval
        Criterion endDateLimit = Restrictions.between("endDate", startDate,
                endDate);
        //interval may be within the iteration
        Criterion overlaps = Restrictions.or(startDateLimit, endDateLimit);
        Criterion withinIteration = Restrictions.and(Restrictions.le(
                "startDate", startDate), Restrictions.ge("endDate", endDate));
        backlog.add(Restrictions.or(overlaps, withinIteration));
        return asList(crit);
    }

}
