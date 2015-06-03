import java.io.File;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Gitlet {

    private static boolean dangerous() {
        String msg1 = "Warning: The command you entered may alter the files in ";
        String msg2 = "your working directory. Uncommitted changes may be lost.";
        String msg3 = " Are you sure you want to continue? (yes/no)";
        System.out.println(msg1 + msg2 + msg3);
        
        Scanner in = new Scanner(System.in);
        String response = in.nextLine();
        if (response.equals("yes")) {
            return true;
        }
        return false;
    }
    
    private static void init() {
        Commit.counter = 0;
        File home = new File(".gitlet");
       
        /* Checks for and creates .gitlet directory */
        if (home.exists()) {
            String msg = "A gitlet version control system already exists in the current directory.";
            System.out.println(msg);
            return;
        }
        
        home.mkdir();
        
        /*Creates all the scaffolding for future commits */
        
        File commits = new File(".gitlet/commits/initial commit");
        commits.mkdirs();

        /*Serializing initial commit. Adapted from tutorialspoint.com/java/java_serialization.htm*/
        
        Commit initialCommit = new Commit("initial commit", null, new HashMap<String, String>());
        String id = initialCommit.getID();
        File commitFolder = new File(".gitlet/commits/initial commit/" + id);
        commitFolder.mkdir();
        initialCommit.serialize(".gitlet/commits/initial commit/" + id + "/" + id + ".ser");


    /*Creates all the scaffolding for branches */

        File branches = new File(".gitlet/branches/master");
        branches.mkdirs();
        
        Branch initial = new Branch("master", id);
        initial.serialize(".gitlet/branches/master/master.ser");
        initial.serialize(".gitlet/branches/currBranch.ser");

        /* Creates blank staged.ser and blank toRemove.ser */
        Staged blankStaged = new Staged();
        blankStaged.serialize(".gitlet/staged.ser");

        toRemove blankRemove = new toRemove();
        blankRemove.serialize(".gitlet/toRemove.ser");
        
        CommitList commitList = new CommitList();
        commitList.add(id);
        commitList.serialize(".gitlet/commitList.ser");
        
    }


    private static void add(String fileName) {
        File toAdd = new File(fileName);
        if (toAdd.exists()) {
            if (GitletUtils.toRemoveContains(fileName)) {
                return;
            }

            
            if (GitletUtils.modSinceLastCommit(fileName)) {
                Staged stagedFiles = (Staged) GitletUtils.deSerialize(".gitlet/staged.ser");
                stagedFiles.add(fileName);
                stagedFiles.serialize(".gitlet/staged.ser");
                return;
            } else {
                System.out.println("File has not been modified since the last commit.");
                return;
            }

        } else {
            System.out.println("File does not exist.");
            return;
        }
    }


    private static void 
        stagedCommitHelper(String msg, String newID, HashMap<String, String> newFileLocations) {
        
        Staged stagedFiles = (Staged) GitletUtils.deSerialize(".gitlet/staged.ser");
        for (String s : stagedFiles.allFiles()) {   
            newFileLocations.put(s, newID);
            File newFile = new File(".gitlet/commits/" + msg + "/" + newID + "/" + s);
            newFile.getParentFile().mkdirs();
            GitletUtils.fileCopy(s, ".gitlet/commits/" + msg + "/" + newID + "/" + s);
        }

        stagedFiles.wipe();
           

    }

    private static void toRemoveCommitHelper(HashMap<String, String> newFileLocations) {
        toRemove toRemoveFiles = (toRemove) GitletUtils.deSerialize(".gitlet/toRemove.ser");
        for (String s : toRemoveFiles.allFiles()) {
            newFileLocations.remove(s);
        }

        toRemoveFiles.wipe();
    }


    private static void commit(String msg) {
        Staged stagedFiles = GitletUtils.getStaged();
        toRemove toRemoveFiles = GitletUtils.getToRemove();

        if (stagedFiles.isEmpty() && toRemoveFiles.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        
        /* creating new fileLocations map for the commit */
        Branch currBranch = GitletUtils.getCurrBranch();
        Commit lastCommit = GitletUtils.getHead(currBranch);
        HashMap<String, String> newFileLocations = new HashMap<String, String>();
        newFileLocations.putAll(lastCommit.getFileLocations());

        /*fixing up the new Commit to have all the files in Staged*/
        Commit newCommit = new Commit(msg, lastCommit.getID(), null);
        String newID = newCommit.getID();     
        File newCommitDirectory = new File(".gitlet/commits/" + msg + "/" + newID);
        newCommitDirectory.mkdirs();

        /*takes care of stuff for staged.ser */
        stagedCommitHelper(msg, newID, newFileLocations);
        

        /*takes care of everything for toRemove.ser*/
        toRemoveCommitHelper(newFileLocations);
        

        newCommit.setFileLocations(newFileLocations);
        newCommit.serialize(".gitlet/commits/" + msg + "/" + newID + "/" + newID + ".ser");
        GitletUtils.updateCurrBranchHead(newID);
        GitletUtils.updateBranchHead(currBranch.getBranchName(), newID);

        CommitList list = GitletUtils.getCommitList();
        list.add(newID);
        
    }
    
    private static void rm(String fileName) {
        Staged stagedFiles = GitletUtils.getStaged();
        Commit lastCommit = GitletUtils.getLastCommit();

        for (String s : stagedFiles.allFiles()) {
            if (s.equals(fileName)) {
                stagedFiles.remove(s);
                return;
            }
        }
        
        if (lastCommit.contains(fileName)) {
            toRemove toRemoveFiles = GitletUtils.getToRemove();
            toRemoveFiles.add(fileName);
        } else {
            System.out.println("No reason to remove the file.");
        }

        return;

    }

    private static void log() {
        Commit startCommit = GitletUtils.getLastCommit();
        while (startCommit != null) {
            System.out.println("====");
            System.out.println("Commit " + startCommit.getID() + ".");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(sdf.format(startCommit.getTime().getTime()));
            System.out.println(startCommit.getMsg());
            System.out.println("");
            startCommit = GitletUtils.getPrevCommit(startCommit);
        }
    }

    private static void globalLog() {
        CommitList list = GitletUtils.getCommitList();
        for (String id : list.allFiles()) {
            Commit comm = GitletUtils.getCommit(id);
            System.out.println("====");
            System.out.println("Commit " + comm.getID() + ".");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(sdf.format(comm.getTime().getTime()));
            System.out.println(comm.getMsg());
            System.out.println("");

        }
    }

    private static void find(String msg) {
        File allCommits = new File(".gitlet/commits/" + msg);
        
        if (!allCommits.exists()) {
            System.out.println("Found no commit with that message.");
            return;
        }

        /* taken from http://stackoverflow.com/questions/5694385/gett
        ing-the-filenames-of-all-files-in-a-folder */
        File[] listOfFiles = allCommits.listFiles();
        for (File f : listOfFiles) {
            System.out.println(f.getName());
        }
    }

    private static void status() {
        File allBranches = new File(".gitlet/branches");
        Branch currBranch = GitletUtils.getCurrBranch();
        String currBranchName = currBranch.getBranchName();

        File[] listOfFiles = allBranches.listFiles();
        System.out.println("=== Branches ===");
        for (File f : listOfFiles) {
            if (f.isDirectory()) {
                String name = f.getName();
                if (name.equals(currBranchName)) {
                    System.out.print("*");
                }
                System.out.println(name);

            }
        }

        System.out.println("");

        Staged stagedFiles = GitletUtils.getStaged();
        System.out.println("=== Staged Files ===");
        for (String s : stagedFiles.allFiles()) {
            System.out.println(s);
        }
        System.out.println("");

        toRemove toRemove = GitletUtils.getToRemove();
        System.out.println("=== Files Marked for Removal ===");
        for (String s : toRemove.allFiles()) {
            System.out.println(s);
        }

    }

    private static void checkoutBranchStuff(String val) {
        Branch currBranch = GitletUtils.getCurrBranch();

        if (val.equals(currBranch.getBranchName())) {
            System.out.println("No need to checkout the current branch.");
            return;
        }

        Branch targetBranch = GitletUtils.getBranch(val);
        Commit targetCommit = GitletUtils.getHead(targetBranch);

        for (String origLocation : targetCommit.getFileLocations().keySet()) {
            String newLocation = GitletUtils.getFileLocation(targetCommit, origLocation);
            GitletUtils.fileCopy(newLocation, origLocation);
        }

        targetBranch.serialize(".gitlet/branches/currBranch.ser");
        return;
    }

    private static void checkoutCommitStuff(String val) {
        Commit lastCommit = GitletUtils.getLastCommit();
        boolean fileInLastCommit = lastCommit.contains(val);
        
        if (fileInLastCommit) {
            String commitVersionFile = GitletUtils.getFileLocation(lastCommit, val);
            GitletUtils.fileCopy(commitVersionFile, val);
            return;
        } else {
            String msg = "File does not exist in the most recent commit, or no such branch exists.";
            System.out.println(msg);
            return;
        }
    }



    private static void checkOutHelper(String val) {
        /* checking if val is a current branch */
        File branch = new File(".gitlet/branches/" + val);
        if (branch.exists()) {
            checkoutBranchStuff(val);
            return;
        } else {
            checkoutCommitStuff(val);
            return;
        }



    }

    private static void checkOutHelper2(String id, String fileName) {
        String msg = GitletUtils.getMessage(id);
        File newFile = new File(".gitlet/commits/" + msg + "/" + id + "/" + id + ".ser");
        if (!newFile.exists()) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit c = GitletUtils.getCommit(id);
        if (c.contains(fileName)) {
            String commitVersionFile = GitletUtils.getFileLocation(c, fileName);
            GitletUtils.fileCopy(commitVersionFile, fileName);
            return;
        } else {
            System.out.println("File does not exist in that commit.");
            return;
        }
    }

    private static void branch(String branchName) {
        String fileLocation = GitletUtils.getBranchFileName(branchName);
        File newFile = new File(fileLocation);

        if (newFile.exists()) {
            System.out.println("A branch with that name already exists.");
            return;
        }

        Commit newHead = GitletUtils.getLastCommit();
        Branch newBranch = new Branch(branchName, newHead.getID());
        newFile.getParentFile().mkdirs();
        newBranch.serialize(fileLocation);
        return;

    }

    private static void rmBranch(String branchName) {
        String branchLocation = GitletUtils.getBranchFileName(branchName);
        File newFile = new File(branchLocation);

        if (!newFile.exists()) {
            System.out.println("A branch with that name does not exist.");
        } else if (branchName.equals(GitletUtils.getCurrBranch().getBranchName())) {
            System.out.println("Cannot remove the current branch.");
        } else {
            GitletUtils.recursiveDelete(newFile.getParentFile());
        }
        return;
    }

    private static void reset(String id) {
        String msg = GitletUtils.getMessage(id);
        File commitFile = new File(".gitlet/commits/" + msg + "/" + id + "/" + id + ".ser");

        if (!commitFile.exists()) {
            System.out.println("No commit with that id exists.");
            return;
        }

        Commit c = GitletUtils.getCommit(id);

        for (String fileName : c.getFileLocations().keySet()) {
            String commitVersionFile = GitletUtils.getFileLocation(c, fileName);
            GitletUtils.fileCopy(commitVersionFile, fileName);
            
        }
        
        String newHeadID = c.getID();

        GitletUtils.updateCurrBranchHead(newHeadID);

        Branch currBranch = GitletUtils.getCurrBranch();
        GitletUtils.updateBranchHead(currBranch.getBranchName(), newHeadID);
        return;
        
    }
    /**
    * Creates two ArrayList<Commit> from the history
    * of the commits of the current Branch and the commits of branchName.
    * Then it uses the compareTo method and iterates through the two lists to find the split point
    */
    public static Commit mergeHelper(String branchName) {
        Branch currBranchPointer = GitletUtils.getCurrBranch();
        Branch branchPointer = GitletUtils.getBranch(branchName);

        ArrayList<Commit> currBranchHistory = new ArrayList<Commit>();
        ArrayList<Commit> targetBranchHistory = new ArrayList<Commit>();

        Commit currBranchHeadCommit = GitletUtils.getHead(currBranchPointer);
        Commit targetBranchHeadCommit = GitletUtils.getHead(branchPointer);

        while (currBranchHeadCommit != null) {
            currBranchHistory.add(currBranchHeadCommit);
            currBranchHeadCommit = GitletUtils.getPrevCommit(currBranchHeadCommit);
        }

        while (targetBranchHeadCommit != null) {
            targetBranchHistory.add(targetBranchHeadCommit);
            targetBranchHeadCommit = GitletUtils.getPrevCommit(targetBranchHeadCommit);
        }

        int counter = 0;
        int i = 0;
        int j = 0;
        

        while (counter <= currBranchHistory.size() + targetBranchHistory.size()) {
            if (currBranchHistory.get(i).compareTo(targetBranchHistory.get(j)) < 0) {
                j++;
            } else if (currBranchHistory.get(i).compareTo(targetBranchHistory.get(j)) > 0) {
                i++;
            } else {
                return targetBranchHistory.get(j);
            }
            counter++;

        }
        return null;

    }

    private static void merge(String branchName) {
        File branchFile = new File(GitletUtils.getBranchFileName(branchName));
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        } else if (GitletUtils.getCurrBranch().getBranchName().equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }

        Branch branch = GitletUtils.getBranch(branchName);
        
        Commit branchHead = GitletUtils.getHead(branch);
        Commit currBranchHead = GitletUtils.getHead(GitletUtils.getCurrBranch());
        Commit splitPoint = mergeHelper(branchName);

        for (String file : branchHead.getFileLocations().keySet()) {
            boolean currChanged = GitletUtils.changedSinceSplit(splitPoint, currBranchHead, file);
            boolean givenChanged = GitletUtils.changedSinceSplit(splitPoint, branchHead, file);

            if (currChanged && givenChanged) {
                HashMap<String, String> splitMap = splitPoint.getFileLocations();


                String splitPointID = splitMap.get(file);
                String givenBranchID = branchHead.getFileLocations().get(file);
                Commit givenLocation = GitletUtils.getCommit(givenBranchID);
                String givenBranchFileLocation = GitletUtils.getFileLocation(givenLocation, file);

                GitletUtils.fileCopy(givenBranchFileLocation, file + ".conflicted");
            } else if (!currChanged && givenChanged) {
                String splitPointID = splitPoint.getFileLocations().get(file);
                String givenBranchID = branchHead.getFileLocations().get(file);
                Commit givenLocation = GitletUtils.getCommit(givenBranchID);
                String givenBranchFileLocation = GitletUtils.getFileLocation(givenLocation, file);

                GitletUtils.fileCopy(givenBranchFileLocation, file);
            }

        }

        return;

    }

    private static void rebase(String targetBranch) {

        File branchFile = new File(GitletUtils.getBranchFileName(targetBranch));
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        } else if (targetBranch.equals(GitletUtils.getCurrBranch().getBranchName())) {
            System.out.println("Cannot rebase a branch onto itself.");
            return;
        }


        Commit splitPoint = mergeHelper(targetBranch);
        Branch actualBranch = GitletUtils.getBranch(targetBranch);

        if (splitPoint.getID().equals(GitletUtils.getHead(actualBranch).getID())) {
            System.out.println("Already up-to-date.");
            return;
        } else if (splitPoint.getID().equals(GitletUtils.getLastCommit().getID())) {
            String name = GitletUtils.getCurrBranch().getBranchName();
            GitletUtils.updateBranchHead(name, GitletUtils.getHead(actualBranch).getID());
            return;
        }

        ArrayList<Commit> currBranchHistory = new ArrayList<Commit>();
        Commit commitPointer = GitletUtils.getLastCommit();

        while (!commitPointer.getID().equals(splitPoint.getID())) {
            currBranchHistory.add(commitPointer);
            commitPointer = GitletUtils.getPrevCommit(commitPointer);
        }

        Commit targetBranchHead = GitletUtils.getHead(GitletUtils.getBranch(targetBranch));

        Commit prevCommit = targetBranchHead;
        for (int i = currBranchHistory.size() - 1; i >= 0; i--) {
            Commit toCopyCommit = currBranchHistory.get(i);

            String newMSG = toCopyCommit.getMsg();
            HashMap<String, String> map = toCopyCommit.getFileLocations();
            Commit copiedCommit = new Commit(newMSG, prevCommit.getID(), map);

            for (String file : targetBranchHead.getFileLocations().keySet()) {
                boolean tarchg = GitletUtils.changedSinceSplit(splitPoint, targetBranchHead, file);
                boolean currchg = GitletUtils.changedSinceSplit(splitPoint, toCopyCommit, file);

                if (tarchg && !currchg) {

                    HashMap<String, String> map2 = targetBranchHead.getFileLocations();
                    Commit targetCommit = GitletUtils.getCommit(map2.get(file));
                    String newLocation = GitletUtils.getFileLocation(targetCommit, file);
                    copiedCommit.getFileLocations().put(file, newLocation);

                    
                    GitletUtils.fileCopy(newLocation, file);
                    
                    
                }

            }

            prevCommit = copiedCommit;

            if (i == 0) {
                GitletUtils.updateCurrBranchHead(copiedCommit.getID());

                String currBranchName = GitletUtils.getCurrBranch().getBranchName();
                GitletUtils.updateBranchHead(currBranchName, copiedCommit.getID());
            }


        }
    }

    private static void iRebase(String targetBranch) {
        File branchFile = new File(GitletUtils.getBranchFileName(targetBranch));
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        } else if (targetBranch.equals(GitletUtils.getCurrBranch().getBranchName())) {
            System.out.println("Cannot rebase a branch onto itself.");
            return;
        }
        Commit splitPoint = mergeHelper(targetBranch);
        Branch actualBranch = GitletUtils.getBranch(targetBranch);

        if (splitPoint.getID().equals(GitletUtils.getHead(actualBranch).getID())) {
            System.out.println("Already up-to-date.");
            return;
        } else if (splitPoint.getID().equals(GitletUtils.getLastCommit().getID())) {
            String brnchname = GitletUtils.getCurrBranch().getBranchName();
            GitletUtils.updateBranchHead(brnchname, GitletUtils.getHead(actualBranch).getID());
            return;
        }

        ArrayList<Commit> currBranchHistory = new ArrayList<Commit>();
        Commit commitPointer = GitletUtils.getLastCommit();

        while (commitPointer != splitPoint) {
            currBranchHistory.add(commitPointer);
            commitPointer = GitletUtils.getPrevCommit(commitPointer);
        }

        Commit targetBranchHead = GitletUtils.getHead(GitletUtils.getBranch(targetBranch));

        Commit prevCommit = targetBranchHead;
        for (int i = currBranchHistory.size() - 1; i >= 0; i--) {

            Commit toCopyCommit = currBranchHistory.get(i);

            System.out.println("Currently replaying:");
            System.out.println("====");
            System.out.println("Commit " + toCopyCommit.getID() + ".");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(sdf.format(toCopyCommit.getTime().getTime()));
            System.out.println(toCopyCommit.getMsg());
            System.out.println("");
            String msg1 = "Would you like to (c)ontinue,";
            String msg2 = " (s)kip this commit, or change this commit's (m)essage?";
            System.out.println(msg1 + msg2);
            String response = System.console().readLine();

            while (!response.equals("c") && !response.equals("s") && !response.equals("m")) {
                response = System.console().readLine();
            }


            while (response.equals("s") && (i == 0 || i == currBranchHistory.size() - 1)) {
                response = System.console().readLine();
            }

            if (response.equals("c")) {
                String msg = toCopyCommit.getMsg();
                HashMap<String, String> map = toCopyCommit.getFileLocations();
                Commit copiedCommit = new Commit(msg, prevCommit.getID(), map);
                for (String file : targetBranchHead.getFileLocations().keySet()) {
                    boolean ct = GitletUtils.changedSinceSplit(splitPoint, targetBranchHead, file);
                    boolean cc = GitletUtils.changedSinceSplit(splitPoint, copiedCommit, file);

                    if (ct && !cc) {
                        String newLocation = targetBranchHead.getFileLocations().get(file);
                        copiedCommit.getFileLocations().put(file, newLocation);

                        if (i == 0) {
                            GitletUtils.fileCopy(file, newLocation);
                        }

                    }

                }

            } else if (response.equals("m")) {
                System.out.println("Please enter a new message for this commit.");
                response = System.console().readLine();
                String oldid = prevCommit.getID();
                Commit copiedCommit = new Commit(response, oldid, toCopyCommit.getFileLocations());
                for (String file : targetBranchHead.getFileLocations().keySet()) {
                    boolean ct = GitletUtils.changedSinceSplit(splitPoint, targetBranchHead, file);
                    boolean cc = GitletUtils.changedSinceSplit(splitPoint, copiedCommit, file);

                    if (ct && !cc) {
                        String newLocation = targetBranchHead.getFileLocations().get(file);
                        copiedCommit.getFileLocations().put(file, newLocation);

                        if (i == 0) {
                            GitletUtils.fileCopy(file, newLocation);
                        }

                    }

                }
                prevCommit = copiedCommit;

                if (i == 0) {
                    GitletUtils.updateCurrBranchHead(copiedCommit.getID());

                    String currBranchName = GitletUtils.getCurrBranch().getBranchName();
                    GitletUtils.updateBranchHead(currBranchName, copiedCommit.getID());
                }
            
            }
        }
    }

            
        
    
        








    
    public static void main(String[] args) {
        String command = args[0];
        switch (command) {
            case "init":
                init();
                break;
            case "add":
                add(args[1]);
                break;
            case "commit":
                if (args.length == 1 || args[1].equals("")) {
                    System.out.println("Please enter a commit message.");
                    break;
                }
                String message = args[1];
                commit(message);
                break;
            case "rm":
                String fileName2 = args[1];
                rm(fileName2);
                break;
            case "log":
                log();
                break;
            case "global-log":
                globalLog();
                break;
            case "find":
                find(args[1]);
                break;
            case "status":
                status();
                break;
            case "checkout":
                if (dangerous()) {
                    if (args.length == 3) {
                        String id = args[1];
                        String fileName3 = args[2];
                        checkOutHelper2(id, fileName3);
                        break;
                    }
                    String val = args[1];
                    checkOutHelper(val);
                }
                break;
            case "branch":
                branch(args[1]);
                break;
            case "rm-branch":
                rmBranch(args[1]);
                break;
            case "reset":
                if (dangerous()) {
                    String commitID = args[1];
                    reset(commitID);
                }
                break;
            case "merge":
                if (dangerous()) {  
                    String mergeFrom = args[1];
                    merge(mergeFrom);
                }
                break;
            case "rebase":
                if (dangerous()) {
                    String rebaseTo = args[1];
                    rebase(rebaseTo);
                }   
                break;
            case "i-rebase":
                if (dangerous()) {
                    String brannch = args[1];
                    iRebase(brannch);
                    break;
                }
            default:
                System.out.println("Invalid Command.");
        } 
    }
}
