package fi.hut.soberit.agilefant.business;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;

import fi.hut.soberit.agilefant.business.SettingBusiness.BranchMetricsType;
import fi.hut.soberit.agilefant.business.impl.SettingBusinessImpl;
import fi.hut.soberit.agilefant.db.SettingDAO;
import fi.hut.soberit.agilefant.model.Setting;

public class SettingBusinessTest {

    private SettingDAO settingDAO;  
    private SettingBusinessImpl testable;
    private Setting setting;
    
    /*
     * An argument matcher for EasyMock. See SettingEquals.java for more information.
     */
    public static Setting eqSetting(Setting in){
        EasyMock.reportMatcher(new SettingEquals(in));
        return null;
    }
    
    @Before
    public void setUp() {
        testable = new SettingBusinessImpl();
        testable.setTransactionManager(new PlatformTransactionManager() {
            @Override
            public void rollback(TransactionStatus status) throws TransactionException {
            }

            @Override
            public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
                return new SimpleTransactionStatus();
            }

            @Override
            public void commit(TransactionStatus status) throws TransactionException {
            }
        });
        settingDAO = createMock(SettingDAO.class);
        testable.setSettingDAO(settingDAO);
        setting = new Setting();
        setting.setName("foo");
    }
    
    @Test
    public void testStoreSetting_int() {
    	expect(settingDAO.getByName("int")).andReturn(null);
        expect(settingDAO.create(EasyMock.isA(Setting.class))).andReturn((Integer)1);
        Setting created = new Setting();
        created.setValue("15");
        expect(settingDAO.getByName("int")).andReturn(created);
        replay(settingDAO);
        testable.storeSetting("int", 15);
        assertEquals("15", testable.retrieveByName("int").getValue());
        verify(settingDAO);
    }
    
    @Test
    public void testStoreSetting_boolean() {
    	expect(settingDAO.getByName("bool")).andReturn(null);
        expect(settingDAO.create(EasyMock.isA(Setting.class))).andReturn((Integer)2);
        Setting created = new Setting();
        created.setValue("true");
        expect(settingDAO.getByName("bool")).andReturn(created);
        replay(settingDAO);
        testable.storeSetting("bool", true);
        assertEquals("true", testable.retrieveByName("bool").getValue());
        verify(settingDAO);
    }
    
    @Test 
    public void testStoreSetting() {
        expect(settingDAO.getByName("foo")).andReturn(setting);
        settingDAO.store(setting);
        replay(settingDAO);
        testable.storeSetting("foo", "new");
        assertEquals("new", setting.getValue());
        verify(settingDAO);
    }
    @Test
    public void testSetHourReporting_SettingExists() {
        Setting setting = new Setting();
        Setting parameterSetting = new Setting();
        
        setting.setName(SettingBusinessImpl.SETTING_NAME_HOUR_REPORTING);
        setting.setValue("false");
        
        parameterSetting.setName(SettingBusinessImpl.SETTING_NAME_HOUR_REPORTING);
        parameterSetting.setValue("true");
        
        expect(settingDAO.getByName(SettingBusinessImpl.SETTING_NAME_HOUR_REPORTING)).andReturn(setting);
        settingDAO.store(eqSetting(parameterSetting));
        replay(settingDAO);
        testable.setHourReporting(true);
        verify(settingDAO);
    }
    
    @Test
    public void testSetHourReporting_SettingDoesNotExist() {
        Setting parameterSetting = new Setting();
    
        parameterSetting.setName(SettingBusinessImpl.SETTING_NAME_HOUR_REPORTING);
        parameterSetting.setValue("true");
        
        expect(settingDAO.getByName(SettingBusinessImpl.SETTING_NAME_HOUR_REPORTING)).andReturn(null);
        expect(settingDAO.create(eqSetting(parameterSetting))).andReturn(1);
        replay(settingDAO);
        testable.setHourReporting(true);
        verify(settingDAO);
    }
    
    @Test
    public void testSetStoryTreeFieldOrder_doesNotExist() {
        Setting parameterSetting = new Setting();
        
        parameterSetting.setName(SettingBusinessImpl.SETTING_NAME_HOUR_REPORTING);
        parameterSetting.setValue("true");
        
        expect(settingDAO.getByName(SettingBusinessImpl.SETTING_NAME_HOUR_REPORTING)).andReturn(null);
        expect(settingDAO.create(eqSetting(parameterSetting))).andReturn(1);
        replay(settingDAO);
        testable.setHourReporting(true);
        verify(settingDAO);
    }
    
    @Test
    public void testSetStoryTreeFieldOrder_exists() {
        Setting previousSetting = new Setting();
        Setting parameterSetting = new Setting();
        
        previousSetting.setName(SettingBusinessImpl.SETTING_NAME_STORY_TREE_FIELD_ORDER);
        previousSetting.setValue("name");
        
        parameterSetting.setName(SettingBusinessImpl.SETTING_NAME_STORY_TREE_FIELD_ORDER);
        parameterSetting.setValue("state,storyPoints,labels,name,backlog");
        
        expect(settingDAO.getByName(SettingBusinessImpl.SETTING_NAME_STORY_TREE_FIELD_ORDER)).andReturn(previousSetting);
        settingDAO.store(eqSetting(parameterSetting));
        
        replay(settingDAO);
        testable.setStoryTreeFieldOrder("state,storyPoints,labels,name,backlog");
        verify(settingDAO);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStoryTreeFieldOrder_incorrectString() {
        testable.setStoryTreeFieldOrder("name,storyPoints,foo");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStoryTreeFieldOrder_emptyString() {
        testable.setStoryTreeFieldOrder("");
    }
    
    @Test
    public void testSetBranchMetricsType_doesNotExist() {
        Setting parameterSetting = new Setting();

        parameterSetting.setName(SettingBusinessImpl.SETTING_NAME_BRANCH_METRICS);
        parameterSetting.setValue("leaf");
        
        expect(settingDAO.getByName(SettingBusinessImpl.SETTING_NAME_BRANCH_METRICS)).andReturn(null);
        expect(settingDAO.create(eqSetting(parameterSetting))).andReturn(1);
        
        replay(settingDAO);
        testable.setBranchMetricsType(BranchMetricsType.leaf);
        verify(settingDAO);
    }
    
    @Test
    public void testSetBranchMetricsType_settingExist() {
        Setting parameterSetting = new Setting();
        Setting setting = new Setting();
        
        setting.setName(SettingBusinessImpl.SETTING_NAME_BRANCH_METRICS);
        setting.setValue(SettingBusiness.BranchMetricsType.estimate.toString());
        
        parameterSetting.setName(SettingBusinessImpl.SETTING_NAME_BRANCH_METRICS);
        parameterSetting.setValue("off");
        
        expect(settingDAO.getByName(SettingBusinessImpl.SETTING_NAME_BRANCH_METRICS)).andReturn(setting);
        settingDAO.store(eqSetting(parameterSetting));
        
        replay(settingDAO);
        testable.setBranchMetricsType(BranchMetricsType.off);
        verify(settingDAO);
    }
    
    @Test
    public void testGetBranchMetricsType() {
        Setting setting = new Setting();
        setting.setName(SettingBusinessImpl.SETTING_NAME_BRANCH_METRICS);
        setting.setValue(SettingBusiness.BranchMetricsType.estimate.toString());
        
        expect(settingDAO.getByName(SettingBusinessImpl.SETTING_NAME_BRANCH_METRICS)).andReturn(setting);
        
        replay(settingDAO);
        assertEquals(BranchMetricsType.estimate, testable.getBranchMetricsType());
        verify(settingDAO);
    }
    
    @Test
    public void testGetBranchMetricsType_noSuchSettings() {
        expect(settingDAO.getByName(SettingBusinessImpl.SETTING_NAME_BRANCH_METRICS)).andReturn(null);
        
        replay(settingDAO);
        assertEquals(SettingBusiness.DEFAULT_BRANCH_METRICS, testable.getBranchMetricsType());
        verify(settingDAO);
    }
}
