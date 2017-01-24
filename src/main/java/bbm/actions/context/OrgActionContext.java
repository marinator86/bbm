package bbm.actions.context;

/**
 * Created by mario on 1/24/17.
 */
public interface OrgActionContext extends ActionContext {
    String getSessionId();
    String getSandboxPrefix();
}
