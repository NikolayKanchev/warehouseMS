package warehouseMS.users;

import warehouseMS.server.ManagementSystem;
import warehouseMS.users.behaviours.WorkBehaviour;
import java.io.Serializable;
import java.net.Socket;

public abstract class User implements Serializable
{
    private String name;
    private String address;
    private String phoneNumber;
    private WorkBehaviour workBehaviour;

    public User(String name, String address, String phone)
    {
        this.name = name;
        this.address = address;
        this.phoneNumber = phone;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPhone()
    {
        return phoneNumber;
    }

    public void setPhone(String phone)
    {
        this.phoneNumber = phone;
    }

    public void setWorkBehaviour(WorkBehaviour workBehaviour)
    {
        this.workBehaviour = workBehaviour;
    }

    public void work(Socket socket, ManagementSystem ms)
    {
        this.workBehaviour.work(socket, ms, this);
    }

    @Override
    public String toString()
    {
        return name;
    }
}
