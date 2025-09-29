package refactortoec.generation;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.set.ImmutableSetMultimap;
import org.eclipse.collections.api.partition.set.PartitionImmutableSet;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.tuple.Triple;
import org.eclipse.collections.api.tuple.Triplet;
import org.eclipse.collections.api.tuple.Twin;
import org.eclipse.collections.api.tuple.primitive.IntObjectPair;
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

import static refactortoec.generation.Generation.MILLENNIAL;
import static refactortoec.generation.Generation.UNCLASSIFIED;
import static refactortoec.generation.GenerationEc.GENERATION_IMMUTABLE_SET;
import static refactortoec.generation.GenerationEc.find;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@Warmup(iterations = 10, time = 2)
@Measurement(iterations = 10, time = 2)
public class GenerationEcBenchmark
{
    @Benchmark
    public IntObjectPair<ImmutableBag<Integer>> counting()
    {
        int count =
                GENERATION_IMMUTABLE_SET.countWith(Generation::contains, 1995);

        ImmutableBag<Integer> generationCountByYears =
                GENERATION_IMMUTABLE_SET.countBy(Generation::numberOfYears);

        return PrimitiveTuples.pair(count, generationCountByYears);
    }

    @Benchmark
    public Triplet<Boolean> testing()
    {
        ImmutableSet<Generation> generations = GENERATION_IMMUTABLE_SET;
        // boolean any = generations.anySatisfyWith(Generation::contains, 1995);
        // boolean all = generations.allSatisfyWith(Generation::contains, 1995);
        // boolean none = generations.noneSatisfyWith(Generation::contains, 1995);
        boolean any = generations.anySatisfy(gen -> gen.contains(1995));
        boolean all = generations.allSatisfy(gen -> gen.contains(1995));
        boolean none = generations.noneSatisfy(gen -> gen.contains(1995));

        return Tuples.triplet(any, all, none);
    }

    @Benchmark
    public List<Generation> finding()
    {
        Generation detected =
                GENERATION_IMMUTABLE_SET.detectWith(Generation::contains, 1995);

        Generation notFound =
                GENERATION_IMMUTABLE_SET.detectWithIfNone(Generation::contains, 1795, () -> UNCLASSIFIED);

        Generation three = find(1985);
        Generation four = find(2016);

        return List.of(detected, notFound, three, four);
    }

    @Benchmark
    public Triple<ImmutableSet<Generation>, ImmutableSet<Generation>, PartitionImmutableSet<Generation>> filtering()
    {
        ImmutableSet<Generation> filtered =
                GENERATION_IMMUTABLE_SET.selectWith(Generation::yearsCountEqualsEc, 16);

        ImmutableSet<Generation> filteredNot =
                GENERATION_IMMUTABLE_SET.rejectWith(Generation::yearsCountEqualsEc, 16);

        PartitionImmutableSet<Generation> partition =
                GENERATION_IMMUTABLE_SET.partitionWith(Generation::yearsCountEqualsEc, 16);

        return Tuples.triple(filtered, filteredNot, partition);
    }

    @Benchmark
    public ImmutableSetMultimap<Integer, Generation> grouping()
    {
        ImmutableSetMultimap<Integer, Generation> generationByYears =
                GENERATION_IMMUTABLE_SET.groupBy(generation -> generation.yearsInterval().size());

        return generationByYears;
    }

    @Benchmark
    public List<ListIterable<Generation>> converting()
    {
        MutableList<Generation> mutableList =
                GENERATION_IMMUTABLE_SET.toList();
        ImmutableList<Generation> immutableList =
                GENERATION_IMMUTABLE_SET.toImmutableList();

        MutableList<Generation> sortedMutableList =
                mutableList.toSortedListBy(gen -> gen.yearsInterval().getFirst());

        ImmutableList<Generation> sortedImmutableList =
                immutableList.toImmutableSortedListBy(gen -> gen.yearsInterval().getFirst());

        return List.of(mutableList, immutableList, sortedImmutableList, sortedImmutableList);
    }

    @Benchmark
    public Twin<Iterable<String>> transforming()
    {
        ImmutableSet<String> names =
                GENERATION_IMMUTABLE_SET.collect(Generation::getName);

        Set<String> mutableNames = names.toSet();

        return Tuples.twin(names, mutableNames);
    }

    @Benchmark
    public Twin<String> chunking()
    {
        RichIterable<RichIterable<Generation>> chunkGenerations =
                GenerationEc.chunkGenerations(3);
        String generationsAsString = chunkGenerations.makeString();

        String yearsAsString = MILLENNIAL.yearsInterval()
                .chunk(4)
                .makeString();

        return Tuples.twin(generationsAsString, yearsAsString);
    }

    @Benchmark
    public Triplet<Integer> folding()
    {
        Integer maxYears = GenerationEc.fold(
                Integer.MIN_VALUE,
                (Integer value, Generation generation) ->
                        Math.max(value, generation.yearsInterval().size()));

        Integer minYears = GenerationEc.fold(
                Integer.MAX_VALUE,
                (Integer value, Generation generation) ->
                        Math.min(value, generation.yearsInterval().size()));

        Integer sumYears = GenerationEc.fold(
                Integer.valueOf(0),
                (Integer value, Generation generation) ->
                        Integer.sum(value, generation.yearsInterval().size()));

        return Tuples.triplet(maxYears, minYears, sumYears);
    }
}
