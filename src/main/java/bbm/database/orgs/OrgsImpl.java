package bbm.database.orgs;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import java.util.Optional;

/**
 * Created by mario on 1/24/17.
 */
public class OrgsImpl implements Orgs {

    private final Datastore datastore;

    @Inject
    public OrgsImpl(Datastore datastore){
        this.datastore = datastore;
    }

    @Override
    public Optional<Org> getOrg(String salesforceId) {
        return Optional.ofNullable(datastore.find(Org.class).field("salesforceId").equal(salesforceId).get());
    }

    @Override
    public Org createOrg(String salesforceId) {
        Optional<Org> org = getOrg(salesforceId);
        if(org.isPresent()) return org.get();
        Org newOrg = new Org();
        newOrg.setId(new ObjectId());
        newOrg.setSalesforceId(salesforceId);
        datastore.save(newOrg);
        return newOrg;
    }
}
