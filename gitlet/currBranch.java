import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class currBranch implements Serializable {
    private Branch currentBranch;

    public currBranch(Branch branch) {
        this.currentBranch = branch;
        
    }

    public Branch getCurrBranch() {
        return currentBranch;
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
