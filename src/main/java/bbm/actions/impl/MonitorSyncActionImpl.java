package bbm.actions.impl;

import bbm.actions.ActionResult;
import bbm.actions.HookAction;
import bbm.actions.context.HookActionContext;
import bbm.database.branches.Branch;
import bbm.database.branches.Branches;
import bbm.database.repositories.Repositories;
import bbm.database.repositories.Repository;
import bbm.database.sandboxes.Sandbox;
import bbm.database.sandboxes.Sandboxes;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import java.util.Map;
import java.util.Optional;

/**
 * Created by mario on 1/18/17.
 */
public class MonitorSyncActionImpl implements HookAction {

    private final Branches branches;
    private final Sandboxes sandboxes;
    private final Repositories repositories;

    @Inject
    public MonitorSyncActionImpl(Branches branches, Sandboxes sandboxes, Repositories repositories) {
        this.branches = branches;
        this.sandboxes = sandboxes;
        this.repositories = repositories;
    }

    @Override
    public ActionResult apply(HookActionContext context) {
        final String branchName = context.getBranchName();
        Optional<Sandbox> sandboxOptional = sandboxes.getFreeSandbox();
        if(!sandboxOptional.isPresent()) {
            return getActionResult(false, ImmutableMap.of(
                "msg", "No free Sandbox!"
            ));
        }
        Optional<Repository> repositoryOptional = repositories.getRepository(context.getRepoUuid());
        if(!repositoryOptional.isPresent()){
            return getActionResult(false, ImmutableMap.of(
                "msg", "Repository " + context.getRepoUuid() + " not found!"
            ));
        }
        Repository repo = repositoryOptional.get();
        branches.createManagedBranch(branchName, repositoryOptional.get(), context.getHookCommit());
        Branch branch = branches.getBranch(branchName, repo).get();
        sandboxes.setBranch(sandboxOptional.get(), branch);
        return getActionResult(true, ImmutableMap.of(
                "branch", branchName
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
