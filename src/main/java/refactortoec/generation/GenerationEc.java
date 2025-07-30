package refactortoec.generation;

import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.map.primitive.ImmutableIntObjectMap;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;
import org.eclipse.collections.impl.list.primitive.IntInterval;

public enum GenerationEc
{
    UNCLASSIFIED("Unclassified", 0, 1842),
    PROGRESSIVE("Progressive Generation", 1843, 1859),
    MISSIONARY("Missionary Generation", 1860, 1882),
    LOST("Lost Generation", 1883, 1900),
    GREATEST("Greatest Generation", 1901, 1927),
    SILENT("Silent Generation", 1928, 1945),
    BOOMER("Baby Boomers", 1946, 1964),
    X("Generation X", 1965, 1980),
    MILLENNIAL("Millennials", 1981, 1996),
    Z("Generation Z", 1997, 2012),
    ALPHA("Generation Alpha", 2013, 2029);

    public static final ImmutableSet<GenerationEc> ALL =
            Sets.immutable.with(GenerationEc.values());

    private static final ImmutableIntObjectMap<GenerationEc> BY_YEAR =
            GenerationEc.groupEachByYear();

    private static ImmutableIntObjectMap<GenerationEc> groupEachByYear()
    {
        MutableIntObjectMap<GenerationEc> map = IntObjectMaps.mutable.empty();
        return GenerationEc.ALL.injectInto(
                        map, (map1, generation) ->
                                generation.years().injectInto(
                                        map1, (map2, year) ->
                                                map2.withKeyValue(year, generation)))
                .toImmutable();
    }

    public static GenerationEc find(int year)
    {
        return BY_YEAR.getIfAbsent(year, () -> UNCLASSIFIED);
    }

    private final String name;
    private final IntInterval years;

    GenerationEc(String name, int from, int to)
    {
        this.name = name;
        this.years = IntInterval.fromTo(from, to);
    }

    public IntInterval years()
    {
        return this.years;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean yearsCountEquals(int years)
    {
        return this.years.size() == years;
    }

    public boolean contains(int year)
    {
        return this.years().contains(year);
    }
}
