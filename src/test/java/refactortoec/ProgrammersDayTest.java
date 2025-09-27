package refactortoec;

import java.util.stream.Gatherers;
import java.util.stream.Stream;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.impl.list.fixed.ArrayAdapter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The text in this test was borrowed from a social media post from JetBrains on Programmer's Day. It was an
 * encoded message for programmer's to decipher. We thought it might be interesting to see how we would
 * decipher the message using imperative Java (eager), Java Stream (lazy) and Eclipse Collections (eager and lazy).
 */
public class ProgrammersDayTest
{
    private static final String TEXT = """
            01001000 01100001 01110000 01110000 01111001 00100000 01010000 01110010 01101111 01100111 01110010 \
            01100001 01101101 01101101 01100101 01110010 00100111 01110011 00100000 01000100 01100001 01111001 \
            00100000 01100110 01110010 01101111 01101101 00100000 01001010 01100101 01110100 01000010 01110010 \
            01100001 01101001 01101110 01110011 00100001""";

    private static final String EXPECTED_MESSAGE = "Happy Programmer's Day from JetBrains!";

    @Test
    public void decipherMessageImperative()
    {
        String[] strings = TEXT.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String each : strings)
        {
            builder.appendCodePoint(Integer.parseInt(each, 2));
        }
        String message = builder.toString();

        assertEquals(EXPECTED_MESSAGE, message);
    }

    /**
     * We wanted to try and solve this using a fluent approach with Java Stream. We decided to use
     * the gather method with Gatherers.fold. There is no primitive Stream version of gather, so
     * we had to box the IntString as a Stream<Intege> to use gather.
     */
    @Test
    public void decipherMessageStream()
    {
        String message = Stream.of(TEXT.split(" "))
                .mapToInt(ascii -> Integer.parseInt(ascii, 2))
                .boxed()
                .gather(
                        Gatherers.fold(StringBuilder::new, StringBuilder::appendCodePoint)
                )
                .findFirst()
                .orElse(new StringBuilder())
                .toString();

        assertEquals(EXPECTED_MESSAGE, message);
    }

    /**
     * We wanted to try and solve this using a fluent and eager approach with Eclipse Collections. We decided to use
     * the injectInto method, which has a primitive version that works with IntList.
     */
    @Test
    public void decipherMessageEcEager()
    {
        String message = ArrayAdapter.adapt(TEXT.split(" "))
                .collectInt(ascii -> Integer.parseInt(ascii, 2))
                .injectInto(new StringBuilder(), StringBuilder::appendCodePoint)
                .toString();

        assertEquals(EXPECTED_MESSAGE, message);
    }

    /**
     * We wanted to try and solve this using a fluent and lazy approach with Eclipse Collections. We decided to use
     * the injectInto method. The call to asLazy here is a nice potential optimization because an IntList will not be
     * created during the call to collectInt.
     */
    @Test
    public void decipherMessageEcLazy()
    {
        String message = ArrayAdapter.adapt(TEXT.split(" "))
                .asLazy()
                .collectInt(ascii -> Integer.parseInt(ascii, 2))
                .injectInto(new StringBuilder(), StringBuilder::appendCodePoint)
                .toString();

        assertEquals(EXPECTED_MESSAGE, message);
    }
}
