import org.junit.Test;
import static org.junit.Assert.*;
public class TrieTest {


    /** Taken from project spec 
    * depends on working insert
    * so should test that as well
    */
    @Test
    public void findTest() {
    Trie t = new Trie();
    t.insert("hello");
    t.insert("hey");    
    t.insert("goodbye");
    assertTrue(t.find("hell", false));
    assertTrue(t.find("hello", false));
    assertTrue(t.find("hello", true));
    assertTrue(t.find("good", false));
    assertTrue(!t.find("good", true));
    assertTrue(!t.find("bye", false));
    assertTrue(!t.find("heyy", false));
    assertTrue(!t.find("heyy", true));
    assertTrue(!t.find("hell", true));
    assertTrue(t.find("hell", false));
    }


    @Test
    public void findTestIntense() {
    Trie t = new Trie();
    t.insert("hello");
    t.insert("goodday");    
    t.insert("goodbye");
    t.insert("death");
    t.insert("d");
    t.insert("alph");
    t.insert("alp");


    assertTrue(t.find("hello", true));
    assertTrue(t.find("goodday", true));
    assertTrue(t.find("goodbye", true));
    assertTrue(t.find("death", true));
    assertTrue(t.find("d", true));
    assertTrue(t.find("alph", true));
    assertTrue(t.find("alp", true));

    }




    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TrieTest.class);
    }
}