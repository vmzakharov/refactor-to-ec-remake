package refactortoec.generation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.factory.Bags;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.set.ImmutableSetMultimap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.primitive.MutableIntSet;
import org.eclipse.collections.impl.factory.Multimaps;
import org.eclipse.collections.impl.list.Interval;
import org.eclipse.collections.impl.list.primitive.IntInterval;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.GraphLayout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenerationEcTest
{
    @Test
    public void contains()
    {
        assertTrue(GenerationEc.ALPHA.contains(2024));
        assertFalse(GenerationEc.ALPHA.contains(2000));
        assertTrue(GenerationEc.MILLENNIAL.contains(1985));
        assertFalse(GenerationEc.MILLENNIAL.contains(1960));
    }

    @Test
    public void find()
    {
        assertEquals(GenerationEc.MILLENNIAL, GenerationEc.find(1985));
        assertEquals(GenerationEc.ALPHA, GenerationEc.find(2016));
    }

    @Test
    public void anySatisfyWith()
    {
        assertTrue(GenerationEc.ALL.anySatisfyWith(GenerationEc::contains, 1995));
    }

    @Test
    public void counting()
    {
        assertEquals(1, GenerationEc.ALL.countWith(GenerationEc::contains, 1995));
        ImmutableBag<Integer> generationCountByYears =
                GenerationEc.ALL.countBy(generation -> generation.years().size());
        var expected = Bags.mutable.withOccurrences(17, 2)
                .withOccurrences(16, 3)
                .withOccurrences(19, 1)
                .withOccurrences(18, 2)
                .withOccurrences(23, 1)
                .withOccurrences(27, 1)
                .withOccurrences(1843, 1);
        assertEquals(expected, generationCountByYears);

        // ImmutableArrayBag (232)
        System.out.println(GraphLayout.parseInstance(generationCountByYears).toFootprint());
    }

    @Test
    public void testing()
    {
        assertTrue(GenerationEc.ALL.anySatisfyWith(GenerationEc::contains, 1995));
    }

    @Test
    public void finding()
    {
        assertEquals(GenerationEc.MILLENNIAL, GenerationEc.ALL.detectWith(GenerationEc::contains, 1995));
    }

    @Test
    public void filtering()
    {
        var expected = Sets.mutable.with(GenerationEc.X, GenerationEc.MILLENNIAL, GenerationEc.Z);
        ImmutableSet<GenerationEc> filtered = GenerationEc.ALL.selectWith(GenerationEc::yearsCountEquals, 16);
        assertEquals(expected, filtered);

        // ImmutableTripletonSet (536)
        System.out.println(GraphLayout.parseInstance(filtered).toFootprint());
    }

    @Test
    public void grouping()
    {
        ImmutableSetMultimap<Integer, GenerationEc> generationByYears =
                GenerationEc.ALL.groupBy(generation -> generation.years().size());
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

        // ImmutableSetMultimapImpl (2368)
        System.out.println(GraphLayout.parseInstance(generationByYears).toFootprint());
    }

    @Test
    public void converting()
    {
        MutableList<GenerationEc> mutableList = GenerationEc.ALL.toList();
        ImmutableList<GenerationEc> immutableList = GenerationEc.ALL.toImmutableList();

        // FastList (2016)
        System.out.println(GraphLayout.parseInstance(mutableList).toFootprint());
        // ImmutableArrayList (1992)
        System.out.println(GraphLayout.parseInstance(immutableList).toFootprint());

        MutableList<GenerationEc> sortedMutableList =
                mutableList.toSortedListBy(gen -> gen.years().getFirst());
        var expected = Lists.mutable.with(GenerationEc.values());
        assertEquals(expected, sortedMutableList);

        ImmutableList<GenerationEc> sortedImmutableList =
                immutableList.toImmutableSortedListBy(gen -> gen.years().getFirst());
        assertEquals(expected, sortedImmutableList);
    }
}
