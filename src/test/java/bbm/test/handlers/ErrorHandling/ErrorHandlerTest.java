package bbm.test.handlers.ErrorHandling;

import bbm.handlers.ErrorHandler;
import org.junit.Assert;
import org.junit.Test;
import ratpack.guice.Guice;
import ratpack.http.client.ReceivedResponse;
import ratpack.test.embed.EmbeddedApp;

/**
 * Created by mario on 3/8/17.
 */
public class ErrorHandlerTest {

    @Test
    public void testThrowError() throws Exception {
        EmbeddedApp.of(ratpackServerSpec -> {
            ratpackServerSpec.registry(Guice.registry(b -> {
                b.bind(ThrowErrorHandler.class);
                b.bind(ErrorHandler.class);
            }));

            ratpackServerSpec.handlers(chain -> {
                chain.all(ThrowErrorHandler.class);
            });
        }).test(testHttpClient -> {
            ReceivedResponse res = testHttpClient.get("test");
            Assert.assertEquals(500, res.getStatusCode());
        });

    }

    @Test
    public void testClientError() throws Exception {
        EmbeddedApp.of(ratpackServerSpec -> {
            ratpackServerSpec.registry(Guice.registry(b -> {
                b.bind(ClientErrorHandler.class);
                b.bind(ErrorHandler.class);
            }));

            ratpackServerSpec.handlers(chain -> {
                chain.all(ClientErrorHandler.class);
            });
        }).test(testHttpClient -> {
            ReceivedResponse res = testHttpClient.get("test");
            Assert.assertEquals(400, res.getStatusCode());
        });

    }

}