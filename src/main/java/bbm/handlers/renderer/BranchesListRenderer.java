package bbm.handlers.renderer;

import bbm.database.branches.Branch;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ratpack.handling.Context;
import ratpack.render.Renderer;
import ratpack.render.RendererSupport;
import ratpack.util.Types;

import java.util.List;

/**
 * Created by mario on 3/18/17.
 */
public class BranchesListRenderer extends RendererSupport<BranchList> {

    @Override
    public void render(Context ctx, BranchList branchList) throws Exception {
        JsonArray results = new JsonArray();
        for(Branch branch : branchList.getList()){
            JsonObject repoObject = new JsonObject();
            repoObject.addProperty("name", branch.getName());
            repoObject.addProperty("is_managed", branch.getManaged());
            repoObject.addProperty("initial_commit", branch.getInitialCommit());
            results.add(repoObject);
        }
        ctx.render(results.toString());
    }
}
