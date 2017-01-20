package bbm;

import bbm.handlers.ActionHandler;
import bbm.handlers.ActionRenderer;
import bbm.handlers.AuthHandler;
import bbm.handlers.OptionalOrgRenderer;
import bbm.actions.ActionModule;
import bbm.database.DatabaseModule;
import bbm.database.orgs.OrgModule;
import bbm.database.branches.BranchModule;
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
                b.module(OrgModule.class);
                b.module(BranchModule.class);
                b.module(ActionModule.class);
                b.bind(AuthHandler.class);
                b.bind(ActionHandler.class);
                b.bind(ActionRenderer.class);
                b.bind(OptionalOrgRenderer.class);
            }))



            .handlers(chain -> {
                final FormClient formClient = new FormClient("/loginForm.html", new SimpleTestUsernamePasswordAuthenticator());
                chain
                    .all(RatpackPac4j.authenticator("callback", formClient))

                    .get(ctx -> ctx.render(groovyTemplate("index.html")))
                    .prefix("admin", protectedchain -> {
                        protectedchain
                            .all(RatpackPac4j.requireAuth(FormClient.class))
                            .path("index.html", ctx -> ctx.render("protected admin"));
                    })
                    .post("hooks", ActionHandler.class)
                    .path("loginForm.html", ctx ->
                            ctx.render(groovyTemplate(
                                    singletonMap("callbackUrl", formClient.getCallbackUrl()),
                                    "loginForm.html"
                            ))
                        )
                    .files(f -> f.dir("public"));
            })
        );
    }
}