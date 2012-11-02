package fi.hut.soberit.agilefant.security;

import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.model.ExactEstimate;
import fi.hut.soberit.agilefant.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.security.*;
import org.springframework.security.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;


public class PreAuthenticatedAgilefantUserDetailsService implements AuthenticationUserDetailsService {


    public static final String EMPTY_TEXT = "PLEASE UPDATE";
    public static final String EMPTY_EMAIL = "PLEASE.UPDATE@YOUR.DOM";
    private static final String EMPTY_PASSWORD = "";
    private UserBusiness userBusiness;

    public void setUserBusiness(UserBusiness userBusiness) {
        this.userBusiness = userBusiness;
    }

    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {


        // try getting user by given username
        fi.hut.soberit.agilefant.model.User user = userBusiness.retrieveByLoginName(userName);

        // no user found, create one, because it IS authenticated
        if (user == null) {
            user = new User();
            user.setLoginName(userName);
            user.setFullName(EMPTY_TEXT);
            user.setInitials(EMPTY_TEXT);
            user.setEmail(EMPTY_EMAIL);
            user.setAdmin(false);
            user = userBusiness.storeUser(user, null, EMPTY_PASSWORD,EMPTY_PASSWORD);
        }

        // success, return UserDetails-instance
        return new AgilefantUserDetails(user);
    }

    /**
     * Get a UserDetails object based on the user name contained in the given
     * token
     */
    public final UserDetails loadUserDetails(Authentication token) throws AuthenticationException {
            return loadUserByUsername(token.getPrincipal().toString());
    }
}
