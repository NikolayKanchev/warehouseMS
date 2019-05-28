package warehouseMS.users.behaviours;

import warehouseMS.items.Order;
import warehouseMS.items.Box;
import warehouseMS.items.Item;
import warehouseMS.server.ManagementSystem;
import warehouseMS.users.Customer;
import warehouseMS.users.User;

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class CustomerWorkBehaviour implements WorkBehaviour
{
    private ManagementSystem ms;
    private transient Socket socket;

    @Override
    public void work(Socket socket, ManagementSystem ms, User user)
    {
        this.ms = ms;
        this.socket = socket;

        try
        {
            mainMenu((Customer) user);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    private void mainMenu(Customer customer) throws IOException
    {
        while (true)
        {
            ms.sendMessage(
                    "Manage Orders: \n" +
                            "\t 1. Create order \n" +
                            "\t 2. Delete order \n" +
                            "Check Warehouse \n" +
                            "\t 3. See not sent orders \n" +
                            "\t 4. See sent orders \n" +
                            "\t 5. See what you have in the warehouse \n" +
                            "\t 6. See number of items in the warehouse \n" +
                            "\t 7. See total amount of all items in the warehouse \n" +
                            "\t 8. Save and exit \n"
                    , socket);

            int input = ms.validateChoice(8, socket);

            switch (input)
            {
                case 1:
                    createOrder(customer);
                    break;
                case 2:
                    deleteOrder(customer);
                    break;
                case 3:
                    sendOrders(customer, "Not sent");
                    break;
                case 4:
                    sendOrders(customer, "Sent");
                    break;
                case 5:
                    checkWarehouse(customer);
                    break;
                case 6:
                    totalNumItems(customer);
                    break;
                case 7:
                    totalAmount(customer);
                    break;
                case 8:
                    ms.saveAndExit(socket);
            }
        }
    }

    private void totalAmount(Customer customer)
    {
        ms.sendMessage("***************************************************\n" +
                "Total amount of all items in the warehouse: " + customer.getTotalOfAllItems() +
                "\n***************************************************\n", socket);
    }

    private void totalNumItems(Customer customer)
    {
        ms.sendMessage("*********************************************\n" +
                "Total number of items in the warehouse: " + customer.getNumItems() +
                "\n*********************************************\n", socket);
    }

    private void createOrder(Customer customer) throws IOException
    {
        Order o = new Order();
        Scanner scanner = new Scanner(socket.getInputStream());

        printOrder(o);

        boolean itemName;

        do {
            ms.sendMessage("Add new row or type 'done' when done !", socket);

            itemName = addNewRow(scanner, customer, o);

        }while (itemName);

        printOrder(o);

        customer.addOrder(o);

        mainMenu(customer);
    }

    private boolean addNewRow(Scanner scanner, Customer customer, Order o)
    {
        int itemQuantity;
        String itemName;

        do
        {
            ms.sendMessage("Type item name: ", socket);
            itemName = scanner.nextLine();

            if (itemName.equals("done"))
            {
                return false;
            }

            ms.sendMessage("Enter quantity: ", socket);
            itemQuantity = Integer.parseInt(scanner.nextLine());

        }while (checkAvailability(itemName, itemQuantity, customer));

        for (int i = 0; i < itemQuantity; i++)
        {
            Item item = customer.getItemByName(itemName);

            if(item instanceof Box)
            {
                Item it = ((Box) item).getItemByName(itemName);
                o.addItem(it);
                ((Box) item).removeItem(it);
            }else
            {
                o.addItem(item);
                customer.removeItem(item);
            }
        }
        return true;
    }

    private void deleteOrder(Customer customer)
    {
        Scanner scanner = null;

        try
        {
            scanner = new Scanner(socket.getInputStream());

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        int orderNum = sentOrders(customer);

        if (orderNum == 0)
        {
            ms.sendMessage("**********************************\nYou don't have any orders !" +
                    "\n**********************************\n", socket);
            try
            {
                Thread.sleep(2000);

            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }else
        {
            boolean orderWasDeleted;
            do
            {
                ms.sendMessage("Which order do you want to delete? ", socket);

                assert scanner != null;
                String input = scanner.nextLine();

                orderWasDeleted = customer.deleteOrder(input);

                if (orderWasDeleted)
                {
                    ms.sendMessage("*****************************************\nThe order " + input +
                            " was deleted successfully !!!\n*****************************************\n", socket);
                } else
                {
                    ms.sendMessage("**********************************\nThe order you are trying to delete doesn't exist !!!\n**********************************\n", socket);
                }
            }while (!orderWasDeleted);
        }

        sentOrders(customer);

        try
        {
            mainMenu(customer);

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private void checkWarehouse(Customer customer)
    {
        String availableItemsStr = "";
        String notAvailableItemsStr = "";
        List<Item> availableItems = customer.getItems();
        List<Item> notAvailableItems = customer.getNotAvailableItems();
        Set<String> availableItemsSet = new HashSet<>();
        Set<String> notAvailableItemsSet = new HashSet<>();

        for (Item i: availableItems)
        {
            availableItemsSet.addAll(i.getUniqueItemsNames());
        }

        for (String name : availableItemsSet)
        {
            availableItemsStr += name + " - " + customer.getItemsQuantity(name) + " pcs.\n";
        }

        for (Item i: notAvailableItems)
        {
            notAvailableItemsSet.addAll(i.getUniqueItemsNames());
        }

        for (String name : notAvailableItemsSet)
        {
            notAvailableItemsStr += name + " - " + customer.getNotAvailableItemsQuantity(name) + " pcs.\n";
        }

        if (availableItemsStr.equals("") && notAvailableItemsStr.equals(""))
        {
            ms.sendMessage("*******************************************\n" +
                    "You don't have anything in the warehouse !!!\n*******************************************", socket);
        }else if (notAvailableItemsStr.equals(""))
        {
            ms.sendMessage("List of all you have in the warehouse " +
                    "\n*****************************************\nAvailable items:\n********************\n" + availableItemsStr +
                    "\n*****************************************\n", socket);
        }else
        {
            ms.sendMessage("List of all you have in the warehouse " +
                    "\n*****************************************\nAvailable items:\n********************\n" + availableItemsStr +
                    "\nNot available items:\n********************\n" + notAvailableItemsStr +
                    "\n*****************************************\n", socket);
        }
    }

    private boolean checkAvailability(String name, int number, Customer customer)
    {

        int availableItems = customer.getItemsQuantity(name);

        if (availableItems < number)
        {
            ms.sendMessage(
                    "**************** There are only " + availableItems + " pcs. available of this kind ! ************\n" +
                            "**************** Please try again with correct number of items !************", socket);
        }

        return availableItems < number;
    }

    private void printOrder(Order o)
    {
        ms.sendMessage(o.toString(), socket);
    }

    private int sentOrders(Customer customer)
    {
        String ordersStr = "";
        List<Order> ordersList = customer.getOrders();

        for (Order o: ordersList)
        {
            ordersStr += o.getNumberString() + "\n";
        }

        if(ordersList.size() != 0)
        {
            ms.sendMessage("******************\nYour orders: \n" + ordersStr + "*******************\n", socket);
        }

        return ordersList.size();
    }

    private void sendOrders(Customer customer, String status)
    {
        List<Order> orders;

        if (status.equals("Sent"))
        {
           orders = customer.getSentOrders();
        }else
        {
            orders = customer.getNotSentOrders();
        }

        String searchedOrders = "";

        for (Order o: orders)
        {
            if (o.getStatus().equals(status))
            {
                searchedOrders += o.getNumberString() + "\n";
            }
        }

        if (!searchedOrders.equals(""))
        {
            String message = status + " orders: \n*******************\n";

            ms.sendMessage( message + searchedOrders + "\n*******************\n", socket);
        }else
        {
            ms.sendMessage("***************************************\n No orders !!!\n***************************************\n", socket);
        }
    }
}
