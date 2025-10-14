package refactortoec.generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.collections.impl.list.Interval;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpliteratorTest
{
    private final Interval interval = Interval.oneTo(1_000_000);
    private final List<Integer> listN = List.copyOf(interval);
    private final List<Integer> arrayList = new ArrayList<>(interval);
    private final List<Integer> arraysAsList = Arrays.asList(interval.toArray());

    @Test
    public void listNSpliteratorType()
    {
        assertEquals("RandomAccessSpliterator", listN.spliterator().getClass().getSimpleName());
    }

    @Test
    public void ArraysAsListSpliteratorType()
    {
        assertEquals("ArraySpliterator", arraysAsList.spliterator().getClass().getSimpleName());
    }

    @Test
    public void ArrayListSpliteratorType()
    {
        assertEquals("ArrayListSpliterator", arrayList.spliterator().getClass().getSimpleName());
    }
}
