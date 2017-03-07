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
public class RepositoryListRenderer implements Renderer<List<Repository>> {

    @Override
    public Class<List<Repository>> getType() {
        return (Class<List<Repository>>) Types.listOf(Repository.class).getRawType();
    }

    @Override
    public void render(Context ctx, List<Repository> repositories) throws Exception {
        JsonArray results = new JsonArray();
        for(Repository repository : repositories){
            JsonObject repoObject = new JsonObject();
            repoObject.addProperty("name", repository.getRepo_slug());
            repoObject.addProperty("id", repository.getUuid());
            repoObject.addProperty("hoster", repository.getProvider().name());
            results.add(repoObject);
        }
        ctx.render(results.toString());
    }
}
