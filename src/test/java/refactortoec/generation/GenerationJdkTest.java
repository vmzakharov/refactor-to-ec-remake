package refactortoec.generation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.collections.api.factory.Lists;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.GraphLayout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenerationJdkTest
{
    @Test
    public void contains()
    {
        assertTrue(GenerationJdk.ALPHA.contains(2024));
        assertFalse(GenerationJdk.ALPHA.contains(2000));
        assertTrue(GenerationJdk.MILLENNIAL.contains(1985));
        assertFalse(GenerationJdk.MILLENNIAL.contains(1960));
    }

    @Test
    public void find()
    {
        assertEquals(GenerationJdk.MILLENNIAL, GenerationJdk.find(1985));
        assertEquals(GenerationJdk.ALPHA, GenerationJdk.find(2016));
    }

    @Test
    public void counting()
    {
        assertEquals(
                1L,
                GenerationJdk.ALL.stream()
                        .filter(generation -> generation.contains(1995))
                        .count());

        Map<Long, Long> generationCountByYears =
                GenerationJdk.ALL.stream()
                        .collect(Collectors.groupingBy(generation -> generation.years().count(),
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
        System.out.println(GraphLayout.parseInstance(generationCountByYears).toFootprint());
    }

    @Test
    public void testing()
    {
        assertTrue(GenerationJdk.ALL.stream()
                .anyMatch(generation -> generation.contains(1995)));
    }

    @Test
    public void finding()
    {
        assertEquals(
                GenerationJdk.MILLENNIAL,
                GenerationJdk.ALL.stream()
                        .filter(generation -> generation.contains(1995))
                        .findFirst().orElse(null));
    }

    @Test
    public void filtering()
    {
        var expected = Set.of(GenerationJdk.X, GenerationJdk.MILLENNIAL, GenerationJdk.Z);
        Set<GenerationJdk> filtered = GenerationJdk.ALL.stream()
                .filter(generation -> generation.yearsCountEquals(16))
                .collect(Collectors.toSet());
        assertEquals(expected, filtered);

        // java.util.HashSet (712)
        System.out.println(GraphLayout.parseInstance(filtered).toFootprint());
    }

    @Test
    public void grouping()
    {
        Map<Long, Set<GenerationJdk>> generationByYears =
                GenerationJdk.ALL.stream()
                        .collect(Collectors.groupingBy(generation -> generation.years().count(),
                                Collectors.toSet()));
        var expected = new HashMap<>();
        expected.put(17L, Set.of(GenerationJdk.ALPHA, GenerationJdk.PROGRESSIVE));
        expected.put(16L, Set.of(GenerationJdk.X, GenerationJdk.MILLENNIAL, GenerationJdk.Z));
        expected.put(19L, Set.of(GenerationJdk.BOOMER));
        expected.put(18L, Set.of(GenerationJdk.SILENT, GenerationJdk.LOST));
        expected.put(23L, Set.of(GenerationJdk.MISSIONARY));
        expected.put(27L, Set.of(GenerationJdk.GREATEST));
        expected.put(1843L, Set.of(GenerationJdk.UNCLASSIFIED));
        assertEquals(expected, generationByYears);
        assertNull(generationByYears.get(30L));

        // java.util.HashMap (3656)
        System.out.println(GraphLayout.parseInstance(generationByYears).toFootprint());
    }

    @Test
    public void converting()
    {
        List<GenerationJdk> mutableList = new ArrayList<>(GenerationJdk.ALL);
        List<GenerationJdk> immutableList = GenerationJdk.ALL.stream().toList();

        // ArrayList (1736)
        System.out.println(GraphLayout.parseInstance(mutableList).toFootprint());
        // ImmutableCollections$ListN (1736)
        System.out.println(GraphLayout.parseInstance(immutableList).toFootprint());

        List<GenerationJdk> sortedMutableList =
                mutableList.stream().sorted(Comparator.comparing(gen -> gen.years().findFirst().getAsInt()))
                        .collect(Collectors.toList());
        var expected = Lists.mutable.with(GenerationJdk.values());
        assertEquals(expected, sortedMutableList);

        List<GenerationJdk> sortedImmutableList =
                immutableList.stream().sorted(Comparator.comparing(gen -> gen.years().findFirst().getAsInt()))
                        .toList();
        assertEquals(expected, sortedImmutableList);
    }
}
