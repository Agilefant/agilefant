package fi.hut.soberit.agilefant.db.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.SettingDAO;
import fi.hut.soberit.agilefant.model.Setting;

@Repository("settingDAO")
public class SettingDAOHibernate extends GenericDAOHibernate<Setting> implements SettingDAO {

    public SettingDAOHibernate() {
        super(Setting.class);
    }

    /**
     * {@inheritDoc}
     */
    public Setting getByName(String name) {
        Criteria criteria = this.createCriteria(this.getPersistentClass());
        criteria.add(Restrictions.eq("name", name));
        List<Setting> list = this.asList(criteria);
        return super.getFirst(list);
    }

    /**
     * {@inheritDoc}
     */
    public List<Setting> getAllOrderByName() {
        Criteria criteria = this.createCriteria(this.getPersistentClass());
        criteria.addOrder(Order.asc("name"));
        return this.asList(criteria);
    }

    @Override
    public Collection<Setting> getAll() {
        Criteria criteria = this.createCriteria(this.getPersistentClass());
        return this.asList(criteria);
    }

}
