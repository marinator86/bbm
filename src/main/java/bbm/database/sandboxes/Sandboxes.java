package bbm.database.sandboxes;

import bbm.database.branches.Branch;

import java.util.Optional;

/**
 * Created by mario on 1/19/17.
 */
public interface Sandboxes {
    Optional<Sandbox> getFreeSandbox();
    Optional<Sandbox> getUsedSandbox(Branch branch);
    Optional<Sandbox> getSandbox(String name);
    void createSandbox(String name);
    void setBranch(Sandbox sandbox, Branch branch);
}
