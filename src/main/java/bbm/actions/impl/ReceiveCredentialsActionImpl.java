package bbm.actions.impl;

import bbm.actions.ActionContext;
import bbm.actions.ActionResult;
import bbm.actions.ReceiveCredentialsAction;
import bbm.database.branches.Branch;
import bbm.database.branches.Branches;
import bbm.database.orgs.Org;
import bbm.database.orgs.Orgs;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import java.util.Map;
import java.util.Optional;

/**
 * Created by mario on 1/18/17.
 */
public class ReceiveCredentialsActionImpl implements ReceiveCredentialsAction {

    private final Orgs orgs;
    private final Branches branches;

    @Inject
    public ReceiveCredentialsActionImpl(Orgs orgs, Branches branches) {
        this.orgs = orgs;
        this.branches = branches;
    }

    @Override
    public ActionResult execute(ActionContext context) {
        final String branchName = context.getBranchName();
        final Optional<Branch> branch = branches.getBranch(branchName);
        if(!(branch.isPresent() && branch.get().getManaged()))
            return getActionResult(false, branchName, null);

        final Optional<Org> usedOrg = orgs.getUsedOrg(branch.get());
        if(usedOrg.isPresent()) {
            orgs.setBranch(usedOrg.get(), branch.get());
            return getActionResult(true, branchName, usedOrg.get().getName());
        }

        final Optional<Org> freeOrg = orgs.getFreeOrg();
        if(freeOrg.isPresent()) {
            orgs.setBranch(freeOrg.get(), branch.get());
            return getActionResult(true, branchName, freeOrg.get().getName());
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
