package refactortoec.generation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

import org.eclipse.collections.api.block.function.Function2;

public class GenerationJdk
{
    public static final Set<Generation> GENERATION_SET =
            Set.of(Generation.values());

    public static final Map<Integer, Generation> BY_YEAR =
            GenerationJdk.groupEachByYear();

    private static Map<Integer, Generation> groupEachByYear()
    {
        Map<Integer, Generation> map = new HashMap<>();
        GENERATION_SET.forEach(generation ->
                generation.yearsStream()
                        .forEach(year -> map.put(year, generation)));
        return Map.copyOf(map);
    }

    public static Generation find(int year)
    {
        return BY_YEAR.getOrDefault(year, Generation.UNCLASSIFIED);
    }

    public static Stream<List<Generation>> windowFixedGenerations(int size)
    {
        return Arrays.stream(Generation.values())
                .gather(Gatherers.windowFixed(size));
    }

    public static <IV> IV fold(IV value, BiFunction<IV, Generation, IV> function)
    {
        return GENERATION_SET.stream()
                .gather(Gatherers.fold(() -> value, function))
                .findFirst()
                .orElse(value);
    }
}
