package refactortoec.generation;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Sets;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static refactortoec.generation.Generation.*;
import static refactortoec.generation.GenerationJdk.GENERATION_SET;
import static refactortoec.generation.GenerationJdk.find;
import static refactortoec.util.MemoryMeter.outputMemory;

public class GenerationJdkToEcRefactorTest
{
    @Test
    public void counting()
    {
        long count = GENERATION_SET.stream()
                .filter(generation -> generation.contains(1995))
                .count();

        assertEquals(1L, count);

        Map<Long, Long> generationCountByYears =
                GENERATION_SET.stream()
                        .collect(Collectors.groupingBy(
                                generation -> generation.yearsStream().count(),
                                Collectors.counting()));

        var expected = new HashMap<>();
        expected.put(17L, 2L);
        expected.put(16L, 3L);
        expected.put(19L, 1L);
        expected.put(18L, 2L);
        expected.put(23L, 1L);
        expected.put(27L, 1L);
        expected.put(1843L, 1L);
        assertEquals(expected, generationCountByYears);
        assertNull(generationCountByYears.get(30L));

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

        assertEquals(MILLENNIAL, find(1985));
        assertEquals(ALPHA, find(2016));
    }

    @Test
    public void filtering()
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

        // java.util.HashSet (760)
        // Java 25 COH (648)
        outputMemory(filtered);
    }

    @Test
    public void grouping()
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

        // java.util.HashMap (3832)
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
