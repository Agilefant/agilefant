package fi.hut.soberit.agilefant.business.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.IterationBusiness;
import fi.hut.soberit.agilefant.business.TeamBusiness;
import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.business.ProductBusiness;
import fi.hut.soberit.agilefant.db.TeamDAO;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Team;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.model.Product;

@Service("teamBusiness")
@Transactional
public class TeamBusinessImpl extends GenericBusinessImpl<Team> implements
        TeamBusiness {

    private TeamDAO teamDAO;

    private UserBusiness userBusiness;
    
    @Autowired
    private ProductBusiness productBusiness;
    
    @Autowired
    private IterationBusiness iterationBusiness;
    
    public TeamBusinessImpl() {
        super(Team.class);
    }
    
    @Autowired
    public void setTeamDAO(TeamDAO teamDAO) {
        this.genericDAO = teamDAO;
        this.teamDAO = teamDAO;
    }
    
    @Autowired
    public void setUserBusiness(UserBusiness userBusiness) {
        this.userBusiness = userBusiness;
    }
    
    @Transactional(readOnly = true)
    public Team getByTeamName(String teamName) {
        return teamDAO.getByTeamName(teamName);
    }


    /** {@inheritDoc} */
    @Transactional
    public Team storeTeam(Team team, Set<Integer> userIds, Set<Integer> productIds, Set<Integer> iterationIds) {
        if (team == null) {
            throw new IllegalArgumentException("Team must be supplied.");
        }
        
        // Get users
        Set<User> users = new HashSet<User>();
        if (userIds != null) {
            for (Integer uid : userIds) {
                users.add(userBusiness.retrieve(uid));
            }
            team.setUsers(users);
        }
        
        // Get products
        Set<Product> products = new HashSet<Product>();
        if (productIds != null) {
            for (Integer pid : productIds) {
                products.add(productBusiness.retrieve(pid));
            }
            team.setProducts(products);
        }
        
        // Get iterations
        Set<Iteration> iterations = new HashSet<Iteration>();
        if (iterationIds != null) {
            for (Integer pid : iterationIds) {
                iterations.add(iterationBusiness.retrieve(pid));
            }
            team.setIterations(iterations);
        }
        
        Team stored = null;
        if (team.getId() != 0) {
            stored = team;
            teamDAO.store(stored);
        }
        else {
            int newId = (Integer)teamDAO.create(team);
            stored = teamDAO.get(newId);
        }
        
        return stored;
    }

	@Override
	@Transactional(readOnly=true)
	public Set<User> getUsersInSameTeams(int userId) {
		User user = this.userBusiness.retrieve(userId);

		Set<User> users = new HashSet<User>();
		
        Collection<Team> teams = user.getTeams();
        for (Team team: teams) {
            users.addAll(team.getUsers());
        }
        
        return users;
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Team> withUsers(Call<Collection<Team>> call) {
		Collection<Team> teams = call.call();
		for(Team team : teams) {
			Hibernate.initialize(team.getUsers());
		}
		return teams;
	}

}
