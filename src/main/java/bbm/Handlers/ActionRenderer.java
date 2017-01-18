package bbm.Handlers;

import bbm.actions.ActionResult;
import ratpack.handling.Context;
import ratpack.render.RendererSupport;

/**
 * Created by mario on 1/18/17.
 */
public class ActionRenderer extends RendererSupport<ActionResult> {
    @Override
    public void render(Context ctx, ActionResult actionResult) throws Exception {
        ctx.render("test" + actionResult);
    }
}
