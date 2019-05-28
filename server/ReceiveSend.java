package warehouseMS.server;
import warehouseMS.users.User;

import java.io.*;
import java.net.Socket;

public class ReceiveSend extends Thread
{
    private transient Socket socket;
    private ManagementSystem ms;

    ReceiveSend(Socket socket, ManagementSystem ms) throws FileNotFoundException
    {
        this.socket = socket;
        this.ms = ms;
    }

    @Override
    public void run()
    {
        User loggedUser;

        while (true)
        {
            if (socket.isClosed())
            {
                return;
            }

            ms.sendMessage("Type your username: ", socket);

            String received = ms.getReceivedString(socket);

            if (received.equals(""))
            {
                continue;
            }

            loggedUser = ms.getUser(received);

            if (loggedUser == null)
            {
                ms.sendMessage("User '" + received +
                        "' doesn't exist !", socket);
                continue;
            }

            break;
        }

        loggedUser.work(socket, ms);

    }
}

