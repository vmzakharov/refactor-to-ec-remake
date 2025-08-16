package refactortoec.generation;

import java.util.HashMap;
import java.util.stream.Collectors;

import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.GraphLayout;

public class GenerationMemoryTest
{
    /**
     * Use JOL to output memory for an object
     */
    private void outputMemory(Object instance)
    {
        // System.out.println(instance.getClass().getSimpleName() + ": " + GraphLayout.parseInstance(instance).totalSize());
        System.out.println(GraphLayout.parseInstance(instance).toFootprint());
    }

    @Test
    public void toFootprintImmutableAll()
    {
        // ImmutableUnifiedSet (2,016)
        this.outputMemory(GenerationEc.ALL);
        // ImmutableCollections$SetN (1,952)
        this.outputMemory(GenerationJdk.ALL);
    }

    @Test
    public void toFootprintImmutableByYear()
    {
        // ImmutableIntObjectHashMap (34,704)
        this.outputMemory(GenerationEc.BY_YEAR);
        // ImmutableCollections$MapN (66,832)
        this.outputMemory(GenerationJdk.BY_YEAR);
    }

    @Test
    public void toFootprintMutableAll()
    {
        // UnifiedSet (2,000)
        this.outputMemory(GenerationEc.ALL.toSet());
        // HashSet (2,336)
        this.outputMemory(GenerationJdk.ALL.stream().collect(Collectors.toSet()));
    }

    @Test
    public void toFootprintMutableByYear()
    {
        // IntObjectHashMap (34,688)
        this.outputMemory(IntObjectMaps.mutable.withAll(GenerationEc.BY_YEAR));
        // HashMap (115,712)
        this.outputMemory(new HashMap<>(GenerationJdk.BY_YEAR));
    }
}
