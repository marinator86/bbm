package bbm.test.handlers;

import bbm.actions.ActionResult;
import bbm.actions.BuildTriggerAction;
import bbm.actions.HookAction;
import bbm.actions.context.HookActionContext;
import bbm.actions.impl.MonitorSyncActionImpl;
import bbm.actions.impl.UnmonitorSyncActionImpl;
import bbm.database.branches.BranchModule;
import bbm.database.repositories.RepositoryModule;
import bbm.handlers.hooks.BitbucketWebhookHandler;
import bbm.handlers.hooks.WebhookHandler;
import bbm.test.DatabaseModule;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.util.Types;
import org.junit.Assert;
import org.junit.Test;
import ratpack.http.client.ReceivedResponse;
import ratpack.http.client.RequestSpec;
import ratpack.test.embed.EmbeddedApp;
import ratpack.test.handling.HandlingResult;
import ratpack.test.handling.RequestFixture;

import java.util.Map;

/**
 * Created by mario on 3/13/17.
 */
public class BitBucketWebhookHandlerTest {
    @Test
    public void testBitBucketWebHookHandler() throws Exception {
        HookActionMock mock = new HookActionMock();
        BuildTriggerMock triggerActionMock = new BuildTriggerMock();

        Injector injector = Guice.createInjector(
            new TestModule(mock, triggerActionMock)
        );

        EmbeddedApp.of(ratpackServerSpec -> {
            ratpackServerSpec.registry(ratpack.guice.Guice.registry(b -> {
                b.module(new TestModule(mock, triggerActionMock));
                b.bind(BitbucketWebhookHandler.class);
            }));

            ratpackServerSpec.handlers(chain -> {
                chain.post(BitbucketWebhookHandler.class);
            });
        }).test(testHttpClient -> {
            ReceivedResponse res = testHttpClient.request(requestSpec -> {
                requestSpec.post();
                requestSpec.getHeaders().add(BitbucketWebhookHandler.HEADER_EVENT_KEY, "pullrequest:created");
                RequestSpec.Body body = requestSpec.getBody();
                body.text(getRequestBody().toString());
            });

            Assert.assertNotNull(mock.ctx);
            Assert.assertEquals("a0B", mock.ctx.getHookCommit());
        });
    }

    private JsonObject getRequestBody() {
        JsonObject root = new JsonObject();

        JsonObject repo = new JsonObject();
        repo.addProperty("uuid", "testId");

        JsonObject pr = new JsonObject();
        JsonObject source = new JsonObject();
        JsonObject branch = new JsonObject();
        branch.addProperty("name", "testBranchName");
        JsonObject commit = new JsonObject();
        commit.addProperty("hash", "a0B");

        source.add("branch", branch);
        source.add("commit", commit);

        pr.add("source", source);

        root.add("repository", repo);
        root.add("pullrequest", pr);
        return root;
    }

    private class HookActionMock implements HookAction{
        public HookActionContext ctx;
        @Override
        public ActionResult apply(HookActionContext actionContext) throws Exception {
            this.ctx = actionContext;
            return new ActionResult() {
                @Override
                public Boolean getSuccess() {
                    return true;
                }

                @Override
                public Map<String, String> getPayload() {
                    return ImmutableMap.of("", "");
                }
            };
        }
    }

    private class BuildTriggerMock implements BuildTriggerAction {
        public HookActionContext ctx;
        @Override
        public void execute(HookActionContext actionContext) throws Exception {
            this.ctx = actionContext;
        }
    }

    private class TestModule extends AbstractModule {

        private final HookAction mock;
        private final BuildTriggerAction triggerActionMock;

        public TestModule(HookAction mock, BuildTriggerAction triggerActionMock) {
            this.mock = mock;
            this.triggerActionMock = triggerActionMock;
        }

        @Override
        protected void configure() {
            MapBinder<HookAction.Types, HookAction> mapBinder = MapBinder.newMapBinder(binder(),
                    HookAction.Types.class, HookAction.class);
            mapBinder.addBinding(HookAction.Types.MONITOR).toInstance(mock);
            mapBinder.addBinding(HookAction.Types.UNMONITOR).toInstance(mock);

            bind(WebhookHandler.class).to(BitbucketWebhookHandler.class);
            bind(BuildTriggerAction.class).toInstance(triggerActionMock);
        }
    }
}