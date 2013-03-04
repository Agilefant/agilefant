package fi.hut.soberit.agilefant.business.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.SettingBusiness;
import fi.hut.soberit.agilefant.db.SettingDAO;
import fi.hut.soberit.agilefant.model.Setting;

/**
 * Business implementation for handling of settings
 * 
 * @author kjniiran
 * @author Pasi Pekkanen
 * 
 */
@Service("settingBusiness")
@Transactional
@Scope(value="singleton")
public class SettingBusinessImpl extends GenericBusinessImpl<Setting> implements
        SettingBusiness {
    
    private static final String CONFIG_LOCATION = "configuration.properties";
    
    private static boolean propertyLoaded = false;
    
    //setting keys
    
    public static final String SETTING_NAME_HOUR_REPORTING = "HourReporting";
    public static final String SETTING_NAME_DAILY_WORK = "DailyWork";
    public static final String SETTING_NAME_TIME_SHEET = "TimeSheet";
    public static final String SETTING_NAME_DEV_PORTFOLIO = "DevPortfolio";
    public static final String SETTING_NAME_RANGE_LOW = "RangeLow";
    public static final String SETTING_NAME_RANGE_HIGH = "RangeHigh";
    public static final String SETTING_NAME_OPTIMAL_LOW = "OptimalLow";
    public static final String SETTING_NAME_OPTIMAL_HIGH = "OptimalHigh";
    public static final String SETTING_NAME_CRITICAL_LOW = "CriticalLow";
    public static final String SETTING_NAME_PORTFOLIO_TIME_SPAN = "PortfolioTimeSpan";
    public static final String SETTING_NAME_STORY_TREE_FIELD_ORDER = "StoryTreeFieldOrder";
    public static final String SETTING_NAME_BRANCH_METRICS = "branchMetricsType";
    public static final String SETTING_NAME_LABELS_IN_STORY_LIST = "labelsInStoryList";
    public static final String SETTING_NAME_WEEKENDS_IN_BURNDOWN = "weekendsInBurndown";
    public static final String SETTING_NAME_ATTACHMENT_SAVE_LOCATION = "AttachmentLocation";
    public static final String SETTING_NAME_MAX_NUMBER_OF_ATTACHMENT = "MaxNumberOfAttachment";
    public static final String SETTING_NAME_MAX_SIZE_OF_ALL_ATTACHMENT = "MaxSizeOfAllAttachment";
    
    private static Properties attachmentProperties;
    
    public SettingBusinessImpl() {
        super(Setting.class);
    }

    @Autowired
    private SettingDAO settingDAO;
    private Map<String,Setting> settingCache = new HashMap<String, Setting>();

    
    public void setSettingDAO(SettingDAO settingDAO) {
        this.genericDAO = settingDAO;
        this.settingDAO = settingDAO;
    }
    
    @PostConstruct
    public void loadSettingCache() {
        this.settingCache.clear();
        Collection<Setting> allSettings = this.settingDAO.getAll();
        for(Setting setting : allSettings) {
            this.settingCache.put(setting.getName(), setting);
        }
        loadProperties();
    }

    @Transactional(readOnly = true)
    public Setting retrieveByName(String name) {
        return this.settingCache.get(name);
    }
    
    public void storeSetting(String settingName, boolean value) {
        this.storeSetting(settingName, ((Boolean)value).toString());
    }
    
    public void storeSetting(String settingName, int value) {
        this.storeSetting(settingName, ((Integer)value).toString());
    }
    
    public synchronized void storeSetting(String settingName, String value) {
        Setting setting = this.retrieveByName(settingName);
        if (setting == null) {
            setting = new Setting();
            setting.setName(settingName);
            setting.setValue(value);
            this.settingDAO.create(setting);
        } else {
            setting.setValue(value);
            this.settingDAO.store(setting);
        } 
        this.settingCache.put(settingName, setting);
    }
    
    @Transactional(readOnly = true)
    public boolean isHourReportingEnabled() {
        Setting setting = this.retrieveByName(SETTING_NAME_HOUR_REPORTING);

        if (setting == null) {
            return false;
        }

        return setting.getValue().equals("true");
    }

    @Transactional
    public void setHourReporting(boolean mode) {
        this.storeSetting(SETTING_NAME_HOUR_REPORTING, mode);

    }
    
    @Transactional(readOnly = true)
    public boolean isDailyWork() {
        Setting setting = this.retrieveByName(SETTING_NAME_DAILY_WORK);

        if (setting == null) {
            return false;
        }

        return setting.getValue().equals("true");
    }

    @Transactional
    public void setDailyWork(boolean mode) {
        this.storeSetting(SETTING_NAME_DAILY_WORK, mode);
    }
    
    @Transactional(readOnly = true)
    public boolean isTimeSheet() {
        Setting setting = this.retrieveByName(SETTING_NAME_TIME_SHEET);

        if (setting == null) {
            return false;
        }

        return setting.getValue().equals("true");
    }
    
    @Transactional
    public void setTimeSheet(boolean mode) {
        this.storeSetting(SETTING_NAME_TIME_SHEET, mode);
    }
    
    @Transactional(readOnly = true)
    public boolean isDevPortfolio() {
        Setting setting = this.retrieveByName(SETTING_NAME_DEV_PORTFOLIO);

        if (setting == null) {
            return false;
        }

        return setting.getValue().equals("true");
    }

    @Transactional
    public void setDevPortfolio(boolean mode) {
        this.storeSetting(SETTING_NAME_DEV_PORTFOLIO, mode);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void setRangeLow(Integer value) {
        if(value == null) {
            this.storeSetting(SETTING_NAME_RANGE_LOW, DEFAULT_RANGE_LOW);
        } else {
            this.storeSetting(SETTING_NAME_RANGE_LOW, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public int getRangeLow() {
        Setting setting = this.retrieveByName(SETTING_NAME_RANGE_LOW);

        if (setting == null) {
            return DEFAULT_RANGE_LOW;
        } else {
            return Integer.parseInt(setting.getValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void setRangeHigh(Integer value) {
        if(value == null) {
            this.storeSetting(SETTING_NAME_RANGE_HIGH, DEFAULT_RANGE_HIGH);
        } else {
            this.storeSetting(SETTING_NAME_RANGE_HIGH, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public int getRangeHigh() {
        Setting setting = this.retrieveByName(SETTING_NAME_RANGE_HIGH);

        if (setting == null) {
            return DEFAULT_RANGE_HIGH;
        }
        return Integer.parseInt(setting.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void setOptimalLow(Integer value) {
        if(value == null) {
            this.storeSetting(SETTING_NAME_OPTIMAL_LOW, DEFAULT_OPTIMAL_LOW);
        } else {
            this.storeSetting(SETTING_NAME_OPTIMAL_LOW, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public int getOptimalLow() {
        Setting setting = this.retrieveByName(SETTING_NAME_OPTIMAL_LOW);

        if (setting == null) {
            return DEFAULT_OPTIMAL_LOW;
        }
        return Integer.parseInt(setting.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void setOptimalHigh(Integer value) {
        if(value == null) {
            this.storeSetting(SETTING_NAME_OPTIMAL_HIGH, DEFAULT_OPTIMAL_HIGH);
        } else {
            this.storeSetting(SETTING_NAME_OPTIMAL_HIGH, value);            
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public int getOptimalHigh() {
        Setting setting = this.retrieveByName(SETTING_NAME_OPTIMAL_HIGH);

        if (setting == null) {
            return DEFAULT_OPTIMAL_HIGH;
        }
        return Integer.parseInt(setting.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void setCriticalLow(Integer value) {
        if(value != null) {
            this.storeSetting(SETTING_NAME_CRITICAL_LOW, value);
        } else {
            this.storeSetting(SETTING_NAME_CRITICAL_LOW, DEFAULT_CRITICAL_LOW);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public int getCriticalLow() {
        Setting setting = this.retrieveByName(SETTING_NAME_CRITICAL_LOW);

        if (setting == null) {
            return DEFAULT_CRITICAL_LOW;
        }
        return Integer.parseInt(setting.getValue());
    }
    
    
    @Transactional(readOnly = true)
    public Period getPortfolioTimeSpan() {
        Setting setting = this.retrieveByName(SETTING_NAME_PORTFOLIO_TIME_SPAN);
        
        if(setting == null) {
            return DEFAULT_PORTFOLIO_TIME_SPAN; 
        }
        return Period.months(Integer.parseInt(setting.getValue()));
        
    }
    
    @Transactional(readOnly = true)
    public void setPortfolioTimeSpan(Period timeSpan) {
        if( timeSpan == null) {
            this.storeSetting(SETTING_NAME_PORTFOLIO_TIME_SPAN, Integer.toString(DEFAULT_PORTFOLIO_TIME_SPAN.getMonths()));
        } else {
            this.storeSetting(SETTING_NAME_PORTFOLIO_TIME_SPAN, Integer.toString(timeSpan.getMonths()));
        }
    }

    @Transactional(readOnly = true)
    public String getStoryTreeFieldOrder() {
        Setting setting = this.retrieveByName(SETTING_NAME_STORY_TREE_FIELD_ORDER);
        if (setting == null) {
            return DEFAULT_STORY_TREE_FIELD_ORDER;
        }
        return setting.getValue();
    }
    
    public void setStoryTreeFieldOrder(String newOrder) {
        checkFieldOrderString(newOrder);
        if (newOrder == null) {
            this.storeSetting(SETTING_NAME_STORY_TREE_FIELD_ORDER, DEFAULT_STORY_TREE_FIELD_ORDER);
        } else {
            this.storeSetting(SETTING_NAME_STORY_TREE_FIELD_ORDER, newOrder);
        }
    }
    
    private void checkFieldOrderString(String order) {
        Collection<String> permitted = Collections.unmodifiableCollection(Arrays.asList("state","storyPoints","labels","name","backlog","breadcrumb"));
        String[] names = order.split(",");
        for (String name : names) {
            if (name.equals("") || !permitted.contains(name)) {
                throw new IllegalArgumentException("Incorrect setting string for story tree field order");
            }
        }
    }
    
    public void setBranchMetricsType(BranchMetricsType type) {
        if(type == null) {
            this.storeSetting(SETTING_NAME_BRANCH_METRICS, DEFAULT_BRANCH_METRICS.toString());
        } else {
            this.storeSetting(SETTING_NAME_BRANCH_METRICS, type.toString());
        }
    }
    
    @Transactional(readOnly = true)
    public BranchMetricsType getBranchMetricsType() {
        Setting setting = this.retrieveByName(SETTING_NAME_BRANCH_METRICS);
        if (setting == null) {
            return DEFAULT_BRANCH_METRICS;
        }
        return BranchMetricsType.valueOf(setting.getValue());
    }
    
    
    public void setLabelsInStoryList(boolean mode) {
        this.storeSetting(SETTING_NAME_LABELS_IN_STORY_LIST, mode);
    }
    
    @Transactional(readOnly = true)
    public boolean isLabelsInStoryList() {
        Setting setting = this.retrieveByName(SETTING_NAME_LABELS_IN_STORY_LIST);

        if (setting == null) {
            return true;
        }

        return setting.getValue().equals("true");
    }
    
    public void setWeekendsInBurndown(boolean mode) {
        this.storeSetting(SETTING_NAME_WEEKENDS_IN_BURNDOWN, mode);
    }
    
    @Transactional(readOnly = true)
    public boolean isWeekendsInBurndown() {
        Setting setting = this.retrieveByName(SETTING_NAME_WEEKENDS_IN_BURNDOWN);
        if (setting == null) {
            return true;
        }
        
        return setting.getValue().equals("true");
    }
/**
 * @author rahul
 * @category Settings 
 * @Creted for Comment attachment configuration
 *  * 
 */
    @Override
    public boolean setAttachmentSaveLocation(String location) {
        boolean attachmentSaveLocationConfiguration = false;
        if(location != null && !location.isEmpty()){
            try {
                attachmentSaveLocationConfiguration = createRequiredDirectory(location);
            } catch (Exception e) {
                System.out.println("Unable to create required directories , check permision of directory selected");     
                attachmentSaveLocationConfiguration = false;
            }
            if(attachmentSaveLocationConfiguration){
                this.storeSetting(SETTING_NAME_ATTACHMENT_SAVE_LOCATION, location);
            }else{
                attachmentSaveLocationConfiguration = false;
            }
        }
        return attachmentSaveLocationConfiguration;
    }

    @Transactional(readOnly = true)
    public String getAttachmentSaveLocation() {
        Setting settings = this.retrieveByName(SETTING_NAME_ATTACHMENT_SAVE_LOCATION);
        if(settings == null){
            return "Not configured";
        }
        return settings.getValue();
    }

    @Override
    public void setMaxNumberOfAttachment(int noOfAttachment) {
        this.storeSetting(SETTING_NAME_MAX_NUMBER_OF_ATTACHMENT, noOfAttachment);        
    }

    @Transactional(readOnly = true)
    public int getMaxNumberOfAttachment() {
        Setting settings = this.retrieveByName(SETTING_NAME_MAX_NUMBER_OF_ATTACHMENT);
        if (settings == null ){
            return 0;
        }
        return Integer.parseInt(settings.getValue());
    }

    @Override
    public void setMaxSizeForAllAttachment(int maxSize) {
        this.storeSetting(SETTING_NAME_MAX_SIZE_OF_ALL_ATTACHMENT, maxSize);
        
    }

    @Transactional(readOnly = true)
    public int getMaxSizeForAllAttachment() {
        Setting settings = this.retrieveByName(SETTING_NAME_MAX_SIZE_OF_ALL_ATTACHMENT);
        if (settings == null ){
            return 0;
        }
        return Integer.parseInt(settings.getValue());
    }

    private boolean attachmentProperyLoaded(){
        return propertyLoaded;        
    }
    
    private void loadProperties(){
        Setting setting = this.retrieveByName(SETTING_NAME_ATTACHMENT_SAVE_LOCATION);
        if(setting == null || setting.getValue() == null || setting.getValue().isEmpty()){
                InputStream is = getClass().getResourceAsStream("/WEB-INF/"+CONFIG_LOCATION);
                final String seperator = System.getProperty("file.separator");
                final String FILE_SAVE_LOCATION = System.getProperty("user.home")+seperator+"agilefant"+seperator+"user_uploads"+seperator;
                final int MAX_FILE_SIZE = 20;
                final int MAX_NUM_FILES = 5;
                
               attachmentProperties = new Properties();
               try {
                    attachmentProperties.load(is);            
                    setAttachmentSaveLocation(attachmentProperties.getProperty("attachment.save.location"));
                    setMaxNumberOfAttachment(Integer.parseInt(attachmentProperties.getProperty("attachment.max.files.count")));
                    setMaxSizeForAllAttachment(Integer.parseInt(attachmentProperties.getProperty("attachment.files.max.size"))); 
                    System.out.println("Attachment properties loaded from "+CONFIG_LOCATION+ " file");
                    propertyLoaded = true;
                } catch (Exception e) {
                    setAttachmentSaveLocation(FILE_SAVE_LOCATION);
                    setMaxNumberOfAttachment(MAX_NUM_FILES);
                    setMaxSizeForAllAttachment(MAX_FILE_SIZE);
                    System.out.println("Default properties loaded");
                    propertyLoaded = true;
                    /*            
                    attachmentProperties.put("attachment.save.location", FILE_SAVE_LOCATION);
                    attachmentProperties.put("attachment.files.max.size", MAX_FILE_SIZE);
                    attachmentProperties.put("attachment.max.files.count", MAX_NUM_FILES);*/
                }
        }
    }
    
    public boolean createRequiredDirectory(String location) throws Exception{
        if(location != null && !location.isEmpty()){
            File parentDirectory = new File(location);
            try{
                if(parentDirectory.exists()){
                    // Parent directory exists make required child directories
                    if(!childDirectoyCreation(location)){
                        System.out.println("Failed to create child directories make sure read write permission are give to the parent directory");
                        return false;
                    }else{
                        System.out.println("Child directories created successfully");
                        return true;
                    }
                }else{
                    // Parent directory not exists trying to create parent directory
                   if( parentDirectory.mkdirs() ){
                       if(!childDirectoyCreation(location)){
                           System.out.println("Failed to create child directories make sure read write permission are give to the parent directory");
                           return false;
                       }else{
                           System.out.println("Child directories created successfully");
                           return true;
                       }
                   }else{
                       return false;                       
                   }
                }
            }catch (Exception e) {
                
            }
        }else{
            return false;
        }
        return false;
    }
    
    public boolean childDirectoyCreation(String parentLocation){
        try{
            // Creating child directories
            boolean childDirectoryStatus = false;
            parentLocation += parentLocation.endsWith(System.getProperty("file.separator")) ? "" : System.getProperty("file.separator");
            File storyCommentDir = new File(parentLocation.trim()+"story");
            File taskCommentDir = new File(parentLocation.trim()+"task");
            if(!storyCommentDir.exists()){
                childDirectoryStatus = storyCommentDir.mkdir();
                System.out.println("Directory created : "+childDirectoryStatus);
            }else{
                childDirectoryStatus = storyCommentDir.canWrite();
                System.out.println("Directory created : "+childDirectoryStatus);
            }
            
            if(!taskCommentDir.exists()){
                childDirectoryStatus = taskCommentDir.mkdir();
                System.out.println("Directory created : "+childDirectoryStatus);
            }else{
                childDirectoryStatus = taskCommentDir.canWrite();
                System.out.println("Directory created : "+childDirectoryStatus);
            }
            return childDirectoryStatus;
        }catch (Exception ioe) {
            System.out.println("Unhandled exception while creating child directories");
            return false;
        }
    }
    
}
