package warehouseMS.items;

import java.util.HashSet;
import java.util.Set;

public class Product implements Item
{
    private String name;
    private String brand;
    private String model;
    private double price;
    private String description;
    private int section;
    private int rack;
    private int rackShelf;

    private Product(){}

    public static class Builder
    {
        private String name;//
        private String brand;
        private String model;
        private double price;//
        private String description;
        private int section;
        private int rack;
        private int rackShelf;


        public Builder(String name, Double price)
        {
            this.name = name;
            this.price = price;
        }

        public Builder setBrand(String brand)
        {
            this.brand = brand;
            return this;
        }

        public Builder setModel(String model)
        {
            this.model = model;
            return this;
        }

        public Builder setDescription(String description)
        {
            this.description = description;
            return this;
        }

        public Builder setRack(int rack)
        {
            this.rack = rack;
            return this;
        }

        public Builder setSection(int section)
        {
            this.section = section;
            return this;
        }

        public Builder setRackShelf(int rackShelf)
        {
            this.rackShelf = rackShelf;
            return this;
        }

        public Product build()
        {
            Product product = new Product();
            product.name = this.name;
            product.price = this.price;
            product.section = this.section;
            product.brand = this.brand;
            product.model = this.model;
            product.description = this.description;
            product.rack = this.rack;
            product.rackShelf = this.rackShelf;
            return product;
        }
    }

    public String getBrand()
    {
        return brand;
    }

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getSection()
    {
        return section;
    }

    public void setSection(int section)
    {
        this.section = section;
    }

    public int getRack()
    {
        return rack;
    }

    public void setRack(int rack)
    {
        this.rack = rack;
    }

    public int getRackShelf()
    {
        return rackShelf;
    }

    public void setRackShelf(int rackShelf)
    {
        this.rackShelf = rackShelf;
    }

    @Override
    public double getPrice()
    {
        return price;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public int getQuantity()
    {
        return 1;
    }

    @Override
    public int getQuantityByName(String name)
    {
        int retVal = 0;

        if (getName().equals(name))
        {
            retVal = 1;
        }

        return retVal;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Set<String> getUniqueItemsNames()
    {
        Set<String> set = new HashSet<>();
        set.add(this.name);
        return set;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public String toString()
    {
        return name;
    }
}
