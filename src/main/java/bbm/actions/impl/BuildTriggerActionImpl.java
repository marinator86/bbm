package bbm.actions.impl;

import bbm.actions.BuildTriggerAction;
import bbm.actions.buildtrigger.BuildTrigger;
import bbm.actions.buildtrigger.Integrators;
import bbm.actions.context.BuildTriggerActionContext;
import bbm.actions.context.BuildTriggerContext;
import bbm.database.repositories.Repositories;
import bbm.database.repositories.Repository;
import com.google.inject.Inject;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.Optional;

/**
 * Created by mario on 2/14/17.
 */
public class BuildTriggerActionImpl implements BuildTriggerAction {

    private final Repositories repositories;
    private final Map<Integrators, BuildTrigger> buildTriggers;

    @Inject
    public BuildTriggerActionImpl(Repositories repositories, Map<Integrators, BuildTrigger> buildTriggers) {
        this.repositories = repositories;
        this.buildTriggers = buildTriggers;
    }

    @Override
    public void execute(BuildTriggerActionContext context) throws Exception {
        Optional<Repository> repositoryOptional = repositories.getRepository(context.getRepUuid());
        Repository repository = repositoryOptional.
                orElseThrow(() -> new InvalidParameterException("Repository with uuid: " + context.getRepUuid() + " not found"));
        switch (repository.getProvider()){
            case BITBUCKET:
                buildTriggers.get(Integrators.BITBUCKETPIPELINES).apply(getTriggerContext(context.getBranchName(), repository));
                break;
        }
    }

    private BuildTriggerContext getTriggerContext(final String branchName, final Repository repository) {
        return new BuildTriggerContext() {
            @Override
            public Repository getRepository() {
                return repository;
            }

            @Override
            public String getBranchName() {
                return branchName;
            }
        };
    }
}
