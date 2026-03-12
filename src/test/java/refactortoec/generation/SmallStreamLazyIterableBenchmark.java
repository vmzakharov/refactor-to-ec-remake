package refactortoec.generation;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.api.LazyLongIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableLongList;
import org.eclipse.collections.impl.list.Interval;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@Warmup(iterations = 20, time = 2)
@Measurement(iterations = 10, time = 2)
public class SmallStreamLazyIterableBenchmark
{
    @Param({"1", "5", "10", "50", "100"})
    int size;

    ArrayList<Integer> arrayList;
    FastList<Integer> fastList;

    @Setup(Level.Iteration)
    public void setup()
    {
        this.arrayList = new ArrayList<>(Interval.oneTo(size));
        this.fastList = new FastList<>(Interval.oneTo(size));
    }

    @Benchmark
    public void baseline()
    {
        // empty
    }

    @Benchmark
    public void arrayListStreamFilterCount(Blackhole bh)
    {
        Stream<Integer> stream = arrayList.stream()
                .filter(i -> i % 2 == 0);
        long count = stream.count();
        bh.consume(count);
    }

    @Benchmark
    public void arrayListForLoopFilterCount(Blackhole bh)
    {
        long count = 0L;
        for (Integer each : arrayList)
        {
            if (each % 2 == 0)
            {
                count++;
            }
        }
        bh.consume(count);
    }

    @Benchmark
    public void fastListLazyIterableSelectSize(Blackhole bh)
    {
        LazyIterable<Integer> lazyIterable = fastList.asLazy()
                .select(i -> i % 2 == 0);
        long count = lazyIterable.size();
        bh.consume(count);
    }

    @Benchmark
    public void fastListEagerSelectSize(Blackhole bh)
    {
        MutableList<Integer> lazyIterable = fastList.select(i -> i % 2 == 0);
        long count = lazyIterable.size();
        bh.consume(count);
    }

    @Benchmark
    public void fastListEagerCount(Blackhole bh)
    {
        long count = fastList.count(i -> i % 2 == 0);
        bh.consume(count);
    }

    @Benchmark
    public void arrayListStreamFilterMapSum(Blackhole bh)
    {
        LongStream stream = arrayList.stream()
                .filter(i -> i % 2 == 0)
                .mapToLong(Integer::longValue);
        long sum = stream.sum();
        bh.consume(sum);
    }

    @Benchmark
    public void arrayListForLoopFilterMapSum(Blackhole bh)
    {
        long sum = 0L;
        for (Integer each : arrayList)
        {
            if (each % 2 == 0)
            {
                sum += each.longValue();
            }
        }
        bh.consume(sum);
    }

    @Benchmark
    public void fastListLazyIterableSelectCollectSum(Blackhole bh)
    {
        LazyLongIterable lazyIterable = fastList.asLazy()
                .select(i -> i % 2 == 0)
                .collectLong(Integer::longValue);
        long sum = lazyIterable.sum();
        bh.consume(sum);
    }

    @Benchmark
    public void fastListEagerSelectCollectSum(Blackhole bh)
    {
        MutableLongList lazyIterable = fastList.select(i -> i % 2 == 0)
                .collectLong(Integer::longValue);
        long sum = lazyIterable.sum();
        bh.consume(sum);
    }

    @Benchmark
    public void fastListEagerSumOfLong(Blackhole bh)
    {
        long sum = fastList.sumOfLong(i -> i % 2 == 0 ? i : 0);
        bh.consume(sum);
    }
}
