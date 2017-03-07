package bbm.handlers;

import bbm.database.repositories.Repositories;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.security.InvalidParameterException;

/**
 * Created by mario on 3/6/17.
 */
public class PostRepositoryHandler implements Handler{

    private final Repositories repositories;

    @Inject
    public PostRepositoryHandler(Repositories repositories) {
        this.repositories = repositories;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        ctx.getRequest().getBody().map(typedData -> new JsonParser().parse(typedData.getText()).getAsJsonObject())
        .map(jsonObject -> {
            if(!ctx.getAllPathTokens().containsKey("uuid"))
                throw new InvalidParameterException("Uuid string of the repository not specified as path token");
            String uuid = ctx.getAllPathTokens().get("uuid");
            JsonElement repoName = jsonObject.get("name");
            if(repoName == null)
                throw new InvalidParameterException("Repository \"name\" property string missing in request body");
            repositories.createRepository(repoName.getAsString(), uuid, Repositories.Provider.BITBUCKET);
            return repositories.getRepository(uuid).get();
        })
        .then(repository -> ctx.render(repository));
    }
}
