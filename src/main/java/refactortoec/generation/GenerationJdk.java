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
                        gen.years().mapToObj(year -> new YearToGeneration(year, gen)))
                .collect(Collectors.toMap(YearToGeneration::year, YearToGeneration::generation)));
    }

    public static GenerationJdk find(int year)
    {
        return BY_YEAR.getOrDefault(year, UNCLASSIFIED);
    }

    public record Years(int from, int to)
    {
        public int count()
        {
            return this.to - this.from + 1;
        }

        public boolean contains(int year)
        {
            return this.from <= year && year <= this.to;
        }

        public IntStream stream()
        {
            return IntStream.rangeClosed(this.from, this.to);
        }
    };

    private final String name;
    private final Years years;

    GenerationJdk(String name, int from, int to)
    {
        this.name = name;
        this.years = new Years(from, to);
    }

    public IntStream years()
    {
        return this.years.stream();
    }

    public String getName()
    {
        return this.name;
    }

    public boolean yearsCountEquals(int years)
    {
        return this.years.count() == years;
    }

    public boolean contains(int year)
    {
        return this.years.contains(year);
        // return this.years().anyMatch(i -> i == year);
    }
}
