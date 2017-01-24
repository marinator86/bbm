package bbm.handlers;

import bbm.database.sandboxes.Sandbox;
import com.google.gson.JsonObject;
import ratpack.handling.Context;
import ratpack.render.RendererSupport;

import java.util.Optional;

/**
 * Created by mario on 1/19/17.
 */
public class OptionalOrgRenderer extends RendererSupport<Optional<?>> {
    @Override
    public void render(Context ctx, Optional<?> org) throws Exception {
        JsonObject result = new JsonObject();
        if(org.isPresent() && org.get().getClass() == Sandbox.class) {
            result.addProperty("success", true);
            result.addProperty("orgName", ((Sandbox)org.get()).getName());
        } else {
            result.addProperty("success", false);
        }
        ctx.render(result.toString());
    }
}