package refactortoec.wordcount;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
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
@Warmup(iterations = 10, time = 2)
@Measurement(iterations = 10, time = 2)
public class WordCountBenchmark
{
    private static final List<String> WORDS = Arrays.asList(
            """
                    Bah, Bah, a black Sheep
                    Have you any Wool?
                    Yes merry I Have,
                    Three Bags full,
                    Two for my Master,
                    One for my Dame,
                    None for the Little Boy
                    That cries in the lane
                    """.split("[ ,\n?]+")
    );
    private static final ImmutableList<String> WORDS_EC =
            Lists.immutable.withAll(WORDS);

    @Benchmark
    public Map<String, Integer> countJdkNaive()
    {
        Map<String, Integer> wordCount = new HashMap<>();

        WORDS.forEach(w -> {
            int count = wordCount.getOrDefault(w, 0);
            count++;
            wordCount.put(w, count);
        });
        return wordCount;
    }

    @Benchmark
    public Map<String, Long> countJdkStream()
    {
        Map<String, Long> wordCounts =
                WORDS.stream()
                        .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        return wordCounts;
    }

    @Benchmark
    public Map<String, LongAdder> countJdkWithCounter()
    {
        Map<String, LongAdder> wordCounts = new HashMap<>();

        WORDS.forEach(
                w -> wordCounts.computeIfAbsent(w, _ -> new LongAdder()).add(1)
        );
        return wordCounts;
    }

    static public class Counter
    {
        private long count;

        public void increment()
        {
            this.count++;
        }

        public long getCount()
        {
            return this.count;
        }
    }

    @Benchmark
    public Map<String, Counter> countJdkWithHomeBrewCounter()
    {
        Map<String, Counter> wordCounts = new HashMap<>();

        WORDS.forEach(
                w -> wordCounts.computeIfAbsent(w, _ -> new Counter()).increment()
        );
        return wordCounts;
    }

    @Benchmark
    public MutableBag<String> countEc()
    {
        MutableBag<String> wordCounts = WORDS_EC.toBag();
        return wordCounts;
    }
}
