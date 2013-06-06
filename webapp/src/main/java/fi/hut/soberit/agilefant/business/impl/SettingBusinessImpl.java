package fi.hut.soberit.agilefant.business.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

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
    
    public SettingBusinessImpl() {
        super(Setting.class);
    }

    @Autowired
    private SettingDAO settingDAO;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public void setSettingDAO(SettingDAO settingDAO) {
        this.genericDAO = settingDAO;
        this.settingDAO = settingDAO;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Transactional(readOnly = true)
    public Setting retrieveByName(String name) {
        return this.settingDAO.getByName(name);
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
    }
    
    @Transactional
    public void setValue(String settingName, String value) {
        this.storeSetting(settingName, value);
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
    
}
