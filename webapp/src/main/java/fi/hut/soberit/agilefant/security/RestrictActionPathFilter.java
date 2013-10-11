package fi.hut.soberit.agilefant.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Filter that denies requests coming to struts that are not / or /ajax/ path
 * 
 * @author vsseppa
 */
public class RestrictActionPathFilter extends GenericFilterBean {
	
	private static final String STRUTS_ACTION_PATTERN = "^(/ajax(/widgets)?)?/[^/]*.action$";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(!((HttpServletRequest)request).getServletPath().matches(STRUTS_ACTION_PATTERN)) {
			throw new AccessDeniedException("Invalid struts path");
		}
		chain.doFilter(request, response);
	}
}
