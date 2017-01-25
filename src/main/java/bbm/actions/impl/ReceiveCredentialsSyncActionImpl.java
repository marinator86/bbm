package bbm.actions.impl;

import bbm.actions.ActionResult;
import bbm.actions.ReceiveCredentialsSyncAction;
import bbm.actions.context.BranchActionContext;
import bbm.database.branches.Branch;
import bbm.database.branches.Branches;
import bbm.database.sandboxes.Sandbox;
import bbm.database.sandboxes.Sandboxes;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import java.util.Map;
import java.util.Optional;

/**
 * Created by mario on 1/18/17.
 */
public class ReceiveCredentialsSyncActionImpl implements ReceiveCredentialsSyncAction {

    private final Sandboxes sandboxes;
    private final Branches branches;

    @Inject
    public ReceiveCredentialsSyncActionImpl(Sandboxes sandboxes, Branches branches) {
        this.sandboxes = sandboxes;
        this.branches = branches;
    }

    @Override
    public ActionResult apply(BranchActionContext context) {
        final String branchName = context.getBranchName();
        final Optional<Branch> branch = branches.getBranch(branchName);
        if(!(branch.isPresent() && branch.get().getManaged()))
            return getActionResult(false, branchName, null);

        final Optional<Sandbox> usedSandbox = sandboxes.getUsedSandbox(branch.get());
        if(usedSandbox.isPresent()) {
            sandboxes.setBranch(usedSandbox.get(), branch.get());
            return getActionResult(true, branchName, usedSandbox.get().getName());
        }

        final Optional<Sandbox> freeSandbox = sandboxes.getFreeSandbox();
        if(freeSandbox.isPresent()) {
            sandboxes.setBranch(freeSandbox.get(), branch.get());
            return getActionResult(true, branchName, freeSandbox.get().getName());
        }

        return getActionResult(false, branchName, null);
    }

    private ActionResult getActionResult(final Boolean success, final String branchName, final String orgName) {
        return new ActionResult() {
            @Override
            public Boolean getSuccess() {
                return success;
            }

            @Override
            public Map<String, String> getPayload() {
                return ImmutableMap.of(
                        "actionType", "receive_credentials",
                        "branchName", branchName,
                        "orgName", orgName
                );
            }
        };
    }
}
