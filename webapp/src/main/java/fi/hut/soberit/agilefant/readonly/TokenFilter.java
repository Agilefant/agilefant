package fi.hut.soberit.agilefant.readonly;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import fi.hut.soberit.agilefant.business.IterationBusiness;

/**
 * Filter which redirects valid read only convenience urls to actual iteration
 *
 * @author jkorri
 */
@Component("tokenFilter")
public class TokenFilter extends GenericFilterBean {
 
	@Autowired
	IterationBusiness iterationBusiness;
	
	private static final int GROUP = 1;

	private static final Pattern tokenURLPattern = Pattern.compile("^.*/token/([0-9]+)$");
	
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
                        
        Matcher matcher = tokenURLPattern.matcher(req.getRequestURL().toString());
        if(matcher.matches()) {
        	String token = matcher.group(GROUP);
            if (this.iterationBusiness.getIterationCountFromReadonlyToken(token)>0) {
                res.sendRedirect(req.getContextPath() + "/ROIteration.action?readonlyToken=" + token);
                return;
            }
        }
        
        throw new InvalidReadOnlyAccessException("Invalid read only token");
    }    
}
