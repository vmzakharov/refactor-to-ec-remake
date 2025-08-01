package refactortoec.donutshop;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.block.factory.Functions;

import java.time.LocalDate;
import java.util.DoubleSummaryStatistics;
import java.util.Map;

public class DonutShopEc
implements DonutShop
{
    private final MutableList<Order> orders = Lists.mutable.empty();
    private final MutableList<Customer> customers = Lists.mutable.empty();
    private final MutableList<Donut> donuts = Lists.mutable.empty();

    private MutableMap<String, Donut> donutsByCode = null;

    @Override
    public void addCustomer(Customer newCustomer)
    {
        this.customers.add(newCustomer);
    }

    @Override
    public void addOrder(Order newOrder)
    {
        this.orders.add(newOrder);
    }

    @Override
    public void addDonut(Donut newDonut)
    {
        this.donuts.add(newDonut);
    }

    @Override
    public Customer findCustomerByName(String name)
    {
        return this.customers
                .detectIfNone(customer -> customer.name().equals(name), () -> null);
    }

    @Override
    public Customer findCustomerById(int id)
    {
        return this.customers
                .detectIfNone(customer -> customer.id() == id, () -> null);
    }

    @Override
    public DoubleSummaryStatistics orderPriceStatistics(LocalDate fromDate, LocalDate toDate)
    {
        return this.orders
                .select(order -> order.betweenInclusive(fromDate, toDate))
                .flatCollect(Order::items)
                .collectDouble(orderItem -> this.getTotalPrice(orderItem.donutCode(), orderItem.count()))
                .summaryStatistics();
    }

    @Override
    public Map<String, Donut> donutsByCode()
    {
        if (this.donutsByCode == null)
        {
            this.donutsByCode = this.donuts().groupByUniqueKey(Donut::code);
        }
        return this.donutsByCode;
    }

    public MutableList<Order> orders()
    {
        return this.orders;
    }

    public MutableList<Customer> customers()
    {
        return this.customers;
    }

    public MutableList<Donut> donuts()
    {
        return this.donuts;
    }
}
