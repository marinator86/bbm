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
import bbm.handlers.renderer.RepositoryListRenderer;
import bbm.salesforce.SalesforceModule;
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
                b.bind(OptionalOrgRenderer.class);
                b.bind(InstructionActionHandler.class);
                b.bind(GetRepositoriesHandler.class);
            }))

            .handlers(chain -> {
                final FormClient formClient = new FormClient("/loginForm.html", new SimpleTestUsernamePasswordAuthenticator());
                chain
                    .all(RatpackPac4j.authenticator("callback", formClient))

                    .get(ctx -> ctx.render(groovyTemplate("index.html")))
                    .get("instruct/:repositoryUID/:branchName", InstructionActionHandler.class)
                    .prefix("admin", protectedchain -> {
                        protectedchain
                            .all(RatpackPac4j.requireAuth(FormClient.class))
                            .path("index.html", ctx -> ctx.render("protected admin"))
                            .path("sync", OrgActionHandler.class);
                    })
                    .post("hooks", BitbucketWebhookHandler.class)
                    .path("loginForm.html", ctx ->
                        ctx.render(groovyTemplate(
                                singletonMap("callbackUrl", formClient.getCallbackUrl()),
                                "loginForm.html"
                        ))
                    )
                    .prefix("repositories", repoChain -> {
                        repoChain.get(GetRepositoriesHandler.class);
                    })
                    .files(f -> f.dir("public"));
            })
        );
    }
}