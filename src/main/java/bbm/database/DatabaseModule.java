package bbm.database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by mario on 1/18/17.
 */
public class DatabaseModule extends AbstractModule {
    private static final String DATABASE_URI = "mongodb://bbmApp:y7X05DaMNg2PktPGUQm7@ds051655.mlab.com:51655/heroku_9mft2pt9";
    private static final String DATABASE_NAME = "heroku_9mft2pt9";
    private static final String DATABASE_PACKAGE = "database";

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    /**
     * Returns the Datastore. This type shouldn't be made available outside of the package
     */
    Datastore provideDatabase(){
        final Morphia morphia = new Morphia();

        // tell morphia where to find your classes
        // can be called multiple times with different packages or classes
        morphia.mapPackage(DATABASE_PACKAGE);

        final Datastore datastore = morphia.createDatastore(new MongoClient(new MongoClientURI(DATABASE_URI)), DATABASE_NAME);

        return datastore;
    }}
