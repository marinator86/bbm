package bbm.auth;

import org.mongodb.morphia.Datastore;
import org.pac4j.http.credentials.UsernamePasswordCredentials;
import org.pac4j.http.credentials.authenticator.UsernamePasswordAuthenticator;

/**
 * Created by mario on 1/23/17.
 */
public class MongoAuthenticator implements UsernamePasswordAuthenticator{

    private final Datastore datastore;

    public MongoAuthenticator (Datastore datastore){
        this.datastore = datastore;
    }

    @Override
    public void validate(UsernamePasswordCredentials credentials) {

    }
}
