package bbm.actions;

import bbm.actions.impl.*;
import bbm.actions.impl.UnmonitorSyncActionImpl;
import com.google.inject.AbstractModule;

/**
 * Created by mario on 1/18/17.
 */
public class ActionModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(MonitorSyncAction.class).to(MonitorSyncActionImpl.class);
        bind(UnmonitorSyncAction.class).to(UnmonitorSyncActionImpl.class);
        bind(SandboxSyncAction.class).to(SandboxSyncSyncActionImpl.class);
        bind(InstructionAction.class).to(InstructionActionImpl.class);
        bind(BuildTriggerAction.class).to(BuildTriggerActionImpl.class);
    }
}
