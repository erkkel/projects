import java.util.Comparator;
public class NodeComparatorReverse<Node> implements Comparator<TernaryTrie.Node>{
    public int compare(TernaryTrie.Node o1, TernaryTrie.Node o2) {
        double number = o2.getWeight() - o1.getWeight();

        if (number < 0) {
            return -1;
        } else if (number > 0) {
            return 1;
        } else {
            return 0;
        }

    }

}