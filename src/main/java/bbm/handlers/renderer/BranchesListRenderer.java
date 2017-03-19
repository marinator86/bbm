package bbm.handlers.renderer;

import bbm.database.branches.Branch;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ratpack.handling.Context;
import ratpack.render.Renderer;
import ratpack.util.Types;

import java.util.List;

/**
 * Created by mario on 3/18/17.
 */
public class BranchesListRenderer implements Renderer<List<Branch>> {
    @Override
    public Class<List<Branch>> getType() {
        return (Class<List<Branch>>) Types.listOf(Branch.class).getRawType();
    }

    @Override
    public void render(Context context, List<Branch> branches) throws Exception {
        JsonArray results = new JsonArray();
        for(Branch branch : branches){
            JsonObject repoObject = new JsonObject();
            repoObject.addProperty("name", branch.getName());
            repoObject.addProperty("is_managed", branch.getManaged());
            repoObject.addProperty("initial_commit", branch.getInitialCommit());
            results.add(repoObject);
        }
        context.render(results.toString());
    }
}
