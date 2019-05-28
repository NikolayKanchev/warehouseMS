package warehouseMS.items;

import java.io.Serializable;
import java.util.*;

public class Order implements Serializable, Item
{
    private static int id;
    private List<Item> items;
    private int number;
    private String status;

    public Order()
    {
        items = new ArrayList<>();
        id++;
        number = id;
        status = "Not sent";
    }

    public int getNumber()
    {
        return number;
    }

    public String getNumberString()
    {
        return String.format("%05d", number);
    }

    public void addItem(Item item)
    {
        items.add(item);
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return this.status;
    }

    public static void setID(int num)
    {
        id = num;
    }

    public List<Item> getItems()
    {
        return items;
    }

    @Override
    public String toString()
    {
        Map<String, Integer> rows = new HashMap<>();
        String rowsStr = "";
        int counter = 1;

        for (Item item: items)
        {
            System.out.println("**********************" + item);
            if (rows.containsKey(item.getName()))
            {
                int newValue = rows.get(item.getName()) + 1;
                rows.put(item.getName(), newValue);
            }else
                rows.put(item.getName(), 1);
        }

        for (String r : rows.keySet())
        {
            rowsStr += counter + ". " + r + "\t - " + rows.get(r) + "pcs.\n";
            counter++;
        }

        if (rowsStr.equals(""))
        {
            rowsStr = "The order is empty !!!\n";
        }

        return "\t\t\tOrder - " + getNumberString() +
                "\n***************************************\n" + rowsStr;
    }

    @Override
    public double getPrice()
    {
        double total = 0.0;

        for (Item i: items)
        {
            total += i.getPrice();
        }
        return total;
    }

    @Override
    public String getDescription()
    {
        return "Order : " + getNumberString() + ", total : " + getPrice();
    }

    @Override
    public int getQuantity()
    {
        return items.size();
    }

    @Override
    public int getQuantityByName(String name)
    {
        int quantity = 0;

        for (Item i: items)
        {
            if (i.getName().equals(name))
            {
                quantity += i.getQuantity();
            }
        }
        return quantity;
    }

    @Override
    public String getName()
    {
        return "Order : " + getNumberString();
    }

    @Override
    public Set<String> getUniqueItemsNames()
    {
        Set<String> names = new HashSet<>();

        for (Item i : items)
        {
            names.add(i.getName());
        }

        return names;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
