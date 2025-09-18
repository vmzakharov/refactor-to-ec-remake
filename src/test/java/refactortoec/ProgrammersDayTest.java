package refactortoec;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.eclipse.collections.api.list.primitive.IntList;
import org.eclipse.collections.impl.factory.Strings;
import org.eclipse.collections.impl.utility.ArrayIterate;
import org.junit.jupiter.api.Test;

public class ProgrammersDayTest
{
    String text = "01001000 01100001 01110000 01110000 01111001 00100000 01010000 01110010 01101111 01100111 01110010 01100001 01101101 01101101 01100101 01110010 00100111 01110011 00100000 01000100 01100001 01111001 00100000 01100110 01110010 01101111 01101101 00100000 01001010 01100101 01110100 01000010 01110010 01100001 01101001 01101110 01110011 00100001";

    @Test
    public void printEc()
    {
        String[] strings = text.split(" ");
        IntList intList = ArrayIterate.collectInt(strings, ascii -> Integer.parseInt(ascii, 2));
        CharSequence message = Strings.toCodePoints(intList.toArray());
        System.out.println(message);
    }

    @Test
    public void printJdk()
    {
        String[] strings = text.split(" ");
        Stream<String> stream = Stream.of(strings);
        IntStream intStream = stream.mapToInt(ascii -> Integer.parseInt(ascii, 2));
        int[] array = intStream.toArray();
        CharSequence message = new String(array, 0, array.length);
        System.out.println(message);
    }
}
