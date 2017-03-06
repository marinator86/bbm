package bbm.test.handlers;

import bbm.database.repositories.Repositories;
import bbm.database.repositories.RepositoryModule;
import bbm.handlers.GetRepositoriesHandler;
import bbm.handlers.renderer.RepositoryListRenderer;
import bbm.test.DatabaseModule;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;
import ratpack.guice.Guice;
import ratpack.http.client.ReceivedResponse;
import ratpack.test.embed.EmbeddedApp;

/**
 * Created by mario on 2/27/17.
 */
public class RepositoriesTest {

    @Test
    public void testBuildTriggerAction() throws Exception {
        Injector injector = com.google.inject.Guice.createInjector(
            new DatabaseModule(),
            new RepositoryModule()
        );

        Repositories repos = injector.getInstance(Repositories.class);
        repos.createRepository("test1", "uuid1", Repositories.Provider.BITBUCKET);
        repos.createRepository("test2", "uuid2", Repositories.Provider.BITBUCKET);

        EmbeddedApp app = EmbeddedApp.fromHandler(new GetRepositoriesHandler(repos));

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
            System.out.print(res.getBody().getText());
            JsonArray result = new JsonParser().parse(res.getBody().getText()).getAsJsonArray();
            Assert.assertEquals(2, result.size());

        });

    }

}
