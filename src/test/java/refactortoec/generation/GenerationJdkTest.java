package refactortoec.generation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
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
import static refactortoec.util.MemoryMeter.outputMemory;

public class GenerationJdkTest
{
    @Test
    public void counting()
    {
        long count = GENERATION_SET.stream()
                .filter(generation -> generation.contains(1995))
                .count();

        assertEquals(1L, count);

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
        assertNull(generationCountByYears.get(30));

        // java.util.HashMap (592)
        // Java 25 COH (448)
        outputMemory(generationCountByYears);
    }

    @Test
    public void testing()
    {
        assertTrue(GENERATION_SET.stream()
                .anyMatch(generation -> generation.contains(1995)));
        assertFalse(GENERATION_SET.stream()
                .allMatch(generation -> generation.contains(1995)));
        assertFalse(GENERATION_SET.stream()
                .noneMatch(generation -> generation.contains(1995)));

        assertTrue(ALPHA.contains(2024));
        assertFalse(ALPHA.contains(2000));
        assertTrue(MILLENNIAL.contains(1985));
        assertFalse(MILLENNIAL.contains(1960));
    }

    @Test
    public void finding()
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

    @Test
    public void filtering()
    {
        Set<Generation> filteredSelected =
                GENERATION_SET.stream()
                        .filter(generation -> generation.yearsCountEqualsJdk(16))
                        .collect(Collectors.toUnmodifiableSet());

        var expectedSelected = Set.of(X, MILLENNIAL, Z);
        assertEquals(expectedSelected, filteredSelected);

        Set<Generation> filteredRejected =
                GENERATION_SET.stream()
                        .filter(generation -> !generation.yearsCountEqualsJdk(16))
                        .collect(Collectors.toUnmodifiableSet());

        var expectedRejected =
                Sets.mutable.with(ALPHA, UNCLASSIFIED, BOOMER, GREATEST, LOST, MISSIONARY, PROGRESSIVE, SILENT);
        assertEquals(expectedRejected, filteredRejected);

        Map<Boolean, Set<Generation>> partition = GENERATION_SET.stream()
                .collect(Collectors.partitioningBy(
                        generation -> generation.yearsCountEqualsJdk(16),
                        Collectors.toUnmodifiableSet()));

        assertEquals(expectedSelected, partition.get(Boolean.TRUE));
        assertEquals(expectedRejected, partition.get(Boolean.FALSE));

        // java.util.HashSet (760)
        // Java 25 COH (648)
        outputMemory(filteredSelected);
    }

    @Test
    public void grouping()
    {
        Map<Integer, Set<Generation>> generationByYears =
                GENERATION_SET.stream()
                        .collect(Collectors.groupingBy(Generation::numberOfYears,
                                Collectors.toSet()));

        var expected = new HashMap<>();
        expected.put(17, Set.of(ALPHA, PROGRESSIVE));
        expected.put(16, Set.of(X, MILLENNIAL, Z));
        expected.put(19, Set.of(BOOMER));
        expected.put(18, Set.of(SILENT, LOST));
        expected.put(23, Set.of(MISSIONARY));
        expected.put(27, Set.of(GREATEST));
        expected.put(1843, Set.of(UNCLASSIFIED));

        assertEquals(expected, generationByYears);
        assertNull(generationByYears.get(30));

        // java.util.HashMap (3776)
        // Java 25 COH (3360)
        outputMemory(generationByYears);
    }

    @Test
    public void converting()
    {
        List<Generation> mutableList =
                GENERATION_SET.stream()
                        .collect(Collectors.toList());
        List<Generation> immutableList =
                GENERATION_SET.stream()
                        .toList();

        // ArrayList (1928)
        // Java 25 COH (1720)
        outputMemory(mutableList);
        // ImmutableCollections$ListN (1912)
        // Java 25 COH (1696)
        outputMemory(immutableList);

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

    @Test
    public void transforming()
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

        // ImmutableCollections$SetN (776)
        // Java 25 COH (712)
        outputMemory(names);
        // java.util.HashSet (1176)
        // Java 25 COH (1016)
        outputMemory(mutableNames);
    }

    @Test
    public void chunking()
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

    @Test
    public void folding()
    {
        Integer maxYears = GenerationJdk.fold(
                Integer.MIN_VALUE,
                (Integer value, Generation generation) ->
                        Math.max(value, generation.numberOfYears()));

        Integer minYears = GenerationJdk.fold(
                Integer.MAX_VALUE,
                (Integer value, Generation generation) ->
                        Math.min(value, generation.numberOfYears()));

        Integer sumYears = GenerationJdk.fold(
                Integer.valueOf(0),
                (Integer value, Generation generation) ->
                        Integer.sum(value, generation.numberOfYears()));

        assertEquals(1843, maxYears);
        assertEquals(16, minYears);
        assertEquals(2030, sumYears);
    }
}
