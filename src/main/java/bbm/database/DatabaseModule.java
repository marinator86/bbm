package bbm.database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mario on 1/18/17.
 */
public class DatabaseModule extends AbstractModule {
    protected final static Logger logger = LoggerFactory.getLogger(DatabaseModule.class);

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
        final String mongodb_uri = System.getenv("MONGODB_URI");
        logger.info(mongodb_uri);
        final Datastore datastore = morphia.createDatastore(new MongoClient(new MongoClientURI(mongodb_uri)), "heroku_9mft2pt9");

        return datastore;
    }}
