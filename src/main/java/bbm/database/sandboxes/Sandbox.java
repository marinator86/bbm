package bbm.database.sandboxes;

import bbm.database.branches.Branch;
import bbm.database.orgs.Org;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

/**
 * Created by mario on 1/19/17.
 */
@Entity("sandboxes")
public final class Sandbox {
    @Id
    private ObjectId id;
    private String name;

    @Reference
    private Branch currentBranch;

    @Reference
    private Org org;

    protected Sandbox(){
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
    public Org getOrg() {
        return org;
    }
    protected void setOrg(Org org) {
        this.org = org;
    }
}
