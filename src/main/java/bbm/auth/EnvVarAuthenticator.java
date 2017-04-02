package bbm.auth;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.http.credentials.UsernamePasswordCredentials;
import org.pac4j.http.credentials.authenticator.UsernamePasswordAuthenticator;

/**
 * Created by mario on 1/23/17.
 */
public class EnvVarAuthenticator implements UsernamePasswordAuthenticator {

    private final EnvironmentVarProvider provider;

    @Inject
    public EnvVarAuthenticator(EnvironmentVarProvider provider) {
        this.provider = provider;
    }

    @Override
    public void validate(UsernamePasswordCredentials credentials) {
        if (credentials == null) {
            throwsException("No credential");
        }
        String username = credentials.getUsername();
        String password = credentials.getPassword();
        if (CommonHelper.isBlank(username)) {
            throwsException("Username cannot be blank");
        }
        if (CommonHelper.isBlank(password)) {
            throwsException("Password cannot be blank");
        }
        String apiUser = provider.getUserName();
        String apiPass = provider.getPassword();
        if(StringUtils.isEmpty(apiUser) || StringUtils.isEmpty(apiPass)) {
            if (CommonHelper.areNotEquals(username, password))
                throwsException("Username : '" + username + "' does not match password");
        } else if(!apiUser.equals(username) || !apiPass.equals(password))
            throwsException("Username or password don't match");

        final CommonProfile profile = new CommonProfile();
        profile.setId(username);
        profile.addAttribute(CommonProfile.USERNAME, username);
        credentials.setUserProfile(profile);
    }

    protected void throwsException(final String message) throws CredentialsException {
        throw new CredentialsException(message);
    }
}
