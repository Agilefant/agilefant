package fi.hut.soberit.agilefant.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Class that can be used for retrieving applicationContext.
 * It is not supposed to be used outside this package.
 *
 * <p>
 * For retrieving application context itself, use {@link ApplicationContextHolder}.
 *
 * @author Juraj Martinka
 *         Date: 25.1.11
 */
@Service("applicationContextProvider")
final class ApplicationContextProvider implements ApplicationContextAware {


    @Override
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.setApplicationContext(applicationContext);
    }
}