package refactortoec.donutshop;

import org.junit.jupiter.api.BeforeEach;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

abstract public class DonutShopTestAbstract
{
    private final Clock clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);
    private final LocalDate today = LocalDate.now(this.clock);
    private final LocalDate tomorrow = this.today.plusDays(1);
    private final LocalDate yesterday = this.today.minusDays(1);

    @BeforeEach
    public void setup()
    {
        this.setupShop();
        this.setupCustomers();
        this.setupDonuts();
        this.setupOrders();
    }

    abstract public void setupShop();

    abstract public DonutShop donutShop();

    private void setupCustomers()
    {
        this.donutShop().addCustomer(new Customer(1, "Alice", "946 Irving Road", "NY"));
        this.donutShop().addCustomer(new Customer(2, "Bob", "381 Peaceful Lane", "MN"));
        this.donutShop().addCustomer(new Customer(3, "Carol", "4190 Golden Ridge Road", "CA"));
        this.donutShop().addCustomer(new Customer(4, "Dave", "2676 Hillcrest Circle", "MN"));
    }

    private void setupDonuts()
    {
        this.donutShop().addDonut(new Donut("BB", "Blueberry", 2.00, 1.75));
        this.donutShop().addDonut(new Donut("GL", "Glazed", 1.50, 1.25));
        this.donutShop().addDonut(new Donut("OF", "Old Fashioned", 1.50, 1.25));
        this.donutShop().addDonut(new Donut("PS", "Pumpkin Spice", 2.00, 1.75));
        this.donutShop().addDonut(new Donut("JL", "Jelly", 2.00, 1.75));
        this.donutShop().addDonut(new Donut("AC", "Apple Cider", 1.50, 1.25));
    }

    private void setupOrders()
    {
        this.addOrder(1, this.today, List.of(new OrderItem("BB", 2), new OrderItem("GL", 4), new OrderItem("OF", 10)));

        this.addOrder(2, this.yesterday, List.of(new OrderItem("BB", 12)));
        this.addOrder(2, this.today, List.of(new OrderItem("BB", 12)));

        this.addOrder(3, this.yesterday, List.of(new OrderItem("OF", 1), new OrderItem("JL", 2)));
        this.addOrder(3, this.tomorrow, List.of(new OrderItem("OF", 10), new OrderItem("GL", 1), new OrderItem("PS", 12)));

        this.addOrder(4, this.yesterday, List.of(new OrderItem("PS", 2)));
        this.addOrder(4, this.today, List.of(new OrderItem("OF", 12)));
        this.addOrder(4, this.tomorrow, List.of(new OrderItem("OF", 10)));
    }

    private void addOrder(int customerId, LocalDate deliveryDate, List<OrderItem> items)
    {
        this.donutShop().addOrder(new Order(customerId, deliveryDate, items));
    }

    protected Customer customer(String name)
    {
        return this.donutShop().findCustomerByName(name);
    }

    public LocalDate today()
    {
        return this.today;
    }

    public LocalDate tomorrow()
    {
        return this.tomorrow;
    }

    public LocalDate yesterday()
    {
        return this.yesterday;
    }
}
