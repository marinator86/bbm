package bbm.Handlers;

import bbm.actions.ActionResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ratpack.handling.Context;
import ratpack.render.RendererSupport;

/**
 * Created by mario on 1/18/17.
 */
public class ActionRenderer extends RendererSupport<ActionResult> {
    @Override
    public void render(Context ctx, ActionResult actionResult) throws Exception {
        JsonObject result = new JsonObject();
        result.addProperty("success", actionResult.getSuccess());
        result.addProperty("payload", new Gson().toJson(actionResult.getPayload()));
        ctx.render(result.toString());
    }
}
