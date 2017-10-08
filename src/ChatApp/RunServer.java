package ChatApp;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by gabriele on 04/10/2017.
 */
public class RunServer {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;

    private static final int maxClientsCount = 10;
    private static final ClientHandler[] threads = new ClientHandler[maxClientsCount];
    private final static int PORT= 7777;
    //static Socket socket = null;


    public static void main(String[] args) {

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("SERVER is Running..");

        } catch (IOException ioe) {
            System.out.println("unable to set-up port!");
            System.exit(1);
        }


        do {
            try {
                clientSocket = serverSocket.accept();
                int i= 0;
                for (i=0; i< maxClientsCount;i++) {
                    if (threads[i] == null) {
                        threads[i] = new ClientHandler(clientSocket, threads);
                        threads[i].start();
                        break;
                    }
                }
                if (i == maxClientsCount) {
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream());
                    output.println("Server too busy. Try later.");
                    output.close();
                    clientSocket.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }

       }while (true);

        }
    }
