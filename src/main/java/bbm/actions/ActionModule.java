package bbm.actions;

import bbm.actions.impl.*;
import bbm.actions.impl.UnmonitorSyncActionImpl;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * Created by mario on 1/18/17.
 */
public class ActionModule extends AbstractModule{
    @Override
    protected void configure() {
        MapBinder<HookAction.Types, HookAction> mapBinder = MapBinder.newMapBinder(binder(),
                HookAction.Types.class, HookAction.class);
        mapBinder.addBinding(HookAction.Types.MONITOR).to(MonitorSyncActionImpl.class);
        mapBinder.addBinding(HookAction.Types.UNMONITOR).to(UnmonitorSyncActionImpl.class);

        bind(SandboxSyncAction.class).to(SandboxSyncSyncActionImpl.class);
        bind(InstructionAction.class).to(InstructionActionImpl.class);
        bind(BuildTriggerAction.class).to(BuildTriggerActionImpl.class);
    }
}
