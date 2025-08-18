package refactortoec.generation;

import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.map.primitive.ImmutableIntObjectMap;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;

public class GenerationEc
{
    public static final ImmutableSet<Generation> GENERATION_IMMUTABLE_SET =
            Sets.immutable.with(Generation.values());

    public static final ImmutableIntObjectMap<Generation> BY_YEAR =
            GenerationEc.groupEachByYear();

    private static ImmutableIntObjectMap<Generation> groupEachByYear()
    {
        MutableIntObjectMap<Generation> map = IntObjectMaps.mutable.empty();
        GENERATION_IMMUTABLE_SET.forEach(generation -> generation.yearsInterval()
                .forEach(year -> map.put(year, generation)));
        return map.toImmutable();
    }

    public static Generation find(int year)
    {
        return BY_YEAR.getIfAbsent(year, () -> Generation.UNCLASSIFIED);
    }
}
