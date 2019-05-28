package warehouseMS.users;

import warehouseMS.items.Order;
import warehouseMS.items.Item;
import warehouseMS.users.behaviours.CustomerWorkBehaviour;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User
{
    private List<Item> items;
    private List<Order> orders;
    private List<Order> sentOrders;

    public Customer(String name, String address, String phone)
    {
        super(name, address, phone);
        items = new ArrayList<>();
        orders = new ArrayList<>();
        sentOrders = new ArrayList<>();
        setWorkBehaviour(new CustomerWorkBehaviour());
    }

    public void addItem(Item item)
    {
        items.add(item);
    }

    public void removeItem(Item item)
    {
        items.remove(item);
    }

    public int getNumItems()
    {
        int retVal = 0;

        ArrayList<Item> itemsList = new ArrayList<>();
        itemsList.addAll(items);
        itemsList.addAll(orders);

        for (Item i : itemsList)
        {
            retVal += i.getQuantity();
        }

        return retVal;
    }

    public Double getTotalOfAllItems()
    {
        List<Item> allItems = new ArrayList<>();
        allItems.addAll(items);
        allItems.addAll(orders);

        Double retVal = 0.0;

        for (Item i : allItems)
        {
            retVal += i.getPrice();
        }

        return retVal;
    }

    public int getItemsQuantity(String name)
    {
        int retVal = 0;

        for (Item i : items)
        {
            retVal += i.getQuantityByName(name);
        }
        return retVal;
    }

    public List<Item> getItems()
    {
        return items;
    }

    public boolean deleteOrder(String orderNum)
    {
        for(Order o: orders)
        {
            if (o.getNumberString().equals(orderNum))
            {
                items.addAll(o.getItems());
                orders.remove(o);
                return true;
            }
        }
        return false;
    }

    public void addOrder(Order order)
    {
        this.orders.add(order);
    }

    public List<Order> getOrders()
    {
        return orders;
    }

    public List<Order> getNotSentOrders()
    {
        return orders;
    }

    public void setSentOrder(Order order)
    {
        Order o;

        for (int i = 0; i < orders.size(); i++)
        {
            o = orders.get(i);

            if (o == order)
            {
                orders.remove(o);
            }
        }

        order.setStatus("Sent");
        sentOrders.add(order);
    }

    public Item getItemByName(String name)
    {
        for (Item i: items)
        {
            if (i.getName().contains(name))
            {
                return i;
            }
        }
        return null;
    }

    public List<Order> getSentOrders()
    {
        return sentOrders;
    }

    public List<Item> getNotAvailableItems()
    {
        List<Item> notAvailableItems = new ArrayList<>();

        for (Order o: orders)
        {
            notAvailableItems.addAll(o.getItems());
        }

        return notAvailableItems;
    }

    public int getNotAvailableItemsQuantity(String name)
    {
        int retVal = 0;

        for (Order o : orders)
        {
            List<Item> notAvailableItems = o.getItems();

            for (Item i: notAvailableItems)
            {
                retVal += i.getQuantityByName(name);
            }
        }
        return retVal;
    }
}
