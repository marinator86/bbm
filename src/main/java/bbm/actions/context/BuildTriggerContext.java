package bbm.actions.context;

import bbm.database.repositories.Repository;

/**
 * Created by mario on 2/19/17.
 */
public interface BuildTriggerContext {

    Repository getRepository();
    String getBranchName();
}
