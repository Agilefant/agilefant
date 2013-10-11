package fi.hut.soberit.agilefant.business;

import java.util.List;

import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.transfer.MenuDataNode;


/**
 * Business interface for handling functionality related to the lefthand menu.
 * 
 * @author rjokelai
 * 
 */
public interface MenuBusiness {

    public List<MenuDataNode> constructBacklogMenuData();

    List<MenuDataNode> constructMyAssignmentsData(User user);

}
