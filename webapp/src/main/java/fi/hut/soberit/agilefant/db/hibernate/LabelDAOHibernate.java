package fi.hut.soberit.agilefant.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.LabelDAO;
import fi.hut.soberit.agilefant.model.Label;
import fi.hut.soberit.agilefant.model.Story;

@Repository("labelDAO")
public class LabelDAOHibernate extends GenericDAOHibernate<Label> implements
LabelDAO {

    public LabelDAOHibernate() {
        super(Label.class);
    }

    public boolean labelExists(String labelName, Story story) {
        Criteria crit = this.createCriteria(Label.class);
        crit.add(Restrictions.eq("story", story));
        crit.add(Restrictions.eq("name", labelName.toLowerCase()));
        crit.setProjection(Projections.projectionList().add(Projections.count("id")));
        Object ret = this.uniqueResult(crit);
        long count = (Long)ret;
        return count > 0;
    }
    
    public List<Label> lookupLabelsLike(String labelName) {
        Criteria crit = this.createCriteria(Label.class);
        crit.add(Restrictions.like("name", labelName.toLowerCase() + "%"));
        crit.addOrder(Order.asc("name"));
        ProjectionList plist = Projections.projectionList();
        plist.add(Projections.groupProperty("name"));
        plist.add(Projections.property("displayName"));
        plist.add(Projections.property("id"));
        crit.setProjection(plist);
        List<Object[]> clist = asList(crit);
        ArrayList<Label> labelList = new ArrayList<Label>();
        for (Object[] obj : clist){
            Label tempLabel = new Label();
            tempLabel.setDisplayName((String)obj[1]);
            tempLabel.setName((String)obj[0]);
            tempLabel.setId((Integer)obj[2]);
            labelList.add(tempLabel);
        }
        return labelList;
        
    }

}
    
