package warehouseMS.server;

import warehouseMS.items.Order;
import warehouseMS.users.Customer;
import warehouseMS.users.User;
import warehouseMS.users.WarehouseWorker;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ManagementSystem implements Serializable
{
    private static final String userTypes = "Manager, Customer, Worker";
    private List<User> users = new ArrayList<>();

    public void addUser(User user)
    {
        users.add(user);
    }

    public void removeUser(User user)
    {
        users.remove(user);
    }

    User getUser(String name)
    {
        for (User u : users)
        {
            if(u.getName().equals(name))
            {
                return u;
            }
        }

        return null;
    }

    public void sendMessage(String message, Socket socket)
    {
        try
        {
            Writer writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            writer.write(message +"\r\n");

            writer.flush();

        } catch (IOException e)
        {
            System.out.println(("The connection to " + socket.getInetAddress().getHostAddress() + " failed"));
        }

    }

    public String getReceivedString(Socket socket)
    {
        String str;

        try {

            Scanner scanner = new Scanner(socket.getInputStream());

            str = scanner.nextLine();

            System.out.println("Server received -  <<<" + str + ">>> from : " +
                    socket.getInetAddress().getHostAddress() + " : " + socket.getPort());

        } catch (IOException | NoSuchElementException e) {

            e.printStackTrace();

            str = "";
        }

        if(str.equals(""))
        {
            sendMessage("You have to type something !", socket);
        }

        return str;
    }

    public int validateChoice(int numOptions, Socket socket)
    {
        Scanner scanner = null;
        try
        {
            scanner = new Scanner(socket.getInputStream());

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        int input;
        String message = "Choose: \n";

        do
        {
            try
            {
                sendMessage(message, socket);
                assert scanner != null;
                input = Integer.parseInt(scanner.nextLine());

            } catch (Exception ignored)
            {
                message = "Choose a valid option (1-" + numOptions + "):\n";
                input = 0;
            }
        }while (input <= 0 || input > numOptions);

        return input;
    }

    public List<User> getUsers()
    {
        return users;
    }

    public String getTypeOfUsers()
    {
        return userTypes;
    }

    public void saveAndExit(Socket socket)
    {
        Serializer.save(this);
        sendMessage("exit", socket);

        while (true)
        {
            try
            {
                Thread.sleep(5555);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public Order getOrder()
    {
        List<Order> orders = new ArrayList<>();

        for (User u: users)
        {
            if (u instanceof Customer)
            {
                orders.addAll(((Customer) u).getNotSentOrders());
                System.out.println(("************************" + orders.toString()));
            }
        }
        return (orders.size() != 0) ? orders.get(0) : null;
    }

    public Customer getCustomer(Order o)
    {
        for (User u: users)
        {
            if (u instanceof Customer)
            {
                if (((Customer) u).getOrders().contains(o))
                {
                    return (Customer) u;
                }
            }
        }

        return null;
    }

    public ArrayList<Customer> getCustomers()
    {
        ArrayList<Customer> customers = new ArrayList<>();

        List<User> users =  getUsers();

        for (User u: users)
        {
            if (u instanceof Customer)
            {
                customers.add((Customer) u);
            }
        }
        return customers;
    }

    public String getNumOfCompletedOrdersOfEachWorker()
    {
        String retVal = "Warehouse worker - done orders:\n---------------------------------\n";

        for (User u : users)
        {
            if (u instanceof WarehouseWorker)
            {
                retVal += u.getName() + " - " + ((WarehouseWorker)u).getCompletedOrders() + "\n";
            }
        }

        return retVal;
    }
}
