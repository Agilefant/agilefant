package fi.hut.soberit.agilefant.readonly;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception thrown when there is a problem with read only access
 * 
 * @author jkorri
 */
public class InvalidReadOnlyAccessException extends AuthenticationException {

	private static final long serialVersionUID = 4192566467511165809L;

	public InvalidReadOnlyAccessException(String message) {
		super(message);
	}
}
