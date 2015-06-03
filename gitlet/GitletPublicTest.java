import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

/**
 * Class that provides JUnit tests for Gitlet, as well as a couple of utility
 * methods.
 * 
 * @author Joseph Moghadam
 * 
 *         Some code adapted from StackOverflow:
 * 
 *         http://stackoverflow.com/questions
 *         /779519/delete-files-recursively-in-java
 * 
 *         http://stackoverflow.com/questions/326390/how-to-create-a-java-string
 *         -from-the-contents-of-a-file
 * 
 *         http://stackoverflow.com/questions/1119385/junit-test-for-system-out-
 *         println
 * 
 */
public class GitletPublicTest {
    private static final String GITLET_DIR = ".gitlet/";
    private static final String TESTING_DIR = "test_files/";

    /* matches either unix/mac or windows line separators */
    private static final String LINE_SEPARATOR = "\r\n|[\r\n]";

    /**
     * Deletes existing gitlet system, resets the folder that stores files used
     * in testing.
     * 
     * This method runs before every @Test method. This is important to enforce
     * that all tests are independent and do not interact with one another.
     */
    @Before
    public void setUp() {
        File f = new File(GITLET_DIR);
        if (f.exists()) {
            recursiveDelete(f);
        }
        f = new File(TESTING_DIR);
        if (f.exists()) {
            recursiveDelete(f);
        }
        f.mkdirs();
    }

    /**
     * Tests that init creates a .gitlet directory. Does NOT test that init
     * creates an initial commit, which is the other functionality of init.
     * code adapted from Proj2 Video
     */


    @Test
    public void testBasicInitialize() {
        gitlet("init");
        File f = new File(GITLET_DIR);
        assertTrue(f.exists());
        String output = gitlet("init");
        String msg = "A gitlet version control system already exists in the current directory.";
        boolean styleblah = output.contains(msg);
        assertTrue(styleblah);
    }
    /* Tests the functionality of add. Checks that add works on initial comm
        it, and checks that add errors out 
        when trying to add a file that does not exist. */
    @Test
    public void testAdd() {
        gitlet("init");
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("add", wugFileName);
        Staged stagedFile = (Staged) GitletUtils.deSerialize(".gitlet/staged.ser");
        assertTrue(stagedFile.contains(wugFileName));
        String output = gitlet("add", TESTING_DIR + "wug1.txt");
        assertTrue(output.contains("File does not exist."));

    }



    /*tries commiting wug.txt*/
    @Test 
    public void testCommit() {
        gitlet("init");
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");
        String nm = ".gitlet/commits/added wug/added wugewang1/added wugewang1.ser";
        Commit newCommit = (Commit) GitletUtils.deSerialize(nm);
        HashMap<String, String> fileLocations = newCommit.getFileLocations();
        assertTrue(fileLocations.containsKey(wugFileName));
        assertTrue(fileLocations.get(wugFileName).equals("added wugewang1"));
        
        Staged stagedFiles = GitletUtils.getStaged();
        assertTrue(stagedFiles.isEmpty());

        toRemove toRemoveFiles = GitletUtils.getToRemove();
        assertTrue(toRemoveFiles.isEmpty());

        Branch currBranch = GitletUtils.getCurrBranch();
        Branch directoryBranch = GitletUtils.getBranch(currBranch.getBranchName());

        assertTrue(currBranch.getBranchHead().equals("added wugewang1"));
        assertTrue(directoryBranch.getBranchHead().equals("added wugewang1"));

        File wug = new File(wugFileName);
    
        String output = gitlet("add", wugFileName);
        assertTrue(output.contains("File has not been modified since the last commit."));
        
        String output2 = gitlet("commit", "lala");
        assertTrue(output2.contains("No changes added to the commit"));

        String output3 = gitlet("commit");
        assertTrue(output3.contains("Please enter a commit message."));

        String output4 = gitlet("commit", "");
        assertTrue(output4.contains("Please enter a commit message."));


    }


    @Test
    public void testRemove() {
        gitlet("init");
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("add", wugFileName);
        gitlet("rm", wugFileName);
        
        Staged stagedFiles = GitletUtils.getStaged();
        toRemove toRemoveFiles = GitletUtils.getToRemove();

        assertTrue(stagedFiles.isEmpty());
        assertTrue(toRemoveFiles.isEmpty());

        
    
    }

    @Test
    public void testRemoveIntense() {
        gitlet("init");
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        
        String output = gitlet("rm", wugFileName);
        assertTrue(output.contains("No reason to remove the file."));

        gitlet("add", wugFileName);
        gitlet("commit", "added wug");

        Staged stagedFiles = GitletUtils.getStaged();
        toRemove toRemoveFiles = GitletUtils.getToRemove();
        
        assertTrue(stagedFiles.isEmpty());
        assertTrue(toRemoveFiles.isEmpty());

        gitlet("rm", wugFileName);

        toRemoveFiles = GitletUtils.getToRemove();

        assertTrue(toRemoveFiles.contains(wugFileName));
        gitlet("commit", "removed wug");

        Commit last = GitletUtils.getLastCommit();
        assertTrue(!last.contains(wugFileName));

    }
    /**
     * Tests that checking out a file name will restore the version of the file
     * from the previous commit. Involves init, add, commit, and checkout.
     */
    
    

    /**
     * Tests that log prints out commit messages in the right order. Involves
     * init, add, commit, and log.
     */
    @Test
    public void testBasicLog() {
        gitlet("init");
        String commitMessage1 = "initial commit";
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("add", wugFileName);
        String commitMessage2 = "added wug";
        gitlet("commit", commitMessage2);

        String logContent = gitlet("log");
        assertArrayEquals(new String[] { commitMessage2, commitMessage1 },
                extractCommitMessages(logContent));
    }


    /* EXPAND THESE LATER ON, when branch/checkout are implemented */
    @Test
    public void testGlobalLog() {
        gitlet("init");
        String commitMessage1 = "initial commit";
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("add", wugFileName);
        String commitMessage2 = "added wug";
        gitlet("commit", commitMessage2);
        String logContent = gitlet("global-log");
        System.out.println(logContent);

    }

    @Test
    public void find() {
        gitlet("init");
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");

        String id = gitlet("find", "blergh");
        assertTrue(id.contains("Found no commit with that message."));

        id = gitlet("find", "added wug");
        assertTrue(id.contains("added wugewang1"));
    }


    /*adds multiples files with the same msg. */
    @Test
    public void findIntense() {
        gitlet("init");
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        
        createFile(wugFileName, wugText);
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");
        
        createFile(TESTING_DIR + "wug1.txt", wugText);
        gitlet("add", TESTING_DIR + "wug1.txt");
        gitlet("commit", "added wug");
        
        createFile(TESTING_DIR + "wug2.txt", wugText);
        gitlet("add", TESTING_DIR + "wug2.txt");
        gitlet("commit", "added wug");

        createFile(TESTING_DIR + "wug3.txt", wugText);
        gitlet("add", TESTING_DIR + "wug3.txt");
        gitlet("commit", "added wug1");

        String id = gitlet("find", "added wug");

        assertTrue(id.contains("added wugewang1"));
        assertTrue(id.contains("added wugewang2"));
        assertTrue(id.contains("added wugewang3"));


    }
    @Test
    public void testStatus() {
        gitlet("init");
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        
        createFile(wugFileName, wugText);
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");
        
        createFile(TESTING_DIR + "wug1.txt", wugText);
        gitlet("add", TESTING_DIR + "wug1.txt");
        gitlet("commit", "added wug");
        
        createFile(TESTING_DIR + "wug2.txt", wugText);
        gitlet("add", TESTING_DIR + "wug2.txt");
        gitlet("commit", "added wug");

        createFile(TESTING_DIR + "wug3.txt", wugText);
        gitlet("add", TESTING_DIR + "wug3.txt");

        gitlet("rm", wugFileName);

        String output = gitlet("status");
        System.out.println(output);
    }

    @Test
    public void testBasicCheckout() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");
        writeFile(wugFileName, "This is not a wug.");

        File branch = new File(".gitlet/branches/" + wugFileName);
        assertEquals(false, branch.exists());

        Commit lastCommit = GitletUtils.getLastCommit();
        boolean fileInLastCommit = lastCommit.contains(wugFileName);
        assertTrue(fileInLastCommit);
        gitlet("checkout", wugFileName);
        assertEquals(wugText, getText(wugFileName));
    }

    @Test
    public void testCheckout() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");
        
        writeFile(wugFileName, "This is not a wug.");
        gitlet("add", wugFileName);
        gitlet("commit", "bloop");

        writeFile(wugFileName, "This is not a wug1.");
        gitlet("add", wugFileName);
        gitlet("commit", "bloopasd");

        writeFile(wugFileName, "This is not a wug2.");
        gitlet("add", wugFileName);
        gitlet("commit", "bloop123");

        gitlet("checkout", "bloopasdewang3", wugFileName);
        assertEquals("This is not a wug1.", getText(wugFileName));

        String output = gitlet("checkout", "DOESNOTEXIST", wugFileName);
        assertTrue(output.contains("No commit with that id exists."));

    }

    
    @Test
    public void testCheckoutIntense() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");
        
        writeFile(wugFileName, "This is not a wug.");
        gitlet("add", wugFileName);
        gitlet("commit", "bloop");

        writeFile(wugFileName, "This is not a wug1.");
        gitlet("add", wugFileName);
        gitlet("commit", "bloopasd");

        writeFile(wugFileName, "CHECKOUTDOESNOTWORK");
        gitlet("add", wugFileName);
        gitlet("commit", "bloop123");

        writeFile(wugFileName, "CHECKOUTWORKS");
        gitlet("add", wugFileName);
        gitlet("commit", "asds");
       
        String error = gitlet("checkout", "master");
        
        assertTrue(error.contains("No need to checkout the current branch."));
        assertEquals("CHECKOUTWORKS", getText(wugFileName));

        String output = gitlet("checkout", "BRANCHDOESNOTEXIST");
        String nm = "File does not exist in the most recent commit, or no such branch exists.";
        assertTrue(output.contains(nm));

        gitlet("branch", "newBranch");  
        gitlet("checkout", "newBranch");

        writeFile(wugFileName, "testing newBranch");
        gitlet("add", wugFileName);
        gitlet("commit", "asdad");

        gitlet("checkout", "master");
        assertEquals("CHECKOUTWORKS", getText(wugFileName));
        gitlet("checkout", "newBranch");
        assertEquals("testing newBranch", getText(wugFileName));

    }

    @Test
    public void checkoutExtraIntense() {
        String wugFileName1 = TESTING_DIR + "wug1.txt";
        String wugFileName2 = TESTING_DIR + "wug2.txt";
        String wugText1 = "wug1";
        String wugText2 = "wug2";
        createFile(wugFileName1, wugText1);
        createFile(wugFileName2, wugText2);
        gitlet("init");
        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit1");

        gitlet("branch", "newBranch");
        gitlet("checkout", "newBranch");

        assertEquals("newBranch", GitletUtils.getCurrBranch().getBranchName());

        wugText1 = "newBranch wug1";
        wugText2 = "newBranch wug2";
        writeFile(wugFileName1, wugText1);
        writeFile(wugFileName2, wugText2);
        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit2");
        assertEquals("newBranch", GitletUtils.getCurrBranch().getBranchName());
        wugText1 = "newBranch2 wug1";
        wugText2 = "newBranch2 wug2";
        writeFile(wugFileName1, wugText1);
        writeFile(wugFileName2, wugText2);

        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit2");

        assertEquals("newBranch", GitletUtils.getCurrBranch().getBranchName());

        gitlet("checkout", "master");
        
        assertEquals("master", GitletUtils.getCurrBranch().getBranchName());
        assertEquals("wug1", getText(wugFileName1));
        assertEquals("wug2", getText(wugFileName2));

        wugText1 = "master wug1";
        wugText2 = "master wug2";
        writeFile(wugFileName1, wugText1);
        writeFile(wugFileName2, wugText2);

        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit3");

        wugText1 = "master2 wug1";
        wugText2 = "master2 wug2";
        writeFile(wugFileName1, wugText1);
        writeFile(wugFileName2, wugText2);

        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit4");

        gitlet("checkout", "newBranch");

        assertEquals("newBranch2 wug1", getText(wugFileName1));
        assertEquals("newBranch2 wug2", getText(wugFileName2));

        gitlet("checkout", "master");

        assertEquals("master2 wug1", getText(wugFileName1));
        assertEquals("master2 wug2", getText(wugFileName2));

        writeFile(wugFileName1, "asdjkasd");
        writeFile(wugFileName2, "asdasd");

        gitlet("checkout", wugFileName1);
        gitlet("checkout", wugFileName2);

        assertEquals("master2 wug1", getText(wugFileName1));
        assertEquals("master2 wug2", getText(wugFileName2));

    }

    /*also tests that the checkout [branch name] commands works */
    @Test
    public void testBranch() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");

        gitlet("branch", "newBranch");
        String location = GitletUtils.getBranchFileName("newBranch");
        File newFile = new File(location);
        assertTrue(newFile.exists());

        Branch newBranch = GitletUtils.getBranch("newBranch");
        assertEquals("newBranch", newBranch.getBranchName());

        Commit current = GitletUtils.getLastCommit();
        assertEquals(newBranch.getBranchHead(), "added wugewang1");

        //currently on the master branch

        writeFile(wugFileName, "This is not a wug.");
        gitlet("add", wugFileName);
        gitlet("commit", "now not a wug");

        gitlet("checkout", "newBranch");
        assertEquals("This is a wug.", getText(wugFileName));

        gitlet("checkout", "master");
        assertEquals("This is not a wug.", getText(wugFileName));

        String output = gitlet("branch", "master");
        assertTrue(output.contains("A branch with that name already exists."));
    }

    @Test
    public void testRemoveBranch() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");
        gitlet("branch", "newBranch");

        String location = GitletUtils.getBranchFileName("newBranch");
        File newFile = new File(location);
        
        assertTrue(newFile.exists());

        gitlet("rm-branch", "newBranch");
        assertTrue(!newFile.exists());

        String output = gitlet("rm-branch", "master");
        assertTrue(output.contains("Cannot remove the current branch."));

        String out = gitlet("rm-branch", "branchThatDoesNotExist");
        assertTrue(out.contains("A branch with that name does not exist."));
    }

    @Test
    public void testReset() {
        String wugFileName1 = TESTING_DIR + "wug1.txt";
        String wugFileName2 = TESTING_DIR + "wug2.txt";
        String wugFileName3 = TESTING_DIR + "wug3.txt";
        String wugFileName4 = TESTING_DIR + "wug4.txt";
        
        String wugText1 = "This is a wug1.";
        String wugText2 = "This is a wug2.";
        String wugText3 = "This is a wug3.";
        String wugText4 = "This is a wug4.";
        
        createFile(wugFileName1, wugText1);
        createFile(wugFileName2, wugText2);
        createFile(wugFileName3, wugText3);
        createFile(wugFileName4, wugText4);

        gitlet("init");
        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("add", wugFileName3);
        gitlet("add", wugFileName4);
        gitlet("commit", "added wugs");

        writeFile(wugFileName1, "This is not a wug1.");
        writeFile(wugFileName2, "This is not a wug2.");
        writeFile(wugFileName3, "This is not a wug3.");
        writeFile(wugFileName4, "This is not a wug4.");

        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("add", wugFileName3);
        gitlet("add", wugFileName4);
        gitlet("commit", "added not wugs");

        writeFile(wugFileName1, "SHOULD NOT APPEAR1");
        writeFile(wugFileName2, "SHOULD NOT APPEAR2.");
        writeFile(wugFileName3, "SHOULD NOT APPEAR3.");
        writeFile(wugFileName4, "SHOULD NOT APPEAR4.");

        gitlet("reset", "added wugsewang1");
        assertEquals("This is a wug1.", getText(wugFileName1));
        assertEquals("This is a wug2.", getText(wugFileName2));
        assertEquals("This is a wug3.", getText(wugFileName3));
        assertEquals("This is a wug4.", getText(wugFileName4));

    }

    @Test 
    public void splitPointTest() {
        String wugFileName1 = TESTING_DIR + "wug1.txt/";
        String wugFileName2 = TESTING_DIR + "wug2.txt/";
        String wugFileName3 = TESTING_DIR + "wug3.txt/";
        String wugFileName4 = TESTING_DIR + "wug4.txt/";
        
        String wugText1 = "This is a wug1.";
        String wugText2 = "This is a wug2.";
        String wugText3 = "This is a wug3.";
        
        createFile(wugFileName1, wugText1);
        createFile(wugFileName2, wugText2);
        createFile(wugFileName3, wugText3);

        gitlet("init");
        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("add", wugFileName3);
        gitlet("commit", "added wugs");

        writeFile(wugFileName1, "This is not a wug1.");
        writeFile(wugFileName2, "This is not a wug2.");
        writeFile(wugFileName3, "This is not a wug3.");

        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("add", wugFileName3);
        gitlet("commit", "added not wugs");

        gitlet("branch", "newBranch");
        gitlet("checkout", "newBranch");

        writeFile(wugFileName2, "SHOULD APPEAR2.");
        writeFile(wugFileName3, "SHOULD APPEAR3.");

        gitlet("add", wugFileName2);
        gitlet("add", wugFileName3);
        gitlet("commit", "added SHOULDs");

        gitlet("checkout", "master");
        
        createFile(wugFileName4, "This is a wug4.");
        writeFile(wugFileName1, "SHOULD APPEAR1.");
        writeFile(wugFileName2, "SHOULD BE CONFLICTED.");
        writeFile(wugFileName3, "SHOULD BE CONFLICTED.");

        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("add", wugFileName3);
        gitlet("add", wugFileName4);
        gitlet("commit", "lala");
        gitlet("checkout", "newBranch");

        assertEquals("added not wugsewang2", Gitlet.mergeHelper("master").getID());


    }
    
    @Test
    public void testMergeIntense() {
        String wugFileName1 = TESTING_DIR + "wug1.txt";
        String wugFileName2 = TESTING_DIR + "wug2.txt";
        String wugText1 = "wug1";
        String wugText2 = "wug2";
        createFile(wugFileName1, wugText1);
        createFile(wugFileName2, wugText2);
        gitlet("init");
        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit1");

        gitlet("branch", "newBranch");
        gitlet("checkout", "newBranch");

        wugText1 = "newBranch wug1";
        wugText2 = "newBranch wug2";
        writeFile(wugFileName1, wugText1);
        writeFile(wugFileName2, wugText2);

        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit2");
        wugText1 = "newBranch2 wug1";
        wugText2 = "newBranch2 wug2";
        writeFile(wugFileName1, wugText1);
        writeFile(wugFileName2, wugText2);

        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit2");

        gitlet("checkout", "master");
        wugText1 = "master wug1";
        wugText2 = "master wug2";
        writeFile(wugFileName1, wugText1);
        writeFile(wugFileName2, wugText2);

        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit3");

        wugText1 = "master2 wug1";
        wugText2 = "master2 wug2";
        writeFile(wugFileName1, wugText1);
        writeFile(wugFileName2, wugText2);

        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit4");

        gitlet("checkout", "newBranch");
        gitlet("merge", "master");

        assertEquals("newBranch2 wug1", getText(wugFileName1));
        assertEquals("newBranch2 wug2", getText(wugFileName2));

    }

    @Test
    public void testMergeIntenser() {
        String wugFileName1 = TESTING_DIR + "wug1.txt";
        String wugFileName2 = TESTING_DIR + "wug2.txt";
        String wugText1 = "wug1";
        String wugText2 = "wug2";
        createFile(wugFileName1, wugText1);
        createFile(wugFileName2, wugText2);
        gitlet("init");
        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit1");

        gitlet("branch", "newBranch");
        gitlet("checkout", "newBranch");

        wugText2 = "newBranch wug2";
        writeFile(wugFileName2, wugText2);
        
        gitlet("add", wugFileName2);
        gitlet("commit", "commit2");
        wugText2 = "newBranch2 wug2";
        writeFile(wugFileName2, wugText2);

        gitlet("add", wugFileName2);
        gitlet("commit", "commit2");

        gitlet("checkout", "master");
        
        wugText2 = "master wug2";
        writeFile(wugFileName2, wugText2);

        gitlet("rm", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit3");

        wugText1 = "wug1";
        wugText2 = "master2 wug2";
        writeFile(wugFileName1, wugText1);
        writeFile(wugFileName2, wugText2);

        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit4");

        gitlet("checkout", "newBranch");
        gitlet("merge", "master");

        assertEquals("wug1", getText(wugFileName1));
        assertEquals("newBranch2 wug2", getText(wugFileName2));

        File newFile1 = new File("test_files/wug1.txt.conflicted");
        File newFile2 = new File("test_files/wug2.txt.conflicted");

        assertTrue(!newFile1.exists());
        assertTrue(newFile2.exists());

    }

    @Test
    public void testRebase() {
        String wugFileName1 = TESTING_DIR + "wug1.txt";
        String wugFileName2 = TESTING_DIR + "wug2.txt";
        String wugText1 = "wug1";
        String wugText2 = "wug2";
        createFile(wugFileName1, wugText1);
        createFile(wugFileName2, wugText2);
        gitlet("init");
        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit1");

        gitlet("branch", "newBranch");
        gitlet("checkout", "newBranch");

        wugText2 = "newBranch wug2";
        writeFile(wugFileName2, wugText2);

        gitlet("add", wugFileName2);
        gitlet("commit", "commit2");
       
        wugText2 = "newBranch2 wug2";
        writeFile(wugFileName2, wugText2);

        gitlet("add", wugFileName2);
        gitlet("commit", "commit3");

        gitlet("checkout", "master");
        wugText1 = "master wug1";
        wugText2 = "master wug2";
        writeFile(wugFileName1, wugText1);
        writeFile(wugFileName2, wugText2);

        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit4");

        wugText1 = "master2 wug1";
        wugText2 = "master2 wug2";
        writeFile(wugFileName1, wugText1);
        writeFile(wugFileName2, wugText2);

        gitlet("add", wugFileName1);
        gitlet("add", wugFileName2);
        gitlet("commit", "commit5");

        gitlet("checkout", "newBranch");

        gitlet("rebase", "master");

        assertEquals("master2 wug1", getText(wugFileName1));
        assertEquals("newBranch2 wug2", getText(wugFileName2));
        


    }

    /**
     * Convenience method for calling Gitlet's main. Anything that is printed
     * out during this call to main will NOT actually be printed out, but will
     * instead be returned as a string from this method.
     * 
     * Prepares a 'yes' answer on System.in so as to automatically pass through
     * dangerous commands.
     * 
     * The '...' syntax allows you to pass in an arbitrary number of String
     * arguments, which are packaged into a String[].
     */
    private static String gitlet(String... args) {
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        ByteArrayOutputStream printingResults = new ByteArrayOutputStream();
        try {
            /*
             * Below we change System.out, so that when you call
             * System.out.println(), it won't print to the screen, but will
             * instead be added to the printingResults object.
             */
            System.setOut(new PrintStream(printingResults));

            /*
             * Prepares the answer "yes" on System.In, to pretend as if a user
             * will type "yes". You won't be able to take user input during this
             * time.
             */
            String answer = "yes";
            InputStream is = new ByteArrayInputStream(answer.getBytes());
            System.setIn(is);

            /* Calls the main method using the input arguments. */
            Gitlet.main(args);

        } finally {
            /*
             * Restores System.out and System.in (So you can print normally and
             * take user input normally again).
             */
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
        return printingResults.toString();
    }

    /**
     * Returns the text from a standard text file (won't work with special
     * characters).
     */
    private static String getText(String fileName) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(fileName));
            return new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Creates a new file with the given fileName and gives it the text
     * fileText.
     */
    private static void createFile(String fileName, String fileText) {
        File f = new File(fileName);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeFile(fileName, fileText);
    }

    /**
     * Replaces all text in the existing file with the given text.
     */
    private static void writeFile(String fileName, String fileText) {
        FileWriter fw = null;
        try {
            File f = new File(fileName);
            fw = new FileWriter(f, false);
            fw.write(fileText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes the file and all files inside it, if it is a directory.
     */
    private static void recursiveDelete(File d) {
        if (d.isDirectory()) {
            for (File f : d.listFiles()) {
                recursiveDelete(f);
            }
        }
        d.delete();
    }

    /**
     * Returns an array of commit messages associated with what log has printed
     * out.
     */
    private static String[] extractCommitMessages(String logOutput) {
        String[] logChunks = logOutput.split("====");
        int numMessages = logChunks.length - 1;
        String[] messages = new String[numMessages];
        for (int i = 0; i < numMessages; i++) {
            System.out.println(logChunks[i + 1]);
            String[] logLines = logChunks[i + 1].split(LINE_SEPARATOR);
            messages[i] = logLines[3];
        }
        return messages;
    }
}
