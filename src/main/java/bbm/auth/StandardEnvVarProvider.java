package bbm.auth;

/**
 * Created by mario on 4/2/17.
 */
public class StandardEnvVarProvider implements EnvironmentVarProvider {
    @Override
    public String getUserName() {
        return System.getenv(EnvironmentVarProvider.API_USERNAME);
    }

    @Override
    public String getPassword() {
        return System.getenv(EnvironmentVarProvider.API_PASSWORD);
    }
}
