package fi.hut.soberit.agilefant.db.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.TeamDAO;
import fi.hut.soberit.agilefant.model.Team;

@Repository("teamDAO")
public class TeamDAOHibernate extends GenericDAOHibernate<Team> implements
        TeamDAO {

    public TeamDAOHibernate() {
        super(Team.class);
    }
    
    /** {@inheritDoc} */
    public Team getByTeamName(String teamName) {
        Criteria crit = this.createCriteria(Team.class);
        crit.add(Restrictions.eq("name", teamName));
        return firstResult(crit);
    }

}
