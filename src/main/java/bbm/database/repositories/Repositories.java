package bbm.database.repositories;

import java.util.List;
import java.util.Optional;

/**
 * Created by mario on 2/13/17.
 */
public interface Repositories {
    enum Provider { BITBUCKET };
    List<Repository> getRepositories();
    Optional<Repository> getRepository(String uuid);
    void createRepository(String name, String uuid, Provider provider) throws IllegalArgumentException;
    void updateTokens(Repository repository, String newAccessToken, String newRefreshToken);
}
