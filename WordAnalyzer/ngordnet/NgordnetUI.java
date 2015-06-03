
package ngordnet;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;

/** Provides a simple user interface for exploring WordNet and NGram data.
 *  @author [Eric Wang]
 */
public class NgordnetUI {
    public static void main(String[] args) {
        In in = new In("./ngordnet/ngordnetui.config");

        String wordFile = in.readString();
        String countFile = in.readString();
        String synsetFile = in.readString();
        String hyponymFile = in.readString();

        WordNet net = new WordNet(synsetFile, hyponymFile);
        NGramMap map = new NGramMap(wordFile, countFile);
        
        int startDate = 1505;
        int endDate = 2008;

        while (true) {
            try {
                System.out.print("> ");
                String line = StdIn.readLine();
                String[] rawTokens = line.split(" ");
                String command = rawTokens[0];
                String[] tokens = new String[rawTokens.length - 1];
                System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
                switch (command) {
                    case "quit": 
                        return;
                    case "help":
                        In in2 = new In("./demos/help.txt");
                        String helpStr = in2.readAll();
                        System.out.println(helpStr);
                        break;  
                    case "range": 
                        startDate = Integer.parseInt(tokens[0]); 
                        endDate = Integer.parseInt(tokens[1]);  
                        System.out.println("Start date: " + startDate);
                        System.out.println("End date: " + endDate);
                        break;
                    case "count":
                        String word = tokens[0];
                        int year = Integer.parseInt(tokens[1]); 
                        System.out.println(map.countInYear(word, year));
                        break;
                    case "hyponyms":
                        String word2 = tokens[0];
                        System.out.println(net.hyponyms(word2));
                        break;
                    case "history":                 
                        Plotter.plotAllWords(map, tokens, startDate, endDate);
                        break;
                    case "hypohist":
                        Plotter.plotCategoryWeights(map, net, tokens, startDate, endDate);
                        break;
                    case "wordlength":
                        WordLengthProcessor wlp = new WordLengthProcessor();
                        Plotter.plotProcessedHistory(map, startDate, endDate, wlp);
                        break;
                    case "zipf":
                        int year2 = Integer.parseInt(tokens[0]);
                        Plotter.plotZipfsLaw(map, year2);
                        break;
                    default:
                }
                    System.out.println("Invalid command.");  
                    break;
              
                    //discussed with friends which exceptions to  catch
            } catch (NumberFormatException e){
                System.out.println(e);
            } catch (IllegalArgumentException e){
                System.out.println(e);
            } catch (IndexOutOfBoundsException e){
                System.out.println(e);
            } catch (NullPointerException e){
                System.out.println(e);
            }
        }

    }
} 
