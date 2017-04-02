package bbm.auth;

/**
 * Created by mario on 4/2/17.
 */
public interface EnvironmentVarProvider {
    String API_USERNAME = "API_USERNAME";
    String API_PASSWORD = "API_PASSWORD";

    String getUserName();
    String getPassword();
}
