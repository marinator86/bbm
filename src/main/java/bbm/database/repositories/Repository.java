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
    private String repo_slug;
    private String uuid;
    private String refreshToken;

    protected ObjectId getId() {
        return id;
    }

    protected void setId(ObjectId id) {
        this.id = id;
    }

    public String getRepo_slug() {
        return repo_slug;
    }

    protected void setRepo_slug(String repo_slug) {
        this.repo_slug = repo_slug;
    }

    private Repositories.Provider provider;

    public String getUuid() {
        return uuid;
    }

    protected void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Repositories.Provider getProvider() {
        return provider;
    }

    protected void setProvider(Repositories.Provider provider) {
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
