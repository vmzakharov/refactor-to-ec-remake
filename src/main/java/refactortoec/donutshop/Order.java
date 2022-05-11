package refactortoec.donutshop;

import java.time.LocalDate;
import java.util.List;

public record Order(int customerId, LocalDate deliveryDate, List<OrderItem> items)
{
    public boolean betweenInclusive(LocalDate fromDate, LocalDate toDate)
    {
        return (this.deliveryDate.isAfter(fromDate) && this.deliveryDate.isBefore(toDate))
                || this.deliveryDate.isEqual(fromDate)
                || this.deliveryDate.isEqual(toDate);
    }
}
