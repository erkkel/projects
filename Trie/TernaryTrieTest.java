import org.junit.Test;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Iterator;
import static org.junit.Assert.*;
public class TernaryTrieTest {
    
    @Test
    public void trieInsertTest() {
        TernaryTrie t = new TernaryTrie();
        t.insert("hell", 1.0);
        t.insert("hello", 5.0);

        TernaryTrie.Node root = t.getRoot();
        assertEquals("h", String.valueOf(root.getVal()));
        assertEquals(5.0, root.getMaxChildWeight(), 0.01);
        assertEquals(0.0, root.getWeight(), 0.01);


        TernaryTrie.Node mid = t.getRoot().getMiddle();
        assertEquals("e", String.valueOf(mid.getVal()));
        assertEquals(5.0, mid.getMaxChildWeight(), 0.01);
        assertEquals(0.0, mid.getWeight(), 0.01);

        TernaryTrie.Node left = t.getRoot().getLeft();
        TernaryTrie.Node right = t.getRoot().getRight();
        assertEquals(null, left);
        assertEquals(null, right);

        TernaryTrie.Node midmidmid = mid.getMiddle().getMiddle();
        assertEquals("l", String.valueOf(midmidmid.getVal()));
        assertEquals(5.0, midmidmid.getMaxChildWeight(), 0.01);
        assertEquals(1.0, midmidmid.getWeight(), 0.01);

        TernaryTrie.Node midmidmidmid = midmidmid.getMiddle();
        assertEquals("o", String.valueOf(midmidmidmid.getVal()));
        assertEquals(5.0, midmidmidmid.getMaxChildWeight(), 0.01);
        assertEquals(5.0, midmidmidmid.getWeight(), 0.01);
    }

    @Test
    public void trieInsertTestIntense() {
        TernaryTrie t = new TernaryTrie();
        t.insert("hell", 1.0);
        t.insert("hello", 5.0);
        t.insert("lala", 7.0);

        TernaryTrie.Node root = t.getRoot();
        assertEquals("h", String.valueOf(root.getVal()));
        assertEquals(7.0, root.getMaxChildWeight(), 0.01);
        assertEquals(0.0, root.getWeight(), 0.01);

        TernaryTrie.Node right = root.getRight();
        assertEquals("l", String.valueOf(right.getVal()));
        assertEquals(7.0, right.getMaxChildWeight(), 0.01);
        assertEquals(0.0, right.getWeight(), 0.01);

        TernaryTrie.Node rightmid = right.getMiddle();
        assertEquals("a", String.valueOf(rightmid.getVal()));
        assertEquals(7.0, rightmid.getMaxChildWeight(), 0.01);
        assertEquals(0.0, rightmid.getWeight(), 0.01);


        TernaryTrie.Node rightmidmidmid = right.getMiddle().getMiddle().getMiddle();
        assertEquals("a", String.valueOf(rightmidmidmid.getVal()));
        assertEquals(7.0, rightmidmidmid.getMaxChildWeight(), 0.01);
        assertEquals(7.0, rightmidmidmid.getWeight(), 0.01);
    }

    @Test
    public void trieInsertTestIntenser() {
        TernaryTrie t = new TernaryTrie();
        t.insert("hell", 1.0);
        t.insert("hello", 5.0);
        t.insert("lala", 7.0);
        t.insert("mama", 9.0);

        TernaryTrie.Node root = t.getRoot();
        
        TernaryTrie.Node rr = root.getRight().getRight();
        assertEquals("m", String.valueOf(rr.getVal()));
        assertEquals(9.0, root.getMaxChildWeight(), 0.01);
        assertEquals(0.0, root.getWeight(), 0.01);

        TernaryTrie.Node rrm = root.getRight().getRight().getMiddle();
        assertEquals("a", String.valueOf(rrm.getVal()));
        assertEquals(9.0, rrm.getMaxChildWeight(), 0.01);
        assertEquals(0.0, rrm.getWeight(), 0.01);

        TernaryTrie.Node rrmmm = rr.getMiddle().getMiddle().getMiddle();
        assertEquals("a", String.valueOf(rrmmm.getVal()));
        assertEquals(9.0, rrmmm.getMaxChildWeight(), 0.01);
        assertEquals(9.0, rrmmm.getWeight(), 0.01);
    }

    @Test
    public void trieInsertTestIntenserr() {
        TernaryTrie t = new TernaryTrie();
        t.insert("hell", 1.0);
        t.insert("hello", 5.0);
        t.insert("help", 15.0);

        TernaryTrie.Node mmmr = t.getRoot().getMiddle().getMiddle().getMiddle().getRight();
        assertEquals("p", String.valueOf(mmmr.getVal()));
        assertEquals(15.0, mmmr.getMaxChildWeight(), 0.01);
        assertEquals(15.0, mmmr.getWeight(), 0.01);
    }

    @Test
    public void trieInsertTestIntenserrr() {
        TernaryTrie t = new TernaryTrie();
        t.insert("ab", 1.0);
        t.insert("ac", 5.0);
        t.insert("ad", 15.0);
        t.insert("abe", 15.0);
        t.insert("ae", 15.0);
        t.insert("ace", 15.0);


        TernaryTrie.Node r = t.getRoot();
        assertEquals("a", String.valueOf(r.getVal()));
        assertEquals("b", String.valueOf(r.getMiddle().getVal()));
        assertEquals("e", String.valueOf(r.getMiddle().getMiddle().getVal()));
        assertEquals("c", String.valueOf(r.getMiddle().getRight().getVal()));
        assertEquals("e", String.valueOf(r.getMiddle().getRight().getMiddle().getVal()));
        assertEquals("d", String.valueOf(r.getMiddle().getRight().getRight().getVal()));
        assertEquals("e", String.valueOf(r.getMiddle().getRight().getRight().getRight().getVal()));

    }

    @Test
    public void getWeightTest() {
        TernaryTrie t = new TernaryTrie();
        t.insert("hell", 1.0);
        t.insert("hello", 5.0);
        t.insert("help", 15.0);


        assertEquals(1.0, t.getWeight("hell", t.getRoot()), 0.01);
        assertEquals(5.0, t.getWeight("hello", t.getRoot()), 0.01);
        assertEquals(15.0, t.getWeight("help", t.getRoot()), 0.01);

        assertEquals(0.0, t.getWeight("hel", t.getRoot()), 0.01);
        assertEquals(0.0, t.getWeight("asd123", t.getRoot()), 0.01);

    }

    @Test
    public void getNodeTest() {
        TernaryTrie t = new TernaryTrie();
        t.insert("hello", 1.0);
        t.insert("hen", 5.0);
        t.insert("help", 15.0);

        TernaryTrie.Node n = t.getNode(t.getRoot(), "hel");
        assertEquals("l", String.valueOf(n.getVal()));
        assertEquals("n", String.valueOf(n.getRight().getVal()));
        assertEquals("l", String.valueOf(n.getMiddle().getVal()));
        assertEquals("p", String.valueOf(n.getMiddle().getRight().getVal()));

    }

    @Test
    public void getMaxWordsTest() {
        TernaryTrie t = new TernaryTrie();
        t.insert("hello", 1.0);
        t.insert("hen", 5.0);
        t.insert("help", 15.0);

        TernaryTrie.Node n = t.getNode(t.getRoot(), "hel");
        Iterator<String> iter = t.getMaxWords(n, 3, "hel").iterator();

        assertEquals("help", iter.next());
        assertEquals("hello", iter.next());

    }

    @Test
    public void getMaxWordsTestIntenser() {
        TernaryTrie t = new TernaryTrie();
        t.insert("the", 56271872.00);
        t.insert("of", 33950064.00);
        t.insert("and", 29944184.00);
        t.insert("to", 25956096.00);
        t.insert("in", 17420636.00);
        t.insert("i", 11764797.00);
        t.insert("that", 11073318.00);
        t.insert("was", 10078245.00);

        TernaryTrie.Node n = t.getNode(t.getRoot(), "t");
        Iterator<String> iter = t.getMaxWords(n, 1, "t").iterator();
        assertEquals("the", iter.next());

        Iterator<String> iter2 = t.getMaxWords(n, 2, "t").iterator();
        assertEquals("the", iter2.next());
        assertEquals("to", iter2.next());

        Iterator<String> iter3 = t.getMaxWords(n, 1, "t").iterator();
        assertEquals("the", iter3.next());
        assertEquals(false, iter3.hasNext());
    }

    @Test
    public void getMaxWordsTestIntenserrr() {
        TernaryTrie t = new TernaryTrie();

        t.insert("the", 56271872.00);
        t.insert("of", 33950064.00);
        t.insert("and", 29944184.00);
        t.insert("to", 25956096.00);
        t.insert("in", 17420636.00);
        t.insert("i", 11764797.00);
        t.insert("that", 11073318.00);
        t.insert("was", 10078245.00);


        TernaryTrie.Node n = t.getNode(t.getRoot(), "o");
        Iterator<String> iter = t.getMaxWords(n, 4, "o").iterator();
        assertEquals("of", iter.next());
    }

    @Test
    public void getMaxWordsTestIntenserr() {
        TernaryTrie t = new TernaryTrie();

        t.insert("in", 17420636.00);
        t.insert("i", 11764797.00);


        TernaryTrie.Node n = t.getNode(t.getRoot(), "i");
        Iterator<String> iter = t.getMaxWords(n, 4, "i").iterator();
        assertEquals("in", iter.next());
        assertEquals("i", iter.next());
    }

    @Test
    public void getMaxWordsTestIntenserrrr() {
        TernaryTrie t = new TernaryTrie();

        t.insert("Shanghai, China", 14608512);
        t.insert("Buenos Aires, Argentina", 13076300);
        t.insert("Mumbai, India", 12691836);
        t.insert("Mexico City, Distrito Federal, Mexico", 12294193);
        t.insert("Karachi, Pakistan", 11624219);
        t.insert("İstanbul, Turkey", 11174257);
        t.insert("Delhi, India", 10927986);
        t.insert("Manila, Philippines", 10444527);
        t.insert("Moscow, Russia", 10381222);
        t.insert("Maracay, Venezuela", 1754256);
        t.insert("Medan, Indonesia", 1750971);

        TernaryTrie.Node n = t.getNode(t.getRoot(), "M");
        Iterator<String> iter = t.getMaxWords(n, 7, "M").iterator();
        assertEquals("Mumbai, India", iter.next());
        assertEquals("Mexico City, Distrito Federal, Mexico", iter.next());
        assertEquals("Manila, Philippines", iter.next());
        assertEquals("Moscow, Russia", iter.next());
        assertEquals("Maracay, Venezuela", iter.next());
        assertEquals("Medan, Indonesia", iter.next());
    }

    @Test
    public void getMaxWordsTestIntenserrrrr() {
        TernaryTrie t = new TernaryTrie();

        t.insert("Shanghai, China", 14608512);
        t.insert("Buenos Aires, Argentina", 13076300);
        t.insert("Mumbai, India", 12691836);
        t.insert("Mexico City, Distrito Federal, Mexico", 12294193);
        t.insert("Karachi, Pakistan", 11624219);
        t.insert("İstanbul, Turkey", 11174257);
        t.insert("Delhi, India", 10927986);
        t.insert("Manila, Philippines", 10444527);
        t.insert("Moscow, Russia", 10381222);
        t.insert("Maracay, Venezuela", 1754256);
        t.insert("Medan, Indonesia", 1750971);

        TernaryTrie.Node n = t.getNode(t.getRoot(), "");
        Iterator<String> iter = t.getMaxWords(n, 7, "").iterator();
        assertEquals("Mumbai, India", iter.next());
        assertEquals("Mexico City, Distrito Federal, Mexico", iter.next());
        assertEquals("Manila, Philippines", iter.next());
        assertEquals("Moscow, Russia", iter.next());
        assertEquals("Maracay, Venezuela", iter.next());
        assertEquals("Medan, Indonesia", iter.next());
    }





    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TernaryTrieTest.class);
    }
}