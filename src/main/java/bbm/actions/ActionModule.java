package bbm.actions;

import bbm.actions.impl.ActionResolverImpl;
import com.google.inject.AbstractModule;

/**
 * Created by mario on 1/18/17.
 */
public class ActionModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(ActionResolver.class).to(ActionResolverImpl.class);
    }
}
