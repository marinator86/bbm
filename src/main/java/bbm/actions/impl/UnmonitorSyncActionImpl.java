package bbm.actions.impl;

import bbm.actions.*;
import bbm.actions.context.HookActionContext;
import bbm.database.branches.Branch;
import bbm.database.branches.Branches;
import bbm.database.repositories.Repositories;
import bbm.database.repositories.Repository;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import java.util.Map;
import java.util.Optional;

/**
 * Created by mario on 1/18/17.
 */
public class UnmonitorSyncActionImpl implements HookAction {

    private final Branches branches;
    private final Repositories repositories;

    @Inject
    public UnmonitorSyncActionImpl(Branches branches, Repositories repositories) {
        this.branches = branches;
        this.repositories = repositories;
    }

    @Override
    public ActionResult apply(HookActionContext context) {
        final String branchName = context.getBranchName();
        Optional<Repository> repositoryOptional = repositories.getRepository(context.getRepoUuid());
        if(!repositoryOptional.isPresent()){
            return getActionResult(branchName, false);
        }

        Repository repository = repositoryOptional.get();
        Optional<Branch> branchOptional = branches.getBranch(branchName, repository);
        if(branchOptional.isPresent()){
            branches.setUnmanaged(branchOptional.get());
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
