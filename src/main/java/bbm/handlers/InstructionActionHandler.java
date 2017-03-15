package bbm.handlers;

import bbm.actions.ActionResult;
import bbm.actions.InstructionAction;
import bbm.actions.context.InstructionActionContext;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;
import ratpack.func.Function;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.path.PathTokens;

/**
 * Created by mario on 1/27/17.
 */
public class InstructionActionHandler implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        PathTokens allPathTokens = ctx.getAllPathTokens();
        String repositoryUID = allPathTokens.get("repositoryUID");
        String branchName = allPathTokens.get("branchName");
        String currentCommit = allPathTokens.get("commit");

        Promise.sync(() -> new InstructionActionContext() {
            @Override
            public String getRepositoryUID() {
                return repositoryUID;
            }

            @Override
            public String getBranchName() {
                return branchName;
            }

            @Override
            public String getCurrentCommit() {
                return currentCommit;
            }
        })
        .map(Function.from(ctx.get(InstructionAction.class)))
        .then(actionResult -> ctx.render(actionResult));
    }
}
