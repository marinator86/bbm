package migration;

import bbm.database.DatabaseModule;
import bbm.database.branches.Branch;
import bbm.database.branches.BranchModule;
import bbm.database.repositories.Repositories;
import bbm.database.repositories.Repository;
import bbm.database.repositories.RepositoryModule;
import com.google.inject.Injector;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

/**
 * Created by mario on 3/8/17.
 * This class is to migrate productive data to have branches related with repositories.
 * One branch belongs to exactly one repo.
 *
 * Requires that MONGODB_URI is set in the environment
 */
public class RelateBranchesWithRepositories {
    private final static Logger logger = LoggerFactory.getLogger(RelateBranchesWithRepositories.class);

    @Deprecated
    public static void main(String[] args) {
        Injector injector = com.google.inject.Guice.createInjector(
                new DatabaseModule(),
                new BranchModule(),
                new RepositoryModule()
        );

        Datastore datastore = injector.getInstance(Datastore.class);
        logger.info("***MongoClientDescription: {}", datastore.getDB().getName());

        Scanner in = new Scanner(System.in);

        logger.info("***Dry run (y/n)?:");
        Boolean dryRun = "y".equals(in.nextLine());

        Repositories repositories = injector.getInstance(Repositories.class);
        Repository repo = null;
        List<Repository> repos = repositories.getRepositories();
        if(repos.size() > 1){
            logger.info("***Found multiple repositories. Exiting!");
            return;
        }
        if(repos.size() == 1){
            logger.info("***Found a repository!");
            logger.info("***Name: {}, Uuid:{}", repos.get(0).getRepo_slug(), repos.get(0).getUuid());
            logger.info("***Take the existing one? (y/n)?:");
            Boolean take = "y".equals(in.nextLine());
            if(take) {
                repo = repos.get(0);
            }
        }
        if(repo == null) {
            Job job = getRepoJob(in, dryRun);
            if (!dryRun) {
                repo = createRepo(repositories, job);
            }
        }

        List<Branch> branches = datastore.find(Branch.class).asList();
        logger.info("***Found branches: {}", branches.size());

        for(Branch branch : branches){
            logger.info("***********************************************************");
            logger.info("***branch name: {}", branch.getName());
            logger.info("***branch managed: {}", branch.getManaged());
            Boolean reassign = true;
            if(branch.getRepository() != null) {
                logger.info("***branch repo is not null: {}", branch.getRepository().getRepo_slug());
                logger.info("Reassign to repo? ({})", repo.getRepo_slug());
                reassign = "y".equals(in.nextLine());
            }
            //if(reassign)
            //    branch.setRepository(repo);
        }

        if(!dryRun){
            datastore.save(branches);
        }
    }

    private static Repository createRepo(Repositories repositories, Job job) {
        repositories.createRepository(job.getRepoSlug(), job.getUuid(), job.getProvider());
        return repositories.getRepository(job.getUuid()).get();
    }

    private static Job getRepoJob(Scanner in, Boolean dryRun) {
        Job job;
        logger.info("***Repo UUID?: ");
        String uuid = in.next();

        logger.info("***Repo Name: ");
        String name = in.next();

        logger.info("***Repo provider is always Bitbucket.");
        job = new Job(dryRun, name, uuid, Repositories.Provider.BITBUCKET);

        logger.info("***Job:\n***dryRun: {}\n***uuid: {}\n***name: {}\n***provider: {}", job.getDryRun(), job.getUuid(), job.getRepoSlug(), job.getProvider().name());
        return job;
    }

    public static class Job {
        private final Boolean dryRun;
        private final String repoSlug;
        private final String uuid;
        private final Repositories.Provider provider;

        public Job(Boolean dryRun, String repoSlug, String uuid, Repositories.Provider provider) {
            this.dryRun = dryRun;
            this.repoSlug = repoSlug;
            this.uuid = uuid;
            this.provider = provider;
        }

        public Boolean getDryRun() {
            return dryRun;
        }

        public String getRepoSlug() {
            return repoSlug;
        }

        public String getUuid() {
            return uuid;
        }

        public Repositories.Provider getProvider() {
            return provider;
        }
    }
}
