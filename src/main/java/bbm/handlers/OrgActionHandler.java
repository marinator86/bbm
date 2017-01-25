package bbm.handlers;

import bbm.actions.SandboxSyncAction;
import bbm.actions.context.OrgActionContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;

/**
 * Created by mario on 1/24/17.
 */
public class OrgActionHandler implements Handler{
    private final static Logger logger = LoggerFactory.getLogger(OrgActionHandler.class);

    @Override
    public void handle(Context ctx) throws Exception {
        ctx.getRequest().getBody().map(typedData -> {
            logger.debug(typedData.getText());
            final JsonParser parser = new JsonParser();
            JsonObject payload = parser.parse(typedData.getText()).getAsJsonObject();
            String username = payload.get("username").getAsString();
            String password = payload.get("password").getAsString();
            String token = payload.get("token").getAsString();
            String sandboxPrefix = payload.get("sandboxPrefix").getAsString();
            return getOrgActionContext(username, password, sandboxPrefix, token, ctx);
        })
        .onError(throwable -> ctx.error(throwable))
        .onNull(() -> ctx.clientError(400))
        .then(orgActionContext -> ctx.get(SandboxSyncAction.class).apply(orgActionContext));
    }

    private OrgActionContext getOrgActionContext(String username, String password, String sandboxPrefix, String token, Context ctx) {
        return new OrgActionContext() {
            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public String getPassword() {
                return password;
            }

            @Override
            public String getToken() {
                return token;
            }

            @Override
            public String getSandboxPrefix() {
                return sandboxPrefix;
            }

            @Override
            public Context getContext() {
                return ctx;
            }
        };
    }
}
