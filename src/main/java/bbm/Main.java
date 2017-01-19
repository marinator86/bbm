package bbm;

import bbm.Handlers.ActionHandler;
import bbm.Handlers.ActionRenderer;
import bbm.Handlers.AuthHandler;
import bbm.Handlers.OptionalOrgRenderer;
import bbm.actions.ActionModule;
import bbm.database.DatabaseModule;
import bbm.database.orgs.OrgModule;
import bbm.database.branches.BranchModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;
import ratpack.groovy.template.TextTemplateModule;
import ratpack.guice.Guice;

import static ratpack.groovy.Groovy.groovyTemplate;

public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) throws Exception {
        RatpackServer.start(s -> s
            .serverConfig(c -> c
                .baseDir(BaseDir.find())
                .env())

            .registry(Guice.registry(b -> {
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



            .handlers(chain -> chain
                .get(ctx -> ctx.render(groovyTemplate("index.html")))

                .get("hello", ctx -> {
                  ctx.render("Hello!");
                })

                .get("db", ctx -> {
                  ctx.render("Hello db!");
                })

                .post("hooks", ActionHandler.class)

                .files(f -> f.dir("public"))
            )
        );
    }
}