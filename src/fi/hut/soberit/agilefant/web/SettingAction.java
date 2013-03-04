package fi.hut.soberit.agilefant.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.business.SettingBusiness;

@Component("settingAction")
@Scope("prototype")
public class SettingAction extends ActionSupport {
    
    private static final long serialVersionUID = 6404856922329136680L;
    private int rangeLow = -1;
    private int rangeHigh = -1;
    private int optimalLow = -1;
    private int optimalHigh = -1;
    private int criticalLow = -1;
    private boolean hourReportingEnabled = false;
    private boolean dailyWorkEnabled = false;
    private boolean devPortfolioEnabled = false;
    private boolean labelsInStoryList = false;
    private boolean weekendsInBurndown = false;
    private String storyTreeFieldOrder;
    private SettingBusiness.BranchMetricsType branchMetricsType;
    
    // File attachment settings configurations
    private String locationForSavingAttachment;
    private int maxSizeOfAllAttachments = 0;
    private int maxNumberOfAttachment = 0;
        
    @Autowired
    private SettingBusiness settingBusiness;
    
    public String edit()  {        
        this.criticalLow = this.settingBusiness.getCriticalLow();
        this.optimalHigh = this.settingBusiness.getOptimalHigh();
        this.optimalLow = this.settingBusiness.getOptimalLow();
        this.rangeHigh = this.settingBusiness.getRangeHigh();
        this.rangeLow = this.settingBusiness.getRangeLow();
        this.hourReportingEnabled = this.settingBusiness.isHourReportingEnabled();
        this.devPortfolioEnabled = this.settingBusiness.isDevPortfolio();
        this.dailyWorkEnabled = this.settingBusiness.isDailyWork();
        this.storyTreeFieldOrder = this.settingBusiness.getStoryTreeFieldOrder();
        this.branchMetricsType = this.settingBusiness.getBranchMetricsType();
        this.labelsInStoryList = this.settingBusiness.isLabelsInStoryList();
        this.weekendsInBurndown = this.settingBusiness.isWeekendsInBurndown();
        this.locationForSavingAttachment = this.settingBusiness.getAttachmentSaveLocation();
        this.maxSizeOfAllAttachments = this.settingBusiness.getMaxSizeForAllAttachment();
        this.maxNumberOfAttachment = this.settingBusiness.getMaxNumberOfAttachment();
        return Action.SUCCESS;
    }
    
    public String storeSettings() {
        this.initilizeEmptyLoadMeterValues();
        if (!this.validateLoadMeterValues()) {
            return Action.ERROR;
        }
        settingBusiness.setRangeLow(rangeLow);
        settingBusiness.setRangeHigh(rangeHigh);
        settingBusiness.setOptimalLow(optimalLow);
        settingBusiness.setOptimalHigh(optimalHigh);
        settingBusiness.setCriticalLow(criticalLow);
        settingBusiness.setHourReporting(hourReportingEnabled);
        settingBusiness.setDevPortfolio(devPortfolioEnabled);
        settingBusiness.setDailyWork(dailyWorkEnabled);
        settingBusiness.setStoryTreeFieldOrder(storyTreeFieldOrder);
        settingBusiness.setBranchMetricsType(branchMetricsType);
        settingBusiness.setLabelsInStoryList(labelsInStoryList);
        settingBusiness.setWeekendsInBurndown(weekendsInBurndown);
        if(!validateAttachmentSettings()){
            return Action.ERROR;
        }
        if(!settingBusiness.setAttachmentSaveLocation(locationForSavingAttachment)){
           this.addActionError("Error while setting attachment location , Verify location and make sure directory has read write permission"); 
           return Action.ERROR;
        }
        settingBusiness.setMaxSizeForAllAttachment(maxSizeOfAllAttachments);
        settingBusiness.setMaxNumberOfAttachment(maxNumberOfAttachment);
        return Action.SUCCESS;
    }
    
    public void initilizeEmptyLoadMeterValues() {
        if(rangeLow < 0) {
            rangeLow = SettingBusiness.DEFAULT_RANGE_LOW;
        }
        if(rangeHigh < 0) {
            rangeHigh = SettingBusiness.DEFAULT_RANGE_HIGH;
        }
        if(optimalLow < 0) {
            optimalLow = SettingBusiness.DEFAULT_OPTIMAL_LOW;
        }
        if(optimalHigh < 0) {
            optimalHigh = SettingBusiness.DEFAULT_OPTIMAL_HIGH;
        }
        if(criticalLow < 0) {
            criticalLow = SettingBusiness.DEFAULT_CRITICAL_LOW;
        }
    }
    
    public boolean validateAttachmentSettings(){
        boolean vaildation = true;
        if(maxNumberOfAttachment <= 0 ){
            this.addActionError(this.getText("setting.comment.attachment.maxFileSize"));
            vaildation = false;
        }
        if(maxSizeOfAllAttachments <= 0){
            this.addActionError(this.getText("setting.comment.attachment.numberOfAttachments"));
            vaildation = false;
        }
        if(locationForSavingAttachment == null || locationForSavingAttachment.isEmpty()){
            this.addActionError("Location Cannot be empty");
            vaildation = false;
        }
        return vaildation;
    }
    
    public boolean validateLoadMeterValues() {
        if(optimalLow <= rangeLow) {
            this.addActionError(this.getText("settings.rangeLowErr"));
            return false;
        }
        if(optimalHigh <= optimalLow) {
            this.addActionError(this.getText("settings.optimalLowErr"));
            return false;
        }
        if(criticalLow <= optimalHigh) {
            this.addActionError(this.getText("settings.optimalHighErr"));
            return false;
        }
        if(rangeHigh <= criticalLow) {
            this.addActionError(this.getText("settings.criticalErr"));
            return false;
        }
        return true;
    }

    public int getRangeLow() {
        return rangeLow;
    }

    public void setRangeLow(int rangeLow) {
        this.rangeLow = rangeLow;
    }

    public int getRangeHigh() {
        return rangeHigh;
    }

    public void setRangeHigh(int rangeHigh) {
        this.rangeHigh = rangeHigh;
    }

    public int getOptimalLow() {
        return optimalLow;
    }

    public void setOptimalLow(int optimalLow) {
        this.optimalLow = optimalLow;
    }

    public int getOptimalHigh() {
        return optimalHigh;
    }

    public void setOptimalHigh(int optimalHigh) {
        this.optimalHigh = optimalHigh;
    }

    public int getCriticalLow() {
        return criticalLow;
    }

    public void setCriticalLow(int criticalLow) {
        this.criticalLow = criticalLow;
    }

    public boolean isHourReportingEnabled() {
        return hourReportingEnabled;
    }

    public void setHourReportingEnabled(boolean hourReportingEnabled) {
        this.hourReportingEnabled = hourReportingEnabled;
    }

    public boolean isDailyWorkEnabled() {
        return dailyWorkEnabled;
    }

    public void setDailyWorkEnabled(boolean dailyWorkEnabled) {
        this.dailyWorkEnabled = dailyWorkEnabled;
    }

    public boolean isDevPortfolioEnabled() {
        return devPortfolioEnabled;
    }

    public void setDevPortfolioEnabled(boolean devPortfolioEnabled) {
        this.devPortfolioEnabled = devPortfolioEnabled;
    }

    public void setSettingBusiness(SettingBusiness settingBusiness) {
        this.settingBusiness = settingBusiness;
    }

    public String getStoryTreeFieldOrder() {
        return storyTreeFieldOrder;
    }

    public void setStoryTreeFieldOrder(String storyTreeFieldOrder) {
        this.storyTreeFieldOrder = storyTreeFieldOrder;
    }

    public SettingBusiness.BranchMetricsType getBranchMetricsType() {
        return branchMetricsType;
    }

    public void setBranchMetricsType(
            SettingBusiness.BranchMetricsType branchMetricsType) {
        this.branchMetricsType = branchMetricsType;
    }

    public boolean isLabelsInStoryList() {
        return labelsInStoryList;
    }

    public void setLabelsInStoryList(boolean labelsInStoryList) {
        this.labelsInStoryList = labelsInStoryList;
    }

    public boolean isWeekendsInBurndown() {
        return weekendsInBurndown;
    }

    public void setWeekendsInBurndown(boolean weekendsInBurndown) {
        this.weekendsInBurndown = weekendsInBurndown;
    }

    public String getLocationForSavingAttachment() {
        return locationForSavingAttachment;
    }

    public void setLocationForSavingAttachment(String locationForSavingAttachment) {
        this.locationForSavingAttachment = locationForSavingAttachment;
    }

    public int getMaxSizeOfAllAttachments() {
        return maxSizeOfAllAttachments;
    }

    public void setMaxSizeOfAllAttachments(int maxSizeOfAllAttachments) {
        this.maxSizeOfAllAttachments = maxSizeOfAllAttachments;
    }

    public int getMaxNumberOfAttachment() {
        return maxNumberOfAttachment;
    }

    public void setMaxNumberOfAttachment(int maxNumberOfAttachment) {
        this.maxNumberOfAttachment = maxNumberOfAttachment;
    }
    
    
    
}
