package bbm.actions;

import bbm.actions.context.ActionContext;

import java.util.function.Function;

/**
 * Created by mario on 1/18/17.
 */
public interface SyncAction<T extends ActionContext> extends Function<T, ActionResult> {
}
