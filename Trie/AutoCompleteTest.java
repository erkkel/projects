import org.junit.Test;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Iterator;
import static org.junit.Assert.*;
public class AutoCompleteTest {

    @Test
    public void topMatchesTest() {
        String[] names = new String[]{"the", "of", "and", "to", "in", "i", "that", "was"};
        double[] weights = new double[]{56271872.00, 33950064.00, 29944184.00, 25956096.00, 17420636.00, 11764797.00, 11073318.00, 10078245.00};

        Autocomplete a = new Autocomplete(names, weights);
        Iterator<String> iter = a.topMatches("t", 2).iterator();
        assertEquals("the", iter.next());
        assertEquals("to", iter.next());
 
    }


    @Test
    public void topMatchesTestIntense() {
        String[] names = new String[]{"the", "of", "and", "to", "in", "i", "that", "was"};
        double[] weights = new double[]{56271872.00, 33950064.00, 29944184.00, 25956096.00, 17420636.00, 11764797.00, 11073318.00, 10078245.00};

        Autocomplete a = new Autocomplete(names, weights);
        Iterator<String> iter = a.topMatches("o", 4).iterator();
        assertEquals("of", iter.next());
    }

    @Test
    public void topMatchesTestIntenser() {
        String[] names = new String[]{"the", "of", "and", "to", "in", "i", "that", "was"};
        double[] weights = new double[]{56271872.00, 33950064.00, 29944184.00, 25956096.00, 17420636.00, 11764797.00, 11073318.00, 10078245.00};

        Autocomplete a = new Autocomplete(names, weights);
        Iterator<String> iter = a.topMatches("i", 4).iterator();
        assertEquals("in", iter.next());
        assertEquals("i", iter.next());
        
    }

    @Test
    public void topMatchesTestIntenserr() {
        String[] names = new String[]{"Mumbai, India", "Mexico City, Distrito Federal, Mexico", "Manila, Philippines", "Moscow, Russia", "Maracay, Venezuela", "Medan, Indonesia"};
        double[] weights = new double[]{12691836, 12294193, 10444527, 10381222, 1754256, 1750971};

        Autocomplete a = new Autocomplete(names, weights);
        Iterator<String> iter = a.topMatches("M", 7).iterator();
        assertEquals("Mumbai, India", iter.next());
        assertEquals("Mexico City, Distrito Federal, Mexico", iter.next());
        assertEquals("Manila, Philippines", iter.next());
        assertEquals("Moscow, Russia", iter.next());
        assertEquals("Maracay, Venezuela", iter.next());
        assertEquals("Medan, Indonesia", iter.next());
        
    }

    @Test
    public void tinyTest() {
        String[] names = new String[]{"smog", "buck", "sad", "spite", "spit", "spy"};
        double[] weights = new double[]{5, 10, 12, 20, 15, 7};

        Autocomplete a = new Autocomplete(names, weights);
        String word = a.topMatch("spit");
        assertEquals("spite", word);
    }




    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(AutoCompleteTest.class);
    }

}