package bbm.test.buildtrigger;

import bbm.actions.*;
import bbm.actions.context.HookActionContext;
import bbm.handlers.hooks.WebhookHandler;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Test;
import ratpack.func.Function;
import ratpack.http.Headers;
import ratpack.test.handling.HandlingResult;
import ratpack.test.handling.RequestFixture;

import java.util.Map;

/**
 * Created by mario on 2/16/17.
 */
public class BuildTriggerActionTest {
    // Register HookAction.class
    // Register BuildTriggerModule.class

    @Test
    public void testBuildTriggerAction(){
        RequestFixture fixture = RequestFixture.requestFixture();
        fixture.getRegistry().add(HookAction.class, getBranchActionWithResult(true));

        HandlingResult handlingResult = fixture.handle(new WebhookHandlerMock(false));
        Assert.assertEquals((long)400, (long)handlingResult.getClientError());

        handlingResult = fixture.handle(new WebhookHandlerMock(true));
        Assert.assertNull(handlingResult.getClientError());
    }

    private class WebhookHandlerMock extends WebhookHandler {

        private final Boolean requestOk;

        public WebhookHandlerMock(Boolean requestOk) {
            this.requestOk = requestOk;
        }

        @Override
        protected Boolean checkRequest(Headers headers) {
            return requestOk;
        }

        @Override
        protected HookAction.Types getHookActionType(Headers headers) {
            return HookAction.Types.MONITOR;
        }

        @Override
        protected Function<JsonObject, HookActionContext> hookActionContextCreator() {
            return jsonObject -> new HookActionContext() {
                @Override
                public String getRepoUuid() {
                    return "testUuid";
                }

                @Override
                public String getBranchName() {
                    return "testBranchName";
                }
            };
        }
    }

    private HookAction getBranchActionWithResult(Boolean result) {
        return hookActionContext -> new ActionResult() {
            @Override
            public Boolean getSuccess() {
                return result;
            }

            @Override
            public Map<String, String> getPayload() {
                return ImmutableMap.of("", "");
            }
        };
    }
}


