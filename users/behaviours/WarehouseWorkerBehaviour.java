package warehouseMS.users.behaviours;

import warehouseMS.items.Order;
import warehouseMS.items.Box;
import warehouseMS.items.Item;
import warehouseMS.items.Product;
import warehouseMS.server.ManagementSystem;
import warehouseMS.users.Customer;
import warehouseMS.users.User;
import warehouseMS.users.WarehouseWorker;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class WarehouseWorkerBehaviour implements WorkBehaviour
{
    private ManagementSystem ms;
    private transient Socket socket;
    private Order currentOrder;

    @Override
    public void work(Socket socket, ManagementSystem ms, User user)
    {
        this.ms = ms;
        this.socket = socket;

        try
        {
            mainMenu(user);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void mainMenu(User user) throws IOException
    {
        mainMenuLoop: while (true)
        {
            ms.sendMessage(
                    "Menu: \n" +
                            "\t 1. Get order \n" +
                            "\t 2. Prepare order \n" +
                            "\t 3. Send order \n" +
                            "\t 4. Add delivered items \n" +
                            "\t 5. Save and exit \n"
                    , socket);

            int input = ms.validateChoice(6, socket);

            switch (input)
            {
                case 1:
                    if (getOrder() == null)
                    {
                        ms.sendMessage("********************************\n" +
                                "There are no orders at the moment !!!\n********************************\n", socket);
                    }else
                    {
                        currentOrder = getOrder();
                        ms.sendMessage(currentOrder.toString(), socket);
                    }
                    break;
                case 2:
                    prepareOrder();
                    break;
                case 3:
                    if (currentOrder != null && !currentOrder.getStatus().equals("Sent"))
                    {
                        if (!currentOrder.getStatus().equals("Ready for sending"))
                        {
                            ms.sendMessage( "******************************************\n" +
                                    "You have to prepare the order first !\n******************************************\n", socket);
                            mainMenu(user);
                        }

                        removeSentItems();
                        currentOrder.setStatus("Sent");
                        ((WarehouseWorker)user).addOneToCompletedOrders();
                        ms.sendMessage( "******************************************\n Order " +
                                currentOrder.getNumberString() + " have been sent successfully !" +
                                "\n******************************************\n", socket);
                        currentOrder = null;

                    }else if (currentOrder != null && currentOrder.getStatus().equals("Sent"))
                    {
                        currentOrder = null;

                    }else
                    {
                        ms.sendMessage("********************************\n" +
                                "You have to get an order first !\n********************************\n", socket);
                    }
                    break;
                case 4:
                    addDeliveredItems();
                    break;
                case 5:
                    ms.saveAndExit(socket);
                    break mainMenuLoop;
            }
        }
    }

    private void addDeliveredItems()
    {
        Customer customer = chooseCustomer();

        addLoop: while (true)
        {

            ms.sendMessage("1. Single item\n2. Box of items\n3. <-- Back", socket);

            int chosenOption = ms.validateChoice(3, socket);

            switch (chosenOption)
            {
                case 1:
                    customer.addItem(createItem());
                    break;
                case 2:
                    customer.addItem(createBoxOfItems());
                    break;
                case 3:
                    break addLoop;
            }
        }
    }

    private Box createBoxOfItems()
    {
        ms.sendMessage(
                "1. Box of the same items\n" +
                        "2. Box different items items\n"
                , socket);

        int userChoice = ms.validateChoice(2, socket);

        switch (userChoice)
        {
            case 1:
                return createBoxOfTheSameItems();
            case 2:
                return createBoxOfDifferentItems();
        }
        return new Box();
    }

    private Box createBoxOfDifferentItems()
    {
        Box box = new Box();

        createItemsLoop: while (true)
        {
            ms.sendMessage("1. Add items to the box\n2. <-- Back", socket);
            int userChoice = ms.validateChoice(2, socket);

            switch (userChoice)
            {
                case 1:
                    createItemsForTheBox(box);
                    break;
                case 2:
                    break createItemsLoop;
            }
        }

        return box;

    }

    private void createItemsForTheBox(Box box)
    {
        Item item = createItem();
        ms.sendMessage("How many pcs. of this item in the box?", socket);
        int itemsNum = getIntegerInput(socket);

        for (int i = 0; i < itemsNum; i++)
        {
            try
            {
                Item newCopy = (Item) item.clone();
                box.addItem(newCopy);

            } catch (CloneNotSupportedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private Box createBoxOfTheSameItems()
    {
        Box box = new Box();
        createItemsForTheBox(box);
        return box;
    }

    private Item createItem()
    {
        String name;
        Double price;

        ms.sendMessage("Type the name of the item:", socket);
        name = ms.getReceivedString(socket);

        while (true)
        {
            ms.sendMessage("Price:", socket);
            try
            {
                price = Double.parseDouble(ms.getReceivedString(socket));

                if (price > 0.0)
                {
                    break;
                }
            }catch (Exception ignored)
            {
                ms.sendMessage("Wrong decimal number ! Try again !", socket);
            }
        }

        Product item = new Product.Builder(name, price).build();

        ms.sendMessage("Do you want to set the rest of the item fields?\n1. yes\n2. no", socket);
        int userChoice = ms.validateChoice(2, socket);

        item.setBrand("");
        item.setModel("");
        item.setDescription("");
        item.setRack(0);
        item.setRackShelf(0);
        item.setSection(0);

        if (userChoice == 1)
        {
            moreOptions(item);
        }

        return item;
    }

    private void moreOptions(Product item)
    {
        optionsMenu: while (true)
        {
            ms.sendMessage(
                    "1. Brand\n" +
                            "2. Model\n" +
                            "3. Description\n" +
                            "4. Rack\n" +
                            "5. Rack shelf\n" +
                            "6. Section\n" +
                            "7. <-- Back\n"
                    , socket);

            int userChoice = ms.validateChoice(7, socket);

            switch (userChoice)
            {
                case 1:
                    ms.sendMessage("Brand: ", socket);
                    String brand = ms.getReceivedString(socket);
                    item.setBrand(brand);
                    break;
                case 2:
                    ms.sendMessage("Model: ", socket);
                    String model = ms.getReceivedString(socket);
                    item.setModel(model);
                    break;
                case 3:
                    ms.sendMessage("Description: ", socket);
                    String description = ms.getReceivedString(socket);
                    item.setDescription(description);
                    break;
                case 4:
                    ms.sendMessage("Rack: ", socket);
                    int rack = getIntegerInput(socket);
                    item.setRack(rack);
                    break;
                case 5:
                    ms.sendMessage("Rack shelf: ", socket);
                    int rackShelf = getIntegerInput(socket);
                    item.setRackShelf(rackShelf);
                    break;
                case 6:
                    ms.sendMessage("Section: ", socket);
                    int section = getIntegerInput(socket);
                    item.setRackShelf(section);
                    break;
                case 7:
                    break optionsMenu;
            }
        }
    }

    private int getIntegerInput(Socket socket)
    {
        String received = ms.getReceivedString(socket);
        int choice = 0;

        try
        {
            choice = Integer.parseInt(received);

        }catch (Exception e)
        {
            ms.sendMessage("This is not a valid number! Try again!", socket);
        }

        if (choice < 1)
        {
            ms.sendMessage("The number should be greater than 0 !!!", socket);
            getIntegerInput(socket);
        }

        return choice;
    }

    private Customer chooseCustomer()
    {
        List<Customer> customers = ms.getCustomers();
        String customersStr = "";
        int counter = 0;

        for (Customer c: customers)
        {
            customersStr += ++counter + ". " + c + "\n";
        }

        ms.sendMessage("Choose a customer: \n" + customersStr, socket);

        int chosenCustomer = ms.validateChoice(counter, socket);

        return customers.get(chosenCustomer - 1);
    }

    private void removeSentItems()
    {
        Customer customer = ms.getCustomer(currentOrder);
        customer.setSentOrder(currentOrder);
    }


    private void prepareOrder()
    {
        if (currentOrder == null)
        {
            ms.sendMessage("********************************\n" +
                    "You have to get an order first !\n********************************\n", socket);
            return;
        }
        ms.sendMessage("Finding the items for "  + currentOrder.toString().replace("\t\t", ""), socket);

        ms.sendMessage("Packing all of the items for order " +
                currentOrder.getNumberString() + "...............", socket);
        threadSleep(2000);

        ms.sendMessage("Putting the package for order " + currentOrder.getNumberString() +
                " in the cage ...............", socket);
        threadSleep(2000);

        currentOrder.setStatus("Ready for sending");
    }

    private void threadSleep(int millis)
    {
        try
        {
            Thread.sleep(millis);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private Order getOrder()
    {
        if (currentOrder == null)
        {
            return ms.getOrder();
        }

        return null;
    }
}
