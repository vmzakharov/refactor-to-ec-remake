package refactortoec.generation;

import java.util.HashMap;
import java.util.stream.Collectors;

import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.GraphLayout;

public class GenerationMemoryTest
{
    @Test
    public void toFootprintImmutableAll()
    {
        // ImmutableUnifiedSet (2,016)
        System.out.println(GraphLayout.parseInstance(GenerationEc.ALL).toFootprint());
        // ImmutableCollections$SetN (1,952)
        System.out.println(GraphLayout.parseInstance(GenerationJdk.ALL).toFootprint());
    }

    @Test
    public void toFootprintImmutableByYear()
    {
        // ImmutableIntObjectHashMap (34,704)
        System.out.println(GraphLayout.parseInstance(GenerationEc.BY_YEAR).toFootprint());
        // ImmutableCollections$MapN (66,832)
        System.out.println(GraphLayout.parseInstance(GenerationJdk.BY_YEAR).toFootprint());
    }

    @Test
    public void toFootprintMutableAll()
    {
        // UnifiedSet (2,000)
        System.out.println(GraphLayout.parseInstance(GenerationEc.ALL.toSet()).toFootprint());
        // HashSet (2,336)
        System.out.println(GraphLayout.parseInstance(GenerationJdk.ALL.stream().collect(Collectors.toSet())).toFootprint());
    }

    @Test
    public void toFootprintMutableByYear()
    {
        // IntObjectHashMap (34,688)
        System.out.println(GraphLayout.parseInstance(IntObjectMaps.mutable.withAll(GenerationEc.BY_YEAR)).toFootprint());
        // HashMap (115,712)
        System.out.println(GraphLayout.parseInstance(new HashMap<>(GenerationJdk.BY_YEAR)).toFootprint());
    }
}
