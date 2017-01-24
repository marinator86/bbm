package bbm.database.sandboxes;

import com.google.inject.AbstractModule;

/**
 * Created by mario on 1/19/17.
 */
public class SandboxModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Sandboxes.class).to(SandboxImpl.class);
    }
}
