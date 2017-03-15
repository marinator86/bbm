package bbm.database.branches;

import bbm.database.repositories.Repository;
import com.google.inject.Inject;
import org.mongodb.morphia.Datastore;

import java.util.Optional;

/**
 * Created by mario on 1/19/17.
 */
class BranchesImpl implements Branches{

    private final Datastore datastore;

    @Inject
    public BranchesImpl(Datastore datastore){
        this.datastore = datastore;
    }

    /**
     * Creates a managed branch or updates an existing branch to be managed.
     * @param name the branch Name
     */
    @Override
    public void createManagedBranch(String name, Repository repository, String initialCommit) {
        Optional<Branch> branchOptional = getBranch(name, repository);
        Branch branch;
        if(!branchOptional.isPresent()){
            branch = new Branch();
            branch.setName(name);
            branch.setRepository(repository);
        } else {
            branch = branchOptional.get();
        }
        branch.setManaged(true);
        branch.setInitialCommit(initialCommit);
        datastore.save(branch);
    }

    @Override
    public Optional<Branch> getBranch(String name, Repository repository) {
        Branch branch = datastore.find(Branch.class)
                .field("name").equal(name)
                .field("repository").equal(repository)
                .get();
        return branch == null ? Optional.empty() : Optional.of(branch);
    }

    @Override
    public void setManaged(Branch branch, String initialCommit) {
        branch.setManaged(true);
        branch.setInitialCommit(initialCommit);
        datastore.save(branch);
    }

    @Override
    public void setUnmanaged(Branch branch) {
        branch.setManaged(false);
        datastore.save(branch);
    }

}
