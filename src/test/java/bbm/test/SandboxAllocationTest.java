package bbm.test;

import bbm.actions.*;
import bbm.actions.context.InstructionActionContext;
import bbm.database.branches.BranchModule;
import bbm.database.branches.Branches;
import bbm.database.orgs.Org;
import bbm.database.orgs.OrgModule;
import bbm.database.orgs.Orgs;
import bbm.database.sandboxes.SandboxModule;
import bbm.database.sandboxes.Sandboxes;
import bbm.salesforce.SalesforceModule;
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
                new OrgModule(),
                new BranchModule(),
                new SandboxModule(),
                new ActionModule(),
                new SalesforceModule()
        );
        Datastore datastore = injector.getInstance(Datastore.class);

        Orgs orgs = injector.getInstance(Orgs.class);
        Org testOrg = orgs.createOrg("testParentOrgId");

        Sandboxes sandboxes = injector.getInstance(Sandboxes.class);
        sandboxes.createSandbox("org1", "adssd", testOrg);
        sandboxes.createSandbox("org2", "adsse", testOrg);
        //sandboxes.createSandbox("org3", "adssf", testOrg);

        Branches branches = injector.getInstance(Branches.class);
        branches.createManagedBranch("branch1");
        sandboxes.setBranch(sandboxes.getFreeSandbox().get(), branches.getBranch("branch1").get());
        branches.createManagedBranch("branch2");
        sandboxes.setBranch(sandboxes.getFreeSandbox().get(), branches.getBranch("branch2").get());

        InstructionAction credAction = injector.getInstance(InstructionAction.class);
        ActionResult result1 = credAction.apply(getInstructionActionContext("branch1"));
        ActionResult result2 = credAction.apply(getInstructionActionContext("branch2"));
        Assert.assertEquals(true, result1.getSuccess());
        Assert.assertEquals(true, result2.getSuccess());
        Assert.assertEquals(false, sandboxes.getFreeSandbox().isPresent());

        UnmonitorSyncAction unmonitorAction = injector.getInstance(UnmonitorSyncAction.class);
        ActionResult result3 = unmonitorAction.apply(() -> "branch1");
        Assert.assertEquals(true, result3.getSuccess());
        Assert.assertEquals(true, sandboxes.getFreeSandbox().isPresent());

        ActionResult result4 = credAction.apply(getInstructionActionContext("branch1"));
        Assert.assertEquals(true, result4.getSuccess());
        Assert.assertEquals(true, sandboxes.getFreeSandbox().isPresent());

        branches.createManagedBranch("branch3");
        sandboxes.setBranch(sandboxes.getFreeSandbox().get(), branches.getBranch("branch3").get());

        ActionResult result5 = credAction.apply(getInstructionActionContext("branch3"));
        Assert.assertEquals(true, result5.getSuccess());
        Assert.assertEquals(false, sandboxes.getFreeSandbox().isPresent());
    }

    private InstructionActionContext getInstructionActionContext(String branch) {
        return new InstructionActionContext() {
            @Override
            public String getRepositoryUID() {
                return "123";
            }

            @Override
            public String getBranchName() {
                return branch;
            }
        };
    }
}