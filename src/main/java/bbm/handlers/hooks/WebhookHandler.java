package bbm.handlers.hooks;

import bbm.actions.BuildTriggerAction;
import bbm.actions.HookAction;
import bbm.actions.context.HookActionContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Promise;
import ratpack.func.Action;
import ratpack.func.Function;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.Headers;
import ratpack.http.Request;
import ratpack.http.TypedData;

import java.util.Map;

/**
 * Created by mario on 2/16/17.
 */
public abstract class WebhookHandler implements Handler{
    protected final static Logger logger = LoggerFactory.getLogger(BitbucketWebhookHandler.class);

    @Inject
    private Map<HookAction.Types, HookAction> hookActions;

    @Override
    public void handle(Context ctx) throws Exception {
        logger.info("Executing web hook: " + ctx.getRequest().getRemoteAddress());
        final Request request = ctx.getRequest();
        final Headers headers = request.getHeaders();
        if(!checkRequest(headers)) {
            ctx.clientError(400);
            return;
        }

        HookAction.Types action = getHookActionType(headers);
        Promise<TypedData> body = request.getBody();

        body.fork().map(typedData -> new JsonParser().parse(typedData.getText()).getAsJsonObject())
        .onError(throwable -> ctx.error(throwable))
        .onNull(() -> ctx.clientError(400))
        .map(hookActionContextCreator())
        .right(hookActions.get(action))
        .onError(throwable -> logger.error("Error in hook execution", throwable))
        .next(resultPair -> ctx.render(resultPair.right()))
        .route(actionResult -> !actionResult.right().getSuccess(), pair -> logger.error("Error in webhook handling. Skipping build trigger."))
        .map(resultPair -> resultPair.left())
        .next(actionContext -> logger.info("Executing build trigger: " + actionContext.getBranchName() + ", repo: " + actionContext.getRepoUuid()))
        .next(ctx.get(BuildTriggerAction.class))
        .onError(throwable -> logger.error("Error during build trigger execution: ", throwable))
        .then(Action.noop());
    }

    protected abstract Boolean checkRequest(Headers headers);

    protected abstract HookAction.Types getHookActionType(Headers headers);

    protected abstract Function<JsonObject, HookActionContext> hookActionContextCreator();

}
