package warehouseMS.users.behaviours;

import warehouseMS.server.ManagementSystem;
import warehouseMS.users.User;
import warehouseMS.factories.UserFactory;
import java.net.Socket;
import java.util.List;

public class ManagerWorkBehaviour implements WorkBehaviour
{
    private ManagementSystem ms;
    private User loggedUser;

    @Override
    public void work(Socket socket, ManagementSystem ms, User user)
    {
        this.ms = ms;
        this.loggedUser = user;

        ms.sendMessage(
                "-----------------------------\n" +
                        "1. See users\n" +
                        "2. Create user\n" +
                        "3. Delete user\n" +
                        "4. Update user\n" +
                        "5. Check warehouse workers\n" +
                        "6. Save and exit\n" +
                        "-----------------------------",
                socket);

        int userChoice = ms.validateChoice(5, socket);

        switch (userChoice)
        {
            case 1:
                sendListOfUsers(socket);
                work(socket, ms, loggedUser);
                break;
            case 2:
                createUser(socket);
                work(socket, ms, loggedUser);
                break;
            case 3:
                deleteUser(socket);
                work(socket, ms, loggedUser);
                break;
            case 4:
                updateUser(socket);
                work(socket, ms, loggedUser);
                break;
            case 5:
                checkWorkers(socket);
                work(socket, ms, loggedUser);
                break;
            case 6:
                ms.saveAndExit(socket);
        }
    }

    private void checkWorkers(Socket socket)
    {
        String s = ms.getNumOfCompletedOrdersOfEachWorker();
        ms.sendMessage(s, socket);
    }

    private void updateUser(Socket socket)
    {
        int numUsers = sendListOfUsers(socket);
        ms.sendMessage("Update user ", socket);
        int userToUpdate =  ms.validateChoice(numUsers, socket);

        User user = ms.getUsers().get(userToUpdate - 1);

        ms.sendMessage(
                "Choose the field that you want to update: \n" +
                        "1. " + user.getName() + "\n" +
                        "2. " + user.getAddress() + "\n" +
                        "3. " + user.getPhone() + "\n"
                , socket);

        int fieldNum = ms.validateChoice(3, socket);

        switch (fieldNum)
        {
            case 1:
                String name =  getUserInput("Type the new name: ", socket);
                user.setName(name);
                break;
            case 2:
                String address =  getUserInput("Type the new address: ", socket);
                user.setAddress(address);
                break;
            case 3:
                String phone =  getUserInput("Type the new phone: ", socket);
                user.setPhone(phone);
                break;
        }
    }

    private void deleteUser(Socket socket)
    {
        int numUsers = sendListOfUsers(socket);
        int userToDelete =  ms.validateChoice(numUsers, socket);

        User user = ms.getUsers().get(userToDelete - 1);

        ms.removeUser(user);

    }

    private void createUser(Socket socket)
    {
        String typeOfUser;
        String name;
        String address;
        String phone;
        UserFactory uf = new UserFactory();

        while (true)
        {
            typeOfUser =  getUserInput("Which kind of user do you want to create? ", socket);
            if(ms.getTypeOfUsers().contains(typeOfUser))
            {
                break;
            }else {
                ms.sendMessage("Wrong type of user !!!", socket);
            }
        }

        name =  getUserInput("Name: ", socket);
        address =  getUserInput("Address: ", socket);
        phone =  getUserInput("Phone: ", socket);

        User user = uf.create(typeOfUser, name, address, phone);
        ms.addUser(user);
        work(socket, ms, loggedUser);
    }

    private String getUserInput(String message, Socket socket)
    {
        String retVal = "";
        ms.sendMessage(
                message, socket);

        while (retVal.equals(""))
        {
            retVal = ms.getReceivedString(socket);
        }

        return retVal;
    }

    private int sendListOfUsers(Socket socket)
    {
        List<User> users = ms.getUsers();
        String listUsers = "Users list: ";
        int counter = 0;

        for (User u: users)
        {
            listUsers += "\n" + ++counter + ". " + u.getName() + " - " + u.getClass().getSimpleName();
        }

        ms.sendMessage(listUsers + "\n", socket);

        return counter;
    }
}
