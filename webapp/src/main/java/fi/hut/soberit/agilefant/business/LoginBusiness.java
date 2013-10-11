package fi.hut.soberit.agilefant.business;

import java.util.List;

import fi.hut.soberit.agilefant.model.Login;
import fi.hut.soberit.agilefant.model.User;

/**
 * Business class for login tracking related operations
 * 
 */
public interface LoginBusiness extends GenericBusiness<Login> {
	
	/**
	 * Retrieve logins for a user
	 * 
	 * @param user User for which logins are to be retrieved
	 * @return List of logins for user
	 */
    public List<Login> retrieveLoginsByUser(User user);

    /**
     * Retrieve number of logins for a user
     * 
     * @param user User for which login count is to be retrieved
     * @return login count for User
     */
    public int retrieveLoginCountByUser(User user);
}
