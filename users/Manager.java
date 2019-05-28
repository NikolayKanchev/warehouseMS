package warehouseMS.users;
import warehouseMS.users.behaviours.ManagerWorkBehaviour;

public class Manager extends User
{

    public Manager(String name, String address, String phone)
    {
        super(name, address, phone);
        setWorkBehaviour(new ManagerWorkBehaviour());
    }
}
