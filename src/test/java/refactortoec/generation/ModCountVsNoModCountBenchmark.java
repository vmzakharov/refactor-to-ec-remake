package refactortoec.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.api.tuple.primitive.IntObjectPair;
import org.eclipse.collections.impl.Counter;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.tuple.Tuples;
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
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@Warmup(iterations = 20, time = 2)
@Measurement(iterations = 10, time = 2)
public class ModCountVsNoModCountBenchmark
{
    private static final int SIZE = 100;

    @Benchmark
    public List<String> modCountAdd()
    {
        List<String> arrayList = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++)
        {
            arrayList.add("");
        }
        return arrayList;
    }

    @Benchmark
    public List<String> noModCountAdd()
    {
        List<String> fastList = new FastList<>(SIZE);
        for (int i = 0; i < SIZE; i++)
        {
            fastList.add("");
        }
        return fastList;
    }

    @Benchmark
    public Pair<Counter, List<String>> modCountAddForEach()
    {
        List<String> arrayList = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++)
        {
            arrayList.add("");
        }
        Counter counter = new Counter();
        arrayList.forEach(each -> counter.increment());
        return Tuples.pair(counter, arrayList);
    }

    @Benchmark
    public Pair<Counter, List<String>> noModCountAddForEach()
    {
        List<String> fastList = new FastList<>(SIZE);
        for (int i = 0; i < SIZE; i++)
        {
            fastList.add("");
        }
        Counter counter = new Counter();
        fastList.forEach(each -> counter.increment());
        return Tuples.pair(counter, fastList);
    }

    @Benchmark
    public IntObjectPair<List<String>> modCountAddIterator()
    {
        List<String> arrayList = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++)
        {
            arrayList.add("");
        }
        int counter = 0;
        for (String each : arrayList)
        {
            counter += each.length() + 1;
        }
        return PrimitiveTuples.pair(counter, arrayList);
    }

    @Benchmark
    public IntObjectPair<List<String>> noModCountAddIterator()
    {
        List<String> fastList = new FastList<>(SIZE);
        for (int i = 0; i < SIZE; i++)
        {
            fastList.add("");
        }
        int counter = 0;
        for (String each : fastList)
        {
            counter += each.length() + 1;
        }
        return PrimitiveTuples.pair(counter, fastList);
    }

    @Benchmark
    public IntObjectPair<List<String>> modCountAddIndexed()
    {
        List<String> arrayList = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++)
        {
            arrayList.add("");
        }
        int counter = 0;
        final int localSize = arrayList.size();
        for (int i = 0; i < localSize; i++)
        {
            String each = arrayList.get(i);
            counter += each.length() + 1;
        }
        return PrimitiveTuples.pair(counter, arrayList);
    }

    @Benchmark
    public IntObjectPair<List<String>> noModCountAddIndexed()
    {
        List<String> fastList = new FastList<>(SIZE);
        for (int i = 0; i < SIZE; i++)
        {
            fastList.add("");
        }
        int counter = 0;
        final int localSize = fastList.size();
        for (int i = 0; i < localSize; i++)
        {
            String each = fastList.get(i);
            counter += each.length() + 1;
        }
        return PrimitiveTuples.pair(counter, fastList);
    }
}
