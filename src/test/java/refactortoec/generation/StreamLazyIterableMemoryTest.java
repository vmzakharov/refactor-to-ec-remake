package refactortoec.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamLazyIterableMemoryTest
{
    @Test
    public void emptyArrayListAndStream()
    {
        // An Empty ArrayList costs 40 bytes
        // 24 bytes for ArrayList + 16 bytes for singleton Empty Array
        assertEquals(
                40, GraphLayout.parseInstance(new ArrayList<>()).totalSize());
        assertEquals(
                24, ClassLayout.parseInstance(new ArrayList<>()).instanceSize());

        Stream<?> stream =
                new ArrayList<>().stream()
                        .filter(each -> true)
                        .map(each -> each);

       System.out.println(GraphLayout.parseInstance(stream).toFootprint());
    }

    @Test
    public void emptyFastListAndLazyIterable()
    {
        // An Empty ArrayList costs 32 bytes
        // 16 bytes for FastList + 16 bytes for singleton Empty Array
        assertEquals(
                32, GraphLayout.parseInstance(new FastList<>()).totalSize());
        assertEquals(
                16, ClassLayout.parseInstance(new FastList<>()).instanceSize());

        LazyIterable<?> lazyIterable =
                new FastList<>().asLazy()
                        .select(each -> true)
                        .collect(each -> each);

        // An Empty Select/Collect LazyIterable costs 80 bytes (Includes empty FastList)
        assertEquals(
                120, GraphLayout.parseInstance(lazyIterable).totalSize());
        System.out.println(GraphLayout.parseInstance(lazyIterable).toFootprint());
    }

    @Test
    public void emptyImmutableJDKListAndStream()
    {
        assertEquals(
                32, GraphLayout.parseInstance(List.of()).totalSize());

        Stream<?> stream =
                List.of().stream()
                        .filter(each -> true)
                        .map(each -> each);
        assertEquals(
                240, GraphLayout.parseInstance(stream).totalSize());
    }

    @Test
    public void emptyImmutableListAndLazyIterable()
    {
        assertEquals(
                8, GraphLayout.parseInstance(Lists.immutable.empty()).totalSize());

        LazyIterable<?> lazyIterable = Lists.immutable.empty().asLazy()
                .select(each -> true)
                .collect(each -> each);

        assertEquals(
                56, GraphLayout.parseInstance(lazyIterable).totalSize());
    }
}

