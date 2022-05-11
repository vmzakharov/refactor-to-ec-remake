package refactortoec.donutshop;

public class DonutShopRunner
extends DonutShopTestAbstract
{
    private DonutShopEc donutShop;

    @Override
    public void setupShop()
    {
        this.donutShop = new DonutShopEc();
    }

    public static void main(String[] args)
    {
        DonutShopRunner runner = new DonutShopRunner();
        runner.setup();

        System.out.println(runner.donutShop.orders().size());
    }

    @Override
    public DonutShop donutShop()
    {
        return this.donutShop;
    }
}
