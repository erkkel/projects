import java.util.ArrayList;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Staged implements Serializable {

    private ArrayList<String> stagedFiles;

    public Staged() {
        stagedFiles = new ArrayList<String>();
    }

    public void add(String fileLocation) {
        stagedFiles.add(fileLocation);
        this.serialize(".gitlet/staged.ser");
    }

    public void remove(String s) {
        stagedFiles.remove(s);
        this.serialize(".gitlet/staged.ser");
    }

    public void wipe() {
        stagedFiles = new ArrayList<String>();
        this.serialize(".gitlet/staged.ser");
    }

    public boolean contains(String fileLocation) {
        return stagedFiles.contains(fileLocation);
    }

    public ArrayList<String> allFiles() {
        return stagedFiles;
    }

    public boolean isEmpty() {
        if (stagedFiles.size() == 0) {
            return true;
        }
        return false;
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
