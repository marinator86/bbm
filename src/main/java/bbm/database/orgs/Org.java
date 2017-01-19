package bbm.database.orgs;

import bbm.database.branches.Branch;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

/**
 * Created by mario on 1/19/17.
 */
@Entity("orgs")
public final class Org {
    @Id
    private ObjectId id;
    private String name;

    @Reference
    private Branch currentBranch;

    protected Org(){
    }

    protected ObjectId getId() {
        return id;
    }
    protected void setId(ObjectId id) {
        this.id = id;
    }
    public Branch getCurrentBranch() {
        return currentBranch;
    }
    protected void setCurrentBranch(Branch currentBranch) {
        this.currentBranch = currentBranch;
    }
    public String getName() {
        return name;
    }
    protected void setName(String name) {
        this.name = name;
    }
}
