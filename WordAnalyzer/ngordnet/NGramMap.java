package ngordnet;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

public class NGramMap {
    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    private TimeSeries<Long> totalWords;
    private TreeMap<Integer, YearlyRecord> yearToWord;
 
    //WORKS CITED: used http://www.cpe.ku.ac.th/~jim/java-io.html for scanning optimization
    public NGramMap(String wordsFilename, String countsFilename) {
        totalWords = new TimeSeries<Long>();
        yearToWord = new TreeMap<Integer, YearlyRecord>();
        
        yearToWordMaker(wordsFilename);
        totalWordsMaker(countsFilename);

    }
    
    private void totalWordsMaker(String countsFilename) {
        try { 
            FileReader wordFile = new FileReader(countsFilename);
            BufferedReader reader = new BufferedReader(wordFile);
    
            while (reader.ready()) {
                StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
                
                int year = Integer.parseInt(tokenizer.nextToken(","));
                Long totalWordNum  = Long.parseLong(tokenizer.nextToken(","));            
                totalWords.put(year, totalWordNum);
            }
            
            reader.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        
    }
    
    private void yearToWordMaker(String wordsFilename) {
        try {
        
            FileReader wordFile = new FileReader(wordsFilename);
            BufferedReader reader = new BufferedReader(wordFile);
    
            while (reader.ready()) {
                StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
                
                String word = tokenizer.nextToken();
                int year = Integer.parseInt(tokenizer.nextToken());              
                int timesAppeared = Integer.parseInt(tokenizer.nextToken());
                tokenizer.nextToken();
    
                /* takes care of yeraToWord */
                if (yearToWord.keySet().contains(year)) {
                    yearToWord.get(year).put(word, timesAppeared);
                } else {
                    YearlyRecord yr = new YearlyRecord();
                    yr.put(word, timesAppeared);
                    yearToWord.put(year, yr);
                }
    
            }
            
            reader.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    
    /** Returns the absolute count of WORD in the given YEAR. If the word
      * did not appear in the given year, return 0. */
    public int countInYear(String word, int year) {

        return yearToWord.get(year).count(word);
        
    }

    /** Returns a defensive copy of the YearlyRecord of WORD. */
    public YearlyRecord getRecord(int year) {
        YearlyRecord orig = yearToWord.get(year);
        YearlyRecord copy = new YearlyRecord();
        Collection<String> words = orig.words();
        
        for (String word : words) {
            copy.put(word, orig.count(word));
        }
        return copy;
        
    }

    /** Returns the total number of words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory() {
        return totalWords;
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear) {
        TimeSeries<Integer> ts = this.countHistory(word);
        return new TimeSeries<Integer>(ts, startYear, endYear);
        
    }

    /** Provides a defensive copy of the history of WORD. */
    public TimeSeries<Integer> countHistory(String word) {
        TimeSeries<Integer> ts = new TimeSeries<Integer>();
        
        for (Integer year : yearToWord.keySet()) {
            Integer countOfWord = yearToWord.get(year).count(word);
            if (countOfWord > 0) {
                ts.put(year, countOfWord);
            }
        }
        
        return ts;
    }

    /** Provides the relative frequency of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear) {
        TimeSeries<Double> ts = this.weightHistory(word);
        return new TimeSeries<Double>(ts, startYear, endYear);
    }

    /** Provides the relative frequency of WORD. */
    public TimeSeries<Double> weightHistory(String word) {
        return this.countHistory(word).dividedBy(this.totalCountHistory());
    }

    /** Provides the summed relative frequency of all WORDS between
      * STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> 
        summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        TimeSeries<Double> ts = this.summedWeightHistory(words);
        TimeSeries<Double> newSeries = new TimeSeries<Double>(ts, startYear, endYear);
        return newSeries;
       
    }
                              

    /** Returns the summed relative frequency of all WORDS. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words) {
        TimeSeries<Double> sum = new TimeSeries<Double>();
        for (String word : words) {
            sum = sum.plus(this.weightHistory(word));
        }
        return sum;
        
        
    }

    /** Provides processed history of all words between STARTYEAR and ENDYEAR as processed
      * by YRP. */
    public TimeSeries<Double> 
        processedHistory(int startYear, int endYear, YearlyRecordProcessor yrp) {
        TimeSeries<Double> processed = new TimeSeries<Double>();
        NavigableMap<Integer, YearlyRecord> map = yearToWord.subMap(startYear, true, endYear, true);
        
        for (Integer year : map.keySet()) {
            processed.put(year, yrp.process(yearToWord.get(year)));
            
        }
        
        return processed; 
    }
                                               

    /** Provides processed history of all words ever as processed by YRP. */
    public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp) {
        TimeSeries<Double> processed = new TimeSeries<Double>();
        for (Integer year : yearToWord.keySet()) {
            processed.put(year, yrp.process(yearToWord.get(year)));
        }
        
        return processed;
    }
}
