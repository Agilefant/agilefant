package fi.hut.soberit.agilefant.business.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.AuthorizationBusiness;
import fi.hut.soberit.agilefant.business.BacklogBusiness;
import fi.hut.soberit.agilefant.business.IterationBusiness;
import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Team;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.security.SecurityUtil;

@Service("authorizationBusiness")
public class AuthorizationBusinessImpl implements AuthorizationBusiness {

	@Autowired
	BacklogBusiness backlogBusiness;
	
	@Autowired
	IterationBusiness iterationBusiness;
	
	@Autowired
	UserBusiness userBusiness;
	
	@Override
	@Transactional(readOnly=true)
	public boolean isBacklogAccessible(int backlogId, User user) {
		user = this.userBusiness.retrieve(user.getId());		
        Collection<Team> teams = user.getTeams();
        
        Product product = (backlogBusiness.getParentProduct(backlogBusiness.retrieve(backlogId)));
        if(product == null){
            //standalone iteration
            Iteration iteration = iterationBusiness.retrieve(backlogId);
            if(iteration.isStandAlone()){
                for (Iterator<Team> iter = teams.iterator(); iter.hasNext();){
                    Team team = (Team) iter.next();
                    
                    Set<Iteration> iterations = team.getIterations();
                    if (iterations.contains(iteration)) {
                        return true; 
                    }
                }
                return false;
            }            
        }        

        for (Iterator<Team> iter = teams.iterator(); iter.hasNext();){
            Team team = (Team) iter.next();
            Set<Product> prods = team.getProducts();
            if (prods.contains(product)) {
                return true; 
            }
        }
        return false;
	}
}
