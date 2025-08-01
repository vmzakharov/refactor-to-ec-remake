package refactortoec.donutshop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.GraphLayout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static refactortoec.donutshop.DonutDataFactory.TOMORROW;
import static refactortoec.donutshop.DonutDataFactory.YESTERDAY;
import static refactortoec.donutshop.DonutDataFactory.addCustomers;
import static refactortoec.donutshop.DonutDataFactory.addDonuts;
import static refactortoec.donutshop.DonutDataFactory.addOrders;

public class DonutShopJdkTest
{
    private DonutShopJdk donutShopJdk;

    @BeforeEach
    public void setup()
    {
        this.setupShop();

        addCustomers(donutShopJdk);
        addDonuts(donutShopJdk);
        addOrders(donutShopJdk);
    }

    public void setupShop()
    {
        this.donutShopJdk = new DonutShopJdk();
    }

    private Customer customer(String name)
    {
        return this.donutShopJdk.findCustomerByName(name);
    }

    @Test
    public void findCustomerByNameJdk()
    {
       assertEquals("Alice", this.donutShopJdk.findCustomerByName("Alice").name());
       assertNull(this.donutShopJdk.findCustomerByName("Rupert"));
    }

    @Test
    public void customersByStateJdk()
    {
        Map<String, Set<Customer>> customersByState =
            this.donutShopJdk.customers()
                             .stream()
                             .collect(
                       Collectors.groupingBy(
                               Customer::state,
                               Collectors.mapping(Function.identity(), Collectors.toSet())
                       )
               );

        Map<String, Set<Customer>> expected = new HashMap<>();
        expected.put("NY", Set.of(this.customer("Alice")));
        expected.put("CA", Set.of(this.customer("Carol")));
        expected.put("MN", Set.of(this.customer("Bob"), this.customer("Dave")));

        assertEquals(expected, customersByState);
    }

    @Test
    public void customerNamesWithDeliveriesTomorrowJdk()
    {
        Set<Customer> tomorrowsDeliveries = this.donutShopJdk.orders()
            .stream()
            .filter(order -> order.deliveryDate() == TOMORROW)
            .map(Order::customerId)
            .map(id -> this.donutShopJdk.findCustomerById(id))
            .collect(Collectors.toSet());

        assertEquals(
                Set.of(this.customer("Carol"), this.customer("Dave")),
                tomorrowsDeliveries);
    }

    @Test
    public void orderPriceStatisticsForDateRangesJdk()
    {
        var stats1 = this.donutShopJdk.orderPriceStatistics(YESTERDAY, YESTERDAY);
        assertEquals(30.5, stats1.getSum());
        assertEquals(7.625, stats1.getAverage());
        assertEquals(4, stats1.getCount());
        assertEquals(1.5, stats1.getMin());
        assertEquals(21.0, stats1.getMax());

        var stats2 = this.donutShopJdk.orderPriceStatistics(YESTERDAY, TOMORROW);
        assertEquals(136.5, stats2.getSum());
        assertEquals(10.5, stats2.getAverage());
        assertEquals(13, stats2.getCount());
        assertEquals(1.5, stats2.getMin());
        assertEquals(21.0, stats2.getMax());
    }

    @Test
    public void top3SellersJdk()
    {
        Map<String, Integer> donutCounts = this.donutShopJdk
                .orders()
                .stream()
                .flatMap(order -> order.items().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::donutCode,
                        Collectors.summingInt(OrderItem::count)
                ));

        List<String> popular = donutCounts
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .map(code -> this.donutShopJdk.findDonutByCode(code).description())
                .toList();

        assertEquals(
                List.of("Old Fashioned", "Blueberry", "Pumpkin Spice"),
                popular);
    }

    @Test
    public void countTheMoneyJdk()
    {
        double totalRevenue = this.donutShopJdk
                .orders()
                .stream()
                .flatMap(order -> order.items().stream())
                .mapToDouble(orderItem -> this.donutShopJdk.getTotalPrice(orderItem.donutCode(), orderItem.count()))
                .sum();

        assertEquals(136.5, totalRevenue);
    }

    @Test
    public void countTheMoney2jdk()
    {
        double totalRevenue = this.donutShopJdk
                .orders()
                .stream()
                .flatMap(order -> order.items().stream())
                .collect(Collectors.summingDouble(
                        orderItem -> this.donutShopJdk.getTotalPrice(orderItem.donutCode(), orderItem.count()))
                );

        assertEquals(136.5, totalRevenue);
    }

    @Test
    public void toFootprint()
    {
        this.donutShopJdk.donutsByCode();
        // donutShopEc (3000)
        System.out.println(GraphLayout.parseInstance(this.donutShopJdk).toFootprint());
    }
}
