package refactortoec.generation;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.factory.Bags;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.set.ImmutableSetMultimap;
import org.eclipse.collections.api.partition.set.PartitionImmutableSet;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Multimaps;
import org.eclipse.collections.impl.list.fixed.ArrayAdapter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import static refactortoec.generation.GenerationEc.GENERATION_IMMUTABLE_SET;
import static refactortoec.generation.GenerationEc.find;
import static refactortoec.util.MemoryMeter.outputMemory;

public class GenerationEcTest
{
    @Test
    public void counting()
    {
        int count =
                GENERATION_IMMUTABLE_SET.countWith(Generation::contains, 1995);

        assertEquals(1, count);

        ImmutableBag<Integer> generationCountByYears =
                GENERATION_IMMUTABLE_SET.countBy(Generation::numberOfYears);

        var expected = Bags.mutable.withOccurrences(17, 2)
                .withOccurrences(16, 3)
                .withOccurrences(19, 1)
                .withOccurrences(18, 2)
                .withOccurrences(23, 1)
                .withOccurrences(27, 1)
                .withOccurrences(1843, 1);
        assertEquals(expected, generationCountByYears);

        // ImmutableArrayBag (232)
        // Java 25 COH (208)
        outputMemory(generationCountByYears);
    }

    @Test
    public void testing()
    {
        ImmutableSet<Generation> generations = GENERATION_IMMUTABLE_SET;
        assertTrue(generations.anySatisfyWith(Generation::contains, 1995));
        assertFalse(generations.allSatisfyWith(Generation::contains, 1995));
        assertFalse(generations.noneSatisfyWith(Generation::contains, 1995));

        assertTrue(ALPHA.contains(2024));
        assertFalse(ALPHA.contains(2000));
        assertTrue(MILLENNIAL.contains(1985));
        assertFalse(MILLENNIAL.contains(1960));
    }

    @Test
    public void finding()
    {
        Generation detected =
                GENERATION_IMMUTABLE_SET.detectWith(Generation::contains, 1995);

        assertEquals(MILLENNIAL, detected);

        Generation notFound =
                GENERATION_IMMUTABLE_SET.detectWithIfNone(Generation::contains, 1795, () -> UNCLASSIFIED);

        assertEquals(UNCLASSIFIED, notFound);

        MutableList<Generation> generationsNotUnclassified =
                ArrayAdapter.adapt(values())
                        .rejectWith(Generation::equals, UNCLASSIFIED);

        Generation maxByYears = generationsNotUnclassified.maxBy(Generation::numberOfYears);
        assertEquals(GREATEST, maxByYears);

        Generation minByYears = generationsNotUnclassified.minBy(Generation::numberOfYears);
        assertEquals(X, minByYears);
    }

    @Test
    public void filtering()
    {
        ImmutableSet<Generation> filteredSelected =
                GENERATION_IMMUTABLE_SET.selectWith(Generation::yearsCountEqualsEc, 16);

        var expectedSelected = Sets.mutable.with(X, MILLENNIAL, Z);
        assertEquals(expectedSelected, filteredSelected);

        ImmutableSet<Generation> filteredRejected =
                GENERATION_IMMUTABLE_SET.rejectWith(Generation::yearsCountEqualsEc, 16);

        var expectedRejected =
                Sets.mutable.with(ALPHA, UNCLASSIFIED, BOOMER, GREATEST, LOST, MISSIONARY, PROGRESSIVE, SILENT);
        assertEquals(expectedRejected, filteredRejected);

        PartitionImmutableSet<Generation> partition =
                GENERATION_IMMUTABLE_SET.partitionWith(Generation::yearsCountEqualsEc, 16);

        assertEquals(expectedSelected, partition.getSelected());
        assertEquals(expectedRejected, partition.getRejected());

        // ImmutableTripletonSet (512)
        // Java 25 COH (440)
        outputMemory(filteredSelected);
    }

    @Test
    public void grouping()
    {
        ImmutableSetMultimap<Integer, Generation> generationByYears =
                GENERATION_IMMUTABLE_SET.groupBy(Generation::numberOfYears);

        var expected = Multimaps.immutable.set.empty()
                .newWithAll(17, Set.of(ALPHA, PROGRESSIVE))
                .newWithAll(16, Set.of(X, MILLENNIAL, Z))
                .newWithAll(19, Set.of(BOOMER))
                .newWithAll(18, Set.of(SILENT, LOST))
                .newWithAll(23, Set.of(MISSIONARY))
                .newWithAll(27, Set.of(GREATEST))
                .newWithAll(1843, Set.of(UNCLASSIFIED));

        assertEquals(expected, generationByYears);
        assertTrue(generationByYears.get(30).isEmpty());

        // ImmutableSetMultimapImpl (2280)
        // Java 25 COH (2056)
        outputMemory(generationByYears);
    }

    @Test
    public void converting()
    {
        MutableList<Generation> mutableList =
                GENERATION_IMMUTABLE_SET.toList();
        ImmutableList<Generation> immutableList =
                GENERATION_IMMUTABLE_SET.toImmutableList();

        // FastList (1,928)
        // Java 25 COH (1,720)
        outputMemory(mutableList);
        // ImmutableArrayList (1,904)
        // Java 25 COH (1,696)
        outputMemory(immutableList);

        MutableList<Generation> sortedMutableList =
                mutableList.toSortedListBy(gen -> gen.yearsInterval().getFirst());

        var expected = Lists.mutable.with(values());
        assertEquals(expected, sortedMutableList);

        ImmutableList<Generation> sortedImmutableList =
                immutableList.toImmutableSortedListBy(gen -> gen.yearsInterval().getFirst());
        assertEquals(expected, sortedImmutableList);
    }

    @Test
    public void transforming()
    {
        ImmutableSet<String> names =
                GENERATION_IMMUTABLE_SET.collect(Generation::getName);

        var expected = Sets.immutable.with(
                "Unclassified", "Greatest Generation", "Lost Generation", "Millennials",
                "Generation X", "Baby Boomers", "Generation Z", "Silent Generation", "Progressive Generation",
                "Generation Alpha", "Missionary Generation");
        assertEquals(expected, names);

        Set<String> mutableNames = names.toSet();
        assertEquals(expected, mutableNames);

        // ImmutableUnifiedSet (840)
        // Java 25 COH (760)
        outputMemory(names);
        // UnifiedSet (824)
        // Java 25 COH (744)
        outputMemory(mutableNames);
    }

    @Test
    public void chunking()
    {
        RichIterable<RichIterable<Generation>> chunkGenerations =
                GenerationEc.chunkGenerations(3);
        String generationsAsString = chunkGenerations.makeString();

        String expectedGenerations =
                "[UNCLASSIFIED, PROGRESSIVE, MISSIONARY], [LOST, GREATEST, SILENT], [BOOMER, X, MILLENNIAL], [Z, ALPHA]";
        assertEquals(expectedGenerations, generationsAsString);

        String yearsAsString = MILLENNIAL.yearsInterval()
                .chunk(4)
                .makeString();

        String expectedYears =
                "[1981, 1982, 1983, 1984], [1985, 1986, 1987, 1988], [1989, 1990, 1991, 1992], [1993, 1994, 1995, 1996]";
        assertEquals(expectedYears, yearsAsString);
    }

    @Test
    public void folding()
    {
        Integer maxYears = GenerationEc.fold(
                Integer.MIN_VALUE,
                (Integer value, Generation generation) ->
                        Math.max(value, generation.numberOfYears()));

        Integer minYears = GenerationEc.fold(
                Integer.MAX_VALUE,
                (Integer value, Generation generation) ->
                        Math.min(value, generation.numberOfYears()));

        Integer sumYears = GenerationEc.fold(
                Integer.valueOf(0),
                (Integer value, Generation generation) ->
                        Integer.sum(value, generation.numberOfYears()));

        assertEquals(1843, maxYears);
        assertEquals(16, minYears);
        assertEquals(2030, sumYears);
    }
}
