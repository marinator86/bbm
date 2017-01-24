package bbm.handlers;

import bbm.actions.SandboxSyncAction;
import bbm.actions.context.OrgActionContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ratpack.handling.Context;
import ratpack.handling.Handler;

/**
 * Created by mario on 1/24/17.
 */
public class OrgActionHandler implements Handler{
    @Override
    public void handle(Context ctx) throws Exception {
        ctx.getRequest().getBody().map(typedData -> {
            final JsonParser parser = new JsonParser();
            JsonObject payload = parser.parse(typedData.getText()).getAsJsonObject();
            String sessionId = payload.get("sessionId").getAsString();
            String sandboxPrefix = payload.get("sandboxPrefix").getAsString();
            return getOrgActionContext(sessionId, sandboxPrefix);
        })
        .onError(throwable -> ctx.error(throwable))
        .onNull(() -> ctx.clientError(400))
        .map(orgActionContext -> ctx.get(SandboxSyncAction.class).apply(orgActionContext))
        .then(actionResult -> ctx.render(actionResult));
    }

    private OrgActionContext getOrgActionContext(String sessionId, String sandboxPrefix) {
        return new OrgActionContext() {
            @Override
            public String getSessionId() {
                return sessionId;
            }

            @Override
            public String getSandboxPrefix() {
                return sandboxPrefix;
            }
        };
    }
}
