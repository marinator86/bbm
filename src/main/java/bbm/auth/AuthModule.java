package bbm.auth;

import com.google.inject.AbstractModule;
import org.pac4j.http.credentials.authenticator.Authenticator;
import org.pac4j.http.credentials.authenticator.UsernamePasswordAuthenticator;
import ratpack.pac4j.RatpackPac4j;

/**
 * Created by mario on 1/23/17.
 */
public class AuthModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UsernamePasswordAuthenticator.class).to(EnvVarAuthenticator.class);
        bind(EnvironmentVarProvider.class).to(StandardEnvVarProvider.class);
    }
}
