package bbm.actions;

import java.util.Map;

/**
 * Created by mario on 1/18/17.
 */
public interface ActionResult {
    Boolean getSuccess();
    Map<String, String> getPayload();
}
