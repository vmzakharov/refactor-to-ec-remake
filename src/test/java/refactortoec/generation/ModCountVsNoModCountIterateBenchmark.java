package refactortoec.generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.list.Interval;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.primitive.IntInterval;
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
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@Warmup(iterations = 20, time = 2)
@Measurement(iterations = 20, time = 2)
public class ModCountVsNoModCountIterateBenchmark
{
    private static final int SIZE = 100_000;

    private final List<Integer> arrayList = new ArrayList<>(Interval.oneTo(SIZE));
    private final List<Integer> listN = arrayList.stream().toList();
    private final List<Integer> arraysAsList = Arrays.asList(Interval.oneTo(SIZE).toArray());
    private final MutableList<Integer> fastList = new FastList<>(Interval.oneTo(SIZE));
    private final ImmutableList<Integer> immutableList = fastList.toImmutable();
    private final MutableIntList mutableIntList = IntInterval.oneTo(SIZE).toList();
    private final ImmutableIntList immutableIntList = mutableIntList.toImmutable();

    @Benchmark
    public long arrayListStreamCountEvens()
    {
        return this.arrayList.stream().filter(i -> i % 2 == 0).count();
    }

    @Benchmark
    public long listNStreamCountEvens()
    {
        return this.listN.stream().filter(i -> i % 2 == 0).count();
    }

    @Benchmark
    public long arraysAsListStreamCountEvens()
    {
        return this.arraysAsList.stream().filter(i -> i % 2 == 0).count();
    }

    @Benchmark
    public long fastListStreamCountEvens()
    {
        return this.fastList.stream().filter(i -> i % 2 == 0).count();
    }

    @Benchmark
    public long immutableListStreamCountEvens()
    {
        return this.fastList.stream().filter(i -> i % 2 == 0).count();
    }

    @Benchmark
    public long mutableIntListStreamCountEvens()
    {
        return this.mutableIntList.primitiveStream().filter(i -> i % 2 == 0).count();
    }

    @Benchmark
    public long immutableIntListStreamCountEvens()
    {
        return this.immutableIntList.primitiveStream().filter(i -> i % 2 == 0).count();
    }

    @Benchmark
    public long arrayListParallelStreamCountEvens()
    {
        return this.arrayList.parallelStream().filter(i -> i % 2 == 0).count();
    }

    @Benchmark
    public long listNParallelStreamCountEvens()
    {
        return this.listN.parallelStream().filter(i -> i % 2 == 0).count();
    }

    @Benchmark
    public long arraysAsListParallelStreamCountEvens()
    {
        return this.arraysAsList.parallelStream().filter(i -> i % 2 == 0).count();
    }

    @Benchmark
    public long fastListParallelStreamCountEvens()
    {
        return this.fastList.parallelStream().filter(i -> i % 2 == 0).count();
    }

    @Benchmark
    public long immutableListParallelStreamCountEvens()
    {
        return this.immutableList.parallelStream().filter(i -> i % 2 == 0).count();
    }

    @Benchmark
    public long mutableIntListParallelStreamCountEvens()
    {
        return this.mutableIntList.primitiveParallelStream().filter(i -> i % 2 == 0).count();
    }

    @Benchmark
    public long immutableIntListParallelStreamCountEvens()
    {
        return this.immutableIntList.primitiveParallelStream().filter(i -> i % 2 == 0).count();
    }
}
