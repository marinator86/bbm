package bbm.actions;

import bbm.actions.context.OrgActionContext;
import ratpack.func.Action;

/**
 * Created by mario on 1/24/17.
 */
public interface SandboxSyncAction {
    void apply(OrgActionContext ctx);
}
