package fi.hut.soberit.agilefant.db.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.UserDAO;
import fi.hut.soberit.agilefant.model.User;

/**
 * Hibernate implementation of UserDAO interface using GenericDAOHibernate.
 */
@Repository("userDAO")
public class UserDAOHibernate extends GenericDAOHibernate<User> implements
        UserDAO {

    public UserDAOHibernate() {
        super(User.class);
    }

    /** {@inheritDoc} */
    public User getByLoginName(String loginName) {
        Criteria crit = this.createCriteria(User.class);
        crit.add(Restrictions.eq("loginName", loginName));
        return firstResult(crit);
    }

    /** {@inheritDoc} */
    public User getByLoginNameIgnoreCase(String loginName) {
        Criteria crit = this.createCriteria(User.class);
        crit.add(Restrictions.eq("loginName", loginName).ignoreCase());
        return firstResult(crit);
    }

    /** {@inheritDoc} */
    public List<User> listUsersByEnabledStatus(boolean enabled) {
        Criteria crit = this.createCriteria(User.class);
        crit.add(Restrictions.eq("enabled", enabled));
        return asList(crit);
    }

    public List<User> searchByName(String searchTerm) {
        Criteria crit = this.createCriteria(User.class);
        crit.add(Restrictions.or(Restrictions.like("fullName", searchTerm,
                MatchMode.ANYWHERE), Restrictions.like("initials", searchTerm,
                MatchMode.ANYWHERE)));
        return asList(crit);
    }

}
