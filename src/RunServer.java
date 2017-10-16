
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by gabriele on 04/10/2017.
 */
public class RunServer {

    public static ServerSocket serverSocket;
    public static Socket clientSocket;

    private static final int maxClientsCount = 100;
    private static final ClientHandler[] threads = new ClientHandler[maxClientsCount];

    private final static int PORT=7777;
    //static Socket socket = null;


    public static void main(String[] args) {

        try {

            serverSocket = new ServerSocket(PORT);
            System.out.println("SERVER is Running..");
            InetAddress testIP = InetAddress.getLocalHost();
            System.out.println(testIP);

        } catch (IOException ioe) {
            System.out.println("unable to set-up port!");
            System.exit(1);
        }


        do {
            try {

                clientSocket = serverSocket.accept();
                //System.out.println("here");
                int i= 0;
                for (i=0; i< maxClientsCount;i++) {
                    if (threads[i] == null) {
                        //System.out.println("here1");
                        threads[i] = new ClientHandler(clientSocket, threads);
                        threads[i].start();
                        break;
                    }
                }
                if (i == maxClientsCount) {
                    //System.out.println("here2");
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream());
                    output.close();
                    clientSocket.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }

        }while (true);

    }
}
