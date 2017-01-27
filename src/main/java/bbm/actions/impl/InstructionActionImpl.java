package bbm.actions.impl;

import bbm.actions.ActionResult;
import bbm.actions.InstructionAction;
import bbm.actions.context.InstructionActionContext;
import bbm.database.branches.Branch;
import bbm.database.branches.Branches;
import bbm.database.orgs.Orgs;
import bbm.database.sandboxes.Sandbox;
import bbm.database.sandboxes.Sandboxes;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import java.util.Map;
import java.util.Optional;

/**
 * Created by mario on 1/27/17.
 */
public class InstructionActionImpl implements InstructionAction{

    private final Branches branches;
    private final Sandboxes sandboxes;

    @Inject
    public InstructionActionImpl(Branches branches, Sandboxes sandboxes){
        this.branches = branches;
        this.sandboxes = sandboxes;
    }

    @Override
    public ActionResult apply(InstructionActionContext instructionActionContext) {
        Optional<Branch> branchOptional = branches.getBranch(instructionActionContext.getBranchName());
        if(!branchOptional.isPresent() || !branchOptional.get().getManaged())
            return getActionResult(true, ImmutableMap.of(
                    "msg", "branchNotManaged",
                    "sandbox", "",
                    "buildType", "NOBUILD"));
        Branch branch = branchOptional.get();
        Optional<Sandbox> usedSandboxOptional = sandboxes.getUsedSandbox(branch);
        if(!usedSandboxOptional.isPresent())
            return getActionResult(false, ImmutableMap.of(
                    "msg", "sandboxNotFound",
                    "sandbox", "",
                    "buildType", "NOBUILD"));
        return getActionResult(true, ImmutableMap.of(
                "sandbox", usedSandboxOptional.get().getName(),
                "buildType", "CLEANDEPLOY"));
    }

    private ActionResult getActionResult(Boolean success, Map<String, String> payload) {
        return new ActionResult() {
            @Override
            public Boolean getSuccess() {
                return success;
            }

            @Override
            public Map<String, String> getPayload() {
                return payload;
            }
        };
    }
}
