package bbm.handlers;

import bbm.actions.*;
import bbm.actions.context.BranchActionContext;
import bbm.actions.context.BuildTriggerActionContext;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.Headers;
import ratpack.http.TypedData;

import java.util.Map;

/**
 * Created by mario on 1/18/17.
 */
public class BranchActionHandler implements Handler {
    private static final String HEADER_EVENT_KEY = "X-Event-Key";
    private static final String HEADER_HOOK_ID = "X-Hook-UUID";
    private static final String HOOK_ID = "a1bb129d-e8a6-4201-9980-3b19a8f6e98e";

    private final static Logger logger = LoggerFactory.getLogger(BranchActionHandler.class);

    private final static Map<String, Class> actionMap = ImmutableMap.of(
            "pullrequest:created", MonitorSyncAction.class,
            "pullrequest:fulfilled", UnmonitorSyncAction.class,
            "pullrequest:rejected", UnmonitorSyncAction.class
    );

    @Override
    public void handle(Context ctx) throws Exception {

        final Headers headers = ctx.getRequest().getHeaders();
        final Boolean eventKeyHeaderNotSet = !headers.contains(HEADER_EVENT_KEY);
        final Boolean eventKeyNotImplemented = !actionMap.containsKey(headers.get(HEADER_EVENT_KEY));
        final Boolean wrongHookUUIdFromBitbucket = !HOOK_ID.equals(headers.get(HEADER_HOOK_ID));

        if(eventKeyHeaderNotSet || eventKeyNotImplemented || wrongHookUUIdFromBitbucket) {
            ctx.clientError(400);
            return;
        }

        Class<SyncAction> action = actionMap.get(headers.get(HEADER_EVENT_KEY));
        Promise<TypedData> body = ctx.getRequest().getBody();

        body.fork().map(typedData -> new JsonParser().parse(typedData.getText()).getAsJsonObject())
        .onError(throwable -> ctx.error(throwable))
        .onNull(() -> ctx.clientError(400))
        .map(root -> extractBranchName(root))
        .map(branchName -> ctx.get(action).apply((BranchActionContext) () -> branchName))
        .onError(throwable -> logger.error("Error in hook execution", throwable))
        .then(actionResult -> ctx.render(actionResult));

        body.fork().map(typedData -> new JsonParser().parse(typedData.getText()).getAsJsonObject())
        .map(root -> {
            String branchName = extractBranchName(root);
            String repoUuid = extractRepoUuid(root);
            ctx.get(BuildTriggerAction.class).execute(getBuildTriggerActionContext(branchName, repoUuid));
            return null;
        })
        .onError(throwable -> logger.error("Error in Buildtrigger exec", throwable));
    }

    private BuildTriggerActionContext getBuildTriggerActionContext(final String branchName, final String repoUuid) {
        return new BuildTriggerActionContext() {
            @Override
            public String getBranchName() {
                return branchName;
            }

            @Override
            public String getRepUuid() {
                return repoUuid;
            }
        };
    }

    private String extractRepoUuid(JsonObject root) {
        return root.getAsJsonObject("repository").get("uuid").getAsString();
    }

    private String extractBranchName(JsonObject root) {
        return root.getAsJsonObject("pullrequest")
                .getAsJsonObject("source")
                .getAsJsonObject("branch")
                .get("name").getAsString();
    }
}
