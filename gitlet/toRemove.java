import java.util.ArrayList;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
public class toRemove implements Serializable {
    private ArrayList<String> toBeRemoved;

    public toRemove() {
        toBeRemoved = new ArrayList<String>();
    }

    public void add(String fileLocation) {
        toBeRemoved.add(fileLocation);
        this.serialize(".gitlet/toRemove.ser");
    }

    public void remove(String s) {
        toBeRemoved.remove(s);
        this.serialize(".gitlet/toRemove.ser");
    }

    public boolean contains(String s) {
        if (toBeRemoved.contains(s)) {
            return true;
        }
        return false;
    }

    public void wipe() {
        toBeRemoved = new ArrayList<String>();
        this.serialize(".gitlet/toRemove.ser");
    }

    public boolean isEmpty() {
        if (toBeRemoved.size() == 0) {
            return true;
        }
        return false;
    }

    public ArrayList<String> allFiles() {
        return toBeRemoved;
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
