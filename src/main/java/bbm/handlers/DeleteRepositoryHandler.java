package bbm.handlers;

import bbm.database.repositories.Repositories;
import bbm.database.repositories.Repository;
import com.google.inject.Inject;
import ratpack.exec.Blocking;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.util.Optional;

/**
 * Created by mario on 3/8/17.
 */
public class DeleteRepositoryHandler implements Handler {

    private final Repositories repositories;

    @Inject
    public DeleteRepositoryHandler(Repositories repositories) {
        this.repositories = repositories;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        if(!ctx.getAllPathTokens().containsKey("uuid"))
            ctx.clientError(400);
        Blocking.get(() -> {
            String uuid = ctx.getAllPathTokens().get("uuid");
            return repositories.getRepository(uuid);
        })
        .route(repository -> !repository.isPresent(), repository -> ctx.clientError(404))
        .map(repository -> {
            String uuid = repository.get().getUuid();
            repositories.deleteRepository(uuid);
            return uuid;
        })
        .onError(throwable -> ctx.error(throwable))
        .then(uuid -> ctx.next());
    }
}
