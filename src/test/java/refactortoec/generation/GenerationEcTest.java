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
import static refactortoec.generation.GenerationEc.*;

public class GenerationEcTest
{
    /**
     * Use JOL to output memory for an object
     */
    private void outputMemory(Object instance)
    {
        System.out.println(instance.getClass().getSimpleName() + ": " + GraphLayout.parseInstance(instance).totalSize());
        // System.out.println(GraphLayout.parseInstance(instance).toFootprint());
    }

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
        this.outputMemory(generationCountByYears);
    }

    @Test
    public void testing()
    {
        ImmutableSet<Generation> generations = GENERATION_IMMUTABLE_SET;
        assertTrue(generations.anySatisfyWith(Generation::contains, 1995));
        assertFalse(generations.allSatisfyWith(Generation::contains, 1995));
        assertFalse(generations.noneSatisfyWith(Generation::contains, 1995));

        assertTrue(Generation.ALPHA.contains(2024));
        assertFalse(Generation.ALPHA.contains(2000));
        assertTrue(Generation.MILLENNIAL.contains(1985));
        assertFalse(Generation.MILLENNIAL.contains(1960));
    }

    @Test
    public void finding()
    {
        Generation detected =
                GENERATION_IMMUTABLE_SET.detectWith(Generation::contains, 1995);

        assertEquals(Generation.MILLENNIAL, detected);

        assertEquals(Generation.MILLENNIAL, find(1985));
        assertEquals(Generation.ALPHA, find(2016));
    }

    @Test
    public void filtering()
    {
        ImmutableSet<Generation> filtered =
                GENERATION_IMMUTABLE_SET.selectWith(Generation::yearsCountEqualsEc, 16);

        var expected = Sets.mutable.with(Generation.X, Generation.MILLENNIAL, Generation.Z);
        assertEquals(expected, filtered);

        // ImmutableTripletonSet (512)
        this.outputMemory(filtered);
    }

    @Test
    public void grouping()
    {
        ImmutableSetMultimap<Integer, Generation> generationByYears =
                GENERATION_IMMUTABLE_SET.groupBy(generation -> generation.yearsInterval().size());

        var expected = Multimaps.immutable.set.empty()
                .newWithAll(17, Set.of(Generation.ALPHA, Generation.PROGRESSIVE))
                .newWithAll(16, Set.of(Generation.X, Generation.MILLENNIAL, Generation.Z))
                .newWithAll(19, Set.of(Generation.BOOMER))
                .newWithAll(18, Set.of(Generation.SILENT, Generation.LOST))
                .newWithAll(23, Set.of(Generation.MISSIONARY))
                .newWithAll(27, Set.of(Generation.GREATEST))
                .newWithAll(1843, Set.of(Generation.UNCLASSIFIED));
        assertEquals(expected, generationByYears);
        assertTrue(generationByYears.get(30).isEmpty());

        // ImmutableSetMultimapImpl (2280)
        this.outputMemory(generationByYears);
    }

    @Test
    public void converting()
    {
        MutableList<Generation> mutableList =
                GENERATION_IMMUTABLE_SET.toList();
        ImmutableList<Generation> immutableList =
                GENERATION_IMMUTABLE_SET.toImmutableList();

        // FastList (1928)
        this.outputMemory(mutableList);
        // ImmutableArrayList (1904)
        this.outputMemory(immutableList);

        MutableList<Generation> sortedMutableList =
                mutableList.toSortedListBy(gen -> gen.yearsInterval().getFirst());

        var expected = Lists.mutable.with(Generation.values());
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
        this.outputMemory(names);
        // UnifiedSet (824)
        this.outputMemory(mutableNames);
    }
}
