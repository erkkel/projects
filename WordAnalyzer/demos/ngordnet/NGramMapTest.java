package ngordnet;
import static org.junit.Assert.*;
import ngordnet.NGramMap;
import ngordnet.TimeSeries;
import ngordnet.YearlyRecord;

import java.util.Collection;
import java.util.Iterator;




import org.junit.Before;
import org.junit.Test;

public class NGramMapTest {

    @Before
    public void setUp() {
        

    }
    
    @Test
    public void countHistoryTest() {
        NGramMap ngm = new NGramMap("./ngrams/words_that_start_with_q.csv", 
                "./ngrams/total_counts.csv");
        
        TimeSeries<Integer> history = ngm.countHistory("quantity");
        
        assertEquals(1, (int) history.get(1505));
        assertEquals(0, (int) history.get(1507));
        assertEquals(0, (int) history.get(1520));
        assertEquals(719377, (int) history.get(2008));
    }
    
    public static void main(String... args) {
        jh61b.junit.textui.runClasses(NGramMapTest.class);
    }       
}