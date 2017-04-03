package bbm.handlers;

import bbm.database.branches.Branch;
import bbm.database.branches.Branches;
import bbm.database.repositories.Repositories;
import bbm.handlers.renderer.BranchList;
import com.google.inject.Inject;
import ratpack.exec.Blocking;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.util.List;

/**
 * Created by mario on 3/18/17.
 */
public class GetRepositoryBranchesHandler implements Handler{

    private final Branches branches;
    private final Repositories repositories;

    @Inject
    public GetRepositoryBranchesHandler(Branches branches, Repositories repositories) {
        this.branches = branches;
        this.repositories = repositories;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        String uuid = ctx.getAllPathTokens().get("uuid");
        Blocking.get(() -> repositories.getRepository(uuid))
        .route(repository -> !repository.isPresent(), repository -> ctx.clientError(404))
        .map(repository -> branches.getBranches(repository.get()))
        .then(repoBranches -> ctx.render((BranchList) () -> repoBranches));
    }
}
