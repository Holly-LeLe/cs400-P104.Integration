import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.Iterator;

/**
 * JUnit tests for IterableRedBlackTree iterator behavior.
 */
public class IterableRedBlackTreeTests {

    /**
     * Tests that an integer tree without min or max iterates in sorted order.
     */
    @Test
    public void testIntegerIterationNoBounds() {
        IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
        tree.insert(5);
        tree.insert(2);
        tree.insert(8);
        tree.insert(1);
        tree.insert(3);

        Iterator<Integer> it = tree.iterator();

        Assertions.assertEquals(1, it.next());
        Assertions.assertEquals(2, it.next());
        Assertions.assertEquals(3, it.next());
        Assertions.assertEquals(5, it.next());
        Assertions.assertEquals(8, it.next());
        Assertions.assertFalse(it.hasNext());
    }

    /**
     * Tests that string values iterate in sorted order with both min and max bounds.
     */
    @Test
    public void testStringIterationWithBounds() {
        IterableRedBlackTree<String> tree = new IterableRedBlackTree<>();
        tree.insert("banana");
        tree.insert("apple");
        tree.insert("date");
        tree.insert("cherry");
        tree.insert("fig");

        tree.setIteratorMin("banana");
        tree.setIteratorMax("date");

        Iterator<String> it = tree.iterator();

        Assertions.assertEquals("banana", it.next());
        Assertions.assertEquals("cherry", it.next());
        Assertions.assertEquals("date", it.next());
        Assertions.assertFalse(it.hasNext());
    }

    /**
     * Tests that duplicate values within the iterator range are not skipped.
     */
    @Test
    public void testDuplicateValuesWithinRange() {
        IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
        tree.insert(4);
        tree.insert(2);
        tree.insert(4);
        tree.insert(6);
        tree.insert(4);

        tree.setIteratorMin(4);
        tree.setIteratorMax(4);

        Iterator<Integer> it = tree.iterator();

        Assertions.assertEquals(4, it.next());
        Assertions.assertEquals(4, it.next());
        Assertions.assertEquals(4, it.next());
        Assertions.assertFalse(it.hasNext());
    }
}
