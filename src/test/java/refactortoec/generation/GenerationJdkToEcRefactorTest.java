package refactortoec.generation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Sets;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static refactortoec.generation.Generation.ALPHA;
import static refactortoec.generation.Generation.BOOMER;
import static refactortoec.generation.Generation.GREATEST;
import static refactortoec.generation.Generation.LOST;
import static refactortoec.generation.Generation.MILLENNIAL;
import static refactortoec.generation.Generation.MISSIONARY;
import static refactortoec.generation.Generation.PROGRESSIVE;
import static refactortoec.generation.Generation.SILENT;
import static refactortoec.generation.Generation.UNCLASSIFIED;
import static refactortoec.generation.Generation.X;
import static refactortoec.generation.Generation.Z;
import static refactortoec.generation.Generation.values;
import static refactortoec.generation.GenerationJdk.GENERATION_SET;

/**
 * In this test we will refactor from JDK patterns to Eclipse Collections patterns.
 * The categories of patterns we will cover in this refactoring are:
 *
 * <ul>
 * <li>Counting - 🧮</li>
 * <li>Testing - 🧪</li>
 * <li>Finding - 🔎</li>
 * <li>Filtering - 🚰</li>
 * <li>Grouping - 🏘️</li>
 * <li>Converting - 🔌</li>
 * <li>Transforming - 🦋</li>
 * <li>Chunking - 🖖</li>
 * <li>Folding - 🪭</li>
 * </ul>
 *
 * Note: We work with unit tests so we know code works to start, and continues to
 * work after the refactoring is complete.
 */
public class GenerationJdkToEcRefactorTest
{
    /**
     * There are two use cases for counting we will explore.
     *
     * 1. Counting with a Predicate -> return is a primitive value
     * 2. Counting by a Function -> return is a Map<Long, Long>
     */
    @Test
    public void counting() // 🧮🐿️
    {
        // Counting with Predicate -> Count of Generation instances that match
        long count = GENERATION_SET.stream()
                .filter(generation -> generation.contains(1995))
                .count();

        assertEquals(1L, count);

        // Counting by a Function -> Number of years in a Generation -> Count of Generations
        Map<Integer, Long> generationCountByYears =
                GENERATION_SET.stream()
                        .collect(Collectors.groupingBy(Generation::numberOfYears,
                                Collectors.counting()));

        var expected = new HashMap<>();
        expected.put(17, 2L);
        expected.put(16, 3L);
        expected.put(19, 1L);
        expected.put(18, 2L);
        expected.put(23, 1L);
        expected.put(27, 1L);
        expected.put(1843, 1L);
        assertEquals(expected, generationCountByYears);
        assertNull(generationCountByYears.get(30L));
    }

    /**
     * Testing methods return a boolean. We will explore three testing methods. Testing methods
     * are always eager, but can often short-circuit execution, meaning they don't have to visit
     * all elements of the collection if the condition is met.
     *
     * 1. Stream.anyMatch(Predicate) -> RichIterable.anySatisfy(Predicate)
     * 2. Stream.allMatch(Predicate) -> RichIterable.allSatisfy(Predicate)
     * 3. Stream.noneMatch(Predicate) -> RichIterable.noneSatisfy(Predicate)
     */
    @Test
    public void testing() // 🧪🦤
    {
        assertTrue(GENERATION_SET.stream()
                .anyMatch(generation -> generation.contains(1995)));
        assertFalse(GENERATION_SET.stream()
                .allMatch(generation -> generation.contains(1995)));
        assertFalse(GENERATION_SET.stream()
                .noneMatch(generation -> generation.contains(1995)));
    }

    /**
     * Finding methods return some element of a collection. Finding methods are always
     * eager.
     *
     * 1. Stream.filter(Predicate).findFirst() -> RichIterable.detect(Predicate) / detectOptional(Predicate)
     * 2. Collectors.maxBy(Comparator) -> RichIterable.maxBy(Function)
     * 3. Collectors.minBy(Comparator) -> RichIterable.minBy(Function)
     */
    @Test
    public void finding() // 🔎🐿️
    {
        Generation findFirst =
                GENERATION_SET.stream()
                        .filter(generation -> generation.contains(1995))
                        .findFirst()
                        .orElse(null);

        assertEquals(MILLENNIAL, findFirst);

        Generation notFound =
                GENERATION_SET.stream()
                        .filter(generation -> generation.contains(1795))
                        .findFirst()
                        .orElse(UNCLASSIFIED);

        assertEquals(UNCLASSIFIED, notFound);

        List<Generation> generationsNotUnclassified =
                Stream.of(Generation.values())
                        .filter(gen -> !gen.equals(UNCLASSIFIED))
                        .toList();

        Generation maxByYears =
                generationsNotUnclassified.stream()
                        .collect(Collectors.maxBy(
                                Comparator.comparing(Generation::numberOfYears)))
                        .orElse(null);
        assertEquals(GREATEST, maxByYears);

        Generation minByYears =
                generationsNotUnclassified.stream()
                        .collect(Collectors.minBy(
                                Comparator.comparing(Generation::numberOfYears)))
                        .orElse(null);
        assertEquals(X, minByYears);
    }

    /**
     * Filtering methods return another Stream or Collection based on a Predicate.
     * Filtering can be eager or lazy. We will explore three filtering methods.
     *
     * 1. Stream.filter(Predicate) -> RichIterable.select(Predicate)
     * 2. Stream.filter(Predicate.not()) -> RichIterable.reject(Predicate)
     * 3. Collectors.partitioningBy(Predicate) -> RichIterable.partition(Predicate)
     */
    @Test
    public void filtering() // 🚰🦤
    {
        Set<Generation> filtered =
                GENERATION_SET.stream()
                        .filter(generation -> generation.yearsCountEqualsJdk(16))
                        .collect(Collectors.toUnmodifiableSet());

        var expected = Set.of(X, MILLENNIAL, Z);
        assertEquals(expected, filtered);

        Set<Generation> filteredNot =
                GENERATION_SET.stream()
                        .filter(generation -> !generation.yearsCountEqualsJdk(16))
                        .collect(Collectors.toUnmodifiableSet());

        var expectedNot =
                Sets.mutable.with(ALPHA, UNCLASSIFIED, BOOMER, GREATEST, LOST, MISSIONARY, PROGRESSIVE, SILENT);
        assertEquals(expectedNot, filteredNot);

        Map<Boolean, Set<Generation>> partition = GENERATION_SET.stream()
                .collect(Collectors.partitioningBy(
                        generation -> generation.yearsCountEqualsJdk(16),
                        Collectors.toUnmodifiableSet()));

        assertEquals(expected, partition.get(Boolean.TRUE));
        assertEquals(expectedNot, partition.get(Boolean.FALSE));
    }

    /**
     * Grouping methods return a Map with some key calculated by a Function and the values
     * contained in a Collection. We will explore one grouping method.
     *
     * 1. Collectors.groupingBy(Function) -> RichIterable.groupBy(Function)
     */
    @Test
    public void grouping() // 🏘️🐿️
    {
        Map<Long, Set<Generation>> generationByYears =
                GENERATION_SET.stream()
                        .collect(Collectors.groupingBy(
                                generation -> generation.yearsStream().count(),
                                Collectors.toSet()));

        var expected = new HashMap<>();
        expected.put(17L, Set.of(ALPHA, PROGRESSIVE));
        expected.put(16L, Set.of(X, MILLENNIAL, Z));
        expected.put(19L, Set.of(BOOMER));
        expected.put(18L, Set.of(SILENT, LOST));
        expected.put(23L, Set.of(MISSIONARY));
        expected.put(27L, Set.of(GREATEST));
        expected.put(1843L, Set.of(UNCLASSIFIED));

        assertEquals(expected, generationByYears);
        assertNull(generationByYears.get(30L));
    }

    /**
     * Converting method convert from a source Collection type to a target Collection
     * type. Converting methods in both Java and Eclipse Collections usually have
     * a prefix of "to". We'll explore a few converting methods in this test.
     *
     * 1. Collectors.toList() -> RichIterable.toList()
     * 2. Stream.toList() -> RichIterable.toImmutableList()
     */
    @Test
    public void converting() // 🔌🦤
    {
        List<Generation> mutableList =
                GENERATION_SET.stream()
                        .collect(Collectors.toList());
        List<Generation> immutableList =
                GENERATION_SET.stream()
                        .toList();

        List<Generation> sortedMutableList =
                mutableList.stream()
                        .sorted(Comparator.comparing(gen -> gen.yearsStream().findFirst().getAsInt()))
                        .collect(Collectors.toList());

        var expected = Lists.mutable.with(values());
        assertEquals(expected, sortedMutableList);

        List<Generation> sortedImmutableList =
                immutableList.stream()
                        .sorted(Comparator.comparing(gen -> gen.yearsStream().findFirst().getAsInt()))
                        .toList();
        assertEquals(expected, sortedImmutableList);
    }

    /**
     * Transforming methods convert the elements of a collection to another type by
     * applying a Function to each element. We'll explore the following methods.
     *
     * 1. Stream.map() -> RichIterable.collect()
     * 2. Collectors.toUnmodifiableSet() -> ???
     *
     * Note: Certain methods on RichIterable are covariant, so return a type that
     * makes sense for the source type.
     * Hint: If we collect on an ImmutableSet, the return type is an ImmutableSet.
     */
    @Test
    public void transforming() // 🦋🐿️
    {
        Set<String> names =
                GENERATION_SET.stream()
                        .map(Generation::getName)
                        .collect(Collectors.toUnmodifiableSet());

        var expected = Sets.immutable.with(
                "Unclassified", "Greatest Generation", "Lost Generation", "Millennials",
                "Generation X", "Baby Boomers", "Generation Z", "Silent Generation", "Progressive Generation",
                "Generation Alpha", "Missionary Generation");
        assertEquals(expected, names);

        Set<String> mutableNames = names.stream()
                .collect(Collectors.toSet());
        assertEquals(expected, mutableNames);
    }

    /**
     * Chunking is a kind of grouping method, but for our purposes we will put
     * the methods in their own category. Chunking is great for breaking collections
     * into smaller collections based on a size parameter. We'll explore the following methods.
     *
     * 1. Stream.gather(Gatherers.windowFixed()) -> RichIterable.chunk()
     * 2. Collectors.joining() -> RichIterable.makeString()
     */
    @Test
    public void chunking() // 🖖🦤
    {
        Stream<List<Generation>> windowFixedGenerations =
                GenerationJdk.windowFixedGenerations(3);
        String generationsAsString = windowFixedGenerations.map(Object::toString)
                .collect(Collectors.joining(", "));

        String expected =
                "[UNCLASSIFIED, PROGRESSIVE, MISSIONARY], [LOST, GREATEST, SILENT], [BOOMER, X, MILLENNIAL], [Z, ALPHA]";
        assertEquals(expected, generationsAsString);

        String yearsAsString = MILLENNIAL.yearsStream()
                .boxed()
                .gather(Gatherers.windowFixed(4))
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        String expectedYears =
                "[1981, 1982, 1983, 1984], [1985, 1986, 1987, 1988], [1989, 1990, 1991, 1992], [1993, 1994, 1995, 1996]";
        assertEquals(expectedYears, yearsAsString);
    }

    /**
     * Folding is a mechanism for reducing a type to some new result type. We'll explore
     * folding to calculate a min, max, and sum. Methods we'll cover:
     *
     * 1. Stream.gather(Gatherers.fold() -> RichIterable.injectInto()
     */
    @Test
    public void folding() // 🪭🐿️
    {
        Integer maxYears = GenerationJdk.fold(
                Integer.MIN_VALUE,
                (Integer value, Generation generation) ->
                        Math.max(value, generation.yearsInterval().size()));

        Integer minYears = GenerationJdk.fold(
                Integer.MAX_VALUE,
                (Integer value, Generation generation) ->
                        Math.min(value, generation.yearsInterval().size()));

        Integer sumYears = GenerationJdk.fold(
                Integer.valueOf(0),
                (Integer value, Generation generation) ->
                        Integer.sum(value, generation.yearsInterval().size()));

        assertEquals(1843, maxYears);
        assertEquals(16, minYears);
        assertEquals(2030, sumYears);
    }
}
