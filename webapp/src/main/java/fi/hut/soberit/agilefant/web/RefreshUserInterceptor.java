package fi.hut.soberit.agilefant.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import flexjson.JSONSerializer;

/**
 * Interceptor, which ensures proper user-id is set during each request. Ie.
 * makes getLoggedUser - calls valid for struts stuff.
 */
@Component("refreshUserInterceptor")
@Scope("prototype")
public class RefreshUserInterceptor implements Interceptor {

    private static final long serialVersionUID = 1668784370092320107L;

    @Autowired
    private UserBusiness userBusiness;

    public void destroy() {
    }

    public void init() {
    }
    
    public String intercept(ActionInvocation invocation) throws Exception {
        User user = getLoggedInUser();

        //push current user to the value stack
        invocation.getStack().set("currentUser", user);
        invocation.getStack().set("currentUserJson", new JSONSerializer().serialize(user));

        return invocation.invoke();
    }
    
    private User getLoggedInUser() {
        int userId = SecurityUtil.getLoggedUserId();
        return userBusiness.retrieve(userId);
    }
    
    public void setUserBusiness(UserBusiness userBusiness) {
        this.userBusiness = userBusiness;
    }

}
