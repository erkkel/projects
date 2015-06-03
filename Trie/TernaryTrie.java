import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.ArrayList;

/**
* Implementation of trie as a TST
* @author Eric Wang 
*/ 
public class TernaryTrie {

    private Node root;

    /**
    * Gets the root of the TernaryTrie
    * @return the root Node
    */
    public Node getRoot() {
        return root;
    }
    
    /**
    * Finds the string s in isFullWord form in the TernaryTrie
    * @param s the String we're looking for
    * @param isFullWord whether or not we want to look for the full word in the trie
    * @return whether or not s is in the trie
    */
    public boolean find(String s, boolean isFullWord) {
        Node n = this.getNode(this.root, s);
        if (n == null) {
            return false;
        } 
        if (!isFullWord) {
            return true;
        }
        if (isFullWord && n.weight > 0) {
            return true;
        }
        return false;
    }

    /**
    * Handles the case of inserting a word into an empty root Node
    * @param s the string to be inserted
    * @param weight the weight of the string
    */
    public void initalize(String s, double weight) {
        if (s.length() == 1) {
            root = new Node(s.charAt(0), weight, weight);
            return;
        }
        root = new Node(s.charAt(0), weight);
        Node pointer = root;
        for (int i = 1; i < s.length(); i++) {
            if (i < s.length() - 1) {
                pointer.middle = new Node(s.charAt(i), weight);
                pointer = pointer.middle;
            } else {
                pointer.middle = new Node(s.charAt(i), weight, weight);
            }
        }  
    }

    /**
    * Helps with insertion into the left branch of the TernaryTrie
    * @param p the Node whose left child we will insert into
    * @param s the string to be inserted
    * @param weight the weight of the string we want to insert
    */
    public void leftHelper(Node p, String s, double weight) {
        if (s.equals("")) {
            double newChildWeight = 0;
            if (weight > p.maxChildWeight) {
                newChildWeight = weight;
            } else {
                newChildWeight = p.maxChildWeight;
            }

            p.weight = weight;
            p.maxChildWeight = newChildWeight;  
            return;
        }

        if (p.left == null) {
            p.left = new Node(s.charAt(0), weight);
        } else {
            if (weight > p.left.maxChildWeight) {
                p.left.maxChildWeight = weight;
            }
        }
        
        String newChild = whichChild(p.left, s.charAt(0));
        if (newChild.equals("middle")) {
            insert(p.left, newChild, s.substring(1, s.length()), weight);
            return;
        } 
        insert(p.left, newChild, s, weight);
    }
    /**
    * Helps with insertion into the middle branch of the TernaryTrie
    * @param p the Node whose middle child we will insert into
    * @param s the string to be inserted
    * @param weight the weight of the string we want to insert
    */
    public void middleHelper(Node p, String s, double weight) {
        if (s.equals("")) {
            double newChildWeight = 0;
            if (weight > p.maxChildWeight) {
                newChildWeight = weight;
            } else {
                newChildWeight = p.maxChildWeight;
            }

            p.weight = weight;
            p.maxChildWeight = newChildWeight;
            return;
        }

        if (p.middle == null) {
            p.middle = new Node(s.charAt(0), weight);
        } else {
            if (weight > p.middle.maxChildWeight) {
                p.middle.maxChildWeight = weight;
            }
        }
        
        String newChild = whichChild(p.middle, s.charAt(0));
        if (newChild.equals("middle")) {
            insert(p.middle, newChild, s.substring(1, s.length()), weight);
            return;
        } 
        insert(p.middle, newChild, s, weight);
    }
    /**
    * Helps with insertion into the right branch of the TernaryTrie
    * @param p the Node whose right child we will insert into
    * @param s the string to be inserted
    * @param weight the weight of the string we want to insert
    */
    public void rightHelper(Node p, String s, double weight) {
        if (s.equals("")) {
            double newChildWeight = 0;
            if (weight > p.maxChildWeight) {
                newChildWeight = weight;
            } else {
                newChildWeight = p.maxChildWeight;
            }

            p.weight = weight;
            p.maxChildWeight = newChildWeight;
            return;
        }

        if (p.right == null) {
            p.right = new Node(s.charAt(0), weight);
        } else {
            if (weight > p.right.maxChildWeight) {
                p.right.maxChildWeight = weight;
            }
        }
        
        String newChild = whichChild(p.right, s.charAt(0));
        if (newChild.equals("middle")) {
            insert(p.right, newChild, s.substring(1, s.length()), weight);
            return;
        } 
        insert(p.right, newChild, s, weight);
    }


    /**
    * Inserts the word s into p's child
    * @param p the node we want to insert into
    * @param child which child of p we want to insert into
    * @param s the string we want to insert
    * @param weight the weight of the string
    */
    public void insert(Node p, String child, String s, double weight) {
        
        if (child.equals("left")) {
            this.leftHelper(p, s, weight);
        } else if (child.equals("middle")) {
            this.middleHelper(p, s, weight);
        } else {
            this.rightHelper(p, s, weight);
        }
    }

    /** 
    *Insert a word into the trie.
    * @param s The word to be inserted.
    * @param weight The weight of the word.
    */
    public void insert(String s, double weight) {
        if (s == null) {
            return;
        }
        if (root == null) {
            initalize(s, weight);
            return;
        }
        String child = whichChild(root, s.charAt(0));
        if (child.equals("middle")) {
            s = s.substring(1, s.length());
        }
        if (weight > root.maxChildWeight) {
            root.maxChildWeight = weight;
        }

        insert(root, child, s, weight);
    }

    

    

    /**
    * Returns which child of Node n we should insert char c into
    * @param n the node whose child we want to insert into
    * @param c the character we want to insert
    * @return  "left" if c belongs in n's left child
    * returns "middle" if c belongs in n's middle child
    * returns "right" if c belongs in n's right child
    */
    public String whichChild(Node n, char c) {

        int comparer = Character.toString(c).compareTo(Character.toString(n.val));
        if (comparer < 0) {
            return "left";
        } else if (comparer > 0) {
            return "right";
        } else {
            return "middle";
        }
    }



    /**
    * Find the weight of a given term. If it is not in the trie,
    * return 0.0.
    * @param term the term whose weight we're looking for
    * @param n the Node that we start looking from
    * @return the weight of the term
    */
    public double getWeight(String term, Node n) {
        if (n == null) {
            return 0.0;
        }
        if (term.equals("")) {
            return 0.0;
        }
        if (term.length() == 1 && String.valueOf(n.val).equals(term)) {
            return n.weight;
        }
        String which = this.whichChild(n, term.charAt(0));
        if (which.equals("left")) {
            return this.getWeight(term, n.left);
        } else if (which.equals("right")) {
            return this.getWeight(term, n.right);
        } else {
            return this.getWeight(term.substring(1, term.length()), n.middle);
        }
    }

    /**
    * Find the maxChildWeight of a given term. If it is not in the trie,
    * return 0.0.
    * @param term the term we're looking for
    * @param n the node that we begin the search from
    * @return the maxChildWeight of the term starting from node n
    */
    public double getMaxWeight(String term, Node n) {
        if (n == null) {
            return 0.0;
        }
        if (term.equals("")) {
            return 0.0;
        }

        if (term.length() == 1 && String.valueOf(n.val).equals(term)) {
            return n.maxChildWeight;
        }

        String which = this.whichChild(n, term.charAt(0));
        if (which.equals("left")) {
            return this.getWeight(term, n.left);
        } else if (which.equals("right")) {
            return this.getWeight(term, n.right);
        } else {
            return this.getWeight(term.substring(1, term.length()), n.middle);
        }
    }

    /** 
    * Gets the Node n with the given prefix. 
    * @param n The node that we'll be looking in
    * @param prefix The prefix that our node must have
    * @return the Node in the TernaryTrie that has the given prefix
    */
    public Node getNode(Node n, String prefix) {
        if (n == null) {
            return null;
        }
        if (String.valueOf(n.val).equals(prefix)) {
            return n;
        }
        String child = this.whichChild(n, prefix.charAt(0));
        if (child.equals("left")) {
            return this.getNode(n.left, prefix);
        } else if (child.equals("right")) {
            return this.getNode(n.right, prefix);
        } else {
            return this.getNode(n.middle, prefix.substring(1, prefix.length()));
        }
    }
    /**
    * Gets the k largest words starting from Node n that have the greatest weight
    * @param n The node that we start the search from\
    * @param k the max number of elements in our iterable
    * @param prefix The string that keeps track of our word as we search through the trie
    * @return an Iterable of the largest words in the TernaryTrie starting from node n 
    */
    public Iterable<String> getMaxWords(Node n, int k, String prefix) {
        PriorityQueue<Node> toCheck = new PriorityQueue<Node>(k, new NodeComparator());
        PriorityQueue<Node> maxWeights = new PriorityQueue<Node>(k, new NodeComparatorReverse());
        HashMap<Node, String> map = new HashMap<Node, String>();
        if (n.weight > 0) {
            maxWeights.add(n);
        }
        if (n.middle != null) {
            toCheck.add(n.middle);
        } 
        map.put(n, "");
        map.put(n.middle, String.valueOf(n.val));
        while (toCheck.peek() != null && ((maxWeights.size() < k) 
            || toCheck.peek().maxChildWeight > this.getSmallest(maxWeights, k).weight)) {
            Node c = toCheck.poll();
            if (c.weight > 0) {
                maxWeights.add(c);
            }
            if (c.left != null) {
                toCheck.add(c.left);
                map.put(c.left, map.get(c));
            }
            if (c.middle != null) {
                toCheck.add(c.middle);
                map.put(c.middle, map.get(c) + c.val);
            }
            if (c.right != null) {
                toCheck.add(c.right);
                map.put(c.right, map.get(c));
            }
        }
        ArrayList<String> maxWords = new ArrayList<String>();
        int counter = 0;
        while (maxWeights.peek() != null && counter < k) {
            Node node = maxWeights.poll();
            maxWords.add(prefix.substring(0, prefix.length() - 1) + map.get(node) + node.val);
            counter++;
        }
        return maxWords;
    }

    /**
    * Handles empty prefix case
    * @param n The node that we start the search from
    * @param k the max number of elements in our iterable
    * @param prefix The string that keeps track of our word as we search through the trie
    * @return an Iterable of the largest words in the TernaryTrie starting from node n
    */
    public Iterable<String> getMaxWordsEmpty(Node n, int k, String prefix) {
        PriorityQueue<Node> toCheck = new PriorityQueue<Node>(k, new NodeComparator());
        PriorityQueue<Node> maxWeights = new PriorityQueue<Node>(k, new NodeComparatorReverse());
        HashMap<Node, String> map = new HashMap<Node, String>();
        if (n.weight > 0) {
            maxWeights.add(n);
        }
        if (n.middle != null) {
            toCheck.add(n);
        } 
        map.put(n, "");
        map.put(n.middle, String.valueOf(n.val));
        while (toCheck.peek() != null && ((maxWeights.size() < k) 
            || toCheck.peek().maxChildWeight > this.getSmallest(maxWeights, k).weight)) {
            Node c = toCheck.poll();
            if (c.weight > 0) {
                maxWeights.add(c);
            }
            if (c.left != null) {
                toCheck.add(c.left);
                map.put(c.left, map.get(c));
            }
            if (c.middle != null) {
                toCheck.add(c.middle);
                map.put(c.middle, map.get(c) + c.val);
            }
            if (c.right != null) {
                toCheck.add(c.right);
                map.put(c.right, map.get(c));
            }
        }
        ArrayList<String> maxWords = new ArrayList<String>();
        int counter = 0;
        while (maxWeights.peek() != null && counter < k) {
            Node node = maxWeights.poll();
            maxWords.add(prefix.substring(0, prefix.length() - 1) + map.get(node) + node.val);
            counter++;
        }
        return maxWords;
    }
    /** 
    * Gets the smallest element in the priorityqueue n
    * @param n the PriorityQueue we're looking through
    * @param k the max number of elements we want to go through
    * @return the smallest element or the kth smallest element in n, whichever comes first 
    */
    public Node getSmallest(PriorityQueue<Node> n, int k) {
        if (n == null) {
            return null;
        }

        Node smallest = n.peek();
        ArrayList<Node> lst = new ArrayList<Node>();
        int counter = 0;
        while (n.peek() != null && counter < k) {
            smallest = n.poll();
            lst.add(smallest);
            counter++;
        }

        for (Node nod : lst) {
            n.add(nod);
        }

        return smallest;
    }
    /** 
    * Represents the breaking-up of words into characters within our try
    */
    public class Node {
        private char val;
        private Node left;
        private Node middle;
        private Node right;
        private double maxChildWeight;
        private double weight;

        /**
        * Constructor for node. Sets a char val
        * @param val the value of the character at this node
        */
        public Node(char val) {
            this.val = val;
        }
        /**
        * Constructor for node. Sets a char val and whether the node is a word
        * @param val the value of the character at this node
        * @param maxChildWeight the max weight of this node's children
        */
        public Node(char val, double maxChildWeight) {
            this.val = val;
            this.maxChildWeight = maxChildWeight;
        }
        /**
        * Constructor for node. Sets a char val and whether the node is a word
        * @param val the value of the character at this node
        * @param maxChildWeight the max weight of this node's children
        * @param weight the weight at the node
        */
        public Node(char val, double maxChildWeight, double weight) {
            this.val = val;
            this.maxChildWeight = maxChildWeight;
            this.weight = weight;
        }        
        /** 
        * Getter for the associated instance variable
        * @return the char val
        */
        public char getVal() {
            return val;
        }
        /** 
        * Getter for the associated instance variable
        * @return the left node
        */
        public Node getLeft() {
            return this.left;
        }
        /** 
        * Getter for the associated instance variable
        * @return the middle node
        */
        public Node getMiddle() {
            return this.middle;
        }
        /** 
        * Getter for the associated instance variable
        * @return the right node
        */
        public Node getRight() {
            return this.right;
        }
        /** 
        * Getter for the associated instance variable
        * @return the maxChildWeight
        */
        public double getMaxChildWeight() {
            return this.maxChildWeight;
        }
        /** 
        * Getter for the associated instance variable
        * @return the weight
        */
        public double getWeight() {
            return this.weight;
        }
    }

}

