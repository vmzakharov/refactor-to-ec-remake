package refactortoec.generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.analysis.function.Add;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.tuple.primitive.IntIntPair;
import org.eclipse.collections.impl.list.Interval;
import org.eclipse.collections.impl.tuple.primitive.PrimitiveTuples;
import org.junit.jupiter.api.Test;
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
public class RandomAccessVsArraySpliteratorBenchmark2
{
    private final Interval interval = Interval.oneTo(1_000_000);
    private final List<Integer> listN = List.copyOf(interval);
    private final List<Integer> arrayList = new ArrayList<>(interval);
    private final List<Integer> arraysAsList = Arrays.asList(interval.toArray());
    private final ImmutableList<Integer> immutableList = interval.toImmutableList();

    @Benchmark
    public IntIntPair eagerInjectIntoIntMinMaxImmutableList()
    {
        int min = this.immutableList.injectIntoInt(Integer.MAX_VALUE, Math::min);
        int max = this.immutableList.injectIntoInt(Integer.MIN_VALUE, Math::max);
        return PrimitiveTuples.pair(min, max);
    }

    @Benchmark
    public IntIntPair streamMinMaxImmutableList()
    {
        int[] minMax = {Integer.MAX_VALUE, Integer.MIN_VALUE};
        this.immutableList.stream()
                .forEach(i -> minMax[0] = Math.min(minMax[0], i));
        this.immutableList.stream()
                .forEach(i -> minMax[1] = Math.max(minMax[1], i));
        return PrimitiveTuples.pair(minMax[0], minMax[1]);
    }

    @Benchmark
    public IntIntPair streamMinMaxListN()
    {
        int[] minMax = {Integer.MAX_VALUE, Integer.MIN_VALUE};
        this.listN.stream()
                .forEach(i -> minMax[0] = Math.min(minMax[0], i));
        this.listN.stream()
                .forEach(i -> minMax[1] = Math.max(minMax[1], i));
        return PrimitiveTuples.pair(minMax[0], minMax[1]);
    }

    @Benchmark
    public IntIntPair streamMinMaxArrayList()
    {
        int[] minMax = {Integer.MAX_VALUE, Integer.MIN_VALUE};
        this.arrayList.stream()
                .forEach(i -> minMax[0] = Math.min(minMax[0], i));
        this.arrayList.stream()
                .forEach(i -> minMax[1] = Math.max(minMax[1], i));
        return PrimitiveTuples.pair(minMax[0], minMax[1]);
    }

    @Benchmark
    public IntIntPair streamMinMaxArraysAsList()
    {
        int[] minMax = {Integer.MAX_VALUE, Integer.MIN_VALUE};
        this.arraysAsList.stream()
                .forEach(i -> minMax[0] = Math.min(minMax[0], i));
        this.arraysAsList.stream()
                .forEach(i -> minMax[1] = Math.max(minMax[1], i));
        return PrimitiveTuples.pair(minMax[0], minMax[1]);
    }

    @Benchmark
    public IntIntPair parallelMinMaxImmutableList()
    {
        int min = this.immutableList.parallelStream()
                .reduce(Math::min)
                .orElse(0);
        int max = this.immutableList.parallelStream()
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
