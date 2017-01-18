package bbm.actions.impl;

import bbm.actions.Action;
import bbm.actions.ActionResolver;

import static bbm.actions.ActionResolver.ActionType.*;

/**
 * Created by mario on 1/18/17.
 */
public class ActionResolverImpl implements ActionResolver{
    @Override
    public Action resolveAction(ActionType type, String branchName) {
        switch (type){
            case MONTIOR:
                return new MonitorAction(branchName);
            case UNMONITOR:
                return new UnmonitorAction(branchName);
            case RETURN_CREDENTIALS:
                return new ReceiveCredentialsAction(branchName);
        }
        throw new IllegalStateException("type not implemented");
    }
}
