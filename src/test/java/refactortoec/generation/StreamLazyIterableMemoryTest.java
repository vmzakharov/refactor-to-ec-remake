package refactortoec.generation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamLazyIterableMemoryTest
{
    @Test
    public void emptyArrayListAndStreamFilter()
    {
        // An Empty ArrayList costs 40 bytes
        // 24 bytes for ArrayList + 16 bytes for singleton Empty Array
        assertEquals(
                40, GraphLayout.parseInstance(new ArrayList<>()).totalSize());
        assertEquals(
                24, ClassLayout.parseInstance(new ArrayList<>()).instanceSize());

        Stream<?> stream =
                new ArrayList<>().stream()
                        .filter(each -> true);

        // An Empty Filter Stream costs 176 bytes (Includes empty ArrayList)
        assertEquals(
                176, GraphLayout.parseInstance(stream).totalSize());
        System.out.println(GraphLayout.parseInstance(stream).toFootprint());
    }

    @Test
    public void emptyFastListAndLazyIterableSelect()
    {
        // An Empty ArrayList costs 32 bytes
        // 16 bytes for FastList + 16 bytes for singleton Empty Array
        assertEquals(
                32, GraphLayout.parseInstance(new FastList<>()).totalSize());
        assertEquals(
                16, ClassLayout.parseInstance(new FastList<>()).instanceSize());

        LazyIterable<?> lazyIterable =
                new FastList<>().asLazy()
                        .select(each -> true);

        // An Empty Select LazyIterable costs 56 bytes (Includes empty FastList)
        assertEquals(
                56, GraphLayout.parseInstance(lazyIterable).totalSize());
        System.out.println(GraphLayout.parseInstance(lazyIterable).toFootprint());
    }

    @Test
    public void emptyArrayListAndStreamFilterMap()
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

        // An Empty Filter/Map Stream costs 240 bytes (Includes empty ArrayList)
        assertEquals(
                240, GraphLayout.parseInstance(stream).totalSize());
    }

    @Test
    public void emptyFastListAndLazyIterableSelectCollect()
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
                80, GraphLayout.parseInstance(lazyIterable).totalSize());
        System.out.println(GraphLayout.parseInstance(lazyIterable).toFootprint());
    }

    @Test
    public void streamVsLazyIterableMemory()
    {
        List<String> list = List.of();
        ImmutableList<String> ecList = Lists.immutable.empty();
        long listSize = GraphLayout.parseInstance(list).totalSize();
        long ecListSize = GraphLayout.parseInstance(ecList).totalSize();

        System.out.println("JDK Type | # Bytes | Eclipse Collections Type | # Bytes");
        System.out.println("-------- | ------ | ------- | --------");

        var stream = list.stream();
        var asLazy = ecList.asLazy();
        System.out.printf(
                "List.of().stream() | %s | Lists.immutable.empty().asLazy() | %s \n",
                GraphLayout.parseInstance(stream).totalSize() - listSize,
                GraphLayout.parseInstance(asLazy).totalSize() - ecListSize);
        var streamFilter = list.stream().filter(i -> true);
        var lazySelect = Lists.immutable.empty().asLazy().select(i -> true);
        System.out.printf(
                "Stream.filter() | %s | LazyIterable.select() | %s \n",
                GraphLayout.parseInstance(streamFilter).totalSize() - listSize,
                GraphLayout.parseInstance(lazySelect).totalSize() - ecListSize);
        var streamMap = list.stream().map(i -> null);
        var lazyCollect = Lists.immutable.empty().asLazy().collect(i -> null);
        System.out.printf(
                "Stream.map() | %s | LazyIterable.collect() | %s \n",
                GraphLayout.parseInstance(streamMap).totalSize() - listSize,
                GraphLayout.parseInstance(lazyCollect).totalSize() - ecListSize);
        var streamLimit = list.stream().limit(1L);
        var lazyTake = Lists.immutable.empty().asLazy().take(1);
        System.out.printf(
                "Stream.limit() | %s | LazyIterable.take() | %s \n",
                GraphLayout.parseInstance(streamLimit).totalSize() - listSize,
                GraphLayout.parseInstance(lazyTake).totalSize() - ecListSize);
        var streamFlatMap = list.stream().flatMap(i -> null);
        var lazyFlatCollect = Lists.immutable.empty().asLazy().flatCollect(i -> null);
        System.out.printf(
                "Stream.flatMap() | %s | LazyIterable.flatCollect() | %s \n",
                GraphLayout.parseInstance(streamFlatMap).totalSize() - listSize,
                GraphLayout.parseInstance(lazyFlatCollect).totalSize() - ecListSize);
        var streamMapToLong = list.stream().mapToLong(i -> 1L);
        var lazyCollectLong = Lists.immutable.empty().asLazy().collectLong(i -> 1L);
        System.out.printf(
                "Stream.mapToLong() | %s | LazyIterable.collectLong() | %s \n",
                GraphLayout.parseInstance(streamMapToLong).totalSize() - listSize,
                GraphLayout.parseInstance(lazyCollectLong).totalSize() - ecListSize);
        var streamMapToInt = list.stream().mapToInt(i -> 1);
        var lazyCollectInt = Lists.immutable.empty().asLazy().collectInt(i -> 1);
        System.out.printf(
                "Stream.mapToInt() | %s | LazyIterable.collectInt() | %s \n",
                GraphLayout.parseInstance(streamMapToInt).totalSize() - listSize,
                GraphLayout.parseInstance(lazyCollectInt).totalSize() - ecListSize);
        var streamMapToDouble = list.stream().mapToDouble(i -> 1.0);
        var lazyCollectDouble = Lists.immutable.empty().asLazy().collectDouble(i -> 1.0);
        System.out.printf(
                "Stream.mapToDouble() | %s | LazyIterable.collectDouble() | %s \n",
                GraphLayout.parseInstance(streamMapToDouble).totalSize() - listSize,
                GraphLayout.parseInstance(lazyCollectDouble).totalSize() - ecListSize);
        var streamDistinct = list.stream().distinct();
        var lazyDistinct = Lists.immutable.empty().asLazy().distinct();
        System.out.printf(
                "Stream.distinct() | %s | LazyIterable.distinct() | %s \n",
                GraphLayout.parseInstance(streamDistinct).totalSize() - listSize,
                GraphLayout.parseInstance(lazyDistinct).totalSize() - ecListSize);
        var streamDropWhile = list.stream().dropWhile(i -> true);
        var lazyDropWhile = Lists.immutable.empty().asLazy().dropWhile(i -> true);
        System.out.printf(
                "Stream.dropWhile() | %s | LazyIterable.dropWhile() | %s \n",
                GraphLayout.parseInstance(streamDropWhile).totalSize() - listSize,
                GraphLayout.parseInstance(lazyDropWhile).totalSize() - ecListSize);
        var streamTakeWhile = list.stream().takeWhile(i -> true);
        var lazyTakeWhile = Lists.immutable.empty().asLazy().takeWhile(i -> true);
        System.out.printf(
                "Stream.takeWhile() | %s | LazyIterable.takeWhile() | %s \n",
                GraphLayout.parseInstance(streamTakeWhile).totalSize() - listSize,
                GraphLayout.parseInstance(lazyTakeWhile).totalSize() - ecListSize);
        var streamPeek = list.stream().peek(i -> {});
        var lazyTap = Lists.immutable.empty().asLazy().tap(i -> {});
        System.out.printf(
                "Stream.peek() | %s | LazyIterable.tap() | %s \n",
                GraphLayout.parseInstance(streamPeek).totalSize() - listSize,
                GraphLayout.parseInstance(lazyTap).totalSize() - ecListSize);
        var streamGatherWindowFixed = list.stream().gather(Gatherers.windowFixed(2));
        var lazyChunk = Lists.immutable.empty().asLazy().chunk(2);
        System.out.printf(
                "Stream.gather(Gatherers.windowFixed()) | %s | LazyIterable.chunk() | %s \n",
                GraphLayout.parseInstance(streamGatherWindowFixed).totalSize() - listSize,
                GraphLayout.parseInstance(lazyChunk).totalSize() - ecListSize);
        var streamGatherFold = list.stream().gather(Gatherers.fold(Object::new, (o, o2) -> null));
        System.out.printf(
                "Stream.gather(Gatherers.fold()) | %s | LazyIterable.injectInto() | 0 \n",
                GraphLayout.parseInstance(streamGatherFold).totalSize() - listSize);
    }
}

