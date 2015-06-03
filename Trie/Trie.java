/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author Eric Wang
 */
import java.util.HashMap;
import java.util.ArrayList;
import java.util.TreeMap;
/** 
* Trie. Supports linear time insertion and search for words 
* @author Eric Wang
*/

public class Trie {
    private HashMap<Character, Node> head;

    /**
    *Constructor for the trie. Implemented as a ternary search trie.
    */
    public Trie() {
        head = new HashMap<Character, Node>();
    }
    /** 
    * getter for the head instance variable of trie
    * @return the root hashmap of a trie
    */
    public HashMap<Character, Node> getHead() {
        return head;
    }

    /**
    *@param s The string you want to find
    *@param isFullWord whether or not you want to check if the full word is in the trie
    *@return whether or not the String s is in isFullWord form in the trie
    */
    public boolean find(String s, boolean isFullWord) {
        HashMap<Character, Node> pointer = head;
        char[] array = s.toCharArray();
        for (int i = 0; i < array.length; i++) {
            char c = array[i];
            if (!pointer.containsKey(c)) {
                return false;
            }
            if (i == array.length - 1) {
                Node n = pointer.get(c);
                return (!isFullWord || n.isWord());
            }
            pointer = pointer.get(c).getChildren();

        }
        return false;
    }


    /** 
    *Insert a word into the trie.
    * @param s The word to be inserted.
    */
    public void insert(String s) {
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException();
        }
        HashMap<Character, Node> pointer = head;
        char[] array = s.toCharArray();
        for (int i = 0; i < array.length; i++) {
            char c = array[i];
            if (!pointer.containsKey(c)) {
                if (i == array.length - 1) {
                    pointer.put(c, new Node(c, true));
                } else {
                    pointer.put(c, new Node(c));
                }
            } else {
                if (i == array.length - 1) {
                    pointer.get(c).exists();
                }
            }
            pointer = pointer.get(c).getChildren();
        }

    }

    /**
    * Returns the order that we should look through a map's keys in, based on on an alphabet
    * @param map the map whose keys we'll be ordering
    * @param alphabet the ordering on the keys of the map
    * @return a map with the values sorted in the order they should be looked through
    */
    public TreeMap<Integer, Character> getOrder(HashMap<Character, Node> map, String alphabet) {
        TreeMap<Integer, Character> order = new TreeMap<Integer, Character>();
        for (char c : map.keySet()) {
            int index = alphabet.indexOf(c);
            if (index != -1) {  
                order.put(index, c);
            }
        }
        return order;
    }
    /**
    * Does the meat of the alphabet sorting algorithm
    * @param n the node whose words we want to alphabetsort
    * @param alphabet the alphabet whose ordering we'll impose on the node's elements
    * @return An arraylist of the elements of Node n in sorted order
    */
    public ArrayList<String> aSort(Node n, String alphabet) {
        if (n == null) {
            return null;
        }
        HashMap<Character, Node> map = n.getChildren();
        if (map.size() == 0) {
            ArrayList<String> list = new ArrayList<String>();
            if (n.isWord) {
                list.add(String.valueOf(n.val));
            }
            return list;
        }
        TreeMap<Integer, Character> order = this.getOrder(map, alphabet);
        ArrayList<String> lst = new ArrayList<String>();
        if (n.isWord) {
            lst.add("");
        }
        for (Integer i : order.keySet()) {
            char c = order.get(i);
            lst.addAll(aSort(map.get(c), alphabet));
        }

        ArrayList<String> newLst = new ArrayList<String>();
        for (String s : lst) {
            newLst.add(n.val + s);
        }
        return newLst;
    }

    /** Node class. Supports the splitting of words into characters within the trie.*/
    public class Node {
        private char val;
        private HashMap<Character, Node> children;
        private boolean isWord = false;

        /**
        * Constructor for node. Sets a char val
        * @param val the value of the character at this node
        */
        public Node(char val) {
            this.val = val;
            children = new HashMap<Character, Node>();
        }
        /**
        * Constructor for node. Sets a char val and whether the node is a word
        * @param val the value of the character at this node
        * @param isWord whether this node marks the end of a word
        */
        public Node(char val, boolean isWord) {
            this.val = val;
            children = new HashMap<Character, Node>();
            this.isWord = isWord;

        }
        /**
        * Puts a character into the trie
        * @param c The character
        * @param nod The node the character should map to
        */
        public void put(Character c, Node nod) {
            children.put(c, nod);
        }
        /**
        * Checks if the Node contains a character c
        * @param c the character to check
        * @return whether or not the character is in the trie 
        */
        public boolean contains(Character c) {
            return children.containsKey(c);
        }
        /**
        * Returns the HashMap of all character-node mappings in the  node of trie
        * @return all the mappings
        */
        public HashMap<Character, Node> getChildren() {
            return children;
        }
        /**
        * Checks if the Node marks the end of a word
        * @return the boolean indicating if the Node is a word
        */
        public boolean isWord() {
            return isWord;
        }
        /**
        * Utility method for changing the isWord instance variable of a node. 
        * Makes it true.
        */
        public void exists() {
            isWord = true;
        }
    }
    


}
