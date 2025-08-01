package refactortoec.generation;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum GenerationJdk
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

    public static final Set<GenerationJdk> ALL =
            Set.of(GenerationJdk.values());

    public static final Map<Integer, GenerationJdk> BY_YEAR =
            GenerationJdk.groupEachByYear();

    private static Map<Integer, GenerationJdk> groupEachByYear()
    {
        record YearToGeneration(int year, GenerationJdk generation) {}
        return Map.copyOf(GenerationJdk.ALL.stream()
                .flatMap(gen ->
                        gen.yearsStream().mapToObj(year -> new YearToGeneration(year, gen)))
                .collect(Collectors.toMap(YearToGeneration::year, YearToGeneration::generation)));
    }

    public static GenerationJdk find(int year)
    {
        return BY_YEAR.getOrDefault(year, UNCLASSIFIED);
    }

    private final String name;
    private final YearRange years;

    GenerationJdk(String name, int from, int to)
    {
        this.name = name;
        this.years = new YearRange(from, to);
    }

    public IntStream yearsStream()
    {
        return this.years.stream();
    }

    public String getName()
    {
        return this.name;
    }

    public boolean yearsCountEquals(int years)
    {
        return this.yearsStream().count() == years;
    }

    public boolean contains(int year)
    {
        return this.years.contains(year);
        // return this.yearsStream().anyMatch(i -> i == year);
    }
}
