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
    public void testLoadSettingCache() {
        Setting setting2 = new Setting();
        setting2.setName("faa");
        Collection<Setting> allSettings = Arrays.asList(setting, setting2);
        expect(settingDAO.getAll()).andReturn(allSettings);
        replay(settingDAO);
        testable.loadSettingCache();
        assertNotNull(testable.retrieveByName("foo"));
        assertNotNull(testable.retrieveByName("faa"));
        verify(settingDAO);
    }
    
    @Test
    public void testStoreSetting_int() {
        expect(settingDAO.create(EasyMock.isA(Setting.class))).andReturn((Integer)1);
        replay(settingDAO);
        testable.storeSetting("int", 15);
        assertEquals("15", testable.retrieveByName("int").getValue());
        verify(settingDAO);
    }
    
    @Test
    public void testStoreSetting_boolean() {
        expect(settingDAO.create(EasyMock.isA(Setting.class))).andReturn((Integer)2);
        replay(settingDAO);
        testable.storeSetting("bool", true);
        assertEquals("true", testable.retrieveByName("bool").getValue());
        verify(settingDAO);
    }
    
    @Test 
    public void testStoreSetting() {
        settingDAO.store(setting);
        expect(settingDAO.getAll()).andReturn(Arrays.asList(setting));
        replay(settingDAO);
        testable.loadSettingCache();
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
        
        expect(settingDAO.getAll()).andReturn(Arrays.asList(setting));
        settingDAO.store(eqSetting(parameterSetting));
        replay(settingDAO);
        testable.loadSettingCache();
        testable.setHourReporting(true);
        verify(settingDAO);
    }
    
    @Test
    public void testSetHourReporting_SettingDoesNotExist() {
        Setting parameterSetting = new Setting();
    
        parameterSetting.setName(SettingBusinessImpl.SETTING_NAME_HOUR_REPORTING);
        parameterSetting.setValue("true");
        
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
        
        expect(settingDAO.getAll()).andReturn(new ArrayList<Setting>());
        expect(settingDAO.create(eqSetting(parameterSetting))).andReturn(1);
        replay(settingDAO);
        testable.loadSettingCache();
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
        
        expect(settingDAO.getAll()).andReturn(Arrays.asList(previousSetting));
        settingDAO.store(eqSetting(parameterSetting));
        
        replay(settingDAO);
        testable.loadSettingCache();
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
        
        expect(settingDAO.getAll()).andReturn(new ArrayList<Setting>());
        expect(settingDAO.create(eqSetting(parameterSetting))).andReturn(1);
        
        replay(settingDAO);
        testable.loadSettingCache();
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
        
        expect(settingDAO.getAll()).andReturn(Arrays.asList(setting));
        settingDAO.store(eqSetting(parameterSetting));
        
        replay(settingDAO);
        testable.loadSettingCache();
        testable.setBranchMetricsType(BranchMetricsType.off);
        verify(settingDAO);
    }
    
    @Test
    public void testGetBranchMetricsType() {
        Setting setting = new Setting();
        setting.setName(SettingBusinessImpl.SETTING_NAME_BRANCH_METRICS);
        setting.setValue(SettingBusiness.BranchMetricsType.estimate.toString());
        
        expect(settingDAO.getAll()).andReturn(Arrays.asList(setting));
        
        replay(settingDAO);
        testable.loadSettingCache();
        assertEquals(BranchMetricsType.estimate, testable.getBranchMetricsType());
        verify(settingDAO);
    }
    
    @Test
    public void testGetBranchMetricsType_noSuchSettings() {
        expect(settingDAO.getAll()).andReturn(new ArrayList<Setting>());
        
        replay(settingDAO);
        testable.loadSettingCache();
        assertEquals(SettingBusiness.DEFAULT_BRANCH_METRICS, testable.getBranchMetricsType());
        verify(settingDAO);
    }
}
