
import com.googlecode.concurrenttrees.common.KeyValuePair;
import com.googlecode.concurrenttrees.common.PrettyPrinter;
import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.node.NodeFactory;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;

import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ConcurrentRadixTreeTest {

    private final NodeFactory nodeFactory = new DefaultCharArrayNodeFactory();

    protected NodeFactory getNodeFactory()
    {
        return nodeFactory;
    }

    @Test
    public void testPut_AddToRoot()
    {
        ConcurrentRadixTree<Integer> tree = new ConcurrentRadixTree<Integer>(getNodeFactory());
        tree.put("A", 1);
        String expected =
                "○\n" +
                "└── ○ A (1)\n";
        String actual = PrettyPrinter.prettyPrint(tree);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPut_AppendChild() {
        ConcurrentRadixTree<Integer> tree = new ConcurrentRadixTree<Integer>(getNodeFactory());
        tree.put("FOO", 1);
        tree.put("FOOBAR", 2);

        String expected =
                "○\n" +
                        "└── ○ FOO (1)\n" +
                        "    └── ○ BAR (2)\n";
        String actual = PrettyPrinter.prettyPrint(tree);
        assertEquals(expected, actual);
    }

    @Test
    public void testKeysStartingWith()
    {
        ConcurrentRadixTree<Integer> tree = new ConcurrentRadixTree<Integer>(getNodeFactory());
        tree.put("FOO", 1);
        tree.put("FooBAR", 2);
        tree.put("Fast", 1);
        tree.put("Foster", 5);
        tree.put("test", 3);

        Iterable<KeyValuePair<Integer>> keyValuePairs = tree.getKeyValuePairsForKeysStartingWith("Fo");
        ArrayList<String> expectedKeys = new ArrayList<String>();
        expectedKeys.add("FooBAR");
        expectedKeys.add("Foster");
        for(KeyValuePair<Integer> keyValuePair : keyValuePairs)
        {
            expectedKeys.remove(keyValuePair.getKey());
        }
        Assert.assertTrue(expectedKeys.isEmpty());
    }
}
