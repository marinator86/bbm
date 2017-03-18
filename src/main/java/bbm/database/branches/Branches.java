package bbm.database.branches;

import bbm.database.repositories.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by mario on 1/19/17.
 */
public interface Branches {
    void createManagedBranch(String name, Repository repository, String initialCommit);
    Optional<Branch> getBranch(String name, Repository repository);
    List<Branch> getBranches(Repository repository);
    void setManaged(Branch branch, String initialCommit);
    void setUnmanaged(Branch branch);
}
