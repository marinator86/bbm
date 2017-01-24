package bbm.auth;

import com.google.inject.AbstractModule;
import org.pac4j.core.credentials.authenticator.Authenticator;
import ratpack.pac4j.RatpackPac4j;

/**
 * Created by mario on 1/23/17.
 */
public class AuthModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Authenticator.class).to(MongoAuthenticator.class);
        bind(RatpackPac4j.ClientsProvider.class).to(ClientsProviderImpl.class);
    }
}
