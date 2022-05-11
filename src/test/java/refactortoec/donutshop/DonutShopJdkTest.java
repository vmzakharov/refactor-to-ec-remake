package refactortoec.donutshop;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DonutShopJdkTest
extends DonutShopTestAbstract
{
    private DonutShopJdk donutShop;

    @Override
    public void setupShop()
    {
        this.donutShop = new DonutShopJdk();
    }

    @Override
    public DonutShop donutShop()
    {
        return this.donutShop;
    }

    @Test
    public void findCustomerByName()
    {
       assertEquals("Alice", this.donutShop.findCustomerByName("Alice").name());
       assertNull(this.donutShop.findCustomerByName("Rupert"));
    }

    @Test
    public void customersByState()
    {
        Map<String, Set<Customer>> customersByState =
            this.donutShop.customers()
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
    public void customerNamesWithDeliveriesTomorrow()
    {
        Set<Customer> tomorrowsDeliveries = this.donutShop.orders()
                .stream()
                .filter(order -> order.deliveryDate() == this.tomorrow())
                .map(Order::customerId)
                .map(id -> this.donutShop.findCustomerById(id))
                .collect(Collectors.toSet());

        assertEquals(
                Set.of(this.customer("Carol"), this.customer("Dave")),
                tomorrowsDeliveries);
    }

    @Test
    public void orderPriceStatisticsForDateRanges()
    {
        var stats1 = this.donutShop.orderPriceStatistics(this.yesterday(), this.yesterday());
        assertEquals(30.5, stats1.getSum());
        assertEquals(7.625, stats1.getAverage());
        assertEquals(4, stats1.getCount());
        assertEquals(1.5, stats1.getMin());
        assertEquals(21.0, stats1.getMax());

        var stats2 = this.donutShop.orderPriceStatistics(this.yesterday(), this.tomorrow());
        assertEquals(136.5, stats2.getSum());
        assertEquals(10.5, stats2.getAverage());
        assertEquals(13, stats2.getCount());
        assertEquals(1.5, stats2.getMin());
        assertEquals(21.0, stats2.getMax());
    }

    @Test
    public void donutsInPopularityOrder()
    {
        Map<String, Integer> donutCounts = this.donutShop
                .orders()
                .stream()
                .flatMap(order -> order.items().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::donutCode,
                        Collectors.summingInt(OrderItem::count)
                ));

        List<String> popularity = donutCounts
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .map(this.donutShop::findDonutByCode)
                .map(Donut::description)
                .toList();

        assertEquals(
                List.of("Old Fashioned", "Blueberry", "Pumpkin Spice", "Glazed", "Jelly"),
                popularity);
    }

    @Test
    public void countTheMoney()
    {
        double totalRevenue = this.donutShop
                .orders()
                .stream()
                .flatMap(order -> order.items().stream())
                .mapToDouble(orderItem -> this.donutShop.getTotalPrice(orderItem.donutCode(), orderItem.count()))
                .sum();

        assertEquals(136.5, totalRevenue);
    }

    @Test
    public void countTheMoney2()
    {
        double totalRevenue = this.donutShop
                .orders()
                .stream()
                .flatMap(order -> order.items().stream())
                .collect(Collectors.summingDouble(
                        orderItem -> this.donutShop.getTotalPrice(orderItem.donutCode(), orderItem.count()))
                );

        assertEquals(136.5, totalRevenue);
    }
}
