package refactortoec.generation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
                generation.yearsStream().forEach(year -> map.put(year, generation)));
        return Map.copyOf(map);
    }

    public static Generation find(int year)
    {
        return BY_YEAR.getOrDefault(year, Generation.UNCLASSIFIED);
    }
}
