package refactortoec.generation;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.GraphLayout;

public class GenerationMemoryTest
{
    @Test
    public void toFootprintImmutable()
    {
        // ImmutableUnifiedSet (2,168)
        System.out.println(GraphLayout.parseInstance(GenerationEc.ALL).toFootprint());
        // ImmutableCollections$SetN (1,776)
        System.out.println(GraphLayout.parseInstance(GenerationJdk.ALL).toFootprint());
        // ImmutableIntObjectHashMap (34,792)
        System.out.println(GraphLayout.parseInstance(GenerationEc.BY_YEAR).toFootprint());
        // ImmutableCollections$MapN (66,656)
        System.out.println(GraphLayout.parseInstance(GenerationJdk.BY_YEAR).toFootprint());
    }

    @Test
    public void toFootprintMutable()
    {
        // UnifiedSet (2,152)
        System.out.println(GraphLayout.parseInstance(GenerationEc.ALL.toSet()).toFootprint());
        // HashSet (2,160)
        System.out.println(GraphLayout.parseInstance(new HashSet<>(GenerationJdk.ALL)).toFootprint());
        // IntObjectHashMap (34,776)
        System.out.println(GraphLayout.parseInstance(IntObjectMaps.mutable.withAll(GenerationEc.BY_YEAR)).toFootprint());
        // HashMap (115,536)
        System.out.println(GraphLayout.parseInstance(new HashMap<>(GenerationJdk.BY_YEAR)).toFootprint());
    }
}
