package bbm.salesforce;

import com.google.inject.AbstractModule;

/**
 * Created by mario on 1/25/17.
 */
public class SalesforceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Api.class).to(ApiImpl.class);
    }
}
