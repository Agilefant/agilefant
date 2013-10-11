package fi.hut.soberit.agilefant.model.notification;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.Validate;

public class NotificationConfiguration {

    /** Persistent (Hibernate) class which should be monitored for changes of properties' values*/
    private Class notificationAwareClass;

    /**
     * List of all properties' names in {@code notificationAwareClass} which are supposed to be monitored for change
     * and notifications are sent when any of these fields change.
     * By default this list can be empty which means that any of {@code notificationAwareClass}'s property change triggers
     * notification event.
     */
    private List<String> classPropertiesNames;

    /** Types of events which should trigger notification event - by default any type of event triggers notification event. */
    private List<NotificationEventType> eventTypes =
            Arrays.asList(NotificationEventType.INSERT, NotificationEventType.UPDATE, NotificationEventType.DELETE);

    public NotificationConfiguration(Class notificationAwareClass) {
        Validate.notNull(notificationAwareClass, "At least class for notification event must be specified!");
        this.notificationAwareClass = notificationAwareClass;
    }

    public Class getNotificationAwareClass() {
        return notificationAwareClass;
    }

    public List<String> getClassPropertiesNames() {
        return classPropertiesNames;
    }

    public List<NotificationEventType> getEventTypes() {
        return eventTypes;
    }


    public void setClassPropertiesNames(List<String> classPropertiesNames) {
        this.classPropertiesNames = classPropertiesNames;
    }

    public void setEventTypes(List<NotificationEventType> eventTypes) {
        this.eventTypes = eventTypes;
    }
}
