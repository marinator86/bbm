package bbm.test.handlers.ErrorHandling;

import ratpack.handling.Context;
import ratpack.handling.Handler;

/**
 * Created by mario on 3/8/17.
 */
class ClientErrorHandler implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        ctx.clientError(400);
    }
}
