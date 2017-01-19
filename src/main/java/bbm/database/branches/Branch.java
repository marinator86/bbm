package bbm.database.branches;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by mario on 1/19/17.
 */
@Entity("branches")
public final class Branch {
    @Id
    private ObjectId id;
    private String name;
    private Boolean managed;

    protected Branch(){

    }

    public Boolean getManaged() {
        return managed;
    }
    protected void setManaged(Boolean managed) {
        this.managed = managed;
    }
    protected ObjectId getId() {
        return id;
    }
    protected void setId(ObjectId id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    protected void setName(String name) {
        this.name = name;
    }
}
