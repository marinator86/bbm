package bbm.database.orgs;

import bbm.database.branches.Branch;
import com.google.inject.Inject;
import org.mongodb.morphia.Datastore;

import java.util.List;
import java.util.Optional;

/**
 * Created by mario on 1/19/17.
 */
class OrgsImpl implements Orgs{

    private final Datastore datastore;

    @Inject
    public OrgsImpl(Datastore datastore){
        this.datastore = datastore;
    }

    @Override
    public Optional<Org> getFreeOrg() {
        List<Org> orgs = datastore.find(Org.class).asList();
        for(Org org : orgs) {
            Branch branch = org.getCurrentBranch();
            if(branch == null || !branch.getManaged()) return Optional.of(org);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Org> getUsedOrg(Branch branch) {
        Org org = datastore.find(Org.class).field("currentBranch").equal(branch).get();
        return org == null ? Optional.empty() : Optional.of(org);
    }

    @Override
    public Optional<Org> getOrg(String name) {
        Org org = datastore.find(Org.class).field("name").equal(name).get();
        return org == null ? Optional.empty() : Optional.of(org);
    }

    @Override
    public void createOrg(String name) {
        if(getOrg(name).isPresent()) return;
        Org org = new Org();
        org.setName(name);
        datastore.save(org);
    }

    @Override
    public void setBranch(Org org, Branch branch) {
        org.setCurrentBranch(branch);
        datastore.save(org);
    }
}
