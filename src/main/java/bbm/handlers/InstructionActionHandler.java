package bbm.handlers;

import bbm.actions.ActionResult;
import bbm.actions.InstructionAction;
import bbm.actions.context.InstructionActionContext;
import ratpack.handling.Context;
import ratpack.handling.Handler;

/**
 * Created by mario on 1/27/17.
 */
public class InstructionActionHandler implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        String repositoryUID = ctx.getAllPathTokens().get("repositoryUID");
        String branchName = ctx.getAllPathTokens().get("branchName");

        ActionResult result = ctx.get(InstructionAction.class).apply(new InstructionActionContext() {
            @Override
            public String getRepositoryUID() {
                return repositoryUID;
            }

            @Override
            public String getBranchName() {
                return branchName;
            }
        });

        ctx.render(result);
    }
}
