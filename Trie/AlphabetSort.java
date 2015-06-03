import java.util.Scanner;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.HashSet;
/** 
* Implements alphabet sorting functionality by means of a trie
* @author Eric Wang
*/
public class AlphabetSort {

    /**
    * Calls alphabet sort on the elements of the trie t
    * @param t the trie whose elements we will be alphabet sorting
    * @param alphabet the alphabet whose ordering we will impose on the elements of t
    */
    public static void aSort(Trie t, String alphabet) {
        TreeMap<Integer, Character> order = t.getOrder(t.getHead(), alphabet);
        for (Integer i : order.keySet()) {
            ArrayList<String> lst = t.aSort(t.getHead().get(order.get(i)), alphabet);
            for (String s : lst) {
                System.out.println(s);
            }
        }
    }
    
    /** 
    * puts elements of our input file into a trie and runs the alphabet sorting algorithm
    * @param args empty argument
    */
    public static void main(String[] args) {
        Trie t = new Trie();
        Scanner in = new Scanner(System.in);
        String alphabet = in.nextLine();
        HashSet<Character> dupChecker = new HashSet<Character>();   

        for (char c : alphabet.toCharArray()) {
            dupChecker.add(c);
        }
        if (dupChecker.size() != alphabet.length()) {
            throw new IllegalArgumentException();
        }

        int lineCounter = 0;
        while (in.hasNextLine()) {
            t.insert(in.nextLine());
            lineCounter++;
        }
        if (lineCounter == 0) {
            throw new IllegalArgumentException();
        }

        aSort(t, alphabet);


    }
}
