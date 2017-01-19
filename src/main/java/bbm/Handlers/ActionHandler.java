package bbm.Handlers;

import bbm.actions.*;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.Headers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mario on 1/18/17.
 */
public class ActionHandler implements Handler {
    private static final String HEADER_EVENT_KEY = "X-Event-Key";
    private static final String HEADER_HOOK_ID = "X-Hook-UUID";
    private static final String HOOK_ID = "a1bb129d-e8a6-4201-9980-3b19a8f6e98e";

    private final static Logger logger = LoggerFactory.getLogger(ActionHandler.class);

    private final static Map<String, Class> actionMap = ImmutableMap.of(
            "pullrequest:created", MonitorAction.class,
            "pullrequest:fulfilled", UnmonitorAction.class,
            "pullrequest:rejected", UnmonitorAction.class
    );

    @Override
    public void handle(Context ctx) throws Exception {

        final Headers headers = ctx.getRequest().getHeaders();
        final Boolean eventKeyHeaderNotSet = !headers.contains(HEADER_EVENT_KEY);
        final Boolean eventKeyNotImplemented = !actionMap.containsKey(headers.get(HEADER_EVENT_KEY));
        final Boolean wrongHookUUIdFromBitbucket = !HOOK_ID.equals(headers.get(HEADER_HOOK_ID));

        if(eventKeyHeaderNotSet || eventKeyNotImplemented || wrongHookUUIdFromBitbucket) ctx.clientError(400);

        Class<Action> action = actionMap.get(headers.get(HEADER_EVENT_KEY));
        ctx.getRequest().getBody().map(typedData -> {
            final JsonParser parser = new JsonParser();
            return parser.parse(typedData.getText())
                    .getAsJsonObject()
                    .getAsJsonObject("pullrequest")
                    .getAsJsonObject("source")
                    .getAsJsonObject("branch")
                    .get("name").getAsString();
        })
        .onError(throwable -> ctx.error(throwable))
        .onNull(() -> ctx.clientError(400))
        .map(branchName -> ctx.get(action).execute(() -> branchName))
        .then(actionResult -> ctx.render(actionResult));
    }
}
