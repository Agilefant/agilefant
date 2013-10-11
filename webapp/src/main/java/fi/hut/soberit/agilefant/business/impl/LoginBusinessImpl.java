package fi.hut.soberit.agilefant.business.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.LoginBusiness;
import fi.hut.soberit.agilefant.db.LoginDAO;
import fi.hut.soberit.agilefant.model.Login;
import fi.hut.soberit.agilefant.model.User;

@Service("loginBusiness")
@Transactional
//@Scope(value="singleton")
public class LoginBusinessImpl extends GenericBusinessImpl<Login> implements
        LoginBusiness {
    
    @Autowired
    private LoginDAO loginDAO;
    
    @Autowired
    public void setLoginDAO(LoginDAO loginDAO) {
        this.loginDAO = loginDAO;
        this.genericDAO = loginDAO;
    }

    public LoginBusinessImpl() {
        super(Login.class);
    }

    @Transactional(readOnly = true)
    public List<Login> retrieveLoginsByUser(User user) {
        return this.loginDAO.retrieveLoginsByUser(user);
    }

	@Override
	public int retrieveLoginCountByUser(User user) {
		return this.loginDAO.retrieveLoginCountByUser(user);
	}

  
}
