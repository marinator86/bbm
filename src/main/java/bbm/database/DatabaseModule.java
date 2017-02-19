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
    private static final String MONGODB_URI = "MONGODB_URI";

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

        String mongodb_uri = System.getenv(MONGODB_URI);
        if(mongodb_uri == null)
            throw new IllegalStateException(MONGODB_URI + " environment variable must be set with complete path to db!");
        String db_name = mongodb_uri.substring(mongodb_uri.lastIndexOf('/') +1);

        logger.info("MongoDB URI was extracted from database.");
        final Datastore datastore = morphia.createDatastore(new MongoClient(new MongoClientURI(mongodb_uri)), db_name);

        return datastore;
    }}
