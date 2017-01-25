package bbm.salesforce;

import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.tooling.QueryResult;
import com.sforce.soap.tooling.ToolingConnection;
import com.sforce.soap.tooling.sobject.SObject;
import com.sforce.soap.tooling.sobject.SandboxInfo;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 1/25/17.
 */
public class ApiImpl implements Api{
    @Override
    public LoginResult blockingPartnerLogin(String username, String password, String token) throws ConnectionException {
        ConnectorConfig config = new ConnectorConfig();
        config.setManualLogin(true);
        PartnerConnection connection = com.sforce.soap.partner.Connector.newConnection(config);
        LoginResult loginResult = connection.login(username, password + token);
        if(loginResult.isSandbox())
            throw new ConnectionException("Sandbox login (" + loginResult.getUserInfo().getOrganizationName() +
                    ") is not allowed!");
        return loginResult;
    }

    @Override
    public ToolingConnection blockingToolingApiLogin(LoginResult loginResult) throws ConnectionException {
        ConnectorConfig toolingConfig = new ConnectorConfig();
        toolingConfig.setSessionId(loginResult.getSessionId());
        toolingConfig.setServiceEndpoint(loginResult.getServerUrl().replace('u', 'T'));
        return com.sforce.soap.tooling.Connector.newConnection(toolingConfig);
    }

    @Override
    public SandboxInfo[] blockingGetSandboxes(ToolingConnection toolingConnection) throws ConnectionException {
        List<SandboxInfo> result = new ArrayList();
        QueryResult query = toolingConnection.query("select id, SandboxName from SandboxInfo");
        for(SObject record : query.getRecords()){
            result.add((SandboxInfo) record);
        }
        return result.toArray(new SandboxInfo[]{});
    }
}
