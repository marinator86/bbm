package bbm.database.orgs;

import com.google.inject.AbstractModule;

/**
 * Created by mario on 1/24/17.
 */
public class OrgModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Orgs.class).to(OrgsImpl.class);
    }
}
