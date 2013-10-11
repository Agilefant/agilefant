package fi.hut.soberit.agilefant.business.impl;

import fi.hut.soberit.agilefant.business.NotificationBusiness;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.model.notification.NotificationConfiguration;
import fi.hut.soberit.agilefant.model.notification.NotificationEvent;
import fi.hut.soberit.agilefant.model.notification.NotificationEventType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.antlr.stringtemplate.StringTemplate;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

public class NotificationEmailSender implements NotificationBusiness {

    public static final String NOTIFICATION_BUSINESS_BEAN_NAME = "notificationEmailSender";
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationEmailSender.class);

    private List<NotificationConfiguration> notificationsConfiguration;
    private JavaMailSender mailSender;
    private AsyncTaskExecutor asyncTaskExecutor;


    private SimpleMailMessage newStoryTemplate;
    private SimpleMailMessage updateStoryTemplate;
    private SimpleMailMessage deleteStoryTemplate;
    private SimpleMailMessage newTaskTemplate;
    private SimpleMailMessage updateTaskTemplate;
    private SimpleMailMessage deleteTaskTemplate;

    private String agilefantUrl;
    /** Global turn on/off for all notifications */
    private boolean notificationsEnabled = false;

    /**
     * Checks whether specified {@code notificationEvent} actually represents valid notification event.
     * @param notificationEvent
     * @return
     */
    @Override
    public boolean isNotificationEvent(NotificationEvent notificationEvent) {
        if (!notificationsEnabled) {
            return false;
        }
        Validate.notNull(notificationEvent, "notificationEvent cannot be null");
        if (CollectionUtils.isEmpty(notificationsConfiguration)) {
            return false;
        }
        for (NotificationConfiguration notificationConfiguration : notificationsConfiguration) {
            if (matchNotificationConfiguration(notificationEvent, notificationConfiguration)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Checks if given event represents a notification that we are interested in and in such a case creates
     * an email from template and send it to the recipients.
     * <p>
     *     This implementation perform its task asynchronously because sending an email can take a while and we do not
     *     want to force the user to wait until email is sent.
     * </p>
     * @param notificationEvent
     */
    @Override
    public void handleNotificationEvent(final NotificationEvent notificationEvent) {
        this.asyncTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (!isNotificationEvent(notificationEvent)) {
                    // since this is not an notification event, simply do nothing
                    return;
                }

                if (CollectionUtils.isEmpty(getNotificationRecipients(notificationEvent))) {
                    // cause there are no recipients it does not make sense to send a notification message
                    return;
                }

                sendMessage(notificationEvent);
            }
        });
    }

    //------------------------------------------ SETTERS - better for testability --------------------------------------

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    public void setAsyncTaskExecutor(AsyncTaskExecutor asyncTaskExecutor) {
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    @Resource(name = "notificationsConfiguration")
    public void setNotificationsConfiguration(List<NotificationConfiguration> notificationsConfiguration) {
        this.notificationsConfiguration = notificationsConfiguration;
    }

    @Resource(name = "newTaskTemplate")
    public void setNewTaskTemplate(SimpleMailMessage newTaskTemplate) {
        this.newTaskTemplate = newTaskTemplate;
    }

    @Resource(name = "updateTaskTemplate")
    public void setUpdateTaskTemplate(SimpleMailMessage updateTaskTemplate) {
        this.updateTaskTemplate = updateTaskTemplate;
    }

    @Resource(name = "deleteTaskTemplate")
    public void setDeleteTaskTemplate(SimpleMailMessage deleteTaskTemplate) {
        this.deleteTaskTemplate = deleteTaskTemplate;
    }

    @Resource(name = "newStoryTemplate")
    public void setNewStoryTemplate(SimpleMailMessage newStoryTemplate) {
        this.newStoryTemplate = newStoryTemplate;
    }

    @Resource(name = "updateStoryTemplate")
    public void setUpdateStoryTemplate(SimpleMailMessage updateStoryTemplate) {
        this.updateStoryTemplate = updateStoryTemplate;
    }

    @Resource(name = "deleteStoryTemplate")
    public void setDeleteStoryTemplate(SimpleMailMessage deleteStoryTemplate) {
        this.deleteStoryTemplate = deleteStoryTemplate;
    }

    public void setAgilefantUrl(String agilefantUrl) {
        this.agilefantUrl = agilefantUrl;
    }
    
    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    /**
     * Checks whether given {@code notificationEvent} is supposed to be valid notification event
     * by specification in {@code notificationsConfiguration}.
     *
     * @param notificationEvent see {@link fi.hut.soberit.agilefant.model.notification.NotificationEvent#getEntity()}
     * @param notificationConfiguration
     * @return
     */
    private boolean matchNotificationConfiguration(NotificationEvent notificationEvent,
            NotificationConfiguration notificationConfiguration) {

        if ( ! notificationConfiguration.getNotificationAwareClass()
                .isAssignableFrom(notificationEvent.getEntity().getClass())) {
            return false;
        }

        if ( ! notificationConfiguration.getEventTypes().contains(notificationEvent.getEventType())) {
            // we are not interested in this kind of events
            return false;
        }

        if ( ! CollectionUtils.isEmpty(notificationConfiguration.getClassPropertiesNames())) {
            // finer notification configuration has been specified - we must check whether notification event
            // relates to the particular properties
            if (CollectionUtils.isEmpty(notificationEvent.getEntityProperties())) {
                return false;
            }

            for (String notificationEventEntityProperty : notificationEvent.getEntityProperties().keySet()) {
                if (notificationConfiguration.getClassPropertiesNames().contains(notificationEventEntityProperty)) {
                    return true;
                }
            }
            // no property marked to be notifications' sensitive has been set in notificationEvent object
            return false;
        }

        return true;
    }


    private void sendMessage(NotificationEvent notificationEvent) {
        if (mailSender == null) {
            throw new IllegalStateException("Mail sender is not set - cannot send notification email!");
        }
        
        try {
            final SimpleMailMessage notificationMessage = createNotificationMessage(notificationEvent);
            mailSender.send(notificationMessage);
            logger.info("Notification message '" + notificationMessage.getSubject() + "' has been sent successfully.");
        } catch (Exception e) {
            logger.error("Cannot send notification message", e);
        }
        
    }


    private SimpleMailMessage createNotificationMessage(NotificationEvent notificationEvent) {
        SimpleMailMessage mail = new SimpleMailMessage(getMessageTemplate(notificationEvent));

        final StringTemplate subjectTemplate = new StringTemplate(mail.getSubject());
        final StringTemplate bodyTemplate = new StringTemplate(mail.getText());

//        common attributes
        bodyTemplate.setAttribute("agilefantUrl", this.agilefantUrl);

        if (notificationEvent.getEntity() instanceof Story) {
            final Story story = (Story) notificationEvent.getEntity();
            subjectTemplate.setAttribute("storyName", story.getName());
            bodyTemplate.setAttribute("storyName", story.getName());
            bodyTemplate.setAttribute("storyId", story.getId());
        }
        if (notificationEvent.getEntity() instanceof Task) {
            final Task task = (Task) notificationEvent.getEntity();
            subjectTemplate.setAttribute("taskName", task.getName());
            bodyTemplate.setAttribute("taskName", task.getName());
            String taskParentUrl = agilefantUrl;
            if (task.getStory() != null) {
                taskParentUrl += "/qr.action?q=story:" + task.getStory().getId();
            } else if (task.getIteration() != null) {
                // this task is without story, set reference to the iteration
                taskParentUrl += "/editIteration.action?iterationId=" + task.getIteration().getId();
            } else {
                taskParentUrl = "<Error: Cannot find task's parent>";
            }
            bodyTemplate.setAttribute("taskParentUrl", taskParentUrl);
        }

        mail.setSubject(subjectTemplate.toString());
        mail.setText(bodyTemplate.toString());


        final List<String> notificationRecipients = getNotificationRecipients(notificationEvent);
        mail.setTo(notificationRecipients.toArray(new String[0]));

        return mail;
    }

    private SimpleMailMessage getMessageTemplate(NotificationEvent notificationEvent) {
        if (notificationEvent.getEntity() instanceof Story) {
            switch (notificationEvent.getEventType()) {
                case INSERT:
                    return newStoryTemplate;
                case UPDATE:
                    return updateStoryTemplate;
                case DELETE:
                    return deleteStoryTemplate;
            }
        } else if (notificationEvent.getEntity() instanceof Task) {
            switch (notificationEvent.getEventType()) {
                case INSERT:
                    return newTaskTemplate;
                case UPDATE:
                    return updateTaskTemplate;
                case DELETE:
                    return deleteTaskTemplate;
            }
        }

        return null;
    }

    private List<String> getNotificationRecipients(NotificationEvent notificationEvent) {
        final List<String> recipientsMails = new ArrayList<String>();

        final Set<User> responsibleUsers = new HashSet<User>();

        if (notificationEvent.getEntity() instanceof Story) {
            responsibleUsers.addAll(((Story) notificationEvent.getEntity()).getResponsibles());
        } else if (notificationEvent.getEntity() instanceof Task) {
            responsibleUsers.addAll(((Task) notificationEvent.getEntity()).getResponsibles());
        }

        for (User user : responsibleUsers) {
            recipientsMails.add(user.getEmail());
        }
        
        return recipientsMails;
    }

}
