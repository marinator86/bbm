package bbm.actions.impl;

import bbm.actions.Action;
import bbm.actions.ActionContext;
import bbm.actions.ActionResult;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by mario on 1/18/17.
 */
public class MonitorAction implements Action {

    public MonitorAction() {
    }

    @Override
    public ActionResult execute(ActionContext context) {
        return new ActionResult() {
            @Override
            public Boolean getSuccess() {
                return true;
            }

            @Override
            public Map<String, String> getPayload() {
                return ImmutableMap.of(
                        "actionType", "monitor",
                        "branchName", context.getBranchName()
                );
            }
        };
    }
}
