package fi.hut.soberit.agilefant.db;

import java.util.List;

import fi.hut.soberit.agilefant.model.Login;
import fi.hut.soberit.agilefant.model.User;

/**
 * DAO object for Login model object
 * 
 */
public interface LoginDAO extends GenericDAO<Login> {

	/**
	 * Retrieve logins for a user
	 * 
	 * @param user user for which logins are to be retrieved
	 * @return list of logins for user
	 */
    public List<Login> retrieveLoginsByUser(User user);
    
    /**
     * Retrieve the number of logins for a user
     * 
     * @param user user for which login count is to be retrieved
     * @return login count
     */
    public int retrieveLoginCountByUser(User user);
}

