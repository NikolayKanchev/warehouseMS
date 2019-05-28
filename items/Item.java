package warehouseMS.items;
import java.io.Serializable;
import java.util.Set;

public interface Item extends Serializable, Cloneable
{
    double getPrice();
    String getDescription();
    int getQuantity();
    int getQuantityByName(String name);
    String getName();
    Set<String> getUniqueItemsNames();
    Object clone() throws CloneNotSupportedException;
}
