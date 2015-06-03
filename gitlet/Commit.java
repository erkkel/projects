import java.util.Calendar;
import java.util.HashMap;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Commit implements Serializable, Comparable<Commit> {
    private String msg;
    private String prevCommitID;
    private HashMap<String, String> fileLocations; 
    private Calendar time;
    private String id;

    public static int counter; //Meant to help with the creation of IDs
    
    
    
    public Commit(String msg, String prevCommitID, HashMap<String, String> fileLocations) {
        this.msg = msg;
        this.prevCommitID = prevCommitID;
        this.fileLocations = fileLocations;
        time = Calendar.getInstance();


        //FIGURE OUT WAY TO GENERATE IDS
        this.id = msg + "ewang" + counter;
        counter += 1;

    }

    public String getMsg() {
        return msg;
    }

    public Calendar getTime() {
        return time;
    }

    public String getPrevCommitID() {
        return prevCommitID;
    }

    public void setFileLocations(HashMap<String, String> fileLocations) {
        this.fileLocations = fileLocations;
    }

    public HashMap<String, String> getFileLocations() {
        return fileLocations;
    }

    public String getID() {
        return id;
    }

    public boolean contains(String fileName) {
        return fileLocations.containsKey(fileName);
    }

    /** 
    *compares commits based on their ID numbers i.e.
    *"initial commitewang0" is less than "2nd commitewang1" because its counter 
    *at the end of the id is less
    */
        
    public int compareTo(Commit c) {
        String[] currComm = id.split("ewang");
        int currIDNum = Integer.parseInt(currComm[1]);

        String[] cComm = c.getID().split("ewang");
        int cIDNum = Integer.parseInt(cComm[1]);

        return currIDNum - cIDNum;
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

