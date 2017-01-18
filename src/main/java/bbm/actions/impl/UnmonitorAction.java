package bbm.actions.impl;

import bbm.actions.Action;
import bbm.actions.ActionResult;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mario on 1/18/17.
 */
public class UnmonitorAction implements Action {
    private final String branchName;

    public UnmonitorAction(String branchName) {
        this.branchName = branchName;
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
                return ImmutableMap.of(
                        "actionType", "unmonitor",
                        "branchName", branchName
                );
            }
        };
    }
}
