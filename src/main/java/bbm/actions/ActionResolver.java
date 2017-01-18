package bbm.actions;

/**
 * Created by mario on 1/18/17.
 */
public interface ActionResolver {
    public enum ActionType {MONITOR, UNMONITOR, RETURN_CREDENTIALS}

    Action resolveAction(ActionType type, String branchName);
}
