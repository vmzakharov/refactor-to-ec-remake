package refactortoec;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import refactortoec.util.FailOnSizeChangeIterator;

public class ModCountTest
{
    @Test
    public void howToEncounterAConcurrentModificationExceptionInArrayList()
    {
        List<Integer> integers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        Assertions.assertThrows(
                ConcurrentModificationException.class,
                () -> {
                    for (Integer each : integers)
                    {
                        if (each % 2 == 0)
                        {
                            integers.remove(each);
                        }
                    }
                });
    }

    @Test
    public void howToEncounterAConcurrentModificationExceptionInArrayDeque()
    {
        Deque<Integer> integers = new ArrayDeque<>(List.of(1, 2, 3, 4, 5));
        Assertions.assertThrows(
                ConcurrentModificationException.class,
                () -> {
                    for (Integer each : integers)
                    {
                        if (each % 2 == 0)
                        {
                            integers.remove(each);
                        }
                    }
                });
    }

    @Test
    public void howToBypassConcurrentModificationExceptionInArrayList()
    {
        List<Integer> integers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        for (int i = 0; i < integers.size(); i++)
        {
            Integer each = integers.get(i);
            if (each % 2 == 0)
            {
                integers.remove(each);
            }
        }
        Assertions.assertEquals(List.of(1, 3, 5), integers);
    }

    @Test
    public void howToAvoidConcurrentModificationExceptionInArrayList1()
    {
        List<Integer> integers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        for (Iterator<Integer> it = integers.iterator(); it.hasNext(); )
        {
            Integer each = it.next();
            if (each % 2 == 0)
            {
                it.remove();
            }
        }
        Assertions.assertEquals(List.of(1, 3, 5), integers);
    }

    @Test
    public void howToAvoidConcurrentModificationExceptionInArrayList2()
    {
        List<Integer> integers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        List<Integer> result = new ArrayList<>(integers);
        for (Integer each : integers)
        {
            if (each % 2 == 0)
            {
                result.remove(each);
            }
        }
        Assertions.assertEquals(List.of(1, 3, 5), result);
    }

    @Test
    public void howToAvoidConcurrentModificationExceptionInArrayList3()
    {
        List<Integer> integers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        List<Integer> result = integers.stream()
                .filter(each -> each % 2 != 0)
                .toList();
        Assertions.assertEquals(List.of(1, 3, 5), result);
    }

    @Test
    public void howToAvoidConcurrentModificationExceptionInArrayList4()
    {
        List<Integer> integers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        integers.removeIf(each -> each % 2 == 0);
        Assertions.assertEquals(List.of(1, 3, 5), integers);
    }

    @Test
    public void nothingThrownWithCopyOnWriteArrayList()
    {
        List<Integer> integers = new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4, 5));
        for (Integer each : integers)
        {
            if (each % 2 == 0)
            {
                integers.remove(each);
            }
        }
        Assertions.assertEquals(List.of(1, 3, 5), integers);
    }

    @Test
    public void howToEncounterAnExceptionInFastList()
    {
        FastList<Integer> integers = new FastList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> {
                    for (Integer each : integers)
                    {
                        if (each % 2 == 0)
                        {
                            integers.remove(each);
                        }
                    }
                });
    }

@Test
public void howToEncounterAFailFastExceptionInFastList()
{
    FastList<Integer> integers = new FastList<>(List.of(1, 2, 3, 4, 5));
    Assertions.assertThrows(
            ConcurrentModificationException.class,
            () -> {
                for (Iterator<Integer> it = new FailOnSizeChangeIterator<>(integers); it.hasNext(); )
                {
                    Integer each = it.next();
                    if (each % 2 == 0)
                    {
                        integers.remove(each);
                    }
                }
            });
}

    @Test
    public void howToEncounterAConcurrentModificationExceptionInHashSet()
    {
        HashSet<Integer> integers = new HashSet<>(List.of(1, 2, 3, 4, 5));
        Assertions.assertThrows(
                ConcurrentModificationException.class,
                () -> {
                    for (Integer each : integers)
                    {
                        if (each % 2 == 0)
                        {
                            integers.remove(each);
                        }
                    }
                });
    }

    @Test
    public void howToEncounterAConcurrentModificationExceptionInUnifiedSet()
    {
        Set<Integer> integers = UnifiedSet.newSetWith(1, 2, 3, 4, 5);
        for (Integer each : integers)
        {
            if (each % 2 == 0)
            {
                integers.remove(each);
            }
        }
        Assertions.assertEquals(Set.of(1, 3, 5), integers);
    }
}
