package bbm.actions.impl;

import bbm.actions.ActionResult;
import bbm.actions.SandboxSyncAction;
import bbm.actions.context.OrgActionContext;
import bbm.database.orgs.Orgs;
import bbm.database.sandboxes.Sandboxes;
import com.google.inject.Inject;
import com.sforce.soap.tooling.QueryResult;
import com.sforce.soap.tooling.ToolingConnection;
import com.sforce.soap.tooling.sobject.SObject;
import com.sforce.soap.tooling.sobject.SandboxInfo;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mario on 1/24/17.
 */
public class SandboxSyncActionImpl implements SandboxSyncAction {

    private final Orgs orgs;
    private final Sandboxes sandboxes;
    private final static Logger logger = LoggerFactory.getLogger(SandboxSyncActionImpl.class);

    @Inject
    public SandboxSyncActionImpl(Orgs orgs, Sandboxes sandboxes){
        this.orgs = orgs;
        this.sandboxes = sandboxes;
    }

    @Override
    public ActionResult apply(OrgActionContext orgActionContext) {
        // Async stuff here
        ConnectorConfig config = new ConnectorConfig();
        config.setSessionId(orgActionContext.getSessionId());
        try {
            ToolingConnection conn = com.sforce.soap.tooling.Connector.newConnection(config);
            QueryResult query = conn.query("select id, name from SandboxInfo where name like \'"+ orgActionContext.getSandboxPrefix() +"%\'");
            SObject[] records = query.getRecords();
            for(SObject record : records){
                SandboxInfo info = (SandboxInfo) record;
                logger.debug(info.getSandboxName());
                logger.debug(info.getLastModifiedDate().toString());
            }
        } catch (ConnectionException e) {
            e.printStackTrace();
        }

        return null;
    }
}
