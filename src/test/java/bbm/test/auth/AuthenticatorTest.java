package bbm.test.auth;

import bbm.auth.EnvVarAuthenticator;
import bbm.auth.EnvironmentVarProvider;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.http.credentials.UsernamePasswordCredentials;
import org.pac4j.http.credentials.authenticator.UsernamePasswordAuthenticator;

/**
 * Created by mario on 4/2/17.
 */
public class AuthenticatorTest {

    @Test
    public void testAuthenticator(){
        Injector injector = com.google.inject.Guice.createInjector(
            binder -> {
                binder.bind(EnvironmentVarProvider.class).toInstance(new TestProvider("x", "y"));
                binder.bind(UsernamePasswordAuthenticator.class).to(EnvVarAuthenticator.class);
            }
        );

        UsernamePasswordAuthenticator auth = injector.getInstance(UsernamePasswordAuthenticator.class);
        try {
            auth.validate(new UsernamePasswordCredentials("r", "t", "test"));
            Assert.fail("wrong credentials supplied but no exception thrown");
        } catch (CredentialsException e){}

        try {
            auth.validate(new UsernamePasswordCredentials("x", "y", "test"));
        } catch (CredentialsException e) {
            Assert.fail("right credentials supplied but exception thrown");
        }

    }

    @Test
    public void testAuthenticatorNoCreds(){
        Injector injector = com.google.inject.Guice.createInjector(
            binder -> {
                binder.bind(EnvironmentVarProvider.class).toInstance(new TestProvider(null, ""));
                binder.bind(UsernamePasswordAuthenticator.class).to(EnvVarAuthenticator.class);
            }
        );

        UsernamePasswordAuthenticator auth = injector.getInstance(UsernamePasswordAuthenticator.class);
        try {
            auth.validate(new UsernamePasswordCredentials("r", "t", "test"));
            Assert.fail("wrong credentials supplied but no exception thrown");
        } catch (CredentialsException e){}

        try {
            auth.validate(new UsernamePasswordCredentials("x", "x", "test"));
        } catch (CredentialsException e) {
            Assert.fail("right credentials supplied but exception thrown");
        }

    }

    private class TestProvider implements EnvironmentVarProvider{

        private final String user;
        private final String pass;

        public TestProvider(String user, String pass) {
            this.user = user;
            this.pass = pass;
        }

        @Override
        public String getUserName() {
            return user;
        }

        @Override
        public String getPassword() {
            return pass;
        }
    }
}
