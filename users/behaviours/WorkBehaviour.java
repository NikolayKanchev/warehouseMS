package warehouseMS.users.behaviours;

import warehouseMS.server.ManagementSystem;
import warehouseMS.users.User;

import java.io.Serializable;
import java.net.Socket;

public interface WorkBehaviour extends Serializable
{
    void work(Socket socket, ManagementSystem ms, User user);
}
