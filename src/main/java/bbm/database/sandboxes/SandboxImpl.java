package bbm.database.sandboxes;

import bbm.database.branches.Branch;
import bbm.database.orgs.Org;
import com.google.inject.Inject;
import org.mongodb.morphia.Datastore;

import java.util.List;
import java.util.Optional;

/**
 * Created by mario on 1/19/17.
 */
class SandboxImpl implements Sandboxes {

    private final Datastore datastore;

    @Inject
    public SandboxImpl(Datastore datastore){
        this.datastore = datastore;
    }

    @Override
    public Optional<Sandbox> getFreeSandbox() {
        List<Sandbox> sandboxes = datastore.find(Sandbox.class).asList();
        for(Sandbox sandbox : sandboxes) {
            Branch branch = sandbox.getCurrentBranch();
            if(branch == null || !branch.getManaged()) return Optional.of(sandbox);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Sandbox> getUsedSandbox(Branch branch) {
        Sandbox sandbox = datastore.find(Sandbox.class).field("currentBranch").equal(branch).get();
        return sandbox == null ? Optional.empty() : Optional.of(sandbox);
    }

    @Override
    public Optional<Sandbox> getSandbox(String name) {
        Sandbox sandbox = datastore.find(Sandbox.class).field("name").equal(name).get();
        return sandbox == null ? Optional.empty() : Optional.of(sandbox);
    }

    @Override
    public void createSandbox(String name, Org org) {
        if(getSandbox(name).isPresent()) return;
        Sandbox sandbox = new Sandbox();
        sandbox.setName(name);
        sandbox.setOrg(org);
        datastore.save(sandbox);
    }

    @Override
    public void setBranch(Sandbox sandbox, Branch branch) {
        sandbox.setCurrentBranch(branch);
        datastore.save(sandbox);
    }
}
