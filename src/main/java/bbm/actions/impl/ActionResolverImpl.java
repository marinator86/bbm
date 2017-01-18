package bbm.actions.impl;

import bbm.actions.Action;
import bbm.actions.ActionResolver;

import static bbm.actions.ActionResolver.ActionType.*;

/**
 * Created by mario on 1/18/17.
 */
public class ActionResolverImpl implements ActionResolver{
    @Override
    public Action resolveAction(ActionType type) {
        switch (type){
            case MONITOR:
                return new MonitorAction();
            case UNMONITOR:
                return new UnmonitorAction();
            case RETURN_CREDENTIALS:
                return new ReceiveCredentialsAction();
        }
        throw new IllegalStateException("type not implemented");
    }
}
