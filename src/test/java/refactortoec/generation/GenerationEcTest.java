package refactortoec.generation;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.factory.Bags;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.set.ImmutableSetMultimap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Multimaps;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static refactortoec.generation.Generation.*;
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
                GENERATION_IMMUTABLE_SET.countBy(generation -> generation.yearsInterval().size());

        var expected = Bags.mutable.withOccurrences(17, 2)
                .withOccurrences(16, 3)
                .withOccurrences(19, 1)
                .withOccurrences(18, 2)
                .withOccurrences(23, 1)
                .withOccurrences(27, 1)
                .withOccurrences(1843, 1);
        assertEquals(expected, generationCountByYears);

        // ImmutableArrayBag (232)
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

        assertEquals(MILLENNIAL, find(1985));
        assertEquals(ALPHA, find(2016));
    }

    @Test
    public void filtering()
    {
        ImmutableSet<Generation> filtered =
                GENERATION_IMMUTABLE_SET.selectWith(Generation::yearsCountEqualsEc, 16);

        var expected = Sets.mutable.with(X, MILLENNIAL, Z);
        assertEquals(expected, filtered);

        // ImmutableTripletonSet (512)
        outputMemory(filtered);
    }

    @Test
    public void grouping()
    {
        ImmutableSetMultimap<Integer, Generation> generationByYears =
                GENERATION_IMMUTABLE_SET.groupBy(generation -> generation.yearsInterval().size());

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
        outputMemory(generationByYears);
    }

    @Test
    public void converting()
    {
        MutableList<Generation> mutableList =
                GENERATION_IMMUTABLE_SET.toList();
        ImmutableList<Generation> immutableList =
                GENERATION_IMMUTABLE_SET.toImmutableList();

        // FastList (1928)
        outputMemory(mutableList);
        // ImmutableArrayList (1904)
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

        var expected = Sets.immutable.with("Unclassified", "Greatest Generation", "Lost Generation", "Millennials",
                "Generation X", "Baby Boomers", "Generation Z", "Silent Generation", "Progressive Generation",
                "Generation Alpha", "Missionary Generation");
        assertEquals(expected, names);

        Set<String> mutableNames = names.toSet();
        assertEquals(expected, mutableNames);

        // ImmutableUnifiedSet (840)
        outputMemory(names);
        // UnifiedSet (824)
        outputMemory(mutableNames);
    }

    @Test
    public void grouping2()
    {
        RichIterable<RichIterable<Generation>> chunks =
                GenerationEc.chunk(3);
        String generationsAsString = chunks.makeString();

        String expected =
                "[UNCLASSIFIED, PROGRESSIVE, MISSIONARY], [LOST, GREATEST, SILENT], [BOOMER, X, MILLENNIAL], [Z, ALPHA]";
        assertEquals(expected, generationsAsString);
    }
}
