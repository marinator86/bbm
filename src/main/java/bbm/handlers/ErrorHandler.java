package bbm.handlers;

import com.google.gson.JsonObject;
import ratpack.handling.Context;
import ratpack.http.Response;

/**
 * Created by mario on 3/7/17.
 */
public class ErrorHandler implements ratpack.error.internal.ErrorHandler {
    @Override
    public void error(Context context, int statusCode) throws Exception {
        JsonObject object = new JsonObject();
        object.addProperty("success", false);
        Response resp = context.getResponse();
        resp.status(statusCode);
        resp.send(object.getAsString());
    }

    @Override
    public void error(Context context, Throwable throwable) throws Exception {
        JsonObject object = new JsonObject();
        object.addProperty("success", false);
        object.addProperty("message", throwable.getMessage());
        Response resp = context.getResponse();
        resp.send(object.getAsString());
    }
}
