package bbm.test;

import bbm.actions.*;
import bbm.database.branches.BranchModule;
import bbm.database.branches.Branches;
import bbm.database.orgs.OrgModule;
import bbm.database.orgs.Orgs;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;
import org.mongodb.morphia.Datastore;

/**
 * Created by mario on 1/19/17.
 */
public class SandboxAllocationTest {

    @Test
    public void testRetrieval() {
        Injector injector = Guice.createInjector(
                new DatabaseModule()
        );
        Datastore datastore = injector.getInstance(Datastore.class);
        Assert.assertEquals(datastore.getDB().getName(), "morphia_test_bbm");
    }

    @Test
    public void test() {
        Injector injector = Guice.createInjector(
                new DatabaseModule(),
                new BranchModule(),
                new OrgModule(),
                new ActionModule()
        );
        Datastore datastore = injector.getInstance(Datastore.class);

        Branches branches = injector.getInstance(Branches.class);
        branches.createManagedBranch("branch1");
        branches.createManagedBranch("branch2");
        branches.createManagedBranch("branch3");

        Orgs orgs = injector.getInstance(Orgs.class);
        orgs.createOrg("org1");
        orgs.createOrg("org2");

        ReceiveCredentialsAction credAction = injector.getInstance(ReceiveCredentialsAction.class);
        ActionResult result1 = credAction.execute(() -> "branch1");
        ActionResult result2 = credAction.execute(() -> "branch2");
        Assert.assertEquals(true, result1.getSuccess());
        Assert.assertEquals(true, result2.getSuccess());
        Assert.assertEquals(false, orgs.getFreeOrg().isPresent());

        UnmonitorAction unmonitorAction = injector.getInstance(UnmonitorAction.class);
        ActionResult result3 = unmonitorAction.execute(() -> "branch1");
        Assert.assertEquals(true, result3.getSuccess());
        Assert.assertEquals(true, orgs.getFreeOrg().isPresent());

        ActionResult result4 = credAction.execute(() -> "branch1");
        Assert.assertEquals(false, result4.getSuccess());
        Assert.assertEquals(true, orgs.getFreeOrg().isPresent());

        ActionResult result5 = credAction.execute(() -> "branch3");
        Assert.assertEquals(true, result5.getSuccess());
        Assert.assertEquals(false, orgs.getFreeOrg().isPresent());
    }
}