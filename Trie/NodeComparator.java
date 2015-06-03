import java.util.Comparator;
public class NodeComparator<Node> implements Comparator<TernaryTrie.Node>{
    public int compare(TernaryTrie.Node o1, TernaryTrie.Node o2) {
        double number = o2.getMaxChildWeight() - o1.getMaxChildWeight();

        if (number < 0) {
            return -1;
        } else if (number > 0) {
            return 1;
        } else {
            return 0;
        }

    }

}