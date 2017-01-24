package bbm.database.orgs;

import bbm.database.sandboxes.Sandbox;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.List;

/**
 * Created by mario on 1/24/17.
 */
@Entity("orgs")
public final class Org {
    @Id
    private ObjectId id;
    private String salesforceId;

    protected ObjectId getId() {
        return id;
    }
    protected void setId(ObjectId id) {
        this.id = id;
    }
    public String getSalesforceId() {
        return salesforceId;
    }
    protected void setSalesforceId(String salesforceId) {
        this.salesforceId = salesforceId;
    }
}
