package refactortoec.generation;

import java.lang.management.ManagementFactory;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JolMemoryTestUtil
{
    private static final boolean IS_COH_ENABLED = JolMemoryTestUtil.checkIfCompactObjectHeadersEnabled();

    private JolMemoryTestUtil()
    {
    }

    private static boolean checkIfCompactObjectHeadersEnabled()
    {
        int majorVersion = Runtime.version().major();

        if (majorVersion >= 24)
        {
            HotSpotDiagnosticMXBean bean = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
            VMOption option = bean.getVMOption("UseCompactObjectHeaders");
            return "true".equals(option.getValue());
        }
        return false;
    }

    public static void assertClassMemoryEquals(long expected, long expectedCOH, Object instance)
    {
        long memoryRequired = IS_COH_ENABLED ? expectedCOH : expected;
        assertEquals(memoryRequired, ClassLayout.parseInstance(instance).instanceSize());
    }

    public static void assertGraphMemoryEqualsAdjusted(long expected, long expectedCOH, Object instance, Object instanceToSubtract)
    {
        long memoryRequired = IS_COH_ENABLED ? expectedCOH : expected;
        long totalSize = GraphLayout.parseInstance(instance).totalSize();
        long adjustedSize = totalSize - (instanceToSubtract != null ?
                GraphLayout.parseInstance(instanceToSubtract).totalSize() : 0L);
        assertEquals(memoryRequired, adjustedSize);
    }

    public static void assertGraphMemoryEquals(long expected, long expectedCOH, Object instance)
    {
        long memoryRequired = IS_COH_ENABLED ? expectedCOH : expected;
        long totalSize = GraphLayout.parseInstance(instance).totalSize();
        assertEquals(memoryRequired, totalSize);
    }

    public static void outputGraphLayoutFootprint(Object instance)
    {
        System.out.println(GraphLayout.parseInstance(instance).toFootprint());
    }

    public static void outputClassLayoutPrintable(Object instance)
    {
        System.out.println(ClassLayout.parseInstance(instance).toPrintable());
    }
}
