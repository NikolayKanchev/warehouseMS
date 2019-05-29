package warehouseMS.factories;

import warehouseMS.users.Customer;
import warehouseMS.users.Manager;
import warehouseMS.users.User;
import warehouseMS.users.WarehouseWorker;

public class UserFactory
{
    public User create(String userType, String name, String address, String phone)
    {
        switch (userType)
        {
            case "Manager":
                return new Manager(name, address, phone);
            case "Customer":
                return new Customer(name, address, phone);
            case "Worker":
                return new WarehouseWorker(name, address, phone);
            default:
                return null;
        }
    }
}
