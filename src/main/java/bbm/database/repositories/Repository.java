package bbm.database.repositories;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by mario on 2/13/17.
 */
@Entity("repos")
public final class Repository {
    @Id
    private ObjectId id;
    private String name;

    private String uuid;
    private Repositories.Provider provider;

    private String refreshToken;

    public ObjectId getId() {
        return id;
    }

    protected void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Repositories.Provider getProvider() {
        return provider;
    }

    public void setProvider(Repositories.Provider provider) {
        this.provider = provider;
    }

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    protected void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    protected void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
