package migration;

import bbm.database.DatabaseModule;
import bbm.database.branches.Branch;
import bbm.database.branches.BranchModule;
import bbm.database.branches.Branches;
import com.google.inject.Injector;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

/**
 * Created by mario on 3/14/17.
 */
public class AddInitialCommitToBranch {
    private final static Logger logger = LoggerFactory.getLogger(AddInitialCommitToBranch.class);

    public static void main(String[] args){
        Injector injector = com.google.inject.Guice.createInjector(
            new DatabaseModule(),
            new BranchModule()
        );

        Datastore datastore = injector.getInstance(Datastore.class);
        Branches branches = injector.getInstance(Branches.class);
        List<Branch> currentBranches = datastore.find(Branch.class).asList();
        Scanner in = new Scanner(System.in);
        logger.info("***Dry run (y/n)?:");
        Boolean dryRun = "y".equals(in.nextLine());


        for(Branch branch : currentBranches){
            if(branch.getManaged()){
                logger.info("Branch " + branch.getName() + " is managed currently and will get set initial commit.");
                String commitHash;
                do {
                    logger.info("Enter commit hash for " + branch.getName() + ": ");
                    commitHash = in.nextLine();
                    logger.info("Correct commit hash " + commitHash + "? (y/n):");
                } while (!"y".equals(in.nextLine()));
                branches.setManaged(branch, commitHash); // effectively will cause all subsequent instructs to be DEPLOY ones
            }
        }

        if(!dryRun){
            datastore.save(currentBranches);
        }
    }
}
