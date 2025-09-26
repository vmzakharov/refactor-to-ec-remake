package refactortoec;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.factory.Lists;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FactoriesTest
{
    @Test
    public void setFactories()
    {
        // JDK Types
        Set<String> jdkMutableSet =
                new HashSet<>(List.of("1", "2", "3"));
        Set<String> jdkImmutableSet =
                Set.of("1", "2", "3");

        // Drop-in replacements
        Set<String> ecMutableSet1 =
                Sets.mutable.of("1", "2", "3");
        Set<String> ecImmutableSet1 =
                Sets.immutable.of("1", "2", "3").castToSet();

        // Eclipse Collections Types
        MutableSet<String> ecMutableSet2 =
                Sets.mutable.of("1", "2", "3");
        ImmutableSet<String> ecImmutableSet2 =
                Sets.immutable.of("1", "2", "3");

        // Testing JDK and Eclipse Collections Set Types for Equality
        MutableSet<Iterable<String>>
                ecSets = Sets.mutable.of(ecMutableSet1, ecImmutableSet1, ecMutableSet2, ecImmutableSet2);
        MutableSet<Set<String>> jdkSets =
                Sets.mutable.of(jdkImmutableSet, jdkMutableSet);
        ecSets.cartesianProduct(jdkSets)
                .forEach(pair -> assertEquals(pair.getOne(), pair.getTwo()));
    }

    @Test
    public void listFactories()
    {
        // JDK Types
        List<String> jdkMutableList =
                new ArrayList<>(List.of("1", "2", "3"));
        List<String> jdkImmutableList =
                List.of("1", "2", "3");

        // Drop-in replacements
        List<String> ecMutableList1 =
                Lists.mutable.of("1", "2", "3");
        List<String> ecImmutableList1 =
                Lists.immutable.of("1", "2", "3").castToList();

        // Eclipse Collections Types
        MutableList<String> ecMutableList2 =
                Lists.mutable.of("1", "2", "3");
        ImmutableList<String> ecImmutableList2 =
                Lists.immutable.of("1", "2", "3");

        // Testing JDK and Eclipse Collections Set Types for Equality
        MutableSet<Iterable<String>>
                ecLists = Sets.mutable.of(ecMutableList1, ecImmutableList1, ecMutableList2, ecImmutableList2);
        MutableSet<List<String>> jdkLists =
                Sets.mutable.of(jdkImmutableList, jdkMutableList);
        ecLists.cartesianProduct(jdkLists)
                .forEach(pair -> assertEquals(pair.getOne(), pair.getTwo()));
    }
}
