package fi.hut.soberit.agilefant.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.hut.soberit.agilefant.business.IterationBusiness;

@Component
public class TokenGenerator {

    @Autowired
    private IterationBusiness iterationBusiness;

    public String generateReadonlyToken()
    {
        SecureRandom r = new SecureRandom();
        String token = new BigInteger(130, r).toString();
        return generateReadonlyToken(token);
    }

    public String generateReadonlyToken(String token)
    {
        int count = iterationBusiness.getIterationCountFromReadonlyToken(token);
        while(count > 0){
            SecureRandom r = new SecureRandom();
            token = new BigInteger(130, r).toString();
            count = iterationBusiness.getIterationCountFromReadonlyToken(token);
        }
        
        return token;
    }	

}
