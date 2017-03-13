package bbm.test.handlers.ErrorHandling;

import ratpack.handling.Context;
import ratpack.handling.Handler;

/**
 * Created by mario on 3/8/17.
 */
class ThrowErrorHandler implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        throw new Exception("test");
    }
}
