package bbm.actions;

import bbm.actions.context.HookActionContext;
import ratpack.func.Function;

/**
 * Created by mario on 2/19/17.
 */
public interface HookAction extends Function<HookActionContext, ActionResult> {
    public enum Types {
        MONITOR,
        UNMONITOR
    }

}
