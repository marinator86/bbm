package bbm.actions.impl;

import bbm.actions.ActionResult;
import bbm.actions.context.BranchActionContext;
import bbm.actions.MonitorSyncAction;
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
public class MonitorSyncActionImpl implements MonitorSyncAction {

    private final Branches branches;
    private final Sandboxes sandboxes;

    @Inject
    public MonitorSyncActionImpl(Branches branches, Sandboxes sandboxes) {
        this.branches = branches;
        this.sandboxes = sandboxes;
    }

    @Override
    public ActionResult apply(BranchActionContext context) {
        final String branchName = context.getBranchName();
        Optional<Sandbox> sandboxOptional = sandboxes.getFreeSandbox();
        if(!sandboxOptional.isPresent()) {

            return getActionResult(false, ImmutableMap.of(
                    "msg", "No free Sandbox!"
            ));
        }
        branches.createManagedBranch(branchName);
        sandboxes.setBranch(sandboxOptional.get(), branches.getBranch(branchName).get());
        return getActionResult(true, ImmutableMap.of(
                "success", "true"
        ));
    }

    private ActionResult getActionResult(Boolean result, Map<String, String> payload) {
        return new ActionResult() {
            @Override
            public Boolean getSuccess() {
                return result;
            }

            @Override
            public Map<String, String> getPayload() {
                return payload;
            }
        };
    }
}
