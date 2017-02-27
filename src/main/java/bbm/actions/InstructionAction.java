package bbm.actions;

import bbm.actions.context.InstructionActionContext;

/**
 * Created by mario on 1/27/17.
 */
public interface InstructionAction extends java.util.function.Function<InstructionActionContext, ActionResult> {

}
