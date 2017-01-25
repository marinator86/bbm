package bbm.salesforce;

import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.tooling.ToolingConnection;
import com.sforce.soap.tooling.sobject.SandboxInfo;
import com.sforce.ws.ConnectionException;
/**
 * Created by mario on 1/25/17.
 */
public interface Api {
    LoginResult blockingPartnerLogin(String username, String password, String token) throws ConnectionException;
    ToolingConnection blockingToolingApiLogin(LoginResult loginResult) throws ConnectionException;
    SandboxInfo[] blockingGetSandboxes(ToolingConnection toolingConnection) throws ConnectionException;
}
