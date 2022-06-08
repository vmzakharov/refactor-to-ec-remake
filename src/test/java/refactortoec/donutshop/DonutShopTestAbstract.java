package refactortoec.donutshop;

import org.junit.jupiter.api.BeforeEach;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

abstract public class DonutShopTestAbstract
{
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
        DonutDataFactory.addCustomers(this.donutShop());
    }

    private void setupDonuts()
    {
        DonutDataFactory.addDonuts(this.donutShop());
    }

    private void setupOrders()
    {
        DonutDataFactory.addOrders(this.donutShop());
    }

    protected Customer customer(String name)
    {
        return this.donutShop().findCustomerByName(name);
    }

    public LocalDate today()
    {
        return DonutDataFactory.TODAY;
    }

    public LocalDate tomorrow()
    {
        return DonutDataFactory.TOMORROW;
    }

    public LocalDate yesterday()
    {
        return DonutDataFactory.YESTERDAY;
    }
}
