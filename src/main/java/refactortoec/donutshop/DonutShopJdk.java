package refactortoec.donutshop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DonutShopJdk
implements DonutShop
{
    private final List<Order> orders = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final List<Donut> donuts = new ArrayList<>();

    private Map<String, Donut> donutsByCode;

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
                .stream()
                .filter(customer -> customer.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Customer findCustomerById(int id)
    {
        return this.customers
                .stream()
                .filter(customer -> customer.id() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public DoubleSummaryStatistics orderPriceStatistics(LocalDate fromDate, LocalDate toDate)
    {
        return this.orders
                .stream()
                .filter(order -> order.betweenInclusive(fromDate, toDate))
                .flatMap(order -> order.items().stream())
                .collect(
                    Collectors.summarizingDouble(
                        orderItem -> this.getTotalPrice(orderItem.donutCode(), orderItem.count())
                    )
                );
    }

    @Override
    public Map<String, Donut> donutsByCode()
    {
        if (this.donutsByCode == null)
        {
            this.donutsByCode = this.donuts
                .stream()
                .collect(Collectors.toMap(Donut::code, Function.identity()));
        }

        return this.donutsByCode;
    }

    public List<Order> orders()
    {
        return this.orders;
    }

    public List<Customer> customers()
    {
        return this.customers;
    }

    public List<Donut> donuts()
    {
        return this.donuts;
    }
}
