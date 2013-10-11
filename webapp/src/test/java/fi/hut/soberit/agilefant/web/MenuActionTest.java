package fi.hut.soberit.agilefant.web;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

import fi.hut.soberit.agilefant.business.MenuBusiness;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import fi.hut.soberit.agilefant.transfer.MenuDataNode;

public class MenuActionTest {

    MenuAction menuAction;
    
    MenuBusiness menuBusiness;
    
    @Before
    public void setUp_dependencies() {
        menuAction = new MenuAction();
        
        menuBusiness = createStrictMock(MenuBusiness.class);
        menuAction.setMenuBusiness(menuBusiness);
    }
    
    private void replayAll() {
        replay(menuBusiness);
    }

    private void verifyAll() {
        verify(menuBusiness);
    }
    
    @Test
    public void testConstructBacklogMenuData() {
        expect(menuBusiness.constructBacklogMenuData()).andReturn(
                Arrays.asList(new MenuDataNode()));
        replayAll();
        assertEquals(Action.SUCCESS, menuAction.constructBacklogMenuData());
        verifyAll();
    }
    
    @Test
    public void testConstructAssignmentData() {
        expect(menuBusiness.constructMyAssignmentsData(SecurityUtil.getLoggedUser())).andReturn(Arrays.asList(new MenuDataNode()));
        replayAll();
        assertEquals(Action.SUCCESS, menuAction.constructAssignmentData());
        verifyAll();
    }

}
