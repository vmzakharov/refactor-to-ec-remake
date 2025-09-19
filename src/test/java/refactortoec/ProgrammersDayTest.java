package refactortoec;

import java.util.stream.Gatherers;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.eclipse.collections.api.list.primitive.IntList;
import org.eclipse.collections.impl.factory.Strings;
import org.eclipse.collections.impl.list.fixed.ArrayAdapter;
import org.eclipse.collections.impl.utility.ArrayIterate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgrammersDayTest
{
    String text = """
            01001000 01100001 01110000 01110000 01111001 00100000 01010000 01110010 01101111 01100111 01110010 \
            01100001 01101101 01101101 01100101 01110010 00100111 01110011 00100000 01000100 01100001 01111001 \
            00100000 01100110 01110010 01101111 01101101 00100000 01001010 01100101 01110100 01000010 01110010 \
            01100001 01101001 01101110 01110011 00100001""";

    String expectedMessage = "Happy Programmer's Day from JetBrains!";

    @Test
    public void printEc()
    {
        String[] strings = text.split(" ");

        String message = ArrayAdapter
                .adapt(strings)
                .collectInt(ascii -> Integer.parseInt(ascii, 2))
                .injectInto(new StringBuilder(), StringBuilder::appendCodePoint)
                .toString();

        assertEquals(expectedMessage, message);
    }

    @Test
    public void printJdk()
    {
        String[] strings = text.split(" ");

        String message = Stream.of(strings)
               .mapToInt(ascii -> Integer.parseInt(ascii, 2))
               .boxed()
               .gather(
                       Gatherers.fold(StringBuilder::new, StringBuilder::appendCodePoint)
               )
               .findFirst()
               .orElse(null)
               .toString();

        assertEquals(expectedMessage, message);
    }
}
