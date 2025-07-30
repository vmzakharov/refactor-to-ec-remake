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

    private static final Map<Integer, GenerationJdk> BY_YEAR =
            GenerationJdk.groupEachByYear();

    private static Map<Integer, GenerationJdk> groupEachByYear()
    {
        record YearToGeneration(int year, GenerationJdk generation) {}
        return Map.copyOf(GenerationJdk.ALL.stream()
                .flatMap(gen ->
                        IntStream.rangeClosed(gen.from, gen.to)
                                .mapToObj(year -> new YearToGeneration(year, gen)))
                .collect(Collectors.toMap(YearToGeneration::year, YearToGeneration::generation)));
    }

    public static GenerationJdk find(int year)
    {
        return BY_YEAR.getOrDefault(year, UNCLASSIFIED);
    }

    private final String name;
    private final int from;
    private final int to;

    GenerationJdk(String name, int from, int to)
    {
        this.name = name;
        this.from = from;
        this.to = to;
    }

    public IntStream years()
    {
        return IntStream.rangeClosed(this.from, this.to);
    }

    public String getName()
    {
        return this.name;
    }

    public boolean yearsCountEquals(int years)
    {
        return years == (this.to - this.from + 1);
        // return this.years().count() == years;
    }

    public boolean contains(int year)
    {
        return this.from <= year && year <= this.to;
        // return this.years().anyMatch(i -> i == year);
    }
}
