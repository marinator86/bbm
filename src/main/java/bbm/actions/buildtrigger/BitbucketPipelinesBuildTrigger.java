package bbm.actions.buildtrigger;

import bbm.actions.ActionResult;
import bbm.actions.context.BuildTriggerContext;
import bbm.actions.context.HookActionContext;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by mario on 2/14/17.
 */
class BitbucketPipelinesBuildTrigger implements BuildTrigger{
    private final static Logger logger = LoggerFactory.getLogger(BitbucketPipelinesBuildTrigger.class);

    @Override
    public ActionResult apply(BuildTriggerContext buildTriggerContext) {
        // TODO call API of bitbucket to trigger pipelines
        logger.debug("BitbucketPipelinesBuildTrigger called, not implemented yet");
        return new ActionResult() {
            @Override
            public Boolean getSuccess() {
                return false;
            }

            @Override
            public Map<String, String> getPayload() {
                return ImmutableMap.of("msg", "not implemented");
            }
        };
    }

}
