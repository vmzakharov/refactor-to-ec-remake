package refactortoec.generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.collections.api.tuple.primitive.IntIntPair;
import org.eclipse.collections.impl.list.Interval;
import org.eclipse.collections.impl.tuple.primitive.PrimitiveTuples;
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
@Warmup(iterations = 20, time = 2)
@Measurement(iterations = 10, time = 2)
public class RandomAccessVsArraySpliteratorBenchmark
{
    private final Interval interval = Interval.oneTo(1_000_000);
    private final List<Integer> listN = List.copyOf(interval);
    private final List<Integer> arrayList = new ArrayList<>(interval);
    private final List<Integer> arraysAsList = Arrays.asList(interval.toArray());

    @Benchmark
    public IntIntPair minMaxListN()
    {
        int min = this.listN.stream()
                .reduce(Math::min)
                .orElse(0);
        int max = this.listN.stream()
                .reduce(Math::max)
                .orElse(0);
        return PrimitiveTuples.pair(min, max);
    }

    @Benchmark
    public IntIntPair minMaxArrayList()
    {
        int min = this.arrayList.stream()
                .reduce(Math::min)
                .orElse(0);
        int max = this.arrayList.stream()
                .reduce(Math::max)
                .orElse(0);
        return PrimitiveTuples.pair(min, max);
    }

    @Benchmark
    public IntIntPair minMaxArraysAsList()
    {
        int min = this.arraysAsList.stream()
                .reduce(Math::min)
                .orElse(0);
        int max = this.arraysAsList.stream()
                .reduce(Math::max)
                .orElse(0);
        return PrimitiveTuples.pair(min, max);
    }

    @Benchmark
    public IntIntPair parallelMinMaxListN()
    {
        int min = this.listN.parallelStream()
                .reduce(Math::min)
                .orElse(0);
        int max = this.listN.parallelStream()
                .reduce(Math::max)
                .orElse(0);
        return PrimitiveTuples.pair(min, max);
    }

    @Benchmark
    public IntIntPair parallelMinMaxArrayList()
    {
        int min = this.arrayList.parallelStream()
                .reduce(Math::min)
                .orElse(0);
        int max = this.arrayList.parallelStream()
                .reduce(Math::max)
                .orElse(0);
        return PrimitiveTuples.pair(min, max);
    }

    @Benchmark
    public IntIntPair parallelMinMaxArraysAsList()
    {
        int min = this.arraysAsList.parallelStream()
                .reduce(Math::min)
                .orElse(0);
        int max = this.arraysAsList.parallelStream()
                .reduce(Math::max)
                .orElse(0);
        return PrimitiveTuples.pair(min, max);
    }
}
