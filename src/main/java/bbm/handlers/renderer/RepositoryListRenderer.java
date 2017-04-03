package bbm.handlers.renderer;

import bbm.database.repositories.Repository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ratpack.handling.Context;
import ratpack.render.Renderer;
import ratpack.render.RendererSupport;
import ratpack.util.Types;

import java.util.List;

/**
 * Created by mario on 2/27/17.
 */
public class RepositoryListRenderer extends RendererSupport<RepositoryList>{

    @Override
    public void render(Context ctx, RepositoryList repositoryList) throws Exception {
        JsonArray results = new JsonArray();
        for(Repository repository : repositoryList.getList()){
            JsonObject repoObject = new JsonObject();
            repoObject.addProperty("name", repository.getRepo_slug());
            repoObject.addProperty("id", repository.getUuid());
            repoObject.addProperty("hoster", repository.getProvider().name());
            results.add(repoObject);
        }
        ctx.render(results.toString());
    }
}
