package bbm.actions.impl;

import bbm.actions.*;
import bbm.actions.context.HookActionContext;
import bbm.database.branches.Branch;
import bbm.database.branches.Branches;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import java.util.Map;
import java.util.Optional;

/**
 * Created by mario on 1/18/17.
 */
public class UnmonitorSyncActionImpl implements HookAction {

    private final Branches branches;

    @Inject
    public UnmonitorSyncActionImpl(Branches branches) {
        this.branches = branches;
    }

    @Override
    public ActionResult apply(HookActionContext context) {
        final String branchName = context.getBranchName();

        Optional<Branch> branch = branches.getBranch(branchName);
        if(branch.isPresent()){
            branches.setUnmanaged(branch.get());
            return getActionResult(branchName, true);
        }

        return getActionResult(branchName, false);
    }

    private ActionResult getActionResult(final String branchName, final Boolean success) {
        return new ActionResult() {
            @Override
            public Boolean getSuccess() {
                return success;
            }

            @Override
            public Map<String, String> getPayload() {
                return ImmutableMap.of(
                        "actionType", "unmonitor",
                        "branchName", branchName
                );
            }
        };
    }
}
