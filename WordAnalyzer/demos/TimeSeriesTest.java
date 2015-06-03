import static org.junit.Assert.*;
import ngordnet.TimeSeries;
import java.util.Collection;


import org.junit.Before;
import org.junit.Test;

public class TimeSeriesTest {

    @Before
    public void setUp() {
    	

    }
    
    /* checks edge case of adding TimeSeries with no inputs */
    
    @Test 
    public void constructorTest() {
    	TimeSeries<Integer> ts = new TimeSeries<Integer>();   	
    	ts.put(1992, 3);
        ts.put(1993, 9);
        ts.put(1994, 15);
        ts.put(1995, 16);
        ts.put(1996, -15);
        
        TimeSeries<Integer> newSeries = new TimeSeries<Integer>(ts, 1993, 1995);
        assertEquals(9, newSeries.get(1993), 0.01);
        assertEquals(15, newSeries.get(1994), 0.01);
        assertEquals(16, newSeries.get(1995), 0.01);
        
    	TimeSeries<Integer> newSeries2 = new TimeSeries<Integer>(ts);
    	assertEquals(3, newSeries2.get(1992), 0.01);
    	assertEquals(9, newSeries2.get(1993), 0.01);
        assertEquals(15, newSeries2.get(1994), 0.01);
        assertEquals(16, newSeries2.get(1995), 0.01);
        assertEquals(-15, newSeries2.get(1996), 0.01);
        

    }
    
    
    @Test 
    public void plusTest1() {
    	TimeSeries<Integer> ts = new TimeSeries<Integer>();
    	TimeSeries<Double> ts2 = new TimeSeries<Double>();
    	
    	ts.put(1992, 3);
        ts.put(1993, 9);
        ts.put(1994, 15);
        ts.put(1995, 16);
        ts.put(1996, -15);
        
        TimeSeries<Double> newSeries = ts.plus(ts2);
        
        assertEquals(3.0, newSeries.get(1992), 0.01);
        assertEquals(15.0, newSeries.get(1994), 0.01);
        assertEquals(-15.0, newSeries.get(1996), 0.01);

    }
    
    @Test 
    public void plusTest2() {
    	TimeSeries<Integer> ts = new TimeSeries<Integer>();
    	TimeSeries<Double> ts2 = new TimeSeries<Double>();
    	
    	ts.put(1992, 3);
        ts.put(1993, 9);
        ts.put(1994, -3);
        
        ts2.put(1994, 15.0);
        ts2.put(1995, 16.0);
        ts2.put(1996, -15.0);
        
        TimeSeries<Double> newSeries = ts.plus(ts2);
        
        assertEquals(3.0, newSeries.get(1992), 0.01);
        assertEquals(12.0, newSeries.get(1994), 0.01);
        assertEquals(-15.0, newSeries.get(1996), 0.01);

    }
    
    @Test 
    public void divideTest1() {
    	TimeSeries<Integer> ts = new TimeSeries<Integer>();
    	TimeSeries<Double> ts2 = new TimeSeries<Double>();
    	
    	ts.put(1994, 3);
        ts.put(1995, 4);
        ts.put(1996, 5);
        ts.put(1997, -12);
        
        ts2.put(1994, 15.0);
        ts2.put(1995, 16.0);
        ts2.put(1996, -15.0);

        
        TimeSeries<Double> newSeries = ts2.dividedBy(ts);
        
        assertEquals(5.0, newSeries.get(1994), 0.01);
        assertEquals(4.0, newSeries.get(1995), 0.01);
        assertEquals(-3.0, newSeries.get(1996), 0.01);

    }
    


    public static void main(String... args) {
        jh61b.junit.textui.runClasses(TimeSeriesTest.class);
    }       
}