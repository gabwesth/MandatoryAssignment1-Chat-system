

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by gabriele on 04/10/2017.
 */
public class Client  implements Runnable {

    private static Socket clientSocket;
    private static PrintWriter output;
    private static Scanner input;
    private static Scanner userInput = new Scanner(System.in);
    private static Thread thread;
    private static boolean close = false;

    String IP, username;
    int host;
    String sMessage, rMessage, acceptence;


    public static void main(String[] args) {

        System.out.println("Enter IP: ");
        String IP = userInput.nextLine();
        System.out.println("Enter PORT: ");
        int PORT = Integer.parseInt(userInput.nextLine());
        System.out.println("Enter Username: ");
        String username = userInput.nextLine();
        System.out.println("Trying to Connect..");
        System.out.println();

        if (connectToServer(IP, PORT, username) == true) {
            chat(username);
        } else {
            System.out.println("NO J_OK message from SERVER");
        }

    }


    private static boolean connectToServer(String IP, int port, String username) {
        boolean ok = false;
        try {
            InetAddress inetAddress = InetAddress.getByName(IP);
            clientSocket = new Socket(inetAddress, port);

            input = new Scanner(clientSocket.getInputStream());
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            output.println("JOIN " + username + ", " + IP + ":" + port);

            String acceptence = input.nextLine();
            System.out.println(acceptence);

            if (acceptence.equals("J_OK")) {
                ok = true;
            } else {
                System.out.println("NO J_OK message from SERVER");
                ok = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    private static void chat(String username) {
        try {
            new Thread(new Client()).start();
            String msg = null;
            do  {
                msg = userInput.nextLine();
                output.println("DATA " + username + ": " + msg);

                if(msg.equals("QUIT")){
                    break;
                }
            }while(msg != null);
            output.println("QUIT");
            System.out.println("Connection Closed");

            close = true;
            output.close();
            input.close();
            clientSocket.close();


        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void run() {
        while (close==false) {
            String responseLine = input.nextLine();
            System.out.println(responseLine);
        }
    }

}