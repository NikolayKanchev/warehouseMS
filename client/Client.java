package warehouseMS.client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    public static void main(String[] args)
    {
        try
        {
            Socket socket = new Socket("127.0.0.1", 5555);

            new ClientThread(socket).start();

            Thread.currentThread();

            Thread.sleep(1000);

            while (true)
            {
                Scanner input = new Scanner(System.in);

                String string = input.nextLine();

                PrintStream printStream = new PrintStream(socket.getOutputStream());

                printStream.println(string);
            }

        } catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }

    }
}


