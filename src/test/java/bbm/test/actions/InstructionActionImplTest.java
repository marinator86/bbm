package bbm.test.actions;

import bbm.actions.InstructionAction;
import bbm.actions.impl.InstructionActionImpl;
import bbm.database.branches.Branch;
import bbm.database.branches.BranchModule;
import bbm.database.branches.Branches;
import bbm.database.repositories.Repositories;
import bbm.database.repositories.Repository;
import bbm.database.repositories.RepositoryModule;
import bbm.database.sandboxes.Sandbox;
import bbm.database.sandboxes.SandboxModule;
import bbm.database.sandboxes.Sandboxes;
import bbm.handlers.ActionRenderer;
import bbm.handlers.InstructionActionHandler;
import bbm.test.DatabaseModule;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;
import ratpack.guice.Guice;
import ratpack.http.client.ReceivedResponse;
import ratpack.test.embed.EmbeddedApp;

/**
 * Created by mario on 3/15/17.
 */
public class InstructionActionImplTest {

    @Test
    public void testGetRepositories() throws Exception {
        Injector injector = com.google.inject.Guice.createInjector(
                new DatabaseModule(),
                new RepositoryModule(),
                new SandboxModule(),
                new BranchModule()
        );

        Sandboxes sandboxes = injector.getInstance(Sandboxes.class);
        sandboxes.createSandbox("testSandbox", "a0b", null);
        Sandbox sandbox = sandboxes.getSandbox("a0b").get();

        Repositories repos = injector.getInstance(Repositories.class);
        repos.createRepository("test1", "uuid1", Repositories.Provider.BITBUCKET);

        Branches branches = injector.getInstance(Branches.class);
        Repository repository = repos.getRepository("uuid1").get();

        branches.createManagedBranch("testBranch", repository, "2d89ab7b624d4533b351166508e453ada491acff");
        Branch branch = branches.getBranch("testBranch", repository).get();

        sandboxes.setBranch(sandbox, branch);

        EmbeddedApp.of(ratpackServerSpec -> {
            ratpackServerSpec.registry(Guice.registry(b -> {
                b.bindInstance(Repositories.class, repos);
                b.bindInstance(Sandboxes.class, sandboxes);
                b.bindInstance(Branches.class, branches);
                b.bind(InstructionAction.class, InstructionActionImpl.class);
                b.bind(InstructionActionHandler.class);
                b.bind(ActionRenderer.class);
            }));

            ratpackServerSpec.handlers(chain -> {
                chain.get("instruct/:repositoryUID/:branchName/:commit?", InstructionActionHandler.class);
            });
        }).test(testHttpClient -> {
            ReceivedResponse res = testHttpClient.get("instruct/uuid1/testBranch");
            System.out.println(res.getBody().getText());
            Assert.assertEquals(200, res.getStatusCode());
            Assert.assertEquals("CLEANDEPLOY", getBuildType(res.getBody().getText()));

            res = testHttpClient.get("instruct/uuid1/testBranch1/2d89ab7b6");
            System.out.println(res.getBody().getText());
            Assert.assertEquals(200, res.getStatusCode());
            Assert.assertEquals("NOBUILD", getBuildType(res.getBody().getText()));

            res = testHttpClient.get("instruct/uuid1/testBranch/2d89ab7b6");
            System.out.println(res.getBody().getText());
            Assert.assertEquals(200, res.getStatusCode());
            Assert.assertEquals("CLEANDEPLOY", getBuildType(res.getBody().getText()));

            res = testHttpClient.get("instruct/uuid1/testBranch/f2d89ab7b6");
            System.out.println(res.getBody().getText());
            Assert.assertEquals(200, res.getStatusCode());
            Assert.assertEquals("DEPLOY", getBuildType(res.getBody().getText()));
        });
    }

    private String getBuildType(String response){
        JsonObject root = new JsonParser().parse(response).getAsJsonObject();
        return root.get("payload").getAsJsonObject().get("buildType").getAsString();
    }
}
