package refactortoec.donutshop;

import org.eclipse.collections.api.bag.Bag;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.factory.Bags;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.set.MutableSetMultimap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.api.tuple.primitive.ObjectIntPair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Multimaps;
import org.eclipse.collections.impl.factory.primitive.ObjectIntMaps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DonutShopEcTest
extends DonutShopTestAbstract
{
    private DonutShopEc donutShop;

    @Override
    public void setupShop()
    {
        this.donutShop = new DonutShopEc();
    }

    @Override
    public DonutShop donutShop()
    {
        return this.donutShop;
    }

    @Test
    public void findCustomerByNameEc()
    {
        assertEquals("Alice", this.donutShop.findCustomerByName("Alice").name());
        assertNull(this.donutShop.findCustomerByName("Rupert"));

        assertEquals("Bob", this.customer("Bob").name());
        assertNull(this.customer("Peggy"));
    }

    @Test
    public void customersByStateEc()
    {
        MutableSetMultimap<String, Customer> customersByState =
                this.donutShop.customers().groupBy(Customer::state, Multimaps.mutable.set.of());

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
        MutableSet<Customer> tomorrowsDeliveries = this.donutShop.orders()
            .select(order -> order.deliveryDate() == this.tomorrow())
            .collect(Order::customerId)
            .collect(this.donutShop::findCustomerById)
            .toSet();

        assertEquals(
            Sets.immutable.of(this.customer("Carol"), this.customer("Dave")),
            tomorrowsDeliveries);
    }

    @Test
    public void orderPriceStatisticsForDateRangesEc()
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
    public void donutsInPopularityOrderEc()
    {
        MutableBag<String> donutCounts = Bags.mutable.of();

        this.donutShop
                .orders()
                .flatCollect(Order::items)
                .forEach(orderItem -> donutCounts.addOccurrences(orderItem.donutCode(), orderItem.count()));

        MutableList<String> popularity = donutCounts
                .topOccurrences(donutCounts.sizeDistinct())
                .collect(ObjectIntPair::getOne)
                .collect(this.donutShop::findDonutByCode)
                .collect(Donut::description);
        
        assertEquals(
                Lists.immutable.of("Old Fashioned", "Blueberry", "Pumpkin Spice", "Glazed", "Jelly"),
                popularity);
    }

    @Test
    public void countTheMoneyEc()
    {
        double totalRevenue = this.donutShop
                .orders()
                .flatCollect(Order::items)
                .collectDouble(orderItem -> this.donutShop.getTotalPrice(orderItem.donutCode(), orderItem.count()))
                .sum();

        assertEquals(136.5, totalRevenue);
    }

    @Test
    public void countTheMoneyLazyEc()
    {
        double totalRevenue = this.donutShop
                .orders()
                .asLazy()
                .flatCollect(Order::items)
                .collectDouble(orderItem -> this.donutShop.getTotalPrice(orderItem.donutCode(), orderItem.count()))
                .sum();

        assertEquals(136.5, totalRevenue);
    }
}
