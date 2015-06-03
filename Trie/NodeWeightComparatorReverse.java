import java.util.Comparator;
public class NodeWeightComparatorReverse<Node> implements Comparator<TernaryTrie.Node>{
    public int compare(TernaryTrie.Node o1, TernaryTrie.Node o2) {
        double number = o1.getWeight() - o2.getWeight();

        if (number < 0) {
            return -1;
        } else if (number > 0) {
            return 1;
        } else {
            return 0;
        }

    }

}