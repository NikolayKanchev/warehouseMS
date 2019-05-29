package warehouseMS;

import warehouseMS.items.*;
import warehouseMS.server.ManagementSystem;
import warehouseMS.server.Serializer;
import warehouseMS.users.Customer;
import warehouseMS.users.Manager;
import warehouseMS.users.WarehouseWorker;

public class Main
{
    public static void main(String[] args)
    {

        ManagementSystem ms = new ManagementSystem();
        ms.addUser(new Manager("Nik", "Folehaven 115 st th 2500 Valby", "27628022"));

        // region *************** items ***************
        Product tv = new Product.Builder("TV",20000.0)
                .setBrand("Samsung")
                .setDescription("The best")
                .setModel("Super flat")
                .setSection(5)
                .build();

        Product spoon = new Product.Builder("Spoon", 50.0)
                .setBrand("OOOOO")
                .build();

        Product fork = new Product.Builder("Fork", 40.0)
                .setBrand("Silver")
                .build();

        Product logo = new Product.Builder("Logo", 12.0)
                .setBrand("Company")
                .build();
        // endregion

        // region ********** Boxes of items ***********
        Box box1 = new Box();

        for (int i = 0; i < 50; i++)
        {
            box1.addItem(spoon);
            box1.addItem(fork);
            box1.addItem(logo);
        }

        Box box2 = new Box();

        for (int i = 0; i < 80; i++)
        {
            box2.addItem(logo);
        }
        // endregion

        // region ************* Customers *************
        Customer customer = new Customer("Nikolay", "Folehaven 115 st th 2500 Valby", "27628022");
        Customer customer1 = new Customer("Alex", "Folehaven 115 st th 2500 Valby", "27628022");
        Customer customer2 = new Customer("Emma", "Folehaven 115 st th 2500 Valby", "27628022");
        Customer customer3 = new Customer("Didi", "Folehaven 115 st th 2500 Valby", "27628022");

        WarehouseWorker worker = new WarehouseWorker("Tom", "Somewhere", "222221111");

        ms.addUser(customer);
        ms.addUser(customer1);
        ms.addUser(customer2);
        ms.addUser(customer3);
        ms.addUser(worker);


        customer2.addItem(tv);
        customer2.addItem(tv);
        customer2.addItem(tv);
        customer2.addItem(box1);
        customer2.addItem(box2);
        customer2.addItem(logo);
        customer2.addItem(logo);

        Order o = new Order();
        o.addItem(tv);
        o.addItem(tv);
        o.addItem(logo);
        o.addItem(logo);
        o.addItem(logo);
        o.addItem(logo);
        o.addItem(logo);
        o.addItem(logo);
        o.addItem(logo);
        o.addItem(spoon);
        o.addItem(spoon);
        o.addItem(spoon);
        o.addItem(spoon);
        o.addItem(spoon);
        o.addItem(spoon);
        o.addItem(spoon);
        o.addItem(fork);
        o.addItem(fork);
        o.addItem(fork);
        o.addItem(fork);
        o.addItem(fork);
        o.addItem(fork);
        o.addItem(fork);
        o.addItem(fork);
        o.addItem(fork);
        o.addItem(fork);
        o.addItem(fork);
        o.addItem(fork);
        customer2.addOrder(o);

        customer.addItem(logo);
        customer.addItem(logo);
        customer.addItem(logo);

        customer3.addItem(logo);
        customer3.addItem(logo);
        customer3.addItem(logo);
        customer3.addItem(logo);

        //endregion

        Serializer.save(ms);
    }
}
