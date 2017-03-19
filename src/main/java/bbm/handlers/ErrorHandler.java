package bbm.handlers;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.error.ClientErrorHandler;
import ratpack.handling.Context;
import ratpack.http.Response;

/**
 * Created by mario on 3/7/17.
 */
public class ErrorHandler implements ratpack.error.internal.ErrorHandler {
    private final static Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    public void error(Context context, int statusCode) throws Exception {
        JsonObject object = new JsonObject();
        object.addProperty("success", false);
        Response resp = context.getResponse();
        resp.status(statusCode);
        logger.info("Retrieved error: " + statusCode);
        resp.send(object.toString());
    }

    @Override
    public void error(Context context, Throwable throwable) throws Exception {
        JsonObject object = new JsonObject();
        object.addProperty("success", false);
        object.addProperty("exceptionMessage", throwable.getMessage());
        Response resp = context.getResponse();
        resp.status(500);
        logger.info("Retrieved error");
        logger.info(throwable.getClass().toString());
        logger.info(throwable.getMessage());
        for(StackTraceElement element : throwable.getStackTrace())
            logger.info(element.toString());
        logger.info("Cause: " + throwable.getCause().getClass().toString());
        resp.send(object.toString());
    }
}
