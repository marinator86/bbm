package bbm.database.orgs;

import bbm.database.branches.Branch;

import java.util.Optional;

/**
 * Created by mario on 1/19/17.
 */
public interface Orgs {
    Optional<Org> getFreeOrg();
    Optional<Org> getOrg(String name);
    void createOrg(String name);
    void setBranch(Org org, Branch branch);
}
