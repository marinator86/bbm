package bbm.auth;

import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.http.client.indirect.FormClient;
import ratpack.handling.Context;
import ratpack.pac4j.RatpackPac4j;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mario on 1/23/17.
 */
public class ClientsProviderImpl implements RatpackPac4j.ClientsProvider {

    private final Authenticator authenticator;

    public ClientsProviderImpl(Authenticator authenticator){
        this.authenticator = authenticator;
    }

    @Override
    public Iterable<FormClient> get(Context ctx) {
        return () -> {
            ArrayList<FormClient> arrayList = new ArrayList();
            arrayList.add(new FormClient("callback", authenticator));
            return arrayList.iterator();
        };
    }
}