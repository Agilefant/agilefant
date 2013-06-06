package fi.hut.soberit.agilefant.business;

import java.util.Collection;
import java.util.Set;

import fi.hut.soberit.agilefant.model.Team;
import fi.hut.soberit.agilefant.model.User;

/**
 * Interface for team business.
 * 
 * @author hhaataja
 * 
 */
public interface TeamBusiness extends GenericBusiness<Team> {

    /**
     * Store or create a team.
     */
    Team storeTeam(Team team, Set<Integer> userIds, Set<Integer> productIds, Set<Integer> iterationIds);
    
    Set<User> getUsersInSameTeams(int userId);

    public Collection<Team> withUsers(Call<Collection<Team>> call);

    Team getByTeamName(String teamName);
    
	/**
	 * Interface to specify a method call
	 * 
	 * @param <T> return type for the call
	 */
	interface Call<T> {
		public T call();
	}    
    
}
