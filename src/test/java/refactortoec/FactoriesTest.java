package refactortoec;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FactoriesTest
{
    @Test
    public void setFactories()
    {
        // JDK Types
        Set<String> jdkMutableSet = new HashSet<>(List.of("1", "2", "3"));
        Set<String> jdkImmutableSet = Set.of("1", "2", "3");

        // Eclipse Collections Types
        MutableSet<String> ecMutableSet1 = Sets.mutable.of("1", "2", "3");
        ImmutableSet<String> ecImmutableSet1 = Sets.immutable.of("1", "2", "3");

        // Eclipse Collections using JDK Types
        Set<String> ecMutableSet2 = ecMutableSet1;
        Set<String> ecImmutableSet2 = ecImmutableSet1.castToSet();

        // Testing JDK and Eclipse Collections Set Types for Equality
        var jdkSets = Sets.mutable.of(jdkImmutableSet, jdkMutableSet);
        var ecSets = Sets.mutable.of(ecMutableSet1, ecImmutableSet1, ecMutableSet2, ecImmutableSet2);
        jdkSets.cartesianProduct(ecSets)
                .forEach(pair -> assertEquals(pair.getOne(), pair.getTwo()));
    }
}
