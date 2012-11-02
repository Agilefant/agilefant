package fi.hut.soberit.agilefant.web;


import com.opensymphony.xwork2.ActionSupport;
import fi.hut.soberit.agilefant.business.BacklogBusiness;
import fi.hut.soberit.agilefant.business.SettingBusiness;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static fi.hut.soberit.agilefant.security.PreAuthenticatedAgilefantUserDetailsService.EMPTY_EMAIL;
import static fi.hut.soberit.agilefant.security.PreAuthenticatedAgilefantUserDetailsService.EMPTY_TEXT;

@Component("loginContextAction")
@Scope("prototype")
public class LoginContextAction extends ActionSupport {

    private static final long serialVersionUID = -477483113446767662L;

    @Autowired
    private SettingBusiness settingBusiness;
    
    @Autowired
    private BacklogBusiness backlogBusiness;

    @Override
    public String execute(){

        User user = SecurityUtil.getLoggedUser();
        if (isIncomplete(user)) {
            return "edit";
        } else if (backlogBusiness.countAll() == 0)
            return "help";
        else if (settingBusiness.isDailyWork()) {
            return "dailyWork";
        }
        else {
            return "selectBacklog";
        }
    }

    private boolean isIncomplete(User user) {
        return EMPTY_EMAIL.equals(user.getEmail())
                || EMPTY_TEXT.equals(user.getInitials())
                || EMPTY_TEXT.equals(user.getFullName());
    }
}
