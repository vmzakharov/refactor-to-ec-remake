package refactortoec.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.api.block.function.primitive.LongFunction;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.primitive.IntLists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.list.Interval;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.primitive.IntInterval;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @ParameterizedTest
    @MethodSource("jdkListProvider")
    public void javaTypesForImmutableListAndStream(List<String> list)
    {
        var stream = list.stream();
        var filter = stream.filter(i -> true);
        var map = filter.map(Object::toString);
        var mapToLong = map.mapToLong(String::length);
        IO.println(GraphLayout.parseInstance(mapToLong).toFootprint());
        assertTrue(mapToLong.allMatch(each -> each == 1));
        IO.println("** stream = " + stream.getClass().getTypeName());
        IO.println("** filter = " + filter.getClass().getTypeName());
        IO.println("** map = " + map.getClass().getTypeName());
        IO.println("** mapToLong = " + mapToLong.getClass().getTypeName());
        IO.println("** Size of -> " + list.getClass().getSimpleName() + " = " + list.size());
        IO.println("=======================");
    }

    static Stream<Arguments> jdkListProvider()
    {
        // Returns a Stream of Arguments containing List.of() from size 0 to 9
        // e.g. List.of(), List.of("1"), List.of("1", "2"), etc.
        var list = Stream.concat(
                        Stream.of(Arguments.of(List.of())),
                        IntStream.range(2, 11)
                                .mapToObj(i -> IntStream.range(1, i))
                                .map(s -> s.mapToObj(Integer::toString))
                                .map(Stream::toList)
                                .map(List::copyOf)
                                .map(Arguments::of))
                .collect(Collectors.toList());
        Collections.shuffle(list);
        return list.stream();
    }

    @ParameterizedTest
    @MethodSource("ecImmutableListProvider")
    public void eclipseCollectionTypesForImmutableListAndLazyIterable(ImmutableList<String> list)
    {
        var lazy = list.asLazy();
        var select = lazy.select(i -> true);
        var collect = select.collect(Object::toString);
        var collectLong = collect.collectLong(String::length);
        IO.println(GraphLayout.parseInstance(collectLong).toFootprint());
        assertTrue(collectLong.allSatisfy(each -> each == 1));
        // IO.println("** lazy = " + lazy.getClass().getSimpleName());
        // IO.println("** select = " + select.getClass().getSimpleName());
        // IO.println("** collect = " + collect.getClass().getSimpleName());
        // IO.println("** collectLong = " + collectLong.getClass().getSimpleName());
        // IO.println("** Size of -> " + list.getClass().getSimpleName() + " = " + list.size());
        // IO.println("=======================");
    }

    static Stream<Arguments> ecImmutableListProvider()
    {
        // Returns a Stream of Arguments containing Lists.immutable.of() from size 0 to 9
        // e.g. Lists.immutable.of(), Lists.immutable.of("1"), Lists.immutable.of("1", "2"), etc.
        return Lists.mutable.of(Arguments.of(Lists.immutable.of()))
                .withAll(
                        IntInterval.oneTo(9)
                                .collect(IntInterval::oneTo)
                                .collect(interval -> interval.collect(Integer::toString))
                                .collect(Arguments::of))
                .shuffleThis()
                .stream();
    }
}

