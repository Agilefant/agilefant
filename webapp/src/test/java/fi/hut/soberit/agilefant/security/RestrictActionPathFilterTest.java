package fi.hut.soberit.agilefant.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Assert;
import org.junit.Before;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.security.access.AccessDeniedException;

/**
 * Unit test for RestrictActionPathFilter
 * 
 * @author vsseppa
 */
public class RestrictActionPathFilterTest {
	
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	
	private RestrictActionPathFilter filter;
	
	@Before
	public void setup() {
		this.request = new MockHttpServletRequest();
		this.response = new MockHttpServletResponse();		
		this.filter = new RestrictActionPathFilter();
	}
	
	/**
	 * Test valid paths.
	 * Should not throw an exception.
	 */
	@Test
	public void testValidPath() throws Exception {
		
		this.request.setServletPath("/validpath.action");

		filter.doFilter(request, response, new MockFilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
			}
		});
		
		Assert.assertEquals(200, this.response.getStatus());
		
		this.request.setServletPath("/ajax/anothervalidpath.action");
		
		filter.doFilter(request, response, new MockFilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
			}
		});
		
		Assert.assertEquals(200, this.response.getStatus());
	}
	
	/**
	 * Test invalid path.
	 * Must throw AccessDeniedException
	 */
	@Test(expected = AccessDeniedException.class)
	public void testInvalidPath() throws Exception {
		
		this.request.setServletPath("/invalid/path.action");
		
		filter.doFilter(request, response, new MockFilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
				Assert.fail();
			}
		});
	}
}
