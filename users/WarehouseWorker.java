package warehouseMS.users;
import warehouseMS.users.behaviours.WarehouseWorkerBehaviour;

public class WarehouseWorker extends User
{
    private int completedOrders;

    public WarehouseWorker(String name, String address, String phone)
    {
        super(name, address, phone);
        setWorkBehaviour(new WarehouseWorkerBehaviour());
        completedOrders = 0;
    }

    public int getCompletedOrders()
    {
        return completedOrders;
    }

    public void addOneToCompletedOrders()
    {
        this.completedOrders += 1;
    }
}
