package bbm.auth;

import org.mongodb.morphia.Datastore;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.exception.HttpAction;

/**
 * Created by mario on 1/23/17.
 */
public class MongoAuthenticator implements Authenticator<UsernamePasswordCredentials>{

    private final Datastore datastore;

    public MongoAuthenticator (Datastore datastore){
        this.datastore = datastore;
    }

    @Override
    public void validate(UsernamePasswordCredentials credentials, WebContext context) throws HttpAction {

    }
}
