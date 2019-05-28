package warehouseMS.server;
import java.io.*;

public class Serializer
{
    private static String filePath = System.getProperty("user.dir") + "\\src\\warehouseMS\\server\\data.ser";

    public static synchronized void save(ManagementSystem ms)
    {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(ms);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in data.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static synchronized ManagementSystem getManagementSystem()
    {
        ManagementSystem ms = null;
        try {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ms = (ManagementSystem) in.readObject();
            in.close();
            fileIn.close();
            return ms;
        } catch (IOException i) {
            i.printStackTrace();
            return ms;
        } catch (ClassNotFoundException c) {
            System.out.println("The class not found");
            c.printStackTrace();
            return ms;
        }
    }
}
