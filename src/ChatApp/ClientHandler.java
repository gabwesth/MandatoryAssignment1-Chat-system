package ChatApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


class ClientHandler extends Thread {

    String ip, port;
    String joinM, msg, text;

    private String clientName;
    private Scanner input;
    private PrintWriter output;
    private Socket clientSocket;
    private final ClientHandler[] threads;
    private int maxClientsCount;


    public ClientHandler(Socket socket, ClientHandler[] threads) {
        clientSocket = socket;
        this.threads = threads;
        maxClientsCount = threads.length;


    }

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        ClientHandler[] threads = this.threads;

        try {
            input = new Scanner(clientSocket.getInputStream());
            output = new PrintWriter(clientSocket.getOutputStream(), true);


            String join = input.nextLine();
            System.out.println(join);
            checkJoin(join);
            newClient();
            chat();
            disconnect();

            input.close();
            output.close();
            clientSocket.close();


        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
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

    private void checkJoin(String join) {
        String[] splitArray = join.split(", ");
        String one = splitArray[0];
        String[] divideOne = one.split(" ");
        String two = splitArray[1];
        String[] divideTwo = two.split(":");

        joinM = divideOne[0];
        clientName = divideOne[1];
        ip = divideTwo[0];
        port = divideTwo[1];

        if (joinM.equals("JOIN")) {
            output.println("J_OK");
            System.out.println("new user: " + clientName);

        } else {
            output.println("J_ER MissingJoin: Wrong Protocol Statemenet");
        }
    }

    private void newClient() {
        ArrayList clientList = new ArrayList();
        synchronized (this) {
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null) {
                    clientList.add(threads[i].clientName);
                }
            }

            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    System.out.println(clientName + " entered the chat room");
                    threads[i].output.println(clientName + " entered the chat room");
                    threads[i].output.println("Active users: " + clientList.toString());
                }
                if (threads[i] == this) {
                    threads[i].output.println("Active users: " + clientList.toString());
                }
            }


        }
    }

    private void chat() {
        while (true) {
            String msg = input.nextLine();
            if (msg.equals("QUIT")) {
                break;
            }
            String[] split = msg.split(": ");
            String text = split[1];
            System.out.println(clientName + " > " + text);
            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && msg.startsWith("DATA")) {
                        threads[i].output.println("DATA " + clientName + ": " + text);

                    }
                    if (!msg.startsWith("DATA")) {
                        System.out.println("wrong protocol");
                    }
                }
            }
        }
        System.out.println(clientName + " left the chat room");
    }

    private void disconnect() {
        synchronized (this) {
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }
        }
        ArrayList clientList = new ArrayList();
        synchronized (this) {
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null) {
                    clientList.add(threads[i].clientName);
                }
            }

            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && threads[i] != this) {
                        threads[i].output.println("*** The user " + clientName + " is leaving the chat room !!! ***");
                        threads[i].output.println("Active Users: " + clientList.toString());
                    }
                }
            }

        }


    }
}