package bbm.database.repositories;

import bbm.database.branches.Branch;
import com.google.inject.Inject;
import org.mongodb.morphia.Datastore;

import java.util.List;
import java.util.Optional;

/**
 * Created by mario on 2/13/17.
 */
public class RepositoriesImpl implements Repositories {
    private final Datastore datastore;

    @Inject
    public RepositoriesImpl(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public Optional<Repository> getRepository(String uuid) {
        return Optional.ofNullable(datastore.find(Repository.class).field("uuid").equal(uuid).get());
    }

    @Override
    public List<Repository> getRepositories() {
        return datastore.find(Repository.class).asList();
    }

    @Override
    public void createRepository(String name, String uuid, Provider provider) throws IllegalArgumentException{
        if(getRepository(uuid).isPresent())
            throw new IllegalArgumentException("Repository uuid is already present");

        Repository newRepo = new Repository();
        newRepo.setUuid(uuid);
        newRepo.setRepo_slug(name);
        newRepo.setProvider(provider);
        datastore.save(newRepo);
    }

    @Override
    public void updateTokens(Repository repository, String newAccessToken, String newRefreshToken) {
        repository.setAccessToken(newAccessToken);
        repository.setRefreshToken(newRefreshToken);
        datastore.save(repository);
    }

    @Override
    public void deleteRepository(String uuid) throws IllegalStateException {
        Optional<Repository> repository = getRepository(uuid);
        if(!repository.isPresent())
            throw new IllegalStateException("specified repository does not exist");

        Boolean hasManagedBranches = datastore.find(Branch.class)
                .field("repository").equal(repository.get())
                .field("managed").equal(true)
                .countAll()
                > 0;
        if(hasManagedBranches)
            throw new IllegalStateException("specified repository can't be deleted because it has managed branches");

        datastore.delete(repository.get());
    }
}
