package refactortoec;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.primitive.IntLists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.eclipse.collections.impl.list.primitive.IntInterval;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WithAndWithoutTest
{
    @Test
    public void addListVsWithMutableList()
    {
        var expected = Lists.immutable.of("Mary", "Ted", "Sally");

        List<String> jdkList = new ArrayList<>();
        jdkList.add("Mary");
        jdkList.add("Ted");
        jdkList.add("Sally");

        assertEquals(expected, jdkList);

        MutableList<String> ecMutableList = Lists.mutable.empty();
        ecMutableList.with("Mary").with("Ted").with("Sally");

        assertEquals(expected, ecMutableList);
    }

    @Test
    public void removeListVsWithoutMutableList()
    {
        var expected = Lists.immutable.of("Mary", "Sally");

        List<String> jdkList = new ArrayList<>();
        jdkList.add("Mary");
        jdkList.add("Ted");
        jdkList.add("Sally");
        jdkList.remove("Ted");

        assertEquals(expected, jdkList);

        MutableList<String> ecMutableList = Lists.mutable.empty();
        ecMutableList.with("Mary").with("Ted").with("Sally").without("Ted");

        assertEquals(expected, ecMutableList);
    }

    @Test
    public void addAllListVsWithAllMutableList()
    {
        Supplier<Stream<String>> one = () -> Stream.of("a", "b", "c");
        Supplier<Stream<String>> two = () -> Stream.of("d", "e", "f");
        Supplier<Stream<String>> three = () -> Stream.of("g", "h", "i");
        var expected = List.of("a", "b", "c", "d", "e", "f", "g", "h", "i");

        List<String> jdkList = new ArrayList<>();
        jdkList.addAll(one.get().toList());
        jdkList.addAll(two.get().toList());
        jdkList.addAll(three.get().toList());

        assertEquals(expected, jdkList);

        MutableList<String> ecMutableList = Lists.mutable.<String>empty()
                .withAll(one.get()::iterator)
                .withAll(two.get()::iterator)
                .withAll(three.get()::iterator);

        assertEquals(expected, ecMutableList);

        ImmutableList<Integer> immutableList = Lists.immutable.with(1, 2, 3);
        ArrayList<Integer> list = immutableList.into(new ArrayList<>());
        HashSet<Integer> set = immutableList.into(new HashSet<>());
    }

    @Test
    public void withAllMutableIntList()
    {
        ImmutableIntList list = IntLists.immutable.withAll(IntStream.rangeClosed(1, 10));
        ImmutableIntList interval = IntInterval.oneTo(10);
        assertEquals(list, interval);
    }

    public record Person(String firstName, String lastName)
    {
        public boolean lastNameEquals(String name)
        {
            return name.equals(this.lastName);
        }
    }

    @Test
    public void collectLastNamesAsString()
    {
        MutableList<Person> people = Lists.mutable.with(
                new Person("Sally", "Smith"),
                new Person("Ted", "Watson"),
                new Person("Mary", "Williams"));
        MutableList<String> lastNames = people.collect(Person::lastName);
        assertEquals("Smith, Watson, Williams", lastNames.makeString());
    }
}
