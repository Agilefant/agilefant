/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package fi.hut.soberit.agilefant.core;

import fi.hut.soberit.agilefant.model.notification.NotificationConfiguration;
import java.util.List;
import javax.annotation.Resource;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:conf/applicationContext-core.xml", "file:conf/applicationContext-notifications.xml" })
public class ApplicationContextHolderTest {

    private List<NotificationConfiguration> notificationsConfiguration;

    @Resource(name = "notificationsConfiguration")
    public void setNotificationsConfiguration(List<NotificationConfiguration> notificationsConfiguration) {
        this.notificationsConfiguration = notificationsConfiguration;
    }

    @Test
    public void checkApplicationContextCorrectlyBuild() {
        final ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
        assertNotNull(applicationContext);

        final ApplicationContextProvider contextProvider =
                (ApplicationContextProvider) applicationContext.getBean("applicationContextProvider");
        assertNotNull(contextProvider);
    }
}