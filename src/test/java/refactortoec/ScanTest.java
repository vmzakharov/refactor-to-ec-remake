package refactortoec;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.primitive.IntLists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScanTest
{
    @Test
    public void scanJdk()
    {
        var result = Stream.of(1, 2, 3, 4)
                .gather(Gatherers.scan(() -> "", (string, i) -> string + i))
                .toList();

        assertEquals(List.of("1", "12", "123", "1234"), result);
    }

    @Test
    public void scanEc()
    {
        MutableList<String> result = Lists.mutable.empty();

        IntLists.immutable.of(1, 2, 3, 4)
                .injectInto("", (bloop, i) -> result.with(bloop + i).getLast());

        assertEquals(List.of("1", "12", "123", "1234"), result);
    }

    @Test
    public void scanEcImmutable()
    {
        ImmutableList<String> result = IntLists.immutable.of(1, 2, 3, 4)
                .injectInto(
                        Lists.immutable.empty(),
                        (bloop, i) -> bloop.newWith(bloop.getLastOptional().orElse("") + i)
                );

        assertEquals(List.of("1", "12", "123", "1234"), result);
    }
}
