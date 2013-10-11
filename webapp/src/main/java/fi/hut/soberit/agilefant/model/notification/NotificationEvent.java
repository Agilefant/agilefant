package fi.hut.soberit.agilefant.model.notification;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.Validate;

/**
 * Class that encapsulates all information about notifications' aware event.
 */
public class NotificationEvent {

    private Object entity;
    /**
     * Map containing pairs [propertyName, propertyValue] for which property that is relevant to this notification event.
     */
    private Map<String, Object> entityProperties = new HashMap<String, Object>();

    private NotificationEventType eventType;

    public NotificationEvent(Object entity, NotificationEventType eventType) {
        Validate.notNull(entity, "NotificationEvent's entity cannot be null!");
        this.entity = entity;
        this.eventType = eventType;
    }

    public Object getEntity() {
        return entity;
    }

    public NotificationEventType getEventType() {
        return eventType;
    }

    public Map<String, Object> getEntityProperties() {
        return entityProperties;
    }

    public void setEntityProperties(Map<String, Object> entityProperties) {
        this.entityProperties = Collections.unmodifiableMap(entityProperties);
    }
}
