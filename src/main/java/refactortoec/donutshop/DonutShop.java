package refactortoec.donutshop;

import java.time.LocalDate;
import java.util.DoubleSummaryStatistics;
import java.util.Map;

public interface DonutShop
{
    void addCustomer(Customer newCustomer);
    void addOrder(Order newOrder);
    void addDonut(Donut newDonut);

    Customer findCustomerByName(String name);
    Customer findCustomerById(int id);

    DoubleSummaryStatistics orderPriceStatistics(LocalDate fromDate, LocalDate toDate);

    default double getTotalPrice(String donutCode, int donutCount)
    {
        Donut donut = this.findDonutByCode(donutCode);
        return  donutCount * (donutCount < 10 ? donut.price() : donut.discountPrice());
    }

    default Donut findDonutByCode(String donutCode)
    {
        return this.donutsByCode().get(donutCode);
    }

    Map<String, Donut> donutsByCode();
}
