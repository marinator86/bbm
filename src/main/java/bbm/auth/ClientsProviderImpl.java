package bbm.auth;

import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.http.credentials.authenticator.Authenticator;
import ratpack.handling.Context;
import ratpack.pac4j.RatpackPac4j;

import java.util.ArrayList;

/**
 * Created by mario on 1/24/17.
 */
public class ClientsProviderImpl implements RatpackPac4j.ClientsProvider{

    private final Authenticator authenticator;

    public ClientsProviderImpl(Authenticator authenticator){

        this.authenticator = authenticator;
    }

    @Override
    public Iterable<FormClient> get(Context ctx) {
        FormClient formClient = new FormClient();
        formClient.setCallbackUrl("loginForm.html");
        formClient.setAuthenticator(authenticator);

        ArrayList<FormClient> list = new ArrayList<>();
        list.add(formClient);
        return () -> list.iterator();
    }
}
