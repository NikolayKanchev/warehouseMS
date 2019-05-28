package warehouseMS.server;

import warehouseMS.items.Order;
import warehouseMS.users.Customer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Server
{
    public static void main(String[] args)
    {
        ServerSocket serverSocket;

        ManagementSystem ms = Serializer.getManagementSystem();

        setOrderHighestId(ms);

        System.out.println("The Server is running...................\n");

        try
        {
            serverSocket = new ServerSocket(5555, 100);

            while (true)
            {
                Socket socket = serverSocket.accept();

                ReceiveSend receiveSend = new ReceiveSend(socket, ms);

                receiveSend.start();

                Thread.sleep(500);

                System.out.println("\nWaiting for clients......................\n");
            }
        }catch (InterruptedException | IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void setOrderHighestId(ManagementSystem ms)
    {
        List<Customer> customers = ms.getCustomers();
        Collection<Integer> numbers = new ArrayList<>();

        for (Customer customer : customers)
        {
            if (customer.getOrders().size() != 0)
            {
                Order lastOrder = customer.getOrders().get(customer.getOrders().size() - 1);
                numbers.add(lastOrder.getNumber());
            }
        }

        if (!numbers.isEmpty())
        {
            int highestOrderNumber = Collections.max(numbers);
            Order.setID(highestOrderNumber);
        }

    }
}
