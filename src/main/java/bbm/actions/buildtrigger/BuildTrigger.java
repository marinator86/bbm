package bbm.actions.buildtrigger;

import bbm.actions.ActionResult;
import bbm.actions.context.BuildTriggerContext;

import java.util.function.Function;

/**
 * Created by mario on 2/14/17.
 */
public interface BuildTrigger extends Function<BuildTriggerContext, ActionResult> {
}
