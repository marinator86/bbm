package bbm.test;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by mario on 7/27/16.
 */
public class DatabaseModule extends AbstractModule {

    private static final String DATABASE_PACKAGE = "bbm.database";

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


        MongoClient mongoClient = new MongoClient();
        mongoClient.dropDatabase("morphia_test_bbm");
        final Datastore datastore = morphia.createDatastore(mongoClient, "morphia_test_bbm");

        return datastore;
    }
}