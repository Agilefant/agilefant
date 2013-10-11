package fi.hut.soberit.agilefant.business;

import fi.hut.soberit.agilefant.model.notification.NotificationEvent;

public interface NotificationBusiness {

    /**
     * Checks whether given event is notifications' aware, i.e. whether we should react to that event
     * and take appropriate action (most probably send an email).
     * @return true, if event is notifications' aware (sensitive), false otherwise
     */
    boolean isNotificationEvent(NotificationEvent notificationEvent);

    /**
     * Handles given notification event taking appropriate action, mostly sending an email or something similar.
     */
    void handleNotificationEvent(NotificationEvent notificationEvent);


}
