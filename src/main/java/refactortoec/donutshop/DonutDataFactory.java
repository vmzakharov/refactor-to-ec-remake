package refactortoec.donutshop;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public class DonutDataFactory
{
    static private final Clock CLOCK = Clock.fixed(Instant.now(), ZoneOffset.UTC);
    static public final LocalDate TODAY = LocalDate.now(CLOCK);
    static public final LocalDate TOMORROW = TODAY.plusDays(1);
    static public final LocalDate YESTERDAY = TODAY.minusDays(1);

    public static void addCustomers(DonutShop donutShop)
    {
        donutShop.addCustomer(new Customer(1, "Alice", "946 Irving Road", "NY"));
        donutShop.addCustomer(new Customer(2, "Bob", "381 Peaceful Lane", "MN"));
        donutShop.addCustomer(new Customer(3, "Carol", "4190 Golden Ridge Road", "CA"));
        donutShop.addCustomer(new Customer(4, "Dave", "2676 Hillcrest Circle", "MN"));
    }

    public static void addDonuts(DonutShop donutShop)
    {
        donutShop.addDonut(new Donut("BB", "Blueberry", 2.00, 1.75));
        donutShop.addDonut(new Donut("GL", "Glazed", 1.50, 1.25));
        donutShop.addDonut(new Donut("OF", "Old Fashioned", 1.50, 1.25));
        donutShop.addDonut(new Donut("PS", "Pumpkin Spice", 2.00, 1.75));
        donutShop.addDonut(new Donut("JL", "Jelly", 2.00, 1.75));
        donutShop.addDonut(new Donut("AC", "Apple Cider", 1.50, 1.25));
    }

    public static void addOrders(DonutShop donutShop)
    {
        donutShop.addOrder(new Order(1, TODAY, List.of(new OrderItem("BB", 2), new OrderItem("GL", 4), new OrderItem("OF", 10))));

        donutShop.addOrder(new Order(2, YESTERDAY, List.of(new OrderItem("BB", 12))));
        donutShop.addOrder(new Order(2, TODAY, List.of(new OrderItem("BB", 12))));

        donutShop.addOrder(new Order(3, YESTERDAY, List.of(new OrderItem("OF", 1), new OrderItem("JL", 2))));
        donutShop.addOrder(new Order(3, TOMORROW, List.of(new OrderItem("OF", 10), new OrderItem("GL", 1), new OrderItem("PS", 12))));

        donutShop.addOrder(new Order(4, YESTERDAY, List.of(new OrderItem("PS", 2))));
        donutShop.addOrder(new Order(4, TODAY, List.of(new OrderItem("OF", 12))));
        donutShop.addOrder(new Order(4, TOMORROW, List.of(new OrderItem("OF", 10))));
    }
}
