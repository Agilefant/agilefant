package fi.hut.soberit.agilefant.core;

import org.springframework.context.ApplicationContext;

/**
 * This class can be used for retrieving of Spring's applicationContext.
 * <p>
 * Please, use wisely and only if no better option (i.e. Dependency Injection) is available!
 *
 * @author Juraj Martinka
 *         Date: 25.1.11
 */
public final class ApplicationContextHolder {

    private static ApplicationContext applicationContext;

    private ApplicationContextHolder() {
        // DO NOT INSTANTIATE!
    }


    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    static synchronized void setApplicationContext(ApplicationContext appContext) {
        applicationContext = appContext;
    }

}
