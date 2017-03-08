package bbm.handlers.renderer;

import bbm.database.repositories.Repository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ratpack.handling.Context;
import ratpack.render.Renderer;
import ratpack.util.Types;

import java.util.List;

/**
 * Created by mario on 2/27/17.
 */
public class RepositoryRenderer implements Renderer<Repository> {

    @Override
    public Class<Repository> getType() {
        return (Class<Repository>) Types.token(Repository.class).getRawType();
    }

    @Override
    public void render(Context ctx, Repository repository) throws Exception {
        JsonObject repoObject = new JsonObject();
        repoObject.addProperty("name", repository.getRepo_slug());
        repoObject.addProperty("id", repository.getUuid());
        repoObject.addProperty("hoster", repository.getProvider().name());
        ctx.render(repoObject.toString());
    }
}
