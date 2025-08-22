package refactortoec.util;

import org.openjdk.jol.info.GraphLayout;

public class MemoryMeter
{
    /**
     * Use JOL to output memory for an object
     */
    static public void outputMemory(Object instance)
    {
        System.out.println(instance.getClass().getSimpleName() + ": " + GraphLayout.parseInstance(instance).totalSize());
        // System.out.println(GraphLayout.parseInstance(instance).toFootprint());
    }
}
