package bbm.database.branches;

import bbm.database.repositories.Repository;

import java.util.Optional;

/**
 * Created by mario on 1/19/17.
 */
public interface Branches {
    void createManagedBranch(String name, Repository repository);
    Optional<Branch> getBranch(String name, Repository repository);
    void setManaged(Branch branch);
    void setUnmanaged(Branch branch);
}