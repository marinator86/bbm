package bbm.actions;

import ratpack.exec.Promise;

/**
 * Created by mario on 1/18/17.
 */
public interface Action {

    /**
     * A general action that returns a success flag and a key-value response for a client.
     */
    ActionResult execute(ActionContext context);
}
