package refactortoec.generation;

import java.util.ArrayList;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.api.LazyLongIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.LongList;
import org.eclipse.collections.impl.list.Interval;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.primitive.IntInterval;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SmallStreamLazyIterableTest
{
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 50, 100})
    public void arrayListStreamFilterCount(int size)
    {
        ArrayList<Integer> arrayList = new ArrayList<>(Interval.oneTo(size));
        Stream<Integer> stream = arrayList.stream()
                .filter(i -> i % 2 == 0);
        long count = stream.count();
        Assertions.assertEquals(IntInterval.oneTo(size).count(i -> i % 2 == 0), count);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 50, 100})
    public void fastListLazyIterableSelectSize(int size)
    {
        FastList<Integer> fastList = new FastList<>(Interval.oneTo(size));
        LazyIterable<Integer> lazyIterable = fastList.asLazy()
                .select(i -> i % 2 == 0);
        int count = lazyIterable.size();
        Assertions.assertEquals(IntInterval.oneTo(size).count(i -> i % 2 == 0), count);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 50, 100})
    public void fastListEagerSelectSize(int size)
    {
        FastList<Integer> fastList = new FastList<>(Interval.oneTo(size));
        MutableList<Integer> lazyIterable = fastList.select(i -> i % 2 == 0);
        int count = lazyIterable.size();
        Assertions.assertEquals(IntInterval.oneTo(size).count(i -> i % 2 == 0), count);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 50, 100})
    public void fastListEagerCount(int size)
    {
        FastList<Integer> fastList = new FastList<>(Interval.oneTo(size));
        long count = fastList.count(i -> i % 2 == 0);
        Assertions.assertEquals(IntInterval.oneTo(size).count(i -> i % 2 == 0), count);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 50, 100})
    public void arrayListStreamFilterMapSum(int size)
    {
        ArrayList<Integer> arrayList = new ArrayList<>(Interval.oneTo(size));
        LongStream stream = arrayList.stream()
                .filter(i -> i % 2 == 0)
                .mapToLong(Integer::longValue);
        long sum = stream.sum();
        Assertions.assertEquals(IntInterval.evensFromTo(1, size).sum(), sum);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 50, 100})
    public void fastListLazyIterableSelectCollectSum(int size)
    {
        FastList<Integer> fastList = new FastList<>(Interval.oneTo(size));
        LazyLongIterable lazyIterable = fastList.asLazy()
                .select(i -> i % 2 == 0)
                .collectLong(Integer::longValue);
        long sum = lazyIterable.sum();
        Assertions.assertEquals(IntInterval.evensFromTo(1, size).sum(), sum);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 50, 100})
    public void fastListEagerSelectCollectSum(int size)
    {
        FastList<Integer> fastList = new FastList<>(Interval.oneTo(size));
        LongList lazyIterable = fastList.select(i -> i % 2 == 0)
                .collectLong(Integer::longValue);
        long sum = lazyIterable.sum();
        Assertions.assertEquals(IntInterval.evensFromTo(1, size).sum(), sum);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 50, 100})
    public void fastListEagerSum(int size)
    {
        FastList<Integer> fastList = new FastList<>(Interval.oneTo(size));
        LazyLongIterable lazyIterable = fastList.asLazy()
                .select(i -> i % 2 == 0)
                .collectLong(Integer::longValue);
        long sum = fastList.sumOfLong(i -> i % 2 == 0 ? i : 0);
        Assertions.assertEquals(IntInterval.evensFromTo(1, size).sum(), sum);
    }
}
