package bbm.test.handlers;

import bbm.database.branches.Branch;
import bbm.database.branches.BranchModule;
import bbm.database.branches.Branches;
import bbm.database.repositories.Repositories;
import bbm.database.repositories.Repository;
import bbm.database.repositories.RepositoryModule;
import bbm.handlers.DeleteRepositoryHandler;
import bbm.handlers.GetRepositoriesHandler;
import bbm.handlers.GetRepositoryBranchesHandler;
import bbm.handlers.PostRepositoryHandler;
import bbm.handlers.renderer.BranchesListRenderer;
import bbm.handlers.renderer.RepositoryListRenderer;
import bbm.handlers.renderer.RepositoryRenderer;
import bbm.test.DatabaseModule;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;
import ratpack.guice.Guice;
import ratpack.http.client.ReceivedResponse;
import ratpack.http.client.RequestSpec;
import ratpack.test.embed.EmbeddedApp;

/**
 * Created by mario on 2/27/17.
 */
public class RepositoriesTest {

    @Test
    public void testGetRepositories() throws Exception {
        Injector injector = com.google.inject.Guice.createInjector(
            new DatabaseModule(),
            new RepositoryModule()
        );

        Repositories repos = injector.getInstance(Repositories.class);
        repos.createRepository("test1", "uuid1", Repositories.Provider.BITBUCKET);
        repos.createRepository("test2", "uuid2", Repositories.Provider.BITBUCKET);

        EmbeddedApp.of(ratpackServerSpec -> {
            ratpackServerSpec.registry(Guice.registry(b -> {
                b.bindInstance(Repositories.class, repos);
                b.bind(GetRepositoriesHandler.class);
                b.bind(RepositoryListRenderer.class);
            }));

            ratpackServerSpec.handlers(chain -> {
                chain.get("repositories",GetRepositoriesHandler.class);
            });
        }).test(testHttpClient -> {
            ReceivedResponse res = testHttpClient.get("repositories");
            Assert.assertEquals(200, res.getStatusCode());
            System.out.println(res.getBody().getText());
            JsonArray result = new JsonParser().parse(res.getBody().getText()).getAsJsonArray();
            Assert.assertEquals(2, result.size());

        });

    }

    @Test
    public void testPostRepositories() throws Exception {
        Injector injector = com.google.inject.Guice.createInjector(
            new DatabaseModule(),
            new RepositoryModule()
        );

        Repositories repos = injector.getInstance(Repositories.class);

        EmbeddedApp.of(ratpackServerSpec -> {
            ratpackServerSpec.registry(Guice.registry(b -> {
                b.bindInstance(Repositories.class, repos);
                b.bind(PostRepositoryHandler.class);
                b.bind(RepositoryRenderer.class);
            }));

            ratpackServerSpec.handlers(chain -> {
                chain.post("repositories/:uuid",PostRepositoryHandler.class);
            });
        }).test(testHttpClient -> {
            ReceivedResponse res = testHttpClient.request("repositories/123456a", requestSpec -> {
                RequestSpec.Body body = requestSpec.getBody();
                requestSpec.post();

                JsonObject requestBody = new JsonObject();
                requestBody.addProperty("name", "test");
                body.text(requestBody.toString());
            });

            Assert.assertEquals(200, res.getStatusCode());
            System.out.println(res.getBody().getText());
            JsonObject result = new JsonParser().parse(res.getBody().getText()).getAsJsonObject();
            Assert.assertEquals(3, result.size());

            Assert.assertEquals(1, repos.getRepositories().size());
            Assert.assertEquals("123456a", repos.getRepositories().get(0).getUuid());
        });

    }

    @Test
    public void testDeleteRepositories() throws Exception {
        Injector injector = com.google.inject.Guice.createInjector(
                new DatabaseModule(),
                new RepositoryModule()
        );

        Repositories repos = injector.getInstance(Repositories.class);
        repos.createRepository("test1", "123456a", Repositories.Provider.BITBUCKET);
        Assert.assertTrue(repos.getRepository("123456a").isPresent());

        EmbeddedApp.of(ratpackServerSpec -> {
            ratpackServerSpec.registry(Guice.registry(b -> {
                b.bindInstance(Repositories.class, repos);
                b.bind(DeleteRepositoryHandler.class);
            }));

            ratpackServerSpec.handlers(chain -> {
                chain.delete("repositories/:uuid", DeleteRepositoryHandler.class);
            });
        }).test(testHttpClient -> {
            ReceivedResponse res = testHttpClient.delete("repositories/123456b");
            Assert.assertEquals(404, res.getStatusCode());

            res = testHttpClient.delete("repositories/123456a");
            Assert.assertEquals(200, res.getStatusCode());
        });

    }

    @Test
    public void testGetRepositoryBranches() throws Exception {
        Injector injector = com.google.inject.Guice.createInjector(
                new DatabaseModule(),
                new BranchModule(),
                new RepositoryModule()
        );

        Repositories repos = injector.getInstance(Repositories.class);
        repos.createRepository("test1", "123456a", Repositories.Provider.BITBUCKET);
        Repository repo = repos.getRepository("123456a").get();

        Branches branches = injector.getInstance(Branches.class);
        branches.createManagedBranch("testBranch1", repo, "a0b");
        branches.createManagedBranch("testBranch2", repo, "a0b");

        EmbeddedApp.of(ratpackServerSpec -> {
            ratpackServerSpec.registry(Guice.registry(b -> {
                b.bindInstance(Repositories.class, repos);
                b.bindInstance(Branches.class, branches);
                b.bind(GetRepositoryBranchesHandler.class);
                b.bind(BranchesListRenderer.class);
            }));

            ratpackServerSpec.handlers(chain -> {
                chain.prefix("repositories", repositories ->
                    repositories.prefix(":uuid", repository ->
                        repository.get("branches", GetRepositoryBranchesHandler.class)));
            });
        }).test(testHttpClient -> {
            ReceivedResponse res = testHttpClient.get("repositories/123456ab/branches");
            Assert.assertEquals(404, res.getStatusCode());

            res = testHttpClient.get("repositories/123456a/branches");
            Assert.assertEquals(200, res.getStatusCode());
            JsonArray result = new JsonParser().parse(res.getBody().getText()).getAsJsonArray();
            Assert.assertEquals(2, result.size());
            Assert.assertEquals("testBranch1", result.get(0).getAsJsonObject().get("name").getAsString());
            Assert.assertEquals("testBranch2", result.get(1).getAsJsonObject().get("name").getAsString());
        });

    }
}
