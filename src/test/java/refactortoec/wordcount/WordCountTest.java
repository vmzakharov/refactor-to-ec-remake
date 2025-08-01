package refactortoec.wordcount;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.GraphLayout;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordCountTest
{
    private static List<String> words;
    private static ImmutableList<String> wordsEc;

    @BeforeAll
    static public void loadData()
    {
        words = Arrays.asList(
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

        wordsEc = Lists.immutable.withAll(words);
    }

    @Test
    public void countJdkNaive()
    {
        Map<String, Integer> wordCount = new HashMap<>();

        words.forEach(w -> {
            int count = wordCount.getOrDefault(w, 0);
            count++;
            wordCount.put(w, count);
        });

        assertEquals(2, wordCount.get("Bah").intValue());
        assertEquals(3, wordCount.get("for").intValue());
        assertEquals(1, wordCount.get("Sheep").intValue());

        System.out.println(GraphLayout.parseInstance(wordCount).toFootprint());
    }


    @Test
    public void countJdkStream()
    {
        Map<String, Long> wordCounts = words.stream()
                                            .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        assertEquals(2, wordCounts.get("Bah").intValue());
        assertEquals(3, wordCounts.get("for").intValue());
        assertEquals(1, wordCounts.get("Sheep").intValue());

        System.out.println(GraphLayout.parseInstance(wordCounts).toFootprint());
    }

    @Test
    public void countJdkWithCounter()
    {
        Map<String, LongAdder> wordCounts = new HashMap<>();

        words.forEach(
            w -> wordCounts.computeIfAbsent(w, key -> new LongAdder()).add(1)
        );

        assertEquals(2, wordCounts.get("Bah").intValue());
        assertEquals(3, wordCounts.get("for").intValue());
        assertEquals(1, wordCounts.get("Sheep").intValue());

        System.out.println(GraphLayout.parseInstance(wordCounts).toFootprint());
    }

    @Test
    public void countEc()
    {
        MutableBag<String> bagOfWords = wordsEc.toBag();

        assertEquals(2, bagOfWords.occurrencesOf("Bah"));
        assertEquals(3, bagOfWords.occurrencesOf("for"));
        assertEquals(1, bagOfWords.occurrencesOf("Sheep"));
        assertEquals(0, bagOfWords.occurrencesOf("Cheburashka"));

        System.out.println(GraphLayout.parseInstance(bagOfWords).toFootprint());
    }
}
