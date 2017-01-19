package bbm.database.branches;

import java.util.Optional;

/**
 * Created by mario on 1/19/17.
 */
public interface Branches {
    void createManagedBranch(String name);
    Optional<Branch> getBranch(String name);
    void setManaged(Branch branch);
    void setUnmanaged(Branch branch);
}
