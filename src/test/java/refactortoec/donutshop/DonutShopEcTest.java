package refactortoec.donutshop;

import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.factory.Bags;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.set.MutableSetMultimap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.primitive.ObjectIntPair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Multimaps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.GraphLayout;
import refactortoec.generation.GenerationEc;
import refactortoec.generation.GenerationJdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static refactortoec.donutshop.DonutDataFactory.*;

public class DonutShopEcTest
{
    private DonutShopEc donutShopEc;

    @BeforeEach
    public void setup()
    {
        this.setupShop();

        addCustomers(this.donutShopEc);
        addDonuts(this.donutShopEc);
        addOrders(this.donutShopEc);
    }

    public void setupShop()
    {
        this.donutShopEc = new DonutShopEc();
    }

    private Customer customer(String name)
    {
        return this.donutShopEc.findCustomerByName(name);
    }

    @Test
    public void findCustomerByNameEc()
    {
        assertEquals("Alice", this.donutShopEc.findCustomerByName("Alice").name());
        assertNull(this.donutShopEc.findCustomerByName("Rupert"));

        assertEquals("Bob", this.customer("Bob").name());
        assertNull(this.customer("Peggy"));
    }

    @Test
    public void customersByStateEc()
    {
        MutableSetMultimap<String, Customer> customersByState =
                this.donutShopEc.customers().groupBy(Customer::state, Multimaps.mutable.set.of());

        MutableSetMultimap<String, Customer> expected = Multimaps.mutable.set.of();
        expected.put("NY", this.customer("Alice"));
        expected.put("CA", this.customer("Carol"));
        expected.put("MN", this.customer("Bob"));
        expected.put("MN", this.customer("Dave"));

        assertEquals(expected, customersByState);
    }

    @Test
    public void customerNamesWithDeliveriesTomorrowEc()
    {
        MutableSet<Customer> tomorrowsDeliveries = this.donutShopEc.orders()
                                                                   .select(order -> order.deliveryDate() == TOMORROW)
                                                                   .collect(Order::customerId)
                                                                   .collect(this.donutShopEc::findCustomerById)
                                                                   .toSet();

        assertEquals(
            Sets.immutable.of(this.customer("Carol"), this.customer("Dave")),
            tomorrowsDeliveries);
    }

    @Test
    public void orderPriceStatisticsForDateRangesEc()
    {
        var stats1 = this.donutShopEc.orderPriceStatistics(YESTERDAY, YESTERDAY);
        assertEquals(30.5, stats1.getSum());
        assertEquals(7.625, stats1.getAverage());
        assertEquals(4, stats1.getCount());
        assertEquals(1.5, stats1.getMin());
        assertEquals(21.0, stats1.getMax());

        var stats2 = this.donutShopEc.orderPriceStatistics(YESTERDAY, TOMORROW);
        assertEquals(136.5, stats2.getSum());
        assertEquals(10.5, stats2.getAverage());
        assertEquals(13, stats2.getCount());
        assertEquals(1.5, stats2.getMin());
        assertEquals(21.0, stats2.getMax());
    }

    @Test
    public void top3SellersEc()
    {
        MutableBag<String> donutCounts = Bags.mutable.of();

        this.donutShopEc
                .orders()
                .flatCollect(Order::items)
                .forEach(orderItem -> donutCounts.addOccurrences(orderItem.donutCode(), orderItem.count()));

        MutableList<String> popularity = donutCounts
                .topOccurrences(3)
                .collect(ObjectIntPair::getOne)
                .collect(code -> this.donutShopEc.findDonutByCode(code).description());

        assertEquals(
                Lists.immutable.of("Old Fashioned", "Blueberry", "Pumpkin Spice"),
                popularity);
    }

    @Test
    public void countTheMoneyEc()
    {
        double totalRevenue = this.donutShopEc
                .orders()
                .flatCollect(Order::items)
                .collectDouble(orderItem -> this.donutShopEc.getTotalPrice(orderItem.donutCode(), orderItem.count()))
                .sum();

        assertEquals(136.5, totalRevenue);
    }

    @Test
    public void countTheMoneyLazyEc()
    {
        double totalRevenue = this.donutShopEc
                .orders()
                .asLazy()
                .flatCollect(Order::items)
                .collectDouble(orderItem -> this.donutShopEc.getTotalPrice(orderItem.donutCode(), orderItem.count()))
                .sum();

        assertEquals(136.5, totalRevenue);
    }

    @Test
    public void toFootprint()
    {
        this.donutShopEc.donutsByCode();
        // donutShopEc (2840)
        System.out.println(GraphLayout.parseInstance(this.donutShopEc).toFootprint());
    }
}
