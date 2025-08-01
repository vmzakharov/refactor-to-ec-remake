package refactortoec.generation;

import java.util.stream.IntStream;

import org.eclipse.collections.impl.list.primitive.IntInterval;

public record YearRange(int from, int to)
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

    public IntInterval interval()
    {
        return IntInterval.fromTo(this.from, this.to);
    }
}
