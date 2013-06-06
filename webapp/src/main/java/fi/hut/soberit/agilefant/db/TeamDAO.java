package fi.hut.soberit.agilefant.db;

import fi.hut.soberit.agilefant.model.Team;

/**
 * Interface for a DAO of a Team.
 * 
 * @see GenericDAO
 */
public interface TeamDAO extends GenericDAO<Team> {
	
    /**
     * Get the team by team name.
     * 
     * Is case-sensitive.
     */
	Team getByTeamName(String teamName);

}
