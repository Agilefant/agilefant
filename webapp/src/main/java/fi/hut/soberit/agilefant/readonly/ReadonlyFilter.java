package fi.hut.soberit.agilefant.readonly;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.GenericFilterBean;

import fi.hut.soberit.agilefant.business.IterationBusiness;

public class ReadonlyFilter extends GenericFilterBean {

	private static final String READONLYTOKEN_PARAM = "readonlyToken";
	
	private static final String READONLY_ACTION_PATTERN = ".*/RO[^/]*\\.action$";
	
	@Autowired
	IterationBusiness iterationBusiness;
	
    @Transactional(readOnly = true)
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String readonlyToken = request.getParameter(READONLYTOKEN_PARAM);
		
		if(readonlyToken!=null) {
			boolean iterationExists = this.iterationBusiness.getIterationCountFromReadonlyToken(readonlyToken) > 0;
			boolean actionIsReadOnly = ((HttpServletRequest)request).getRequestURL().toString().matches(READONLY_ACTION_PATTERN);
			if(iterationExists && actionIsReadOnly) {
				chain.doFilter(request, response);
				return;
			}
		}

        throw new InvalidReadOnlyAccessException("Invalid read only access");
    }
}
