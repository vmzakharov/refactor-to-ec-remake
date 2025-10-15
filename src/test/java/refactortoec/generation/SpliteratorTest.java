package refactortoec.generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpliteratorTest
{
    @Test
    public void listNSpliteratorType()
    {
        List<Integer> integers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertEquals(
                "RandomAccessSpliterator",
                integers.spliterator().getClass().getSimpleName());
    }

    @Test
    public void list12SpliteratorType()
    {
        List<Integer> list1Of12 = List.of(1);
        assertEquals(
                "",
                list1Of12.spliterator().getClass().getSimpleName());
        List<Integer> list2Of12 = List.of(1, 2);
        assertEquals(
                "RandomAccessSpliterator",
                list2Of12.spliterator().getClass().getSimpleName());
    }

    @Test
    public void ArraysAsListSpliteratorType()
    {
        List<Integer> arraysAsList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertEquals(
                "ArraySpliterator",
                arraysAsList.spliterator().getClass().getSimpleName());
    }

    @Test
    public void ArrayListSpliteratorType()
    {
        List<Integer> arrayList = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        assertEquals(
                "ArrayListSpliterator",
                arrayList.spliterator().getClass().getSimpleName());
    }
}
