package fi.hut.soberit.agilefant.db.hibernate;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.HolidayDAO;
import fi.hut.soberit.agilefant.model.Holiday;
import fi.hut.soberit.agilefant.model.User;

@Repository("holidayDAO")
public class HolidayDAOHibernate extends GenericDAOHibernate<Holiday> implements
        HolidayDAO {

    public HolidayDAOHibernate() {
        super(Holiday.class);
    }
    
    public List<Holiday> retrieveFutureHolidaysByUser(User user) {
        Criteria crit = this.createCriteria(Holiday.class);
        crit.add(Restrictions.eq("user", user));
        crit.add(Restrictions.ge("endDate", new DateTime()));
        crit.addOrder(Order.asc("startDate"));
        return asList(crit);
    }

}
