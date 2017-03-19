package bbm;

import bbm.actions.buildtrigger.BuildTriggerModule;
import bbm.database.orgs.OrgModule;
import bbm.database.repositories.RepositoryModule;
import bbm.handlers.*;
import bbm.actions.ActionModule;
import bbm.database.DatabaseModule;
import bbm.database.sandboxes.SandboxModule;
import bbm.database.branches.BranchModule;
import bbm.handlers.hooks.BitbucketWebhookHandler;
import bbm.handlers.renderer.BranchesListRenderer;
import bbm.handlers.renderer.RepositoryListRenderer;
import bbm.handlers.renderer.RepositoryRenderer;
import bbm.salesforce.SalesforceModule;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.pac4j.RatpackPac4j;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;
import ratpack.groovy.template.TextTemplateModule;
import ratpack.guice.Guice;
import ratpack.session.SessionModule;

import static ratpack.groovy.Groovy.groovyTemplate;
import static java.util.Collections.singletonMap;

public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) throws Exception {

        RatpackServer.start(s -> s
            .serverConfig(c -> c
                .baseDir(BaseDir.find())
                .env())

            .registry(Guice.registry(b -> {
                b.module(SessionModule.class);
                b.module(TextTemplateModule.class, conf -> conf.setStaticallyCompile(true));
                b.module(DatabaseModule.class);
                b.module(BuildTriggerModule.class);
                b.module(RepositoryModule.class);
                b.module(OrgModule.class);
                b.module(SandboxModule.class);
                b.module(BranchModule.class);
                b.module(ActionModule.class);
                b.module(SalesforceModule.class);
                b.bind(BitbucketWebhookHandler.class);
                b.bind(OrgActionHandler.class);
                b.bind(ActionRenderer.class);
                b.bind(RepositoryListRenderer.class);
                b.bind(RepositoryRenderer.class);
                b.bind(OptionalOrgRenderer.class);
                b.bind(InstructionActionHandler.class);
                b.bind(GetRepositoriesHandler.class);
                b.bind(PostRepositoryHandler.class);
                b.bind(DeleteRepositoryHandler.class);
                b.bind(GetRepositoryBranchesHandler.class);
                b.bind(BranchesListRenderer.class);
                b.bind(ErrorHandler.class);
            }))

            .handlers(chain -> {
                final DirectBasicAuthClient directBasicAuthClient = new DirectBasicAuthClient(new SimpleTestUsernamePasswordAuthenticator());
                chain
                    .all(RatpackPac4j.authenticator(directBasicAuthClient))
                    .all(RatpackPac4j.requireAuth(DirectBasicAuthClient.class))

                    .get(ctx -> ctx.render(groovyTemplate("index.html")))
                    .get("instruct/:repositoryUID/:branchName/:commit?", InstructionActionHandler.class)
                    .prefix("admin", admin -> {
                        admin.path("sync", OrgActionHandler.class);
                    })
                    .prefix("hooks", hookChain -> {
                        hookChain.post("bitbucket", BitbucketWebhookHandler.class);
                    })
                    .prefix("repositories", repositories -> {
                        repositories.get(GetRepositoriesHandler.class);
                        repositories.prefix(":uuid", repository ->{
                            repository.delete(DeleteRepositoryHandler.class);
                            repository.post(PostRepositoryHandler.class);
                            repository.get("branches", GetRepositoryBranchesHandler.class);
                        });

                    })
                    .files(f -> f.dir("public"));
            })
        );
    }
}