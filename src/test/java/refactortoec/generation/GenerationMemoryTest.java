package refactortoec.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.map.primitive.ImmutableIntObjectMap;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

public class GenerationMemoryTest
{
    /**
     * JDK 25
     * java.lang.Object@62230c58d footprint:
     * COUNT       AVG       SUM   DESCRIPTION
     * 1        16        16   java.lang.Object
     * 1                  16   (total)
     *
     * JDK 25 COH
     * java.lang.Object@2e55dd0cd footprint:
     * COUNT       AVG       SUM   DESCRIPTION
     * 1         8         8   java.lang.Object
     * 1                   8   (total)
     */
    @Test
    public void object()
    {
        // this.outputMemory(new Object());
        this.outputMemory("Hello World!");
    }

    /**
     * JDK
     * org.eclipse.collections.impl.set.immutable.ImmutableUnifiedSet@2cb4893bd footprint:
     * COUNT       AVG       SUM   DESCRIPTION
     * 22        30       680   [B
     * 1        80        80   [Ljava.lang.Object;
     * 22        24       528   java.lang.String
     * 1        16        16   org.eclipse.collections.impl.set.immutable.ImmutableUnifiedSet
     * 1        32        32   org.eclipse.collections.impl.set.mutable.UnifiedSet
     * 2        32        64   org.eclipse.collections.impl.set.mutable.UnifiedSet$ChainedBucket
     * 11        32       352   refactortoec.generation.Generation
     * 11        24       264   refactortoec.generation.YearRange
     * 71                2016   (total)
     *
     * JDK 25 COH
     * org.eclipse.collections.impl.set.immutable.ImmutableUnifiedSet@413d1bafd footprint:
     * COUNT       AVG       SUM   DESCRIPTION
     * 22        25       568   [B
     * 1        80        80   [Ljava.lang.Object;
     * 22        24       528   java.lang.String
     * 1        16        16   org.eclipse.collections.impl.set.immutable.ImmutableUnifiedSet
     * 1        24        24   org.eclipse.collections.impl.set.mutable.UnifiedSet
     * 2        24        48   org.eclipse.collections.impl.set.mutable.UnifiedSet$ChainedBucket
     * 11        32       352   refactortoec.generation.Generation
     * 11        16       176   refactortoec.generation.YearRange
     * 71                1792   (total)
     */
    @Test
    public void immutableSetGenerationEc()
    {
        // ImmutableUnifiedSet
        // Java 25 - 1,984
        ImmutableSet<Generation> generationImmutableSet = GenerationEc.GENERATION_IMMUTABLE_SET;
        this.outputMemory(generationImmutableSet);
    }

    /**
     * JDK 25
     * java.util.ImmutableCollections$SetN@a3d8174d footprint:
     * COUNT       AVG       SUM   DESCRIPTION
     * 22        30       680   [B
     * 1       104       104   [Ljava.lang.Object;
     * 22        24       528   java.lang.String
     * 1        24        24   java.util.ImmutableCollections$SetN
     * 11        32       352   refactortoec.generation.Generation
     * 11        24       264   refactortoec.generation.YearRange
     * 68                1952   (total)
     *
     * JDK 25 COH
     * java.util.ImmutableCollections$SetN@732c2a62d footprint:
     * COUNT       AVG       SUM   DESCRIPTION
     * 22        25       568   [B
     * 1       104       104   [Ljava.lang.Object;
     * 22        24       528   java.lang.String
     * 1        16        16   java.util.ImmutableCollections$SetN
     * 11        32       352   refactortoec.generation.Generation
     * 11        16       176   refactortoec.generation.YearRange
     * 68                1744   (total)
     */
    @Test
    public void immutableSetGenerationJdk()
    {
        // ImmutableCollections$SetN
        // Java 25 - 1,952
        Set<Generation> generationSet = GenerationJdk.GENERATION_SET;
        this.outputMemory(generationSet);
    }

    @Test
    public void generationsByYearImmutableEc()
    {
        // ImmutableIntObjectHashMap
        // Java 25 - 34,704
        ImmutableIntObjectMap<Generation> byYearEc = GenerationEc.BY_YEAR;
        this.outputMemory(byYearEc);
    }

    @Test
    public void generationsByYearImmutableJdk()
    {
        // ImmutableCollections$MapN
        // Java 25 - 66,832
        Map<Integer, Generation> byYearJdk = GenerationJdk.BY_YEAR;
        this.outputMemory(byYearJdk);
    }

    @Test
    public void mutableSetGenerationEc()
    {
        // UnifiedSet
        // Java 25 - 1,968
        MutableSet<Generation> ecSet = GenerationEc.GENERATION_IMMUTABLE_SET.toSet();
        this.outputMemory(ecSet);
    }

    @Test
    public void mutableSetGenerationJdk()
    {
        // UnifiedSet
        // Java 25 - 2,336
        Set<Generation> jdkSet = GenerationJdk.GENERATION_SET.stream().collect(Collectors.toSet());
        this.outputMemory(jdkSet);
    }

    @Test
    public void generationBysYearMutableEc()
    {
        // IntObjectHashMap
        // Java 25 - 34,688
        MutableIntObjectMap<Generation> byYearEc = IntObjectMaps.mutable.withAll(GenerationEc.BY_YEAR);
        this.outputMemory(byYearEc);
    }

    @Test
    public void generationBysYearMutableJdk()
    {
        // HashMap
        // Java 25 - 115,712
        HashMap<Integer, Generation> byYearJdk = new HashMap<>(GenerationJdk.BY_YEAR);
        this.outputMemory(byYearJdk);
    }

    @Test
    public void presizedArrayListVsFastList()
    {
        this.outputMemory(new ArrayList());
        this.outputMemory(new FastList());
    }

    private void outputMemory(Object instance)
    {
        System.out.println(ClassLayout.parseInstance(instance).toPrintable());
        System.out.println(GraphLayout.parseInstance(instance).toFootprint());
    }

    @Test
    public void immutableLists()
    {
        this.outputMemory(List.of());
        this.outputMemory(Lists.immutable.of());
        this.outputMemory(List.of(""));
        this.outputMemory(Lists.immutable.of(""));
        this.outputMemory(List.of("", ""));
        this.outputMemory(Lists.immutable.of("", ""));
        this.outputMemory(List.of("", "", ""));
        this.outputMemory(Lists.immutable.of("", "", ""));
        this.outputMemory(List.of("", "", "", ""));
        this.outputMemory(Lists.immutable.of("", "", "", ""));
        this.outputMemory(List.of("", "", "", "", ""));
        this.outputMemory(Lists.immutable.of("", "", "", "", ""));
        this.outputMemory(List.of("", "", "", "", "", ""));
        this.outputMemory(Lists.immutable.of("", "", "", "", "", ""));
        this.outputMemory(List.of("", "", "", "", "", "", ""));
        this.outputMemory(Lists.immutable.of("", "", "", "", "", "", ""));
        this.outputMemory(List.of("", "", "", "", "", "", "", ""));
        this.outputMemory(Lists.immutable.of("", "", "", "", "", "", "", ""));
        this.outputMemory(List.of("", "", "", "", "", "", "", "", ""));
        this.outputMemory(Lists.immutable.of("", "", "", "", "", "", "", "", ""));
        this.outputMemory(List.of("", "", "", "", "", "", "", "", "", ""));
        this.outputMemory(Lists.immutable.of("", "", "", "", "", "", "", "", "", ""));
        this.outputMemory(List.of("", "", "", "", "", "", "", "", "", "", ""));
        this.outputMemory(Lists.immutable.of("", "", "", "", "", "", "", "", "", "", ""));
    }
}

