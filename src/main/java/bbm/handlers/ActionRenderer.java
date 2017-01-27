package bbm.handlers;

import bbm.actions.ActionResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ratpack.handling.Context;
import ratpack.render.RendererSupport;

import java.util.Map;

/**
 * Created by mario on 1/18/17.
 */
public class ActionRenderer extends RendererSupport<ActionResult> {
    @Override
    public void render(Context ctx, ActionResult actionResult) throws Exception {
        JsonObject result = new JsonObject();
        result.addProperty("success", actionResult.getSuccess());
        Map<String, String> payload = actionResult.getPayload();
        JsonObject payloadObject = new JsonObject();
        for(String key : payload.keySet()){
            payloadObject.addProperty(key, payload.get(key).toString());
        }
        result.add("payload", payloadObject);
        ctx.render(result.toString());
    }
}
