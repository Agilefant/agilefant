package fi.hut.soberit.agilefant.web;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;

/**
 * This actions acts as a dispatcher for different context based views. Basic
 * idea of context is that once some context is activated, it can be accessed
 * again by simply executing this action. Caller of action doesn't need to be
 * aware of the current context.
 * 
 * Context also supports some "context object". Basically this is a stored id
 * which will be passed as a parameter to resulting action.
 * 
 * Context view uses following rules: 1) If context is found, result name is
 * "success_<contextName>" 2) If context is not found, result is "success" (aka
 * default view) 3) If context name is passed, current sontext is changed to
 * that context and value is stored to session. 4) If context object id is
 * passed, its value is stored to session. 5) If context is changed (new context !=
 * current context) old object id is removed
 * 
 * All parameters can also be accessed via session using constants defined in
 * this class.
 */

@Component("contextViewAction")
@Scope("prototype")
public class ContextViewAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 1992243588151483793L;

    private String contextName;
    private Integer contextObjectId;

    @SuppressWarnings("unchecked")
    private Map session;

    public String execute() {
        if (contextName == null) {
            contextName = (String)session.get(ContextAware.CONTEXT_NAME);
            if (contextName == null) {
                return "success_noContext";
            }
        }
        contextObjectId = (Integer)session.get(ContextAware.CONTEXT_OBJECT_ID_AFFIX + contextName);

        if (contextObjectId == null) {
          return "success_" + contextName;
        } else {
          return "success_id_" + contextName;
        }
    }

    @SuppressWarnings("unchecked")
    public void setSession(Map session) {
        this.session = session;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public Integer getContextObjectId() {
        return contextObjectId;
    }
}
