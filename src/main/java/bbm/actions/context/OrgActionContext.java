package bbm.actions.context;

import ratpack.handling.Context;

/**
 * Created by mario on 1/24/17.
 */
public interface OrgActionContext extends ActionContext {
    String getUsername();
    String getPassword();
    String getToken();
    String getSandboxPrefix();
    Context getContext();
}
