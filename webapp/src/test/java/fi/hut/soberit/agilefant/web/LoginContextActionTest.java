package fi.hut.soberit.agilefant.web;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.hut.soberit.agilefant.business.BacklogBusiness;
import fi.hut.soberit.agilefant.business.LoginBusiness;
import fi.hut.soberit.agilefant.business.SettingBusiness;
import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.model.Login;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.test.Mock;
import fi.hut.soberit.agilefant.test.MockContextLoader;
import fi.hut.soberit.agilefant.test.MockedTestCase;
import fi.hut.soberit.agilefant.test.TestedBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = MockContextLoader.class)
public class LoginContextActionTest extends MockedTestCase {

    @TestedBean
    private LoginContextAction testable;
    
    @Mock
    private SettingBusiness settingBusiness;
    
    @Mock
    private BacklogBusiness backlogBusiness;
    
    @Mock
    private UserBusiness userBusiness;
    
    @Mock
    private LoginBusiness loginBusiness;
    
    
    @Test
    @DirtiesContext
    public void testLoginContext_dailyWorkEnabled() {
        expect(loginBusiness.retrieveLoginCountByUser(EasyMock.anyObject(User.class))).andReturn(10);
        expect(settingBusiness.isDailyWork()).andReturn(true);
        loginBusiness.store(anyObject(Login.class));
        replayAll();
        assertEquals("dailyWork", testable.execute());
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testLoginContext_dailyWorkDisabled() {
        expect(loginBusiness.retrieveLoginCountByUser(EasyMock.anyObject(User.class))).andReturn(10);
        expect(settingBusiness.isDailyWork()).andReturn(false);
        loginBusiness.store(anyObject(Login.class));
        replayAll();
        assertEquals("selectBacklog", testable.execute());
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testLoginContext_noBacklogs_dailyWorkDisabled() {
        expect(loginBusiness.retrieveLoginCountByUser(EasyMock.anyObject(User.class))).andReturn(0);
        loginBusiness.store(anyObject(Login.class));
        replayAll();
        assertEquals("help", testable.execute());
        verifyAll();
    }
    
    @Test
    @DirtiesContext
    public void testLoginContext_noBacklogs_dailyWorkEnabled() {
        expect(loginBusiness.retrieveLoginCountByUser(EasyMock.anyObject(User.class))).andReturn(0);
        loginBusiness.store(anyObject(Login.class));
        replayAll();
        assertEquals("help", testable.execute());
        verifyAll();
    }
}
