package bbm.handlers.hooks;

import bbm.actions.*;
import bbm.actions.context.HookActionContext;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.func.Function;
import ratpack.http.Headers;

import java.util.Map;

/**
 * Created by mario on 1/18/17.
 */
public class BitbucketWebhookHandler extends WebhookHandler {
    protected final static Logger logger = LoggerFactory.getLogger(BitbucketWebhookHandler.class);

    private static final String HEADER_EVENT_KEY = "X-Event-Key";

    private final static Map<String, HookAction.Types> actionMap = ImmutableMap.of(
            "pullrequest:created", HookAction.Types.MONITOR,
            "pullrequest:fulfilled", HookAction.Types.UNMONITOR,
            "pullrequest:rejected", HookAction.Types.UNMONITOR
    );

    @Override
    protected Boolean checkRequest(Headers headers) {
        final Boolean eventKeyHeaderNotSet = !headers.contains(HEADER_EVENT_KEY);
        final Boolean eventKeyNotImplemented = !actionMap.containsKey(headers.get(HEADER_EVENT_KEY));
        if(eventKeyHeaderNotSet || eventKeyNotImplemented){
            logger.info("Request not ok! Header \"X-Event-Key\" set properly?");
            logger.info(headers.toString());
            return false;
        }
        return true;
    }

    @Override
    protected HookAction.Types getHookActionType(Headers headers) {
        return actionMap.get(headers.get(HEADER_EVENT_KEY));
    }

    @Override
    protected Function<JsonObject, HookActionContext> hookActionContextCreator() {
        return jsonObject -> new HookActionContext() {
            @Override
            public String getRepoUuid() {
                return jsonObject.getAsJsonObject("repository").get("uuid").getAsString();
            }

            @Override
            public String getBranchName() {
                return jsonObject.getAsJsonObject("pullrequest")
                        .getAsJsonObject("source")
                        .getAsJsonObject("branch")
                        .get("name").getAsString();
            }
        };
    }
}
