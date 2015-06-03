package ngordnet;

import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collection;
import java.util.TreeSet;

public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {
    /** Constructs a new empty TimeSeries. */
    public TimeSeries() {
        
    }
    
    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR. inclusive
     * of both end points.
     */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {
        this.putAll(ts);
        Set<Integer> keys = ts.keySet();
        for (Integer i : keys) {
            if (i < startYear || i > endYear) {
                this.remove(i);
            }
        }
    }
    
    /** Creates a copy of TS. */
    public TimeSeries(TimeSeries<T> ts) {
        this.putAll(ts);
    }
    
    /**
     * Returns the quotient of this time series divided by the relevant value in
     * ts. If ts is missing a key in this time series, return an
     * IllegalArgumentException.
     */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts) {
        boolean canOperate = ts.keySet().containsAll(this.keySet());
        
        if (canOperate) {
            TimeSeries<Double> newSeries = new TimeSeries<Double>();
            
            for (Integer i : this.keySet()) {
                double numerator = this.get(i).doubleValue();
                if (numerator != 0) {
                    double value =  numerator / ts.get(i).doubleValue();
                            
                    newSeries.put(i, value);
                }
            }
            return newSeries;
        } else {
            throw new IllegalArgumentException();
        }
        
    }
    
    /**
     * Returns the sum of this time series with the given ts. The result is a a
     * Double time series (for simplicity).
     */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> newSeries = new TimeSeries<Double>();
        TreeSet<Integer> allKeys = new TreeSet<Integer>();
        /*
         * puts all the keys in keySet into allKeys--we do this so we don't have
         * a UnSupportedOperationException when using addAll
         */
        for (Integer key : this.keySet()) {
            allKeys.add(key);
        }
        
        Set<Integer> keys1 = this.keySet();
        Set<Integer> keys2 = ts.keySet();
        allKeys.addAll(keys2);
        
        double currValue1 = 0;
        double currValue2 = 0;
        for (Integer i : allKeys) {
            
            if (keys1.contains(i)) {
                currValue1 = this.get(i).doubleValue();
            } else {
                currValue1 = 0;
            }
            
            if (keys2.contains(i)) {
                currValue2 = ts.get(i).doubleValue();
            } else {
                currValue2 = 0;
            }
            
            newSeries.put(i, currValue1 + currValue2);
            
        }
        
        return newSeries;
    }
    
    /** Returns all years for this time series (in any order). */
    public Collection<Number> years() {
        Set<Integer> keys = this.keySet();
        ArrayList<Number> collection = new ArrayList<Number>();
        
        for (Integer i : keys) {
            collection.add(i);
        }
        
        return collection;
        
    }
    
    /** Returns all data for this time series (in any order). */
    public Collection<Number> data() {
        return (Collection<Number>) this.values();
        
      
    }
    
}
