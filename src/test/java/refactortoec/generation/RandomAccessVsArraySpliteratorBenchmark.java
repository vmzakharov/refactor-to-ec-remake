package refactortoec.generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.collections.impl.list.Interval;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(2)
@Warmup(iterations = 10, time = 2)
@Measurement(iterations = 10, time = 2)
public class RandomAccessVsArraySpliteratorBenchmark
{
    private final List<Integer> listN = List.copyOf(Interval.oneTo(1_000_000));
    private final List<Integer> arrayList = new ArrayList<>(Interval.oneTo(1_000_000));
    private final List<Integer> arraysAsList = Arrays.asList(Interval.oneTo(1_000_000).toArray());

    @Benchmark
    public long summingListN()
    {
        long sum = this.listN.stream()
                .mapToInt(Integer::intValue)
                .sum();
        return sum;
    }

    @Benchmark
    public long summingArrayList()
    {
        long sum = this.arrayList.stream()
                .mapToInt(Integer::intValue)
                .sum();
        return sum;
    }

    @Benchmark
    public long summingArraysAsList()
    {
        long sum = this.arraysAsList.stream()
                .mapToInt(Integer::intValue)
                .sum();
        return sum;
    }

    @Benchmark
    public long parallelSummingListN()
    {
        long sum = this.listN.parallelStream()
                .mapToInt(Integer::intValue)
                .sum();
        return sum;
    }

    @Benchmark
    public long parallelSummingArrayList()
    {
        long sum = this.arrayList.parallelStream()
                .mapToInt(Integer::intValue)
                .sum();
        return sum;
    }

    @Benchmark
    public long parallelSummingArraysAsList()
    {
        long sum = this.arraysAsList.parallelStream()
                .mapToInt(Integer::intValue)
                .sum();
        return sum;
    }
}
