package bbm.actions;

import bbm.actions.impl.*;
import bbm.actions.impl.UnmonitorActionImpl;
import com.google.inject.AbstractModule;

/**
 * Created by mario on 1/18/17.
 */
public class ActionModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(MonitorAction.class).to(MonitorActionImpl.class);
        bind(ReceiveCredentialsAction.class).to(ReceiveCredentialsActionImpl.class);
        bind(UnmonitorAction.class).to(UnmonitorActionImpl.class);
        bind(SandboxSyncAction.class).to(SandboxSyncActionImpl.class);
    }
}
