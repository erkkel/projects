import java.util.Iterator;
import java.util.HashSet;
import java.util.ArrayList;
/**
* Implements autocomplete on prefixes for a given dictionary of terms and weights.
* @author Eric Wang
*/
public class Autocomplete {

    private TernaryTrie t;
    /**
     * Initializes required data structures from parallel arrays.
     * @param terms Array of terms.
     * @param weights Array of weights.
     */
    public Autocomplete(String[] terms, double[] weights) {

        if (terms.length != weights.length) {
            throw new IllegalArgumentException();
        }
        HashSet<String> dupChecker = new HashSet<String>();
        t = new TernaryTrie();
        for (int i = 0; i < terms.length; i++) {
            if (weights[i] < 0) {
                throw new IllegalArgumentException();
            }
            t.insert(terms[i], weights[i]);
            dupChecker.add(terms[i]);
        }
        if (dupChecker.size() != terms.length) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Find the weight of a given term. If it is not in the dictionary, return 0.0
     * @param term the term we're looking for
     * @return the weight of the term
     */
    public double weightOf(String term) {
        return t.getWeight(term, t.getRoot());
    }

    /**
     * Find the maxChildWeight of a given term. If it is not in the dictionary, return 0.0
     * @param term the term we're looking for
     * @return the maxChildWeight of the term
     */
    public double maxChildWeight(String term) {
        return t.getMaxWeight(term, t.getRoot());
    }



    /**
     * Return the top match for given prefix, or null if there is no matching term.
     * @param prefix Input prefix to match against.
     * @return Best (highest weight) matching string in the dictionary.
     */
    public String topMatch(String prefix) {
        Iterable<String> matches = this.topMatches(prefix, 1);
        Iterator<String> iter = matches.iterator();
        if (iter.hasNext()) {
            return iter.next();
        }
        return null;
    }

    /**
     * Returns the top k matching terms (in descending order of weight) as an iterable.
     * If there are less than k matches, return all the matching terms.
     * @param prefix the prefix of the words we're looking for 
     * @param k The number of words with the prefix that we want to return
     * @return An iterable<String> of the elements with the given prefix with the highest weight
     */
    public Iterable<String> topMatches(String prefix, int k) {
        if (k < 0) {
            throw new IllegalArgumentException();
        }
        TernaryTrie.Node n = null;
        if (prefix.isEmpty()) {
            n = t.getRoot();
            return t.getMaxWordsEmpty(n, k, " ");
        } else {
            n = t.getNode(t.getRoot(), prefix);
            if (n == null) {
                return new ArrayList<String>();
            }
            return t.getMaxWords(n, k, prefix);
        }
        
    }
    

    /**
     * Returns the highest weighted matches within k edit distance of the word.
     * If the word is in the dictionary, then return an empty list.
     * @param word The word to spell-check
     * @param dist Maximum edit distance to search
     * @param k    Number of results to return 
     * @return Iterable in descending weight order of the matches
     */
    public Iterable<String> spellCheck(String word, int dist, int k) {
        return null;
    }
    /**
     * Test client. Reads the data from the file, 
     * then repeatedly reads autocomplete queries from 
     * standard input and prints out the top k matching terms.
     * @param args takes the name of an input file and an integer k as command-line arguments
     */
    public static void main(String[] args) {
        // initialize autocomplete data structure
        In in = new In(args[0]);
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();
            terms[i] = in.readLine();       // read the next term
        }

        Autocomplete autocomplete = new Autocomplete(terms, weights);
        // process queries from standard input
        int k = Integer.parseInt(args[1]);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            for (String term : autocomplete.topMatches(prefix, k)) {
                StdOut.printf("%14.1f  %s\n", autocomplete.weightOf(term), term);
            }
        }

    }
}
