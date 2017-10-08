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

    class ClientHandler extends Thread{


    String user, ip, port;
    String joinM,msg,text;

    private String clientName;
    private Scanner input;
    private PrintWriter output;
    private Socket clientSocket;
    private final ClientHandler[] threads;
    private int maxClientsCount;

    public ClientHandler(Socket socket,ClientHandler[] threads){
        clientSocket = socket;
        this.threads = threads;
        maxClientsCount = threads.length;


    }

    public void run(){
        int maxClientsCount = this.maxClientsCount;
        ClientHandler[] threads = this.threads;

        try{
            input = new Scanner(clientSocket.getInputStream());
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            newClient();
            String join = input.nextLine();
            System.out.println(join);
            checkJoin(join);
            chat();
            disconnect();

            input.close();
            output.close();
            clientSocket.close();


        }catch (IOException ioe){
            ioe.printStackTrace();
        }
//        String received;
//        do{
//            received = input.nextLine();
//            output.println("ECHO: " + received);
//
//        }while (!received.equals("QUIT"));
//
//        try {
//            if(client!=null){
//                System.out.println("closing down connection");
//                client.close();
//            }
//
//        }catch (IOException ioe){
//            System.out.println("unable to disconnect");
//        }

    }

        private void newClient() {

            output.println("Welcome! Enter your clientName: ");
            clientName = input.nextLine();
            output.println("Welcome " + clientName + " to our chat room!");


            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && threads[i] != this) {
                        threads[i].output.println(clientName + " entered the chat room");
                    }
                }
            }
        }
        private void checkJoin(String join){
            String[] splitArray = join.split(", ");
            String one = splitArray[0];
            String[] divideOne = one.split(" ");
            String two = splitArray[1];
            String[] divideTwo = two.split(":");

            joinM = divideOne[0];
            user = divideOne[1];
            ip = divideTwo[0];
            port = divideTwo[1];

            if(joinM.equals("JOIN")){
                output.println("J_OK");
                System.out.println("new user: " + user);

            }else{
                output.println("J_ER MissingJoin: Wrong Protocol Statemenet");
            }
        }

        private void chat(){
            while (true) {
                msg = input.nextLine();
                String[] divide = msg.split(": ");
                text = divide[1];

                if (msg.equals("QUIT")) {
                    break;
                }

                synchronized (this) {
                    for (int i = 0; i < maxClientsCount; i++) {
                        if (threads[i] != null && msg.startsWith("DATA")) {
                            System.out.println(user + " > " + text);
                            threads[i].output.println("DATA " + user + ": " + text);
                        }else{
                            System.out.println("wrong protocol");
                        }
                    }
                }
            }
        }

        private void disconnect(){
            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && threads[i] != this) {
                        threads[i].output.println("*** The user " + clientName
                                + " is leaving the chat room !!! ***");
                    }
                }
            }
            output.println("*** Bye " + clientName + " ***");
            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == this) {
                        threads[i] = null;
                    }
                }
            }
        }
}

