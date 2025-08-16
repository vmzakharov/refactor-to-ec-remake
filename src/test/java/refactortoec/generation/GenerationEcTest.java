package refactortoec.generation;

import java.util.Set;

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
import org.openjdk.jol.info.GraphLayout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenerationEcTest
{
    /**
     * Use JOL to output memory for an object
     */
    private void outputMemory(Object instance)
    {
        System.out.println(GraphLayout.parseInstance(instance).toFootprint());
    }

    @Test
    public void counting()
    {
        int count = GenerationEc.ALL.countWith(GenerationEc::contains, 1995);

        assertEquals(1, count);

        ImmutableBag<Integer> generationCountByYears =
                GenerationEc.ALL.countBy(generation -> generation.yearsInterval().size());

        var expected = Bags.mutable.withOccurrences(17, 2)
                .withOccurrences(16, 3)
                .withOccurrences(19, 1)
                .withOccurrences(18, 2)
                .withOccurrences(23, 1)
                .withOccurrences(27, 1)
                .withOccurrences(1843, 1);
        assertEquals(expected, generationCountByYears);

        // ImmutableArrayBag (232)
        this.outputMemory(generationCountByYears);
    }

    @Test
    public void testing()
    {
        ImmutableSet<GenerationEc> generations = GenerationEc.ALL;
        assertTrue(generations.anySatisfyWith(GenerationEc::contains, 1995));
        assertFalse(generations.allSatisfyWith(GenerationEc::contains, 1995));
        assertFalse(generations.noneSatisfyWith(GenerationEc::contains, 1995));

        assertTrue(GenerationEc.ALPHA.contains(2024));
        assertFalse(GenerationEc.ALPHA.contains(2000));
        assertTrue(GenerationEc.MILLENNIAL.contains(1985));
        assertFalse(GenerationEc.MILLENNIAL.contains(1960));
    }

    @Test
    public void finding()
    {
        GenerationEc detected = GenerationEc.ALL.detectWith(GenerationEc::contains, 1995);

        assertEquals(GenerationEc.MILLENNIAL, detected);

        assertEquals(GenerationEc.MILLENNIAL, GenerationEc.find(1985));
        assertEquals(GenerationEc.ALPHA, GenerationEc.find(2016));
    }

    @Test
    public void filtering()
    {
        ImmutableSet<GenerationEc> filtered = GenerationEc.ALL.selectWith(GenerationEc::yearsCountEquals, 16);

        var expected = Sets.mutable.with(GenerationEc.X, GenerationEc.MILLENNIAL, GenerationEc.Z);
        assertEquals(expected, filtered);

        // ImmutableTripletonSet (512)
        this.outputMemory(filtered);
    }

    @Test
    public void grouping()
    {
        ImmutableSetMultimap<Integer, GenerationEc> generationByYears =
                GenerationEc.ALL.groupBy(generation -> generation.yearsInterval().size());

        var expected = Multimaps.immutable.set.empty()
                .newWithAll(17, Set.of(GenerationEc.ALPHA, GenerationEc.PROGRESSIVE))
                .newWithAll(16, Set.of(GenerationEc.X, GenerationEc.MILLENNIAL, GenerationEc.Z))
                .newWithAll(19, Set.of(GenerationEc.BOOMER))
                .newWithAll(18, Set.of(GenerationEc.SILENT, GenerationEc.LOST))
                .newWithAll(23, Set.of(GenerationEc.MISSIONARY))
                .newWithAll(27, Set.of(GenerationEc.GREATEST))
                .newWithAll(1843, Set.of(GenerationEc.UNCLASSIFIED));
        assertEquals(expected, generationByYears);
        assertTrue(generationByYears.get(30).isEmpty());

        // ImmutableSetMultimapImpl (2280)
        this.outputMemory(generationByYears);
    }

    @Test
    public void converting()
    {
        MutableList<GenerationEc> mutableList = GenerationEc.ALL.toList();
        ImmutableList<GenerationEc> immutableList = GenerationEc.ALL.toImmutableList();

        // FastList (1928)
        this.outputMemory(mutableList);
        // ImmutableArrayList (1904)
        this.outputMemory(immutableList);

        MutableList<GenerationEc> sortedMutableList =
                mutableList.toSortedListBy(gen -> gen.yearsInterval().getFirst());

        var expected = Lists.mutable.with(GenerationEc.values());
        assertEquals(expected, sortedMutableList);

        ImmutableList<GenerationEc> sortedImmutableList =
                immutableList.toImmutableSortedListBy(gen -> gen.yearsInterval().getFirst());
        assertEquals(expected, sortedImmutableList);
    }

    @Test
    public void transforming()
    {
        ImmutableSet<String> names = GenerationEc.ALL.collect(GenerationEc::getName);

        var expected = Sets.immutable.with("Unclassified", "Greatest Generation", "Lost Generation", "Millennials",
                "Generation X", "Baby Boomers", "Generation Z", "Silent Generation", "Progressive Generation",
                "Generation Alpha", "Missionary Generation");
        assertEquals(expected, names);

        // ImmutableUnifiedSet (840)
        this.outputMemory(names);
        Set<String> mutableNames = names.toSet();
        assertEquals(expected, mutableNames);
        // UnifiedSet (824)
        this.outputMemory(mutableNames);
    }
}
