package bbm.database.repositories;

import com.google.inject.AbstractModule;

/**
 * Created by mario on 2/13/17.
 */
public class RepositoryModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(Repositories.class).to(RepositoriesImpl.class);
    }
}
