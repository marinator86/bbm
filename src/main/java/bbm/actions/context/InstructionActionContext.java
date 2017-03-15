package bbm.actions.context;

/**
 * Created by mario on 1/27/17.
 */
public interface InstructionActionContext extends ActionContext{
    String getRepositoryUID();
    String getBranchName();
    String getCurrentCommit();
}
