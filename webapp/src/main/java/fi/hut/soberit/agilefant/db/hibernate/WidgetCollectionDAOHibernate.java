package fi.hut.soberit.agilefant.db.hibernate;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.WidgetCollectionDAO;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.model.WidgetCollection;

@Repository("widgetCollectionDAO")
public class WidgetCollectionDAOHibernate extends
        GenericDAOHibernate<WidgetCollection> implements WidgetCollectionDAO {
    
    public WidgetCollectionDAOHibernate() {
        super(WidgetCollection.class);
    }
    
    /** {@inheritDoc} */
    public List<WidgetCollection> getCollectionsForUser(User user) {
        Criteria collectionCriteria = this.createCriteria(WidgetCollection.class);
        if (user == null) {
            collectionCriteria.add(Restrictions.isNull("user"));
        }
        else {
            collectionCriteria.add(Restrictions.eq("user", user));
        }
        
        collectionCriteria.addOrder(Order.asc("name"));
        return asList(collectionCriteria);
    }
}
