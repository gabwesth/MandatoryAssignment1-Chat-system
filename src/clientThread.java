import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


class clientThread extends Thread {

    private String clientName;
    private Scanner input;
    private PrintStream output;;
    private Socket clientSocket;
    private final clientThread[] threads;
    private int maxClientsCount;

    public clientThread(Socket clientSocket, clientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;

        try {
            input = new Scanner(clientSocket.getInputStream());
            output = new PrintStream(clientSocket.getOutputStream());
            newClient();
            chat();
            disconnect();

            input.close();
            output.close();
            clientSocket.close();

        } catch (IOException e) {
        }
    }

    private void newClient(){

        output.println("Welcome! Enter your clientName: ");
        clientName = input.nextLine();
        output.println("Welcome "+clientName+" to our chat room!");


        synchronized (this) {
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].output.println(clientName + " entered the chat room");
                }
            }
        }

    }

    private void chat(){
        while (true) {
            String line = input.nextLine();
            if (line.equals("QUIT")) {
                break;
            }
            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null) {
                        threads[i].output.println("<" + clientName + "> " + line);
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
