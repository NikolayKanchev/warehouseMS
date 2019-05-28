package warehouseMS.client;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread extends Thread
{

    private Socket socket;


    public ClientThread(Socket socket) {

        this.socket = socket;

    }

    @Override
    public void run() {
        try {

            while (true) {

                Scanner scanner = new Scanner(socket.getInputStream());

                while (scanner.hasNextLine())
                {
                    String str = scanner.nextLine();

                    if (str.equals("exit"))
                    {
                        System.exit(0);
                    }

                    System.out.println(str);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

