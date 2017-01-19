package bbm.handlers;

import bbm.database.orgs.Org;
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
        if(org.isPresent() && org.get().getClass() == Org.class) {
            result.addProperty("success", true);
            result.addProperty("orgName", ((Org)org.get()).getName());
        } else {
            result.addProperty("success", false);
        }
        ctx.render(result.toString());
    }
}