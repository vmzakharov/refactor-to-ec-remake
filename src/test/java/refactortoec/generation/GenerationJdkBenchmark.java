package refactortoec.generation;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

import org.eclipse.collections.api.tuple.Triple;
import org.eclipse.collections.api.tuple.Triplet;
import org.eclipse.collections.api.tuple.Twin;
import org.eclipse.collections.api.tuple.primitive.LongObjectPair;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static refactortoec.generation.Generation.MILLENNIAL;
import static refactortoec.generation.Generation.UNCLASSIFIED;
import static refactortoec.generation.GenerationJdk.GENERATION_SET;
import static refactortoec.generation.GenerationJdk.find;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@Warmup(iterations = 10, time = 2)
@Measurement(iterations = 10, time = 2)
public class GenerationJdkBenchmark
{
    @Benchmark
    public LongObjectPair<Map<Integer, Long>> counting()
    {
        long count = GENERATION_SET.stream()
                .filter(generation -> generation.contains(1995))
                .count();

        Map<Integer, Long> generationCountByYears =
                GENERATION_SET.stream()
                        .collect(Collectors.groupingBy(Generation::numberOfYears,
                                Collectors.counting()));

        return PrimitiveTuples.pair(count, generationCountByYears);
    }

    @Benchmark
    public Triplet<Boolean> testing()
    {
        boolean any = GENERATION_SET.stream()
                .anyMatch(generation -> generation.contains(1995));
        boolean all = GENERATION_SET.stream()
                .allMatch(generation -> generation.contains(1995));
        boolean none = GENERATION_SET.stream()
                .noneMatch(generation -> generation.contains(1995));

        return Tuples.triplet(any, all, none);
    }

    @Benchmark
    public List<Generation> finding()
    {
        Generation findFirst =
                GENERATION_SET.stream()
                        .filter(generation -> generation.contains(1995))
                        .findFirst()
                        .orElse(null);

        Generation notFound =
                GENERATION_SET.stream()
                        .filter(generation -> generation.contains(1795))
                        .findFirst()
                        .orElse(UNCLASSIFIED);

        Generation three = find(1985);
        Generation four = find(2016);

        return List.of(findFirst, notFound, three, four);
    }

    @Benchmark
    public Triple<Set<Generation>, Set<Generation>, Map<Boolean, Set<Generation>>> filtering()
    {
        Set<Generation> filteredSelected =
                GENERATION_SET.stream()
                        .filter(generation -> generation.yearsCountEqualsJdk(16))
                        .collect(Collectors.toUnmodifiableSet());

        Set<Generation> filteredRejected =
                GENERATION_SET.stream()
                        .filter(generation -> !generation.yearsCountEqualsJdk(16))
                        .collect(Collectors.toUnmodifiableSet());

        Map<Boolean, Set<Generation>> partition = GENERATION_SET.stream()
                .collect(Collectors.partitioningBy(
                        generation -> generation.yearsCountEqualsJdk(16),
                        Collectors.toUnmodifiableSet()));

        return Tuples.triple(filteredSelected, filteredRejected, partition);
    }

    @Benchmark
    public Map<Integer, Set<Generation>> grouping()
    {
        Map<Integer, Set<Generation>> generationByYears =
                GENERATION_SET.stream()
                        .collect(Collectors.groupingBy(Generation::numberOfYears,
                                Collectors.toSet()));

        return generationByYears;
    }

    @Benchmark
    public List<List<Generation>> converting()
    {
        List<Generation> mutableList =
                GENERATION_SET.stream()
                        .collect(Collectors.toList());
        List<Generation> immutableList =
                GENERATION_SET.stream()
                        .toList();

        List<Generation> sortedMutableList =
                mutableList.stream()
                        .sorted(Comparator.comparing(gen -> gen.yearsStream().findFirst().getAsInt()))
                        .collect(Collectors.toList());

        List<Generation> sortedImmutableList =
                immutableList.stream()
                        .sorted(Comparator.comparing(gen -> gen.yearsStream().findFirst().getAsInt()))
                        .toList();

        return List.of(mutableList, immutableList, sortedImmutableList, sortedImmutableList);
    }

    @Benchmark
    public Twin<Set<String>> transforming()
    {
        Set<String> names =
                GENERATION_SET.stream()
                        .map(Generation::getName)
                        .collect(Collectors.toUnmodifiableSet());

        Set<String> mutableNames = names.stream()
                .collect(Collectors.toSet());

        return Tuples.twin(names, mutableNames);
    }

    @Benchmark
    public Twin<String> chunking()
    {
        Stream<List<Generation>> windowFixedGenerations =
                GenerationJdk.windowFixedGenerations(3);
        String generationsAsString = windowFixedGenerations.map(Object::toString)
                .collect(Collectors.joining(", "));

        String yearsAsString = MILLENNIAL.yearsStream()
                .boxed()
                .gather(Gatherers.windowFixed(4))
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        return Tuples.twin(generationsAsString, yearsAsString);
    }

    @Benchmark
    public Triplet<Integer> folding()
    {
        Integer maxYears = GenerationJdk.fold(
                Integer.MIN_VALUE,
                (Integer value, Generation generation) ->
                        Math.max(value, generation.numberOfYears()));

        Integer minYears = GenerationJdk.fold(
                Integer.MAX_VALUE,
                (Integer value, Generation generation) ->
                        Math.min(value, generation.numberOfYears()));

        Integer sumYears = GenerationJdk.fold(
                Integer.valueOf(0),
                (Integer value, Generation generation) ->
                        Integer.sum(value, generation.numberOfYears()));

        return Tuples.triplet(maxYears, minYears, sumYears);
    }
}
