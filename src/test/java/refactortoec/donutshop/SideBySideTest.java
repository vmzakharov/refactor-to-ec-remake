package refactortoec.donutshop;

import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.factory.Bags;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.set.MutableSetMultimap;
import org.eclipse.collections.api.tuple.primitive.ObjectIntPair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Multimaps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static refactortoec.donutshop.DonutDataFactory.TOMORROW;

public class SideBySideTest
{
    private DonutShopJdk donutShopJdk;
    private DonutShopEc donutShopEc;

    @BeforeEach
    public void configureShops()
    {
        this.donutShopJdk = new DonutShopJdk();

        DonutDataFactory.addCustomers(this.donutShopJdk);
        DonutDataFactory.addDonuts(this.donutShopJdk);
        DonutDataFactory.addOrders(this.donutShopJdk);

        this.donutShopEc = new DonutShopEc();

        DonutDataFactory.addCustomers(this.donutShopEc);
        DonutDataFactory.addDonuts(this.donutShopEc);
        DonutDataFactory.addOrders(this.donutShopEc);
    }

    private Customer customer(String name)
    {
        return this.donutShopJdk.findCustomerByName(name);
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
    public void customersByStateEc()
    {
        MutableSetMultimap<String, Customer> customersByState = this.donutShopEc.customers()
                .groupBy(Customer::state, Multimaps.mutable.set.of());

        MutableSetMultimap<String, Customer> expected = Multimaps.mutable.set.of();
        expected.put("NY", this.customer("Alice"));
        expected.put("CA", this.customer("Carol"));
        expected.put("MN", this.customer("Bob"));
        expected.put("MN", this.customer("Dave"));

        assertEquals(expected, customersByState);
    }

    @Test
    public void customerNamesWithDeliveriesTomorrowJdk()
    {
        Set<Customer> tomorrowsDeliveries = this.donutShopJdk.orders()
                                     .stream()
                                     .filter(order -> order.deliveryDate() == TOMORROW)
                                     .map(Order::customerId)
                                     .map(this.donutShopJdk::findCustomerById)
                                     .collect(Collectors.toSet());

        assertEquals(
                Set.of(this.customer("Carol"), this.customer("Dave")),
                tomorrowsDeliveries);
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
    public void top3SellersEc()
    {
        MutableBag<String> donutCounts = Bags.mutable.of();

        this.donutShopEc
                .orders()
                .flatCollect(Order::items)
                .forEach(orderItem -> donutCounts.addOccurrences(orderItem.donutCode(), orderItem.count()));

        MutableList<String> popular = donutCounts
                .topOccurrences(3)
                .collect(ObjectIntPair::getOne)
                .collect(code -> donutShopEc.findDonutByCode(code).description());

        assertEquals(
                Lists.immutable.of("Old Fashioned", "Blueberry", "Pumpkin Spice"),
                popular);
    }
}
