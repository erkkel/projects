import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Branch implements Serializable {
    private String branchName;
    private String branchHead;

    public Branch(String currentBranchName, String headID) {
        this.branchName = currentBranchName;
        this.branchHead = headID;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getBranchHead() {
        return branchHead;
    }

    public void setBranchHead(String newHead) {
        branchHead = newHead;
    }

    public void serialize(String fileName) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            fileOut.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
