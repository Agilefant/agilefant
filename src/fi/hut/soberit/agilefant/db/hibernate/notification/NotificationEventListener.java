/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package fi.hut.soberit.agilefant.db.hibernate.notification;

import fi.hut.soberit.agilefant.core.ApplicationContextHolder;
import fi.hut.soberit.agilefant.business.NotificationBusiness;
import fi.hut.soberit.agilefant.business.impl.NotificationEmailSender;
import fi.hut.soberit.agilefant.model.notification.NotificationEvent;
import fi.hut.soberit.agilefant.model.notification.NotificationEventType;
import org.hibernate.event.PostCollectionRemoveEvent;
import org.hibernate.event.PostCollectionRemoveEventListener;
import org.hibernate.event.PostCollectionUpdateEvent;
import org.hibernate.event.PostCollectionUpdateEventListener;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.springframework.context.ApplicationContext;


/**
 * Hibernate listener which listens to the common "Database" events and handle them
 * via {@link NotificationBusiness}.
 */
public class NotificationEventListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

    private NotificationBusiness notificationBusiness;

    public void onPostInsert(PostInsertEvent postInsertEvent) {
        checkState();
        final NotificationEvent notificationEvent =
                new NotificationEvent(postInsertEvent.getEntity(), NotificationEventType.INSERT);

        // TODO: fill event properties

        notificationBusiness.handleNotificationEvent(notificationEvent);
    }

    public void onPostUpdate(PostUpdateEvent postUpdateEvent) {
        checkState();

        final NotificationEvent notificationEvent =
                new NotificationEvent(postUpdateEvent.getEntity(), NotificationEventType.UPDATE);

        // TODO: fill event properties

        notificationBusiness.handleNotificationEvent(notificationEvent);
    }

    public void onPostDelete(PostDeleteEvent postDeleteEvent) {
        checkState();
        notificationBusiness.handleNotificationEvent(
                new NotificationEvent(postDeleteEvent.getEntity(), NotificationEventType.DELETE));
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    private void checkState() {
        if (notificationBusiness == null) {
            final ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
            if (applicationContext == null) {
                throw new IllegalStateException("Cannot access Spring application context!");
            }

            notificationBusiness = (NotificationBusiness) applicationContext.getBean(
                    NotificationEmailSender.NOTIFICATION_BUSINESS_BEAN_NAME);
        }
    }
}