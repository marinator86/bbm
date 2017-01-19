package bbm.database.branches;

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
    public void createManagedBranch(String name) {
        Optional<Branch> branchOptional = getBranch(name);
        if(branchOptional.isPresent()){
            Branch branch = branchOptional.get();
            branch.setManaged(true);
            datastore.save(branch);
            return;
        }
        Branch branch = new Branch();
        branch.setName(name);
        branch.setManaged(true);
        datastore.save(branch);
    }

    @Override
    public Optional<Branch> getBranch(String name) {
        Branch branch = datastore.find(Branch.class).field("name").equal(name).get();
        return branch == null ? Optional.empty() : Optional.of(branch);
    }

    @Override
    public void setManaged(Branch branch) {
        setManaged(branch, true);
    }

    @Override
    public void setUnmanaged(Branch branch) {
        setManaged(branch, false);
    }

    private void setManaged(Branch branch, Boolean value){
        branch.setManaged(value);
        datastore.save(branch);
    }
}
