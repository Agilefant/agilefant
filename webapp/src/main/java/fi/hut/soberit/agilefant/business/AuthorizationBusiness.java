package fi.hut.soberit.agilefant.business;

import fi.hut.soberit.agilefant.model.User;

public interface AuthorizationBusiness {

	public boolean isBacklogAccessible(int backlogId, User user);
}
