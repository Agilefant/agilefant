package fi.hut.soberit.agilefant.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.business.MenuBusiness;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import fi.hut.soberit.agilefant.transfer.MenuDataNode;

/**
 * The action class for populating the lefthand menu.
 * 
 * @author rjokelai
 */
@Component("menuAction")
@Scope("prototype")
public class MenuAction extends ActionSupport {

    private static final long serialVersionUID = -4817943410890249969L;

    @Autowired
    private MenuBusiness menuBusiness;
    
    private List<MenuDataNode> menuData;
    private List<MenuDataNode> assignmentData;    
    
    public String constructBacklogMenuData() {
        menuData = menuBusiness.constructBacklogMenuData();
        return Action.SUCCESS;
    }
    
    public String constructAssignmentData() {
        assignmentData = menuBusiness.constructMyAssignmentsData(SecurityUtil.getLoggedUser());
        return Action.SUCCESS;
    }

    public List<MenuDataNode> getMenuData() {
        return menuData;
    }

    public void setMenuBusiness(MenuBusiness menuBusiness) {
        this.menuBusiness = menuBusiness;
    }
    
    public List<MenuDataNode> getAssignmentData() {
        return assignmentData;
    }
    
}
