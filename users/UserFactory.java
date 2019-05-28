package warehouseMS.users;

public class UserFactory
{
    public User createUser(String userType, String name, String address, String phone)
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
