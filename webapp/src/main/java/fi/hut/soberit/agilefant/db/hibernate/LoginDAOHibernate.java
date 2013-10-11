package fi.hut.soberit.agilefant.db.hibernate;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.LoginDAO;
import fi.hut.soberit.agilefant.model.Login;
import fi.hut.soberit.agilefant.model.User;

@Repository("loginDAO")
public class LoginDAOHibernate extends GenericDAOHibernate<Login> implements
        LoginDAO {

    public LoginDAOHibernate() {
        super(Login.class);
    }
    
    public List<Login> retrieveLoginsByUser(User user) {
        Criteria crit = this.createCriteria(Login.class);
        crit.add(Restrictions.eq("user", user));
        //crit.add(Restrictions.ge("endDate", new DateTime()));
        crit.addOrder(Order.asc("time"));
        return asList(crit);
    }

	@Override
	public int retrieveLoginCountByUser(User user) {
        Criteria crit = this.createCriteria(Login.class);
        crit.add(Restrictions.eq("user", user));
        crit.setProjection(Projections.rowCount());
        return ((Long)this.uniqueResult(crit)).intValue();
	}
}
