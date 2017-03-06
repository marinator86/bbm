package bbm.handlers;

import bbm.database.repositories.Repositories;
import com.google.inject.Inject;
import ratpack.exec.Blocking;
import ratpack.handling.Context;
import ratpack.handling.Handler;

/**
 * Created by mario on 2/27/17.
 */
public class GetRepositoriesHandler implements Handler{

    private final Repositories repositories;

    @Inject
    public GetRepositoriesHandler(Repositories repositories) {
        this.repositories = repositories;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        Blocking.get(() -> repositories.getRepositories())
        .then(allRepositories -> ctx.render(allRepositories));
    }
}
