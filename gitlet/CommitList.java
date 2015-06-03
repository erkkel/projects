import java.util.ArrayList;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CommitList implements Serializable {
    private ArrayList<String> commitIDs;

    public CommitList() {
        commitIDs = new ArrayList<String>();
    }


    public void add(String fileLocation) {
        commitIDs.add(fileLocation);
        this.serialize(".gitlet/commitList.ser");
    }

    public ArrayList<String> allFiles() {
        return commitIDs;
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
