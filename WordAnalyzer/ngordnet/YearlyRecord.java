package ngordnet;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class YearlyRecord {
    /** Creates a new empty YearlyRecord. */
    private HashMap<String, Integer> wordToNum;
    private TreeSet<Number> nums;
    private boolean listSorted;
    private ArrayList<String> words;
    
    public YearlyRecord() {
        wordToNum = new HashMap<String, Integer>();
        nums = new TreeSet<Number>();
        listSorted = false;
        words = new ArrayList<String>();
    }
    
    /** Creates a YearlyRecord using the given data. */
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {
        wordToNum = new HashMap<String, Integer>();
        wordToNum.putAll(otherCountMap);
        nums = new TreeSet<Number>();
        nums.addAll(otherCountMap.values());
        words = new ArrayList<String>();

        
        listSorted = false;
        
    }
    
    /** Returns the number of times WORD appeared in this year. */
    public int count(String word) {
        try {
            return wordToNum.get(word).intValue();
        } catch (NullPointerException e) {
            return 0;
        }
        
        
    }
    
    /** Records that WORD occurred COUNT times in this year. */
    public void put(String word, int count) {
        listSorted = false;
        wordToNum.put(word, count);
        nums.add(count);        
    }   
    /** Returns the number of words recorded this year. */
    public int size() {
        return wordToNum.size();
    }
    
    private class CountComparer implements Comparator<String> {
        public int compare(String w1, String w2) {
            return wordToNum.get(w1).intValue() - wordToNum.get(w2).intValue();
        }
               
    }
    
    private class CountComparerReverse implements Comparator<String> {
        public int compare(String w1, String w2) {
            int num = wordToNum.get(w2).intValue() - wordToNum.get(w1).intValue();
            if (num == 0) {
                return w1.compareTo(w2);
            }
           
            return num;
            
        }
               
    }
    
    /** Returns all words in ascending order of count. */
    public Collection<String> words() {
        ArrayList<String> wordsList = new ArrayList<String>();
        wordsList.addAll(wordToNum.keySet());
        Collections.sort(wordsList, new CountComparer());
        return wordsList;        
    }
    

    /** Returns all counts in ascending order of count. */
    public Collection<Number> counts() {
        return nums;
    }
    
    /**
     * Returns rank of WORD. Most common word is rank 1. If two words have the
     * same rank, break ties arbitrarily. No two words should have the same
     * rank.
     */
    public int rank(String word) {
        if (!listSorted) {
            words.clear();
            words.addAll(wordToNum.keySet());
            Collections.sort(words, new CountComparerReverse());
            listSorted = true;
        }
        int index = Collections.binarySearch(words, word, new CountComparerReverse());
        return index + 1;
    }
}
