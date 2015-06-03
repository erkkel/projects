import static org.junit.Assert.*;
import ngordnet.YearlyRecord;
import java.util.Collection;
import java.util.Iterator;


import org.junit.Before;
import org.junit.Test;

public class YearlyRecordTest {

    @Before
    public void setUp() {
        

    }
    
    @Test
    public void rankTest1() {
        YearlyRecord yr = new YearlyRecord();
        yr.put("a", 95);        
        yr.put("b", 340);
        yr.put("c", 181);
        
        assertEquals(3, yr.rank("a"));
        assertEquals(2, yr.rank("c"));
        assertEquals(1, yr.rank("b"));
        
        yr.put("d", 500);
        assertEquals(1, yr.rank("d"));
        assertEquals(4, yr.rank("a"));
        assertEquals(3, yr.rank("c"));
        assertEquals(2, yr.rank("b"));
  
    }
    
    @Test
    public void rankTest2() {
        YearlyRecord yr = new YearlyRecord();
        yr.put("a", 95);        
        yr.put("b", 340);
        yr.put("c", 181);
        yr.put("d", 181);
        

        assertEquals(4, yr.rank("a"));
        assertEquals(1, yr.rank("b"));
        assertEquals(2, yr.rank("c"));
        assertEquals(3, yr.rank("d"));
        
        
        yr.put("e", 181);
        yr.put("f", 181);
        yr.put("g", 181);
        yr.put("h", 181);
        
 
        assertEquals(8, yr.rank("a"));
        assertEquals(1, yr.rank("b"));
        assertEquals(2, yr.rank("c"));
        assertEquals(3, yr.rank("d"));
        assertEquals(4, yr.rank("e"));
        assertEquals(5, yr.rank("f"));
        assertEquals(6, yr.rank("g"));
        assertEquals(7, yr.rank("h"));
  
    }
    
    @Test
    public void rankTest3(){
        YearlyRecord yr = new YearlyRecord();
        yr.put("quayside", 95); 
        yr.put("surrogate", 340);
        yr.put("merchantman", 181); 
        assertEquals(1, yr.rank("surrogate"));
        assertEquals(3, yr.rank("quayside"));
        yr.put("potato", 5000);
         
        assertEquals(1, yr.rank("potato"));
        assertEquals(4, yr.rank("quayside"));
         
        yr.put("quayside", 10000);
        assertEquals(1, yr.rank("quayside")); 
        assertEquals(2, yr.rank("potato"));
    }
    @Test
    public void wordsTest() {
        YearlyRecord yr = new YearlyRecord();
        yr.put("a", 95);        
        yr.put("b", 340);
        yr.put("c", 181);
        yr.put("d", 100);
        yr.put("e", 100);
        
        Collection<String> words = yr.words();
        Iterator<String> iter = words.iterator();
        assertEquals("a", iter.next());
        assertEquals("d", iter.next());
        assertEquals("e", iter.next());
        assertEquals("c", iter.next());
        assertEquals("b", iter.next());
        
        
        
        
    }
    
    
    
    public static void main(String... args) {
        jh61b.junit.textui.runClasses(YearlyRecordTest.class);
    }       
}