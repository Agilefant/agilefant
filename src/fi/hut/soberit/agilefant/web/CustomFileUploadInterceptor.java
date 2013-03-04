package fi.hut.soberit.agilefant.web;

import org.apache.struts2.interceptor.FileUploadInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.interceptor.Interceptor;

import fi.hut.soberit.agilefant.business.SettingBusiness;
@Component("customMultipartFormInterceptor")
@Scope("prototype")
public class CustomFileUploadInterceptor extends FileUploadInterceptor implements Interceptor {

    /**
     * 
     */
    private static final long serialVersionUID = -2838989148798419422L;
    @Autowired
    private SettingBusiness settingBusiness;
    
    public CustomFileUploadInterceptor() {
        System.out.println("customMultipartFormInterceptor started");
    }
    
    @Override
    public void setMaximumSize(Long maximumSize) {
        // TODO Auto-generated method stub
        int maxFileSize =  settingBusiness.getMaxSizeForAllAttachment() * 1024 * 1024;
        if (maxFileSize <= 1){
            this.maximumSize = (long) maxFileSize;
        };
    }
    
    public void setSettingBusiness(SettingBusiness settingBusiness) {
        this.settingBusiness = settingBusiness;
    }
    
    
    
    

}
