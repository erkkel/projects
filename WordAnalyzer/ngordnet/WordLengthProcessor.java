package ngordnet;
import java.util.Collection;
public class WordLengthProcessor implements YearlyRecordProcessor {
    public double process(YearlyRecord yearlyRecord) {
        double weightedSum = 0;
        double totalCount = 0;
        Collection<String> allWords = yearlyRecord.words();
        for (String word : allWords) {
            int numInYear = yearlyRecord.count(word);
            totalCount += numInYear;
            weightedSum += numInYear * word.length();
        }
        return weightedSum / totalCount;
        
    }
}
 