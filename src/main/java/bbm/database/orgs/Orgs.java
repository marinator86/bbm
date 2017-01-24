package bbm.database.orgs;

import java.util.Optional;

/**
 * Created by mario on 1/24/17.
 */
public interface Orgs {
    Optional<Org> getOrg(String salesforceId);
    Org createOrg(String salesforceId);
}
