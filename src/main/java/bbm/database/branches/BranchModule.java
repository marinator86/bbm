package bbm.database.branches;

import com.google.inject.AbstractModule;

/**
 * Created by mario on 1/19/17.
 */
public class BranchModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Branches.class).to(BranchesImpl.class);
    }
}
