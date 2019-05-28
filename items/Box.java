package warehouseMS.items;
import java.util.*;

public class Box implements Item
{
    private List<Item> items;

    public Box()
    {
        items = new ArrayList<>();
    }

    public void removeItem(Item item)
    {
        if (item instanceof Box)
        {
            removeItem(item);
        }else
        {
            items.remove(item);
        }
    }

    public void addItem(Item item)
    {
        items.add(item);
    }

    public String getName()
    {
        String retVal = "";

        for (Item i: items)
        {
            if (retVal.equals(""))
            {
                retVal += i.getName();
            }else
            {
                retVal += ", " + i.getName();
            }
        }

        return retVal;
    }

    public Item getItemByName(String name)
    {
        for (Item i: items)
        {
            if (i instanceof Box)
            {
                return ((Box)i).getItemByName(name);
            }else
            {
                if (i.getName().equals(name))
                {
                    return i;
                }
            }
        }
        return null;
    }

    @Override
    public double getPrice()
    {
        double price = 0;

        for (Item i : items)
        {
            price += i.getPrice();
        }
        return price;
    }

    @Override
    public String getDescription()
    {
        String itemTypes = "";
        String itemsDescriptions = "";
        Set<String> descriptions = new HashSet<>();

        for (Item i : items)
        {
            String itemType = i.getClass().getSimpleName();

            descriptions.add(i.getDescription());

            if (!itemTypes.contains(itemType))
            {
                if (itemTypes.equals(""))
                {
                    itemTypes += itemType;

                }else {

                    itemTypes += ", " + itemType;
                }
            }
        }
        return "Box of " + getQuantity() +
                " items: " + itemsDescriptions + " " + descriptions;
    }

    @Override
    public int getQuantity()
    {
        int retVal = 0;

        for (Item i : items)
        {
            retVal += i.getQuantity();
        }
        return retVal;
    }

    @Override
    public int getQuantityByName(String name)
    {
        int retVal = 0;

        for (Item i : items)
        {
            if (i.getName().contains(name))
            {
                retVal += i.getQuantityByName(name);
            }
        }

        return retVal;
    }

    @Override
    public Set<String> getUniqueItemsNames()
    {
        Set<String> names = new HashSet<>();

        for (Item i : items)
        {
            if (i instanceof Box)
            {
                names.addAll( i.getUniqueItemsNames());
            }else
            {
                names.add(i.getName());
            }
        }

        return names;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
