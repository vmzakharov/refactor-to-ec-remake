package refactortoec.generation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.collections.api.map.primitive.ImmutableIntObjectMap;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;
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
        System.out.println(instance.getClass().getSimpleName() + ": " + GraphLayout.parseInstance(instance).totalSize());
        // System.out.println(GraphLayout.parseInstance(instance).toFootprint());
    }

    @Test
    public void toFootprintImmutableSetAll()
    {
        // ImmutableUnifiedSet (2,048)
        ImmutableSet<Generation> generationImmutableSet = GenerationEc.GENERATION_IMMUTABLE_SET;
        this.outputMemory(generationImmutableSet);
        // ImmutableCollections$SetN (1,952)
        Set<Generation> generationSet = GenerationJdk.GENERATION_SET;
        this.outputMemory(generationSet);
    }

    @Test
    public void toFootprintImmutableByYear()
    {
        // ImmutableIntObjectHashMap (34,704)
        ImmutableIntObjectMap<Generation> byYearEc = GenerationEc.BY_YEAR;
        this.outputMemory(byYearEc);
        // ImmutableCollections$MapN (66,832)
        Map<Integer, Generation> byYearJdk = GenerationJdk.BY_YEAR;
        this.outputMemory(byYearJdk);
    }

    @Test
    public void toFootprintMutableSetAll()
    {
        // UnifiedSet (2,032)
        MutableSet<Generation> ecSet = GenerationEc.GENERATION_IMMUTABLE_SET.toSet();
        this.outputMemory(ecSet);
        // HashSet (2,336)
        Set<Generation> jdkSet = GenerationJdk.GENERATION_SET.stream().collect(Collectors.toSet());
        this.outputMemory(jdkSet);
    }

    @Test
    public void toFootprintMutableByYear()
    {
        // IntObjectHashMap (34,688)
        MutableIntObjectMap<Generation> byYearEc = IntObjectMaps.mutable.withAll(GenerationEc.BY_YEAR);
        this.outputMemory(byYearEc);
        // HashMap (115,712)
        HashMap<Integer, Generation> byYearJdk = new HashMap<>(GenerationJdk.BY_YEAR);
        this.outputMemory(byYearJdk);
    }
}
