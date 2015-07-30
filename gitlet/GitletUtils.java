import java.util.HashMap;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Path;


public class GitletUtils { 

    /* deserializes the .ser file at fileName*/
    public static Object deSerialize(String fileName) {

        try { 
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object o = in.readObject();
            fileIn.close();
            in.close();
            return o;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException f) {
            f.printStackTrace();
            return null;
        }
    }

    /*gets the DIRECTORY location of the head commit of branch b*/
    public static String getHeadLocation(Branch b) {
        String id = b.getBranchHead();
        String[] lst = id.split("ewang");
        String msg = lst[0];
        String location = ".gitlet/commits/" + msg + "/" + id + "/" + id + ".ser";
        return location;
    }


    

    /*gets the actual head commit of branch b*/
    public static Commit getHead(Branch b) {
        return (Commit) deSerialize(getHeadLocation(b));
    }


    /* returns the current branch you're in */
    public static Branch getCurrBranch() {
        return (Branch) deSerialize(".gitlet/branches/currBranch.ser");
    }

    /* returns the DIRECTORY location of currentbranch.ser */
    public static String getCurrBranchFileName() {
        return ".gitlet/branches/currBranch.ser";
    }
    
    /* returns the DIRECTORY branch of branchName */
    public static Branch getBranch(String branchName) {
        return (Branch) deSerialize(".gitlet/branches/" + branchName + "/" + branchName + ".ser");
    }

    /* returns the DIRECTORY of branchName as a String */
    public static String getBranchFileName(String branchName) {
        return ".gitlet/branches/" + branchName + "/" + branchName + ".ser";
    }


    public static Commit getCommit(String commitID) {
        String msg = getMessage(commitID);
        String nm = ".gitlet/commits/" + msg + "/" + commitID + "/" + commitID + ".ser";
        return (Commit) deSerialize(nm);
    }

    /* returns the last Commit in the current branch */
    public static Commit getLastCommit() {
        return getHead(getCurrBranch());
    }

    public static Commit getPrevCommit(Commit c) {
        String prevID = c.getPrevCommitID();
        if (prevID != null) {
            return getCommit(prevID);
        } else {
            return null;
        }
    }

    public static Staged getStaged() {
        return (Staged) deSerialize(".gitlet/staged.ser");
    }

    public static toRemove getToRemove() {
        return (toRemove) deSerialize(".gitlet/toRemove.ser");
    }

    public static CommitList getCommitList() {
        return (CommitList) deSerialize(".gitlet/commitList.ser");
    }

    public static String getFileName(String fileLocation) {
        String[] info = fileLocation.split("/");
        return info[info.length - 1];
    }

    /*Gets the file location of the *actual file* of the version in Commit c */
    public static String getFileLocation(Commit c, String fileName) {
        HashMap<String, String> fileLocations = c.getFileLocations();
        String id = fileLocations.get(fileName);
        String msg = getMessage(id);
        return ".gitlet/commits/" + msg + "/" + id + "/" + fileName;
    }

    /* returns the message from a commit ID */
    public static String getMessage(String id) {
        String[] info = id.split("ewang");
        return info[0];

    }

    public static void updateCurrBranchHead(String newHead) {
        Branch currBranch = GitletUtils.getCurrBranch();
        currBranch.setBranchHead(newHead);
        currBranch.serialize(getCurrBranchFileName());
    }

    /* updates the head of Branch b (NOT CURRBRANCH.SER) to be the newHead commit ID
    i.e. updateBranchHead(master, lala) -> makes the branch master's head into lala */
    public static void updateBranchHead(String branchName, String newHead) {
        Branch toChange = getBranch(branchName);
        toChange.setBranchHead(newHead);
        toChange.serialize(getBranchFileName(branchName));
    }

    /*if toRemove.ser contains fileName -> removes fileName from toRemove.ser and returns true
        else if toRemove.ser does not contain fileName -> return false;*/
    public static boolean toRemoveContains(String fileName) {
 
        toRemove removeList = (toRemove) deSerialize(".gitlet/toRemove.ser");
        ArrayList<String> toBeRemoved = removeList.allFiles();
        if (toBeRemoved.contains(fileName)) {
            toBeRemoved.remove(fileName);
            return true;
        } else {
            return false;
        }
       
    }
    

    /*copies the file from origLocation to endLocation */
    public static void fileCopy(String origLocation, String endLocation) {
        try {
            File orig = new File(origLocation);
            File destination = new File(endLocation);
            if (destination.exists()) {
                recursiveDelete(destination);    
            }
            
            Files.copy(orig.toPath(), destination.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Deletes the file and all files inside it, if it is a directory.
     * Taken from GitletPublicTest
     */

    public static void recursiveDelete(File d) {
        if (d.isDirectory()) {
            for (File f : d.listFiles()) {
                recursiveDelete(f);
            }
        }
        d.delete();
    }


    /* if fileName has been modified since last commit in current branch -> return true
        else -> return false */
    public static boolean modSinceLastCommit(String fileName) {
        Branch currBranch = getCurrBranch();
        Commit head = getHead(currBranch);

        HashMap<String, String> fileLocations = head.getFileLocations();

        if (!fileLocations.containsKey(fileName)) {
            return true;
        } else {
            try {
                File currFile = new File(fileName);
                String oldFileNameLocation = getFileLocation(head, fileName);
                File oldFile = new File(oldFileNameLocation);

                Path cr = currFile.toPath();
                Path of = oldFile.toPath();
                return !Arrays.equals(Files.readAllBytes(cr), Files.readAllBytes(of));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }


    public static boolean 
        changedSinceSplit(Commit splitPoint, Commit laterCommit, String fileName) {
        HashMap<String, String> splitPointFileLocations = splitPoint.getFileLocations();
        HashMap<String, String> laterCommitFileLocations = laterCommit.getFileLocations();

        if (!splitPointFileLocations.containsKey(fileName) 
            && laterCommitFileLocations.containsKey(fileName)) {
            return true;
        } else if (splitPointFileLocations.containsKey(fileName) 
            && !laterCommitFileLocations.containsKey(fileName)) { 

            Commit commitPointer = laterCommit;
            
            while (!commitPointer.getID().equals(splitPoint.getID())) {
                if (commitPointer.contains(fileName)) {
                    return true;
                }
                commitPointer = GitletUtils.getPrevCommit(commitPointer);
            }

            return false;
            
        } else if (!splitPointFileLocations.containsKey(fileName) 
            && !laterCommitFileLocations.containsKey(fileName)) { 
            Commit commitPointer = laterCommit;
            
            while (!commitPointer.getID().equals(splitPoint.getID())) {
                if (commitPointer.contains(fileName)) {
                    return true;
                }
                commitPointer = GitletUtils.getPrevCommit(commitPointer);
            }

            return false;



        } else {
            Commit commitPointer = laterCommit;
            HashMap<String, String> map = commitPointer.getFileLocations();
            
            while (!commitPointer.getID().equals(splitPoint.getID())) {
                if (!commitPointer.contains(fileName)) {
                    return true;
                
                } else if (!map.get(fileName).equals(splitPointFileLocations.get(fileName))) {
                    return true;
                    
                } 

                commitPointer = GitletUtils.getPrevCommit(commitPointer);
            }

            return false;

            
        }


    }


}
