package fi.hut.soberit.agilefant.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.GenericFilterBean;

import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.model.User;

/**
 * Filter sets current User for the duration of the request
 * 
 * @author jkorri
 */
@Component("setupUserFilter")
public class SetupUserFilter extends GenericFilterBean {

	@Autowired
	UserBusiness userBusiness;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();		

		User user;
		if(principal instanceof AgilefantUserDetails) {
			user = this.userBusiness.retrieve(((AgilefantUserDetails)principal).getUserId());
		} else {
            user = userBusiness.retrieveByLoginName("readonly");
		}
		
    	try {
    		SecurityUtil.setLoggedUser(user);
    		chain.doFilter(request, response);
    	} finally {
    		SecurityUtil.setLoggedUser(null);
    	}
	}	
}
