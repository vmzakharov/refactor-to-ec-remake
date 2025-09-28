package refactortoec.generation;

import java.util.stream.IntStream;

import org.eclipse.collections.impl.list.primitive.IntInterval;

public enum Generation
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

    private final String name;
    private final YearRange years;

    Generation(String name, int from, int to)
    {
        this.name = name;
        this.years = new YearRange(from, to);
    }

    public int numberOfYears()
    {
        return this.years.count();
    }

    public IntInterval yearsInterval()
    {
        return this.years.interval();
    }

    public IntStream yearsStream()
    {
        return this.years.stream();
    }

    public boolean yearsCountEqualsEc(int years)
    {
        return this.yearsInterval().size() == years;
    }

    public boolean yearsCountEqualsJdk(int years)
    {
        return this.yearsStream().count() == years;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean contains(int year)
    {
        return this.years.contains(year);
    }
}
