package bbm.actions.impl;

import bbm.actions.Action;
import bbm.actions.ActionResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mario on 1/18/17.
 */
public class UnmonitorAction implements Action {
    public UnmonitorAction(String branchName) {
    }

    @Override
    public ActionResult execute() {
        return new ActionResult() {
            @Override
            public Boolean getSuccess() {
                return true;
            }

            @Override
            public Map<String, String> getPayload() {
                return new HashMap<>();
            }
        };
    }
}
