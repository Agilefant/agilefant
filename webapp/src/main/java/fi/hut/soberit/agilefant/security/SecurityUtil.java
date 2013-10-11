package fi.hut.soberit.agilefant.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import fi.hut.soberit.agilefant.business.UserBusiness;
import fi.hut.soberit.agilefant.core.ApplicationContextHolder;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.web.RefreshUserInterceptor;

/**
 * Some security-related utilities.
 * 
 * @author Turkka Äijälä
 */
public class SecurityUtil {

    /** A thread local variable to save the user object in during the request. */
    private static ThreadLocal<User> threadLocalUser = new ThreadLocal<User>() {
        protected synchronized User initialValue() {
            return null;
        }
    };

    private SecurityUtil() {
    }

    /**
     * Get id for the currently logged user. It's always valid to call this, as
     * opposed to setLoggedUser, which is valid only during a web request.
     * 
     * @return logged user id
     * @throws IllegalStateException
     *                 when there's no user logged
     */
    public static int getLoggedUserId() throws IllegalStateException {
        if (SecurityContextHolder.getContext().getAuthentication() == null)
            throw new IllegalStateException("no logged user");

        try{
            AgilefantUserDetails ud = (AgilefantUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (ud == null)
                throw new IllegalStateException("no logged user");
            
            return ud.getUserId();
        } catch(ClassCastException cce){
            UserBusiness userBusiness = ApplicationContextHolder.getApplicationContext().getBean(UserBusiness.class);
            User user = userBusiness.retrieveByLoginName("readonly");
            return user.getId();
        }
    }

    /**
     * Set the currently logged-in user (for the current thread/request).
     * <p>
     * The purpose is to store the user-object during a single WWW-request. This
     * is achieved by saving the object in a thread local variable. (is this
     * ok/valid?)
     * <p>
     * You shouldn't normally call this function.
     * 
     * @see RefreshUserInterceptor
     * @see getLoggedUser
     * @param user
     *                currently logged user
     */
    public static void setLoggedUser(User user) {
        threadLocalUser.set(user);
    }

    public static void clearLoggedUser() {
        threadLocalUser.remove();
    }

    /**
     * Get currently logged-in user (for the current thread/request) as set by
     * setLoggedUser.
     * <p>
     * <b>Currently only valid for struts-stuff.</b> ... since
     * RefreshUserInterceptor ensures proper user is set.
     * 
     * @see RefreshUserInterceptor
     * @see setLoggedUser
     * @return User object for the user who's logged in, or null if no user.
     */
    public static User getLoggedUser() {
        return threadLocalUser.get();
    }

    /**
     * Calculate MD5 hash from a string.
     * 
     * @param text
     *                string to calculate hash from
     * @return MD5 hash
     */
    public static String MD5(String text) {

        byte[] bytes = text.getBytes();

        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(bytes);
            byte messageDigest[] = algorithm.digest();

            StringBuffer hex = new StringBuffer();

            for (int i = 0; i < messageDigest.length; i++) {

                if ((messageDigest[i] & 0xff) < 16)
                    hex.append('0');

                hex.append(Integer.toHexString(messageDigest[i] & 0xff));
            }

            return hex.toString();

        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
    }
    
    public static void logoutCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) return;
        Authentication authentication = context.getAuthentication();
        if (authentication == null) return;
        authentication.setAuthenticated(false);
    }
}
