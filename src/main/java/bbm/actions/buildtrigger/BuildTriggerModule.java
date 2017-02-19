package bbm.actions.buildtrigger;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * Created by mario on 2/14/17.
 */
public class BuildTriggerModule extends AbstractModule {
    @Override
    protected void configure() {
        MapBinder<Integrators, BuildTrigger> mapBinder =
                MapBinder.newMapBinder(binder(), Integrators.class, BuildTrigger.class);
        mapBinder.addBinding(Integrators.BITBUCKETPIPELINES).to(BitbucketPipelinesBuildTrigger.class);
    }
}
