import static org.junit.Assert.*;
import ngordnet.Plotter;
import ngordnet.TimeSeries;

import java.util.Collection;
import java.util.Iterator;



import org.junit.Before;
import org.junit.Test;

public class PlotterTest {

    @Before
    public void setUp() {
        

    }   
    
    @Test
    public void visualTest() {
        TimeSeries<Integer> ts = new TimeSeries<Integer>();     
        ts.put(1992, 3);
        ts.put(1993, 9);
        ts.put(1994, 15);
        ts.put(1995, 16);
        ts.put(1996, -15);
        
        Plotter lala = new Plotter();
        lala.plotTS(ts, "Eric's Graph", "x", "y", "legendddd");
    }
    
    public static void main(String... args) {
        jh61b.junit.textui.runClasses(PlotterTest.class);
    }       
}