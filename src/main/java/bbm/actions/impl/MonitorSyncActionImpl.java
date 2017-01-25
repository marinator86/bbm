package bbm.actions.impl;

import bbm.actions.ActionResult;
import bbm.actions.context.BranchActionContext;
import bbm.actions.MonitorSyncAction;
import bbm.database.branches.Branches;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import java.util.Map;

/**
 * Created by mario on 1/18/17.
 */
public class MonitorSyncActionImpl implements MonitorSyncAction {

    private final Branches branches;

    @Inject
    public MonitorSyncActionImpl(Branches branches) {
        this.branches = branches;
    }

    @Override
    public ActionResult apply(BranchActionContext context) {
        final String branchName = context.getBranchName();
        branches.createManagedBranch(branchName);

        return new ActionResult() {
            @Override
            public Boolean getSuccess() {
                return true;
            }

            @Override
            public Map<String, String> getPayload() {
                return ImmutableMap.of(
                        "actionType", "monitor",
                        "branchName", context.getBranchName()
                );
            }
        };
    }
}
